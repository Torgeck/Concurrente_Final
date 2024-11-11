package pasivos;

public class Hall {

    private Aeropuerto aeropuerto;

    public Hall() {
    }

    public synchronized void avisoLugarLibre() {
        notifyAll();
    }

    public synchronized void esperarEnHall() throws InterruptedException {
        wait();
    }

}
