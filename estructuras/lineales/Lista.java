package estructuras.lineales;

public class Lista {

    private NodoDoble cabecera;
    private NodoDoble fin;
    private int longitud;

    public Lista() {
        this.cabecera = null;
        this.fin = null;
        longitud = 0;
    }

    public int longitud() {
        return this.longitud;
    }

    public boolean insertar(Object elemento, int posicion) {
        boolean exito = false;

        // Caso donde se quiera insertar nodo en una posicion que no exista
        if (posicion >= 1 && posicion <= this.longitud + 1) {
            exito = true;

            // Caso en donde se quiere insertar elemento en la lista vacia
            if (this.longitud == 0) {
                this.cabecera = new NodoDoble(elemento, null, null);
                this.fin = this.cabecera;
            } else {
                // Si se quiere insertar al inicio de la lista
                if (posicion == 1) {
                    this.cabecera.setPrevio(new NodoDoble(elemento, null, this.cabecera));
                    this.cabecera = this.cabecera.getPrevio();
                } else {
                    // Si se quiere insertar al final de la lista
                    if (posicion == this.longitud + 1) {
                        // Caso en el que tenga un solo elemento la Lista
                        if (this.fin.getPrevio() == null) {
                            this.fin = new NodoDoble(elemento, this.cabecera, null);
                            this.cabecera.setSiguiente(this.fin);
                        } else {
                            this.fin.setSiguiente(new NodoDoble(elemento, this.fin, null));
                            this.fin = this.fin.getSiguiente();
                        }
                    } else {
                        // Si se quiere insertar en el medio de la lista
                        NodoDoble siguiente, anterior, nuevoNodo;
                        int i;

                        // Si es menor a la mitad entonces empiezo por cabecera
                        if (posicion <= (this.longitud / 2)) {
                            anterior = this.cabecera;
                            i = 1;

                            // Avanza hasta el nodo de la posicion -1
                            while (i < posicion - 1) {
                                anterior = anterior.getSiguiente();
                                i++;
                            }
                            siguiente = anterior.getSiguiente();

                        } else {
                            // Caso contrario empiezo por fin
                            siguiente = this.fin;
                            i = this.longitud;

                            while (i > posicion) {
                                siguiente = siguiente.getPrevio();
                                i--;
                            }
                            anterior = siguiente.getPrevio();

                        }
                        nuevoNodo = new NodoDoble(elemento, anterior, siguiente);
                        anterior.setSiguiente(nuevoNodo);
                        siguiente.setPrevio(nuevoNodo);
                    }
                }
            }
            this.longitud++;
        }
        return exito;
    }

    public boolean insertarInicio(Object elemento) {
        // Metodo que inserta al principio de una lista un nuevo nodo

        if (this.longitud == 0) {
            this.cabecera = new NodoDoble(elemento, null, null);
            this.fin = this.cabecera;
        } else {
            this.cabecera.setPrevio(new NodoDoble(elemento, null, this.cabecera));
            this.cabecera = this.cabecera.getPrevio();
        }
        this.longitud++;

        return true;
    }

    public boolean insertarFin(Object elemento) {
        // Metodo que inserta al final de la lista un nuevo nodo

        if (this.longitud == 0) {
            this.cabecera = new NodoDoble(elemento, null, null);
            this.fin = this.cabecera;
        } else {
            this.fin.setSiguiente(new NodoDoble(elemento, this.fin, null));
            this.fin = this.fin.getSiguiente();
        }
        this.longitud++;

        return true;
    }

