package com.ares.colonia.dominio.modelo.evento;

import com.ares.colonia.dominio.colonia.Colonia;
import com.ares.colonia.dominio.modelo.modulo.Modulo;
import com.ares.colonia.dominio.modelo.recurso.TipoRecurso;

/**
 * ============================================================================
 * ARCHIVO: TormentaDeArena.java
 * PAQUETE: com.ares.colonia.dominio.modelo.evento
 * ============================================================================
 *
 * PROPOSITO Y FUNCION PRINCIPAL:
 * Evento global que simula una tormenta de arena marciana. Reduce la
 * produccion de energia solar, desgasta modulos expuestos (Invernadero,
 * Hangar) y puede danar unidades exteriores. Es el evento mas frecuente
 * en Marte y el que mas impacta la produccion de energia.
 *
 * ANOTACIONES Y ETIQUETAS UTILIZADAS: Ninguna directa.
 * HERENCIA E INTERFACES IMPLEMENTADAS:
 * - Extiende de: Evento
 *
 * PARTICIPACION EN EL MOTOR POR TURNOS:
 * Se aplica en la fase de eventos (paso 1). Al ser global, EventoService
 * lo aplica sobre todas las colonias de la red mediante aplicar(Colonia).
 * ============================================================================
 */
public class TormentaDeArena extends Evento {

    /**
     * Factor de reduccion de energia solar (0.0 sin reduccion, 1.0 sin energia).
     * A mayor severidad, mayor reduccion.
     */
    private final double factorReduccionEnergia;

    /**
     * Construye una tormenta de arena con su severidad.
     * La severidad determina el factor de reduccion de energia solar.
     *
     * @param severidad severidad de 1 a 5
     */
    public TormentaDeArena(int severidad) {
        super(
            "Tormenta de Arena Nivel " + severidad,
            "Tormenta de arena que reduce la energia solar y desgasta los modulos expuestos.",
            severidad
        );
        // A mayor severidad, mayor reduccion de energia solar (de 10% a 50%)
        this.factorReduccionEnergia = getSeveridad() * 0.10;
    }

    /**
     * Aplica los efectos de la tormenta sobre la colonia:
     * 1. Reduce la energia almacenada por el factor de reduccion solar.
     * 2. Aplica desgaste extra a los modulos del inventario.
     *
     * @param colonia la colonia objetivo
     */
    @Override
    public void aplicar(Colonia colonia) {
        // 1. Reducir la energia disponible (paneles solares bloqueados por polvo)
        double energiaActual = colonia.getInventario().getCantidad(TipoRecurso.ENERGIA);
        double perdidaEnergia = energiaActual * factorReduccionEnergia;
        colonia.getInventario().consumirSeguro(TipoRecurso.ENERGIA, perdidaEnergia);

        // 2. Desgastar los modulos expuestos al exterior
        for (Modulo modulo : colonia.getModulos()) {
            // La tormenta dania mas los modulos si la severidad es alta
            modulo.reparar(-(getSeveridad() * 0.02)); // simulamos desgaste como "reparacion negativa"
        }
    }

    /**
     * La tormenta de arena marciana es un evento global: afecta a toda la red.
     *
     * @return true, siempre es global
     */
    @Override
    public boolean esGlobal() { return true; }

    /** @return factor de reduccion de energia solar (0.0 - 1.0) */
    public double getFactorReduccionEnergia() { return factorReduccionEnergia; }
}
