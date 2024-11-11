package hilos;

import java.util.Random;

import pasivos.PuestoAtencion;

public class Empleado implements Runnable {

    private static int ID = 0;
    private int idEmpleado;
    private PuestoAtencion puesto;
    private Random random;

    public Empleado(PuestoAtencion puesto) {
        this.idEmpleado = generarID();
        this.puesto = puesto;
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
        // TODO Cambiar condicion para utilizar horarios
        while (true) {
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
