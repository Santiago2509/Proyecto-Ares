package com.ares.colonia.dominio.excepciones;

/**
 * ============================================================================
 * ARCHIVO: SeguridadException.java
 * PAQUETE: com.ares.colonia.dominio.excepciones
 * ============================================================================
 *
 * PROPOSITO Y FUNCION PRINCIPAL:
 * Agrupa los errores relacionados con el sistema de roles y permisos.
 * Es lanzada por el mecanismo de evaluacion de la anotacion @RequierePermiso.
 *
 * ANOTACIONES Y ETIQUETAS UTILIZADAS: Ninguna.
 * HERENCIA E INTERFACES IMPLEMENTADAS:
 * - Extiende de: SistemaColonialException
 * PARTICIPACION EN EL MOTOR POR TURNOS:
 * No participa directamente en el turno; es parte del control de acceso.
 * ============================================================================
 */
public class SeguridadException extends SistemaColonialException {
    public SeguridadException(String codigo, String mensaje) {
        super(codigo, mensaje);
    }
}
