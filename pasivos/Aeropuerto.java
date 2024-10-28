package pasivos;

import java.util.HashMap;
import java.util.List;
import java.util.Random;
import estructuras.lineales.Lista;
import hilos.Guardia;
import hilos.Reloj;

public class Aeropuerto {

    private Reloj reloj;
    private PuestoInformes puestoInformes;
    private HashMap<String, PuestoAtencion> hashPuestoAtencion;
    private Hall hall;
    private Guardia guardia;
    private HashMap<Character, Terminal> hashTerminal;
    private HashMap<String, Lista> hashVuelos;
    private static final char[] arregloTerminales = { 'A', 'B', 'C' };

    public Aeropuerto(List<String> empresas, int capacidadMax) {
        // generar aeropuerto
        this.hashPuestoAtencion = new HashMap<>();
        this.puestoInformes = new PuestoInformes(hashPuestoAtencion);
        this.hall = new Hall(this);
        this.guardia = new Guardia(this);
        this.hashTerminal = new HashMap<>();
        inicializarHashVuelos(empresas);
        generarPuestosAtencion(empresas, capacidadMax);
        generarTerminales();
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

    public void generarPuestosAtencion(List<String> empresas, int capacidadMax) {
        PuestoAtencion puesto;
        for (int i = 0; i < empresas.size(); i++) {
            puesto = new PuestoAtencion(this, empresas.get(i), capacidadMax, this.guardia.getWalkie());
            this.hashPuestoAtencion.put(empresas.get(i), puesto);
        }
    }

    public void generarTerminales() {
        hashTerminal.put('A', new Terminal(this, 'A', 1, 7));
        hashTerminal.put('B', new Terminal(this, 'B', 8, 15));
        hashTerminal.put('C', new Terminal(this, 'C', 16, 20));
    }

    private void inicializarHashVuelos(List<String> listaEmpresas) {
        for (String empresa : listaEmpresas) {
            hashVuelos.put(empresa, new Lista());
        }
    }

    public boolean agregarVuelo(Vuelo vuelo) {
        Lista lista = hashVuelos.get(vuelo.getAerolinea());
        return lista.esVacia() ? false : lista.insertarInicio(vuelo);
    }

    public Terminal getTerminalRandom() {
        Random rand = new Random();
        Terminal terminal = hashTerminal.get(arregloTerminales[rand.nextInt(3)]);
        return terminal;
    }
}
