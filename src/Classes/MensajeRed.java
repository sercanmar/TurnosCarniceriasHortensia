package Classes;

public class MensajeRed {
    private String accion;
    private String rol;
    private String ticket;
    private String mesa;

    private String nombre;

    public MensajeRed(String accion, String rol, String ticket, String mesa) {
        this.accion = accion;
        this.rol = rol;
        this.ticket = ticket;
        this.mesa = mesa;
    }

    public MensajeRed(String accion, String rol, String ticket, String mesa, String nombre) {
        this.accion = accion;
        this.rol = rol;
        this.ticket = ticket;
        this.mesa = mesa;
        this.nombre = nombre;
    }

    public String getAccion() { return accion; }
    public String getRol() { return rol; }
    public String getTicket() { return ticket; }
    public String getMesa() { return mesa; }

    public String getNombre() { return nombre; }
}