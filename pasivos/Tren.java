package pasivos;

import console.Console;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Tren {

    private Aeropuerto aeropuerto;
    private int[] gruposTerminal;
    private int capacidadMax;
    private int capacidadActual;
    private char terminalActual;
    private boolean flagEnViaje;
    private Lock lock;
    private Condition enViaje;
    private Condition enAeropuerto;
    private CyclicBarrier barreraEmbarque;
    private CountDownLatch latchDesembarque;

    public Tren(Aeropuerto aeropuerto, int capacidad) {
        this.aeropuerto = aeropuerto;
        this.capacidadMax = capacidad;
        this.capacidadActual = 0;
        this.flagEnViaje = false;
        this.lock = new ReentrantLock();
        this.enViaje = lock.newCondition();
        this.enAeropuerto = lock.newCondition();
        this.terminalActual = 'Q';
        this.gruposTerminal = new int[3];
        this.barreraEmbarque = new CyclicBarrier(capacidad);
    }

    private synchronized void ocuparLugar(char terminal) {
        // Ocupa lugar en su grupo
        int indice = getIndiceTerminal(terminal);
        this.capacidadActual++;
        this.gruposTerminal[indice]++;
    }

    private static int getIndiceTerminal(char terminal) {
        int indice = switch (terminal) {
            case 'C' -> 0;
            case 'B' -> 1;
            case 'A' -> 2;
            default -> -1;
        };
        return indice;
    }

    // Metodos pasajeros
    public void subirTren(int idPasajero, char terminal) {
        lock.lock();
        try {
            // Si esta viajando espera a que llegue al aeropuerto
            while (flagEnViaje) {
                System.out.println("Pasajero " + idPasajero + " esperando el tren");
                this.enAeropuerto.await();
            }

            // Ocupa lugar para su terminal
            System.out.println("Pasajero " + idPasajero + " embarco el tren");
            ocuparLugar(terminal);

            if (capacidadActual == capacidadMax) {
                this.enViaje.signal();
            }

            barreraEmbarque.await();

        } catch (Exception e) {
            System.out.println("ERROR con pasajero al querer subir al tren");
        } finally {
            lock.unlock();
        }
    }

    public void bajarTren(int idPasajero, char terminal) {
        lock.lock();
        try {
            // Verifica que llego a su terminal si no espera
            while (terminal != terminalActual) {
                this.enViaje.await();
            }

            // Se baja en su terminal
            latchDesembarque.countDown();
            System.out.println("Pasajero " + idPasajero + " bajo del tren en terminal: " + terminalActual);
        } catch (Exception e) {
            System.out.println("ERROR con pasajero al querer bajar al tren");
        } finally {
            lock.unlock();
        }
    }

    // Metodos Tren? Maquinista?
    public void esperarEnAeropuerto() {
        lock.lock();
        try {
            System.out.println("El tren se esta esperando en el aeropuerto");
            this.enAeropuerto.signalAll();
            this.enViaje.await();
            flagEnViaje = true;

        } catch (Exception e) {
            System.out.println("ERROR exploto el tren en el aeropuerto");
        } finally {
            lock.unlock();
        }
    }

    public void irSiguieteTerminal() {
        lock.lock();
        try {
            System.out.println("El tren se esta moviendo de la terminal: " + terminalActual);
            terminalActual = getTerminalSiguiente(terminalActual);
            latchDesembarque = new CountDownLatch(this.gruposTerminal[getIndiceTerminal(terminalActual)]);

        } catch (Exception e) {
            System.out.println(Console.colorString("RED", "ERROR con el tren al querer ir a otra terminal"));
        } finally {
            lock.unlock();
        }
    }

    public void esperarDesembarquePasajeros() {
        lock.lock();
        try {
            System.out.println("El tren llego a la terminal: " + terminalActual + " avisa los pasajeros");
            enViaje.signalAll();
            // Espera que se bajen todos los pasajeros
            latchDesembarque.await();
            System.out.println("Se bajaron todos los pasajeros");
        } catch (Exception e) {
            System.out.println(Console.colorString("RED", "ERROR el tren exploto en la terminal: " + terminalActual));
        }
    }

    public void irAeropuerto() {
        lock.lock();
        try {
            System.out.println("El tren se esta moviendo al Aeropuerto ");
            terminalActual = 'Q';
            barreraEmbarque.reset();
            this.gruposTerminal = new int[3];
            this.capacidadActual = 0;
            this.flagEnViaje = false;
        } catch (Exception e) {
            System.out.println(Console.colorString("RED", "ERROR con el tren al querer ir al aeropuerto"));
        } finally {
            lock.unlock();
        }
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

}
