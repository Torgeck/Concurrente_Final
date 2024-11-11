package hilos;

import console.Console;

import java.util.concurrent.atomic.AtomicInteger;

public class Reloj implements Runnable {
    // FORMATO DE HORA 24-00
    private int dia;
    private final AtomicInteger horaActual;
    private final AtomicInteger minutoActual;

    public Reloj() {
        this.dia = 0;
        this.horaActual = new AtomicInteger(6);
        this.minutoActual = new AtomicInteger(0);
    }

    public static int addMin(int hora, int min) {
        // Metodo que aniade minutos a la hora pasada por parametro
        int horaAux = hora / 100;
        int minAux = (hora % 100) + min;
        int aux;

        if (minAux >= 60) {
            aux = minAux / 60;
            minAux = minAux % 60;
            horaAux = horaAux + aux;
        }

        if (horaAux >= 24) {
            horaAux = horaAux % 24;
        }

        return horaAux * 100 + minAux;
    }

    public synchronized void incrementTime() {
        // Aumenta el tiempo actual mas 15 min
        if (horaActual.get() < 24) {
            if (minutoActual.addAndGet(15) == 60) {
                minutoActual.set(0);
                if (horaActual.incrementAndGet() == 24) {
                    horaActual.set(0);
                    this.dia++;
                }
            }
        }
    }

    public synchronized int getTime() {
        return horaActual.get() * 100 + minutoActual.get();
    }

    public synchronized boolean estaAbiertoAlPublico() {
        return (this.horaActual.get() < 22) && (horaActual.get() >= 6);
    }

    public synchronized boolean tieneTiempoFreeshop(int tiempoEmbarque) {
        // TODO implementar funcionalidad en Pasajero Tiene mas de 30min antes del vuelo
        return (this.horaActual.get() * 100) - tiempoEmbarque >= 50;
    }

    public synchronized boolean perdioVuelo(int tiempoEmbarque) {
        // TODO implementar funcionalidad en Pasajero
        return tiempoEmbarque > horaActual.get() * 100 + minutoActual.get();
    }

    public static int convertirHora(int hora, int minutos) {
        /* Metodo que devuelve la hora en formato hhmm.
        i.e: hora = 4; minutos = 40 => 440
        */
        int minAux = minutos, horaAux = hora;

        if (minAux >= 60) {
            minAux = minutos % 60;
            horaAux += Math.floorDiv(minutos, 60);
        }

        if (horaAux > 24) {
            horaAux = horaAux % 24;
        }

        return horaAux * 100 + minAux;
    }

    public static int convertirHora(int hora) {
        /* Metodo que devuelve la hora en formato hhmm.
        i.e: hora = 4 => 400
        */
        int horaAux = hora;

        if (horaAux > 24) {
            horaAux = horaAux % 24;
        }

        return horaAux * 100;
    }

    public void run() {
        while (this.dia < 7) {
            try {
                System.out.println(Console.colorString("BLUE", "Tiempo actual " + this.horaActual.get() + ":" + this.minutoActual.get()));
                Thread.sleep(5000);
                incrementTime();
            } catch (Exception e) {
                System.out.println("ERROR exploto el reloj");
            }
        }
    }

}
