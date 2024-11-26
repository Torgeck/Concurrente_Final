package hilos;

import console.Console;
import pasivos.Aeropuerto;
import pasivos.PuestoEmbarque;

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

        while (!this.aeropuerto.estaCerrado()) {

            if (this.aeropuerto.estaCerradoAlPublico() || !puesto.hayVuelos()) {
                System.out.println(Console.colorString("WHITE", "Empleado embarque avisa que va a cerrar su puesto"));
                this.puesto.avisarPasajerosEmbarque();
                System.out.println(Console.colorString("WHITE", "Empleado embarque, Aeropuerto cerrado / No mas vuelos, me voy a casa"));
                this.aeropuerto.esperarApertura();
            } else {
                System.out.println(Console.colorString("YELLOW", "Empleado embarque " + this.idEmpleado + " entra a laburar"));
                while (puesto.hayVuelos()) {
                    puesto.actualizarVuelo();
                    System.out.println(Console.colorString("YELLOW", "Empleado embarque " + this.idEmpleado + " crea recordatorio embarque del vuelo"));
                    puesto.ponerAlarma();
                    System.out.println(Console.colorString("YELLOW", "Empleado embarque " + this.idEmpleado + " avisa a pasajeros para el embarque del vuelo: " + puesto.getVueloActual()));
                    puesto.avisarPasajerosEmbarque();
                }
                System.out.println(Console.colorString("YELLOW", "Empleado embarque " + this.idEmpleado + " no hay mas vuelos para este puesto por hoy"));
            }
        }
        System.out.println(Console.colorString("BLACK", "\uD83C\uDFC4\uD83C\uDFC4 Empleado de embarque " + this.idEmpleado + " se toma vacaciones por siempre \uD83C\uDFC4\uD83C\uDFC4"));
    }

}
