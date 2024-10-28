package estructuras.arbolAVL;

import estructuras.lineales.Lista;

@SuppressWarnings({ "rawtypes", "unchecked" })

public class ArbolAVL {
    // Atributo
    private NodoAVL raiz;

    // Inicializador
    public ArbolAVL() {
        this.raiz = null;
    }

    // Metodos
    public boolean insertar(Comparable key, Object elemento) {
        // Inserta un elemento en el arbol AVL, retorna un boolean dependiendo si
        // inserto o no el elemento
        boolean exito = true;

        // Caso especial el arbol esta vacio
        if (this.raiz == null)
            this.raiz = new NodoAVL(elemento, key);
        else
            exito = insertarAux(null, this.raiz, elemento, key);

        return exito;
    }

    private boolean insertarAux(NodoAVL padre, NodoAVL nodo, Object elemento, Comparable key) {
        // Metodo auxiliar que recorre el arbol para poder insertar el elemento
        boolean exito = true;

        // Si se encuentra en el arbol, se retorna false (No acepta keys duplicadas)
        if (key.compareTo(nodo.getKey()) == 0) {
            exito = false;
        }
        // Si es menor entra al subarbol izquierdo del nodo
        else if (key.compareTo(nodo.getKey()) < 0) {
            // Verifica si el nodo tiene HI y entra al subarbol izquierdo
            if (nodo.getIzquierdo() != null) {
                exito = insertarAux(nodo, nodo.getIzquierdo(), elemento, key);
            }
            // Caso contrario lo inserta como su propio HI
            else {
                nodo.setIzquierdo(new NodoAVL(elemento, key));
            }
        }
        // Verifica si el nodo tiene HD y entra al subarbol derecho
        else if (nodo.getDerecho() != null) {
            exito = insertarAux(nodo, nodo.getDerecho(), elemento, key);
        }
        // Caso contrario lo inserta como su propio HD
        else {
            nodo.setDerecho(new NodoAVL(elemento, key));
        }

        // Si se logró insertar entonces se balancea el arbol
        if (exito) {
            nodo.recalcularAltura();
            balancear(nodo, padre);
        }

        return exito;
    }

    private void balancear(NodoAVL nodo, NodoAVL padre) {
        // Metodo que analiza los casos y balancea al arbol
        int balanceNodo = getBalanceNodo(nodo);
        NodoAVL hijo;

        // Si el nodo esta desbalanceado procede a balancearlo
        if (Math.abs(balanceNodo) > 1) {
            if (balanceNodo > 1) {
                hijo = nodo.getIzquierdo();
                // El nodo esta caido hacia la izq, observo el balance de su HI
                if (getBalanceNodo(hijo) < 0) {
                    // Aplico rotacion doble izq-der
                    rotacionIzqDer(nodo, hijo, padre);
                } else {
                    // Aplico rotacion simple der
                    rotacionSimpleDer(nodo, padre);
                }
            } else {
                hijo = nodo.getDerecho();
                // El nodo esta caido hacia la der, observo el balance de su HD
                if (getBalanceNodo(hijo) > 0) {
                    // Aplico rotacion doble der-izq
                    rotacionDerIzq(nodo, hijo, padre);
                } else {
                    // Aplico rotacion simple izq
                    rotacionSimpleIzq(nodo, padre);
                }
            }
        }
    }

    private static int getBalanceNodo(NodoAVL nodo) {
        // Metodo que obtiene las alturas de los hijos y retorna la resta entre ellos
        int alturaIzq = -1, alturaDer = -1;

        if (nodo.getIzquierdo() != null)
            alturaIzq = nodo.getIzquierdo().getAltura();
        if (nodo.getDerecho() != null)
            alturaDer = nodo.getDerecho().getAltura();

        return alturaIzq - alturaDer;
    }

    private void rotacionSimpleDer(NodoAVL nodo, NodoAVL padre) {
        // Metodo que realiza una rotacion simple a derecha
        if (padre == null)
            this.raiz = rotarDerecha(nodo);
        else if (padre.getIzquierdo() == nodo) {
            padre.setIzquierdo(rotarDerecha(nodo));
        } else {
            padre.setDerecho(rotarDerecha(nodo));
        }
    }

