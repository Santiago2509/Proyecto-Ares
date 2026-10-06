package com.ares.colonia.dominio.etiquetas;

/**
 * ============================================================================
 * ARCHIVO: Etiqueta.java
 * PAQUETE: com.ares.colonia.dominio.etiquetas
 * ============================================================================
 *
 * PROPOSITO Y FUNCION PRINCIPAL:
 * Enumerado de marcas semanticas que pueden aplicarse a modulos, tripulantes
 * y unidades para filtrar entidades, priorizar operaciones y activar
 * comportamientos especiales en el motor de simulacion.
 *
 * ANOTACIONES Y ETIQUETAS UTILIZADAS: Ninguna (es la definicion de etiquetas).
 * HERENCIA E INTERFACES IMPLEMENTADAS: Ninguna.
 * PARTICIPACION EN EL MOTOR POR TURNOS:
 * El motor consulta las etiquetas de cada entidad para aplicar reglas
 * diferenciadas (ej. los modulos ESENCIAL tienen prioridad en el consumo
 * de energia durante escasez).
 * ============================================================================
 */
public enum Etiqueta {

    /** Elemento critico para la supervivencia; tiene prioridad en recursos. */
    ESENCIAL,

    /** Representa un riesgo alto para la tripulacion o la colonia. */
    PELIGROSO,

    /** Puede ser reparado por un Ingeniero. */
    REPARABLE,

    /** Modulo o tecnologia en fase de pruebas; puede fallar inesperadamente. */
    EXPERIMENTAL,

    /** Operacion o elemento que debe quedar registrado en el historial de auditoría. */
    AUDITABLE,

    /** Elemento en estado de emergencia; requiere atencion inmediata. */
    EMERGENCIA
}
