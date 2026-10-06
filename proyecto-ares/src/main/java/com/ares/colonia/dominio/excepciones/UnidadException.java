package com.ares.colonia.dominio.excepciones;

/**
 * ============================================================================
 * ARCHIVO: UnidadException.java
 * PAQUETE: com.ares.colonia.dominio.excepciones
 * ============================================================================
 *
 * PROPOSITO Y FUNCION PRINCIPAL:
 * Agrupa los errores relacionados con las unidades roboticas (Rover, Dron,
 * RobotMinero) de la colonia.
 *
 * ANOTACIONES Y ETIQUETAS UTILIZADAS: Ninguna.
 * HERENCIA E INTERFACES IMPLEMENTADAS:
 * - Extiende de: SistemaColonialException
 * PARTICIPACION EN EL MOTOR POR TURNOS:
 * Evaluada durante la asignacion de unidades a misiones.
 * ============================================================================
 */
public class UnidadException extends SistemaColonialException {
    public UnidadException(String codigo, String mensaje) {
        super(codigo, mensaje);
    }
}