    private void rotacionSimpleIzq(NodoAVL nodo, NodoAVL padre) {
        // Metodo que realiza una rotacion simple a izquierda
        if (padre == null)
            this.raiz = rotarIzquierda(nodo);
        else if (padre.getIzquierdo() == nodo)
            padre.setIzquierdo(rotarIzquierda(nodo));
        else
            padre.setDerecho(rotarIzquierda(nodo));
    }

    private void rotacionDerIzq(NodoAVL nodo, NodoAVL hijo, NodoAVL padre) {
        // Metodo que realiza una rotacion doble derecha-izquierda

        nodo.setDerecho(rotarDerecha(hijo));
        rotacionSimpleIzq(nodo, padre);
    }

    private void rotacionIzqDer(NodoAVL nodo, NodoAVL hijo, NodoAVL padre) {
        // Metodo que realiza una rotacion doble izquierda-derecha

        nodo.setIzquierdo(rotarIzquierda(hijo));
        rotacionSimpleDer(nodo, padre);
    }

    // Rotaciones
    private NodoAVL rotarIzquierda(NodoAVL nodoCritico) {
        // Algoritmo de rotacion simple a izquierda sobre pivote pasado por parametro
        NodoAVL nuevaRaiz = nodoCritico.getDerecho(), temp = nuevaRaiz.getIzquierdo();
        nuevaRaiz.setIzquierdo(nodoCritico);
        nodoCritico.setDerecho(temp);

        // Actualizo sus alturas
        nodoCritico.recalcularAltura();
        nuevaRaiz.recalcularAltura();

        return nuevaRaiz;
    }

    private NodoAVL rotarDerecha(NodoAVL nodoCritico) {
        // Algoritmo de rotacion simple a derecha sobre pivote pasado por parametro
        NodoAVL nuevaRaiz = nodoCritico.getIzquierdo(), temp = nuevaRaiz.getDerecho();
        nuevaRaiz.setDerecho(nodoCritico);
        nodoCritico.setIzquierdo(temp);

        // Actualizo sus alturas
        nodoCritico.recalcularAltura();
        nuevaRaiz.recalcularAltura();

        return nuevaRaiz;
    }

    public boolean eliminar(Comparable key) {
        // Metodo que elimina un elemento del arbol y lo balancea si es necesario
        boolean exito;

        if (this.raiz == null)
            exito = false;
        else
            exito = eliminarAux(null, this.raiz, key);

        return exito;
    }

    private boolean eliminarAux(NodoAVL padre, NodoAVL nodo, Comparable key) {
        // Metodo auxiliar que recorre el arbol en forma recursiva hasta encontrar el
        // nodo a eliminar
        boolean exito = false;

        // Si no es nulo lo busco
        if (nodo != null) {
            // Si es el nodo actual entonces lo elimino segun el caso y cambio el boolean a
            // retornar
            if (key.compareTo(nodo.getKey()) == 0) {
                exito = true;
                eliminarNodo(padre, nodo);
            }
            // Si es menor recorro el subarbol izquierdo
            else if (key.compareTo(nodo.getKey()) < 0) {
                exito = eliminarAux(nodo, nodo.getIzquierdo(), key);
            }
            // Caso contrario recorro el subarbol derecho
            else {
                exito = eliminarAux(nodo, nodo.getDerecho(), key);
            }

            // Si se elimino entonces rebalanceo el arbol
            if (exito) {
                nodo.recalcularAltura();
                balancear(nodo, padre);
            }
        }
        return exito;
    }

    private void eliminarNodo(NodoAVL padre, NodoAVL nodo) {
        // Metodo que identifica el caso y elimina el nodo apropiadamente

        // Nodo sin hijos
        if (nodo.getDerecho() == null && nodo.getIzquierdo() == null) {
            eliminarHoja(padre, nodo);
        } else if (nodo.getDerecho() != null && nodo.getIzquierdo() != null) {
            // Nodo con 2 hijos
            eliminarNodoConHijos(padre, nodo);
        } else {
            // Nodo con 1 hijo
            eliminarNodoConUnHijo(padre, nodo);
        }
    }

