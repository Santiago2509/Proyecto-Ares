package com.ares.colonia.dominio.colonia;

import com.ares.colonia.dominio.excepciones.ColoniaException;
import com.ares.colonia.dominio.excepciones.ModuloAveriadoException;
import com.ares.colonia.dominio.excepciones.TripulanteCriticoException;
import com.ares.colonia.dominio.excepciones.TripulanteFallecidoException;
import com.ares.colonia.dominio.modelo.evento.Evento;
import com.ares.colonia.dominio.modelo.mision.EstadoMision;
import com.ares.colonia.dominio.modelo.mision.Mision;
import com.ares.colonia.dominio.modelo.modulo.Modulo;
import com.ares.colonia.dominio.modelo.recurso.TipoRecurso;
import com.ares.colonia.dominio.modelo.unidad.Unidad;
import com.ares.colonia.dominio.personal.Tripulante;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * ============================================================================
 * ARCHIVO: Colonia.java
 * PAQUETE: com.ares.colonia.dominio.colonia
 * ============================================================================
 *
 * PROPOSITO Y FUNCION PRINCIPAL:
 * Representa una colonia humana en Marte. Es el contenedor central del dominio:
 * agrupa modulos, tripulantes, unidades, misiones activas y el inventario de
 * recursos. Implementa la logica de cada fase del turno (sol): aplicar eventos,
 * operar modulos, actualizar vitalidad y avanzar misiones.
 *
 * ANOTACIONES Y ETIQUETAS UTILIZADAS: Ninguna directa.
 * HERENCIA E INTERFACES IMPLEMENTADAS:
 * - Extiende de: Ninguna
 * - Implementa: Ninguna
 *
 * PARTICIPACION EN EL MOTOR POR TURNOS:
 * RedColonial.avanzarTurno() invoca avanzarTurno() en cada Colonia. El metodo
 * ejecuta las 6 fases del ciclo: eventos, modulos, vitalidad, misiones, alertas
 * y reporte, en ese orden.
 * ============================================================================
 */
public class Colonia {

    private static final Logger LOG = Logger.getLogger(Colonia.class.getName());

    /** Identificador unico de la colonia. */
    private final String id;

    /** Nombre de la colonia (ej. "Colonia Ares Alpha"). */
    private final String nombre;

    /** Numero de sol (turno) actual de la colonia. */
    private int solActual;

    /** Inventario de recursos de la colonia. */
    private final Inventario inventario;

    /** Modulos fisicos de la colonia. */
    private final List<Modulo> modulos;

    /** Tripulacion de la colonia. */
    private final List<Tripulante> tripulantes;

    /** Unidades roboticas de la colonia. */
    private final List<Unidad> unidades;

    /** Misiones activas o en proceso de la colonia. */
    private final List<Mision> misiones;

    /** Historial de eventos ocurridos en la colonia. */
    private final List<String> historialEventos;

    /**
     * Construye una nueva colonia con su nombre.
     *
     * @param nombre nombre de la colonia (no puede ser nulo o vacio)
     * @throws IllegalArgumentException si el nombre es invalido
     */
    public Colonia(String nombre) {
        if (nombre == null || nombre.isBlank())
            throw new IllegalArgumentException("El nombre de la colonia no puede ser nulo o vacio.");
        this.id               = UUID.randomUUID().toString();
        this.nombre           = nombre;
        this.solActual        = 0;
        this.inventario       = new Inventario();
        this.modulos          = new ArrayList<>();
        this.tripulantes      = new ArrayList<>();
        this.unidades         = new ArrayList<>();
        this.misiones         = new ArrayList<>();
        this.historialEventos = new ArrayList<>();
    }

    // =========================================================================
    // MOTOR DE TURNOS
    // =========================================================================

