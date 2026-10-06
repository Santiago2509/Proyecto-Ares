package com.ares.colonia.dominio.modelo.modulo;

import com.ares.colonia.dominio.excepciones.ModuloAveriadoException;
import com.ares.colonia.dominio.interfaces.Productor;
import com.ares.colonia.dominio.modelo.recurso.TipoRecurso;

import java.util.Map;

/**
 * ============================================================================
 * ARCHIVO: Invernadero.java
 * PAQUETE: com.ares.colonia.dominio.modelo.modulo
 * ============================================================================
 *
 * PROPOSITO Y FUNCION PRINCIPAL:
 * Modulo que produce comida y oxigeno aprovechando el cultivo hidropónico
 * marciano. Su eficiencia depende directamente de los botanicos asignados
 * y del agua disponible en el inventario. Es el segundo modulo mas critico
 * para la supervivencia tras la Planta de Energia.
 *
 * ANOTACIONES Y ETIQUETAS UTILIZADAS: Ninguna directa.
 * HERENCIA E INTERFACES IMPLEMENTADAS:
 * - Extiende de: Modulo
 * - Implementa: Productor (produce comida y oxigeno)
 *
 * PARTICIPACION EN EL MOTOR POR TURNOS:
 * Procesado en la fase de modulos. Requiere agua en el inventario para
 * funcionar; sin agua, la produccion cae a cero.
 * ============================================================================
 */
public class Invernadero extends Modulo implements Productor {

    private static final double PRODUCCION_BASE_COMIDA   = 50.0; // kg/turno
    private static final double PRODUCCION_BASE_OXIGENO  = 30.0; // L/turno
    private static final double TASA_DESGASTE = 0.015;

    /** Botanicos asignados al invernadero. */
    private int botanicosAsignados;

    /** Nivel de agua disponible, inyectado por el inventario antes de calcular. */
    private double aguaDisponible;

    public Invernadero(String nombre) {
        super(nombre);
    }

    /**
     * La eficiencia del invernadero depende de: botanicos (bono) + agua (requisito).
     * Sin agua el invernadero no puede producir nada aunque haya botanicos.
     *
     * @return eficiencia entre 0.0 y 1.3
     */
    @Override
    public double calcularEficiencia() {
        if (!estaOperativo()) throw new ModuloAveriadoException(getNombre());

        // Sin agua no hay produccion, independientemente de los botanicos
        if (aguaDisponible <= 0) return 0.0;

        double eficiencia = 0.5; // base sin botanicos

        // Cada botanico agrega 25% de eficiencia, maximo 2 botanicos
        eficiencia += Math.min(botanicosAsignados, 2) * 0.25;

        // Penalizacion por desgaste al superar el 50%
        if (getNivelDesgaste() > UMBRAL_DESGASTE_EFICIENCIA) {
            eficiencia -= (getNivelDesgaste() - UMBRAL_DESGASTE_EFICIENCIA) * 0.5;
        }

        return Math.max(0.0, Math.min(1.3, eficiencia));
    }

    @Override
    public double desgastePorTurno() { return TASA_DESGASTE; }

    @Override
    public Map<TipoRecurso, Double> producirPorTurno() {
        double eff = calcularEficiencia();
        return Map.of(
                TipoRecurso.COMIDA,   PRODUCCION_BASE_COMIDA  * eff * getNivel(),
                TipoRecurso.OXIGENO,  PRODUCCION_BASE_OXIGENO * eff * getNivel()
        );
    }

    @Override
    public Map<TipoRecurso, Double> consumirPorTurno() {
        // Consume agua y energia para el sistema de cultivo hidroponico
        return Map.of(
                TipoRecurso.AGUA,    20.0 * getNivel(),
                TipoRecurso.ENERGIA, 30.0 * getNivel()
        );
    }

    public void setBotanicosAsignados(int cantidad) { this.botanicosAsignados = Math.max(0, cantidad); }
    public void setAguaDisponible(double agua) { this.aguaDisponible = Math.max(0, agua); }
    public int getBotanicosAsignados() { return botanicosAsignados; }
}