    private void eliminarHoja(NodoAVL padre, NodoAVL nodo) {
        // Metodo que elimina una hoja de un arbol

        // Caso especial donde se quiere eliminar la raiz
        if (padre == null)
            this.raiz = null;

        // Si el elemento a eliminar es el HI seteo al mismo como null
        else if (padre.getIzquierdo() == nodo)
            padre.setIzquierdo(null);

        // Caso contrario seteo el HD a null
        else
            padre.setDerecho(null);
    }

    // Caso 2
    private void eliminarNodoConUnHijo(NodoAVL padre, NodoAVL nodo) {
        // Metodo que elimina un nodo que tiene un solo hijo
        NodoAVL hijoIzq = nodo.getIzquierdo(), hijoDer = nodo.getDerecho();

        // Caso especial donde se quiere eliminar la raiz
        if (padre == null)
            this.raiz = (hijoDer == null) ? hijoIzq : hijoDer;

        // Veo si nodo es HI del padre
        else if (padre.getIzquierdo() == nodo) {

            // Si el HI del nodo no es nulo entonces seteo al mismo con su padre
            if (hijoIzq != null)
                padre.setIzquierdo(hijoIzq);

            // Caso contrario seteo a su HD
            else
                padre.setIzquierdo(hijoDer);
        }
        // Caso contrario el nodo es HD del padre
        else {

            // Si el HI del nodo no es nulo entonces seteo al mismo con su padre
            if (hijoIzq != null)
                padre.setDerecho(hijoIzq);

            // Caso contrario seteo a su HD
            else
                padre.setDerecho(hijoDer);
        }
    }

    // Caso 3
    private void eliminarNodoConHijos(NodoAVL padre, NodoAVL nodo) {
        // Metodo que elimina un nodo que tiene ambos hijos
        obtenerYEliminarNodoCandidato(padre, nodo.getDerecho(), nodo);
    }

    private void obtenerYEliminarNodoCandidato(NodoAVL padre, NodoAVL nodo, NodoAVL nodoAReemplazar) {

        // No lo encontre, sigo buscando
        if (nodo.getIzquierdo() != null) {
            obtenerYEliminarNodoCandidato(nodo, nodo.getIzquierdo(), nodoAReemplazar);
        } else {
            // Lo encontre y seteo los valores del nodo OG por el candidato
            nodoAReemplazar.setValue(nodo.getValue());
            nodoAReemplazar.setKey(nodo.getKey());

            // Luego procedo a eliminar el nodo candidato del arbol
            eliminarAux(padre, nodo, nodo.getKey());
        }
    }

    public boolean eliminarMinimo() {
        // Metodo que elimina el minimo de un arbolAVL haciendo uso de metodos
        // anteriores
        boolean exito = false;

        // Checkeo que el arbol no este vacio
        if (this.raiz != null) {
            exito = eliminarMinimoAux(null, this.raiz);
        }

        return exito;
    }

    private boolean eliminarMinimoAux(NodoAVL padre, NodoAVL nodo) {
        // Metodo que recorre el arbol recursivamente hasta encontrar el nodo minimo
        // para eliminarlo
        boolean exito;
        // Si no es el minimo continuo recorriendo el arbol
        if (nodo.getIzquierdo() != null) {
            exito = eliminarMinimoAux(nodo, nodo.getIzquierdo());
        }
        // Si es el minimo entonces lo elimino
        else {
            eliminarAux(padre, nodo, nodo.getKey());
            exito = true;
        }

        // Recalculo la altura del nodo y rebalanceo el arbol si es necesario
        nodo.recalcularAltura();
        balancear(nodo, padre);

        return exito;
    }

    public boolean eliminarMaximo() {
        // Metodo que elimina el maximo de un arbolAVL haciendo uso de metodos
        // anteriores
        boolean exito = false;
        // Checkeo que el arbol no este vacio
        if (this.raiz != null) {
            exito = eliminarMaximoAux(null, this.raiz);
        }

        return exito;
    }

    private boolean eliminarMaximoAux(NodoAVL padre, NodoAVL nodo) {
        // Metodo que recorre el arbol recursivamente hasta encontrar el nodo maximo
        // para eliminarlo
        boolean exito;
        // Si no es el maximo continuo recorriendo el arbol
        if (nodo.getDerecho() != null) {
            exito = eliminarMaximoAux(nodo, nodo.getDerecho());
        }
        // Si es el maximo entonces lo elimino
        else {
            eliminarAux(padre, nodo, nodo.getKey());
            exito = true;
        }

        // Recalculo la altura del nodo y rebalanceo el arbol si es necesario
        nodo.recalcularAltura();
        balancear(nodo, padre);

        return exito;
    }

