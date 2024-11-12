import java.util.ArrayList;
import java.util.Random;

import hilos.*;
import pasivos.*;

import java.util.List;
import java.util.HashMap;

public class Main {
    public static void main(String[] args) {
        List<String> empresas = List.of("Aerolineas", "FlyBondi", "Latam", "Pepinho");
        int cantPuestos = empresas.size();
        int capacidadMax = 5;
        int cantPasajeros = 50;
        int cantCajas = 6;
        int cantPuestosEmbarque = 20;
        Reloj reloj = new Reloj();
        Random rand = new Random();
        Tren tren = new Tren(10);
        Aeropuerto ar = new Aeropuerto(empresas, capacidadMax, tren, reloj);
        Thread threadReloj = new Thread(reloj);
        Thread conductor = new Thread(new Conductor(tren));
        Thread[] pasajeros = new Thread[cantPasajeros];
        Thread[] empleados = new Thread[cantPuestos];
        Thread[] cajeros = new Thread[cantCajas];
        Thread[] empEmbarque = new Thread[cantPuestosEmbarque];
        HashMap<String, PuestoAtencion> puestos = ar.getHashPuestoAtencion();
        HashMap<Character, Terminal> terminales = ar.getHashTerminal();

        new Thread(ar.getGuardia()).start();
        conductor.start();

        threadReloj.start();
        inicializarEmpleadosTerminal(terminales, cajeros, empEmbarque);
        inicializarPasajeros(pasajeros, ar, empresas, rand, cantPuestos);
        inicializarEmpleadosPuestos(empleados, puestos, empresas);

    }


    private static void inicializarEmpleadosTerminal(HashMap<Character, Terminal> terminales, Thread[] cajeros, Thread[] empEmbarque) {
        int indiceCajero = 0;
        int indiceEmpleado = 0;
        for (int i = 0; i < 3; i++) {
            char idTerminal = (char) (65 + i);
            Caja[] cajas = terminales.get(idTerminal).getFreeshop().getCajas();

            // Asocia cajeros con cajas
            for (int indiceCaja = 0; indiceCaja < cajas.length; indiceCaja++) {
                cajeros[indiceCajero] = new Thread(new Cajero(cajas[indiceCaja]));
                cajeros[indiceCajero].start();
                indiceCajero++;
            }

            // Asocia empleadoEmbarque con puestoEmbarque
            ArrayList<Integer> puertas = terminales.get(idTerminal).getArrayPuestosEmbarques();
            int limInferior = puertas.get(0);
            int limSuperior = puertas.get(puertas.size() - 1);
            HashMap<Integer, PuestoEmbarque> puestosEmbarque = terminales.get(idTerminal).getMapPuestoEmbarques();

            for (int indicePuerta = limInferior; indicePuerta <= limSuperior; indicePuerta++) {
                empEmbarque[indiceEmpleado] = new Thread(new EmpleadoEmbarque(puestosEmbarque.get(indicePuerta)));
                empEmbarque[indiceEmpleado].start();
            }
        }
    }

    private static void inicializarPasajeros(Thread[] pasajeros, Aeropuerto ar, List<String> empresas, Random rand, int cantPuestos) {
        for (int i = 0; i < pasajeros.length; i++) {
            pasajeros[i] = new Thread(new Pasajero(ar, empresas.get(rand.nextInt(cantPuestos))));
            pasajeros[i].start();
        }
    }

    private static void inicializarEmpleadosPuestos(Thread[] empleados, HashMap<String, PuestoAtencion> puestos, List<String> empresas) {
        Empleado emp;
        PuestoAtencion puesto;
        for (int i = 0; i < empleados.length; i++) {
            puesto = puestos.get(empresas.get(i));
            emp = new Empleado(puesto);
            empleados[i] = new Thread(emp);
            empleados[i].start();
        }
    }

}