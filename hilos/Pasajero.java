package hilos;

import console.Console;
import pasivos.Aeropuerto;
import pasivos.PuestoAtencion;
import pasivos.Reserva;
import pasivos.Tren;

public class Pasajero implements Runnable {

    private static int ID = 0;
    private int idPasajero;
    private Reserva reserva;
    private Aeropuerto aeropuerto;

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

    public void run() {
        PuestoAtencion atencion = null;
        // El aeropuerto se encarga de producir pasajeros de tal a cierta hora
        try {
            // Se dirige al puesto de informes y obtiene el puesto de atencion
            atencion = aeropuerto.getPuestoInformes().obtenerPuestoAtencion(reserva.getEmpresa());
            System.out.println(Console.colorString("GREEN",
                    "El pasajero " + this.idPasajero + " se dirige al puesto de atencion [" + atencion.getAerolinea()
                            + "]"));
            Thread.sleep(5000);
        } catch (Exception e) {
            System.out.println("Le exploto la reserva en la cara a pasajero " + this.idPasajero);
        }

        // Si no hay lugar en la cola entra a hall a esperar
        // TODO solucionar de algun modo el nullPointerException de atencion
        while (!atencion.entrarCola(this)) {
            try {
                System.out.println(Console.colorString("WHITE", "Pasajero " + this.idPasajero + " esperando en hall \uD83D\uDCA4\uD83D\uDCA4"));
                aeropuerto.getHall().esperarEnHall();
            } catch (Exception e) {
                System.out.println("ERROR al esperar en hall");
            }
        }
        this.reserva = atencion.esperarAtencion();
        atencion.salirPuestoAtencion(this);

        if (this.reserva == null) {
            System.out.println(Console.colorString("BLACK", "\uD83D\uDC80\uD83D\uDC80 Me voy del aeropuerto, no quedan vuelos \uD83D\uDC80\uD83D\uDC80"));
        } else {
            System.out.println("Pasajero " + this.idPasajero + " se dirije al tren");
            try {
                Thread.sleep(5000);
            } catch (Exception e) {
                System.out.println("ERROR al ir al tren");
            }

            aeropuerto.getTren().subirTren(this.idPasajero, reserva.getTerminal());
            aeropuerto.getTren().esperarEnTren(this.idPasajero);
            aeropuerto.getTren().bajarTren(this.idPasajero, reserva.getTerminal());

        }
    }
}