    public boolean pertenece(Comparable key) {
        // Metodo que devuelve un boolean si encuentra el elemento ingresado por
        // parametro en el arbolBB
        boolean exito = false;

        // Checkeo que el arbol no este vacio
        if (this.raiz != null)
            exito = perteneceAux(this.raiz, key) != null;

        return exito;
    }

    private NodoAVL perteneceAux(NodoAVL nodo, Comparable key) {
        NodoAVL nodoEncontrado = null;

        // Si encontramos el nodo con igual elemento lo devolvemos
        if (key.compareTo(nodo.getKey()) == 0) {
            nodoEncontrado = nodo;

            // Si el elemento es menor que el elemento del nodo actual busco en HI
        } else if (key.compareTo(nodo.getKey()) < 0) {

            if (nodo.getIzquierdo() != null)
                nodoEncontrado = perteneceAux(nodo.getIzquierdo(), key);
        }
        // Caso contrario busco en HD
        else {
            if (nodo.getDerecho() != null)
                nodoEncontrado = perteneceAux(nodo.getDerecho(), key);
        }

        return nodoEncontrado;
    }

    public Object obtenerElemento(Comparable key) {
        // Metodo que devuelve el value de un nodo con la key ingresada por parametro
        NodoAVL nodo = null;

        // Checkea que el arbol no esta vacio
        if (this.raiz != null) {
            nodo = perteneceAux(this.raiz, key);
        }

        return (nodo != null) ? nodo.getValue() : null;
    }

    public boolean esVacio() {
        // Metodo que devuelve un boolean dependiendo si hay o no elementos dentro del
        // arbol
        return this.raiz == null;
    }

    public void vaciar() {
        // Metodo que vacia el arbol AVL con el garbage colector de java
        this.raiz = null;
    }

    public Lista listar() {
        // Metodo que lista en orden al arbol
        Lista lista = new Lista();

        // Checkeo si el arbol esta vacio
        if (this.raiz != null)
            listarAux(this.raiz, lista);

        return lista;
    }

    private void listarAux(NodoAVL nodo, Lista lista) {
        // Metodo que lista al AVL en inorden

        if (nodo != null) {

            // Recorro el HI
            listarAux(nodo.getIzquierdo(), lista);

            lista.insertar(nodo.getKey(), lista.longitud() + 1);

            // Recorro el HD
            listarAux(nodo.getDerecho(), lista);
        }
    }

    public Lista listarRango(Comparable elemMinimo, Comparable elemMaximo) {
        /*
         * Recorre parte del arbol y devuelve una lista ordenada con los elementos que
         * se encuentran en el intervalo de los parametros ingresados []
         */
        Lista lista = new Lista();

        // Checkeo si el arbol esta vacio
        if (this.raiz != null)
            // Checkeo que los parametros esten ingresados bien
            if (elemMinimo.compareTo(elemMaximo) <= 0)
                listarRangoAux(elemMinimo, elemMaximo, lista, this.raiz);

        return lista;
    }

    private void listarRangoAux(Comparable min, Comparable max, Lista lista, NodoAVL nodo) {
        // Metodo recursivo que devuelve una lista ordenada entre min y max

        // Checkeo que le nodo no sea null
        if (nodo != null) {
            Comparable key = nodo.getKey();

            // Si el elemento actual es menor que el maximo entonces bajo por derecha
            if (key.compareTo(max) < 0)
                listarRangoAux(min, max, lista, nodo.getDerecho());

            // Si el elemento actual se encuentra entremedio de los valores pedidos lo
            // agrego a la lista
            if (key.compareTo(min) >= 0 && key.compareTo(max) <= 0)
                lista.insertar(key, 1);

            // Si el elemento actual es mayor que el minimo bajo a la izquierda
            if (key.compareTo(min) > 0)
                listarRangoAux(min, max, lista, nodo.getIzquierdo());
        }
    }

    public Comparable minimoElem() {
        // Metodo que devuelve el elemento minimo del arbolBB
        return minimoAux(this.raiz, null);
    }

