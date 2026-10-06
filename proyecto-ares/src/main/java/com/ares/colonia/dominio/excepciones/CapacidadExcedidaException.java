package com.ares.colonia.dominio.excepciones;

/**
 * ============================================================================
 * ARCHIVO: CapacidadExcedidaException.java
 * PAQUETE: com.ares.colonia.dominio.excepciones
 * ============================================================================
 *
 * PROPOSITO Y FUNCION PRINCIPAL:
 * Se lanza cuando se intenta agregar mas recursos de los que el inventario
 * puede almacenar, evitando overflow silencioso del stock.
 *
 * ANOTACIONES Y ETIQUETAS UTILIZADAS: Ninguna.
 * HERENCIA E INTERFACES IMPLEMENTADAS:
 * - Extiende de: RecursoException
 * PARTICIPACION EN EL MOTOR POR TURNOS:
 * Evaluada al recibir suministros o produccion que supera la capacidad maxima.
 * ============================================================================
 */
public class CapacidadExcedidaException extends RecursoException {
    public CapacidadExcedidaException(String tipoRecurso, double capacidadMax) {
        super("CAPACIDAD_EXCEDIDA",
              "La capacidad maxima de '" + tipoRecurso + "' es " + capacidadMax + " unidades.");
    }
}
