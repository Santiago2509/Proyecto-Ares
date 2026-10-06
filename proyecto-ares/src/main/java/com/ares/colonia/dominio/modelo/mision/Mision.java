package com.ares.colonia.dominio.modelo.mision;

import com.ares.colonia.dominio.colonia.Colonia;
import com.ares.colonia.dominio.excepciones.MisionImposibleException;
import com.ares.colonia.dominio.personal.Tripulante;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

/**
 * ============================================================================
 * ARCHIVO: Mision.java
 * PAQUETE: com.ares.colonia.dominio.modelo.mision
 * ============================================================================
 *
 * PROPOSITO Y FUNCION PRINCIPAL:
 * Clase abstracta base para todas las misiones que puede ejecutar una colonia:
 * Exploracion, ConstruccionDeModulo, Investigacion, Rescate y Terraformacion.
 * Centraliza los atributos comunes (duracion, riesgo, recompensa, tripulacion
 * asignada) y define los contratos abstractos de validacion y ejecucion que
 * cada tipo de mision implementa segun sus reglas de negocio.
 *
 * ANOTACIONES Y ETIQUETAS UTILIZADAS: Ninguna directa.
 * HERENCIA E INTERFACES IMPLEMENTADAS:
 * - Extiende de: Ninguna
 * - Implementa: Ninguna
 *
 * PARTICIPACION EN EL MOTOR POR TURNOS:
 * En la fase de misiones (paso 4), el motor llama a:
 *   1. validarRequisitos(colonia) -> lanza excepcion si no se puede ejecutar
 *   2. avanzarTurno() -> reduce la duracion restante
 *   3. ejecutar() -> cuando duracion llega a 0, aplica el resultado
 * ============================================================================
 */
public abstract class Mision {

    /** Identificador unico de la mision. */
    private final String id;

    /** Nombre descriptivo de la mision. */
    private final String nombre;

    /** Descripcion del objetivo y resultado esperado. */
    private final String descripcion;

    /** Duracion total en sols (turnos) que requiere la mision. */
    private final int duracionTotal;

    /** Sols restantes para completar la mision. */
    private int duracionRestante;

    /**
     * Nivel de riesgo para los tripulantes (0.0 = sin riesgo, 1.0 = mortal).
     * Afecta los signos vitales de los tripulantes asignados durante la mision.
     */
    private final double nivelRiesgo;

    /** Estado actual de la mision. */
    private EstadoMision estado;

    /** Tripulantes asignados a esta mision. */
    private final List<Tripulante> tripulacionAsignada;

    /**
     * Construye una mision con sus parametros base.
     *
     * @param nombre       nombre de la mision
     * @param descripcion  descripcion del objetivo
     * @param duracionTotal duracion en sols
     * @param nivelRiesgo  riesgo para la tripulacion (0.0 - 1.0)
     */
    protected Mision(String nombre, String descripcion, int duracionTotal, double nivelRiesgo) {
        if (nombre == null || nombre.isBlank())
            throw new IllegalArgumentException("El nombre de la mision no puede ser vacio.");
        if (duracionTotal <= 0)
            throw new IllegalArgumentException("La duracion debe ser al menos 1 sol.");

        this.id                  = UUID.randomUUID().toString();
        this.nombre              = nombre;
        this.descripcion         = descripcion;
        this.duracionTotal       = duracionTotal;
        this.duracionRestante    = duracionTotal;
        this.nivelRiesgo         = Math.max(0.0, Math.min(1.0, nivelRiesgo));
        this.estado              = EstadoMision.PENDIENTE;
        this.tripulacionAsignada = new ArrayList<>();
    }

    // =========================================================================
    // METODOS ABSTRACTOS
    // =========================================================================

    /**
     * Valida que la colonia cumpla todos los requisitos para lanzar esta mision
     * (tripulacion, unidades, recursos, modulos operativos).
     *
     * @param colonia la colonia que ejecutara la mision
     * @throws MisionImposibleException    si las condiciones del entorno lo impiden
     * @throws com.ares.colonia.dominio.excepciones.EquipoInsuficienteException si falta tripulacion
     * @throws com.ares.colonia.dominio.excepciones.RecursoAgotadoException     si faltan recursos
     */
    public abstract void validarRequisitos(Colonia colonia);

    /**
     * Ejecuta el resultado final de la mision cuando la duracion llega a 0.
     * Aplica las recompensas (recursos, modulos, tecnologias) o penalizaciones
     * sobre la colonia y los tripulantes asignados.
     *
     * @return descripcion del resultado obtenido
     */
    public abstract String ejecutar();

    // =========================================================================
    // LOGICA DEL MOTOR DE TURNOS
    // =========================================================================

    /**
     * Avanza la mision un sol (turno). Si la duracion llega a 0, cambia
     * el estado a COMPLETADA.
     *
     * @return true si la mision se completo en este turno
     */
    public boolean avanzarTurno() {
        if (estado != EstadoMision.EN_CURSO) return false;

        // Reducir la duracion restante en 1 sol
        this.duracionRestante = Math.max(0, this.duracionRestante - 1);

        // Si llego a cero, la mision se completa
        if (this.duracionRestante == 0) {
            this.estado = EstadoMision.COMPLETADA;
            return true;
        }
        return false;
    }

    /**
     * Inicia la mision, cambiando su estado de PENDIENTE a EN_CURSO.
     *
     * @throws IllegalStateException si la mision no esta en estado PENDIENTE
     */
    public void iniciar() {
        if (estado != EstadoMision.PENDIENTE)
            throw new IllegalStateException("La mision '" + nombre + "' no esta en estado PENDIENTE.");
        this.estado = EstadoMision.EN_CURSO;
    }

    /**
     * Cancela la mision y libera a los tripulantes asignados.
     */
    public void cancelar() {
        this.estado = EstadoMision.CANCELADA;
        tripulacionAsignada.forEach(t -> t.setModuloAsignado(null));
        tripulacionAsignada.clear();
    }

    /**
     * Asigna un tripulante a la mision si esta disponible.
     *
     * @param tripulante el tripulante a asignar
     */
    public void asignarTripulante(Tripulante tripulante) {
        if (!tripulante.esOperativo())
            throw new IllegalStateException(
                "El tripulante " + tripulante.getNombre() + " no esta en estado operativo.");
        tripulacionAsignada.add(tripulante);
    }

    // =========================================================================
    // GETTERS
    // =========================================================================

    public String getId()                              { return id; }
    public String getNombre()                          { return nombre; }
    public String getDescripcion()                     { return descripcion; }
    public int getDuracionTotal()                      { return duracionTotal; }
    public int getDuracionRestante()                   { return duracionRestante; }
    public double getNivelRiesgo()                     { return nivelRiesgo; }
    public EstadoMision getEstado()                    { return estado; }
    public List<Tripulante> getTripulacionAsignada()   { return Collections.unmodifiableList(tripulacionAsignada); }

    @Override
    public String toString() {
        return String.format("Mision[%s | %s | Duracion: %d/%d sols | Estado: %s]",
                nombre, getClass().getSimpleName(), duracionRestante, duracionTotal, estado);
    }
}
