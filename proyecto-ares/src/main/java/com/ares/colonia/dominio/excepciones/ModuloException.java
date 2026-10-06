package com.ares.colonia.dominio.excepciones;

/**
 * ============================================================================
 * ARCHIVO: ModuloException.java
 * PAQUETE: com.ares.colonia.dominio.excepciones
 * ============================================================================
 *
 * PROPOSITO Y FUNCION PRINCIPAL:
 * Agrupa errores relacionados con los modulos de la colonia (habitacional,
 * laboratorio, invernadero, etc.). Cubre fallos, incompatibilidades y
 * estados no operativos.
 *
 * ANOTACIONES Y ETIQUETAS UTILIZADAS: Ninguna.
 * HERENCIA E INTERFACES IMPLEMENTADAS:
 * - Extiende de: SistemaColonialException
 * PARTICIPACION EN EL MOTOR POR TURNOS:
 * Evaluada en la fase de eficiencia y desgaste de modulos (paso 2 del turno).
 * ============================================================================
 */
public class ModuloException extends SistemaColonialException {
    public ModuloException(String codigo, String mensaje) {
        super(codigo, mensaje);
    }
}
