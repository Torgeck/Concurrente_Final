package hilos;

import console.Console;
import pasivos.Aeropuerto;
import pasivos.Tren;

public class Conductor implements Runnable {

    private Tren tren;
    private Aeropuerto aeropuerto;
    private int tiempoViaje;

    public Conductor(Tren tren, Aeropuerto aeropuerto) {
        this.tren = tren;
        this.aeropuerto = aeropuerto;
        this.tiempoViaje = 1000 * 4;
    }

    public void run() {
        int terminalActual;

        while (this.aeropuerto.getReloj().getDiaActual() < 7) {

            if (!this.aeropuerto.estaAbiertoAlPublico()) {
                System.out.println(Console.colorString("WHITE", "Conductor, Aeropuerto cerrado me voy a casa"));
                this.aeropuerto.esperarApertura();
            } else {

                terminalActual = 0;
                tren.esperarEnAeropuerto();

                while (terminalActual < 3) {
                    tren.irSiguienteTerminal();
                    try {
                        Thread.sleep(this.tiempoViaje);
                        tren.avisarPasajerosParada();
                    } catch (Exception e) {
                        System.out.println(Console.colorString("RED", "ERROR exploto el tren"));
                    }
                    tren.esperarDesembarquePasajeros();
                    terminalActual++;
                }

                tren.irAeropuerto();
                try {
                    // Antes simulaba tiempoViaje * 3
                    Thread.sleep(this.tiempoViaje);
                } catch (InterruptedException e) {
                    System.out.println(Console.colorString("RED", "ERROR exploto el tren"));
                }
            }
        }
    }
}

