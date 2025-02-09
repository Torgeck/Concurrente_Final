package hilos;

import console.Console;
import pasivos.Aeropuerto;
import pasivos.Alarma;

import java.util.concurrent.PriorityBlockingQueue;
import java.util.concurrent.atomic.AtomicInteger;

public class Reloj implements Runnable {
    // FORMATO DE HORA 24-00
    private static final int CIEN = 100;
    private static final int HORA_APERTURA = 600;
    private static final int HORA_CIERRE = 2200;
    private static final int HORA_MAX = 24;
    private static final int MINUTOS_MAX = 60;
    private static final int INCREMENTO_MINUTOS = 15;
    private static final int ESPERA_MILLIS = 3000;
    private final AtomicInteger dia;
    private final AtomicInteger horaActual;
    private final AtomicInteger minutoActual;
    private boolean aeropuertoCerrado;
    private PriorityBlockingQueue<Alarma> alarmas;
    private Aeropuerto aeropuerto;
    private final int diaFinal;

    public Reloj(int diaFinal) {
        this.diaFinal = diaFinal;
        this.dia = new AtomicInteger(1);
        this.horaActual = new AtomicInteger(6);
        this.minutoActual = new AtomicInteger(0);
        this.aeropuertoCerrado = false;
        this.alarmas = new PriorityBlockingQueue<>();
    }

    public int getDiaActual() {
        return dia.get();
    }

    public void setAeropuerto(Aeropuerto aeropuerto) {
        this.aeropuerto = aeropuerto;
    }

    public static int addMin(int hora, int min) {
        // Metodo que aniade minutos a la hora pasada por parametro
        int horaAux = hora / CIEN;
        int minAux = (hora % CIEN) + min;

        horaAux += minAux / MINUTOS_MAX;
        minAux %= MINUTOS_MAX;
        horaAux %= HORA_MAX;

        return horaAux * CIEN + minAux;
    }

    public void agregarAlarma(Alarma alarm) {
        this.alarmas.add(alarm);
    }

    public void incrementarTiempo(int min) {
        // Aumenta el tiempo actual mas 15 min
        if (horaActual.get() < HORA_MAX) {
            if (minutoActual.addAndGet(min) == MINUTOS_MAX) {
                minutoActual.set(0);
                if (horaActual.incrementAndGet() == HORA_MAX) {
                    horaActual.set(0);
                    this.dia.incrementAndGet();
                    System.out.println(Console.colorString("BLUE", "DIA " + this.dia.get()));
                }
            }
        }
    }

    public synchronized int getTiempoActual() {
        return horaActual.get() * CIEN + minutoActual.get();
    }

    public static int convertirHora(int hora, int minutos) {
        /*
         * Metodo que devuelve la hora en formato hhmm.
         * i.e: hora = 4; minutos = 40 => 440
         */
        int minAux = minutos, horaAux = hora;

        if (minAux >= MINUTOS_MAX) {
            minAux = minutos % MINUTOS_MAX;
            horaAux += (minutos / MINUTOS_MAX);
        }

        if (horaAux > HORA_MAX) {
            horaAux = horaAux % HORA_MAX;
        }

        return horaAux * CIEN + minAux;
    }

    public static int convertirHora(int hora) {
        /*
         * Metodo que devuelve la hora en formato hhmm.
         * i.e: hora = 4 => 400
         */
        int horaAux = hora;

        if (horaAux > HORA_MAX) {
            horaAux = horaAux % HORA_MAX;
        }

        return horaAux * CIEN;
    }

    private void checkAlarma() {
        if (!this.alarmas.isEmpty() && this.getTiempoActual() == alarmas.peek().getTiempo()) {
            // Avisa si el tiempo actual es el mismo o mayor que el tiempo de la alarma
            try {
                alarmas.poll().getPuestoEmbarque().sonarAlarma();
            } catch (Exception e) {
                System.out.println(Console.colorString("RED", "ERROR al querer hacer signal en RELOJ"));
                e.printStackTrace();
            }
        }
    }

    public int getTiempoRestante(int horaEmbarque) {
        // Metodo que devuelve el tiempo restante en minutos con respecto al actual
        // En caso de que sea negativo retorna 0
        int embarqueMinutos = ((horaEmbarque / CIEN) * MINUTOS_MAX) + (horaEmbarque % CIEN);
        int actualMinutos = ((this.getTiempoActual() / CIEN) * MINUTOS_MAX) + (this.getTiempoActual() % CIEN);

        // Calcula la diferencia en minutos
        int diferenciaMinutos = embarqueMinutos - actualMinutos;

        return Math.max(diferenciaMinutos, 0);
    }

    private void checkApertura() {
        if (this.getTiempoActual() == HORA_APERTURA) {
            this.aeropuertoCerrado = false;
            this.aeropuerto.cambiarFlagCerrado(this.aeropuertoCerrado);
            this.aeropuerto.avisarApertura();
        }
    }

    private void checkCierre() {
        if (this.getTiempoActual() == HORA_CIERRE) {
            this.aeropuertoCerrado = true;
            this.aeropuerto.cambiarFlagCerrado(this.aeropuertoCerrado);
            this.aeropuerto.cerrarAeropuerto();
        }
    }

    public void run() {
        while (this.dia.get() <= this.diaFinal) {
            try {
                System.out.println(Console.colorString("BLUE",
                        String.format("⏰⏰ Tiempo actual %02d:%02d ⏰⏰", this.horaActual.get(), this.minutoActual.get())));
                Thread.sleep(ESPERA_MILLIS);
                incrementarTiempo(INCREMENTO_MINUTOS);

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
