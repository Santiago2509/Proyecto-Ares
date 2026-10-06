package com.ares.colonia.dominio.excepciones;

/**
 * ============================================================================
 * ARCHIVO: TripulanteCriticoException.java
 * PAQUETE: com.ares.colonia.dominio.excepciones
 * ============================================================================
 *
 * PROPOSITO Y FUNCION PRINCIPAL:
 * Se lanza cuando el EstadoVital de un tripulante cae a CRITICO.
 * Dispara una alerta Observer que notifica al LiderDeColonia y
 * activa protocolos de atencion medica de emergencia.
 *
 * ANOTACIONES Y ETIQUETAS UTILIZADAS: Ninguna.
 * HERENCIA E INTERFACES IMPLEMENTADAS:
 * - Extiende de: TripulacionException
 * PARTICIPACION EN EL MOTOR POR TURNOS:
 * Lanzada en la fase de vitalidad cuando salud o hidratacion < umbral critico.
 * ============================================================================
 */
public class TripulanteCriticoException extends TripulacionException {
    public TripulanteCriticoException(String nombreTripulante) {
        super("TRIPULANTE_CRITICO",
              "El tripulante '" + nombreTripulante + "' ha alcanzado un estado vital CRITICO.");
    }
}
