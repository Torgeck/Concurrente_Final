package pasivos;

import console.Console;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

public class Terminal {

    private Aeropuerto aeropuerto;
    private char idTerminal;
    private Freeshop freeshop;
    private ArrayList<Integer> arrayPuestosEmbarques;
    private HashMap<Integer, PuestoEmbarque> mapPuestoEmbarques;
    private Random random;

    public Terminal(Aeropuerto aeropuerto, char idTerminal, int limiteInferior, int limiteSuperior, int cantCajas, int capMax) {
        this.aeropuerto = aeropuerto;
        this.idTerminal = idTerminal;
        this.mapPuestoEmbarques = new HashMap<Integer, PuestoEmbarque>();
        this.arrayPuestosEmbarques = new ArrayList<>();
        this.random = new Random();
        this.freeshop = new Freeshop(idTerminal, cantCajas, capMax);
        generarPuestoEmbarque(limiteInferior, limiteSuperior);
    }

    public char getId() {
        return idTerminal;
    }

    public Freeshop getFreeshop() {
        return freeshop;
    }

    public ArrayList<Integer> getArrayPuestosEmbarques() {
        return arrayPuestosEmbarques;
    }

    public HashMap<Integer, PuestoEmbarque> getMapPuestoEmbarques() {
        return mapPuestoEmbarques;
    }

    public void generarPuestoEmbarque(int limiteInferior, int limiteSuperior) {
        for (int i = limiteInferior; i <= limiteSuperior; i++) {
            this.mapPuestoEmbarques.put(i, new PuestoEmbarque(i, this));
            arrayPuestosEmbarques.add(i);
        }
    }

    public PuestoEmbarque getPuestoRandom() {
        return mapPuestoEmbarques.get(arrayPuestosEmbarques.get(random.nextInt(arrayPuestosEmbarques.size())));
    }

    public synchronized void esperarLlamado() {
        // TODO tendrian que esperar en su puerta
        try {
            wait();
        } catch (InterruptedException e) {
            System.out.println(Console.colorString("RED", "ERROR con el pasajero al esperar"));
        }
    }
}
