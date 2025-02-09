package pasivos;

public class Hall {

    public Hall() {
    }

    // Metodos Pasajero
    public synchronized void notificarGuardia() {
        notify();
    }

    public synchronized void esperarEnHall() throws InterruptedException {
        wait();
    }

    // Metodos Guardia
    public synchronized void avisoLugarLibre() {
        notifyAll();
    }

    public synchronized void esperarLlamada() throws InterruptedException {
        wait();
    }

}
