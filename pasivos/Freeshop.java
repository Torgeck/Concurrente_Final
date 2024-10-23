package pasivos;

import hilos.Cajero;

import java.util.Random;
import java.util.concurrent.locks.ReentrantLock;

public class Freeshop {

    private Cajero[] cajero;
    private int capMax;
    private int capActual;
    private ReentrantLock modificar;
    private Random random;

    public Freeshop(Cajero[] cajero) {
        this.cajero = cajero;
    }

    // Metodos para modificar y checkear variables
    public int getCapActual() {
        int respuesta = -1;

        try {
            modificar.lock();
            respuesta = capActual;
        } catch (Exception e) {
            System.out.println("ERROR al querer acceder a la variable capActual en Freeshop");
            ;
        } finally {
            modificar.unlock();
        }
        return respuesta;
    }

    public void disminuirCapActual() {
        try {
            modificar.lock();
            capActual--;
        } catch (Exception e) {
            System.out.println("ERROR al querer disminuir capActual en Freeshop");
        } finally {
            modificar.unlock();
        }
    }

    public void aumentarCapActual() {
        try {
            modificar.lock();
            capActual++;
        } catch (Exception e) {
            System.out.println("ERROR al querer aumentar capActual en Freeshop");
        } finally {
            modificar.unlock();
        }
    }

    // TODO hacer toda la logica para freeshop
    // Metodos pasajeros
    public void ingresarFreeshop() {
    }

    public void salirFreeshop() {
    }

    public boolean hizoCompra() {
        // Hace un 50/50 para saber si pasajero quiere comprar en el freeshop
        return random.nextBoolean();
    }

    public void esperarCajero() {
    }

    public void liberarCajero() {
    }

    // Metodos cajeros
    public void esperarCliente() {
    }

    public void liberarCliente() {
    }


}
