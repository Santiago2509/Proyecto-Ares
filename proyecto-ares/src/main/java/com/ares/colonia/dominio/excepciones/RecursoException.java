package com.ares.colonia.dominio.excepciones;

/**
 * ============================================================================
 * ARCHIVO: RecursoException.java
 * PAQUETE: com.ares.colonia.dominio.excepciones
 * ============================================================================
 *
 * PROPOSITO Y FUNCION PRINCIPAL:
 * Agrupa todos los errores relacionados con los recursos vitales de la
 * colonia (oxigeno, agua, energia, comida, repuestos). Se lanza cuando
 * el inventario no puede satisfacer una demanda o supera su capacidad.
 *
 * ANOTACIONES Y ETIQUETAS UTILIZADAS: Ninguna.
 * HERENCIA E INTERFACES IMPLEMENTADAS:
 * - Extiende de: SistemaColonialException
 * PARTICIPACION EN EL MOTOR POR TURNOS:
 * Evaluada durante la fase de produccion/consumo de recursos (paso 2 del turno).
 * ============================================================================
 */
public class RecursoException extends SistemaColonialException {
    public RecursoException(String codigo, String mensaje) {
        super(codigo, mensaje);
    }
}
