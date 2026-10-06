package com.ares.colonia.dominio.colonia;

import com.ares.colonia.dominio.excepciones.ColoniaNoEncontradaException;
import com.ares.colonia.dominio.excepciones.TransferenciaInvalidaException;
import com.ares.colonia.dominio.modelo.evento.Evento;
import com.ares.colonia.dominio.modelo.recurso.TipoRecurso;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;
import java.util.logging.Logger;

/**
 * ============================================================================
 * ARCHIVO: RedColonial.java
 * PAQUETE: com.ares.colonia.dominio.colonia
 * ============================================================================
 *
 * PROPOSITO Y FUNCION PRINCIPAL:
 * Contenedor principal del sistema. Agrupa todas las colonias humanas en Marte
 * y coordina los ciclos de simulacion globales. Gestiona las transferencias de
 * recursos entre colonias, la aplicacion de eventos globales y la generacion
 * de reportes comparativos de toda la red.
 *
 * ANOTACIONES Y ETIQUETAS UTILIZADAS: Ninguna directa.
 * HERENCIA E INTERFACES IMPLEMENTADAS:
 * - Extiende de: Ninguna
 * - Implementa: Ninguna
 *
 * PARTICIPACION EN EL MOTOR POR TURNOS:
 * SimulacionService invoca avanzarTurnoGlobal() en cada sol. Este metodo
 * primero aplica los eventos globales sobre todas las colonias y luego
 * delega avanzarTurno() a cada Colonia individual.
 * ============================================================================
 */
public class RedColonial {

    private static final Logger LOG = Logger.getLogger(RedColonial.class.getName());

    /** Identificador unico de la red colonial. */
    private final String id;

    /** Nombre de la red (ej. "Red Ares - Primera Oleada"). */
    private final String nombre;

    /** Lista de colonias que forman parte de la red. */
    private final List<Colonia> colonias;

    /** Numero de sol global (turno compartido por toda la red). */
    private int solGlobal;

    /** Historial de eventos globales que afectaron a toda la red. */
    private final List<String> historialGlobal;

    /**
     * Construye una red colonial con su nombre.
     *
     * @param nombre nombre de la red colonial
     * @throws IllegalArgumentException si el nombre es nulo o vacio
     */
    public RedColonial(String nombre) {
        if (nombre == null || nombre.isBlank())
            throw new IllegalArgumentException("El nombre de la red colonial no puede ser nulo o vacio.");
        this.id             = UUID.randomUUID().toString();
        this.nombre         = nombre;
        this.colonias       = new ArrayList<>();
        this.solGlobal      = 0;
        this.historialGlobal = new ArrayList<>();
    }

    // =========================================================================
    // MOTOR DE TURNOS GLOBAL
    // =========================================================================

    /**
     * Avanza toda la red colonial un sol (turno) completo.
     * Primero aplica los eventos globales sobre todas las colonias, luego
     * avanza el turno en cada colonia individualmente.
     *
     * @param eventosGlobales lista de eventos globales del turno
     */
    public void avanzarTurnoGlobal(List<Evento> eventosGlobales) {
        this.solGlobal++;
        LOG.info("=== Sol Global " + solGlobal + " - " + nombre + " ===");

        // PASO 1: Aplicar eventos globales a todas las colonias
        for (Evento evento : eventosGlobales) {
            if (evento.esGlobal()) {
                historialGlobal.add("Sol " + solGlobal + " - Evento Global: " + evento.getNombre());
                for (Colonia colonia : colonias) {
                    colonia.aplicarEvento(evento);
                }
            }
        }

        // PASO 2: Avanzar cada colonia individual
        for (Colonia colonia : colonias) {
            if (colonia.estaViva()) {
                colonia.avanzarTurno();
            } else {
                LOG.warning("Colonia " + colonia.getNombre() + " no tiene tripulantes vivos. Inactiva.");
            }
        }
    }

    // =========================================================================
    // GESTION DE COLONIAS
    // =========================================================================

