package com.ares.colonia.dominio.excepciones;

/**
 * ============================================================================
 * ARCHIVO: PermisoInsuficienteException.java
 * PAQUETE: com.ares.colonia.dominio.excepciones
 * ============================================================================
 *
 * PROPOSITO Y FUNCION PRINCIPAL:
 * Se lanza cuando el tripulante tiene el permiso pero no el nivel de alcance
 * requerido (ej. AdministradorDeColonia intenta operar sobre una colonia ajena).
 *
 * ANOTACIONES Y ETIQUETAS UTILIZADAS: Ninguna.
 * HERENCIA E INTERFACES IMPLEMENTADAS:
 * - Extiende de: SeguridadException
 * PARTICIPACION EN EL MOTOR POR TURNOS: No aplica.
 * ============================================================================
 */
public class PermisoInsuficienteException extends SeguridadException {
    public PermisoInsuficienteException(String nombreRol, String alcance) {
        super("PERMISO_INSUFICIENTE",
              "El rol '" + nombreRol + "' no tiene alcance suficiente: " + alcance);
    }
}
