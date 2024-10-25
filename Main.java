import java.util.Random;

import hilos.Empleado;
import hilos.Pasajero;
import pasivos.Aeropuerto;
import pasivos.PuestoAtencion;

import java.util.HashMap;

public class Main {
    public static void main(String[] args) {
        int cantPuestos = 1;
        int capacidadMax = 5;
        Random rand = new Random();
        Aeropuerto ar = new Aeropuerto(cantPuestos, capacidadMax);
        Thread[] pasajeros = new Thread[6];
        Thread[] empleados = new Thread[cantPuestos];
        HashMap<String, PuestoAtencion> puestos = ar.getHashPuestoAtencion();
        PuestoAtencion puesto;
        Empleado emp;

        new Thread(ar.getGuardia()).start();

        for (int i = 0; i < empleados.length; i++) {
            puesto = puestos.get("Empresa[" + i + "]");
            emp = new Empleado(puesto);
            empleados[i] = new Thread(emp);
            puesto.setEmpleado(emp);
            empleados[i].start();
        }

        for (int i = 0; i < pasajeros.length; i++) {
            pasajeros[i] = new Thread(new Pasajero(ar, "Empresa[" + rand.nextInt(cantPuestos) +
                    "]"));
            pasajeros[i].start();
        }

    }
}