package com.ares.colonia.dominio.excepciones;

/**
 * ============================================================================
 * ARCHIVO: ModuloAveriadoException.java
 * PAQUETE: com.ares.colonia.dominio.excepciones
 * ============================================================================
 *
 * PROPOSITO Y FUNCION PRINCIPAL:
 * Se lanza cuando se intenta operar un modulo que ha alcanzado un nivel
 * de desgaste critico (>= 100 %) y no puede funcionar sin reparacion.
 *
 * ANOTACIONES Y ETIQUETAS UTILIZADAS: Ninguna.
 * HERENCIA E INTERFACES IMPLEMENTADAS:
 * - Extiende de: ModuloException
 * PARTICIPACION EN EL MOTOR POR TURNOS:
 * Lanzada durante el calculo de eficiencia si desgaste == MAX.
 * ============================================================================
 */
public class ModuloAveriadoException extends ModuloException {
    public ModuloAveriadoException(String nombreModulo) {
        super("MODULO_AVERIADO",
              "El modulo '" + nombreModulo + "' esta averiado y no puede operar.");
    }
}
