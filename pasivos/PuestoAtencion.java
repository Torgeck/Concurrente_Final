package pasivos;

import java.util.Random;
import java.util.concurrent.Exchanger;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;

import console.Console;
import customExceptions.AeropuertoCerradoException;
import estructuras.arbolAVL.ArbolAVL;
import estructuras.lineales.Lista;
import hilos.Empleado;
import hilos.Pasajero;
import hilos.Reloj;

public class PuestoAtencion {

    private static int ID = 0;
    private int idPuesto;
    private int MAX;
    private boolean estaCerrado;
    private Aeropuerto aeropuerto;
    private String aerolinea;
    private ArbolAVL vuelosAerolinea;
    private Semaphore colaDisponibilidad;
    private Semaphore pasajeroListo;
    private Semaphore mutexEmpleado;
    private Semaphore mostrador;
    private Exchanger<Reserva> exchanger;
    private WalkieTalkie walkie;

    public PuestoAtencion(Aeropuerto aeropuerto, String aerolinea, int max, WalkieTalkie walkieGuardia) {
        this.aeropuerto = aeropuerto;
        this.aerolinea = aerolinea;
        this.MAX = max;
        this.idPuesto = generarID();
        this.estaCerrado = false;
        this.exchanger = new Exchanger<>();
        this.colaDisponibilidad = new Semaphore(this.MAX, true);
        this.mostrador = new Semaphore(1, true);
        this.mutexEmpleado = new Semaphore(0);
        this.pasajeroListo = new Semaphore(0);
        this.walkie = walkieGuardia;
        iniVuelosAerolinea(aeropuerto.getHashVuelos().get(aerolinea));
    }

    public String getAerolinea() {
        return this.aerolinea;
    }

    public Aeropuerto getAeropuerto() {
        return this.aeropuerto;
    }

    public int getIdPuesto() {
        return this.idPuesto;
    }

    private int generarID() {
        return ++ID;
    }

    public void iniVuelosAerolinea(Lista lista) {
        this.vuelosAerolinea = new ArbolAVL();
        Vuelo vuelo;
        if (!lista.esVacia()) {
            for (int i = 1; i <= lista.longitud(); i++) {
                vuelo = (Vuelo) lista.recuperar(i);
                this.vuelosAerolinea.insertar(vuelo.getHoraEmbarque(), vuelo);
            }
        }
    }

    public void abrirPuesto() {
        mutexEmpleado.drainPermits();
        this.estaCerrado = false;
    }

    // Metodos empleado
    public boolean atenderPasajero(Empleado empleado) {
        Vuelo vueloObtenido;
        Reserva reserva = null;
        boolean hayPasajeros = true;
        try {
            System.out.println(Console.colorString("PURPLE",
                    "Empleado " + empleado.getIdEmpleado() + " en puesto de atencion[" + this.aerolinea
                            + "] esperando pasajeros para atender"));
            hayPasajeros = mutexEmpleado.tryAcquire(4, TimeUnit.SECONDS);

            if (hayPasajeros) {
                vueloObtenido = obtenerVueloAleatorio(2);

                if (vueloObtenido != null) {
                    reserva = new Reserva(vueloObtenido);
                } else {
                    System.out.println(Console.colorString("RED", "NO HAY VUELOS DISPONIBLES EN " + this.aerolinea));
                }
                exchanger.exchange(reserva);
                System.out.println(Console.colorString("PURPLE",
                        "== Empleado[" + empleado.getIdEmpleado() + "] atendiendo pasajero =="));
            }
        } catch (Exception e) {
            System.out.println(Console.colorString("RED",
                    "ERROR con empleado al querer atender al pasajero " + this.idPuesto + e.getMessage()));
            e.printStackTrace();
        }
        return hayPasajeros;
    }

    public void liberarPasajero() {
        try {
            pasajeroListo.release();
        } catch (Exception e) {
            System.out.println(Console.colorString("RED", "ERROR con empleado al querer liberar al pasajero"));
        }
    }

    public Vuelo obtenerVueloAleatorio(int brechaTiempo) {
        Random random = new Random();

        int hora = Reloj.convertirHora(brechaTiempo);
        int tiempoBase = this.aeropuerto.getReloj().getTiempoActual() + hora;
        int tiempoMax = Reloj.convertirHora(22);
        Lista listaVuelos;
        Vuelo vueloObtenido = null;

        if (!vuelosAerolinea.esVacio()) {
            listaVuelos = vuelosAerolinea.listarValuesRango(tiempoBase, tiempoMax);
            if (!listaVuelos.esVacia()) {
                vueloObtenido = (Vuelo) listaVuelos.recuperar(random.nextInt(listaVuelos.longitud()) + 1);
            }
        }

        return vueloObtenido;
    }

    public boolean hayPasajerosEsperando() {
        return this.colaDisponibilidad.availablePermits() < this.MAX;
    }

    public void eliminarVuelosExpirados(long brechaTiempo) {
        Lista listaExpirados;
        Vuelo vuelo;

        System.out.println(Console.colorString("WHITE", "Eliminando vuelos expirados"));
        listaExpirados = vuelosAerolinea.listarRango(0, this.aeropuerto.getReloj().getTiempoActual() + brechaTiempo);

        while (!listaExpirados.esVacia()) {
            vuelo = (Vuelo) listaExpirados.recuperar(1);
            vuelosAerolinea.eliminar(vuelo.getHoraEmbarque());
            listaExpirados.eliminar(1);
        }
    }

    // Metodos pasajero
    public synchronized boolean entrarCola(Pasajero pasajero) throws AeropuertoCerradoException {
        boolean exito = true;
        this.aeropuerto.verificarAbierto();

        try {
            if (colaDisponibilidad.tryAcquire()) {
                System.out
                        .println(Console.colorString("GREEN",
                                "Pasajero " + pasajero.getIdPasajero() + " esperando en la cola en puesto de atencion["
                                        + this.aerolinea + "]"));
            } else {
                exito = false;
            }
        } catch (Exception e) {
            System.out.println(Console.colorString("RED", "ERROR con pasajero al querer entrar en cola"));
            exito = false;
        }

        return exito;
    }

    public Reserva esperarAtencion() throws AeropuertoCerradoException {
        Reserva reserva = null;

        this.aeropuerto.verificarAbierto();
        try {
            // Espero a que el empleado este desocupado
            mostrador.acquire();
            mutexEmpleado.release();
            // Obtengo mis datos de vuelo
            reserva = this.exchanger.exchange(null);
        } catch (Exception e) {
            System.out.println(Console.colorString("RED", "Le avisaron al pasajero que cerro el Aeropuerto"));
        }
        return reserva;
    }

    public void salirPuestoAtencion(Pasajero pasajero) {

        try {
            // Se queda bloqueado hasta que lo libere empleado
            pasajeroListo.acquire();
            System.out.println(Console.colorString("GREEN",
                    "Pasajero " + pasajero.getIdPasajero() + " saliendo del puesto de atencion["
                            + this.aerolinea + "]"));
            // Es liberado entonces libera el mostrador
            mostrador.release();
            colaDisponibilidad.release();
            hayLugarCola();

        } catch (Exception e) {
            System.out.println(Console.colorString("RED", "ERROR con pasajero al salir de puesto de atencion"));
        }
    }

    // Metodos guardia
    public synchronized void hayLugarCola() {
        try {
            walkie.notificarGuardia();
        } catch (Exception e) {
            System.out.println(Console.colorString("RED", "ERROR avisar lugar disponible a guardia"));
        }
    }

}
