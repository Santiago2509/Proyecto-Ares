package com.ares.colonia.dominio.seguridad;

/**
 * ============================================================================
 * ARCHIVO: Permiso.java
 * PAQUETE: com.ares.colonia.dominio.seguridad
 * ============================================================================
 *
 * PROPOSITO Y FUNCION PRINCIPAL:
 * Enumerado de todas las operaciones autorizables del sistema. Cada valor
 * representa una accion que puede estar restringida a ciertos roles.
 * La anotacion @RequierePermiso referencia estos valores para validar
 * el acceso mediante reflexion en tiempo de ejecucion.
 *
 * ANOTACIONES Y ETIQUETAS UTILIZADAS: Ninguna.
 * HERENCIA E INTERFACES IMPLEMENTADAS: Ninguna.
 * PARTICIPACION EN EL MOTOR POR TURNOS:
 * Los permisos son verificados antes de ejecutar operaciones de servicio
 * en cada fase del turno (aprobar misiones, asignar tripulantes, etc.).
 * ============================================================================
 */
public enum Permiso {

    // ── Permisos de vitalidad ──────────────────────────────────────────────
    /** Ver los signos vitales propios. */
    VER_VITALIDAD_PROPIA,
    /** Ver la vitalidad de todos los tripulantes de la colonia. */
    VER_VITALIDAD_TODOS,

    // ── Permisos de modulos ───────────────────────────────────────────────
    /** Agregar o eliminar modulos de la colonia. */
    GESTIONAR_MODULOS,
    /** Declarar un modulo en estado de emergencia. */
    DECLARAR_EMERGENCIA_MODULO,

    // ── Permisos de misiones ──────────────────────────────────────────────
    /** Proponer una nueva mision. */
    PROPONER_MISION,
    /** Aprobar o rechazar misiones propuestas. */
    APROBAR_MISION,

    // ── Permisos de tripulacion ───────────────────────────────────────────
    /** Registrar o dar de baja tripulantes. */
    REGISTRAR_TRIPULANTE,
    /** Asignar tripulantes a modulos o misiones. */
    ASIGNAR_TRIPULANTE,

    // ── Permisos de recursos ──────────────────────────────────────────────
    /** Gestionar el inventario de recursos. */
    GESTIONAR_RECURSOS,
    /** Aprobar transferencias de recursos entre colonias. */
    APROBAR_TRANSFERENCIA,

    // ── Permisos de reporte y red ─────────────────────────────────────────
    /** Generar reportes de la propia colonia. */
    GENERAR_REPORTE_COLONIA,
    /** Ver y comparar todas las colonias de la red. */
    VER_RED_COLONIAL,
    /** Aprobar decisiones que afectan a toda la red. */
    ADMINISTRAR_RED
}
