public class Empleado implements Runnable {

    private static int ID = 0;
    private int idEmpleado;
    private PuestoAtencion atencion;

    public Empleado(PuestoAtencion atencion) {
        this.idEmpleado = generarID();
    }

    private int generarID() {
        // Genera un ID unico para una instancia
        return ++ID;
    }

    public int getIdEmpleado() {
        return idEmpleado;
    }

    public PuestoAtencion getAtencion() {
        return atencion;
    }

}
