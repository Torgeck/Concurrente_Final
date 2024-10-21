public class Pasajero implements Runnable {

    private static int ID = 0;
    private int idPasajero;
    private Reserva reserva;
    private Aeropuerto aeropuerto;
    private Tren tren;

    public Pasajero(Aeropuerto aeropuerto, String nombreEmpresa) {
        this.idPasajero = generarID();
        this.aeropuerto = aeropuerto;
        this.reserva = new Reserva(nombreEmpresa);
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

    public Tren getTren() {
        return tren;
    }

    public void setTren(Tren tren) {
        this.tren = tren;
    }

    public void run() {
        PuestoAtencion atencion = null;
        // El aeropuerto se encarga de producir pasajeros de tal a cierta hora
        try {
            // Se dirige al puesto de informes y obtiene el puesto de atencion
            atencion = aeropuerto.getPuestoInformes().obtenerPuestoAtencion(reserva.getEmpresa());
            System.out.println(
                    "El pasajero " + this.idPasajero + " se dirige al puesto de atencion [" + atencion.getIdPuesto()
                            + "]");
            Thread.sleep(5000);
        } catch (Exception e) {
            System.out.println("Le exploto la reserva en la cara a pasajero " + this.idPasajero);
        }

        // Si no hay lugar en la cola entra a hall a esperar
        while (!atencion.entrarCola(this)) {
            try {
                System.out.println("Pasajero " + this.idPasajero + "Esperando en hall");
                aeropuerto.getHall().esperarEnHall();
            } catch (Exception e) {
                System.out.println("ERROR al esperar en hall");
            }
        }
        atencion.esperarAtencion(this);
        atencion.salirPuestoAtencion(this);

        System.out.println("Pasajero " + this.idPasajero + " murio");
        // Luego se va al cole a hacer sus cosas
    }
}
