package hilos;

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
        // TODO implementar run de empleadoEmbarque

        // Checkea si aeropuerto esta abierto

        // Se fija si hay vuelos proximos en su puerta

        // avisa a los pasajeros en terminal

    }

}
