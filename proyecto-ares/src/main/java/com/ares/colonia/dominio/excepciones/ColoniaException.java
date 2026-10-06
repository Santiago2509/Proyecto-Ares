package com.ares.colonia.dominio.excepciones;

/**
 * ============================================================================
 * ARCHIVO: ColoniaException.java
 * PAQUETE: com.ares.colonia.dominio.excepciones
 * ============================================================================
 *
 * PROPOSITO Y FUNCION PRINCIPAL:
 * Excepcion de segundo nivel que agrupa todos los errores relacionados
 * con la gestion de colonias dentro de la RedColonial (busqueda fallida,
 * transferencias invalidas, etc.).
 *
 * ANOTACIONES Y ETIQUETAS UTILIZADAS: Ninguna.
 * HERENCIA E INTERFACES IMPLEMENTADAS:
 * - Extiende de: SistemaColonialException
 * PARTICIPACION EN EL MOTOR POR TURNOS: lanzada durante validaciones de colonia.
 * ============================================================================
 */
public class ColoniaException extends SistemaColonialException {
    public ColoniaException(String codigo, String mensaje) {
        super(codigo, mensaje);
    }
}
