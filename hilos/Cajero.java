package hilos;

import console.Console;
import pasivos.Aeropuerto;
import pasivos.Caja;

import java.util.Random;

public class Cajero implements Runnable {

    private static final int MAX_TIEMPO = 2;
    private static final int SEGUNDO = 1;
    private static final int SEG_MILLIS = 1000;
    private String idEmpleado;
    private Caja caja;
    private Random random;
    private Aeropuerto aeropuerto;

    public Cajero(Caja caja, Aeropuerto aeropuerto) {
        this.caja = caja;
        this.aeropuerto = aeropuerto;
        this.random = new Random();
        this.idEmpleado = caja.getIdCaja();
    }

    public void run() {
        boolean hayClientes;
        while (!this.aeropuerto.getFlagSimulacion()) {

            if (this.aeropuerto.estaCerradoAlPublico()) {

                // Se fija si hay clientes esperando en la cola
                while (this.caja.getCantClientes() > 0) {
                    System.out.println(Console.colorString("WHITE", "Cajero atiende a cliente para poder irse"));
                    this.caja.atenderCliente();
                    this.caja.liberarCliente();
                }

                System.out.println(Console.colorString("RED", "Cajero, Aeropuerto cerrado me voy a casa"));
                this.aeropuerto.esperarApertura();
            } else {
                System.out.println(
                        Console.colorString("CYAN", "Cajero " + idEmpleado + " esperando a clientes para atender"));
                hayClientes = this.caja.atenderCliente();

                if (hayClientes) {
                    System.out.println(Console.colorString("CYAN", "Cajero " + idEmpleado + " ATENDIENDO cliente"));
                    try {
                        Thread.sleep((random.nextInt(MAX_TIEMPO) + SEGUNDO) * SEG_MILLIS);
                    } catch (Exception e) {
                        System.out.println(Console.colorString("RED", "ERROR CON CAJERO " + idEmpleado));
                    }
                    System.out.println(
                            Console.colorString("CYAN", "Cajero " + idEmpleado + " termino de atender un cliente"));
                    this.caja.liberarCliente();
                } else {
                    System.out.println(Console.colorString("CYAN", "Cajero " + idEmpleado + " se tomo un recreo"));
                }

            }
        }
        System.out.println(Console.colorString("BLACK", "\uD83C\uDFC4\uD83C\uDFC4 Cajero " + this.idEmpleado
                + " se toma vacaciones por siempre \uD83C\uDFC4\uD83C\uDFC4"));
    }
}
