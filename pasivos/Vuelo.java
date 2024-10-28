package pasivos;

public class Vuelo {

    private static int NUMERO_VUELO = 0;
    private String idVuelo;
    private String aerolinea;
    private char terminal;
    private int puertaEmbarque;
    private long horaEmbarque;

    public Vuelo(String aerolinea, char terminal, int puertaEmbarque, long horaEmbarque) {
        this.aerolinea = aerolinea;
        this.horaEmbarque = horaEmbarque;
        this.terminal = terminal;
        this.puertaEmbarque = puertaEmbarque;
        this.idVuelo = generarId();
    }

    private String generarId() {
        NUMERO_VUELO++;
        return aerolinea.substring(0, 3).concat(String.valueOf(NUMERO_VUELO));

    }

    public String getIdVuelo() {
        return idVuelo;
    }

    public String getAerolinea() {
        return aerolinea;
    }

    public char getTerminal() {
        return terminal;
    }

    public int getPuertaEmbarque() {
        return puertaEmbarque;
    }

    public long getHoraEmbarque() {
        return horaEmbarque;
    }

}
