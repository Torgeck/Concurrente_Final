package hilos;

import console.Console;
import pasivos.Aeropuerto;
import pasivos.PuestoEmbarque;
import pasivos.Terminal;

public class EmpleadoEmbarque implements Runnable {

    private static int ID = 0;
    private int idEmpleado;
    private PuestoEmbarque puesto;
    private Aeropuerto aeropuerto;

    public EmpleadoEmbarque(PuestoEmbarque puesto, Aeropuerto aeropuerto) {
        this.puesto = puesto;
        this.aeropuerto = aeropuerto;
        this.idEmpleado = genID();
    }

    private int genID() {
        return ID++;
    }

    public void run() {

        while (this.aeropuerto.getReloj().getDiaActual() < 7) {

            if (!this.aeropuerto.estaAbiertoAlPublico() || !puesto.hayVuelos()) {
                System.out.println(Console.colorString("WHITE", "Empleado embarque, Aeropuerto cerrado me voy a casa"));
                this.aeropuerto.esperarApertura();
            } else {
                System.out.println(Console.colorString("YELLOW", "Empleado embarque " + idEmpleado + " entra a laburar"));
                while (puesto.hayVuelos()) {
                    puesto.actualizarVuelo();
                    System.out.println(Console.colorString("YELLOW", "Empleado embarque " + idEmpleado + " crea recordatorio embarque del vuelo"));
                    puesto.ponerAlarma();
                    System.out.println(Console.colorString("YELLOW", "Empleado embarque " + idEmpleado + " avisa a pasajeros para el embarque del vuelo: " + puesto.getVueloActual()));
                    puesto.avisarPasajerosEmbarque();
                }
                System.out.println(Console.colorString("YELLOW", "Empleado embarque " + idEmpleado + " no hay mas vuelos para este puesto por hoy"));
            }
        }
    }

}
