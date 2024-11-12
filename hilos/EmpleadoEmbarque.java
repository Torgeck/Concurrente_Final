package hilos;

import console.Console;
import pasivos.PuestoEmbarque;
import pasivos.Terminal;

public class EmpleadoEmbarque implements Runnable {

    private static int ID = 0;
    private int idEmpleado;
    private PuestoEmbarque puesto;

    public EmpleadoEmbarque(PuestoEmbarque puesto) {
        this.puesto = puesto;
        this.idEmpleado = genID();
    }

    private int genID() {
        return ID++;
    }

    public void run() {
        boolean flag = true;
        while (flag) {
            System.out.println(Console.colorString("YELLOW", "Empleado embarque " + idEmpleado + " entra a laburar"));
            // Y mientras sea horario de trabajo
            while (puesto.hayVuelos()) {
                puesto.actualizarVuelo();
                System.out.println(Console.colorString("YELLOW", "Empleado embarque " + idEmpleado + " crea recordatorio embarque del vuelo"));
                puesto.ponerAlarma();
                System.out.println(Console.colorString("YELLOW", "Empleado embarque " + idEmpleado + " avisa a pasajeros para el embarque del vuelo: " + puesto.getVueloActual()));
                puesto.avisarPasajerosEmbarque();
            }
            System.out.println(Console.colorString("YELLOW", "Empleado embarque " + idEmpleado + " no hay mas vuelos para este puesto por hoy"));
            flag = false;
        }

    }

}
