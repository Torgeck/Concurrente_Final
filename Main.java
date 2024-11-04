import java.util.Random;

import console.Console;
import hilos.Conductor;
import hilos.Empleado;
import hilos.Pasajero;
import hilos.Reloj;
import pasivos.*;

import java.util.List;
import java.util.HashMap;

public class Main {
    public static void main(String[] args) {
        List<String> empresas = List.of("Aerolineas", "FlyBondi", "Latam", "Pepinho");
        int cantPuestos = empresas.size();
        int capacidadMax = 5;
        Reloj reloj = new Reloj();
        Random rand = new Random();
        Tren tren = new Tren(10);
        Aeropuerto ar = new Aeropuerto(empresas, capacidadMax, tren, reloj);
        Thread conductor = new Thread(new Conductor(tren));
        Thread[] pasajeros = new Thread[30];
        Thread[] empleados = new Thread[cantPuestos];
        HashMap<String, PuestoAtencion> puestos = ar.getHashPuestoAtencion();
        PuestoAtencion puesto;
        Empleado emp;

        new Thread(ar.getGuardia()).start();
        conductor.start();
        for (int i = 0; i < empleados.length; i++) {
            puesto = puestos.get(empresas.get(i));
            emp = new Empleado(puesto);
            empleados[i] = new Thread(emp);
            puesto.setEmpleado(emp);
            empleados[i].start();
        }

        for (int i = 0; i < pasajeros.length; i++) {
            pasajeros[i] = new Thread(new Pasajero(ar, empresas.get(rand.nextInt(cantPuestos))));
            pasajeros[i].start();
        }

    }

}