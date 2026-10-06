package com.ares.colonia.dominio.interfaces;

/**
 * ============================================================================
 * ARCHIVO: Observador.java
 * PAQUETE: com.ares.colonia.dominio.interfaces
 * ============================================================================
 *
 * PROPOSITO Y FUNCION PRINCIPAL:
 * Parte del patron Observer. Contrato para los objetos que reciben
 * notificaciones de cambio de estado (alertas criticas, logs, reportes).
 *
 * ANOTACIONES Y ETIQUETAS UTILIZADAS: Ninguna.
 * HERENCIA E INTERFACES IMPLEMENTADAS: Ninguna.
 * PARTICIPACION EN EL MOTOR POR TURNOS:
 * Recibe notificaciones al final del turno (paso 5 - Alertas).
 * ============================================================================
 */
public interface Observador {

    /**
     * Recibe y procesa una notificacion de alerta del observable.
     *
     * @param fuente  nombre del objeto que genero la alerta
     * @param alerta  descripcion del evento critico
     */
    void actualizar(String fuente, String alerta);
}
