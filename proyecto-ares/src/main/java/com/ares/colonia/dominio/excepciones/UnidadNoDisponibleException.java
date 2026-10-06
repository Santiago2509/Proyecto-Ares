package com.ares.colonia.dominio.excepciones;

/**
 * ============================================================================
 * ARCHIVO: UnidadNoDisponibleException.java
 * PAQUETE: com.ares.colonia.dominio.excepciones
 * ============================================================================
 *
 * PROPOSITO Y FUNCION PRINCIPAL:
 * Se lanza cuando se intenta asignar a una mision una unidad que ya esta
 * ocupada, averiada o en mantenimiento.
 *
 * ANOTACIONES Y ETIQUETAS UTILIZADAS: Ninguna.
 * HERENCIA E INTERFACES IMPLEMENTADAS:
 * - Extiende de: UnidadException
 * PARTICIPACION EN EL MOTOR POR TURNOS:
 * Lanzada durante validarRequisitos de misiones de exploracion o rescate.
 * ============================================================================
 */
public class UnidadNoDisponibleException extends UnidadException {
    public UnidadNoDisponibleException(String nombreUnidad) {
        super("UNIDAD_NO_DISPONIBLE",
              "La unidad '" + nombreUnidad + "' no esta disponible para la mision.");
    }
}
