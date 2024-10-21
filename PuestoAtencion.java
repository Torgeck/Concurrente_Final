import java.util.concurrent.Semaphore;
import java.util.concurrent.ArrayBlockingQueue;

public class PuestoAtencion {

    private static int ID = 0;
    private int idPuesto;
    private int capacidadMax;
    private Semaphore colaDisponibilidad;
    private Semaphore pasajeroListo;
    private Semaphore mutexEmpleado;
    private Semaphore mostrador;
    private Empleado empleado;
    private WalkieTalkie walkie;

    public PuestoAtencion(int max, WalkieTalkie walkieGuardia) {
        this.idPuesto = generarID();
        this.capacidadMax = max;
        this.colaDisponibilidad = new Semaphore(max, true);
        this.mostrador = new Semaphore(1);
        this.mutexEmpleado = new Semaphore(0);
        this.pasajeroListo = new Semaphore(0);
        this.walkie = walkieGuardia;
    }

    public int getIdPuesto() {
        return this.idPuesto;
    }

    public boolean setEmpleado(Empleado empleado) {
        this.empleado = empleado;
        return true;
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
            System.out.println("== Empleado[" + this.empleado.getIdEmpleado() + "] atendiendo pasajero ==");
        } catch (Exception e) {
            System.out.println("ERROR con empleado al querer atender al pasajero " + this.idPuesto
                    + e.getMessage());
        }
    }

    public void liberarPasajero() {
        try {
            pasajeroListo.release();
        } catch (Exception e) {
            System.out.println("ERROR con empleado al querer liberar al pasajero");
        }
    }

    // Metodo pasajero
    public synchronized boolean entrarCola(Pasajero pasajero) {
        boolean exito = true;

        try {
            if (colaDisponibilidad.tryAcquire()) {
                System.out
                        .println("Pasajero " + pasajero.getIdPasajero() + " esperando en la cola en puesto de atencion["
                                + this.idPuesto + "]");
                // Disminuyo el espacio actual
            } else {
                exito = false;
            }
        } catch (Exception e) {
            System.out.println("ERROR con pasajero al querer entrar en cola");
        }

        return exito;
    }

    public void esperarAtencion(Pasajero pasajero) {

        try {
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
            // Es liberado entonces libera el mostrador
            mostrador.release();
            colaDisponibilidad.release();
            hayLugarCola();

        } catch (Exception e) {
            System.out.println("ERROR con pasajero al salir de puesto de atencion");
        }
    }

    // Metodos guardia
    public synchronized void hayLugarCola() {
        try {
            walkie.notificarGuardia();
        } catch (Exception e) {
            System.out.println("ERROR avisar lugar disponible a guardia");
        }
    }

}
