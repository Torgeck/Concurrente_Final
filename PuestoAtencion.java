import java.util.concurrent.Semaphore;
import java.util.concurrent.locks.ReentrantLock;

public class PuestoAtencion {

    private int capacidadMax;
    private int capacidadActual;
    private Semaphore colaDisponibilidad;
    private Semaphore pasajeroListo;
    private Semaphore mutexEmpleado;
    private ReentrantLock lock;
    private Empleado empleado;

    public PuestoAtencion(int max) {
        this.capacidadMax = max;
        this.capacidadActual = max;
        colaDisponibilidad = new Semaphore(capacidadMax, true);
        mutexEmpleado = new Semaphore(0);
        pasajeroListo = new Semaphore(0);
        empleado = new Empleado(this);
    }

    // Metodo empleado
    public void atenderPasajero() {
        // TODO cambiar condicion por horario de aeropuerto o poner la condicion en
        // Empleado
        try {
            System.out.println("Empleado " + this.empleado.getIdEmpleado() + " esperando pasajeros para atender");
            mostrador.acquire();

            System.out.println("== Atendiendo pasajero ==");
            // TODO ponerle un random
            Thread.sleep(1000);

            mutexEmpleado.release();
        } catch (Exception e) {
            // TODO: handle exception
        }
    }

    // Metodo pasajero

    public void esperarAtencion(Pasajero pasajero) {

        try {
            System.out.println("Pasajero " + pasajero.getIdPasajero() + " esperando en la cola");
            mostrador.acquire();
            mutexEmpleado.acquire();

            pasajeroListo.release();
        } catch (Exception e) {
            // TODO: handle exception
        }

    }

    public void entrarCola() {
        colaDisponibilidad.tryAcquire();
        try {
            lock.lock();
            capacidadActual--;
            lock.unlock();
        } catch (Exception e) {
            System.out.println("ERROR al querer disminuir capacidad actual en cola");
        }
    }
}
