package pasivos;

import console.Console;

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
            System.out.println(Console.colorString("RED", "ERROR al querer poner alarma"));
        } finally {
            lock.unlock();
        }
    }

    public void avisarPasajerosEmbarque() {
        lock.lock();
        try {
            vuelos.removeFirst();
            esperarEmbarque.signalAll();
        } catch (NoSuchElementException e) {
            System.out.println(Console.colorString("RED", "ERROR al querer sacar elemeto de cola vuelo"));
            esperarEmbarque.signalAll();
        } finally {
            lock.unlock();
        }
    }

    public void esperarEmbarque(Reserva reserva) {
        lock.lock();
        try {
            while (!this.vueloActual.getIdVuelo().equals(reserva.getVueloID()) || (this.terminal.getTiempoActual() != reserva.getHoraEmbarque())) {
                esperarEmbarque.await();
            }
        } catch (Exception e) {
            System.out.println(Console.colorString("RED", "ERROR al esperar embarque"));
        } finally {
            lock.unlock();
        }
    }

}
