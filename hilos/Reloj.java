package hilos;

import console.Console;

import java.util.concurrent.atomic.AtomicInteger;

public class Reloj implements Runnable {
    // FORMATO DE HORA 24-00
    private int dia;
    private AtomicInteger horaActual;
    private AtomicInteger minutoActual;

    public Reloj() {
        this.dia = 0;
        this.horaActual = new AtomicInteger(6);
        this.minutoActual = new AtomicInteger(0);
    }

    public void run() {
        while (true) {
            try {
                System.out.println(Console.colorString("BLUE", "Tiempo actual" + this.getTime()));
                Thread.sleep(5000);
                incrementTime();
            } catch (Exception e) {
                System.out.println("ERROR exploto el reloj");
            }
        }
    }

    public synchronized void incrementTime() {
        // Aumenta el tiempo actual mas 15 min
        if (horaActual.get() < 24) {
            if (minutoActual.addAndGet(15) == 60) {
                minutoActual.set(0);
                if (horaActual.incrementAndGet() == 24) {
                    horaActual.set(0);
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


}
