package com.ares.colonia.dominio.seguridad;

import java.util.Set;

/**
 * ============================================================================
 * ARCHIVO: LiderDeColonia.java
 * PAQUETE: com.ares.colonia.dominio.seguridad
 * ============================================================================
 *
 * PROPOSITO Y FUNCION PRINCIPAL:
 * Rol de mayor autoridad dentro de una colonia individual. Puede declarar
 * emergencias, aprobar misiones, asignar tripulantes y ver la vitalidad
 * de toda la colonia. Su alcance se limita a su propia colonia.
 *
 * ANOTACIONES Y ETIQUETAS UTILIZADAS: Ninguna.
 * HERENCIA E INTERFACES IMPLEMENTADAS:
 * - Extiende de: Rol
 * PARTICIPACION EN EL MOTOR POR TURNOS:
 * Aprueba misiones durante la fase de misiones (paso 4).
 * ============================================================================
 */
public class LiderDeColonia extends Rol {

    private static final Set<Permiso> PERMISOS = Set.of(
            Permiso.VER_VITALIDAD_PROPIA,
            Permiso.VER_VITALIDAD_TODOS,
            Permiso.DECLARAR_EMERGENCIA_MODULO,
            Permiso.APROBAR_MISION,
            Permiso.PROPONER_MISION,
            Permiso.ASIGNAR_TRIPULANTE,
            Permiso.GENERAR_REPORTE_COLONIA
    );

    public LiderDeColonia() { super("Lider de Colonia"); }

    @Override
    public Set<Permiso> permisos() { return PERMISOS; }

    @Override
    public boolean puede(Permiso permiso) { return PERMISOS.contains(permiso); }
}
