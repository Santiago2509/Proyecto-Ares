package com.ares.colonia.dominio.seguridad;

import java.util.Set;

/**
 * ============================================================================
 * ARCHIVO: AdministradorGeneral.java
 * PAQUETE: com.ares.colonia.dominio.seguridad
 * ============================================================================
 *
 * PROPOSITO Y FUNCION PRINCIPAL:
 * Rol con maximo alcance: toda la RedColonial. Puede ver y comparar todas
 * las colonias, aprobar transferencias de recursos entre colonias y
 * administrar la red completa. Solo existe un AdministradorGeneral.
 *
 * ANOTACIONES Y ETIQUETAS UTILIZADAS: Ninguna.
 * HERENCIA E INTERFACES IMPLEMENTADAS:
 * - Extiende de: Rol
 * PARTICIPACION EN EL MOTOR POR TURNOS:
 * Aprueba transferencias inter-coloniales durante la fase de misiones.
 * ============================================================================
 */
public class AdministradorGeneral extends Rol {

    /** Todos los permisos del sistema. */
    private static final Set<Permiso> PERMISOS = Set.of(Permiso.values());

    public AdministradorGeneral() { super("Administrador General"); }

    @Override
    public Set<Permiso> permisos() { return PERMISOS; }

    @Override
    public boolean puede(Permiso permiso) { return true; } // acceso total
}
