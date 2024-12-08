package hilos;

import console.Console;
import pasivos.Aeropuerto;
import pasivos.Alarma;

import java.util.concurrent.PriorityBlockingQueue;
import java.util.concurrent.atomic.AtomicInteger;

public class Reloj implements Runnable {
    // FORMATO DE HORA 24-00
    private final AtomicInteger dia;
    private final AtomicInteger horaActual;
    private final AtomicInteger minutoActual;
    private boolean aeropuertoCerrado;
    private PriorityBlockingQueue<Alarma> alarms;
    private Aeropuerto aeropuerto;
    private final int diaFinal;

    public Reloj(int diaFinal) {
        this.diaFinal = diaFinal;
        this.dia = new AtomicInteger(1);
        this.horaActual = new AtomicInteger(6);
        this.minutoActual = new AtomicInteger(0);
        this.aeropuertoCerrado = false;
        this.alarms = new PriorityBlockingQueue<>();
    }

    public int getDiaActual() {
        return dia.get();
    }

    public void setAeropuerto(Aeropuerto aeropuerto) {
        this.aeropuerto = aeropuerto;
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

    public void agregarAlarma(Alarma alarm) {
        this.alarms.add(alarm);
    }

    public synchronized void incrementarTiempo(int min) {
        // Aumenta el tiempo actual mas 15 min
        if (horaActual.get() < 24) {
            if (minutoActual.addAndGet(min) == 60) {
                minutoActual.set(0);
                if (horaActual.incrementAndGet() == 24) {
                    horaActual.set(0);
                    this.dia.incrementAndGet();
                    System.out.println(Console.colorString("BLUE", "DIA " + this.dia.get()));
                }
            }
        }
    }

    public synchronized int getTiempoActual() {
        return horaActual.get() * 100 + minutoActual.get();
    }

    public static int convertirHora(int hora, int minutos) {
        /*
         * Metodo que devuelve la hora en formato hhmm.
         * i.e: hora = 4; minutos = 40 => 440
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
        /*
         * Metodo que devuelve la hora en formato hhmm.
         * i.e: hora = 4 => 400
         */
        int horaAux = hora;

        if (horaAux > 24) {
            horaAux = horaAux % 24;
        }

        return horaAux * 100;
    }

    private void checkAlarma() {
        if (!this.alarms.isEmpty() && this.getTiempoActual() == alarms.peek().getTiempo()) {
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
        int actualMinutos = ((this.getTiempoActual() / 100) * 60) + (this.getTiempoActual() % 100);

        // Calcula la diferencia en minutos
        int diferenciaMinutos = embarqueMinutos - actualMinutos;

        return Math.max(diferenciaMinutos, 0);
    }

    private void checkApertura() {
        if (this.getTiempoActual() == 600) {
            this.aeropuertoCerrado = false;
            this.aeropuerto.cambiarFlagCerrado(this.aeropuertoCerrado);
            this.aeropuerto.avisarApertura();
        }
    }

    private void checkCierre() {
        if (this.getTiempoActual() == 2200) {
            this.aeropuertoCerrado = true;
            this.aeropuerto.cambiarFlagCerrado(this.aeropuertoCerrado);
            this.aeropuerto.cerrarAeropuerto();
        }
    }

    public void run() {
        while (this.dia.get() <= this.diaFinal) {
            try {
                System.out.println(Console.colorString("BLUE",
                        String.format("Tiempo actual %02d:%02d", this.horaActual.get(), this.minutoActual.get())));
                Thread.sleep(1000);
                incrementarTiempo(15);

                if (aeropuertoCerrado) {
                    checkApertura();
                } else {
                    checkAlarma();
                    checkCierre();
                }

            } catch (Exception e) {
                System.out.println(Console.colorString("RED", "ERROR exploto RELOJ"));
                e.printStackTrace();
            }
        }
        // Cierra el aeropuerto
        System.out.println(Console.colorString("RED", "CERRO AEROPUERTO"));
        this.aeropuerto.cerrarPermanente();
    }
}
