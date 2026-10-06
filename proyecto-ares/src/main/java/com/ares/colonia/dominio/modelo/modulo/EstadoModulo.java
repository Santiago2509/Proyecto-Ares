package com.ares.colonia.dominio.modelo.modulo;

/**
 * ============================================================================
 * ARCHIVO: EstadoModulo.java
 * PAQUETE: com.ares.colonia.dominio.modelo.modulo
 * ============================================================================
 *
 * PROPOSITO Y FUNCION PRINCIPAL:
 * Enumerado que representa el estado operativo actual de un modulo,
 * implementando el patron State de forma simplificada. El motor de
 * simulacion consulta este estado para decidir si el modulo puede
 * producir, consumir y ser asignado a misiones.
 *
 * ANOTACIONES Y ETIQUETAS UTILIZADAS: Ninguna.
 * HERENCIA E INTERFACES IMPLEMENTADAS: Ninguna.
 * PARTICIPACION EN EL MOTOR POR TURNOS:
 * Evaluado en la fase de modulos (paso 2) para cada modulo de la colonia.
 * ============================================================================
 */
public enum EstadoModulo {

    /** Funcionando correctamente. Produce y consume al 100% de eficiencia. */
    OPERATIVO,

    /** Desgaste entre 75% y 99%. Opera a eficiencia reducida. Requiere mantenimiento. */
    EN_MANTENIMIENTO,

    /** Desgaste al 100%. No puede operar. Requiere reparacion urgente con repuestos. */
    AVERIADO,

    /** Declarado en emergencia por el Lider de Colonia. Prioridad maxima de recursos. */
    EN_EMERGENCIA
}
