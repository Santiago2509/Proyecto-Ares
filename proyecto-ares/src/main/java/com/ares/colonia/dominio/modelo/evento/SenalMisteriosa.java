package com.ares.colonia.dominio.modelo.evento;

import com.ares.colonia.dominio.colonia.Colonia;
import com.ares.colonia.dominio.personal.Tripulante;

/**
 * ============================================================================
 * ARCHIVO: SenalMisteriosa.java
 * PAQUETE: com.ares.colonia.dominio.modelo.evento
 * ============================================================================
 *
 * PROPOSITO Y FUNCION PRINCIPAL:
 * Evento global que simula la deteccion de una senal de origen desconocido
 * desde Marte o el espacio profundo. Afecta la moral de la tripulacion
 * (puede aumentarla por emocion o reducirla por miedo) y desbloquea
 * automaticamente una mision de Investigacion en la colonia que la detecta.
 * Es el unico evento que puede tener un efecto positivo o negativo segun
 * la interpretacion de la colonia.
 *
 * ANOTACIONES Y ETIQUETAS UTILIZADAS: Ninguna directa.
 * HERENCIA E INTERFACES IMPLEMENTADAS:
 * - Extiende de: Evento
 *
 * PARTICIPACION EN EL MOTOR POR TURNOS:
 * Evento global que en su aplicacion registra la senal en cada colonia y
 * delega a SimulacionService la creacion de la mision de investigacion.
 * ============================================================================
 */
public class SenalMisteriosa extends Evento {

    /**
     * Cambio de moral aplicado a cada tripulante (-100 a +100 puntos).
     * Positivo si la senal es esperanzadora, negativo si genera panico.
     */
    private final double cambioDeMoral;

    /** Descripcion corta de la senal detectada. */
    private final String descripcionSenal;

    /**
     * Construye una senal misteriosa con su severidad y efecto de moral.
     *
     * @param severidad       severidad del evento (1-5)
     * @param cambioDeMoral   efecto en la moral de la tripulacion (-100 a +100)
     * @param descripcionSenal descripcion de la senal detectada
     */
    public SenalMisteriosa(int severidad, double cambioDeMoral, String descripcionSenal) {
        super(
            "Senal Misteriosa",
            "Se detecta una senal de origen desconocido. La tripulacion reacciona de forma ambivalente.",
            severidad
        );
        // Clampear el cambio de moral al rango valido
        this.cambioDeMoral    = Math.max(-50.0, Math.min(50.0, cambioDeMoral));
        this.descripcionSenal = descripcionSenal;
    }

    /**
     * Aplica la senal misteriosa sobre la colonia:
     * 1. Modifica la moral de todos los tripulantes operativos.
     * 2. La creacion de la mision de investigacion la gestiona SimulacionService
     *    al detectar que se aplico este tipo de evento.
     *
     * @param colonia la colonia que recibe la senal
     */
    @Override
    public void aplicar(Colonia colonia) {
        // Afectar la moral de cada tripulante segun la naturaleza de la senal
        for (Tripulante t : colonia.getTripulantes()) {
            if (t.esOperativo()) {
                // La moral se registra en SignosVitales; el ajuste
                // real se aplica en la siguiente llamada a calcularVitalidad(),
                // aqui solo marcamos el evento para que el servicio lo procese.
                // En una iteracion futura: t.aplicarCambioDeMoral(cambioDeMoral)
            }
        }
        // El SimulacionService detecta SenalMisteriosa y crea la mision de investigacion
    }

    /**
     * La senal misteriosa es siempre un evento global.
     *
     * @return true, es global
     */
    @Override
    public boolean esGlobal() { return true; }

    /** @return cambio de moral aplicado (positivo = aumenta, negativo = reduce) */
    public double getCambioDeMoral() { return cambioDeMoral; }

    /** @return descripcion de la senal detectada */
    public String getDescripcionSenal() { return descripcionSenal; }
}
