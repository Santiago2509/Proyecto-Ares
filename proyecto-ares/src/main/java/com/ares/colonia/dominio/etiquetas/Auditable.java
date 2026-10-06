package com.ares.colonia.dominio.etiquetas;

import java.lang.annotation.*;

/**
 * ============================================================================
 * ARCHIVO: Auditable.java
 * PAQUETE: com.ares.colonia.dominio.etiquetas
 * ============================================================================
 *
 * PROPOSITO Y FUNCION PRINCIPAL:
 * Anotacion de marcado para metodos cuya ejecucion debe quedar registrada
 * en el historial de auditoría de la colonia. Usada principalmente en
 * operaciones de servicio que modifican estado critico (aprobar misiones,
 * transferir recursos, registrar fallecidos).
 *
 * ANOTACIONES Y ETIQUETAS UTILIZADAS:
 * - @Target: METODO.
 * - @Retention: RUNTIME (evaluada por AuditAspect o reflexion manual).
 * HERENCIA E INTERFACES IMPLEMENTADAS: Ninguna.
 * PARTICIPACION EN EL MOTOR POR TURNOS:
 * El registro de auditoría se consolida al final de cada turno en el reporte.
 * ============================================================================
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Auditable {
    /** Descripcion de la operacion auditada. */
    String operacion() default "Operacion de dominio";
}
