package pasivos;

import java.util.HashMap;
import java.util.List;
import java.util.Random;

import console.Console;
import estructuras.lineales.Lista;
import hilos.Guardia;
import hilos.Reloj;

public class Aeropuerto {

    private static final char[] arregloTerminales = {'A', 'B', 'C'};
    private Reloj reloj;
    private PuestoInformes puestoInformes;
    private HashMap<String, PuestoAtencion> hashPuestoAtencion;
    private Hall hall;
    private Guardia guardia;
    private HashMap<Character, Terminal> hashTerminal;
    private HashMap<String, Lista> hashVuelos;
    private Tren tren;

    public Aeropuerto(List<String> empresas, int capacidadMax, Tren tren, Reloj reloj) {
        // generar aeropuerto
        this.reloj = reloj;
        this.hashPuestoAtencion = new HashMap<>();
        this.puestoInformes = new PuestoInformes(hashPuestoAtencion);
        this.hall = new Hall();
        this.guardia = new Guardia(this);
        this.hashTerminal = new HashMap<>();
        this.tren = tren;
        inicializarHashVuelos(empresas);
        generarTerminales();
        generarVuelos(empresas);
        generarPuestosAtencion(empresas, capacidadMax);
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

    public Tren getTren() {
        return this.tren;
    }

    public Reloj getReloj() {
        return this.reloj;
    }

    public void generarPuestosAtencion(List<String> empresas, int capacidadMax) {
        PuestoAtencion puesto;
        for (String empresa : empresas) {
            puesto = new PuestoAtencion(this, empresa, capacidadMax, this.guardia.getWalkie());
            this.hashPuestoAtencion.put(empresa, puesto);
        }
    }

    public void generarTerminales() {
        int capMax = 15;
        int cantCajas = 2;
        hashTerminal.put('A', new Terminal(this, this.reloj, 'A', 1, 7, cantCajas, capMax));
        hashTerminal.put('B', new Terminal(this, this.reloj, 'B', 8, 15, cantCajas, capMax));
        hashTerminal.put('C', new Terminal(this, this.reloj, 'C', 16, 20, cantCajas, capMax));
    }

    public void generarVuelos(List<String> empresas) {
        int hora = 10;
        int espacio = 30;
        int cantEmpresas = empresas.size();
        int cantVuelos = 5; // 20
        Random rand = new Random();
        Vuelo vuelo;
        String empresa;
        Terminal terminal;
        PuestoEmbarque puestoEmb;
        hora = Reloj.convertirHora(hora, espacio);

        for (int i = 1; i <= cantVuelos; i++) {
            empresa = empresas.get(rand.nextInt(cantEmpresas));
            terminal = getTerminalRandom();
            puestoEmb = terminal.getPuestoRandom();
            vuelo = new Vuelo(empresa, terminal, puestoEmb, hora);
            agregarVuelo(vuelo, puestoEmb);
            hora = Reloj.addMin(hora, espacio);
        }
        System.out.println(Console.colorString("YELLOW", this.hashVuelos.toString()));
    }

    private void inicializarHashVuelos(List<String> listaEmpresas) {
        this.hashVuelos = new HashMap<>();
        for (String empresa : listaEmpresas) {
            hashVuelos.put(empresa, new Lista());
        }
    }

    public void agregarVuelo(Vuelo vuelo, PuestoEmbarque puestoEmb) {
        Lista lista = hashVuelos.get(vuelo.getAerolinea());
        puestoEmb.addVuelo(vuelo);
        lista.insertarInicio(vuelo);
    }

    public Terminal getTerminalRandom() {
        Random rand = new Random();
        Terminal terminal = hashTerminal.get(arregloTerminales[rand.nextInt(3)]);
        return terminal;
    }

    public synchronized boolean estaAbiertoAlPublico() {
        int horaActual = this.reloj.getTime();
        return (horaActual < 2200) && (horaActual >= 6);
    }

    public synchronized void avisarApertura() {
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
}
