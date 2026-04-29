import com.google.gson.Gson;
import Classes.MensajeRed;
import java.io.*;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

public class GestorCliente implements Runnable {
    private final Socket socket;
    private PrintWriter salida;

    public GestorCliente(Socket socket) {
        this.socket = socket;
    }

    private static final Logger logger = Logger.getLogger(GestorCliente.class.getName());

    @Override
    public void run() {
        GestorConexiones.agregarCliente(this);

        try (
                BufferedReader br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        ) {
            salida = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()), true);
            String nuevo;

            Gson gson = new Gson();

            while ((nuevo = br.readLine()) != null) {
                System.out.println("mensaje de " + socket.getInetAddress() + ": " + nuevo);

                MensajeRed mensaje = gson.fromJson(nuevo, MensajeRed.class);

                if (mensaje != null && mensaje.getAccion() != null) {

                    if (mensaje.getAccion().equals("PEDIR_TURNO")) {
                        System.out.println("el paciente " + mensaje.getNombre() + " ha pedido turno");

                        BBDD bd = new BBDD();
                        String nuevoTicket = bd.registrarNuevoTurno(mensaje);

                        SincronizadorOdoo.enviarCliente(nuevoTicket, mensaje.getNombre());

                        MensajeRed respuesta = new MensajeRed("TURNO_ASIGNADO", "SERVIDOR", nuevoTicket, "");
                        String jsonRespuesta = gson.toJson(respuesta);

                        enviarMensaje(jsonRespuesta);

                    } else if (mensaje.getAccion().equals("LLAMAR_PACIENTE")) {
                        System.out.println("operario llama al ticket: " + mensaje.getTicket());

                        MensajeRed aviso = new MensajeRed("ACTUALIZAR_PANTALLA", "SERVIDOR", mensaje.getTicket(), mensaje.getMesa());
                        String jsonAviso = gson.toJson(aviso);
                        GestorConexiones.enviarATodos(jsonAviso);
                    }
                }
            }
        } catch (IOException e) {
            System.out.println("cliente desconectado: " + socket.getInetAddress());
        } catch (Exception e) {
            logger.log(Level.SEVERE, "error en hilo " + Thread.currentThread().getName(), e);
        } finally {
            GestorConexiones.eliminarCliente(this);
            try {
                socket.close();
            } catch (IOException ignored) {}
        }
    }

    public void enviarMensaje(String msg) {
        if (salida != null) {
            salida.println(msg);
        }
    }
}