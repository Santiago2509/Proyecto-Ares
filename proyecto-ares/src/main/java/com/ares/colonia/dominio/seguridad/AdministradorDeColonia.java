package com.ares.colonia.dominio.seguridad;

import java.util.Set;

/**
 * ============================================================================
 * ARCHIVO: AdministradorDeColonia.java
 * PAQUETE: com.ares.colonia.dominio.seguridad
 * ============================================================================
 *
 * PROPOSITO Y FUNCION PRINCIPAL:
 * Rol administrativo con alcance sobre toda su colonia. Gestiona modulos,
 * recursos y tripulantes. No puede aprobar misiones (eso es del Lider),
 * pero puede registrar nuevos tripulantes y generar reportes.
 *
 * ANOTACIONES Y ETIQUETAS UTILIZADAS: Ninguna.
 * HERENCIA E INTERFACES IMPLEMENTADAS:
 * - Extiende de: Rol
 * PARTICIPACION EN EL MOTOR POR TURNOS:
 * Puede intervenir en la gestion de recursos durante la fase de produccion/consumo.
 * ============================================================================
 */
public class AdministradorDeColonia extends Rol {

    private static final Set<Permiso> PERMISOS = Set.of(
            Permiso.VER_VITALIDAD_PROPIA,
            Permiso.VER_VITALIDAD_TODOS,
            Permiso.GESTIONAR_MODULOS,
            Permiso.GESTIONAR_RECURSOS,
            Permiso.REGISTRAR_TRIPULANTE,
            Permiso.PROPONER_MISION,
            Permiso.GENERAR_REPORTE_COLONIA
    );

    public AdministradorDeColonia() { super("Administrador de Colonia"); }

    @Override
    public Set<Permiso> permisos() { return PERMISOS; }

    @Override
    public boolean puede(Permiso permiso) { return PERMISOS.contains(permiso); }
}
