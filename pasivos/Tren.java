package pasivos;

import console.Console;
import customExceptions.AeropuertoCerradoException;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Tren {

    private Aeropuerto aeropuerto;
    private int[] gruposTerminal;
    private int capacidadMax;
    private AtomicInteger capacidadActual;
    private char terminalActual;
    private AtomicBoolean flagEnViaje;
    private AtomicBoolean flagEnAeropuerto;
    private Lock lock;
    private Lock lockTerminal;
    private Condition enViaje;
    private Condition enAeropuerto;
    private CyclicBarrier barreraEmbarque;
    private CountDownLatch latchDesembarque;

    public Tren(Aeropuerto aeropuerto, int capacidad) {
        this.aeropuerto = aeropuerto;
        this.capacidadMax = capacidad;
        this.capacidadActual = new AtomicInteger(0);
        this.flagEnViaje = new AtomicBoolean(false);
        this.flagEnAeropuerto = new AtomicBoolean(true);
        this.lock = new ReentrantLock();
        this.lockTerminal = new ReentrantLock();
        this.enViaje = lock.newCondition();
        this.enAeropuerto = lock.newCondition();
        this.terminalActual = 'Q';
        this.gruposTerminal = new int[3];
        this.barreraEmbarque = new CyclicBarrier(capacidad);
    }

    private void ocuparLugar(char terminal) {
        // Ocupa lugar en su grupo
        lock.lock();
        try {
            int indice = getIndiceTerminal(terminal);
            this.capacidadActual.incrementAndGet();
            this.gruposTerminal[indice]++;

            if (capacidadActual.get() == capacidadMax) {
                flagEnViaje.set(true);
                enViaje.signal();
            } else {
                enAeropuerto.signal();
            }
            System.out.println(Console.colorString("YELLOW", "CAPACIDAD ACTUAL DEL TREN: [" + this.gruposTerminal[0] + "|" + this.gruposTerminal[1] + "|" + this.gruposTerminal[2] + "] = " + this.capacidadActual));
        } finally {
            lock.unlock();
        }
    }

    private int getIndiceTerminal(char terminal) {
        int indice = switch (terminal) {
            case 'C' -> 0;
            case 'B' -> 1;
            case 'A' -> 2;
            default -> -1;
        };
        return indice;
    }

    private char getTerminalSiguiente(char terminal) {
        char nuevaTerminal;
        switch (terminal) {
            case 'C' -> nuevaTerminal = 'B';
            case 'B' -> nuevaTerminal = 'A';
            default -> nuevaTerminal = 'C';
        }
        return nuevaTerminal;
    }

    private char getTerminalActual() {
        char terminal;
        lockTerminal.lock();
        try {
            terminal = this.terminalActual;
        } finally {
            lockTerminal.unlock();
        }
        return terminal;
    }

    private void actualizarTerminal() {
        lockTerminal.lock();
        try {
            terminalActual = getTerminalSiguiente(terminalActual);
            int cantGrupo = this.gruposTerminal[getIndiceTerminal(getTerminalActual())];
            latchDesembarque = new CountDownLatch(cantGrupo);
        } finally {
            lockTerminal.unlock();
        }
    }

    // Metodos pasajeros
    public void subirTren(int idPasajero, char terminal) throws AeropuertoCerradoException {
        lock.lock();
        try {
            while (!flagEnAeropuerto.get()) {
                this.aeropuerto.verificarAbierto();
                System.out.println(Console.colorString("WHITE", "Pasajero " + idPasajero + " esperando el tren \uD83D\uDC40⏳\uD83E\uDD71"));
                enAeropuerto.await();
            }
            this.aeropuerto.verificarAbierto();
            // Ocupa lugar para su terminal
            System.out.println(Console.colorString("GREEN", "Pasajero " + idPasajero + " embarco el tren \uD83D\uDE89\uD83C\uDFC3"));
            ocuparLugar(terminal);
        } catch (InterruptedException e) {
            System.out.println(Console.colorString("RED", "ERROR con pasajero al querer subir al tren"));
        } finally {
            lock.unlock();
        }
    }

    public void esperarEnTren(int idPasajero) {
        try {
            System.out.println(Console.colorString("GREEN", "Pasajero " + idPasajero + " esperando que el tren arranque"));
            barreraEmbarque.await();
        } catch (Exception e) {
            System.out.println(Console.colorString("RED", "ERROR con pasajero dentro del tren"));
        }
    }

    public void bajarTren(int idPasajero, char terminal) {
        lock.lock();
        try {
            // Verifica que llego a su terminal y si el tren esta parado, si no espera
            while ((terminal != getTerminalActual()) || flagEnViaje.get()) {
                this.enViaje.await();
            }
            // Se baja en su terminal
            latchDesembarque.countDown();
        } catch (Exception e) {
            System.out.println(Console.colorString("RED", "ERROR con pasajero al querer bajar al tren"));
            e.printStackTrace();
        } finally {
            lock.unlock();
        }
    }

    // Metodos Conductor
    public void esperarEnAeropuerto() {
        lock.lock();
        try {
            flagEnViaje.set(false);
            flagEnAeropuerto.set(true);
            System.out.println(Console.colorString("YELLOW", "\uD83D\uDE83 El tren se esta esperando en el aeropuerto"));
            enAeropuerto.signal();
            enViaje.await();
            flagEnAeropuerto.set(false);
        } catch (Exception e) {
            System.out.println(Console.colorString("RED", "ERROR exploto el tren en el aeropuerto"));
        } finally {
            lock.unlock();
        }
    }

    public void irSiguienteTerminal() {
        lock.lock();
        try {
            flagEnViaje.set(true);
            System.out.println(Console.colorString("YELLOW", "\uD83D\uDE83 El tren esta saliendo de la terminal: " + terminalActual + "\uD83D\uDE8F"));
            actualizarTerminal();

        } catch (Exception e) {
            System.out.println(Console.colorString("RED", "ERROR con el tren al querer ir a otra terminal"));
        } finally {
            lock.unlock();
        }
    }

    public void avisarPasajerosParada() {
        lock.lock();
        try {
            System.out.println(Console.colorString("YELLOW", "\uD83D\uDE83\uD83D\uDE8F El tren llego a la terminal: " + terminalActual + " avisa los pasajeros \uD83D\uDD0A"));
            flagEnViaje.set(false);
            enViaje.signalAll();

        } catch (Exception e) {
            System.out.println(Console.colorString("RED", "ERROR con tren al avisar pasajeros llegada a destino"));
        } finally {
            lock.unlock();
        }
    }

    public void esperarDesembarquePasajeros() {
        try {
            // Espera que se bajen todos los pasajeros
            latchDesembarque.await();
            System.out.println("Todos los pasajeros bajaron en terminal " + terminalActual);
        } catch (Exception e) {
            System.out.println(Console.colorString("RED", "ERROR el tren exploto en la terminal: " + terminalActual));
        }
    }

    public void irAeropuerto() {
        lock.lock();
        try {
            System.out.println(Console.colorString("YELLOW", "\uD83D\uDE85 El tren esta volviendo al Aeropuerto \uD83D\uDEC4 "));
            terminalActual = 'Q';
            barreraEmbarque.reset();
            this.gruposTerminal = new int[3];
            this.capacidadActual.set(0);
            this.flagEnViaje.compareAndSet(false, true);
            enAeropuerto.signal();
        } catch (Exception e) {
            System.out.println(Console.colorString("RED", "ERROR con el tren al querer ir al aeropuerto"));
        } finally {
            lock.unlock();
        }
    }

    public void avisarPasajerosCierre() {
        // Metodo que nofitica a los pasajeros esperando el tren que el aeropuerto cerro
        lock.lock();
        try {
            enAeropuerto.signalAll();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        } finally {
            lock.unlock();
        }
    }

    public void liberarConductor() {
        lock.lock();
        try {
            enViaje.signal();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        } finally {
            lock.unlock();
        }
    }

}
