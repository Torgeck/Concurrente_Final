package pasivos;

import java.util.HashMap;
import java.util.Random;

import hilos.Guardia;
import hilos.Pasajero;
import hilos.Reloj;

public class Aeropuerto {

    private Reloj reloj;
    private PuestoInformes puestoInformes;
    private HashMap<String, PuestoAtencion> hashPuestoAtencion;
    private Hall hall;
    private Guardia guardia;
    private HashMap<Character, Terminal> hashTerminal;
    // private Colectivo cole;

    public Aeropuerto(int cantPuestos, int capacidadMax) {
        // generar aeropuerto
        this.hashPuestoAtencion = new HashMap<>();
        this.puestoInformes = new PuestoInformes(hashPuestoAtencion);
        this.hall = new Hall(this);
        this.guardia = new Guardia(this);
        this.hashTerminal = new HashMap<>();
        generarPuestosAtencion(cantPuestos, capacidadMax);
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

    public void generarPuestosAtencion(int cantPuestos, int capacidadMax) {
        PuestoAtencion puesto;
        for (int i = 0; i < cantPuestos; i++) {
            puesto = new PuestoAtencion(this, capacidadMax, this.guardia.getWalkie());
            this.hashPuestoAtencion.put("Empresa[" + i + "]", puesto);
        }
    }

    public void generarTerminales() {
        hashTerminal.put('A', new Terminal(this, 'A', 1, 7));
        hashTerminal.put('B', new Terminal(this, 'B', 8, 15));
        hashTerminal.put('C', new Terminal(this, 'C', 16, 20));
    }

    private void generarPasajero() {
        Random rand = new Random();
        new Thread(new Pasajero(this, "Empresa[" + rand.nextInt(hashPuestoAtencion.size()) + "]")).start();
    }

    public Terminal obtenerTerminalRandom() {
        
    }

}
