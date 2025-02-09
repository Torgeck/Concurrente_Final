package hilos;

import console.Console;
import customExceptions.AeropuertoCerradoException;
import pasivos.*;

import java.util.Random;

public class Pasajero implements Runnable {

    private static final int ESPERA_MILLIS = 2500;
    private static final int MINUTOS_MIN = 30;
    private static int ID = 0;
    private int idPasajero;
    private Reserva reserva;
    private Aeropuerto aeropuerto;
    private Random random;
    private Terminal terminal;

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

    public Aeropuerto getAeropuerto() {
        return aeropuerto;
    }

    public void setAeropuerto(Aeropuerto aeropuerto) {
        this.aeropuerto = aeropuerto;
    }

    private void irTerminal() throws AeropuertoCerradoException {
        this.aeropuerto.verificarAbierto();
        System.out.println(Console.colorString("GREEN",
                "Pasajero " + this.idPasajero + " entra a la terminal " + this.terminal.getId()));
        try {
            Thread.sleep(ESPERA_MILLIS);
        } catch (InterruptedException e) {
            System.out.println(Console.colorString("RED", "ERROR con pasajero al entrar en la terminal"));
        }
        // Pasajero decide si ir al freeshop
        irFreeshop();
        // Pasajero se dirije puerta de embarque
        irPuertaEmbarque();
    }

    private void irFreeshop() throws AeropuertoCerradoException {

        this.aeropuerto.verificarAbierto();

        if (visitaFreeshop()) {
            Freeshop freeshop = terminal.getFreeshop();
            System.out.println(
                    Console.colorString("GREEN", "Pasajero " + this.idPasajero + " quiere entrar al freeshop"));
            boolean compra = freeshop.ingresarFreeshop();
            System.out.println(Console.colorString("GREEN", "Pasajero " + this.idPasajero + " entro al freeshop \uD83C\uDD93\uD83C\uDD93"));

            // Simula pasear en el freeshop
            try {
                Thread.sleep(ESPERA_MILLIS);
            } catch (InterruptedException e) {
                System.out.println(Console.colorString("RED", "ERROR con pasajero al comprar en freeshop"));
            }
            // Si el pasajero decidio comprar entonces entra a alguna caja
            if (compra) {
                System.out.println(Console.colorString("GREEN",
                        "Pasajero " + this.idPasajero + " decidio hacer compras en el freeshop \uD83D\uDECD\uD83D\uDECD"));
                Caja caja = freeshop.getCajaMenosOcupada();
                caja.esperarAtencion(String.valueOf(this.idPasajero));
                caja.salirCaja(String.valueOf(this.idPasajero));
                System.out.println(Console.colorString("GREEN", "Pasajero saliendo de la caja"));
            }
            System.out.println(Console.colorString("GREEN", "Pasajero " + this.idPasajero + " saliendo del freeshop"));
            freeshop.salirFreeshop();
        }
    }

    private boolean visitaFreeshop() {
        // Se fija si tiene tiempo, caso positivo tira un dado
        int tiempoRestante = this.aeropuerto.getReloj().getTiempoRestante(this.reserva.getHoraEmbarque());
        boolean respuesta = false;
        if (tiempoRestante >= MINUTOS_MIN) {
            respuesta = random.nextBoolean();
        }
        return respuesta;
    }

