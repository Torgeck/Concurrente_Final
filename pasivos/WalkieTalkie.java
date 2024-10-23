package pasivos;

public class WalkieTalkie {

    public WalkieTalkie() {

    }

    public synchronized void notificarGuardia() {
        notify();
    }

    public synchronized void esperarLlamada() throws InterruptedException {
        wait();
    }
}
