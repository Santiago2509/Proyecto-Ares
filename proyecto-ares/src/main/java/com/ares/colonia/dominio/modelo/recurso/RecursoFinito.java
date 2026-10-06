package com.ares.colonia.dominio.modelo.recurso;

/**
 * ============================================================================
 * ARCHIVO: RecursoFinito.java
 * PAQUETE: com.ares.colonia.dominio.modelo.recurso
 * ============================================================================
 *
 * PROPOSITO Y FUNCION PRINCIPAL:
 * Familia de recursos que no se regeneran naturalmente (Repuestos).
 * Solo aumentan mediante suministros externos o misiones de mineria.
 * La colonia debe planificar el reabastecimiento proactivamente.
 *
 * ANOTACIONES Y ETIQUETAS UTILIZADAS: Ninguna.
 * HERENCIA E INTERFACES IMPLEMENTADAS:
 * - Extiende de: Recurso
 * PARTICIPACION EN EL MOTOR POR TURNOS:
 * actualizarPorTurno() solo reinicia el flag de reposicion del turno anterior.
 * ============================================================================
 */
public class RecursoFinito extends Recurso {

    /** Indica si hubo reposicion en el turno actual. */
    private boolean reposicionEnEsteTurno;

    public RecursoFinito(TipoRecurso tipo, double cantidadInicial,
                          double capacidadMaxima, double umbralCritico) {
        super(tipo, cantidadInicial, capacidadMaxima, umbralCritico);
    }

    /**
     * Reinicia el indicador de reposicion. Sin degradacion espontanea.
     */
    @Override
    public void actualizarPorTurno() {
        this.reposicionEnEsteTurno = false;
    }

    /**
     * Registra la llegada de repuestos por suministro o mineria.
     *
     * @param cantidad cantidad recibida
     */
    public void registrarReposicion(double cantidad) {
        agregar(cantidad);
        this.reposicionEnEsteTurno = true;
    }

    /** @return true si hubo reposicion en el turno actual */
    public boolean huboReposicion() { return reposicionEnEsteTurno; }
}
