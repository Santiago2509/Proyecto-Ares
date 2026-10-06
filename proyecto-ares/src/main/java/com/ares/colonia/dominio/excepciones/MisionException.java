package com.ares.colonia.dominio.excepciones;

/**
 * ============================================================================
 * ARCHIVO: MisionException.java
 * PAQUETE: com.ares.colonia.dominio.excepciones
 * ============================================================================
 *
 * PROPOSITO Y FUNCION PRINCIPAL:
 * Agrupa los errores relacionados con la validacion y ejecucion de misiones.
 *
 * ANOTACIONES Y ETIQUETAS UTILIZADAS: Ninguna.
 * HERENCIA E INTERFACES IMPLEMENTADAS:
 * - Extiende de: SistemaColonialException
 * PARTICIPACION EN EL MOTOR POR TURNOS:
 * Evaluada en la fase de misiones (paso 4 del turno).
 * ============================================================================
 */
public class MisionException extends SistemaColonialException {
    public MisionException(String codigo, String mensaje) {
        super(codigo, mensaje);
    }
}
