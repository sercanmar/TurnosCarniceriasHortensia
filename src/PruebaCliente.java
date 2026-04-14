import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class PruebaCliente {
    public static void main(String[] args) {
        try {
            Socket socket = new Socket("localhost", 8080);
            PrintWriter salida = new PrintWriter(socket.getOutputStream(), true);
            BufferedReader entrada = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            salida.println("{\"accion\":\"PEDIR_TURNO\",\"rol\":\"USUARIO\"}");

            String respuesta = entrada.readLine();
            System.out.println("el servidor me ha respondido: " + respuesta);

            socket.close();
        } catch (Exception e) {
            System.out.println("ha fallado: " + e.getMessage());
        }
    }
}