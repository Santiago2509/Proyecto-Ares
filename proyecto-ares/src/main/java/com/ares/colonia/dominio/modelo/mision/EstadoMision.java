package com.ares.colonia.dominio.modelo.mision;

/**
 * ============================================================================
 * ARCHIVO: EstadoMision.java
 * PAQUETE: com.ares.colonia.dominio.modelo.mision
 * ============================================================================
 *
 * PROPOSITO Y FUNCION PRINCIPAL:
 * Enumerado que representa el ciclo de vida de una mision en la colonia.
 *
 * ANOTACIONES Y ETIQUETAS UTILIZADAS: Ninguna.
 * HERENCIA E INTERFACES IMPLEMENTADAS: Ninguna.
 * PARTICIPACION EN EL MOTOR POR TURNOS:
 * El motor solo procesa misiones en estado EN_CURSO. Las PENDIENTES
 * esperan aprobacion del LiderDeColonia.
 * ============================================================================
 */
public enum EstadoMision {
    /** Propuesta pero no aprobada aun. */
    PENDIENTE,
    /** Aprobada y en ejecucion. */
    EN_CURSO,
    /** Finalizada exitosamente. */
    COMPLETADA,
    /** Interrumpida antes de completarse. */
    CANCELADA,
    /** Fallo durante la ejecucion. */
    FALLIDA
}
