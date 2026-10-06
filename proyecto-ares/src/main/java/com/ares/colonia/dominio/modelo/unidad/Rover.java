package com.ares.colonia.dominio.modelo.unidad;

import com.ares.colonia.dominio.modelo.recurso.TipoRecurso;

import java.util.Map;

/**
 * ============================================================================
 * ARCHIVO: Rover.java
 * PAQUETE: com.ares.colonia.dominio.modelo.unidad
 * ============================================================================
 *
 * PROPOSITO Y FUNCION PRINCIPAL:
 * Unidad terrestre de exploracion de la colonia marciana. El Rover recorre
 * el terreno, tiene buena capacidad de carga y puede descubrir recursos o
 * puntos de interes. Es el vehiculo principal para misiones de exploracion
 * y rescate. Su desventaja es que es lento y consume mucha energia.
 *
 * ANOTACIONES Y ETIQUETAS UTILIZADAS: Ninguna directa.
 * HERENCIA E INTERFACES IMPLEMENTADAS:
 * - Extiende de: Unidad
 * - Implementa: Reparable (heredado de Unidad)
 *
 * PARTICIPACION EN EL MOTOR POR TURNOS:
 * Cuando esta asignado a una mision de Exploracion o Rescate, ejecutarTarea()
 * avanza la exploracion y calcularConsumo() debita la energia del inventario.
 * ============================================================================
 */
public class Rover extends Unidad {

    /** Alcance maximo de exploracion en kilometros. */
    private final double alcanceKm;

    /** Capacidad de carga en kilogramos para transportar recursos. */
    private final double capacidadCargaKg;

    /**
     * Construye un Rover con su nombre, alcance y capacidad de carga.
     *
     * @param nombre          nombre identificador del rover (ej. "Rover-01")
     * @param alcanceKm       alcance maximo en km
     * @param capacidadCargaKg capacidad de carga en kg
     */
    public Rover(String nombre, double alcanceKm, double capacidadCargaKg) {
        super(nombre);
        // Validar que los parametros sean positivos
        this.alcanceKm        = Math.max(1.0, alcanceKm);
        this.capacidadCargaKg = Math.max(0.0, capacidadCargaKg);
    }

    /**
     * Ejecuta la tarea de exploracion del Rover en un turno de mision.
     * Consume energia de su bateria y aplica desgaste por operacion terrestre.
     *
     * @return descripcion del resultado (km explorados)
     */
    @Override
    public String ejecutarTarea() {
        if (!isDisponible()) {
            return getNombre() + " no esta disponible para explorar.";
        }
        // Consumir energia de la bateria interna
        consumirEnergia(15.0); // el Rover consume 15% de bateria por turno
        // Aplicar desgaste mecanico por recorrer terreno marciano
        aplicarDesgaste(0.04); // 4% de desgaste por turno de operacion
        return String.format("%s exploro %.1f km. Desgaste actual: %.1f%%",
                getNombre(), alcanceKm * 0.8, getNivelDesgaste() * 100);
    }

    /**
     * Calcula los recursos que el Rover consume del inventario colonial por turno.
     * El Rover consume energia (alto) y repuestos (bajo).
     *
     * @return mapa de TipoRecurso -> cantidad consumida
     */
    @Override
    public Map<TipoRecurso, Double> calcularConsumo() {
        // Alto consumo de energia; bajo de repuestos por lubricacion
        return Map.of(
                TipoRecurso.ENERGIA,   20.0,  // energetico pero necesario
                TipoRecurso.REPUESTOS, 0.5    // mantenimiento basico
        );
    }

    /** @return alcance maximo de exploracion en km */
    public double getAlcanceKm() { return alcanceKm; }

    /** @return capacidad de carga maxima en kg */
    public double getCapacidadCargaKg() { return capacidadCargaKg; }
}
