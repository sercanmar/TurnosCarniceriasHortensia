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

        int puertoTCP = credenciales.getPORT_SERVER();
        int puertoWeb = 8081;
        ServidorWeb.iniciar(puertoWeb);

        System.out.println("iniciando servidor tcp de turnos...");
        Thread hiloOperario = new Thread(() -> {
            java.util.Scanner teclado = new java.util.Scanner(System.in);
            System.out.println("\n--------------------------------------------------");
            System.out.println("PANEL DE OPERARIO LISTO");
            System.out.println("escribe 's' y pulsa enter para llamar al siguiente");
            System.out.println("--------------------------------------------------\n");
            
            while (true) {
                String letra = teclado.nextLine();
                
                // si el operario escribe s o S, pasamos turno
                if (letra.equalsIgnoreCase("s")) {
                    ServidorWeb.avanzarTurno();
                }
            }
        });
        hiloOperario.start();
      try (ServerSocket serverSocket = new ServerSocket(puertoTCP)) {
            System.out.println("servidor escuchando en el puerto " + puertoTCP);

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