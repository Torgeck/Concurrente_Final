package pasivos;

import console.Console;

import java.util.concurrent.Semaphore;
import java.util.concurrent.locks.ReentrantLock;

public class Caja {

    private static int ID = 0;
    private String idCaja;
    private int cantClientes;
    private ReentrantLock lock;
    private Semaphore mutexCajero;
    private Semaphore mutexCliente;
    private Semaphore mutexAtencion;

    public Caja(char idTerminal) {
        this.idCaja = idTerminal + Integer.toString(generarID());
        this.lock = new ReentrantLock();
        this.mutexCajero = new Semaphore(0);
        this.mutexCliente = new Semaphore(0);
        this.mutexAtencion = new Semaphore(1, true);
        this.cantClientes = 0;
    }

    private int generarID() {
        return ID++;
    }

    public String getIdCaja() {
        return idCaja;
    }

    public void liberarCajero() {
        mutexCajero.release();
        mutexCliente.drainPermits();
    }

    private void modificarCola(int x) {
        lock.lock();
        try {
            cantClientes += x;
        } catch (Exception e) {
            System.out.println(Console.colorString("RED", "ERROR al querer aumentar cola de CAJERO"));
        } finally {
            lock.unlock();
        }
    }

    public int getCantClientes() {
        lock.lock();
        int respuesta = -1;
        try {
            respuesta = cantClientes;
        } catch (Exception e) {
            System.out.println(Console.colorString("RED", "ERROR al adquirir cantidad de cola de CAJERO"));
        } finally {
            lock.unlock();
        }
        return respuesta;
    }

    // Metodos Cliente
    public void esperarAtencion(String pasajero) {
        modificarCola(1);
        try {
            System.out.println(Console.colorString("PURPLE", "Pasajero " + pasajero + " esperando en fila caja " + this.idCaja));
            mutexAtencion.acquire();
            mutexCajero.release();
        } catch (InterruptedException e) {
            System.out.println(Console.colorString("RED", "ERROR al esperar atencion en caja " + this.idCaja));
        }
    }

    public void salirCaja(String pasajero) {
        try {
            Console.colorString("PURPLE", "Pasajero " + pasajero + " esperando cobro cajero " + this.idCaja);
            mutexCliente.acquire();
            modificarCola(-1);
            mutexAtencion.release();
        } catch (InterruptedException e) {
            System.out.println(Console.colorString("RED", "ERROR al salir en caja " + this.idCaja));
        }
    }

    // Metodos Cajero
    public void atenderCliente() {
        try {
            mutexCajero.acquire();
        } catch (InterruptedException e) {
            System.out.println(Console.colorString("RED", "ERROR al atender cliente en caja " + this.idCaja));
        }
    }

    public void liberarCliente() {
        try {
            mutexCliente.release();
        } catch (Exception e) {
            System.out.println(Console.colorString("RED", "ERROR al liberar cliente en caja " + this.idCaja));
        }
    }


}
