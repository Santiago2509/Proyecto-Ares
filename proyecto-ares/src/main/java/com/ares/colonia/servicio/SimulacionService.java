package com.ares.colonia.servicio;

import com.ares.colonia.dominio.colonia.Colonia;
import com.ares.colonia.dominio.colonia.RedColonial;
import com.ares.colonia.dominio.modelo.evento.Evento;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.logging.Logger;

/**
 * ============================================================================
 * ARCHIVO: SimulacionService.java
 * PAQUETE: com.ares.colonia.servicio
 * ============================================================================
 *
 * PROPOSITO Y FUNCION PRINCIPAL:
 * Servicio central del motor de simulacion. Coordina el ciclo completo de
 * cada turno (sol): genera eventos aleatorios o deterministicos, invoca
 * RedColonial.avanzarTurnoGlobal(), detecta condiciones de victoria/derrota
 * y registra el historial de la simulacion. Es el orquestador principal
 * del sistema por turnos.
 *
 * ANOTACIONES Y ETIQUETAS UTILIZADAS: Ninguna directa.
 * HERENCIA E INTERFACES IMPLEMENTADAS:
 * - Extiende de: Ninguna
 * - Implementa: Ninguna
 *
 * PARTICIPACION EN EL MOTOR POR TURNOS:
 * Es el punto de entrada del motor. Main invoca ejecutarSimulacion() o
 * avanzarUnSol() para hacer progresar la simulacion.
 * ============================================================================
 */
public class SimulacionService {

    private static final Logger LOG = Logger.getLogger(SimulacionService.class.getName());
    private static final Random RANDOM = new Random();

    /** La red colonial sobre la que opera la simulacion. */
    private final RedColonial red;

    /** Servicio de eventos para generar y aplicar eventos. */
    private final EventoService eventoService;

    /** Servicio de misiones para gestionar el ciclo de misiones. */
    private final MisionService misionService;

    /** Indica si la simulacion esta en curso. */
    private boolean enCurso;

    /** Razon de fin de simulacion (victoria o derrota). */
    private String razonFin;

    /**
     * Construye el servicio de simulacion con sus dependencias.
     *
     * @param red           la red colonial a simular
     * @param eventoService servicio de generacion de eventos
     * @param misionService servicio de gestion de misiones
     */
    public SimulacionService(RedColonial red, EventoService eventoService, MisionService misionService) {
        this.red          = red;
        this.eventoService = eventoService;
        this.misionService = misionService;
        this.enCurso       = false;
        this.razonFin      = null;
    }

    /**
     * Inicia la simulacion. Cambia el estado a "en curso".
     */
    public void iniciar() {
        this.enCurso = true;
        LOG.info("Simulacion iniciada. Red: " + red.getNombre());
    }

    /**
     * Avanza la simulacion un sol completo:
     * 1. Genera eventos del turno (globales y locales).
     * 2. Aplica eventos locales a cada colonia.
     * 3. Invoca RedColonial.avanzarTurnoGlobal() con los eventos globales.
     * 4. Verifica condiciones de fin de juego.
     *
     * @return true si la simulacion continua, false si termino
     */
    public boolean avanzarUnSol() {
        if (!enCurso) {
            LOG.warning("La simulacion no esta en curso. Llama a iniciar() primero.");
            return false;
        }

        // 1. Generar eventos del turno
        List<Evento> eventosGlobales = eventoService.generarEventosGlobales();
        List<Evento> eventosLocales  = eventoService.generarEventosLocales();

        // 2. Aplicar eventos locales a colonias aleatorias
        for (Evento evento : eventosLocales) {
            if (!red.getColonias().isEmpty()) {
                // Seleccionar una colonia aleatoria para el evento local
                Colonia coloniaObjetivo = red.getColonias().get(
                        RANDOM.nextInt(red.getColonias().size()));
                coloniaObjetivo.aplicarEvento(evento);
            }
        }

        // 3. Avanzar la red con los eventos globales
        red.avanzarTurnoGlobal(eventosGlobales);

        // 4. Verificar condiciones de fin
        return !verificarFinDeJuego();
    }

    /**
     * Ejecuta la simulacion por un numero determinado de sols.
     *
     * @param sols numero de sols a simular
     */
    public void ejecutarSimulacion(int sols) {
        iniciar();
        for (int i = 0; i < sols; i++) {
            LOG.info("\n" + red.generarReporteRed());
            boolean continuar = avanzarUnSol();
            if (!continuar) {
                LOG.info("Simulacion finalizada en el Sol " + red.getSolGlobal() + ". Razon: " + razonFin);
                return;
            }
        }
        LOG.info("Simulacion completada tras " + sols + " sols.");
        LOG.info(red.generarReporteRed());
    }

    /**
     * Verifica si la simulacion debe terminar por victoria o derrota.
     * Condicion de derrota: todas las colonias sin tripulantes.
     * Condicion de victoria: gestionada por MisionService al completar Terraformacion.
     *
     * @return true si la simulacion termino
     */
    private boolean verificarFinDeJuego() {
        // Condicion de derrota: ninguna colonia tiene tripulantes
        boolean todasMuertas = red.getColonias().stream()
                .noneMatch(Colonia::estaViva);
        if (todasMuertas) {
            this.enCurso  = false;
            this.razonFin = "DERROTA: Todas las colonias han perdido a su tripulacion.";
            LOG.severe(razonFin);
            return true;
        }
        return false;
    }

    /** @return true si la simulacion esta en curso */
    public boolean isEnCurso() { return enCurso; }

    /** @return razon de fin de la simulacion, o null si aun esta en curso */
    public String getRazonFin() { return razonFin; }

    /** @return la red colonial asociada */
    public RedColonial getRed() { return red; }
}
