package com.ares.colonia.dominio.modelo.mision;

import com.ares.colonia.dominio.colonia.Colonia;
import com.ares.colonia.dominio.excepciones.EquipoInsuficienteException;
import com.ares.colonia.dominio.excepciones.MisionImposibleException;
import com.ares.colonia.dominio.personal.Medico;
import com.ares.colonia.dominio.personal.Tripulante;

import java.util.List;

/**
 * ============================================================================
 * ARCHIVO: Rescate.java
 * PAQUETE: com.ares.colonia.dominio.modelo.mision
 * ============================================================================
 *
 * PROPOSITO Y FUNCION PRINCIPAL:
 * Mision de rescate de tripulantes en estado critico o de recuperacion de
 * modulos averiados. Puede realizarse dentro de la colonia propia o ser
 * inter-colonial (requiere coordinacion con otra colonia de la red).
 * Es una de las pocas misiones que puede salvar vidas directamente al
 * restaurar la salud de los tripulantes rescatados.
 *
 * ANOTACIONES Y ETIQUETAS UTILIZADAS: Ninguna directa.
 * HERENCIA E INTERFACES IMPLEMENTADAS:
 * - Extiende de: Mision
 *
 * PARTICIPACION EN EL MOTOR POR TURNOS:
 * Es una mision de alta prioridad. Al completarse, ejecutar() restaura
 * la salud de los tripulantes rescatados y los devuelve al estado ESTABLE.
 * ============================================================================
 */
public class Rescate extends Mision {

    /** Tripulantes que esta mision intentara rescatar. */
    private final List<Tripulante> tripulantesARescatar;

    /** Indica si es una mision inter-colonial (entre dos colonias). */
    private final boolean interColonial;

    /** Numero de tripulantes rescatados exitosamente al finalizar. */
    private int rescatadosExitosos;

    /**
     * Construye una mision de rescate.
     *
     * @param nombre                nombre de la mision
     * @param tripulantesARescatar  lista de tripulantes a rescatar
     * @param duracionSols          duracion en sols
     * @param interColonial         true si involucra a otra colonia
     */
    public Rescate(String nombre, List<Tripulante> tripulantesARescatar,
                   int duracionSols, boolean interColonial) {
        super(
            nombre,
            "Rescate de " + tripulantesARescatar.size() + " tripulantes." +
            (interColonial ? " Operacion inter-colonial." : ""),
            duracionSols,
            interColonial ? 0.40 : 0.20  // mayor riesgo si es inter-colonial
        );
        this.tripulantesARescatar = List.copyOf(tripulantesARescatar);
        this.interColonial        = interColonial;
        this.rescatadosExitosos   = 0;
    }

    /**
     * Valida los requisitos del rescate:
     * - Al menos un Medico operativo.
     * - Para rescates inter-coloniales: Hangar y CentroDeComunicaciones activos.
     *
     * @param colonia la colonia que ejecuta el rescate
     * @throws EquipoInsuficienteException si no hay Medico disponible
     * @throws MisionImposibleException    si faltan infraestructuras para el rescate inter-colonial
     */
    @Override
    public void validarRequisitos(Colonia colonia) {
        // Debe haber al menos un medico operativo
        boolean medicoDisponible = colonia.getTripulantes().stream()
                .filter(t -> t instanceof Medico && t.esOperativo())
                .findAny().isPresent();
        if (!medicoDisponible)
            throw new EquipoInsuficienteException(getNombre(), "No hay ningun Medico operativo.");

        // Si es inter-colonial, verificar que el hangar y las comunicaciones esten activos
        if (interColonial) {
            boolean hangarOk = colonia.getModulos().stream()
                    .filter(m -> m instanceof com.ares.colonia.dominio.modelo.modulo.Hangar)
                    .anyMatch(m -> m.estaOperativo());
            boolean comOk = colonia.getModulos().stream()
                    .filter(m -> m instanceof com.ares.colonia.dominio.modelo.modulo.CentroDeComunicaciones)
                    .anyMatch(m -> m.estaOperativo());
            if (!hangarOk || !comOk)
                throw new MisionImposibleException(getNombre(),
                    "El rescate inter-colonial requiere Hangar y Centro de Comunicaciones operativos.");
        }
    }

    /**
     * Ejecuta el rescate al completarse la mision.
     * Cada tripulante objetivo tiene una probabilidad de ser rescatado
     * basada en el numero de medicos y la duracion de la mision.
     *
     * @return descripcion del resultado del rescate
     */
    @Override
    public String ejecutar() {
        this.rescatadosExitosos = 0;

        // Probabilidad de exito por tripulante (80% base, reducida segun riesgo)
        double probExito = 0.80 - (getNivelRiesgo() * 0.30);

        for (Tripulante t : tripulantesARescatar) {
            if (Math.random() < probExito) {
                this.rescatadosExitosos++;
                // El servicio se encargara de restaurar los signos vitales del tripulante
            }
        }

        return String.format(
            "Mision '%s' completada. Rescatados: %d / %d tripulantes.%s",
            getNombre(), rescatadosExitosos, tripulantesARescatar.size(),
            interColonial ? " (Operacion inter-colonial exitosa.)" : "");
    }

    /** @return lista inmutable de tripulantes a rescatar */
    public List<Tripulante> getTripulantesARescatar() { return tripulantesARescatar; }

    /** @return true si es una mision inter-colonial */
    public boolean isInterColonial() { return interColonial; }

    /** @return numero de tripulantes rescatados exitosamente */
    public int getRescatadosExitosos() { return rescatadosExitosos; }
}