    private Comparable minimoAux(NodoAVL nodo, Comparable key) {
        // Metodo recursivo que retorna el elemento minimo de un ABB

        if (nodo != null) {
            // Si no tiene HI entonces es la hoja por lo tanto el minimo elemento
            if (nodo.getIzquierdo() == null)
                key = nodo.getKey();

            // Si no avanza a su HI
            else
                key = minimoAux(nodo.getIzquierdo(), key);
        }

        return key;
    }

    public Comparable maximoElem() {
        // Metodo que devuelve el elemento mazimo del arbolBB
        return maximoAux(this.raiz, null);
    }

    private Comparable maximoAux(NodoAVL nodo, Comparable key) {
        // Metodo recursivo que retorna el elemento maximo de ABB

        if (nodo != null) {
            // Si no tiene HD entonces es la hoja por lo tanto es el maximo elemento
            if (nodo.getDerecho() == null)
                key = nodo.getKey();

            else {
                // Si no avanza a su HD
                key = maximoAux(nodo.getDerecho(), key);
            }
        }
        return key;
    }

    public String toString() {
        // Metodo que retorna un string de la clase arbol
        StringBuilder arbol = new StringBuilder();

        toStringRecursivo(this.raiz, arbol);

        return arbol.toString();
    }

    private void toStringRecursivo(NodoAVL nodo, StringBuilder cadena) {
        // Metodo que concatena un arbol en preorden

        if (nodo != null) {
            // Armo una cadena con todos los datos del nodo
            cadena.append(nodo.getKey() + "\t" +
                    "HI:" + ((nodo.getIzquierdo() != null) ? nodo.getIzquierdo().getKey() : "-") + "\t" +
                    "HD:" + ((nodo.getDerecho() != null) ? nodo.getDerecho().getKey() : "-") + "\n");

            // Llamado recursivo
            toStringRecursivo(nodo.getIzquierdo(), cadena);
            toStringRecursivo(nodo.getDerecho(), cadena);
        }
    }

    public String toKeyValueString() {
        // Metodo que retorna un string con toda la info contenida en el arbol
        StringBuilder arbol = new StringBuilder();

        toKeyValueStringRecursivo(this.raiz, arbol);

        return arbol.toString();
    }

    private void toKeyValueStringRecursivo(NodoAVL nodo, StringBuilder cadena) {

        if (nodo != null) {
            cadena.append("\nKey: " + nodo.getKey() + "\tValue: " + nodo.getValue() + "\tAltura: " + nodo.getAltura()
                    + "\n\t>> HI:" + ((nodo.getIzquierdo() != null) ? nodo.getIzquierdo().getKey() : "-") + "\n" +
                    "\t>> HD:" + ((nodo.getDerecho() != null) ? nodo.getDerecho().getKey() : "-") + "\n");

            toKeyValueStringRecursivo(nodo.getIzquierdo(), cadena);
            toKeyValueStringRecursivo(nodo.getDerecho(), cadena);
        }
    }

    public ArbolAVL clone() {
        // Metodo que clona un arbol y retorna el clon del mismo
        ArbolAVL clon = new ArbolAVL();

        if (this.raiz != null) {
            // Crea un nuevo nodo y se lo asigna a la raiz clon
            clon.raiz = new NodoAVL(this.raiz.getValue(), this.raiz.getKey());

            // Llamada a un metodo privado
            clonRecursivo(clon.raiz, this.raiz);
        }

        return clon;
    }

    private void clonRecursivo(NodoAVL nodoClon, NodoAVL nodoOG) {

        if (nodoOG != null) {

            // Setea HI si es que tiene
            if (nodoOG.getIzquierdo() != null) {
                nodoClon.setIzquierdo(new NodoAVL(nodoOG.getIzquierdo().getValue(), nodoOG.getIzquierdo().getKey()));
                // Recorre y setea los HI
                clonRecursivo(nodoClon.getIzquierdo(), nodoOG.getIzquierdo());
            }

            // Setea HD si es que tiene
            if (nodoOG.getDerecho() != null) {
                nodoClon.setDerecho(new NodoAVL(nodoOG.getDerecho().getValue(), nodoOG.getDerecho().getKey()));
                // Recorre y setea los HD
                clonRecursivo(nodoClon.getDerecho(), nodoOG.getDerecho());
            }
        }
    }

}
