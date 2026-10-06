package com.ares.colonia.dominio.modelo.unidad;

import com.ares.colonia.dominio.modelo.recurso.TipoRecurso;

import java.util.Map;

/**
 * ============================================================================
 * ARCHIVO: Dron.java
 * PAQUETE: com.ares.colonia.dominio.modelo.unidad
 * ============================================================================
 *
 * PROPOSITO Y FUNCION PRINCIPAL:
 * Unidad aerea ligera de reconocimiento y mapeo. El Dron es rapido y puede
 * detectar eventos ambientales (tormentas, impactos de meteoritos) antes de
 * que ocurran, ademas de mapear terreno a gran velocidad. Su debilidad es
 * su corto alcance y su fragilidad ante las tormentas de arena.
 *
 * ANOTACIONES Y ETIQUETAS UTILIZADAS: Ninguna directa.
 * HERENCIA E INTERFACES IMPLEMENTADAS:
 * - Extiende de: Unidad
 * - Implementa: Reparable (heredado de Unidad)
 *
 * PARTICIPACION EN EL MOTOR POR TURNOS:
 * Cuando esta asignado, ejecutarTarea() realiza reconocimiento aereo.
 * Opcionalmente puede detectar el proximo evento del turno siguiente.
 * ============================================================================
 */
public class Dron extends Unidad {

    /** Alcance maximo de vuelo en kilometros (tipicamente corto). */
    private final double alcanceKm;

    /** Probabilidad de detectar un evento antes de que ocurra (0.0 - 1.0). */
    private final double probabilidadDeteccion;

    /**
     * Construye un Dron con su nombre, alcance y capacidad de deteccion.
     *
     * @param nombre                nombre del dron (ej. "Dron-Scout-01")
     * @param alcanceKm             alcance maximo de vuelo en km
     * @param probabilidadDeteccion probabilidad de deteccion de eventos (0.0-1.0)
     */
    public Dron(String nombre, double alcanceKm, double probabilidadDeteccion) {
        super(nombre);
        this.alcanceKm             = Math.max(0.5, alcanceKm);
        // Clampear la probabilidad entre 0 y 1
        this.probabilidadDeteccion = Math.max(0.0, Math.min(1.0, probabilidadDeteccion));
    }

    /**
     * Ejecuta la tarea de reconocimiento aereo del Dron.
     * Consume bateria rapidamente y es vulnerable al desgaste.
     * Los drones son fragiles: aplican mas desgaste por turno.
     *
     * @return descripcion del area mapeada
     */
    @Override
    public String ejecutarTarea() {
        if (!isDisponible()) {
            return getNombre() + " no esta disponible para volar.";
        }
        // Los drones son rapidos pero gastan bateria mas rapido
        consumirEnergia(25.0); // 25% de bateria por turno aereo
        // Los drones son fragiles: mas desgaste por ser mas ligeros
        aplicarDesgaste(0.06); // 6% de desgaste por turno
        return String.format("%s mapeo %.1f km cuadrados. Bateria restante: %.0f%%",
                getNombre(), alcanceKm * alcanceKm * 0.5, getEnergiaRestante());
    }

    /**
     * Calcula los recursos que el Dron consume del inventario colonial por turno.
     * Consume menos energia que el Rover (bateria interna mas eficiente) y
     * minimos repuestos.
     *
     * @return mapa de TipoRecurso -> cantidad consumida
     */
    @Override
    public Map<TipoRecurso, Double> calcularConsumo() {
        // Consumo reducido: es liviano y eficiente en energia
        return Map.of(
                TipoRecurso.ENERGIA,   8.0,  // menos consumo externo (bateria propia)
                TipoRecurso.REPUESTOS, 0.2   // componentes electronicos fragiles
        );
    }

    /** @return alcance maximo de vuelo en km */
    public double getAlcanceKm() { return alcanceKm; }

    /** @return probabilidad de deteccion de eventos anticipados */
    public double getProbabilidadDeteccion() { return probabilidadDeteccion; }
}
