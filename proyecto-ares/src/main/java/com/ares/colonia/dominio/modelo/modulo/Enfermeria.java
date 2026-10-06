package com.ares.colonia.dominio.modelo.modulo;

import com.ares.colonia.dominio.excepciones.ModuloAveriadoException;
import com.ares.colonia.dominio.modelo.recurso.TipoRecurso;
import com.ares.colonia.dominio.personal.Tripulante;

import java.util.Map;

/**
 * ============================================================================
 * ARCHIVO: Enfermeria.java
 * PAQUETE: com.ares.colonia.dominio.modelo.modulo
 * ============================================================================
 *
 * PROPOSITO Y FUNCION PRINCIPAL:
 * Modulo medico que recupera la vitalidad de los tripulantes heridos o enfermos.
 * Su capacidad de recuperacion depende directamente de los medicos asignados.
 * Sin enfermeria, los tripulantes en estado CRITICO no pueden recuperarse.
 *
 * ANOTACIONES Y ETIQUETAS UTILIZADAS: Ninguna.
 * HERENCIA E INTERFACES IMPLEMENTADAS:
 * - Extiende de: Modulo
 * - Implementa: Ninguna adicional
 *
 * PARTICIPACION EN EL MOTOR POR TURNOS:
 * Durante la fase de vitalidad (paso 3), si hay tripulantes en estado CRITICO
 * y la enfermeria esta operativa con medicos, se aplica recuperacion.
 * ============================================================================
 */
public class Enfermeria extends Modulo {

    private static final double TASA_DESGASTE = 0.012;
    /** Puntos de salud recuperados por turno por cada medico. */
    private static final double RECUPERACION_POR_MEDICO = 15.0;

    private int medicosAsignados;

    public Enfermeria(String nombre) { super(nombre); }

    @Override
    public double calcularEficiencia() {
        if (!estaOperativo()) throw new ModuloAveriadoException(getNombre());
        double eficiencia = Math.min(medicosAsignados, 3) * 0.33;
        if (getNivelDesgaste() > UMBRAL_DESGASTE_EFICIENCIA)
            eficiencia -= (getNivelDesgaste() - UMBRAL_DESGASTE_EFICIENCIA) * 0.4;
        return Math.max(0.0, Math.min(1.0, eficiencia));
    }

    @Override
    public double desgastePorTurno() { return TASA_DESGASTE; }

    @Override
    public Map<TipoRecurso, Double> consumirPorTurno() {
        return Map.of(
                TipoRecurso.ENERGIA,   20.0,
                TipoRecurso.REPUESTOS, 0.5 * medicosAsignados
        );
    }

    /**
     * Aplica tratamiento medico a un tripulante, recuperando sus signos vitales.
     * El tratamiento solo es efectivo si la enfermeria esta operativa con medicos.
     *
     * @param tripulante el tripulante a tratar
     * @return puntos de salud recuperados
     */
    public double aplicarTratamiento(Tripulante tripulante) {
        if (!estaOperativo() || medicosAsignados == 0) return 0.0;
        // La recuperacion efectiva depende de la eficiencia del modulo
        return RECUPERACION_POR_MEDICO * medicosAsignados * calcularEficiencia();
    }

    public void setMedicosAsignados(int n) { this.medicosAsignados = Math.max(0, n); }
    public int getMedicosAsignados() { return medicosAsignados; }
}
