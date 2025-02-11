package hilos;

import console.Console;
import pasivos.Aeropuerto;
import pasivos.Hall;

public class Guardia implements Runnable {

    private final String idGuardia;
    private final String color;
    private final Aeropuerto aeropuerto;
    private final Hall hall;

    public Guardia(Aeropuerto aeropuerto) {
        this.aeropuerto = aeropuerto;
        this.hall = aeropuerto.getHall();
        this.idGuardia = "Guardia de hall";
        this.color = "CYAN";
    }

    public void run() {
        System.out.println(Console.colorString(this.color, "Guardia entro a laburar"));
        while (!this.aeropuerto.getFlagSimulacion()) {
            if (this.aeropuerto.estaCerradoAlPublico()) {
                System.out.println(Console.colorString(this.color,
                        "\uD83D\uDDE3️\uD83D\uDCE3 Aviso al hall que cerro Aeropuerto por hoy \uD83D\uDDE3️\uD83D\uDCE3"));
                this.hall.avisoLugarLibre();
                this.aeropuerto.esperarApertura();
                System.out.println(Console.colorString(this.color, "Guardia entro a laburar"));
            } else {
                try {
                    System.out.println(Console.colorString(this.color,
                            idGuardia + " esperando en el hall \uD83D\uDCA4\uD83D\uDCA4"));
                    this.hall.esperarLlamada();
                    System.out.println(Console.colorString(this.color,
                            "\uD83D\uDDE3️\uD83D\uDCE3 Aviso al hall que se desocupo una cola \uD83D\uDDE3️\uD83D\uDCE3"));
                    this.hall.avisoLugarLibre();
                } catch (Exception e) {
                    System.out.println(Console.colorString("RED", "ERROR con el guardia en el hall"));
                }
            }
        }
        System.out.println(Console.colorString("BLACK",
                "\uD83C\uDFC4\uD83C\uDFC4 Guardia se toma vacaciones por siempre \uD83C\uDFC4\uD83C\uDFC4"));
    }

}