    /**
     * Avanza la colonia un sol (turno) completo. Ejecuta las 6 fases del ciclo:
     * 1. Modulos: producen y consumen recursos, y se desgastan.
     * 2. Vitalidad: se recalculan los signos vitales de cada tripulante.
     * 3. Misiones: avanzan las misiones EN_CURSO y se resuelven las completadas.
     *
     * Los eventos son aplicados desde fuera (por EventoService) antes de llamar
     * a este metodo.
     */
    public void avanzarTurno() {
        this.solActual++;
        LOG.info("[Sol " + solActual + "] Colonia " + nombre + " avanzando turno.");

        // FASE 1: Actualizar inventario (efectos de turno en recursos)
        inventario.actualizarPorTurno();

        // FASE 2: Operar modulos (produccion, consumo y desgaste)
        operarModulos();

        // FASE 3: Actualizar vitalidad de la tripulacion
        actualizarVitalidadTripulacion();

        // FASE 4: Avanzar misiones activas
        avanzarMisiones();

        // FASE 5: Consumo de recursos por parte de la tripulacion
        consumirRecursosTripulacion();
    }

    /**
     * Aplica un evento sobre esta colonia.
     *
     * @param evento el evento a aplicar
     */
    public void aplicarEvento(Evento evento) {
        LOG.warning("[Sol " + solActual + "] Evento aplicado en " + nombre + ": " + evento.getNombre());
        historialEventos.add("Sol " + solActual + " - " + evento);
        evento.aplicar(this);
    }

    // =========================================================================
    // FASES DEL TURNO (privadas)
    // =========================================================================

    /**
     * Opera todos los modulos de la colonia: calcula eficiencia y desgaste.
     * Los modulos averiados se desactivan y se registra la averia.
     */
    private void operarModulos() {
        for (Modulo modulo : modulos) {
            if (!modulo.estaOperativo()) continue;
            try {
                // Calcular eficiencia (produce recursos proporcional a ella)
                double eficiencia = modulo.calcularEficiencia();
                LOG.fine("  Modulo " + modulo.getNombre() + " eficiencia: " + eficiencia);

                // Consumir recursos del modulo
                modulo.consumirPorTurno().forEach((tipo, cantidad) ->
                    inventario.consumirSeguro(tipo, cantidad * eficiencia));

                // Aplicar desgaste del turno
                modulo.aplicarDesgaste();

            } catch (ModuloAveriadoException e) {
                LOG.warning("  AVERIA: " + e.getMessage());
                historialEventos.add("Sol " + solActual + " - Modulo averiado: " + modulo.getNombre());
            } catch (Exception e) {
                LOG.log(Level.SEVERE, "Error operando modulo " + modulo.getNombre(), e);
            }
        }
    }

    /**
     * Recalcula la vitalidad de cada tripulante. Elimina a los fallecidos.
     */
    private void actualizarVitalidadTripulacion() {
        List<Tripulante> fallecidos = new ArrayList<>();
        for (Tripulante t : tripulantes) {
            try {
                t.actualizarVitalidad();
            } catch (TripulanteFallecidoException e) {
                LOG.severe("  FALLECIDO: " + e.getMessage());
                fallecidos.add(t);
                historialEventos.add("Sol " + solActual + " - Fallecio: " + t.getNombre());
            } catch (TripulanteCriticoException e) {
                LOG.warning("  CRITICO: " + e.getMessage());
                historialEventos.add("Sol " + solActual + " - Estado critico: " + t.getNombre());
            }
        }
        // Remover tripulantes fallecidos de la colonia
        tripulantes.removeAll(fallecidos);
    }

    /**
     * Avanza un sol en todas las misiones EN_CURSO. Si se completan, llama a ejecutar().
     */
    private void avanzarMisiones() {
        for (Mision mision : misiones) {
            if (mision.getEstado() != EstadoMision.EN_CURSO) continue;
            boolean completada = mision.avanzarTurno();
            if (completada) {
                String resultado = mision.ejecutar();
                LOG.info("  Mision completada: " + resultado);
                historialEventos.add("Sol " + solActual + " - " + resultado);
            }
        }
    }

