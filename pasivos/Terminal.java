package pasivos;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;
import java.util.concurrent.CyclicBarrier;

public class Terminal {

    private Aeropuerto aeropuerto;
    private char idTerminal;
    private Freeshop freeshop;
    private ArrayList<Integer> arrayPuestosEmbarques;
    private HashMap<Integer, PuestoEmbarque> mapPuestoEmbarques;
    private Random random;
    private CyclicBarrier cierre;

    public Terminal(Aeropuerto aeropuerto, char idTerminal, int limiteInferior, int limiteSuperior, int cantCajas,
                    int capMax) {
        this.aeropuerto = aeropuerto;
        this.idTerminal = idTerminal;
        this.mapPuestoEmbarques = new HashMap<>();
        this.arrayPuestosEmbarques = new ArrayList<>();
        this.random = new Random();
        this.freeshop = new Freeshop(idTerminal, cantCajas, capMax);
        generarPuestoEmbarque(limiteInferior, limiteSuperior);
        this.cierre = new CyclicBarrier(arrayPuestosEmbarques.size());
    }

    public char getId() {
        return this.idTerminal;
    }

    public Aeropuerto getAeropuerto() {
        return this.aeropuerto;
    }

    public Freeshop getFreeshop() {
        return this.freeshop;
    }

    public ArrayList<Integer> getArrayPuestosEmbarques() {
        return this.arrayPuestosEmbarques;
    }

    public HashMap<Integer, PuestoEmbarque> getMapPuestoEmbarques() {
        return this.mapPuestoEmbarques;
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

    public void agregarAlarma(Alarma alarma) {
        this.aeropuerto.agregarAlarma(alarma);
    }

    public int getTiempoActual() {
        return this.aeropuerto.getHora();
    }

    public void esperarCierre() {
        try {
            cierre.await();
        } catch (Exception e) {
            System.out.println("ERROR al esperar cierre empleados");
        }
    }

    public void nuevoDia() {
        cierre.reset();
    }

    public void liberarEmpleadosEmbarque() {
        this.mapPuestoEmbarques.forEach((k, puesto) -> puesto.liberarEmpleado());
    }

    public String toString() {
        return Character.toString(this.idTerminal);
    }
}

