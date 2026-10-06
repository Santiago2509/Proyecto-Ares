package com.ares.colonia.dominio.modelo.modulo;

import com.ares.colonia.dominio.excepciones.ModuloAveriadoException;
import com.ares.colonia.dominio.modelo.recurso.TipoRecurso;
import com.ares.colonia.dominio.modelo.unidad.Unidad;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * ============================================================================
 * ARCHIVO: Hangar.java
 * PAQUETE: com.ares.colonia.dominio.modelo.modulo
 * ============================================================================
 *
 * PROPOSITO Y FUNCION PRINCIPAL:
 * Modulo que aloja y da mantenimiento a las unidades roboticas de la colonia
 * (Rovers, Drones, RobotsMineros). Sin hangar operativo no se pueden lanzar
 * misiones de exploracion ni recibir suministros de la Tierra.
 *
 * ANOTACIONES Y ETIQUETAS UTILIZADAS: Ninguna.
 * HERENCIA E INTERFACES IMPLEMENTADAS:
 * - Extiende de: Modulo
 * PARTICIPACION EN EL MOTOR POR TURNOS:
 * El hangar reduce el desgaste de las unidades alojadas cada turno.
 * ============================================================================
 */
public class Hangar extends Modulo {

    private static final double TASA_DESGASTE = 0.018;
    private final List<Unidad> unidadesAlojadas = new ArrayList<>();
    private int pilotos;
    private int ingenieros;

    public Hangar(String nombre) { super(nombre); }

    @Override
    public double calcularEficiencia() {
        if (!estaOperativo()) throw new ModuloAveriadoException(getNombre());
        double eficiencia = 0.5 + (Math.min(pilotos, 2) * 0.15) + (Math.min(ingenieros, 2) * 0.10);
        if (getNivelDesgaste() > UMBRAL_DESGASTE_EFICIENCIA)
            eficiencia -= (getNivelDesgaste() - UMBRAL_DESGASTE_EFICIENCIA);
        return Math.max(0.0, Math.min(1.0, eficiencia));
    }

    @Override
    public double desgastePorTurno() { return TASA_DESGASTE; }

    @Override
    public Map<TipoRecurso, Double> consumirPorTurno() {
        return Map.of(
                TipoRecurso.ENERGIA,   40.0,
                TipoRecurso.REPUESTOS, 1.0 * unidadesAlojadas.size()
        );
    }

    /** Registra una nueva unidad en el hangar. */
    public void alojarUnidad(Unidad unidad) { unidadesAlojadas.add(unidad); }

    /** @return lista inmutable de unidades alojadas en el hangar */
    public List<Unidad> getUnidadesAlojadas() { return Collections.unmodifiableList(unidadesAlojadas); }

    public void setPilotos(int n) { this.pilotos = Math.max(0, n); }
    public void setIngenieros(int n) { this.ingenieros = Math.max(0, n); }
}
