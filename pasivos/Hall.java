package pasivos;

public class Hall {

    private Aeropuerto aeropuerto;

    public Hall(Aeropuerto aeropuerto) {
        this.aeropuerto = aeropuerto;
    }

    public synchronized void avisoLugarLibre() {
        notifyAll();
    }

    public synchronized void esperarEnHall() throws InterruptedException {
        wait();
    }

}
