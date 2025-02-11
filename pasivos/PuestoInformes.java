package pasivos;

import java.util.HashMap;

public class PuestoInformes {

    private HashMap<String, PuestoAtencion> puestosAtencion;

    public PuestoInformes(HashMap<String, PuestoAtencion> puestosAtencion) {
        this.puestosAtencion = puestosAtencion;
    }

    // Le da un puesto de atencion a cada pasajero
    public synchronized PuestoAtencion obtenerPuestoAtencion(String reserva) {
        return puestosAtencion.get(reserva);
    }

}
