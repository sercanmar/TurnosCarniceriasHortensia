import org.apache.xmlrpc.client.XmlRpcClient;
import org.apache.xmlrpc.client.XmlRpcClientConfigImpl;
import java.net.URL;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class SincronizadorOdoo {

    // pongo aqui los datos de mi odoo en docker para no repetirlos luego
    private static final String URL_ODOO = "http://localhost:8069";
    private static final String BD_ODOO = "CarnceriasHortensia";
    private static final String USUARIO = "Administrator";
    private static final String PASSWORD = "12345";

    // esta funcion la llamo desde el gestorcliente cuando se crea un turno nuevo
    public static void enviarCliente(String nombreTicket) {
        try {
            // usamos la libreria que me he bajado para no hacer el http a mano
            XmlRpcClientConfigImpl config = new XmlRpcClientConfigImpl();
            config.setServerURL(new URL(URL_ODOO + "/xmlrpc/2/common"));

            XmlRpcClient client = new XmlRpcClient();
            client.setConfig(config);

            // paso 1: hay que loguearse para que odoo nos deje entrar
            // le pasamos la base de datos, el usuario y la clave
            Object uidObj = client.execute("authenticate", Arrays.asList(
                    BD_ODOO, USUARIO, PASSWORD, Collections.emptyMap()
            ));

            // si la clave esta mal, devuelve un false y nos salimos para que el programa no pete
            if (uidObj instanceof Boolean) return;

            // guardamos el numero de sesion que nos da odoo al entrar (el uid)
            int uid = (Integer) uidObj;

            // paso 2: cambiamos la ruta para poder guardar cosas nuevas
            config.setServerURL(new URL(URL_ODOO + "/xmlrpc/2/object"));

            // creo un mapa que es como guardar variables juntas para mandar el nombre del paciente
            Map<String, Object> datosCliente = new HashMap<>();
            datosCliente.put("name", "Paciente " + nombreTicket);

            // paso 3: le decimos a odoo que cree el contacto en su base de datos
            // los contactos en odoo se llaman res.partner por dentro
            client.execute("execute_kw", Arrays.asList(
                    BD_ODOO, uid, PASSWORD,
                    "res.partner", "create",
                    Collections.singletonList(datosCliente)
            ));

            System.out.println("el paciente se ha guardado bien en odoo");

        } catch (Exception e) {
            // si falla el internet o el contenedor esta apagado, lo saco por pantalla
            System.out.println("fallo la conexion con odoo: " + e.getMessage());
        }
    }
}