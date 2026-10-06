package com.ares.colonia.dominio.personal;

/**
 * ============================================================================
 * ARCHIVO: EstadoVital.java
 * PAQUETE: com.ares.colonia.dominio.personal
 * ============================================================================
 *
 * PROPOSITO Y FUNCION PRINCIPAL:
 * Enumerado que representa el estado de salud global de un tripulante,
 * calculado a partir de sus SignosVitales al final de cada turno.
 * Este valor determina si el tripulante puede operar, requiere atencion
 * medica o ha fallecido.
 *
 * ANOTACIONES Y ETIQUETAS UTILIZADAS: Ninguna.
 * HERENCIA E INTERFACES IMPLEMENTADAS: Ninguna.
 * PARTICIPACION EN EL MOTOR POR TURNOS:
 * Calculado en la fase de vitalidad (paso 3). Cuando el estado cambia
 * a CRITICO o FALLECIDO, se lanza la excepcion correspondiente y se
 * notifica a los observadores.
 * ============================================================================
 */
public enum EstadoVital {

    /**
     * Tripulante en plena forma. Todos los signos vitales dentro del rango saludable.
     * Produce bono de eficiencia en su modulo asignado.
     */
    OPTIMO,

    /**
     * Tripulante funcional pero con alguno de sus indicadores por debajo del ideal.
     * Opera con normalidad pero sin bono de eficiencia.
     */
    ESTABLE,

    /**
     * Tripulante con uno o mas signos vitales en zona de peligro (< 25%).
     * Solo puede realizar tareas basicas. Genera alerta y requiere atencion medica.
     */
    CRITICO,

    /**
     * El tripulante ha fallecido. No puede realizar ninguna tarea.
     * Se retira de la colonia y se reduce la moral de los supervivientes.
     */
    FALLECIDO;

    /**
     * Evalua si el estado es operativo (el tripulante puede realizar tareas).
     *
     * @return true si el tripulante puede operar (OPTIMO o ESTABLE)
     */
    public boolean esOperativo() {
        return this == OPTIMO || this == ESTABLE;
    }

    /**
     * Evalua si el estado requiere atencion medica inmediata.
     *
     * @return true si el tripulante esta en estado CRITICO
     */
    public boolean requiereAtencionMedica() {
        return this == CRITICO;
    }
}
