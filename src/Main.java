import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Main {
    private static final Logger logger = Logger.getLogger(Main.class.getName());

    public static void main(String[] args) {


        Credenciales credenciales = new Credenciales();

        try {
            credenciales.inicializar();
        } catch (IOException e) {
            System.out.println("error al cargar credenciales, deteniendo servidor");
            return;
        }

        int puerto = credenciales.getPORT_SERVER();
        System.out.println("iniciando servidor de turnos...");

        try (ServerSocket serverSocket = new ServerSocket(puerto)) {
            System.out.println("servidor escuchando en el puerto " + puerto);

            while (true) {
                Socket socketCliente = serverSocket.accept();
                System.out.println("nuevo cliente conectado: " + socketCliente.getInetAddress());

                Thread hilo = new Thread(new GestorCliente(socketCliente));
                hilo.start();
            }
        } catch (IOException e) {
            System.out.println("error al arrancar el servidor: " + e.getMessage());
            logger.log(Level.SEVERE, "exception en main: " + e.getMessage() + ".", e);
        }
    }
}