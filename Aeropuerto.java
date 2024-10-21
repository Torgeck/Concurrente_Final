import java.util.HashMap;
import java.util.Random;

public class Aeropuerto {

    // private Reloj reloj;
    private PuestoInformes puestoInformes;
    private HashMap<String, PuestoAtencion> hashPuestoAtencion;
    private Hall hall;
    private Guardia guardia;
    // private Colectivo cole;
    // private HashMap<Character, Terminal> hashTerminal;

    public Aeropuerto(int cantPuestos, int capacidadMax) {
        // generar aeropuerto
        this.hashPuestoAtencion = new HashMap<String, PuestoAtencion>();
        this.puestoInformes = new PuestoInformes(hashPuestoAtencion);
        this.hall = new Hall(this);
        this.guardia = new Guardia(this);
        generarPuestosAtencion(cantPuestos, capacidadMax);
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
            puesto = new PuestoAtencion(capacidadMax, this.guardia.getWalkie());
            this.hashPuestoAtencion.put("Empresa[" + i + "]", puesto);
        }
    }

    private void generarPasajero() {
        Random rand = new Random();
        new Thread(new Pasajero(this, "Empresa[" + rand.nextInt(hashPuestoAtencion.size()) + "]")).start();
    }
}
