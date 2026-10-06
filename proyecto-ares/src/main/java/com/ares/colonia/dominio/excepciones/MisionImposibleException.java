package com.ares.colonia.dominio.excepciones;

/**
 * ============================================================================
 * ARCHIVO: MisionImposibleException.java
 * PAQUETE: com.ares.colonia.dominio.excepciones
 * ============================================================================
 *
 * PROPOSITO Y FUNCION PRINCIPAL:
 * Se lanza cuando una mision no puede ejecutarse porque las condiciones
 * del entorno o de la colonia la hacen inviable (tormenta de arena activa,
 * modulos danados criticos, etc.).
 *
 * ANOTACIONES Y ETIQUETAS UTILIZADAS: Ninguna.
 * HERENCIA E INTERFACES IMPLEMENTADAS:
 * - Extiende de: MisionException
 * PARTICIPACION EN EL MOTOR POR TURNOS:
 * Lanzada en validarRequisitos(Colonia) de cada subclase de Mision.
 * ============================================================================
 */
public class MisionImposibleException extends MisionException {
    public MisionImposibleException(String nombreMision, String motivo) {
        super("MISION_IMPOSIBLE",
              "La mision '" + nombreMision + "' no puede ejecutarse: " + motivo);
    }
}
