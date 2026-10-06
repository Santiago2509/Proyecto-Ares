package com.ares.colonia.dominio.modelo.recurso;

/**
 * ============================================================================
 * ARCHIVO: Recurso.java
 * PAQUETE: com.ares.colonia.dominio.modelo.recurso
 * ============================================================================
 *
 * PROPOSITO Y FUNCION PRINCIPAL:
 * Clase abstracta que representa un recurso vital de la colonia.
 * Centraliza la cantidad actual, la capacidad maxima y los umbrales
 * de alerta, y obliga a cada familia de recurso (renovable, perecedero,
 * finito) a implementar su comportamiento de actualizacion por turno.
 *
 * ANOTACIONES Y ETIQUETAS UTILIZADAS: Ninguna.
 * HERENCIA E INTERFACES IMPLEMENTADAS:
 * - Extiende de: Ninguna
 * - Implementa: Ninguna
 *
 * PARTICIPACION EN EL MOTOR POR TURNOS:
 * El Inventario invoca actualizarPorTurno() en cada Recurso al inicio
 * de la fase de produccion/consumo (paso 2). El metodo aplica degradacion,
 * regeneracion u otros efectos segun la familia del recurso.
 * ============================================================================
 */
public abstract class Recurso {

    /** Tipo de recurso, usado como clave del inventario. */
    private final TipoRecurso tipo;

    /** Cantidad actual disponible en el inventario (en unidades del recurso). */
    private double cantidad;

    /** Capacidad maxima de almacenamiento de este recurso. */
    private final double capacidadMaxima;

    /**
     * Umbral critico: si la cantidad cae por debajo de este valor,
     * se genera una alerta y se aplican restricciones de consumo.
     */
    private final double umbralCritico;

    /**
     * Construye un recurso con su tipo, stock inicial y limite de almacenamiento.
     *
     * @param tipo           tipo de recurso (TipoRecurso enum)
     * @param cantidadInicial cantidad de inicio (>= 0)
     * @param capacidadMaxima capacidad maxima de almacenamiento (> 0)
     * @param umbralCritico   umbral de alerta (< capacidadMaxima)
     */
    protected Recurso(TipoRecurso tipo, double cantidadInicial,
                      double capacidadMaxima, double umbralCritico) {
        if (capacidadMaxima <= 0)
            throw new IllegalArgumentException("La capacidad maxima debe ser positiva.");
        if (cantidadInicial < 0)
            throw new IllegalArgumentException("La cantidad inicial no puede ser negativa.");

        this.tipo           = tipo;
        this.cantidad       = Math.min(cantidadInicial, capacidadMaxima);
        this.capacidadMaxima = capacidadMaxima;
        this.umbralCritico  = umbralCritico;
    }

    // =========================================================================
    // METODO ABSTRACTO — cada familia de recurso define su dinamica de turno
    // =========================================================================

    /**
     * Aplica la logica de actualizacion del recurso al pasar un turno (sol).
     * - RecursoRenovable: no hace nada extra (se regenera mediante modulos).
     * - RecursoPerecedero: reduce la cantidad por degradacion natural.
     * - RecursoFinito: verifica si hubo reposicion; si no, decrece lentamente.
     */
    public abstract void actualizarPorTurno();

    // =========================================================================
    // OPERACIONES DE INVENTARIO
    // =========================================================================

    /**
     * Agrega una cantidad al stock del recurso, sin superar la capacidad maxima.
     *
     * @param cantidad cantidad a agregar (debe ser positiva)
     * @throws IllegalArgumentException   si la cantidad es negativa
     */
    public void agregar(double cantidad) {
        if (cantidad < 0)
            throw new IllegalArgumentException("La cantidad a agregar no puede ser negativa.");
        // Limitar al maximo: el excedente se pierde
        this.cantidad = Math.min(this.capacidadMaxima, this.cantidad + cantidad);
    }

    /**
     * Consume una cantidad del stock del recurso.
     *
     * @param cantidad cantidad a consumir (debe ser positiva y <= stock actual)
     * @throws IllegalArgumentException si la cantidad es negativa o supera el stock
     */
    public void consumir(double cantidad) {
        if (cantidad < 0)
            throw new IllegalArgumentException("La cantidad a consumir no puede ser negativa.");
        if (cantidad > this.cantidad)
            throw new IllegalArgumentException(
                "Stock insuficiente de " + tipo.getNombre() +
                ": se intenta consumir " + cantidad + " pero hay " + this.cantidad);
        this.cantidad -= cantidad;
    }

    // =========================================================================
    // GETTERS Y ESTADO
    // =========================================================================

    /** @return tipo de este recurso */
    public TipoRecurso getTipo() { return tipo; }

    /** @return cantidad actual disponible */
    public double getCantidad() { return cantidad; }

    /** @return capacidad maxima de almacenamiento */
    public double getCapacidadMaxima() { return capacidadMaxima; }

    /** @return umbral critico de alerta */
    public double getUmbralCritico() { return umbralCritico; }

    /** @return true si la cantidad actual esta por debajo del umbral critico */
    public boolean estaCritico() { return cantidad < umbralCritico; }

    /** @return porcentaje de stock actual respecto a la capacidad maxima */
    public double getPorcentajeStock() { return (cantidad / capacidadMaxima) * 100; }

    @Override
    public String toString() {
        return String.format("%s: %.1f / %.1f (%.0f%%)",
                tipo.getNombre(), cantidad, capacidadMaxima, getPorcentajeStock());
    }
}
