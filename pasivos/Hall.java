package pasivos;

public class Hall {

    public Hall() {
    }

    public synchronized void avisoLugarLibre() {
        notifyAll();
    }

    public synchronized void esperarEnHall() throws InterruptedException {
        wait();
    }

}
