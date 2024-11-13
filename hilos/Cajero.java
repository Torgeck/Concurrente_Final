package hilos;

import console.Console;
import pasivos.Aeropuerto;
import pasivos.Caja;

import java.util.Random;

public class Cajero implements Runnable {

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
        while (this.aeropuerto.getReloj().getDiaActual() < 7) {

            if (!this.aeropuerto.estaAbiertoAlPublico()) {
                System.out.println(Console.colorString("WHITE", "Cajero, Aeropuerto cerrado me voy a casa"));
                this.aeropuerto.esperarApertura();
            } else {
                System.out.println(Console.colorString("CYAN", "Cajero " + idEmpleado + " esperando a clientes para atender"));
                this.caja.atenderCliente();
                System.out.println(Console.colorString("CYAN", "Cajero " + idEmpleado + " ATENDIENDO cliente"));
                try {
                    Thread.sleep((random.nextInt(5) + 1) * 1000);
                } catch (Exception e) {
                    System.out.println(Console.colorString("RED", "ERROR CON CAJERO " + idEmpleado));
                }
                System.out.println(Console.colorString("CYAN", "Cajero " + idEmpleado + " termino de atender un cliente"));
                this.caja.liberarCliente();
            }
        }
    }
}
