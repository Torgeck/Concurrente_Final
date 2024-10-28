package hilos;

import console.Console;
import pasivos.Tren;

public class Conductor implements Runnable {

    private Tren tren;

    public void run() {
        int terminalActual;

        // TODO Cambiar a horario de cierre aeropuerto
        while (true) {
            terminalActual = 0;
            tren.esperarEnAeropuerto();

            while (terminalActual < 3) {
                tren.irSiguieteTerminal();
                try {
                    System.out.println(Console.colorString("GREEN", "===== Tren viajando ====="));
                    Thread.sleep(2000);
                } catch (Exception e) {
                    System.out.println(Console.colorString("RED", "ERROR exploto el tren"));
                }
                tren.esperarDesembarquePasajeros();
                terminalActual++;
            }
            tren.irAeropuerto();
        }
    }
}