    /**
     * Agrega una nueva colonia a la red.
     *
     * @param colonia la colonia a incorporar
     */
    public void agregarColonia(Colonia colonia) {
        if (colonia == null) throw new IllegalArgumentException("La colonia no puede ser nula.");
        colonias.add(colonia);
        LOG.info("Colonia '" + colonia.getNombre() + "' incorporada a la red.");
    }

    /**
     * Busca una colonia por su nombre.
     *
     * @param nombre nombre de la colonia buscada
     * @return la colonia encontrada
     * @throws ColoniaNoEncontradaException si no existe ninguna colonia con ese nombre
     */
    public Colonia buscarPorNombre(String nombre) {
        return colonias.stream()
                .filter(c -> c.getNombre().equalsIgnoreCase(nombre))
                .findFirst()
                .orElseThrow(() -> new ColoniaNoEncontradaException(nombre));
    }

    // =========================================================================
    // TRANSFERENCIAS INTER-COLONIALES
    // =========================================================================

    /**
     * Transfiere recursos de una colonia a otra dentro de la red.
     * Solo es posible si ambas colonias tienen sus Centros de Comunicaciones activos.
     *
     * @param origen   nombre de la colonia de origen
     * @param destino  nombre de la colonia de destino
     * @param tipo     tipo de recurso a transferir
     * @param cantidad cantidad a transferir
     * @throws ColoniaNoEncontradaException  si alguna colonia no existe
     * @throws TransferenciaInvalidaException si la transferencia no es viable
     */
    public void transferirRecurso(String origen, String destino,
                                   TipoRecurso tipo, double cantidad) {
        Colonia coloniaOrigen  = buscarPorNombre(origen);
        Colonia coloniaDestino = buscarPorNombre(destino);

        if (coloniaOrigen.equals(coloniaDestino))
            throw new TransferenciaInvalidaException(
                "El origen y el destino de la transferencia no pueden ser la misma colonia.");

        double disponible = coloniaOrigen.getInventario().getCantidad(tipo);
        if (disponible < cantidad)
            throw new TransferenciaInvalidaException(
                "La colonia '" + origen + "' no tiene suficiente " + tipo.getNombre() +
                ". Tiene: " + disponible + ", se piden: " + cantidad);

        // Ejecutar la transferencia
        coloniaOrigen.getInventario().consumir(tipo, cantidad);
        coloniaDestino.getInventario().agregar(tipo, cantidad);

        LOG.info("Transferencia: " + cantidad + " de " + tipo.getNombre() +
                 " de '" + origen + "' a '" + destino + "'.");
        historialGlobal.add("Sol " + solGlobal + " - Transferencia: " +
                cantidad + " " + tipo.getNombre() + " de " + origen + " -> " + destino);
    }

    // =========================================================================
    // REPORTES
    // =========================================================================

    /**
     * Genera un reporte comparativo de todas las colonias de la red.
     *
     * @return cadena con el estado de cada colonia
     */
    public String generarReporteRed() {
        StringBuilder sb = new StringBuilder();
        sb.append("========================================\n");
        sb.append("  RED COLONIAL: ").append(nombre).append("\n");
        sb.append("  Sol Global: ").append(solGlobal).append("\n");
        sb.append("  Colonias activas: ").append(
            colonias.stream().filter(Colonia::estaViva).count()).append(" / ").append(colonias.size()).append("\n");
        sb.append("========================================\n");
        for (Colonia c : colonias) {
            sb.append(c.generarReporte()).append("\n");
        }
        return sb.toString();
    }

    // =========================================================================
    // GETTERS
    // =========================================================================

    /** @return id unico de la red colonial */
    public String getId() { return id; }

    /** @return nombre de la red */
    public String getNombre() { return nombre; }

    /** @return sol global actual */
    public int getSolGlobal() { return solGlobal; }

    /** @return lista inmutable de colonias */
    public List<Colonia> getColonias() { return Collections.unmodifiableList(colonias); }

    /** @return historial de eventos globales */
    public List<String> getHistorialGlobal() { return Collections.unmodifiableList(historialGlobal); }

    @Override
    public String toString() {
        return String.format("RedColonial[%s | Sol: %d | Colonias: %d]",
                nombre, solGlobal, colonias.size());
    }
}
