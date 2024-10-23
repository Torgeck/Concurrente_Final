package hilos;

import console.Console;
import pasivos.Aeropuerto;
import pasivos.WalkieTalkie;

public class Guardia implements Runnable {

    private String idGuardia;
    private String color;
    private Aeropuerto aeropuerto;
    private WalkieTalkie walkie;

    public Guardia(Aeropuerto aeropuerto) {
        this.aeropuerto = aeropuerto;
        this.walkie = new WalkieTalkie();
        idGuardia = "Guardia de hall";
        this.color = "CYAN";
    }

    public WalkieTalkie getWalkie() {
        return this.walkie;
    }

    public void run() {

        System.out.println(Console.colorString(this.color, "Guardia entro a laburar"));
        // TODO hacer que tenga horario
        while (true) {
            try {
                System.out.println(Console.colorString(this.color, idGuardia + " esperando en el hall"));
                walkie.esperarLlamada();
                System.out.println(Console.colorString(this.color, "Aviso al hall que se desocupo una cola"));
                aeropuerto.getHall().avisoLugarLibre();
            } catch (Exception e) {
                System.out.println(Console.colorString(this.color, "ERROR exploto el guardia en el hall"));
            }
        }
    }

}
