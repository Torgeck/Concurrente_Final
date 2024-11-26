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
        while (!this.aeropuerto.estaCerrado()) {

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
        System.out.println(Console.colorString("BLACK", "\uD83C\uDFC4\uD83C\uDFC4 Cajero " + this.idEmpleado + " se toma vacaciones por siempre \uD83C\uDFC4\uD83C\uDFC4"));
    }
}
