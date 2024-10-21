import java.util.concurrent.Semaphore;

public class Guardia implements Runnable {

    private String idGuardia;
    private Aeropuerto aeropuerto;
    private WalkieTalkie walkie;

    public Guardia(Aeropuerto aeropuerto) {
        this.aeropuerto = aeropuerto;
        this.walkie = new WalkieTalkie();
        idGuardia = "Guardia de hall";
    }

    public WalkieTalkie getWalkie() {
        return this.walkie;
    }

    public void run() {
        String CYAN = "\u001B[36m";
        String RESET = "\u001B[0m";

        System.out.println(CYAN + "Guardia entro a laburar" + RESET);
        // TODO hacer que tenga horario
        while (true) {
            try {
                System.out.println(CYAN + idGuardia + " esperando en el hall" + RESET);
                walkie.esperarLlamada();
                System.out.println(CYAN + "Aviso al hall que se desocupo una cola" + RESET);
                aeropuerto.getHall().avisoLugarLibre();
            } catch (Exception e) {
                System.out.println(CYAN + "ERROR exploto el guardia en el hall" + RESET);
            }
        }
    }

}
