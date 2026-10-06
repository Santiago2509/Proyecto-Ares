package com.ares.colonia.servicio;

import com.ares.colonia.dominio.colonia.Colonia;
import com.ares.colonia.dominio.excepciones.EquipoInsuficienteException;
import com.ares.colonia.dominio.excepciones.MisionImposibleException;
import com.ares.colonia.dominio.modelo.mision.EstadoMision;
import com.ares.colonia.dominio.modelo.mision.Mision;
import com.ares.colonia.dominio.personal.Tripulante;

import java.util.List;
import java.util.logging.Logger;
import java.util.stream.Collectors;

/**
 * ============================================================================
 * ARCHIVO: MisionService.java
 * PAQUETE: com.ares.colonia.servicio
 * ============================================================================
 *
 * PROPOSITO Y FUNCION PRINCIPAL:
 * Servicio de aplicacion que gestiona el ciclo de vida completo de las
 * misiones: validacion de requisitos, lanzamiento, asignacion de tripulacion,
 * avance por turno y resolucion de resultados. Actua como intermediario
 * entre la capa de control y las entidades del dominio Mision.
 *
 * ANOTACIONES Y ETIQUETAS UTILIZADAS: Ninguna directa.
 * HERENCIA E INTERFACES IMPLEMENTADAS:
 * - Extiende de: Ninguna
 * - Implementa: Ninguna
 *
 * PARTICIPACION EN EL MOTOR POR TURNOS:
 * Invocado por SimulacionService o por el LiderDeColonia para lanzar nuevas
 * misiones. El avance de misiones activas lo realiza Colonia.avanzarTurno().
 * ============================================================================
 */
public class MisionService {

    private static final Logger LOG = Logger.getLogger(MisionService.class.getName());

    /**
     * Lanza una mision desde una colonia: valida requisitos, asigna tripulacion
     * e inicia la mision.
     *
     * @param colonia     la colonia que ejecutara la mision
     * @param mision      la mision a lanzar
     * @param tripulantes la tripulacion que se asignara a la mision
     * @throws MisionImposibleException    si los requisitos no se cumplen
     * @throws EquipoInsuficienteException si la tripulacion es insuficiente
     */
    public void lanzarMision(Colonia colonia, Mision mision, List<Tripulante> tripulantes) {
        // 1. Validar requisitos del dominio
        mision.validarRequisitos(colonia);

        // 2. Asignar tripulacion a la mision
        for (Tripulante t : tripulantes) {
            mision.asignarTripulante(t);
        }

        // 3. Registrar la mision en la colonia e iniciarla
        colonia.registrarMision(mision);
        mision.iniciar();

        LOG.info("Mision '" + mision.getNombre() + "' iniciada en colonia '" + colonia.getNombre() +
                 "' con " + tripulantes.size() + " tripulantes.");
    }

    /**
     * Cancela una mision activa en una colonia.
     *
     * @param mision la mision a cancelar
     */
    public void cancelarMision(Mision mision) {
        if (mision.getEstado() == EstadoMision.EN_CURSO ||
            mision.getEstado() == EstadoMision.PENDIENTE) {
            mision.cancelar();
            LOG.info("Mision '" + mision.getNombre() + "' cancelada.");
        }
    }

    /**
     * Lista las misiones en un estado especifico dentro de una colonia.
     *
     * @param colonia la colonia a consultar
     * @param estado  el estado deseado
     * @return lista de misiones en ese estado
     */
    public List<Mision> listarMisionesPorEstado(Colonia colonia, EstadoMision estado) {
        return colonia.getMisiones().stream()
                .filter(m -> m.getEstado() == estado)
                .collect(Collectors.toList());
    }

    /**
     * Genera un reporte de misiones activas de la colonia.
     *
     * @param colonia la colonia a reportar
     * @return cadena con el resumen de misiones
     */
    public String reporteMisiones(Colonia colonia) {
        StringBuilder sb = new StringBuilder();
        sb.append("=== Misiones - ").append(colonia.getNombre()).append(" ===\n");
        colonia.getMisiones().forEach(m ->
            sb.append("  ").append(m.toString()).append("\n"));
        return sb.toString();
    }
}