    private void irPuertaEmbarque() throws AeropuertoCerradoException {
        boolean embarco;
        this.aeropuerto.verificarAbierto();
        if (!perdioVuelo()) {
            System.out.println(
                    Console.colorString("WHITE", "Pasajero " + this.idPasajero + " esperando llamado para su vuelo \uD83D\uDCA4\uD83D\uDCA4"));
            embarco = this.reserva.getPuertaEmbarque().esperarEmbarque(this.reserva);
            if (embarco) {
                System.out.println(Console.colorString("GREEN", "\uD83D\uDEEB\uD83D\uDEEB Pasajero " + this.idPasajero + " embarco vuelo: ["
                        + this.reserva.getVueloID() + " | " + this.reserva.getHoraEmbarque() + "] \uD83D\uDEEB\uD83D\uDEEB"));
            } else {
                System.out.println(Console.colorString("RED",
                        "\uD83E\uDD21\uD83E\uDD21 Pasajero " + this.idPasajero + " perdio su vuelo: ["
                                + this.reserva.getVueloID() + " | " + this.reserva.getHoraEmbarque() + "] HORA ACTUAL: " + this.aeropuerto.getHora()));
            }
        } else {
            System.out.println(Console.colorString("RED",
                    "\uD83E\uDD21\uD83E\uDD21 Pasajero " + this.idPasajero + " perdio su vuelo: ["
                            + this.reserva.getVueloID() + " | " + this.reserva.getHoraEmbarque() + "]"));
        }
    }

    private boolean perdioVuelo() {
        return this.aeropuerto.getHora() > this.reserva.getHoraEmbarque();
    }

    private void irTren() throws AeropuertoCerradoException {

        this.terminal = this.reserva.getTerminal();
        System.out.println("Pasajero " + this.idPasajero + " se dirije al tren");

        try {
            Thread.sleep(ESPERA_MILLIS);
        } catch (Exception e) {
            System.out.println("ERROR al ir al tren");
        }

        this.aeropuerto.getTren().subirTren(this.idPasajero, reserva.getTerminal().getId());
        this.aeropuerto.getTren().esperarEnTren(this.idPasajero);
        this.aeropuerto.getTren().bajarTren(this.idPasajero, reserva.getTerminal().getId());
    }

    private void irPuestoAtencion(PuestoAtencion atencion) throws AeropuertoCerradoException {
        this.aeropuerto.verificarAbierto();
        // Si no hay lugar en la cola entra a hall a esperar
        while (!atencion.entrarCola(this)) {
            try {
                System.out.println(Console.colorString("WHITE",
                        "Pasajero " + this.idPasajero + " esperando en hall \uD83D\uDCA4\uD83D\uDCA4"));
                aeropuerto.getHall().esperarEnHall();
            } catch (Exception e) {
                System.out.println("ERROR al esperar en hall");
            }
        }

        this.reserva = atencion.esperarAtencion();
        atencion.salirPuestoAtencion(this);
    }

    private PuestoAtencion irInformes(PuestoAtencion atencion) throws AeropuertoCerradoException {
        this.aeropuerto.verificarAbierto();
        try {
            // Se dirige al puesto de informes y obtiene el puesto de atencion
            atencion = aeropuerto.getPuestoInformes().obtenerPuestoAtencion(reserva.getEmpresa());
            System.out.println(Console.colorString("GREEN",
                    "El pasajero " + this.idPasajero + " se dirige al puesto de atencion [" + atencion.getAerolinea()
                            + "]"));
            Thread.sleep(ESPERA_MILLIS);
        } catch (Exception e) {
            System.out.println("ERROR al ir a puesto de informes, pasajero: " + this.idPasajero);
        }
        return atencion;
    }

    public void run() {
        PuestoAtencion puestoAtencion;
        // Pasajero se dirije al puesto de informe para saber a que puesto de atencion
        // dirigirse
        try {
            puestoAtencion = irInformes(null);
            // Pasajero se dirije al puesto de antencion
            irPuestoAtencion(puestoAtencion);

            if (this.reserva == null) {
                // En caso de no haber vuelos se va del aeropuerto
                System.out.println(Console.colorString("BLACK",
                        "\uD83D\uDC80\uD83D\uDC80 Me voy del aeropuerto, no quedan vuelos \uD83D\uDC80\uD83D\uDC80"));
            } else {
                // Pasajero se dirije al tren
                irTren();
                // Pasajero se dirije a la terminal
                irTerminal();
            }
        } catch (AeropuertoCerradoException e) {
            System.out.println(e.getMessage());
        }
    }
}
