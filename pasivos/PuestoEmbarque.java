package pasivos;

import console.Console;
import customExceptions.AeropuertoCerradoException;

import java.util.LinkedList;
import java.util.NoSuchElementException;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class PuestoEmbarque {

    private int idPuesto;
    private Terminal terminal;
    private LinkedList<Vuelo> vuelos;
    private Vuelo vueloActual;
    private Lock lock;
    private Condition esperarAlarma;
    private Condition esperarEmbarque;

    public PuestoEmbarque(int idPuesto, Terminal terminal) {
        this.idPuesto = idPuesto;
        this.terminal = terminal;
        this.lock = new ReentrantLock();
        this.esperarAlarma = lock.newCondition();
        this.esperarEmbarque = lock.newCondition();
        this.vuelos = new LinkedList<>();
    }

    public int getIdPuesto() {
        return idPuesto;
    }

    public Terminal getTerminal() {
        return terminal;
    }

    public Vuelo getVueloActual() {
        return this.vueloActual;
    }

    public void addVuelo(Vuelo vuelo) {
        vuelos.add(vuelo);
    }

    public void actualizarVuelo() {
        vueloActual = this.vuelos.getFirst();
    }

    // Metodos empleadoEmbarque
    public boolean hayVuelos() {
        return !vuelos.isEmpty();
    }

    public void sonarAlarma() {
        lock.lock();
        try {
            esperarAlarma.signal();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            lock.unlock();
        }
    }

    public void ponerAlarma() {
        lock.lock();
        try {
            this.terminal.agregarAlarma(new Alarma(vuelos.getFirst().getHoraEmbarque(), this));
            esperarAlarma.await();
        } catch (Exception e) {
            System.out.println(Console.colorString("RED", "ERROR al esperar alarma en puesto embarque"));
        } finally {
            lock.unlock();
        }
    }

    public void avisarPasajerosEmbarque() {
        lock.lock();
        try {
            vuelos.removeFirst();
        } catch (NoSuchElementException e) {
            System.out.println(
                    Console.colorString("RED", "ERROR al querer sacar elemento de cola vuelo en puesto de embarque"));
        } finally {
            esperarEmbarque.signalAll();
            lock.unlock();
        }
    }

    public void avisarPasajerosCierre() {
        lock.lock();
        try {
            esperarEmbarque.signalAll();
        } finally {
            lock.unlock();
        }
    }

    public boolean esperarEmbarque(Reserva reserva) throws AeropuertoCerradoException {
        boolean embarco = false;
        lock.lock();
        try {
            while (!embarco) {
                this.terminal.getAeropuerto().verificarAbierto();

                if (reserva.getHoraEmbarque() > this.terminal.getTiempoActual()) {
                    esperarEmbarque.await();
                } else if (reserva.getHoraEmbarque() == this.terminal.getTiempoActual()
                        && this.vueloActual.getIdVuelo().equals(reserva.getVueloID())) {
                    embarco = true;
                } else {
                    // Perdio el vuelo
                    throw new AeropuertoCerradoException("Aeropuerto cerrado");
                }
            }
        } catch (InterruptedException e) {
            System.out.println(Console.colorString("RED", "ERROR al esperar embarque"));
        } finally {
            lock.unlock();
        }
        return embarco;
    }

    public void esperarCierre() {
        this.terminal.esperarCierre();
    }

    public void liberarEmpleado() {
        lock.lock();
        try {
            esperarAlarma.signal();
        } catch (Exception e) {
            System.out.println("ERROR al querer liberar empleadoEmbarque");
        } finally {
            lock.unlock();
        }
    }

}
