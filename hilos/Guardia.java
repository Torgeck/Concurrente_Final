package hilos;

import console.Console;
import pasivos.Aeropuerto;
import pasivos.WalkieTalkie;

public class Guardia implements Runnable {

    private final String idGuardia;
    private final String color;
    private final Aeropuerto aeropuerto;
    private final WalkieTalkie walkie;

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
                System.out.println(Console.colorString(this.color, idGuardia + " esperando en el hall \uD83D\uDCA4\uD83D\uDCA4"));
                walkie.esperarLlamada();
                System.out.println(Console.colorString(this.color, "\uD83D\uDDE3️\uD83D\uDCE3 Aviso al hall que se desocupo una cola \uD83D\uDDE3️\uD83D\uDCE3"));
                aeropuerto.getHall().avisoLugarLibre();
            } catch (Exception e) {
                System.out.println(Console.colorString(this.color, "ERROR exploto el guardia en el hall"));
            }
        }
    }

}
