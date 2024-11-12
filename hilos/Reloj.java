package hilos;

import console.Console;
import pasivos.Alarma;

import java.util.concurrent.PriorityBlockingQueue;
import java.util.concurrent.atomic.AtomicInteger;

public class Reloj implements Runnable {
    // FORMATO DE HORA 24-00
    private int dia;
    private final AtomicInteger horaActual;
    private final AtomicInteger minutoActual;
    private PriorityBlockingQueue<Alarma> alarms;

    public Reloj() {
        this.dia = 0;
        this.horaActual = new AtomicInteger(6);
        this.minutoActual = new AtomicInteger(0);
        this.alarms = new PriorityBlockingQueue<>();
    }

    public static int addMin(int hora, int min) {
        // Metodo que aniade minutos a la hora pasada por parametro
        int horaAux = hora / 100;
        int minAux = (hora % 100) + min;

        horaAux += minAux / 60;
        minAux %= 60;
        horaAux %= 24;

        return horaAux * 100 + minAux;
    }

    public static int subtractMin(int hora, int min) {
        // Metodo que resta minutos a la hora pasada por parámetro
        int horaAux = hora / 100;
        int minAux = hora % 100;

        minAux -= min;

        // Ajustar la hora y los minutos si minAux es negativo
        while (minAux < 0) {
            minAux += 60;
            horaAux--;

            if (horaAux < 0) {
                horaAux += 24;
            }
        }

        return horaAux * 100 + minAux;
    }

    public void addAlarm(Alarma alarm) {
        this.alarms.add(alarm);
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

    public static int convertirHora(int hora, int minutos) {
        /* Metodo que devuelve la hora en formato hhmm.
        i.e: hora = 4; minutos = 40 => 440
        */
        int minAux = minutos, horaAux = hora;

        if (minAux >= 60) {
            minAux = minutos % 60;
            horaAux += (minutos / 60);
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

    // Consultar si esta bien hacer un signal
    private void checkAlarm() {
        if (!this.alarms.isEmpty() && this.getTime() == alarms.peek().getTiempo()) {
            // Avisa si el tiempo actual es el mismo o mayor que el tiempo de la alarma
            try {
                alarms.poll().getPuestoEmbarque().sonarAlarma();
            } catch (Exception e) {
                System.out.println(Console.colorString("RED", "ERROR al querer hacer signal en RELOJ"));
                e.printStackTrace();
            }
        }
    }

    public int getTiempoRestante(int horaEmbarque) {
        // Metodo que devuelve el tiempo restante en minutos con respecto al actual
        // En caso de que sea negativo retorna 0
        int embarqueMinutos = ((horaEmbarque / 100) * 60) + (horaEmbarque % 100);
        int actualMinutos = ((this.getTime() / 100) * 60) + (this.getTime() % 100);

        // Calcula la diferencia en minutos
        int diferenciaMinutos = embarqueMinutos - actualMinutos;

        return Math.max(diferenciaMinutos, 0);
    }

    public void run() {
        while (this.dia < 7) {
            try {
                System.out.println(Console.colorString("BLUE", "Tiempo actual " + this.horaActual.get() + ":" + this.minutoActual.get()));
                Thread.sleep(2000);
                incrementTime();
                checkAlarm();
            } catch (Exception e) {
                System.out.println(Console.colorString("RED", "ERROR exploto RELOJ"));
            }
        }
    }
}
