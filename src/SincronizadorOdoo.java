import org.apache.xmlrpc.client.XmlRpcClient;
import org.apache.xmlrpc.client.XmlRpcClientConfigImpl;
import java.net.URL;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class SincronizadorOdoo {

    private static final String URL_ODOO = "http://localhost:8069";
    private static final String BD_ODOO = "ClinicaAzucena";
    private static final String USUARIO = "sergiocanomarquez@gmail.com";
    private static final String PASSWORD = "12345";

    public static void enviarCliente(String nombreTicket, String nombreReal) {
        try {
            XmlRpcClientConfigImpl config = new XmlRpcClientConfigImpl();
            config.setServerURL(new URL(URL_ODOO + "/xmlrpc/2/common"));

            XmlRpcClient client = new XmlRpcClient();
            client.setConfig(config);

            Object uidObj = client.execute("authenticate", Arrays.asList(
                    BD_ODOO, USUARIO, PASSWORD, Collections.emptyMap()
            ));

            if (uidObj instanceof Boolean) {
                System.out.println("cuidado: el usuario o la contraseña de odoo son incorrectos");
                return;
            }
            int uid = (Integer) uidObj;

            config.setServerURL(new URL(URL_ODOO + "/xmlrpc/2/object"));

            Map<String, Object> datosCliente = new HashMap<>();
            datosCliente.put("name", nombreReal + " (" + nombreTicket + ")");

            Object idPartnerObj = client.execute("execute_kw", Arrays.asList(
                    BD_ODOO, uid, PASSWORD,
                    "res.partner", "create",
                    Collections.singletonList(datosCliente)
            ));

            int idPartner = (Integer) idPartnerObj;

            Map<String, Object> datosCita = new HashMap<>();
            datosCita.put("name", "Turno " + nombreTicket);
            datosCita.put("partner_id", idPartner);
            datosCita.put("description", "el paciente ha completado su turno");

            client.execute("execute_kw", Arrays.asList(
                    BD_ODOO, uid, PASSWORD,
                    "crm.lead", "create",
                    Collections.singletonList(datosCita)
            ));

            System.out.println("el paciente " + nombreReal + " y su atencion se han guardado bien en el crm");

        } catch (Exception e) {
            System.out.println("fallo la conexion con odoo: " + e.getMessage());
        }
    }
}