    public boolean eliminar(int posicion) {
        boolean exito = false;

        if (posicion >= 1 && posicion <= this.longitud) {
            // Caso en el que se elimine el unico elemento
            if (this.longitud == 1) {
                this.cabecera = null;
                this.fin = null;
            } else {
                // Si se elimina el primer elemento
                if (posicion == 1) {
                    this.cabecera = this.cabecera.getSiguiente();
                    this.cabecera.setPrevio(null);
                } else {

                    // Si se elimina el ultimo elemento
                    if (posicion == this.longitud) {
                        this.fin = this.fin.getPrevio();
                        this.fin.setSiguiente(null);
                    } else {
                        // Si se elimina elemento intermedio de la lista
                        NodoDoble nodoBuscado;
                        int i;

                        // Si es menor a la mitad entonces empiezo por cabecera
                        if (posicion < (this.longitud / 2)) {
                            nodoBuscado = this.cabecera;
                            i = 1;

                            // Avanza al siguiente hasta encontrar el nodo
                            while (i != posicion) {
                                nodoBuscado = nodoBuscado.getSiguiente();
                                i++;
                            }

                        } else {
                            // Caso contrario empiezo por fin
                            nodoBuscado = this.fin;
                            i = this.longitud;

                            while (i != posicion) {
                                nodoBuscado = nodoBuscado.getPrevio();
                                i--;
                            }
                        }
                        // Linkeo los nodos previo y siguiente del nodo a eliminar
                        nodoBuscado.getPrevio().setSiguiente(nodoBuscado.getSiguiente());
                        nodoBuscado.getSiguiente().setPrevio(nodoBuscado.getPrevio());
                    }
                }
            }
            // Resto un elemento a la longitud total
            this.longitud--;
            exito = true;
        }
        return exito;
    }

    public Object recuperar(int posicion) {
        Object elemento = null;

        // Excepcion por si la posicion ingresada por parametro no exite en la lista
        if (posicion >= 1 && posicion <= this.longitud) {
            int i;
            NodoDoble aux;

            // Si es menor a la mitad entonces empiezo por cabecera
            if (posicion < (this.longitud / 2)) {
                aux = this.cabecera;
                i = 1;

                // Avanza hasta el nodo de la posicion -1
                while (i < posicion) {
                    aux = aux.getSiguiente();
                    i++;
                }
            } else {
                // Caso contrario empiezo por fin
                aux = this.fin;
                i = this.longitud;

                while (i > posicion) {
                    aux = aux.getPrevio();
                    i--;
                }
            }
            elemento = aux.getElemento();
        }
        return elemento;
    }

    public int localizar(Object elemento) {
        int posicion = -1, i = 1;
        NodoDoble aux = this.cabecera;

        while (aux != null) {
            if (aux.getElemento().equals(elemento)) {
                posicion = i;
                aux = null;
            } else {
                aux = aux.getSiguiente();
                i++;
            }
        }
        return posicion;
    }

    public boolean eliminarElemento(Object objeto) {
        boolean exito = false;

        // Caso que se elimine la primer posicion
        if (this.cabecera.getElemento().equals(objeto)) {
            this.cabecera = this.cabecera.getSiguiente();
            this.cabecera.setPrevio(null);
            exito = true;
        } else {
            exito = eliminarElementoRecursivo(objeto, this.cabecera, this.cabecera.getSiguiente());
        }

        return exito;
    }

    private boolean eliminarElementoRecursivo(Object objeto, NodoDoble nodoAnterior, NodoDoble nodoActual) {
        boolean exito = false;

        if (nodoActual != null) {
            if (nodoActual.getElemento().equals(objeto)) {
                nodoAnterior.setSiguiente(nodoActual.getSiguiente());
                exito = true;
            } else {
                exito = eliminarElementoRecursivo(objeto, nodoActual, nodoActual.getSiguiente());
            }
        }
        return exito;
    }

    public boolean esVacia() {
        return this.cabecera == null;
    }

    public void vaciar() {
        this.cabecera = null;
        this.fin = null;
        this.longitud = 0;
    }

