import java.util.Random;

import hilos.Empleado;
import hilos.Pasajero;
import pasivos.Aeropuerto;
import pasivos.PuestoAtencion;
import pasivos.Terminal;
import pasivos.Vuelo;

import java.util.List;
import java.util.HashMap;

public class Main {
    public static void main(String[] args) {
        List<String> empresas = List.of("Aerolineas", "FlyBondi", "Latam", "Pepinho");
        int cantPuestos = empresas.size();
        int capacidadMax = 5;
        Random rand = new Random();
        Aeropuerto ar = new Aeropuerto(empresas, capacidadMax);
        Thread[] pasajeros = new Thread[6];
        Thread[] empleados = new Thread[cantPuestos];
        HashMap<String, PuestoAtencion> puestos = ar.getHashPuestoAtencion();
        PuestoAtencion puesto;
        Empleado emp;

        new Thread(ar.getGuardia()).start();

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

    public void generarVuelos(Aeropuerto aero, List<String> empresas ,int cantVuelos, long espacio, Random rand) {
        long horaActual = 6000;
        int cantEmpresas = empresas.size();
        Vuelo vuelo;
        String empresa;
        Terminal terminal;
        for (int i = 1; i <= cantVuelos; i++) {
            empresa = empresas.get(rand.nextInt(cantEmpresas));
            terminal = aero.getTerminalRandom();
            vuelo = new Vuelo(empresa, terminal.getIdTerminal(), terminal.getPuestoRandom() , )
        }

    }
}