package com.ares.colonia.dominio.seguridad;

import java.lang.annotation.*;

/**
 * ============================================================================
 * ARCHIVO: RequierePermiso.java
 * PAQUETE: com.ares.colonia.dominio.seguridad
 * ============================================================================
 *
 * PROPOSITO Y FUNCION PRINCIPAL:
 * Anotacion que se coloca sobre los metodos de servicio para declarar
 * que solo pueden ser invocados por tripulantes cuyo Rol posea el permiso
 * especificado. El control de acceso se implementa mediante reflexion:
 * antes de ejecutar el metodo, se verifica que el rol activo tenga
 * el permiso requerido; si no, se lanza AccesoDenegadoException.
 *
 * ANOTACIONES Y ETIQUETAS UTILIZADAS:
 * - @Target: METODO (solo aplicable a metodos).
 * - @Retention: RUNTIME (debe estar disponible en reflexion).
 * - @Documented: aparece en Javadoc.
 *
 * HERENCIA E INTERFACES IMPLEMENTADAS: Ninguna.
 * PARTICIPACION EN EL MOTOR POR TURNOS:
 * Evaluada en cada operacion de servicio que modifica el estado de la colonia.
 * ============================================================================
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface RequierePermiso {

    /**
     * El permiso que debe poseer el rol activo para ejecutar el metodo anotado.
     *
     * @return el Permiso requerido
     */
    Permiso value();
}
