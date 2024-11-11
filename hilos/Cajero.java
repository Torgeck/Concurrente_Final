package hilos;

import console.Console;
import pasivos.Caja;

import java.util.Random;

public class Cajero implements Runnable {

    private String idEmpleado;
    private Caja caja;
    private Random random;

    public Cajero(Caja caja) {
        this.caja = caja;
        this.random = new Random();
        this.idEmpleado = caja.getIdCaja();
    }

    public void run() {
        while (true) {
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
            // TODO se tiene que fijar si es hora de cerrar
        }
    }
}
