package hilos;

import console.Console;
import pasivos.Tren;

public class Conductor implements Runnable {

    private Tren tren;
    private int tiempoViaje;

    public Conductor(Tren tren) {
        this.tren = tren;
        this.tiempoViaje = 1000 * 10;
    }

    public void run() {
        int terminalActual;

        // TODO Cambiar a horario de cierre aeropuerto
        while (true) {
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
                Thread.sleep(this.tiempoViaje * 3);
            } catch (InterruptedException e) {
                System.out.println(Console.colorString("RED", "ERROR exploto el tren"));
            }
        }
    }
}
