package com.ares.colonia.dominio.modelo.mision;

import com.ares.colonia.dominio.colonia.Colonia;
import com.ares.colonia.dominio.excepciones.EquipoInsuficienteException;
import com.ares.colonia.dominio.excepciones.MisionImposibleException;
import com.ares.colonia.dominio.modelo.modulo.Hangar;
import com.ares.colonia.dominio.modelo.unidad.Dron;
import com.ares.colonia.dominio.modelo.unidad.Rover;
import com.ares.colonia.dominio.personal.Piloto;

/**
 * ============================================================================
 * ARCHIVO: Exploracion.java
 * PAQUETE: com.ares.colonia.dominio.modelo.mision
 * ============================================================================
 *
 * PROPOSITO Y FUNCION PRINCIPAL:
 * Mision que envia un Rover o Dron con un Piloto al exterior de la colonia
 * para explorar el terreno marciano. Puede descubrir nuevas fuentes de recursos,
 * puntos de interes o eventos proximos. Es la mision mas comun y la que
 * genera informacion critica para la supervivencia a largo plazo.
 *
 * ANOTACIONES Y ETIQUETAS UTILIZADAS: Ninguna directa.
 * HERENCIA E INTERFACES IMPLEMENTADAS:
 * - Extiende de: Mision
 *
 * PARTICIPACION EN EL MOTOR POR TURNOS:
 * En la fase de misiones (paso 4), avanzarTurno() reduce la duracion.
 * Al completarse, ejecutar() aplica las recompensas de exploracion.
 * ============================================================================
 */
public class Exploracion extends Mision {

    /** Radio de exploracion en kilometros. Define el alcance de la mision. */
    private final double radioExploradoKm;

    /** Indica si la mision descubrio una nueva fuente de recursos. */
    private boolean recursoDescubierto;

    /**
     * Construye una mision de exploracion.
     *
     * @param nombre          nombre de la mision
     * @param radioExploradoKm radio de exploracion en km
     * @param duracionSols    duracion en sols (turnos)
     */
    public Exploracion(String nombre, double radioExploradoKm, int duracionSols) {
        super(
            nombre,
            "Exploracion de " + radioExploradoKm + " km a la redonda en busca de recursos.",
            duracionSols,
            0.25 // 25% de riesgo: es peligroso operar fuera de la colonia
        );
        this.radioExploradoKm  = Math.max(1.0, radioExploradoKm);
        this.recursoDescubierto = false;
    }

    /**
     * Valida que la colonia tenga lo necesario para lanzar la exploracion:
     * - Un Hangar operativo.
     * - Al menos un Rover o Dron disponible.
     * - Al menos un Piloto operativo en la tripulacion.
     *
     * @param colonia la colonia que lanzara la mision
     * @throws MisionImposibleException   si no hay Hangar operativo
     * @throws EquipoInsuficienteException si no hay Rover/Dron o Piloto disponible
     */
    @Override
    public void validarRequisitos(Colonia colonia) {
        // El Hangar debe estar operativo para desplegar las unidades
        boolean hangarActivo = colonia.getModulos().stream()
                .filter(m -> m instanceof Hangar)
                .anyMatch(m -> m.estaOperativo());
        if (!hangarActivo)
            throw new MisionImposibleException(getNombre(), "No hay Hangar operativo para desplegar la mision.");

        // Debe haber al menos un Rover o Dron disponible
        boolean unidadDisponible = colonia.getUnidades().stream()
                .filter(u -> u instanceof Rover || u instanceof Dron)
                .anyMatch(u -> u.isDisponible());
        if (!unidadDisponible)
            throw new EquipoInsuficienteException(getNombre(), "No hay Rover ni Dron disponible.");

        // Debe haber al menos un Piloto operativo
        boolean pilotoDisponible = colonia.getTripulantes().stream()
                .filter(t -> t instanceof Piloto)
                .anyMatch(t -> t.esOperativo());
        if (!pilotoDisponible)
            throw new EquipoInsuficienteException(getNombre(), "No hay Piloto operativo para la mision.");
    }

    /**
     * Ejecuta el resultado final de la exploracion.
     * Con una probabilidad basada en el radio explorado, puede descubrir
     * una nueva fuente de recursos que se notifica al sistema.
     *
     * @return descripcion del resultado de la exploracion
     */
    @Override
    public String ejecutar() {
        // Probabilidad de descubrimiento basada en el radio explorado
        double probabilidad = Math.min(0.9, radioExploradoKm * 0.05);
        this.recursoDescubierto = Math.random() < probabilidad;

        if (recursoDescubierto) {
            return String.format(
                "Mision '%s' completada. Se descubrio una nueva fuente de recursos a %.1f km.",
                getNombre(), radioExploradoKm);
        }
        return String.format(
            "Mision '%s' completada. Area explorada: %.1f km. No se hallaron recursos nuevos.",
            getNombre(), radioExploradoKm);
    }

    /** @return radio explorado en km */
    public double getRadioExploradoKm() { return radioExploradoKm; }

    /** @return true si la exploracion descubrio un nuevo recurso */
    public boolean isRecursoDescubierto() { return recursoDescubierto; }
}
