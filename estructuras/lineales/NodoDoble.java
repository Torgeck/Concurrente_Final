package estructuras.lineales;

public class NodoDoble {

    private NodoDoble previo;
    private NodoDoble siguiente;
    private Object elemento;

    public NodoDoble(Object elemento, NodoDoble previo, NodoDoble siguiente) {
        this.elemento = elemento;
        this.previo = previo;
        this.siguiente = siguiente;
    }

    public Object getElemento() {
        return elemento;
    }

    public void setElemento(Object elemento) {
        this.elemento = elemento;
    }

    public NodoDoble getPrevio() {
        return this.previo;
    }

    public void setPrevio(NodoDoble previo) {
        this.previo = previo;
    }

    public NodoDoble getSiguiente() {
        return this.siguiente;
    }

    public void setSiguiente(NodoDoble siguiente) {
        this.siguiente = siguiente;
    }

    public boolean equals(Object otro) {
        return this.elemento.equals(otro);
    }
}
