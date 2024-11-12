package pasivos;

import java.util.concurrent.locks.Condition;

public class Alarma implements Comparable<Alarma> {

    private int tiempo;
    private PuestoEmbarque puesto;

    public Alarma(int tiempo, PuestoEmbarque puesto) {
        this.tiempo = tiempo;
        this.puesto = puesto;
    }

    public int getTiempo() {
        return this.tiempo;
    }

    public PuestoEmbarque getPuestoEmbarque() {
        return this.puesto;
    }

    @Override
    public int compareTo(Alarma otraAlarma) {
        return Integer.compare(tiempo, otraAlarma.getTiempo());
    }
}
