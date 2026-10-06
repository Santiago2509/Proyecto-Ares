package com.ares.colonia.dominio.excepciones;

/**
 * ============================================================================
 * ARCHIVO: RecursoAgotadoException.java
 * PAQUETE: com.ares.colonia.dominio.excepciones
 * ============================================================================
 *
 * PROPOSITO Y FUNCION PRINCIPAL:
 * Se lanza cuando el inventario de un recurso llega a cero y no puede
 * cubrir la demanda de consumo del turno. Dispara alertas criticas y
 * puede causar reduccion de vitalidad en la tripulacion.
 *
 * ANOTACIONES Y ETIQUETAS UTILIZADAS: Ninguna.
 * HERENCIA E INTERFACES IMPLEMENTADAS:
 * - Extiende de: RecursoException
 * PARTICIPACION EN EL MOTOR POR TURNOS:
 * Lanzada en la fase de consumo de recursos (paso 2) cuando cantidad < demanda.
 * ============================================================================
 */
public class RecursoAgotadoException extends RecursoException {
    public RecursoAgotadoException(String tipoRecurso) {
        super("RECURSO_AGOTADO",
              "El recurso '" + tipoRecurso + "' se ha agotado en la colonia.");
    }
}
