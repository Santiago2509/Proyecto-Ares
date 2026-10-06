package com.ares.colonia.dominio.excepciones;

/**
 * ============================================================================
 * ARCHIVO: ColoniaNoEncontradaException.java
 * PAQUETE: com.ares.colonia.dominio.excepciones
 * ============================================================================
 *
 * PROPOSITO Y FUNCION PRINCIPAL:
 * Se lanza cuando se intenta operar sobre una colonia cuyo identificador
 * no existe en la RedColonial.
 *
 * ANOTACIONES Y ETIQUETAS UTILIZADAS: Ninguna.
 * HERENCIA E INTERFACES IMPLEMENTADAS:
 * - Extiende de: ColoniaException
 * PARTICIPACION EN EL MOTOR POR TURNOS:
 * Puede lanzarse al buscar la colonia destino en una transferencia
 * de recursos entre colonias.
 * ============================================================================
 */
public class ColoniaNoEncontradaException extends ColoniaException {
    public ColoniaNoEncontradaException(String idColonia) {
        super("COLONIA_NO_ENCONTRADA",
              "No se encontro ninguna colonia con el id: " + idColonia);
    }
}