    /**
     * Descuenta del inventario los recursos consumidos por cada tripulante operativo.
     */
    private void consumirRecursosTripulacion() {
        for (Tripulante t : tripulantes) {
            if (!t.esOperativo()) continue;
            t.consumirPorTurno().forEach((tipo, cantidad) ->
                inventario.consumirSeguro(tipo, cantidad));
        }
    }

    // =========================================================================
    // GESTION DE ENTIDADES
    // =========================================================================

    /**
     * Agrega un modulo a la colonia.
     *
     * @param modulo el modulo a agregar (no nulo)
     */
    public void agregarModulo(Modulo modulo) {
        if (modulo == null) throw new IllegalArgumentException("El modulo no puede ser nulo.");
        modulos.add(modulo);
    }

    /**
     * Registra un tripulante en la colonia.
     *
     * @param tripulante el tripulante a registrar
     */
    public void agregarTripulante(Tripulante tripulante) {
        if (tripulante == null) throw new IllegalArgumentException("El tripulante no puede ser nulo.");
        tripulantes.add(tripulante);
    }

    /**
     * Agrega una unidad robotica a la colonia.
     *
     * @param unidad la unidad a agregar
     */
    public void agregarUnidad(Unidad unidad) {
        if (unidad == null) throw new IllegalArgumentException("La unidad no puede ser nula.");
        unidades.add(unidad);
    }

    /**
     * Registra una mision en la colonia. La mision inicia en estado PENDIENTE.
     *
     * @param mision la mision a registrar
     */
    public void registrarMision(Mision mision) {
        if (mision == null) throw new IllegalArgumentException("La mision no puede ser nula.");
        misiones.add(mision);
    }

    // =========================================================================
    // GETTERS
    // =========================================================================

    /** @return id unico de la colonia */
    public String getId() { return id; }

    /** @return nombre de la colonia */
    public String getNombre() { return nombre; }

    /** @return sol (turno) actual */
    public int getSolActual() { return solActual; }

    /** @return inventario de recursos */
    public Inventario getInventario() { return inventario; }

    /** @return lista inmutable de modulos */
    public List<Modulo> getModulos() { return Collections.unmodifiableList(modulos); }

    /** @return lista inmutable de tripulantes */
    public List<Tripulante> getTripulantes() { return Collections.unmodifiableList(tripulantes); }

    /** @return lista inmutable de unidades */
    public List<Unidad> getUnidades() { return Collections.unmodifiableList(unidades); }

    /** @return lista inmutable de misiones */
    public List<Mision> getMisiones() { return Collections.unmodifiableList(misiones); }

    /** @return historial de eventos de la colonia */
    public List<String> getHistorialEventos() { return Collections.unmodifiableList(historialEventos); }

    /** @return numero de tripulantes vivos */
    public int getTripulantesVivos() { return tripulantes.size(); }

    /** @return true si la colonia tiene al menos un tripulante vivo */
    public boolean estaViva() { return !tripulantes.isEmpty(); }

    /**
     * Genera un reporte de estado actual de la colonia.
     *
     * @return cadena con el resumen de la colonia en el sol actual
     */
    public String generarReporte() {
        StringBuilder sb = new StringBuilder();
        sb.append("=== Reporte Colonia: ").append(nombre)
          .append(" | Sol ").append(solActual).append(" ===\n");
        sb.append("Tripulantes vivos: ").append(tripulantes.size()).append("\n");
        sb.append("Modulos activos: ")
          .append(modulos.stream().filter(Modulo::estaOperativo).count()).append("\n");
        sb.append("Misiones en curso: ")
          .append(misiones.stream().filter(m -> m.getEstado() == EstadoMision.EN_CURSO).count()).append("\n");
        sb.append(inventario.toString());
        return sb.toString();
    }

    @Override
    public String toString() {
        return String.format("Colonia[%s | Sol: %d | Tripulantes: %d | Modulos: %d]",
                nombre, solActual, tripulantes.size(), modulos.size());
    }
}
