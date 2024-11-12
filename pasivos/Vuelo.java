package pasivos;

public class Vuelo {

    private static int NUMERO_VUELO = 0;
    private String idVuelo;
    private String aerolinea;
    private Terminal terminal;
    private PuestoEmbarque puertaEmbarque;
    private int horaEmbarque;

    public Vuelo(String aerolinea, Terminal terminal, PuestoEmbarque puertaEmbarque, int horaEmbarque) {
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

    public Terminal getTerminal() {
        return terminal;
    }

    public PuestoEmbarque getPuertaEmbarque() {
        return puertaEmbarque;
    }

    public int getHoraEmbarque() {
        return horaEmbarque;
    }

    public String toString() {
        return "ID: " + this.idVuelo + "| Aerolinea: " + this.aerolinea + "| Terminal: " + this.terminal + "| PEmb: " + this.puertaEmbarque.getIdPuesto() + "| Hora: " + this.horaEmbarque;
    }
}
