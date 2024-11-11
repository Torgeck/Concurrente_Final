package hilos;

import console.Console;
import pasivos.*;

import java.util.Random;

public class Pasajero implements Runnable {

    private static int ID = 0;
    private int idPasajero;
    private Reserva reserva;
    private Aeropuerto aeropuerto;
    private Random random;
    private Terminal terminal;
    private Freeshop freeshop;

    public Pasajero(Aeropuerto aeropuerto, String nombreEmpresa) {
        this.idPasajero = generarID();
        this.aeropuerto = aeropuerto;
        this.reserva = new Reserva(nombreEmpresa);
        this.random = new Random();
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
        // TODO Consultar
        this.reserva = atencion.esperarAtencion();
        this.terminal = this.reserva.getTerminal();
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

            aeropuerto.getTren().subirTren(this.idPasajero, reserva.getTerminal().getId());
            aeropuerto.getTren().esperarEnTren(this.idPasajero);
            aeropuerto.getTren().bajarTren(this.idPasajero, reserva.getTerminal().getId());

            System.out.println(Console.colorString("GREEN", "Pasajero " + this.idPasajero + " entra a la terminal" + this.terminal.getId()));
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                System.out.println(Console.colorString("RED", "ERROR con pasajero al entrar en la terminal"));
            }

            if (irFreeshop()) {
                boolean compra;
                this.freeshop = terminal.getFreeshop();
                System.out.println(Console.colorString("GREEN", "Pasajero " + this.idPasajero + " quiere entrar al freeshop"));
                compra = this.freeshop.ingresarFreeshop();
                System.out.println(Console.colorString("GREEN", "Pasajero " + this.idPasajero + " entro al freeshop"));

                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    System.out.println(Console.colorString("RED", "ERROR con pasajero al comprar en freeshop"));
                }

                if (compra) {
                    System.out.println(Console.colorString("GREEN", "Pasajero " + this.idPasajero + " decidio hacer compras en el freeshop"));
                    Caja caja = this.freeshop.getCajaMenosOcupada();
                    caja.esperarAtencion(String.valueOf(this.idPasajero));
                    caja.salirCaja(String.valueOf(this.idPasajero));
                    System.out.println(Console.colorString("GREEN", "Pasajero saliendo de la caja"));
                }
                System.out.println(Console.colorString("GREEN", "Pasajero " + this.idPasajero + " saliendo del freeshop"));
                this.freeshop.salirFreeshop();
            }
            // TODO que se fije si se le paso el vuelo o no
            System.out.println(Console.colorString("GREEN", "Pasajero " + this.idPasajero + " esperando llamado para su vuelo"));
            this.terminal.esperarLlamado();
            System.out.println(Console.colorString("GREEN", "Pasajero " + this.idPasajero + " embarco"));
        }
    }

    private boolean irFreeshop() {
        // Se fija si tiene tiempo, caso positivo tira un dado
        // if(tieneTiempo) { hace el random }
        return random.nextBoolean();
    }
}
