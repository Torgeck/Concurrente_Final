package pasivos;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

public class Terminal {

    private Aeropuerto aeropuerto;
    private char idTerminal;
    private Freeshop freeshop;
    private ArrayList<Integer> arrayPuestosEmbarques;
    private HashMap<Integer, PuestoEmbarque> mapPuestoEmbarques;

    public Terminal(Aeropuerto aeropuerto, char idTerminal, int limiteInferior, int limiteSuperior) {
        this.aeropuerto = aeropuerto;
        this.idTerminal = idTerminal;
        this.mapPuestoEmbarques = new HashMap<Integer, PuestoEmbarque>();
        this.arrayPuestosEmbarques = new ArrayList<>();
        generarPuestoEmbarque(limiteInferior, limiteSuperior);
    }

    public char getIdTerminal() {
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
        Random random = new Random();
        return mapPuestoEmbarques.get(arrayPuestosEmbarques.get(random.nextInt(arrayPuestosEmbarques.size())));
    }
}
