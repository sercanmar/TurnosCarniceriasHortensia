import java.util.ArrayList;
import java.util.List;

public class GestorConexiones {

    private static final List<GestorCliente> clientesActivos = new ArrayList<>();

    public static synchronized void agregarCliente(GestorCliente c) {
        clientesActivos.add(c);
    }

    public static synchronized void eliminarCliente(GestorCliente c) {
        clientesActivos.remove(c);
    }


    public static synchronized void enviarATodos(String mensaje) {
        for (GestorCliente c : clientesActivos) {
            c.enviarMensaje(mensaje);
        }
    }
}