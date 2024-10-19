import java.util.concurrent.Semaphore;
import java.util.concurrent.locks.ReentrantLock;

public class PuestoAtencion {

    private static int ID = 0;
    private int idPuesto;
    private int capacidadMax;
    private int capacidadActual;
    private Semaphore colaDisponibilidad;
    private Semaphore pasajeroListo;
    private Semaphore mutexEmpleado;
    private Semaphore mostrador;
    private ReentrantLock lock;
    private Empleado empleado;

    public PuestoAtencion(int max) {
        this.idPuesto = generarID();
        this.capacidadMax = max;
        this.capacidadActual = max;
        this.colaDisponibilidad = new Semaphore(capacidadMax, true);
        this.mostrador = new Semaphore(1);
        this.mutexEmpleado = new Semaphore(0);
        this.pasajeroListo = new Semaphore(0);
        this.empleado = new Empleado(this);
    }

    private int generarID() {
        return ++ID;
    }

    // Metodo empleado
    public void atenderPasajero() {
        // TODO cambiar condicion por horario de aeropuerto o poner la condicion en
        // Empleado
        try {
            System.out.println("Empleado " + this.empleado.getIdEmpleado() + " en puesto de atencion[" + this.idPuesto
                    + "] esperando pasajeros para atender");
            mutexEmpleado.acquire();

            System.out.println("== Atendiendo pasajero ==");
            // TODO ponerle un random y asignarle puerta y terminal a pasajero
            Thread.sleep(1000);

            pasajeroListo.release();
        } catch (Exception e) {
            // TODO: handle exception
        }
    }

    // Metodo pasajero

    public void esperarAtencion(Pasajero pasajero) {

        try {
            System.out.println("Pasajero " + pasajero.getIdPasajero() + " esperando en la cola en puesto de atencion["
                    + this.idPuesto + "]");
            colaDisponibilidad.acquire();
            // Disminuyo el espacio actual
            ocuparLugarCola();
            // Espero a que el empleado este desocupado
            mostrador.acquire();
            mutexEmpleado.release();
        } catch (Exception e) {
            System.out.println("ERROR con pasajero en la cola de puesto de atencion");
        }
    }

    public void salirPuestoAtencion(Pasajero pasajero) {

        try {
            // Se queda bloqueado hasta que lo libere empleado
            pasajeroListo.acquire();
            System.out.println("Pasajero " + pasajero.getIdPasajero() + " saliendo del puesto de atencion["
                    + this.idPuesto + "]");
            // Es liberado entonces libera el mostrador y un espacio cola
            liberarLugarCola();
            mostrador.release();

        } catch (Exception e) {
            // TODO: handle exception
        }
    }

    private void ocuparLugarCola() {
        try {
            lock.lock();
            this.capacidadActual--;
            System.out.println("Capacidad actual de la cola en el puesto[" + this.idPuesto + "] es de ["
                    + this.capacidadActual + "]");
            lock.unlock();
        } catch (Exception e) {
            System.out.println("ERROR al disminuir capacidad de cola");
        }
    }

    private void liberarLugarCola() {
        try {
            lock.lock();
            this.capacidadActual++;
            System.out.println("Capacidad actual de la cola en el puesto[" + this.idPuesto + "] es de ["
                    + this.capacidadActual + "]");
            lock.unlock();
        } catch (Exception e) {
            System.out.println("ERROR al aumentar capacidad de cola");
        }
    }

    // Metodo Guardia
    public int hayLugarCola() {
        int capacidad = -1;
        try {
            lock.lock();
            capacidad = this.capacidadActual;
            lock.unlock();
        } catch (Exception e) {
            System.out.println("ERROR al adquirir espacio libre en cola");
        }
        return capacidad;
    }

}
