import java.util.ArrayList;

import hilos.*;
import pasivos.*;

import java.util.List;
import java.util.HashMap;

public class Main {
    public static void main(String[] args) {
        List<String> empresas = List.of("Aerolineas", "FlyBondi", "Latam", "Pepinho");
        int cantPuestos = empresas.size();
        int capacidadMax = 5;
        int cantCajas = 6;
        int cantPuestosEmbarque = 20;
        int cantDiasSimulacion = 2;
        int capacidadTren = 10;
        int cantPasajeros = 20;
        int segundos = 20;
        Reloj reloj = new Reloj(cantDiasSimulacion);
        Aeropuerto ar = new Aeropuerto(empresas, capacidadMax, reloj, capacidadTren);
        CreadorPasajeros creadorPasajeros = new CreadorPasajeros(segundos, cantPasajeros, cantDiasSimulacion, reloj,
                ar);
        reloj.setAeropuerto(ar);
        Thread threadReloj = new Thread(reloj);
        Thread pasajeros = new Thread(creadorPasajeros);
        Thread conductor = new Thread(new Conductor(ar.getTren(), ar));
        Thread guardia = new Thread(ar.getGuardia());
        Thread[] empleados = new Thread[cantPuestos];
        Thread[] cajeros = new Thread[cantCajas];
        Thread[] empEmbarque = new Thread[cantPuestosEmbarque];
        HashMap<String, PuestoAtencion> puestos = ar.getHashPuestoAtencion();
        HashMap<Character, Terminal> terminales = ar.getHashTerminal();

        guardia.start();
        conductor.start();
        threadReloj.start();
        pasajeros.start();
        inicializarEmpleadosTerminal(terminales, cajeros, empEmbarque, ar);
        inicializarEmpleadosPuestos(empleados, puestos, empresas);

    }

    private static void inicializarEmpleadosTerminal(HashMap<Character, Terminal> terminales, Thread[] cajeros,
            Thread[] empEmbarque, Aeropuerto ar) {
        int indiceCajero = 0;
        int indiceEmpleado = 0;
        for (int i = 0; i < 3; i++) {
            char idTerminal = (char) (65 + i);
            Caja[] cajas = terminales.get(idTerminal).getFreeshop().getCajas();

            // Asocia cajeros con cajas
            for (int indiceCaja = 0; indiceCaja < cajas.length; indiceCaja++) {
                cajeros[indiceCajero] = new Thread(new Cajero(cajas[indiceCaja], ar));
                cajeros[indiceCajero].start();
                indiceCajero++;
            }

            // Asocia empleadoEmbarque con puestoEmbarque
            ArrayList<Integer> puertas = terminales.get(idTerminal).getArrayPuestosEmbarques();
            int limInferior = puertas.get(0);
            int limSuperior = puertas.get(puertas.size() - 1);
            HashMap<Integer, PuestoEmbarque> puestosEmbarque = terminales.get(idTerminal).getMapPuestoEmbarques();

            for (int indicePuerta = limInferior; indicePuerta <= limSuperior; indicePuerta++) {
                empEmbarque[indiceEmpleado] = new Thread(new EmpleadoEmbarque(puestosEmbarque.get(indicePuerta), ar));
                empEmbarque[indiceEmpleado].start();
            }
        }
    }

    private static void inicializarEmpleadosPuestos(Thread[] empleados, HashMap<String, PuestoAtencion> puestos,
            List<String> empresas) {
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