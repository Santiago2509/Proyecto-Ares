package com.ares.colonia.dominio.excepciones;

/**
 * ============================================================================
 * ARCHIVO: TripulanteFallecidoException.java
 * PAQUETE: com.ares.colonia.dominio.excepciones
 * ============================================================================
 *
 * PROPOSITO Y FUNCION PRINCIPAL:
 * Se lanza cuando el EstadoVital de un tripulante llega a FALLECIDO.
 * Desencadena el retiro del tripulante de la colonia, actualiza la moral
 * de los supervivientes y registra el evento en el historial.
 *
 * ANOTACIONES Y ETIQUETAS UTILIZADAS: Ninguna.
 * HERENCIA E INTERFACES IMPLEMENTADAS:
 * - Extiende de: TripulacionException
 * PARTICIPACION EN EL MOTOR POR TURNOS:
 * Lanzada en la fase de vitalidad cuando salud llega a 0.
 * ============================================================================
 */
public class TripulanteFallecidoException extends TripulacionException {
    public TripulanteFallecidoException(String nombreTripulante) {
        super("TRIPULANTE_FALLECIDO",
              "El tripulante '" + nombreTripulante + "' ha fallecido. Se actualiza la moral de la colonia.");
    }
}
