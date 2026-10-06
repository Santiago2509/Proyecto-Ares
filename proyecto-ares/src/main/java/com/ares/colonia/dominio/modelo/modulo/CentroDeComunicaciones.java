package com.ares.colonia.dominio.modelo.modulo;

import com.ares.colonia.dominio.excepciones.ModuloAveriadoException;
import com.ares.colonia.dominio.modelo.recurso.TipoRecurso;

import java.util.Map;

/**
 * ============================================================================
 * ARCHIVO: CentroDeComunicaciones.java
 * PAQUETE: com.ares.colonia.dominio.modelo.modulo
 * ============================================================================
 *
 * PROPOSITO Y FUNCION PRINCIPAL:
 * Modulo que mantiene el contacto con la Tierra y con otras colonias de la red.
 * Si este modulo falla, la colonia entra en aislamiento: no puede recibir
 * suministros, no puede pedir rescate y la moral de la tripulacion cae
 * drasticamente cada turno.
 *
 * ANOTACIONES Y ETIQUETAS UTILIZADAS: Ninguna.
 * HERENCIA E INTERFACES IMPLEMENTADAS:
 * - Extiende de: Modulo
 * PARTICIPACION EN EL MOTOR POR TURNOS:
 * Al inicio de cada turno se verifica si esta operativo; si no, aplica
 * penalizacion de moral a todos los tripulantes.
 * ============================================================================
 */
public class CentroDeComunicaciones extends Modulo {

    private static final double TASA_DESGASTE = 0.010;
    /** Penalizacion de moral por turno en aislamiento. */
    public static final double PENALIZACION_MORAL_AISLAMIENTO = 5.0;

    public CentroDeComunicaciones(String nombre) { super(nombre); }

    @Override
    public double calcularEficiencia() {
        if (!estaOperativo()) throw new ModuloAveriadoException(getNombre());
        // La eficiencia cae linealmente con el desgaste
        double eficiencia = 1.0 - getNivelDesgaste();
        return Math.max(0.0, eficiencia);
    }

    /** @return true si la colonia esta en comunicacion con la Tierra */
    public boolean estaEnComunicacion() { return estaOperativo() && calcularEficiencia() > 0.1; }

    @Override
    public double desgastePorTurno() { return TASA_DESGASTE; }

    @Override
    public Map<TipoRecurso, Double> consumirPorTurno() {
        return Map.of(TipoRecurso.ENERGIA, 10.0); // bajo consumo de energia
    }
}
