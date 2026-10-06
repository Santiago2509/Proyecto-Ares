package com.ares.colonia.dominio.modelo.recurso;

/**
 * ============================================================================
 * ARCHIVO: RecursoPerecedero.java
 * PAQUETE: com.ares.colonia.dominio.modelo.recurso
 * ============================================================================
 *
 * PROPOSITO Y FUNCION PRINCIPAL:
 * Familia de recursos que se degradan con el paso del tiempo (Comida).
 * Cada turno se pierde un porcentaje del stock por descomposicion natural,
 * obligando a la colonia a mantener produccion constante en el Invernadero.
 *
 * ANOTACIONES Y ETIQUETAS UTILIZADAS: Ninguna.
 * HERENCIA E INTERFACES IMPLEMENTADAS:
 * - Extiende de: Recurso
 * PARTICIPACION EN EL MOTOR POR TURNOS:
 * actualizarPorTurno() reduce el stock ANTES del consumo de la tripulacion.
 * ============================================================================
 */
public class RecursoPerecedero extends Recurso {

    /** Porcentaje del stock que se pierde por turno (ej. 0.02 = 2%). */
    private final double tasaDegradacion;

    public RecursoPerecedero(TipoRecurso tipo, double cantidadInicial,
                              double capacidadMaxima, double umbralCritico,
                              double tasaDegradacion) {
        super(tipo, cantidadInicial, capacidadMaxima, umbralCritico);
        this.tasaDegradacion = Math.max(0, Math.min(1, tasaDegradacion));
    }

    /**
     * Aplica la degradacion natural: pierde tasaDegradacion% del stock actual.
     */
    @Override
    public void actualizarPorTurno() {
        double perdida = getCantidad() * tasaDegradacion;
        if (perdida > 0 && getCantidad() > 0) {
            consumir(Math.min(perdida, getCantidad()));
        }
    }

    public double getTasaDegradacion() { return tasaDegradacion; }
}
