package pasivos;

import java.util.Random;
import java.util.concurrent.Exchanger;
import java.util.concurrent.Semaphore;

import estructuras.arbolAVL.ArbolAVL;
import estructuras.lineales.Lista;
import hilos.Empleado;
import hilos.Pasajero;

public class PuestoAtencion {

    private static int ID = 0;
    private int idPuesto;
    private String aerolinea;
    private Aeropuerto aeropuerto;
    private ArbolAVL vuelosAerolinea;
    private Semaphore colaDisponibilidad;
    private Semaphore pasajeroListo;
    private Semaphore mutexEmpleado;
    private Semaphore mostrador;
    private Exchanger<Reserva> exchanger;
    private Empleado empleado;
    private WalkieTalkie walkie;

    public PuestoAtencion(Aeropuerto aeropuerto, String aerolinea, int max, WalkieTalkie walkieGuardia) {
        this.aeropuerto = aeropuerto;
        this.aerolinea = aerolinea;
        this.vuelosAerolinea = new ArbolAVL();
        this.idPuesto = generarID();
        this.exchanger = new Exchanger<Reserva>();
        this.colaDisponibilidad = new Semaphore(max, true);
        this.mostrador = new Semaphore(1, true);
        this.mutexEmpleado = new Semaphore(0);
        this.pasajeroListo = new Semaphore(0);
        this.walkie = walkieGuardia;
    }

    public int getIdPuesto() {
        return this.idPuesto;
    }

    public void setEmpleado(Empleado empleado) {
        this.empleado = empleado;
    }

    private int generarID() {
        return ++ID;
    }

    public void llenarArbolVuelos(Lista lista) {
        Vuelo vuelo;
        for (int i = 0; i < lista.longitud(); i++) {
            vuelo = (Vuelo) lista.recuperar(i);
            this.vuelosAerolinea.insertar(vuelo.getHoraEmbarque(), vuelo);
        }
    }

    // Metodos empleado
    public void atenderPasajero() {
        try {
            System.out.println("Empleado " + this.empleado.getIdEmpleado() + " en puesto de atencion[" + this.idPuesto
                    + "] esperando pasajeros para atender");
            mutexEmpleado.acquire();

            Reserva reserva = new Reserva(obtenerVueloAleatorio(2000));
            exchanger.exchange(reserva);

            System.out.println("== Empleado[" + this.empleado.getIdEmpleado() + "] atendiendo pasajero ==");
        } catch (Exception e) {
            System.out.println("ERROR con empleado al querer atender al pasajero " + this.idPuesto
                    + e.getMessage());
        }
    }

    public void liberarPasajero() {
        try {
            eliminarVuelosExpirados(2000);
            pasajeroListo.release();
        } catch (Exception e) {
            System.out.println("ERROR con empleado al querer liberar al pasajero");
        }
    }

    public Vuelo obtenerVueloAleatorio(long brechaTiempo) {
        Random random = new Random();
        Lista listaVuelos;
        Vuelo vueloObtenido = null;

        if (!vuelosAerolinea.esVacio()) {
            listaVuelos = vuelosAerolinea.listarRango(brechaTiempo, brechaTiempo * 5);
            vueloObtenido = (Vuelo) listaVuelos.recuperar(random.nextInt(listaVuelos.longitud()) + 1);
        }

        return vueloObtenido;
    }

    public void eliminarVuelosExpirados(long brechaTiempo) {
        Lista listaExpirados;
        Vuelo vuelo;

        listaExpirados = vuelosAerolinea.listarRango(0, brechaTiempo);

        while (!listaExpirados.esVacia()) {
            vuelo = (Vuelo) listaExpirados.recuperar(1);
            vuelosAerolinea.eliminar(vuelo.getHoraEmbarque());
            listaExpirados.eliminar(1);
        }
    }

    // Metodos pasajero
    public synchronized boolean entrarCola(Pasajero pasajero) {
        boolean exito = true;

        try {
            if (colaDisponibilidad.tryAcquire()) {
                System.out
                        .println("Pasajero " + pasajero.getIdPasajero() + " esperando en la cola en puesto de atencion["
                                + this.idPuesto + "]");
            } else {
                exito = false;
            }
        } catch (Exception e) {
            System.out.println("ERROR con pasajero al querer entrar en cola");
        }

        return exito;
    }

    public void esperarAtencion(Reserva reserva) {
        try {
            // Espero a que el empleado este desocupado
            mostrador.acquire();
            mutexEmpleado.release();
            // Obtengo mis datos de vuelo
            reserva = this.exchanger.exchange(null);
        } catch (Exception e) {
            System.out.println("ERROR con pasajero en el mostrador");
        }

    }

    public void salirPuestoAtencion(Pasajero pasajero) {

        try {
            // Se queda bloqueado hasta que lo libere empleado
            pasajeroListo.acquire();
            System.out.println("Pasajero " + pasajero.getIdPasajero() + " saliendo del puesto de atencion["
                    + this.idPuesto + "] con reserva: " + pasajero.getReserva().toString());
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
