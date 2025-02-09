package hilos;

import console.Console;
import customExceptions.AeropuertoCerradoException;
import pasivos.Aeropuerto;
import pasivos.Tren;

public class Conductor implements Runnable {

    private static final int SEG_MILLIS = 1000;
    private static final int DEMORA = 1;
    private Tren tren;
    private Aeropuerto aeropuerto;
    private int tiempoViaje;

    public Conductor(Tren tren, Aeropuerto aeropuerto) {
        this.tren = tren;
        this.aeropuerto = aeropuerto;
        this.tiempoViaje = SEG_MILLIS * DEMORA;
    }

    public void run() {
        int terminalActual;

        while (!this.aeropuerto.getFlagSimulacion()) {

            if (this.aeropuerto.estaCerradoAlPublico()) {
                System.out.println(Console.colorString("YELLOW",
                        "Conductor notifica pasajeros esperando que cerro por hoy el aeropuerto"));
                this.tren.avisarPasajerosCierre();
                System.out.println(Console.colorString("WHITE", "Conductor, Aeropuerto cerrado me voy a casa"));
                this.aeropuerto.esperarApertura();
            } else {

                terminalActual = 0;
                try {
                    tren.esperarEnAeropuerto();
                } catch (AeropuertoCerradoException e) {
                    terminalActual = -1;
                    tren.reset();
                }

                if (terminalActual == 0) {
                    while (terminalActual < 3) {
                        tren.irSiguienteTerminal();
                        try {
                            Thread.sleep(this.tiempoViaje);
                            tren.avisarPasajerosParada();
                        } catch (Exception e) {
                            System.out.println(Console.colorString("RED", "ERROR al ir a una terminal"));
                        }
                        tren.esperarDesembarquePasajeros();
                        terminalActual++;
                    }
                    tren.irAeropuerto();
                    try {
                        Thread.sleep(this.tiempoViaje);
                    } catch (InterruptedException e) {
                        System.out.println(Console.colorString("RED", "ERROR al volver a aeropuerto"));
                    }
                }
            }
        }
        System.out.println(Console.colorString("YELLOW",
                "Conductor notifica pasajeros esperando que cerro permanentemente el aeropuerto"));
        this.tren.avisarPasajerosCierre();
        System.out.println(Console.colorString("BLACK",
                "\uD83C\uDFC4\uD83C\uDFC4 Conductor se toma vacaciones por siempre \uD83C\uDFC4\uD83C\uDFC4"));
    }
}
