package hilos;

import java.util.Random;

import console.Console;
import pasivos.Aeropuerto;
import pasivos.PuestoAtencion;

public class Empleado implements Runnable {

    private static int ID = 0;
    private static final int TIEMPO_MAX = 8;
    private static final int TIEMPO_MIN = 1;
    private static final int SEG_MILLIS = 1000;
    private int idEmpleado;
    private PuestoAtencion puesto;
    private Random random;
    private Aeropuerto aeropuerto;

    public Empleado(PuestoAtencion puesto) {
        this.idEmpleado = generarID();
        this.puesto = puesto;
        this.aeropuerto = puesto.getAeropuerto();
        this.random = new Random();
    }

    private int generarID() {
        // Genera un ID unico para una instancia
        return ++ID;
    }

    public int getIdEmpleado() {
        return idEmpleado;
    }

    public void run() {

        System.out.println("Empleado " + this.idEmpleado + " se dirige al puesto " + puesto.getIdPuesto());
        while (!this.aeropuerto.getFlagSimulacion()) {

            if (this.aeropuerto.estaCerradoAlPublico()) {
                System.out.println(Console.colorString("PURPLE",
                        "Empleado " + this.idEmpleado + " se fija si quedan pasajeros esperando en cola"));
                // Avisa a los pasajeros que ya cerro el aeropuerto
                while (puesto.hayPasajerosEsperando()) {
                    puesto.atenderPasajero(this);
                    puesto.liberarPasajero();
                }

                System.out.println(Console.colorString("WHITE",
                        "Empleado en puesto atencion " + puesto.getIdPuesto() + ", Aeropuerto cerrado me voy a casa"));

                if (!this.aeropuerto.getFlagSimulacion()) {
                    this.aeropuerto.esperarApertura();
                    System.out.println("Empleado " + this.idEmpleado + " se dirige al puesto " + puesto.getIdPuesto());
                }

            } else {

                if (this.puesto.atenderPasajero(this)) {
                    try {
                        Thread.sleep((random.nextInt(TIEMPO_MAX) + TIEMPO_MIN) * SEG_MILLIS);
                    } catch (Exception e) {
                        System.out.println("ERROR exploto el empleado [" + this.idEmpleado + "]");
                    }

                    puesto.liberarPasajero();
                } else {

                    System.out.println(Console.colorString("PURPLE",
                            "Empleado " + this.idEmpleado + " se tomo un recreo"));
                }
            }
        }
        System.out.println(Console.colorString("BLACK", "\uD83C\uDFC4\uD83C\uDFC4 Empleado de puesto de atencion "
                + this.idEmpleado + " se toma vacaciones por siempre \uD83C\uDFC4\uD83C\uDFC4"));
    }
}
