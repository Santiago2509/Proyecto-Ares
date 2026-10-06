package com.ares.colonia.dominio.modelo.modulo;

import com.ares.colonia.dominio.etiquetas.Critico;
import com.ares.colonia.dominio.excepciones.ModuloAveriadoException;
import com.ares.colonia.dominio.modelo.recurso.TipoRecurso;

import java.util.Map;

/**
 * ============================================================================
 * ARCHIVO: Habitacional.java
 * PAQUETE: com.ares.colonia.dominio.modelo.modulo
 * ============================================================================
 *
 * PROPOSITO Y FUNCION PRINCIPAL:
 * Modulo que aloja a la tripulacion y sostiene su moral. Su capacidad
 * determina cuantos tripulantes puede hospedar comodamente; la sobrepoblacion
 * reduce la moral de todos los ocupantes.
 *
 * ANOTACIONES Y ETIQUETAS UTILIZADAS:
 * - @Critico: sin habitat la tripulacion no puede sobrevivir en Marte.
 *
 * HERENCIA E INTERFACES IMPLEMENTADAS:
 * - Extiende de: Modulo
 * - Implementa: (solo Consumidor via Modulo)
 *
 * PARTICIPACION EN EL MOTOR POR TURNOS:
 * Procesa el efecto en la moral de los tripulantes segun la ocupacion.
 * ============================================================================
 */
@Critico(motivo = "Aloja a la tripulacion; su falla causa baja moral inmediata")
public class Habitacional extends Modulo {

    private static final double TASA_DESGASTE = 0.008;

    /** Capacidad maxima de tripulantes que puede alojar comodamente. */
    private final int capacidadMaxima;

    /** Numero de tripulantes actualmente alojados. */
    private int tripulantesAlojados;

    public Habitacional(String nombre, int capacidadMaxima) {
        super(nombre);
        this.capacidadMaxima = capacidadMaxima;
        this.tripulantesAlojados = 0;
    }

    /**
     * La eficiencia depende del nivel de ocupacion.
     * Optima entre 60-80% de ocupacion. Cae con la sobrepoblacion.
     *
     * @return eficiencia de habitabilidad
     */
    @Override
    public double calcularEficiencia() {
        if (!estaOperativo()) throw new ModuloAveriadoException(getNombre());

        if (capacidadMaxima == 0) return 0.0;
        double ocupacion = (double) tripulantesAlojados / capacidadMaxima;

        // Penalizacion severa por sobrepoblacion (> 100% capacidad)
        if (ocupacion > 1.0) return Math.max(0.1, 1.0 - (ocupacion - 1.0) * 2);

        // Eficiencia optima entre 60-80% de ocupacion
        return ocupacion <= 0.8 ? 1.0 : 1.0 - (ocupacion - 0.8) * 2;
    }

    @Override
    public double desgastePorTurno() { return TASA_DESGASTE; }

    @Override
    public Map<TipoRecurso, Double> consumirPorTurno() {
        // Consume energia, oxigeno y agua por cada tripulante alojado
        return Map.of(
                TipoRecurso.ENERGIA, 5.0  * tripulantesAlojados,
                TipoRecurso.OXIGENO, 1.5  * tripulantesAlojados,
                TipoRecurso.AGUA,    1.0  * tripulantesAlojados
        );
    }

    public void setTripulantesAlojados(int n) { this.tripulantesAlojados = Math.max(0, n); }
    public int getCapacidadMaxima() { return capacidadMaxima; }
    public int getTripulantesAlojados() { return tripulantesAlojados; }
    public boolean estaSobrepoblado() { return tripulantesAlojados > capacidadMaxima; }
}