    @Override
    public Lista clone() {
        Lista clon = new Lista();

        if (this.cabecera != null) {

            // Creo un nuevo nodo identico al nodo de la cabecera del OG
            clon.cabecera = new NodoDoble(this.cabecera.getElemento(), null, null);

            // Seteo la misma longitud
            clon.longitud = this.longitud;

            // Paso nodos ya avanzados
            cloneRecursivo(clon.cabecera, this.cabecera, clon);
        }

        return clon;
    }

    private void cloneRecursivo(NodoDoble nodoClon, NodoDoble nodoOG, Lista clon) {

        if (nodoOG.getSiguiente() == null) {
            nodoClon.setSiguiente(null);
            clon.fin = nodoClon;
        } else {
            // creo y asigno nuevo nodo al nodo clon
            nodoClon.setSiguiente(new NodoDoble(nodoOG.getSiguiente().getElemento(), nodoClon, null));
            // avanzo punteros y hago llamado recursivo
            cloneRecursivo(nodoClon.getSiguiente(), nodoOG.getSiguiente(), clon);
        }
    }

    public String toString() {
        StringBuilder salida = new StringBuilder("[");
        NodoDoble aux = this.cabecera;

        while (aux != null) {
            salida.append(aux.getElemento());
            // Muevo puntero
            aux = aux.getSiguiente();
            // Agrego comas entre elementos
            if (aux != null)
                salida.append(",");
        }

        return salida.append("]").toString();
    }

    public String enumerar() {
        StringBuilder salida = new StringBuilder();
        NodoDoble aux = this.cabecera;
        int indice = 1;

        while (aux != null) {
            salida.append(indice).append(" - ").append(aux.getElemento()).append("\n");

            aux = aux.getSiguiente();
            indice++;
        }

        return salida.toString();
    }

    public Lista invertir() {
        Lista invertida = new Lista();

        if (this.cabecera != null) {

            invertida.cabecera = new NodoDoble(this.fin.getElemento(), null, null);
            invertirRecursivo(invertida, invertida.cabecera, this.fin);

            // Seteo la misma longitud en ambas listas
            invertida.longitud = this.longitud;
        }

        return invertida;
    }

    private void invertirRecursivo(Lista invertida, NodoDoble nodoClon, NodoDoble nodoOG) {

        // Hace llamada recursiva hasta llegar el principio de la lista original
        if (nodoOG.getPrevio() != null) {
            // Avanzo el puntero
            nodoClon.setSiguiente(new NodoDoble(nodoOG.getPrevio().getElemento(), nodoClon, null));
            nodoClon.getSiguiente().setPrevio(nodoClon);

            invertirRecursivo(invertida, nodoClon.getSiguiente(), nodoOG.getPrevio());
        } else {
            invertida.fin = nodoClon;
        }
    }

    public boolean insertarElementosLista(Lista listaElementos) {
        // Metodo que recibe una lista por parametro y agrega los elementos de ella a la
        // lista que llama el metodo
        boolean resultado = true;
        int i = 1;

        if (this.cabecera != null) {
            insertarElementosListaRecursivo(this.cabecera, listaElementos, i);
        } else if (!listaElementos.esVacia()) {
            this.cabecera = new NodoDoble(listaElementos.recuperar(i), null, null);
            this.fin = this.cabecera;
            this.longitud++;
            insertarElementosListaRecursivo(this.cabecera, listaElementos, i++);
        } else {
            resultado = false;
        }

        return resultado;
    }

    private void insertarElementosListaRecursivo(NodoDoble nodoActual, Lista elementos, int posicionElemento) {

        if (nodoActual.getSiguiente() != null) {
            insertarElementosListaRecursivo(nodoActual.getSiguiente(), elementos, posicionElemento);
        } else if (elementos.recuperar(posicionElemento) != null) {
            nodoActual.setSiguiente(new NodoDoble(elementos.recuperar(posicionElemento), nodoActual, null));
            this.fin = nodoActual.getSiguiente();
            this.longitud++;
            insertarElementosListaRecursivo(nodoActual.getSiguiente(), elementos, posicionElemento + 1);
        }
    }
}
