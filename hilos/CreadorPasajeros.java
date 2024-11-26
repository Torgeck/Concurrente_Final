package hilos;

import pasivos.Aeropuerto;

import java.util.List;
import java.util.Random;

public class CreadorPasajeros implements Runnable {

    private Aeropuerto aeropuerto;
    private Reloj reloj;
    private int segundos;
    private int cantPasajeros;
    private int cantDias;

    public CreadorPasajeros(int segundos, int cantPasajeros, int cantDias, Reloj reloj, Aeropuerto aeropuerto) {
        this.segundos = segundos * 1000;
        this.cantPasajeros = cantPasajeros;
        this.cantDias = cantDias;
        this.aeropuerto = aeropuerto;
        this.reloj = reloj;
    }

    public void run() {

        while (this.reloj.getDiaActual() <= this.cantDias) {
            Thread[] pasajeros = new Thread[this.cantPasajeros];
            inicializarPasajeros(pasajeros, this.aeropuerto);
            try {
                Thread.sleep(segundos);
            } catch (InterruptedException e) {
                System.out.println("ERROR al crear pasajeros");
            }
        }
    }

    private static void inicializarPasajeros(Thread[] pasajeros, Aeropuerto aeropuerto) {
        Random r = new Random();
        List<String> empresas = aeropuerto.getEmpresas();
        int cantPuestos = empresas.size();
        for (int i = 0; i < pasajeros.length; i++) {
            pasajeros[i] = new Thread(new Pasajero(aeropuerto, empresas.get(r.nextInt(cantPuestos))));
            pasajeros[i].start();
        }
    }


}
