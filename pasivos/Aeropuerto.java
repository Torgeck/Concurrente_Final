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
        this.hashPuestoAtencion = new HashMap<>();
        this.puestoInformes = new PuestoInformes(hashPuestoAtencion);
        this.hall = new Hall(this);
        this.guardia = new Guardia(this);
        this.hashTerminal = new HashMap<>();
        this.tren = tren;
        this.reloj = reloj;
        inicializarHashVuelos(empresas);
        generarTerminales();
        generarVuelos(empresas);
        generarPuestosAtencion(empresas, capacidadMax);
    }

    public Reloj getReloj() {
        return reloj;
    }

    public Hall getHall() {
        return this.hall;
    }

    public PuestoInformes getPuestoInformes() {
        return puestoInformes;
    }

    public HashMap<String, PuestoAtencion> getHashPuestoAtencion() {
        return hashPuestoAtencion;
    }

    public Guardia getGuardia() {
        return guardia;
    }

    public HashMap<Character, Terminal> getHashTerminal() {
        return hashTerminal;
    }

    public HashMap<String, Lista> getHashVuelos() {
        return hashVuelos;
    }

    public Tren getTren() {
        return tren;
    }

    public void generarPuestosAtencion(List<String> empresas, int capacidadMax) {
        PuestoAtencion puesto;
        for (String empresa : empresas) {
            puesto = new PuestoAtencion(this, empresa, capacidadMax, this.guardia.getWalkie());
            this.hashPuestoAtencion.put(empresa, puesto);
        }
    }

    public void generarTerminales() {
        hashTerminal.put('A', new Terminal(this, 'A', 1, 7));
        hashTerminal.put('B', new Terminal(this, 'B', 8, 15));
        hashTerminal.put('C', new Terminal(this, 'C', 16, 20));
    }

    public void generarVuelos(List<String> empresas) {
        int horaApertura = 6000;
        int espacio = 2000;
        int cantEmpresas = empresas.size();
        int cantVuelos = 13;
        Random rand = new Random();
        Vuelo vuelo;
        String empresa;
        Terminal terminal;
        for (int i = 1; i <= cantVuelos; i++) {
            horaApertura += espacio;
            empresa = empresas.get(rand.nextInt(cantEmpresas));
            terminal = getTerminalRandom();
            vuelo = new Vuelo(empresa, terminal.getIdTerminal(), terminal.getPuestoRandom(), horaApertura);
            agregarVuelo(vuelo);

        }
        System.out.println(Console.colorString("YELLOW", this.hashVuelos.toString()));
    }

    private void inicializarHashVuelos(List<String> listaEmpresas) {
        this.hashVuelos = new HashMap<>();
        for (String empresa : listaEmpresas) {
            hashVuelos.put(empresa, new Lista());
        }
    }

    public boolean agregarVuelo(Vuelo vuelo) {
        Lista lista = hashVuelos.get(vuelo.getAerolinea());
        return lista.insertarInicio(vuelo);
    }

    public Terminal getTerminalRandom() {
        Random rand = new Random();
        Terminal terminal = hashTerminal.get(arregloTerminales[rand.nextInt(3)]);
        return terminal;
    }
}
