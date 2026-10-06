package com.ares.colonia.dominio.excepciones;

/**
 * ============================================================================
 * ARCHIVO: EquipoInsuficienteException.java
 * PAQUETE: com.ares.colonia.dominio.excepciones
 * ============================================================================
 *
 * PROPOSITO Y FUNCION PRINCIPAL:
 * Se lanza cuando la mision no puede lanzarse porque la colonia no cuenta
 * con el numero minimo de tripulantes o unidades requeridos.
 *
 * ANOTACIONES Y ETIQUETAS UTILIZADAS: Ninguna.
 * HERENCIA E INTERFACES IMPLEMENTADAS:
 * - Extiende de: MisionException
 * PARTICIPACION EN EL MOTOR POR TURNOS:
 * Evaluada durante validarRequisitos antes de iniciar la mision.
 * ============================================================================
 */
public class EquipoInsuficienteException extends MisionException {
    public EquipoInsuficienteException(String nombreMision, String especialidadFaltante) {
        super("EQUIPO_INSUFICIENTE",
              "Mision '" + nombreMision + "' requiere: " + especialidadFaltante);
    }
}
