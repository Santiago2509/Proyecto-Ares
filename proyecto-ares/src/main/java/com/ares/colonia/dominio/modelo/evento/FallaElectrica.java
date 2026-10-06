package com.ares.colonia.dominio.modelo.evento;

import com.ares.colonia.dominio.colonia.Colonia;
import com.ares.colonia.dominio.modelo.modulo.Modulo;
import com.ares.colonia.dominio.modelo.recurso.TipoRecurso;

/**
 * ============================================================================
 * ARCHIVO: FallaElectrica.java
 * PAQUETE: com.ares.colonia.dominio.modelo.evento
 * ============================================================================
 *
 * PROPOSITO Y FUNCION PRINCIPAL:
 * Evento local que simula un fallo en el sistema electrico de la colonia.
 * Drena la energia almacenada y reduce la eficiencia de todos los modulos
 * dependientes de electricidad. Si la colonia no tiene suficientes repuestos,
 * la falla puede desencadenar un apagon total que afecta la supervivencia.
 *
 * ANOTACIONES Y ETIQUETAS UTILIZADAS: Ninguna directa.
 * HERENCIA E INTERFACES IMPLEMENTADAS:
 * - Extiende de: Evento
 *
 * PARTICIPACION EN EL MOTOR POR TURNOS:
 * Evento local aplicado en la fase de eventos (paso 1) solo sobre la
 * colonia objetivo. El EventoService revisa si hay repuestos para mitigarlo.
 * ============================================================================
 */
public class FallaElectrica extends Evento {

    /** Porcentaje de energia que se pierde en la falla (0.0 - 1.0). */
    private final double porcentajePerdida;

    /** Indica si la falla fue mitigada con repuestos. */
    private boolean mitigada;

    /**
     * Construye una falla electrica con su severidad.
     *
     * @param severidad severidad de 1 a 5
     */
    public FallaElectrica(int severidad) {
        super(
            "Falla Electrica (Severidad " + severidad + ")",
            "Fallo en el sistema de distribucion electrica. Energia drenada y modulos afectados.",
            severidad
        );
        // A mayor severidad, mayor perdida de energia (20% a 100%)
        this.porcentajePerdida = Math.min(1.0, getSeveridad() * 0.20);
        this.mitigada = false;
    }

    /**
     * Aplica la falla electrica sobre la colonia:
     * 1. Si hay repuestos disponibles, la mitigacion reduce el impacto a la mitad.
     * 2. Drena un porcentaje de la energia almacenada.
     * 3. Aplica desgaste adicional a todos los modulos (sobretension).
     *
     * @param colonia la colonia afectada
     */
    @Override
    public void aplicar(Colonia colonia) {
        double factorImpacto = 1.0;

        // Mitigacion: si hay repuestos, los tecnicos pueden reducir el impacto
        double repuestosDisponibles = colonia.getInventario().getCantidad(TipoRecurso.REPUESTOS);
        if (repuestosDisponibles >= getSeveridad() * 2.0) {
            colonia.getInventario().consumirSeguro(TipoRecurso.REPUESTOS, getSeveridad() * 2.0);
            factorImpacto = 0.5; // el impacto se reduce a la mitad
            this.mitigada = true;
        }

        // Drenar energia almacenada por el cortocircuito
        double energiaActual = colonia.getInventario().getCantidad(TipoRecurso.ENERGIA);
        double perdida = energiaActual * porcentajePerdida * factorImpacto;
        colonia.getInventario().consumirSeguro(TipoRecurso.ENERGIA, perdida);

        // Desgaste por sobretension en todos los modulos electricos
        for (Modulo modulo : colonia.getModulos()) {
            modulo.reparar(-(getSeveridad() * 0.015 * factorImpacto));
        }
    }

    /**
     * La falla electrica es un evento local: solo afecta a una colonia.
     *
     * @return false, es local
     */
    @Override
    public boolean esGlobal() { return false; }

    /** @return true si la falla fue mitigada con repuestos */
    public boolean isMitigada() { return mitigada; }

    /** @return porcentaje de energia perdida en la falla */
    public double getPorcentajePerdida() { return porcentajePerdida; }
}
