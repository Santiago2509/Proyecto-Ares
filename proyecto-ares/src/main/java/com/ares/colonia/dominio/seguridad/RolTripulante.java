package com.ares.colonia.dominio.seguridad;

import java.util.Set;

/**
 * ============================================================================
 * ARCHIVO: RolTripulante.java
 * PAQUETE: com.ares.colonia.dominio.seguridad
 * ============================================================================
 *
 * PROPOSITO Y FUNCION PRINCIPAL:
 * Rol base de todo tripulante sin cargo especial. Solo puede consultar
 * su propia vitalidad y sus tareas asignadas. Es el rol con el menor
 * nivel de acceso del sistema.
 *
 * ANOTACIONES Y ETIQUETAS UTILIZADAS: Ninguna.
 * HERENCIA E INTERFACES IMPLEMENTADAS:
 * - Extiende de: Rol
 * PARTICIPACION EN EL MOTOR POR TURNOS: Solo lectura de datos propios.
 * ============================================================================
 */
public class RolTripulante extends Rol {

    /** Permisos minimos de un tripulante sin cargo. */
    private static final Set<Permiso> PERMISOS = Set.of(
            Permiso.VER_VITALIDAD_PROPIA
    );

    public RolTripulante() {
        super("Tripulante");
    }

    @Override
    public Set<Permiso> permisos() { return PERMISOS; }

    @Override
    public boolean puede(Permiso permiso) { return PERMISOS.contains(permiso); }
}
