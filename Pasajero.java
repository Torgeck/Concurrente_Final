public class Pasajero implements Runnable {

    private static int ID = 0;
    private int idPasajero;
    private Reserva reserva;

    // Objetos pasivos
    private Aeropuerto aeropuerto;
    private PuestoInformes informes;
    private PuestoAtencion atencion;
    private Tren tren;

    public Pasajero(Aeropuerto aeropuerto, Reserva reserva) {
        this.idPasajero = generarID();
        this.aeropuerto = aeropuerto;
        this.reserva = reserva;
    }

    private int generarID() {
        return ++ID;
    }

    public int getIdPasajero() {
        return idPasajero;
    }

    public Reserva getReserva() {
        return reserva;
    }

    public void setReserva(Reserva reserva) {
        this.reserva = reserva;
    }

    public Aeropuerto getAeropuerto() {
        return aeropuerto;
    }

    public void setAeropuerto(Aeropuerto aeropuerto) {
        this.aeropuerto = aeropuerto;
    }

    public PuestoInformes getInformes() {
        return informes;
    }

    public void setInformes(PuestoInformes informes) {
        this.informes = informes;
    }

    public PuestoAtencion getAtencion() {
        return atencion;
    }

    public void setAtencion(PuestoAtencion atencion) {
        this.atencion = atencion;
    }

    public Tren getTren() {
        return tren;
    }

    public void setTren(Tren tren) {
        this.tren = tren;
    }

    public void run() {

        // El aeropuerto se encarga de producir pasajeros de tal a cierta hora
        try {
            // Se dirige al puesto de informes y obtiene el puesto de atencion
            atencion = informes.obtenerPuestoAtencion(reserva.getEmpresa());
            Thread.sleep(1000);
        } catch (Exception e) {
            System.out.println("Le exploto la reserva en la cara a pasajero " + this.idPasajero);
        }

        System.out.println(
                "El pasajero " + this.idPasajero + " se dirige al puesto de atencion [" + this.atencion.toString()
                        + "]");
        try {
            atencion.serAtendido();

        } catch (Exception e) {

        }

    }
}
