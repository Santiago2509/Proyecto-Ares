package com.ares.colonia.dominio.modelo.mision;

import com.ares.colonia.dominio.colonia.Colonia;
import com.ares.colonia.dominio.excepciones.EquipoInsuficienteException;
import com.ares.colonia.dominio.excepciones.MisionImposibleException;
import com.ares.colonia.dominio.modelo.modulo.Laboratorio;
import com.ares.colonia.dominio.personal.Cientifico;

/**
 * ============================================================================
 * ARCHIVO: Investigacion.java
 * PAQUETE: com.ares.colonia.dominio.modelo.mision
 * ============================================================================
 *
 * PROPOSITO Y FUNCION PRINCIPAL:
 * Mision que utiliza el Laboratorio y los Cientificos de la colonia para
 * investigar una linea de conocimiento. Al completarse, desbloquea una
 * tecnologia que mejora la eficiencia de uno o mas modulos de la colonia.
 * Es la mision de mayor impacto a largo plazo y la unica que puede
 * mejorar los parametros base del sistema.
 *
 * ANOTACIONES Y ETIQUETAS UTILIZADAS: Ninguna directa.
 * HERENCIA E INTERFACES IMPLEMENTADAS:
 * - Extiende de: Mision
 *
 * PARTICIPACION EN EL MOTOR POR TURNOS:
 * En cada turno de la mision, los Cientificos generan puntos de investigacion.
 * Al completarse, ejecutar() aplica el bonus de la tecnologia desbloqueada.
 * ============================================================================
 */
public class Investigacion extends Mision {

    /** Nombre de la tecnologia que se investiga. */
    private final String tecnologiaObjetivo;

    /** Puntos de investigacion generados en total (aumenta con cientificos asignados). */
    private double puntosGenerados;

    /** Numero minimo de cientificos para iniciar la investigacion. */
    private final int cientificosRequeridos;

    /**
     * Construye una mision de investigacion.
     *
     * @param nombre               nombre de la mision
     * @param tecnologiaObjetivo   nombre de la tecnologia a investigar
     * @param duracionSols         duracion en sols
     * @param cientificosRequeridos minimo de cientificos requeridos
     */
    public Investigacion(String nombre, String tecnologiaObjetivo,
                          int duracionSols, int cientificosRequeridos) {
        super(
            nombre,
            "Investigacion de: " + tecnologiaObjetivo + ". Requiere " + cientificosRequeridos + " cientificos.",
            duracionSols,
            0.05 // casi sin riesgo: es trabajo en laboratorio
        );
        this.tecnologiaObjetivo   = tecnologiaObjetivo;
        this.cientificosRequeridos = cientificosRequeridos;
        this.puntosGenerados       = 0.0;
    }

    /**
     * Valida los requisitos de la investigacion:
     * - Al menos un Laboratorio operativo.
     * - Al menos el numero requerido de Cientificos operativos.
     *
     * @param colonia la colonia investigadora
     * @throws MisionImposibleException    si no hay Laboratorio operativo
     * @throws EquipoInsuficienteException si no hay suficientes Cientificos
     */
    @Override
    public void validarRequisitos(Colonia colonia) {
        // Debe haber un Laboratorio operativo
        boolean labActivo = colonia.getModulos().stream()
                .filter(m -> m instanceof Laboratorio)
                .anyMatch(m -> m.estaOperativo());
        if (!labActivo)
            throw new MisionImposibleException(getNombre(), "No hay Laboratorio operativo.");

        // Contar cientificos operativos
        long cientificosDisponibles = colonia.getTripulantes().stream()
                .filter(t -> t instanceof Cientifico && t.esOperativo())
                .count();
        if (cientificosDisponibles < cientificosRequeridos)
            throw new EquipoInsuficienteException(getNombre(),
                "Se necesitan " + cientificosRequeridos + " cientificos. Disponibles: " + cientificosDisponibles);
    }

    /**
     * Ejecuta la finalizacion de la investigacion.
     * El resultado desbloquea la tecnologia objetivo en la colonia.
     *
     * @return descripcion de la tecnologia desbloqueada y sus efectos
     */
    @Override
    public String ejecutar() {
        // Calcular la potencia de la investigacion segun puntos generados
        double potencia = Math.min(1.0, puntosGenerados / 100.0);
        return String.format(
            "Mision '%s' completada. Tecnologia '%s' desbloqueada con %.0f%% de efectividad.",
            getNombre(), tecnologiaObjetivo, potencia * 100);
    }

    /**
     * Acumula puntos de investigacion generados por los cientificos asignados.
     *
     * @param puntos puntos a sumar en este turno
     */
    public void acumularPuntos(double puntos) {
        this.puntosGenerados += Math.max(0, puntos);
    }

    /** @return nombre de la tecnologia objetivo */
    public String getTecnologiaObjetivo() { return tecnologiaObjetivo; }

    /** @return puntos de investigacion acumulados hasta ahora */
    public double getPuntosGenerados() { return puntosGenerados; }

    /** @return numero minimo de cientificos requeridos */
    public int getCientificosRequeridos() { return cientificosRequeridos; }
}
