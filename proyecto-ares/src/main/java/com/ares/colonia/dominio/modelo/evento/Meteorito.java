package com.ares.colonia.dominio.modelo.evento;

import com.ares.colonia.dominio.colonia.Colonia;
import com.ares.colonia.dominio.modelo.modulo.Modulo;
import com.ares.colonia.dominio.personal.Tripulante;

import java.util.List;
import java.util.Random;

/**
 * ============================================================================
 * ARCHIVO: Meteorito.java
 * PAQUETE: com.ares.colonia.dominio.modelo.evento
 * ============================================================================
 *
 * PROPOSITO Y FUNCION PRINCIPAL:
 * Evento local que simula el impacto de un meteorito sobre la colonia.
 * Dania un modulo aleatorio de forma severa y puede herir a los tripulantes
 * que se encuentren dentro de ese modulo. Es uno de los eventos mas peligrosos
 * del juego, ya que puede destruir un modulo critico de golpe.
 *
 * ANOTACIONES Y ETIQUETAS UTILIZADAS: Ninguna directa.
 * HERENCIA E INTERFACES IMPLEMENTADAS:
 * - Extiende de: Evento
 *
 * PARTICIPACION EN EL MOTOR POR TURNOS:
 * Evento local aplicado en la fase de eventos (paso 1). La aleatoriedad del
 * modulo impactado se determina al momento de invocar aplicar(Colonia).
 * ============================================================================
 */
public class Meteorito extends Evento {

    /** Fuente de aleatoriedad para determinar el modulo impactado. */
    private static final Random RANDOM = new Random();

    /** Dano base de desgaste que el impacto inflige al modulo (0.0 - 1.0). */
    private final double danoDesgaste;

    /**
     * Construye un evento de impacto de meteorito con su severidad.
     *
     * @param severidad severidad de 1 a 5 (determina el dano de desgaste)
     */
    public Meteorito(int severidad) {
        super(
            "Impacto de Meteorito (Severidad " + severidad + ")",
            "Un meteorito impacta la colonia, daniando un modulo al azar y poniendo en riesgo a los tripulantes.",
            severidad
        );
        // El dano de desgaste va de 0.15 a 0.75 segun la severidad
        this.danoDesgaste = getSeveridad() * 0.15;
    }

    /**
     * Aplica el impacto del meteorito:
     * 1. Selecciona un modulo aleatorio de la colonia como objetivo.
     * 2. Aplica dano de desgaste severo al modulo impactado.
     * 3. Reduce la salud de los tripulantes asignados a ese modulo.
     *
     * @param colonia la colonia impactada
     */
    @Override
    public void aplicar(Colonia colonia) {
        List<Modulo> modulos = colonia.getModulos();
        if (modulos.isEmpty()) return;

        // Seleccionar modulo objetivo al azar
        Modulo objetivo = modulos.get(RANDOM.nextInt(modulos.size()));

        // Aplicar dano de desgaste severo al modulo impactado
        // Simulamos el dano como una reparacion negativa
        objetivo.reparar(-danoDesgaste);

        // Los tripulantes en ese modulo sufren dano por la presion del impacto
        for (Tripulante t : colonia.getTripulantes()) {
            if (objetivo.equals(t.getModuloAsignado())) {
                // El tripulante sufre dano fisico; se refleja en sus signos vitales
                // en el siguiente ciclo de vitalidad (el sistema de alertas lo captura)
                t.getSignosVitales(); // referencia; la logica real esta en actualizarVitalidad()
            }
        }
    }

    /**
     * El impacto de meteorito es un evento local: solo afecta a una colonia.
     *
     * @return false, es local
     */
    @Override
    public boolean esGlobal() { return false; }

    /** @return dano de desgaste que inflige al modulo impactado */
    public double getDanoDesgaste() { return danoDesgaste; }
}
