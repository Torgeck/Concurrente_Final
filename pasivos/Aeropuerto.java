package pasivos;

import java.util.HashMap;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicBoolean;

import console.Console;
import customExceptions.AeropuertoCerradoException;
import estructuras.lineales.Lista;
import hilos.Guardia;
import hilos.Reloj;

public class Aeropuerto {

    private static final char[] ARREGLO_TERMINALES = {'A', 'B', 'C'};
    private static final int CANT_VUELOS = 24;
    private static final int HORA_INICIAL = 10;
    private static final int ESPACIO = 30;
    private static final int CAP_MAXIMA = 15;
    private static final int CANT_CAJAS = 2;
    private Reloj reloj;
    private PuestoInformes puestoInformes;
    private HashMap<String, PuestoAtencion> hashPuestoAtencion;
    private Hall hall;
    private Guardia guardia;
    private HashMap<Character, Terminal> hashTerminal;
    private HashMap<String, Lista> hashVuelos;
    private List<String> empresas;
    private Tren tren;
    private AtomicBoolean flagFinSimulacion;
    private AtomicBoolean flagCerrado;

    public Aeropuerto(List<String> empresas, int capMaxPuestos, Reloj reloj, int capMaxTren) {
        // generar aeropuerto
        this.reloj = reloj;
        this.empresas = empresas;
        this.hashPuestoAtencion = new HashMap<>();
        this.puestoInformes = new PuestoInformes(hashPuestoAtencion);
        this.hall = new Hall();
        this.guardia = new Guardia(this);
        this.hashTerminal = new HashMap<>();
        this.tren = new Tren(this, capMaxTren);
        this.flagFinSimulacion = new AtomicBoolean(false);
        this.flagCerrado = new AtomicBoolean(false);
        inicializarHashVuelos();
        generarTerminales();
        generarVuelos();
        generarPuestosAtencion(empresas, capMaxPuestos);
    }

    public Hall getHall() {
        return this.hall;
    }

    public PuestoInformes getPuestoInformes() {
        return this.puestoInformes;
    }

    public HashMap<String, PuestoAtencion> getHashPuestoAtencion() {
        return this.hashPuestoAtencion;
    }

    public Guardia getGuardia() {
        return this.guardia;
    }

    public HashMap<Character, Terminal> getHashTerminal() {
        return this.hashTerminal;
    }

    public HashMap<String, Lista> getHashVuelos() {
        return this.hashVuelos;
    }

    public List<String> getEmpresas() {
        return this.empresas;
    }

    public Tren getTren() {
        return this.tren;
    }

    public Reloj getReloj() {
        return this.reloj;
    }

    public boolean getFlagSimulacion() {
        return this.flagFinSimulacion.get();
    }

    public void cambiarFlagCerrado(boolean nuevaFlag) {
        this.flagCerrado.set(nuevaFlag);
    }

    public void generarPuestosAtencion(List<String> empresas, int capacidadMax) {
        PuestoAtencion puesto;
        for (String empresa : empresas) {
            puesto = new PuestoAtencion(this, empresa, capacidadMax, this.hall);
            this.hashPuestoAtencion.put(empresa, puesto);
        }
    }

    public void generarTerminales() {
        hashTerminal.put('A', new Terminal(this, 'A', 1, 7, CANT_CAJAS, CAP_MAXIMA));
        hashTerminal.put('B', new Terminal(this, 'B', 8, 15, CANT_CAJAS, CAP_MAXIMA));
        hashTerminal.put('C', new Terminal(this, 'C', 16, 20, CANT_CAJAS, CAP_MAXIMA));
    }

    public void generarVuelos() {
        int hora = HORA_INICIAL;
        int cantEmpresas = this.empresas.size();
        Random rand = new Random();
        Vuelo vuelo;
        String empresa;
        Terminal terminal;
        PuestoEmbarque puestoEmb;
        hora = Reloj.convertirHora(hora, ESPACIO);

        for (int i = 1; i <= CANT_VUELOS; i++) {
            empresa = this.empresas.get(rand.nextInt(cantEmpresas));
            terminal = getTerminalRandom();
            puestoEmb = terminal.getPuestoRandom();
            vuelo = new Vuelo(empresa, terminal, puestoEmb, hora);
            agregarVuelo(vuelo, puestoEmb);
            hora = Reloj.addMin(hora, ESPACIO);
        }
        System.out.println(Console.colorString("YELLOW", this.hashVuelos.toString()));
    }

    private void inicializarHashVuelos() {
        this.hashVuelos = new HashMap<>();
        for (String empresa : this.empresas) {
            hashVuelos.put(empresa, new Lista());
        }
    }

    public void agregarVuelo(Vuelo vuelo, PuestoEmbarque puestoEmb) {
        Lista lista = hashVuelos.get(vuelo.getAerolinea());
        puestoEmb.addVuelo(vuelo);
        lista.insertarInicio(vuelo);
    }

    public int getHora() {
        return this.reloj.getTiempoActual();
    }

    public void agregarAlarma(Alarma alarma) {
        this.reloj.agregarAlarma(alarma);
    }

    public Terminal getTerminalRandom() {
        Random rand = new Random();
        return hashTerminal.get(ARREGLO_TERMINALES[rand.nextInt(3)]);
    }

    public synchronized boolean estaCerradoAlPublico() {
        return this.flagFinSimulacion.get() || this.flagCerrado.get();
    }

    public void verificarAbierto() throws AeropuertoCerradoException {
        if (estaCerradoAlPublico()) {
            throw new AeropuertoCerradoException("El aeropuerto esta cerrado");
        }
    }

    public synchronized void avisarApertura() {
        nuevoDia();
        notifyAll();
    }

    public synchronized void avisarCierre() {
        notifyAll();
    }

    public synchronized void esperarApertura() {
        try {
            wait();
        } catch (InterruptedException e) {
            System.out.println(Console.colorString("RED", "ERROR al esperar apertura"));
            e.printStackTrace();
        }
    }

    private void nuevoDia() {
        inicializarHashVuelos();
        generarVuelos();
        this.hashTerminal.forEach((k, terminal) -> terminal.nuevoDia());
    }

    private void liberarConductor() {
        this.tren.liberarConductor();
    }

    public void cerrarPermanente() {
        this.flagFinSimulacion.set(true);
        avisarCierre();
        cerrarAeropuerto();
    }

    public void cerrarAeropuerto() {
        liberarConductor();
        liberarEmpleadosEmbarque();
    }

    private void liberarEmpleadosEmbarque() {
        this.hashTerminal.forEach((k, terminal) -> terminal.liberarEmpleadosEmbarque());
    }

}
