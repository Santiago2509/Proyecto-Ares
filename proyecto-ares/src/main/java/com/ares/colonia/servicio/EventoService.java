package com.ares.colonia.servicio;

import com.ares.colonia.dominio.modelo.evento.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.logging.Logger;

/**
 * ============================================================================
 * ARCHIVO: EventoService.java
 * PAQUETE: com.ares.colonia.servicio
 * ============================================================================
 *
 * PROPOSITO Y FUNCION PRINCIPAL:
 * Servicio responsable de generar los eventos de cada turno de la simulacion.
 * Encapsula la logica de probabilidad de aparicion de eventos (tormentas,
 * fallos, suministros, etc.) y los clasifica en globales y locales para
 * que SimulacionService los aplique de forma correcta.
 *
 * ANOTACIONES Y ETIQUETAS UTILIZADAS: Ninguna directa.
 * HERENCIA E INTERFACES IMPLEMENTADAS:
 * - Extiende de: Ninguna
 * - Implementa: Ninguna
 *
 * PARTICIPACION EN EL MOTOR POR TURNOS:
 * Invocado en el primer paso del turno por SimulacionService para obtener
 * la lista de eventos globales y locales del sol actual.
 * ============================================================================
 */
public class EventoService {

    private static final Logger LOG = Logger.getLogger(EventoService.class.getName());
    private static final Random RANDOM = new Random();

    // Probabilidades base de eventos por turno (0.0 - 1.0)
    private static final double PROB_TORMENTA      = 0.20; // 20% por turno
    private static final double PROB_FALLA          = 0.10; // 10% por turno
    private static final double PROB_METEORITO      = 0.05; // 5% por turno
    private static final double PROB_BROTE          = 0.07; // 7% por turno
    private static final double PROB_SUMINISTROS    = 0.15; // 15% por turno
    private static final double PROB_SENAL          = 0.03; // 3% por turno

    /**
     * Genera la lista de eventos globales para el turno actual.
     * Los eventos globales se aplican sobre todas las colonias.
     *
     * @return lista de eventos globales (puede estar vacia)
     */
    public List<Evento> generarEventosGlobales() {
        List<Evento> eventos = new ArrayList<>();

        // Tormenta de arena (global)
        if (RANDOM.nextDouble() < PROB_TORMENTA) {
            int severidad = 1 + RANDOM.nextInt(5);
            eventos.add(new TormentaDeArena(severidad));
            LOG.info("Evento global generado: Tormenta de Arena (Severidad " + severidad + ")");
        }

        // Senal misteriosa (global)
        if (RANDOM.nextDouble() < PROB_SENAL) {
            double cambioDeMoral = RANDOM.nextBoolean() ? 15.0 : -10.0;
            eventos.add(new SenalMisteriosa(2, cambioDeMoral, "Senal repetitiva de origen desconocido."));
            LOG.info("Evento global generado: Senal Misteriosa");
        }

        // Llegada de suministros global
        if (RANDOM.nextDouble() < PROB_SUMINISTROS * 0.3) { // menos probable que sea global
            eventos.add(new LlegadaDeSuministros(50.0, 100.0, 200.0, true));
            LOG.info("Evento global generado: Llegada de Suministros (global)");
        }

        return eventos;
    }

    /**
     * Genera la lista de eventos locales para el turno actual.
     * Los eventos locales se aplican solo a una colonia aleatoria.
     *
     * @return lista de eventos locales (puede estar vacia)
     */
    public List<Evento> generarEventosLocales() {
        List<Evento> eventos = new ArrayList<>();

        // Falla electrica (local)
        if (RANDOM.nextDouble() < PROB_FALLA) {
            int severidad = 1 + RANDOM.nextInt(3); // fallas de severidad 1-3
            eventos.add(new FallaElectrica(severidad));
            LOG.warning("Evento local generado: Falla Electrica (Severidad " + severidad + ")");
        }

        // Impacto de meteorito (local)
        if (RANDOM.nextDouble() < PROB_METEORITO) {
            int severidad = 1 + RANDOM.nextInt(4);
            eventos.add(new Meteorito(severidad));
            LOG.warning("Evento local generado: Meteorito (Severidad " + severidad + ")");
        }

        // Brote de enfermedad (local)
        if (RANDOM.nextDouble() < PROB_BROTE) {
            int severidad = 1 + RANDOM.nextInt(3);
            eventos.add(new BroteDeEnfermedad(severidad));
            LOG.warning("Evento local generado: Brote de Enfermedad (Severidad " + severidad + ")");
        }

        // Llegada de suministros local
        if (RANDOM.nextDouble() < PROB_SUMINISTROS) {
            double repuestos = 20 + RANDOM.nextInt(60);
            double comida    = 50 + RANDOM.nextInt(100);
            double agua      = 100 + RANDOM.nextInt(200);
            eventos.add(new LlegadaDeSuministros(repuestos, comida, agua, false));
            LOG.info("Evento local generado: Llegada de Suministros (local)");
        }

        return eventos;
    }
}
