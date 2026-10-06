package com.ares.colonia.dominio.interfaces;

/**
 * ============================================================================
 * ARCHIVO: Observable.java
 * PAQUETE: com.ares.colonia.dominio.interfaces
 * ============================================================================
 *
 * PROPOSITO Y FUNCION PRINCIPAL:
 * Parte del patron Observer. Define el contrato para los objetos que pueden
 * ser observados (Colonia, RedColonial, Tripulante). Permite registrar,
 * eliminar y notificar a los observadores (alertas, logs, reportes).
 *
 * ANOTACIONES Y ETIQUETAS UTILIZADAS: Ninguna.
 * HERENCIA E INTERFACES IMPLEMENTADAS: Ninguna.
 * PARTICIPACION EN EL MOTOR POR TURNOS:
 * Al final de cada turno (paso 5), notifica estados criticos a todos
 * los observadores registrados.
 * ============================================================================
 */
public interface Observable {

    /**
     * Registra un nuevo observador que recibird notificaciones de cambio.
     *
     * @param observador el observador a agregar
     */
    void agregarObservador(Observador observador);

    /**
     * Elimina un observador previamente registrado.
     *
     * @param observador el observador a eliminar
     */
    void eliminarObservador(Observador observador);

    /**
     * Notifica a todos los observadores registrados con un mensaje de alerta.
     *
     * @param alerta descripcion del evento o estado critico detectado
     */
    void notificarObservadores(String alerta);
}
