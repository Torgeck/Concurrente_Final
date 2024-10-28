package pasivos;

public class Reserva {

    private String empresa;
    private int puertaEmbarque;
    private char terminal;
    private long horaEmbarque;

    public Reserva(String empresa) {
        this.empresa = empresa;
    }

    public Reserva(Vuelo vuelo) {
        this.empresa = vuelo.getAerolinea();
        this.terminal = vuelo.getTerminal();
        this.puertaEmbarque = vuelo.getPuertaEmbarque();
        this.horaEmbarque = vuelo.getHoraEmbarque();
    }

    public String getEmpresa() {
        return empresa;
    }

    public int getPuertaEmbarque() {
        return puertaEmbarque;
    }

    public void setPuertaEmbarque(int puertaEmbarque) {
        this.puertaEmbarque = puertaEmbarque;
    }

    public char getTerminal() {
        return terminal;
    }

    public void setTerminal(char terminal) {
        this.terminal = terminal;
    }

    public long getHoraEmbarque() {
        return horaEmbarque;
    }

    public void setHoraEmbarque(long horaEmbarque) {
        this.horaEmbarque = horaEmbarque;
    }

    public String toString() {
        return "[ Emp:" + this.empresa + ", Ter:" + this.terminal + ", P.Emb: " + this.puertaEmbarque + ", H.Emb:"
                + this.horaEmbarque + "]";
    }

}