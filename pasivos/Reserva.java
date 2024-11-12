package pasivos;

public class Reserva {

    private String empresa;
    private PuestoEmbarque puertaEmbarque;
    private Terminal terminal;
    private String vueloID;
    private int horaEmbarque;

    public Reserva(String empresa) {
        this.empresa = empresa;
    }

    public Reserva(Vuelo vuelo) {
        this.empresa = vuelo.getAerolinea();
        this.terminal = vuelo.getTerminal();
        this.vueloID = vuelo.getIdVuelo();
        this.puertaEmbarque = vuelo.getPuertaEmbarque();
        this.horaEmbarque = vuelo.getHoraEmbarque();
    }

    public String getVueloID() {
        return this.vueloID;
    }

    public String getEmpresa() {
        return this.empresa;
    }

    public PuestoEmbarque getPuertaEmbarque() {
        return this.puertaEmbarque;
    }

    public void setPuertaEmbarque(PuestoEmbarque puertaEmbarque) {
        this.puertaEmbarque = puertaEmbarque;
    }

    public Terminal getTerminal() {
        return this.terminal;
    }

    public void setTerminal(Terminal terminal) {
        this.terminal = terminal;
    }

    public int getHoraEmbarque() {
        return this.horaEmbarque;
    }

    public void setHoraEmbarque(int horaEmbarque) {
        this.horaEmbarque = horaEmbarque;
    }

    public String toString() {
        return "[ Emp:" + this.empresa + ", Ter:" + this.terminal + ", P.Emb: " + this.puertaEmbarque.getIdPuesto() + ", H.Emb:"
                + this.horaEmbarque + "]";
    }

}
