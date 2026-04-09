import java.io.*;
import java.util.Properties;

public class Credenciales {
    private String URL_DATABASE;
    private String USER_DATABASE;
    private String PASS_DATABASE;
    private int PORT_SERVER;

    public Credenciales() {
    }

    public void inicializar() throws IOException {
        Properties propiedades = new Properties();

        try (FileInputStream fis = new FileInputStream("server.properties")) {

            propiedades.load(fis);

            this.URL_DATABASE = propiedades.getProperty("db.url");
            this.USER_DATABASE = propiedades.getProperty("db.user");
            this.PASS_DATABASE = propiedades.getProperty("db.pass");

            String portStr = propiedades.getProperty("server.port");
            if (portStr != null) {
                this.PORT_SERVER = Integer.parseInt(portStr);
            }

            System.out.println("configuración cargada con éxito en " + Thread.currentThread().getName());

        } catch (FileNotFoundException e) {
            System.err.println("error: no se encontró el archivo server.properties");
            throw e;
        } catch (NumberFormatException e) {
            System.err.println("error: el puerto en el archivo no es un número válido.");
            throw e;
        }
    }

    public String getURL_DATABASE() {
        return URL_DATABASE;
    }

    public String getUSER_DATABASE() {
        return USER_DATABASE;
    }

    public String getPASS_DATABASE() {
        return PASS_DATABASE;
    }

    public int getPORT_SERVER() {
        return PORT_SERVER;
    }
}