import com.google.gson.Gson;
import Classes.MensajeRed;
import com.sun.net.httpserver.HttpServer;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpExchange;
import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ServidorWeb {
    
    private static final Logger logger = Logger.getLogger(ServidorWeb.class.getName());
    private static int contadorTurnos = 0;
    private static String turnoActual = "T-0";

    public static void iniciar(int puerto) {
        try {
            HttpServer server = HttpServer.create(new InetSocketAddress(puerto), 0);
            
            server.createContext("/api", new HttpHandler() {
                @Override
                public void handle(HttpExchange exchange) throws IOException {
                    // permisos cors chrome
                    exchange.getResponseHeaders().add("Access-Control-Allow-Origin", "*");
                    exchange.getResponseHeaders().add("Access-Control-Allow-Headers", "Content-Type");
                    exchange.getResponseHeaders().add("Access-Control-Allow-Methods", "GET, POST, OPTIONS");

                    if (exchange.getRequestMethod().equals("OPTIONS")) {
                        exchange.sendResponseHeaders(204, -1);
                        return;
                    }

                    if (exchange.getRequestMethod().equals("GET")) {
                        String json = "{\"ticket\":\"" + turnoActual + "\"}";
                        enviarMensaje(exchange, json);
                        
                    } else if (exchange.getRequestMethod().equals("POST")) {
                        // esto lo dejamos por si acaso, pero ya no lo usa la web
                        avanzarTurno();
                        enviarMensaje(exchange, "{\"estado\":\"ok\"}");
                    }
                }
            });
            
            server.setExecutor(null);
            server.start();
            System.out.println("servidor web http escuchando en el puerto " + puerto);
            
        } catch (Exception e) {
            logger.log(Level.SEVERE, "error al arrancar servidor web", e);
        }
    }

    
    public static void avanzarTurno() {
        contadorTurnos++;
        turnoActual = "T-" + contadorTurnos;
        System.out.println("\n>>> has llamado al siguiente: " + turnoActual + " <<<");

        try {
            Gson gson = new Gson();
            MensajeRed aviso = new MensajeRed("ACTUALIZAR_PANTALLA", "SERVIDOR", turnoActual, "MOSTRADOR 1");
            String jsonAviso = gson.toJson(aviso);
            
    
            GestorConexiones.enviarATodos(jsonAviso);
        } catch (Exception e) {
            System.out.println("error al avisar a los moviles");
        }
    }

    public static void enviarMensaje(HttpExchange exchange, String msg) {
        try {
            byte[] bytes = msg.getBytes();
            exchange.sendResponseHeaders(200, bytes.length);
            OutputStream os = exchange.getResponseBody();
            os.write(bytes);
            os.close();
        } catch (IOException e) {
            System.out.println("ha fallado al enviar a la web: " + e.getMessage());
        }
    }
}