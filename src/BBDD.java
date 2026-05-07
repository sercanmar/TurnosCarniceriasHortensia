import Classes.MensajeRed;
import java.sql.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class BBDD {
    private static final Logger logger = Logger.getLogger(BBDD.class.getName());

    public String registrarNuevoTurno (MensajeRed mensaje) {
        Credenciales cre = new Credenciales();
        try {
            cre.inicializar();
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Exception in main: "+e.getMessage()+".", e);
        }

        String user = cre.getUSER_DATABASE();
        String password = cre.getPASS_DATABASE();
        String url = cre.getURL_DATABASE();

        try (Connection conexion = DriverManager.getConnection(url, user, password)) {
            Statement statement = conexion.createStatement();
            {
                String script = "INSERT INTO turnos (rol, accion) VALUES (?, ?)";

                try (PreparedStatement ps = conexion.prepareStatement(script, Statement.RETURN_GENERATED_KEYS)) {
                    ps.setString(1, mensaje.getRol());
                    ps.setString(2, mensaje.getAccion());

                    ps.executeUpdate();
                    System.out.println("datos guardados correctamente.");

                    // saco el idgenerado de la bd
                    ResultSet rs = ps.getGeneratedKeys();
                    rs.next();
                    return "T-" + rs.getInt(1);
                }
            }
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Exception in thread "+Thread.currentThread().getName()+": "+e.getMessage()+".", e);
        }
        return "NO_RECIBIDO";
    }
}