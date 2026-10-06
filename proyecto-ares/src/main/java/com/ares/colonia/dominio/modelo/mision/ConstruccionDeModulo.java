package com.ares.colonia.dominio.modelo.mision;

import com.ares.colonia.dominio.colonia.Colonia;
import com.ares.colonia.dominio.excepciones.EquipoInsuficienteException;
import com.ares.colonia.dominio.excepciones.MisionImposibleException;
import com.ares.colonia.dominio.excepciones.RecursoAgotadoException;
import com.ares.colonia.dominio.modelo.modulo.Modulo;
import com.ares.colonia.dominio.modelo.recurso.TipoRecurso;
import com.ares.colonia.dominio.personal.Ingeniero;

/**
 * ============================================================================
 * ARCHIVO: ConstruccionDeModulo.java
 * PAQUETE: com.ares.colonia.dominio.modelo.mision
 * ============================================================================
 *
 * PROPOSITO Y FUNCION PRINCIPAL:
 * Mision que permite a la colonia construir un nuevo modulo o mejorar uno
 * existente. Requiere Ingenieros y una cantidad considerable de repuestos.
 * Al completarse, el nuevo modulo queda operativo en la colonia, expandiendo
 * su capacidad de produccion y supervivencia.
 *
 * ANOTACIONES Y ETIQUETAS UTILIZADAS: Ninguna directa.
 * HERENCIA E INTERFACES IMPLEMENTADAS:
 * - Extiende de: Mision
 *
 * PARTICIPACION EN EL MOTOR POR TURNOS:
 * Durante la ejecucion, los repuestos se consumen gradualmente. Al completarse,
 * ejecutar() registra el nuevo modulo en el inventario de la colonia.
 * ============================================================================
 */
public class ConstruccionDeModulo extends Mision {

    /** El modulo que sera construido y agregado a la colonia al completarse. */
    private final Modulo moduloAConstruir;

    /** Repuestos necesarios para completar la construccion. */
    private final double repuestosRequeridos;

    /** Numero minimo de ingenieros necesarios para la construccion. */
    private final int ingenierosRequeridos;

    /**
     * Construye una mision de construccion de modulo.
     *
     * @param nombre               nombre de la mision
     * @param moduloAConstruir     el modulo que se construira
     * @param duracionSols         duracion en sols de la construccion
     * @param repuestosRequeridos  repuestos necesarios
     * @param ingenierosRequeridos ingenieros minimos necesarios
     */
    public ConstruccionDeModulo(String nombre, Modulo moduloAConstruir,
                                 int duracionSols, double repuestosRequeridos,
                                 int ingenierosRequeridos) {
        super(
            nombre,
            "Construccion de " + moduloAConstruir.getNombre() + ". Requiere " +
            repuestosRequeridos + " repuestos y " + ingenierosRequeridos + " ingenieros.",
            duracionSols,
            0.10 // bajo riesgo: construccion dentro de la colonia
        );
        this.moduloAConstruir     = moduloAConstruir;
        this.repuestosRequeridos  = repuestosRequeridos;
        this.ingenierosRequeridos = ingenierosRequeridos;
    }

    /**
     * Valida los requisitos de la mision de construccion:
     * - Suficientes repuestos en el inventario.
     * - Al menos el numero requerido de Ingenieros operativos.
     *
     * @param colonia la colonia constructora
     * @throws RecursoAgotadoException     si no hay suficientes repuestos
     * @throws EquipoInsuficienteException si no hay suficientes ingenieros
     * @throws MisionImposibleException    si las condiciones generales lo impiden
     */
    @Override
    public void validarRequisitos(Colonia colonia) {
        // Verificar repuestos suficientes en el inventario
        double repuestosActuales = colonia.getInventario().getCantidad(TipoRecurso.REPUESTOS);
        if (repuestosActuales < repuestosRequeridos)
            throw new RecursoAgotadoException(TipoRecurso.REPUESTOS.getNombre());

        // Contar ingenieros operativos
        long ingenierosDisponibles = colonia.getTripulantes().stream()
                .filter(t -> t instanceof Ingeniero && t.esOperativo())
                .count();
        if (ingenierosDisponibles < ingenierosRequeridos)
            throw new EquipoInsuficienteException(getNombre(),
                    "Se necesitan " + ingenierosRequeridos + " ingenieros. Disponibles: " + ingenierosDisponibles);
    }

    /**
     * Ejecuta la finalizacion de la construccion:
     * 1. Consume los repuestos del inventario de la colonia.
     * 2. Registra el modulo nuevo como parte de la colonia.
     *
     * @return descripcion del modulo construido
     */
    @Override
    public String ejecutar() {
        // Aqui SimulacionService debe agregar moduloAConstruir a la colonia.
        // La logica de consumo de repuestos y el registro del modulo
        // se delegan al servicio para mantener el dominio puro.
        return String.format(
            "Mision '%s' completada. Modulo '%s' construido y listo para operar.",
            getNombre(), moduloAConstruir.getNombre());
    }

    /** @return el modulo que se construyo */
    public Modulo getModuloAConstruir() { return moduloAConstruir; }

    /** @return repuestos requeridos para la construccion */
    public double getRepuestosRequeridos() { return repuestosRequeridos; }

    /** @return numero minimo de ingenieros requeridos */
    public int getIngenieros_requeridos() { return ingenierosRequeridos; }
}
