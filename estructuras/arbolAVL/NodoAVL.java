package estructuras.arbolAVL;

@SuppressWarnings("rawtypes")

public class NodoAVL {

    private Object value;
    private Comparable key;
    private int altura;
    private NodoAVL izquierdo;
    private NodoAVL derecho;

    // Constructor
    public NodoAVL(Object value, Comparable key, NodoAVL izquierdo, NodoAVL derecho) {
        this.value = value;
        this.key = key;
        this.izquierdo = izquierdo;
        this.derecho = derecho;
        this.altura = 0;
    }

    public NodoAVL(Object value, Comparable key) {
        this(value, key, null, null);
    }

    // Getters
    public Object getValue() {
        return value;
    }

    public Comparable getKey() {
        return key;
    }

    public int getAltura() {
        return altura;
    }

    public NodoAVL getIzquierdo() {
        return izquierdo;
    }

    public NodoAVL getDerecho() {
        return derecho;
    }

    // Setters
    public void setValue(Object value) {
        this.value = value;
    }

    public void setKey(Comparable key) {
        this.key = key;
    }

    public void setIzquierdo(NodoAVL izquierdo) {
        this.izquierdo = izquierdo;
    }

    public void setDerecho(NodoAVL derecho) {
        this.derecho = derecho;
    }

    public void recalcularAltura() {
        // Metodo que recalcula la altura del nodo
        int alturaIzquierdo = -1, alturaDerecho = -1;

        if (this.izquierdo != null)
            alturaIzquierdo = this.izquierdo.getAltura();
        if (this.derecho != null)
            alturaDerecho = this.derecho.getAltura();

        this.altura = Math.max(alturaIzquierdo, alturaDerecho) + 1;
    }
}
