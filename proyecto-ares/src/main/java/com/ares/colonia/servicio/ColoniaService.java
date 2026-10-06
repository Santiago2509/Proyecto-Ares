package com.ares.colonia.servicio;

import com.ares.colonia.dominio.colonia.Colonia;
import com.ares.colonia.dominio.modelo.recurso.TipoRecurso;
import com.ares.colonia.dominio.excepciones.ColoniaNoEncontradaException;
import com.ares.colonia.dominio.excepciones.MoralCriticaException;
import com.ares.colonia.dominio.modelo.modulo.Modulo;
import com.ares.colonia.dominio.modelo.unidad.Unidad;
import com.ares.colonia.dominio.personal.Tripulante;
import com.ares.colonia.repositorio.Repositorio;

import java.util.List;
import java.util.logging.Logger;

/**
 * ============================================================================
 * ARCHIVO: ColoniaService.java
 * PAQUETE: com.ares.colonia.servicio
 * ============================================================================
 *
 * PROPOSITO Y FUNCION PRINCIPAL:
 * Servicio de aplicacion que encapsula las operaciones de gestion de una
 * colonia individual: registro de tripulantes, asignacion a modulos, consulta
 * de reportes de vitalidad, y validacion de permisos para operaciones
 * sensibles. Actua como puerta de entrada al dominio desde la capa de control.
 *
 * ANOTACIONES Y ETIQUETAS UTILIZADAS: Ninguna directa.
 * HERENCIA E INTERFACES IMPLEMENTADAS:
 * - Extiende de: Ninguna
 * - Implementa: Ninguna
 *
 * PARTICIPACION EN EL MOTOR POR TURNOS:
 * No participa directamente en el ciclo de turnos; es invocado por el
 * usuario (o por Main) para configurar la colonia antes de iniciar la simulacion.
 * ============================================================================
 */
public class ColoniaService {

    private static final Logger LOG = Logger.getLogger(ColoniaService.class.getName());

    /** Repositorio donde se persisten las colonias. */
    private final Repositorio<Colonia> repositorio;

    /**
     * Construye el servicio con su repositorio de colonias.
     *
     * @param repositorio repositorio de colonias
     */
    public ColoniaService(Repositorio<Colonia> repositorio) {
        this.repositorio = repositorio;
    }

    /**
     * Registra una nueva colonia en el repositorio.
     *
     * @param colonia la colonia a registrar
     */
    public void registrarColonia(Colonia colonia) {
        repositorio.guardar(colonia);
        LOG.info("Colonia '" + colonia.getNombre() + "' registrada.");
    }

    /**
     * Busca una colonia por su ID.
     *
     * @param id identificador de la colonia
     * @return la colonia encontrada
     * @throws ColoniaNoEncontradaException si no existe
     */
    public Colonia buscarColonia(String id) {
        return repositorio.buscarPorId(id)
                .orElseThrow(() -> new ColoniaNoEncontradaException(id));
    }

    /**
     * Lista todas las colonias registradas.
     *
     * @return lista de colonias
     */
    public List<Colonia> listarColonias() {
        return repositorio.listarTodas();
    }

    /**
     * Agrega un tripulante a una colonia existente.
     *
     * @param idColonia   id de la colonia
     * @param tripulante  el tripulante a agregar
     */
    public void agregarTripulante(String idColonia, Tripulante tripulante) {
        Colonia colonia = buscarColonia(idColonia);
        colonia.agregarTripulante(tripulante);
        repositorio.guardar(colonia);
        LOG.info("Tripulante '" + tripulante.getNombre() + "' agregado a colonia '" + colonia.getNombre() + "'.");
    }

    /**
     * Agrega un modulo a una colonia existente.
     *
     * @param idColonia id de la colonia
     * @param modulo    el modulo a agregar
     */
    public void agregarModulo(String idColonia, Modulo modulo) {
        Colonia colonia = buscarColonia(idColonia);
        colonia.agregarModulo(modulo);
        repositorio.guardar(colonia);
        LOG.info("Modulo '" + modulo.getNombre() + "' agregado a colonia '" + colonia.getNombre() + "'.");
    }

    /**
     * Agrega una unidad robotica a una colonia.
     *
     * @param idColonia id de la colonia
     * @param unidad    la unidad a agregar
     */
    public void agregarUnidad(String idColonia, Unidad unidad) {
        Colonia colonia = buscarColonia(idColonia);
        colonia.agregarUnidad(unidad);
        repositorio.guardar(colonia);
    }

    /**
     * Asigna un tripulante a un modulo especifico dentro de la colonia.
     *
     * @param colonia    la colonia donde opera
     * @param tripulante el tripulante a asignar
     * @param modulo     el modulo destino
     */
    public void asignarTripulanteAModulo(Colonia colonia, Tripulante tripulante, Modulo modulo) {
        if (!tripulante.esOperativo())
            throw new IllegalStateException(
                "El tripulante " + tripulante.getNombre() + " no esta operativo.");
        tripulante.setModuloAsignado(modulo);
        repositorio.guardar(colonia);
    }

    /**
     * Genera y retorna el reporte de vitalidad de todos los tripulantes
     * de una colonia dada.
     *
     * @param idColonia id de la colonia
     * @return cadena con el estado vital de cada tripulante
     */
    public String reporteVitalidad(String idColonia) {
        Colonia colonia = buscarColonia(idColonia);
        StringBuilder sb = new StringBuilder();
        sb.append("=== Vitalidad - ").append(colonia.getNombre())
          .append(" (Sol ").append(colonia.getSolActual()).append(") ===\n");
        colonia.getTripulantes().forEach(t ->
            sb.append("  ").append(t.getNombre())
              .append(" [").append(t.getClass().getSimpleName()).append("]")
              .append(" -> ").append(t.getEstadoVital())
              .append(" | Salud: ").append(String.format("%.1f", t.getSignosVitales().salud()))
              .append("\n"));
        return sb.toString();
    }

    /**
     * Verifica si la moral de la colonia es critica (promedio < 20).
     * Lanza MoralCriticaException si se detecta.
     *
     * @param colonia la colonia a evaluar
     * @throws MoralCriticaException si la moral promedio es critica
     */
    public void verificarMoral(Colonia colonia) {
        double promedioMoral = colonia.getTripulantes().stream()
                .mapToDouble(t -> t.getSignosVitales().moral())
                .average()
                .orElse(100.0);
        if (promedioMoral < 20.0)
            throw new MoralCriticaException(colonia.getNombre(), promedioMoral);
    }
}
