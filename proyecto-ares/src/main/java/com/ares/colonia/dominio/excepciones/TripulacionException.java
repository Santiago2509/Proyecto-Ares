package com.ares.colonia.dominio.excepciones;

/**
 * ============================================================================
 * ARCHIVO: TripulacionException.java
 * PAQUETE: com.ares.colonia.dominio.excepciones
 * ============================================================================
 *
 * PROPOSITO Y FUNCION PRINCIPAL:
 * Agrupa todos los errores relacionados con el estado de los tripulantes:
 * signos vitales criticos, fallecimiento y moral colectiva baja.
 *
 * ANOTACIONES Y ETIQUETAS UTILIZADAS: Ninguna.
 * HERENCIA E INTERFACES IMPLEMENTADAS:
 * - Extiende de: SistemaColonialException
 * PARTICIPACION EN EL MOTOR POR TURNOS:
 * Evaluada en la fase de vitalidad (paso 3 del turno).
 * ============================================================================
 */
public class TripulacionException extends SistemaColonialException {
    public TripulacionException(String codigo, String mensaje) {
        super(codigo, mensaje);
    }
}
