package hilos;

import java.util.Random;

import console.Console;
import pasivos.Aeropuerto;
import pasivos.PuestoAtencion;

public class Empleado implements Runnable {

    private static int ID = 0;
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
        while (this.aeropuerto.getReloj().getDiaActual() < 7) {

            if (!this.aeropuerto.estaAbiertoAlPublico()) {
                System.out.println(Console.colorString("WHITE", "Empleado puesto atencion, Aerpuerto cerrado me voy a casa"));
                this.aeropuerto.esperarApertura();
                System.out.println("Empleado " + this.idEmpleado + " se dirige al puesto " + puesto.getIdPuesto());
            } else {
                puesto.atenderPasajero(this);
                // Aca lo atiende
                try {
                    Thread.sleep((random.nextInt(8) + 1) * 1000);
                } catch (Exception e) {
                    System.out.println("ERROR exploto el empleado [" + this.idEmpleado + "]");
                }

                puesto.liberarPasajero();
            }
        }
    }
}
