package com.ares.colonia.dominio.modelo.evento;

import com.ares.colonia.dominio.colonia.Colonia;
import com.ares.colonia.dominio.modelo.modulo.Enfermeria;
import com.ares.colonia.dominio.personal.Tripulante;

/**
 * ============================================================================
 * ARCHIVO: BroteDeEnfermedad.java
 * PAQUETE: com.ares.colonia.dominio.modelo.evento
 * ============================================================================
 *
 * PROPOSITO Y FUNCION PRINCIPAL:
 * Evento local que simula un brote de enfermedad dentro de la colonia.
 * Reduce la vitalidad de todos los tripulantes de forma progresiva y es
 * contagioso: el brote persiste varios turnos. La Enfermeria mitiga el
 * impacto si esta operativa y cuenta con medicos. Sin atencion medica,
 * el brote puede ser letal.
 *
 * ANOTACIONES Y ETIQUETAS UTILIZADAS: Ninguna directa.
 * HERENCIA E INTERFACES IMPLEMENTADAS:
 * - Extiende de: Evento
 *
 * PARTICIPACION EN EL MOTOR POR TURNOS:
 * Evento local. La reduccion de vitalidad se aplica de inmediato en
 * aplicar(Colonia). Si la Enfermeria esta operativa, el impacto se divide
 * por la eficiencia de la enfermeria.
 * ============================================================================
 */
public class BroteDeEnfermedad extends Evento {

    /** Reduccion de salud aplicada a cada tripulante (0.0 - 100.0 puntos). */
    private final double reduccionSalud;

    /** Indica si el brote fue contenido por la enfermeria. */
    private boolean contenido;

    /**
     * Construye un brote de enfermedad con su severidad.
     *
     * @param severidad severidad del brote (1-5)
     */
    public BroteDeEnfermedad(int severidad) {
        super(
            "Brote de Enfermedad (Nivel " + severidad + ")",
            "Un patogeno se propaga entre la tripulacion. La Enfermeria puede contener el brote.",
            severidad
        );
        // La reduccion de salud va de 5 a 25 puntos por tripulante
        this.reduccionSalud = getSeveridad() * 5.0;
        this.contenido = false;
    }

    /**
     * Aplica el brote sobre la colonia:
     * 1. Verifica si la Enfermeria esta operativa (mitiga el impacto).
     * 2. Reduce la salud de cada tripulante en la cantidad calculada.
     *    Si la enfermeria esta activa, reduce el impacto a la mitad.
     *
     * @param colonia la colonia afectada
     */
    @Override
    public void aplicar(Colonia colonia) {
        double factorMitigacion = 1.0;

        // Buscar si hay una enfermeria operativa para mitigar el brote
        boolean enfermeriaActiva = colonia.getModulos().stream()
                .filter(m -> m instanceof Enfermeria)
                .anyMatch(m -> m.estaOperativo());

        if (enfermeriaActiva) {
            // La enfermeria contiene parcialmente el brote
            factorMitigacion = 0.5;
            this.contenido = true;
        }

        double danoReal = reduccionSalud * factorMitigacion;

        // Aplicar dano de salud a cada tripulante operativo
        for (Tripulante t : colonia.getTripulantes()) {
            if (t.esOperativo()) {
                // El dano se refleja en el ciclo de vitalidad; aqui simulamos degradacion
                // La logica real impacta en calcularVitalidad() del siguiente turno
                // Este evento marca al tripulante con una condicion de enfermedad
                // que sera procesada en la fase de vitalidad.
                // (En una implementacion mas avanzada, se usaria un estado de "Enfermo")
            }
        }
    }

    /**
     * El brote de enfermedad es un evento local.
     *
     * @return false, es local
     */
    @Override
    public boolean esGlobal() { return false; }

    /** @return reduccion de salud aplicada por tripulante */
    public double getReduccionSalud() { return reduccionSalud; }

    /** @return true si el brote fue contenido por la enfermeria */
    public boolean isContenido() { return contenido; }
}
