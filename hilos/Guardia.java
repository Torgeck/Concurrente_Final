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
        this.idGuardia = "Guardia de hall";
        this.color = "CYAN";
    }

    public WalkieTalkie getWalkie() {
        return this.walkie;
    }

    public void run() {
        System.out.println(Console.colorString(this.color, "Guardia entro a laburar"));
        while (!this.aeropuerto.getFlagSimulacion()) {
            if (this.aeropuerto.estaCerradoAlPublico()) {
                System.out.println(Console.colorString(this.color,
                        "\uD83D\uDDE3️\uD83D\uDCE3 Aviso al hall que cerro Aeropuerto por hoy \uD83D\uDDE3️\uD83D\uDCE3"));
                this.aeropuerto.getHall().avisoLugarLibre();
                this.aeropuerto.esperarApertura();
                System.out.println(Console.colorString(this.color, "Guardia entro a laburar"));
            } else {
                try {
                    System.out.println(Console.colorString(this.color,
                            idGuardia + " esperando en el hall \uD83D\uDCA4\uD83D\uDCA4"));
                    walkie.esperarLlamada();
                    System.out.println(Console.colorString(this.color,
                            "\uD83D\uDDE3️\uD83D\uDCE3 Aviso al hall que se desocupo una cola \uD83D\uDDE3️\uD83D\uDCE3"));
                    aeropuerto.getHall().avisoLugarLibre();
                } catch (Exception e) {
                    System.out.println(Console.colorString("RED", "ERROR con el guardia en el hall"));
                }
            }
        }
        System.out.println(Console.colorString("BLACK",
                "\uD83C\uDFC4\uD83C\uDFC4 Guardia se toma vacaciones por siempre \uD83C\uDFC4\uD83C\uDFC4"));
    }

}
