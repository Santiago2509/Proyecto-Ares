package com.ares.colonia.dominio.interfaces;

/**
 * ============================================================================
 * ARCHIVO: Reparable.java
 * PAQUETE: com.ares.colonia.dominio.interfaces
 * ============================================================================
 *
 * PROPOSITO Y FUNCION PRINCIPAL:
 * Contrato que deben cumplir todos los elementos del sistema que pueden
 * ser reparados por un Ingeniero: modulos y unidades roboticas.
 * Garantiza que el motor de simulacion pueda reparar cualquier elemento
 * sin conocer su tipo concreto (polimorfismo de interfaz).
 *
 * ANOTACIONES Y ETIQUETAS UTILIZADAS: Ninguna.
 * HERENCIA E INTERFACES IMPLEMENTADAS: Ninguna (es raiz de contrato).
 * PARTICIPACION EN EL MOTOR POR TURNOS:
 * Invocada en la fase de reparacion cuando un Ingeniero esta disponible
 * o cuando se ejecuta una MisionReparacion.
 * ============================================================================
 */
public interface Reparable {

    /**
     * Retorna el nivel actual de desgaste del elemento (0.0 = nuevo, 1.0 = destruido).
     *
     * @return nivel de desgaste entre 0.0 y 1.0
     */
    double getNivelDesgaste();

    /**
     * Aplica una reparacion al elemento, reduciendo su nivel de desgaste.
     * La cantidad reparada depende de la habilidad del Ingeniero y los
     * repuestos disponibles en el inventario.
     *
     * @param cantidadReparacion puntos de reparacion a aplicar (0.0 - 1.0)
     */
    void reparar(double cantidadReparacion);

    /**
     * Indica si el elemento necesita reparacion urgente.
     * Por convencion, se considera urgente cuando el desgaste supera el 75%.
     *
     * @return true si requiere reparacion inmediata
     */
    default boolean necesitaReparacion() {
        // Umbral de reparacion urgente definido en la regla de negocio
        return getNivelDesgaste() >= 0.75;
    }
}
