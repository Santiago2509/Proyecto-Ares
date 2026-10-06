package com.ares.colonia.dominio.modelo.recurso;

/**
 * ============================================================================
 * ARCHIVO: RecursoRenovable.java
 * PAQUETE: com.ares.colonia.dominio.modelo.recurso
 * ============================================================================
 *
 * PROPOSITO Y FUNCION PRINCIPAL:
 * Familia de recursos que se regeneran cada turno gracias a los modulos
 * productores (Energia, Oxigeno, Agua). actualizarPorTurno() no aplica
 * degradacion; la regeneracion la gestiona el Inventario al acreditar
 * la produccion de los modulos Productor.
 *
 * ANOTACIONES Y ETIQUETAS UTILIZADAS: Ninguna.
 * HERENCIA E INTERFACES IMPLEMENTADAS:
 * - Extiende de: Recurso
 * PARTICIPACION EN EL MOTOR POR TURNOS:
 * Actualizado en la fase de produccion/consumo (paso 2).
 * ============================================================================
 */
public class RecursoRenovable extends Recurso {

    public RecursoRenovable(TipoRecurso tipo, double cantidadInicial,
                             double capacidadMaxima, double umbralCritico) {
        super(tipo, cantidadInicial, capacidadMaxima, umbralCritico);
    }

    /**
     * Los recursos renovables no degradan espontaneamente.
     * La regeneracion la aplican los modulos Productor via Inventario.
     */
    @Override
    public void actualizarPorTurno() { /* sin degradacion */ }
}
