package com.ares.colonia.dominio.seguridad;

import java.util.Set;

/**
 * ============================================================================
 * ARCHIVO: Rol.java
 * PAQUETE: com.ares.colonia.dominio.seguridad
 * ============================================================================
 *
 * PROPOSITO Y FUNCION PRINCIPAL:
 * Clase abstracta base del sistema de roles y permisos. Cada tripulante
 * posee un Rol por composicion, desacoplando la especialidad profesional
 * (Ingeniero, Medico, etc.) del cargo administrativo (LiderDeColonia,
 * AdministradorDeColonia, etc.). Esta separacion permite que un Ingeniero
 * sea al mismo tiempo LiderDeColonia sin alterar la jerarquia de Tripulante.
 *
 * ANOTACIONES Y ETIQUETAS UTILIZADAS:
 * - Ninguna directa; la anotacion @RequierePermiso se usa en los metodos
 *   de servicio que verifican los permisos de este Rol.
 *
 * HERENCIA E INTERFACES IMPLEMENTADAS:
 * - Extiende de: Ninguna
 * - Implementa: Ninguna
 *
 * PARTICIPACION EN EL MOTOR POR TURNOS:
 * El motor verifica el Rol del tripulante activo antes de permitir
 * operaciones de modificacion de estado en la colonia.
 * ============================================================================
 */
public abstract class Rol {

    /** Nombre descriptivo del rol (ej. "Lider de Colonia"). */
    private final String nombre;

    /**
     * Construye el rol con su nombre identificador.
     *
     * @param nombre nombre descriptivo del rol
     */
    protected Rol(String nombre) {
        this.nombre = nombre;
    }

    /**
     * Retorna el conjunto inmutable de permisos que posee este rol.
     * Cada subclase define su propio conjunto de permisos.
     *
     * @return conjunto de Permiso del rol
     */
    public abstract Set<Permiso> permisos();

    /**
     * Verifica si este rol posee un permiso especifico.
     * Usado por el mecanismo de reflexion de @RequierePermiso.
     *
     * @param permiso el permiso a verificar
     * @return true si el rol posee el permiso
     */
    public abstract boolean puede(Permiso permiso);

    /**
     * Retorna el nombre descriptivo del rol.
     *
     * @return nombre del rol
     */
    public String getNombre() { return nombre; }

    @Override
    public String toString() {
        return "Rol[" + nombre + "]";
    }
}
