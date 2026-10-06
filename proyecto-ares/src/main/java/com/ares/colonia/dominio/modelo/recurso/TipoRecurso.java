package com.ares.colonia.dominio.modelo.recurso;

/**
 * ============================================================================
 * ARCHIVO: TipoRecurso.java
 * PAQUETE: com.ares.colonia.dominio.modelo.recurso
 * ============================================================================
 *
 * PROPOSITO Y FUNCION PRINCIPAL:
 * Enumerado que sirve como clave del inventario de cada colonia.
 * Define los cinco tipos de recursos vitales que el sistema gestiona
 * y que determinan la supervivencia de la colonia en cada turno.
 *
 * ANOTACIONES Y ETIQUETAS UTILIZADAS: Ninguna.
 * HERENCIA E INTERFACES IMPLEMENTADAS: Ninguna.
 * PARTICIPACION EN EL MOTOR POR TURNOS:
 * Usado en los mapas de produccion y consumo (Productor, Consumidor)
 * y como clave del Inventario<Recurso> de cada Colonia.
 * ============================================================================
 */
public enum TipoRecurso {

    /** Oxigeno: esencial para respiracion. Producido por Invernadero. */
    OXIGENO("Oxigeno", true),

    /** Agua: esencial para hidratacion y cultivos. Producida por RobotMinero. */
    AGUA("Agua", true),

    /** Energia: alimenta todos los modulos. Producida por PlantaDeEnergia. */
    ENERGIA("Energia", true),

    /** Comida: perecedera, producida por Invernadero. */
    COMIDA("Comida", false),

    /** Repuestos: finitos, llegan por suministros o mineria. */
    REPUESTOS("Repuestos", false);

    /** Nombre legible del recurso para reportes. */
    private final String nombre;

    /** Indica si el recurso se regenera cada turno (renovable). */
    private final boolean renovable;

    TipoRecurso(String nombre, boolean renovable) {
        this.nombre = nombre;
        this.renovable = renovable;
    }

    /**
     * Retorna el nombre legible del recurso.
     *
     * @return nombre del recurso
     */
    public String getNombre() { return nombre; }

    /**
     * Indica si el recurso se regenera cada turno.
     *
     * @return true si es renovable
     */
    public boolean esRenovable() { return renovable; }
}
