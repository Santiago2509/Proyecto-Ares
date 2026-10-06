package com.ares.colonia.dominio.excepciones;

/**
 * ============================================================================
 * ARCHIVO: ModuloIncompatibleException.java
 * PAQUETE: com.ares.colonia.dominio.excepciones
 * ============================================================================
 *
 * PROPOSITO Y FUNCION PRINCIPAL:
 * Se lanza cuando se intenta asignar un modulo a una colonia que no
 * cumple los prerequisitos de infraestructura (ej. hangar sin planta de energia).
 *
 * ANOTACIONES Y ETIQUETAS UTILIZADAS: Ninguna.
 * HERENCIA E INTERFACES IMPLEMENTADAS:
 * - Extiende de: ModuloException
 * PARTICIPACION EN EL MOTOR POR TURNOS:
 * Evaluada durante la mision ConstruccionDeModulo.
 * ============================================================================
 */
public class ModuloIncompatibleException extends ModuloException {
    public ModuloIncompatibleException(String tipoModulo, String requisito) {
        super("MODULO_INCOMPATIBLE",
              "El modulo '" + tipoModulo + "' requiere: " + requisito);
    }
}
