package com.ares.colonia.dominio.modelo.evento;

import com.ares.colonia.dominio.colonia.Colonia;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * ============================================================================
 * ARCHIVO: Evento.java
 * PAQUETE: com.ares.colonia.dominio.modelo.evento
 * ============================================================================
 *
 * PROPOSITO Y FUNCION PRINCIPAL:
 * Clase abstracta que representa cualquier evento que puede ocurrir en
 * el sistema: TormentaDeArena, FallaElectrica, Meteorito, BroteDeEnfermedad,
 * LlegadaDeSuministros y SenalMisteriosa. Los eventos son la principal
 * fuente de cambio inesperado en la simulacion y ejercitan el polimorfismo
 * al ser procesados de forma uniforme por el motor de turnos.
 *
 * ANOTACIONES Y ETIQUETAS UTILIZADAS: Ninguna directa.
 * HERENCIA E INTERFACES IMPLEMENTADAS:
 * - Extiende de: Ninguna
 * - Implementa: Ninguna
 *
 * PARTICIPACION EN EL MOTOR POR TURNOS:
 * En la fase de eventos (paso 1 del turno), EventoService genera los eventos
 * del sol y llama a aplicar(Colonia) de forma polimorfica sobre cada uno.
 * ============================================================================
 */
public abstract class Evento {

    /** Identificador unico del evento. */
    private final String id;

    /** Nombre descriptivo del evento (ej. "Tormenta de Arena Nivel 3"). */
    private final String nombre;

    /** Descripcion detallada del evento y sus efectos. */
    private final String descripcion;

    /** Timestamp de cuando ocurrio el evento. */
    private final LocalDateTime fechaOcurrencia;

    /** Severidad del evento en escala de 1 a 5 (1 = leve, 5 = catastrofico). */
    private final int severidad;

    /**
     * Construye un evento con sus datos basicos.
     *
     * @param nombre      nombre del evento
     * @param descripcion descripcion del efecto
     * @param severidad   severidad del 1 al 5
     */
    protected Evento(String nombre, String descripcion, int severidad) {
        this.id              = UUID.randomUUID().toString();
        this.nombre          = nombre;
        this.descripcion     = descripcion;
        this.fechaOcurrencia = LocalDateTime.now();
        this.severidad       = Math.max(1, Math.min(5, severidad));
    }

    // =========================================================================
    // METODOS ABSTRACTOS
    // =========================================================================

    /**
     * Aplica el efecto de este evento sobre la colonia objetivo.
     * El comportamiento es completamente polimorfico: una TormentaDeArena
     * dania modulos y unidades; un BroteDeEnfermedad reduce la vitalidad.
     *
     * @param colonia la colonia sobre la que se aplica el evento
     */
    public abstract void aplicar(Colonia colonia);

    /**
     * Indica si este evento es global (afecta a toda la RedColonial)
     * o local (afecta solo a la colonia objetivo).
     *
     * @return true si el evento afecta a toda la red colonial
     */
    public abstract boolean esGlobal();

    // =========================================================================
    // GETTERS
    // =========================================================================

    /** @return id unico del evento */
    public String getId() { return id; }

    /** @return nombre del evento */
    public String getNombre() { return nombre; }

    /** @return descripcion del efecto del evento */
    public String getDescripcion() { return descripcion; }

    /** @return momento en que ocurrio el evento */
    public LocalDateTime getFechaOcurrencia() { return fechaOcurrencia; }

    /** @return severidad del evento (1-5) */
    public int getSeveridad() { return severidad; }

    @Override
    public String toString() {
        return String.format("[%s] %s (Severidad: %d, Global: %s)",
                getClass().getSimpleName(), nombre, severidad, esGlobal());
    }
}
