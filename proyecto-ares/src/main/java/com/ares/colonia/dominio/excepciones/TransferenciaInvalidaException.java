package com.ares.colonia.dominio.excepciones;

/**
 * ============================================================================
 * ARCHIVO: TransferenciaInvalidaException.java
 * PAQUETE: com.ares.colonia.dominio.excepciones
 * ============================================================================
 *
 * PROPOSITO Y FUNCION PRINCIPAL:
 * Se lanza cuando una transferencia de recursos entre colonias viola
 * las reglas de negocio: cantidad negativa, colonia sin excedente,
 * o colonia destino sin capacidad.
 *
 * ANOTACIONES Y ETIQUETAS UTILIZADAS: Ninguna.
 * HERENCIA E INTERFACES IMPLEMENTADAS:
 * - Extiende de: ColoniaException
 * PARTICIPACION EN EL MOTOR POR TURNOS:
 * Evaluada durante la fase de misiones de rescate inter-colonial.
 * ============================================================================
 */
public class TransferenciaInvalidaException extends ColoniaException {
    public TransferenciaInvalidaException(String detalle) {
        super("TRANSFERENCIA_INVALIDA", "Transferencia invalida: " + detalle);
    }
}
