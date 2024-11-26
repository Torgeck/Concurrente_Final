package pasivos;

import console.Console;

import java.util.Random;
import java.util.concurrent.Semaphore;
import java.util.concurrent.locks.ReentrantLock;

public class Freeshop {

    private Caja[] cajas;
    private int capMax;
    private int capActual;
    private char idFreeshop;
    private Semaphore semCapacidad;
    private ReentrantLock modificar;
    private Random random;

    public Freeshop(char idTerminal, int cantCajas, int capMax) {
        this.idFreeshop = idTerminal;
        this.cajas = new Caja[cantCajas];
        this.capMax = capMax;
        this.capActual = 0;
        this.semCapacidad = new Semaphore(capMax);  // No necesita ser fair
        this.random = new Random();
        this.modificar = new ReentrantLock();
        iniCajas();
    }

    private void iniCajas() {
        // Metodo que crea y asigna cajas creadas al arreglo de cajas
        Caja caja;
        for (int i = 0; i < this.cajas.length; i++) {
            caja = new Caja(this.idFreeshop);
            this.cajas[i] = caja;
        }
    }

    public Caja[] getCajas() {
        return cajas;
    }

    // Metodos para modificar y checkear variables
    private int getCapActual() {
        int respuesta = -1;
        modificar.lock();
        try {
            respuesta = capActual;
        } catch (Exception e) {
            System.out.println("ERROR al querer acceder a la variable capActual en Freeshop");
        } finally {
            modificar.unlock();
        }
        return respuesta;
    }

    private void modificarCapActual(int x) {
        modificar.lock();
        try {
            this.capActual += x;
            System.out.println(Console.colorString("PURPLE", "CAPACIDAD ACTUAL FS" + this.idFreeshop + ": " + this.capActual + " DE MAXIMO: " + this.capMax));
        } catch (Exception e) {
            System.out.println(Console.colorString("RED", "ERROR al querer aumentar capActual en Freeshop"));
        } finally {
            modificar.unlock();
        }
    }
    
    public void liberarCajeros() {
        for (Caja caja : this.cajas) {
            caja.liberarCajero();
        }
    }

    // Metodos pasajeros
    public boolean ingresarFreeshop() {
        boolean compra = false;
        try {
            // Ingresa al FS
            semCapacidad.acquire();
            // Aumenta la capacidad actual
            modificarCapActual(1);
            compra = this.hizoCompra();
        } catch (InterruptedException e) {
            System.out.println(Console.colorString("RED", "ERROR al querer ingresar freeshop"));
        }
        return compra;
    }

    public void salirFreeshop() {
        // Reduce la capacidad actual
        modificarCapActual(-1);
        // Libera un semCapacida
        semCapacidad.release();
    }

    private boolean hizoCompra() {
        // Hace un 50/50 para saber si pasajero quiere comprar en el freeshop
        return random.nextBoolean();
    }

    public Caja getCajaMenosOcupada() {
        // Retorna la caja con menor fila
        return this.cajas[0].getCantClientes() <= this.cajas[1].getCantClientes() ? this.cajas[0] : this.cajas[1];
    }
}
