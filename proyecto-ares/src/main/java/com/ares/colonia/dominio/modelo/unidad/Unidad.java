package com.ares.colonia.dominio.modelo.unidad;

import com.ares.colonia.dominio.excepciones.UnidadNoDisponibleException;
import com.ares.colonia.dominio.interfaces.Reparable;
import com.ares.colonia.dominio.modelo.recurso.TipoRecurso;

import java.util.Map;
import java.util.UUID;

/**
 * ============================================================================
 * ARCHIVO: Unidad.java
 * PAQUETE: com.ares.colonia.dominio.modelo.unidad
 * ============================================================================
 *
 * PROPOSITO Y FUNCION PRINCIPAL:
 * Clase abstracta base para todas las unidades roboticas de la colonia:
 * Rover, Dron y RobotMinero. Centraliza el estado comun (desgaste,
 * disponibilidad, energia restante) e impone el contrato de ejecutar
 * tareas y calcular consumo, que cada subclase implementa segun su funcion.
 *
 * ANOTACIONES Y ETIQUETAS UTILIZADAS: Ninguna directa.
 * HERENCIA E INTERFACES IMPLEMENTADAS:
 * - Extiende de: Ninguna
 * - Implementa: Reparable (las unidades pueden ser reparadas por Ingenieros)
 *
 * PARTICIPACION EN EL MOTOR POR TURNOS:
 * Las unidades son activadas durante la fase de misiones (paso 4) cuando
 * estan asignadas. En cada turno de mision se invoca ejecutarTarea() y
 * se descuenta calcularConsumo() del inventario de la colonia.
 * ============================================================================
 */
public abstract class Unidad implements Reparable {

    /** Identificador unico de la unidad. */
    private final String id;

    /** Nombre de la unidad (ej. "Rover-01"). */
    private final String nombre;

    /** Nivel de desgaste acumulado (0.0 = nueva, 1.0 = destruida). */
    private double nivelDesgaste;

    /** Indica si la unidad esta disponible para ser asignada a una mision. */
    private boolean disponible;

    /** Energia restante en la bateria de la unidad (0.0 - 100.0). */
    private double energiaRestante;

    /**
     * Construye una unidad con su nombre. Inicia con desgaste 0, disponible
     * y bateria al 100%.
     *
     * @param nombre nombre identificador de la unidad
     */
    protected Unidad(String nombre) {
        if (nombre == null || nombre.isBlank())
            throw new IllegalArgumentException("El nombre de la unidad no puede ser vacio.");
        this.id              = UUID.randomUUID().toString();
        this.nombre          = nombre;
        this.nivelDesgaste   = 0.0;
        this.disponible      = true;
        this.energiaRestante = 100.0;
    }

    // =========================================================================
    // METODOS ABSTRACTOS
    // =========================================================================

    /**
     * Ejecuta la tarea principal de la unidad en un turno de mision.
     * Cada subclase define su comportamiento: explorar, mapear o minar.
     *
     * @return descripcion del resultado de la tarea ejecutada
     * @throws UnidadNoDisponibleException si la unidad no esta disponible
     */
    public abstract String ejecutarTarea();

    /**
     * Calcula los recursos que consume la unidad al ejecutar su tarea en un turno.
     *
     * @return mapa de TipoRecurso -> cantidad consumida
     */
    public abstract Map<TipoRecurso, Double> calcularConsumo();

    // =========================================================================
    // IMPLEMENTACION DE Reparable
    // =========================================================================

    @Override
    public double getNivelDesgaste() { return nivelDesgaste; }

    @Override
    public void reparar(double cantidadReparacion) {
        this.nivelDesgaste = Math.max(0.0, this.nivelDesgaste - cantidadReparacion);
        // Si la reparacion la deja en buen estado, vuelve a estar disponible
        if (this.nivelDesgaste < 1.0) this.disponible = true;
    }

    // =========================================================================
    // GETTERS Y ESTADO
    // =========================================================================

    /** @return id unico de la unidad */
    public String getId() { return id; }

    /** @return nombre de la unidad */
    public String getNombre() { return nombre; }

    /** @return true si la unidad puede ser asignada a una mision */
    public boolean isDisponible() { return disponible; }

    /**
     * Marca la unidad como ocupada (asignada a una mision) o disponible.
     *
     * @param disponible true para liberar, false para asignar
     */
    public void setDisponible(boolean disponible) { this.disponible = disponible; }

    /** @return energia restante en la bateria */
    public double getEnergiaRestante() { return energiaRestante; }

    /**
     * Aplica desgaste por turno de operacion a la unidad.
     *
     * @param incremento incremento de desgaste (0.0 - 1.0)
     */
    protected void aplicarDesgaste(double incremento) {
        this.nivelDesgaste = Math.min(1.0, this.nivelDesgaste + incremento);
        if (this.nivelDesgaste >= 1.0) {
            this.disponible = false; // unidad destruida, no disponible
        }
    }

    /**
     * Consume energia de la bateria de la unidad.
     *
     * @param cantidad cantidad de energia a consumir
     */
    protected void consumirEnergia(double cantidad) {
        this.energiaRestante = Math.max(0.0, this.energiaRestante - cantidad);
    }

    @Override
    public String toString() {
        return String.format("Unidad[%s | %s | Desgaste: %.1f%% | Disponible: %s]",
                nombre, getClass().getSimpleName(), nivelDesgaste * 100, disponible);
    }
}
