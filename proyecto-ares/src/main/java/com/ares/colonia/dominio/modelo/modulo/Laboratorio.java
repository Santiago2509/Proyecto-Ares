package com.ares.colonia.dominio.modelo.modulo;

import com.ares.colonia.dominio.excepciones.ModuloAveriadoException;
import com.ares.colonia.dominio.interfaces.Productor;
import com.ares.colonia.dominio.modelo.recurso.TipoRecurso;

import java.util.Map;

/**
 * ============================================================================
 * ARCHIVO: Laboratorio.java
 * PAQUETE: com.ares.colonia.dominio.modelo.modulo
 * ============================================================================
 *
 * PROPOSITO Y FUNCION PRINCIPAL:
 * Modulo de investigacion cientifica que genera puntos de investigacion (PI)
 * que desbloquean tecnologias. Requiere alta energia y cientificos asignados.
 * No produce recursos vitales pero es esencial para la mision de Terraformacion.
 *
 * ANOTACIONES Y ETIQUETAS UTILIZADAS: Ninguna directa.
 * HERENCIA E INTERFACES IMPLEMENTADAS:
 * - Extiende de: Modulo
 * - Implementa: Productor (produce puntos de investigacion)
 *
 * PARTICIPACION EN EL MOTOR POR TURNOS:
 * Genera PI en cada turno. Los PI se acumulan en la colonia y son
 * necesarios para iniciar la mision Investigacion y Terraformacion.
 * ============================================================================
 */
public class Laboratorio extends Modulo implements Productor {

    private static final double PRODUCCION_BASE_PI = 10.0; // puntos de investigacion/turno
    private static final double TASA_DESGASTE = 0.010;

    /** Cientificos asignados al laboratorio. */
    private int cientificosAsignados;

    public Laboratorio(String nombre) { super(nombre); }

    @Override
    public double calcularEficiencia() {
        if (!estaOperativo()) throw new ModuloAveriadoException(getNombre());

        // Sin cientificos el laboratorio opera al minimo automatico
        double eficiencia = 0.2 + (Math.min(cientificosAsignados, 4) * 0.2);

        if (getNivelDesgaste() > UMBRAL_DESGASTE_EFICIENCIA)
            eficiencia -= (getNivelDesgaste() - UMBRAL_DESGASTE_EFICIENCIA) * 0.5;

        return Math.max(0.0, Math.min(1.0, eficiencia));
    }

    @Override
    public double desgastePorTurno() { return TASA_DESGASTE; }

    @Override
    public Map<TipoRecurso, Double> producirPorTurno() {
        // "INVESTIGACION" se podria modelar como un TipoRecurso especial
        // Aqui retornamos vacio porque los PI se gestionan directamente en la Colonia
        return Map.of();
    }

    /** Retorna los puntos de investigacion generados en este turno. */
    public double generarPuntosInvestigacion() {
        return PRODUCCION_BASE_PI * calcularEficiencia() * getNivel();
    }

    @Override
    public Map<TipoRecurso, Double> consumirPorTurno() {
        return Map.of(TipoRecurso.ENERGIA, 80.0 * getNivel()); // alto consumo de energia
    }

    public void setCientificosAsignados(int n) { this.cientificosAsignados = Math.max(0, n); }
    public int getCientificosAsignados() { return cientificosAsignados; }
}
