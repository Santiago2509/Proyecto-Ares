package com.ares.colonia.dominio.excepciones;

/**
 * ============================================================================
 * ARCHIVO: MoralCriticaException.java
 * PAQUETE: com.ares.colonia.dominio.excepciones
 * ============================================================================
 *
 * PROPOSITO Y FUNCION PRINCIPAL:
 * Se lanza cuando la moral media de la colonia cae por debajo del umbral
 * critico, lo que reduce la eficiencia de todos los modulos y aumenta
 * el riesgo de desercion o conflictos internos.
 *
 * ANOTACIONES Y ETIQUETAS UTILIZADAS: Ninguna.
 * HERENCIA E INTERFACES IMPLEMENTADAS:
 * - Extiende de: TripulacionException
 * PARTICIPACION EN EL MOTOR POR TURNOS:
 * Evaluada al final de la fase de vitalidad como promedio de la colonia.
 * ============================================================================
 */
public class MoralCriticaException extends TripulacionException {
    public MoralCriticaException(String nombreColonia, double moralActual) {
        super("MORAL_CRITICA",
              "La moral de la colonia '" + nombreColonia + "' es critica: " + moralActual + "%");
    }
}
