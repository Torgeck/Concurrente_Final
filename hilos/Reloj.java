package hilos;

public class Reloj implements Runnable{

    private long tiempoActual;

    public Reloj() {
        tiempoActual = 6000;
    }

    public void run() {
        while(true) {
            try {
                Thread.sleep(5000);
                incrementTime();
            } catch (Exception e) {
                System.out.println("ERROR exploto el reloj");
            }
        }
    }

    public synchronized void incrementTime(){
        // Aumenta el tiempo actual mas 6 min
        if(tiempoActual <= 24000) {
            tiempoActual += 100;
        }
        else{
            tiempoActual = 0;
        }
    }

    public synchronized long getTime(){
        return tiempoActual;
    }

    public synchronized boolean estaAbiertoAlPublico(){
        return (tiempoActual <= 22000) && (tiempoActual >= 6000);
    }

    public synchronized boolean tieneTiempoFreeshop(long timpoEmbarque){
        // Tiene mas de 30min antes del vuelo
        return this.tiempoActual - timpoEmbarque >= 500;
    }

    public synchronized boolean perdioVuelo(long tiempoEmbarque){
        return tiempoEmbarque > this.tiempoActual;
    }



}
