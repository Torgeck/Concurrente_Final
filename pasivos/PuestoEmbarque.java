package pasivos;

public class PuestoEmbarque {

    private int idPuesto;
    private Terminal terminal;
    // private Vuelo? hora? idk

    public PuestoEmbarque(int idPuesto, Terminal terminal) {
        this.idPuesto = idPuesto;
        this.terminal = terminal;
    }

    // TODO logica para embarque
    public synchronized void avisarEmbarque() {
        notifyAll();
    }

    public synchronized void esperarEmbarque() throws InterruptedException {
        wait();
    }

    
}
