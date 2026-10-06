package com.ares.colonia.dominio.excepciones;

/**
 * ============================================================================
 * ARCHIVO: AccesoDenegadoException.java
 * PAQUETE: com.ares.colonia.dominio.excepciones
 * ============================================================================
 *
 * PROPOSITO Y FUNCION PRINCIPAL:
 * Se lanza cuando un tripulante intenta ejecutar una operacion para la
 * cual su rol no tiene el permiso requerido. La evaluacion ocurre en
 * tiempo de ejecucion mediante reflexion sobre @RequierePermiso.
 *
 * ANOTACIONES Y ETIQUETAS UTILIZADAS: Ninguna.
 * HERENCIA E INTERFACES IMPLEMENTADAS:
 * - Extiende de: SeguridadException
 * PARTICIPACION EN EL MOTOR POR TURNOS: No aplica.
 * ============================================================================
 */
public class AccesoDenegadoException extends SeguridadException {
    public AccesoDenegadoException(String nombreRol, String permiso) {
        super("ACCESO_DENEGADO",
              "El rol '" + nombreRol + "' no tiene el permiso: " + permiso);
    }
}
