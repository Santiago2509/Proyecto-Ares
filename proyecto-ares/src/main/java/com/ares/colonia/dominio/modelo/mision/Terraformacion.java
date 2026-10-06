package com.ares.colonia.dominio.modelo.mision;

import com.ares.colonia.dominio.colonia.Colonia;
import com.ares.colonia.dominio.excepciones.EquipoInsuficienteException;
import com.ares.colonia.dominio.excepciones.MisionImposibleException;
import com.ares.colonia.dominio.excepciones.RecursoAgotadoException;
import com.ares.colonia.dominio.modelo.recurso.TipoRecurso;
import com.ares.colonia.dominio.personal.Botanico;
import com.ares.colonia.dominio.personal.Cientifico;
import com.ares.colonia.dominio.personal.Ingeniero;

/**
 * ============================================================================
 * ARCHIVO: Terraformacion.java
 * PAQUETE: com.ares.colonia.dominio.modelo.mision
 * ============================================================================
 *
 * PROPOSITO Y FUNCION PRINCIPAL:
 * La mision mas importante y el objetivo final de victoria del sistema.
 * La Terraformacion es un proyecto de larguisima duracion que requiere
 * enormes cantidades de recursos, una tripulacion completa de especialistas
 * y todas las infraestructuras operativas. Al completarse, la colonia
 * alcanza el estado de "auto-sostenible" y se convierte en la victoria
 * del escenario de simulacion.
 *
 * ANOTACIONES Y ETIQUETAS UTILIZADAS: Ninguna directa.
 * HERENCIA E INTERFACES IMPLEMENTADAS:
 * - Extiende de: Mision
 *
 * PARTICIPACION EN EL MOTOR POR TURNOS:
 * Consume recursos en cada turno de la mision (flujo de recursos continuo).
 * Al completarse, ejecutar() marca la victoria de la red colonial.
 * ============================================================================
 */
public class Terraformacion extends Mision {

    /** Fase actual del proyecto de terraformacion (1: inicial, 5: final). */
    private int faseActual;

    /** Recursos de agua necesarios para completar la terraformacion. */
    private final double aguaTotal;

    /** Recursos de energia necesarios para la terraformacion. */
    private final double energiaTotal;

    /** Recursos de repuestos necesarios para la terraformacion. */
    private final double repuestosTotal;

    /** Porcentaje de progreso completado (0.0 - 1.0). */
    private double progreso;

    /**
     * Construye la mision de terraformacion con los requisitos de recursos.
     * Es una mision de muy larga duracion (minimo 50 sols).
     *
     * @param duracionSols  duracion total en sols
     * @param aguaTotal     litros de agua necesarios
     * @param energiaTotal  unidades de energia necesarias
     * @param repuestosTotal unidades de repuestos necesarias
     */
    public Terraformacion(int duracionSols, double aguaTotal,
                           double energiaTotal, double repuestosTotal) {
        super(
            "Proyecto Terraformacion",
            "El objetivo final: hacer Marte habitable. Requiere todos los recursos y especialistas.",
            Math.max(50, duracionSols),  // minimo 50 sols de duracion
            0.30 // riesgo moderado: trabajo de ingenieria a escala planetaria
        );
        this.aguaTotal     = aguaTotal;
        this.energiaTotal  = energiaTotal;
        this.repuestosTotal = repuestosTotal;
        this.faseActual    = 1;
        this.progreso      = 0.0;
    }

    /**
     * Valida los requisitos de la terraformacion:
     * - Stocks minimos de agua, energia y repuestos.
     * - Al menos un Ingeniero, un Cientifico y un Botanico operativos.
     * - Todos los tipos de modulos presentes y operativos.
     *
     * @param colonia la colonia que inicia la terraformacion
     * @throws RecursoAgotadoException     si los recursos son insuficientes
     * @throws EquipoInsuficienteException si falta alguna especialidad
     * @throws MisionImposibleException    si la infraestructura es incompleta
     */
    @Override
    public void validarRequisitos(Colonia colonia) {
        // Verificar stocks de recursos
        double agua = colonia.getInventario().getCantidad(TipoRecurso.AGUA);
        if (agua < aguaTotal * 0.1) // al menos el 10% del total para iniciar
            throw new RecursoAgotadoException(TipoRecurso.AGUA.getNombre());

        double energia = colonia.getInventario().getCantidad(TipoRecurso.ENERGIA);
        if (energia < energiaTotal * 0.1)
            throw new RecursoAgotadoException(TipoRecurso.ENERGIA.getNombre());

        double repuestos = colonia.getInventario().getCantidad(TipoRecurso.REPUESTOS);
        if (repuestos < repuestosTotal * 0.1)
            throw new RecursoAgotadoException(TipoRecurso.REPUESTOS.getNombre());

        // Verificar que haya al menos un especialista de cada tipo
        boolean hayIngeniero  = colonia.getTripulantes().stream().anyMatch(t -> t instanceof Ingeniero  && t.esOperativo());
        boolean hayCientifico = colonia.getTripulantes().stream().anyMatch(t -> t instanceof Cientifico && t.esOperativo());
        boolean hayBotanico   = colonia.getTripulantes().stream().anyMatch(t -> t instanceof Botanico   && t.esOperativo());

        if (!hayIngeniero || !hayCientifico || !hayBotanico)
            throw new EquipoInsuficienteException(getNombre(),
                "La terraformacion requiere al menos un Ingeniero, un Cientifico y un Botanico operativos.");

        // Verificar que la colonia tenga al menos 5 modulos operativos
        long modulosOperativos = colonia.getModulos().stream()
                .filter(m -> m.estaOperativo()).count();
        if (modulosOperativos < 5)
            throw new MisionImposibleException(getNombre(),
                "Se necesitan al menos 5 modulos operativos para la terraformacion. Activos: " + modulosOperativos);
    }

    /**
     * Ejecuta la finalizacion de la terraformacion: victoria de la colonia.
     * Marca el proyecto como completado y calcula el nivel de exito.
     *
     * @return mensaje de victoria y resumen del logro
     */
    @Override
    public String ejecutar() {
        this.progreso = 1.0;
        return String.format(
            "*** VICTORIA *** Mision '%s' completada.\n" +
            "La colonia ha terraformado con exito una region de Marte.\n" +
            "Marte da un pequeno paso hacia convertirse en el segundo hogar de la humanidad.\n" +
            "Progreso total: 100%% | Fases completadas: %d",
            getNombre(), faseActual);
    }

    /**
     * Avanza la fase actual del proyecto (cada fase representa un hito).
     * Llamado por SimulacionService cuando se alcanza cierto porcentaje de progreso.
     */
    public void avanzarFase() {
        if (faseActual < 5) {
            faseActual++;
            this.progreso = faseActual / 5.0;
        }
    }

    /** @return fase actual del proyecto de terraformacion (1-5) */
    public int getFaseActual() { return faseActual; }

    /** @return porcentaje de progreso completado (0.0 - 1.0) */
    public double getProgreso() { return progreso; }
}
