package com.ares.colonia.dominio.modelo.evento;

import com.ares.colonia.dominio.colonia.Colonia;
import com.ares.colonia.dominio.modelo.modulo.CentroDeComunicaciones;
import com.ares.colonia.dominio.modelo.modulo.Hangar;
import com.ares.colonia.dominio.modelo.recurso.TipoRecurso;

/**
 * ============================================================================
 * ARCHIVO: LlegadaDeSuministros.java
 * PAQUETE: com.ares.colonia.dominio.modelo.evento
 * ============================================================================
 *
 * PROPOSITO Y FUNCION PRINCIPAL:
 * Evento positivo que simula la llegada de una nave de suministros desde la
 * Tierra. Agrega recursos vitales al inventario de la colonia, pero requiere
 * que el Hangar y el Centro de Comunicaciones esten operativos para poder
 * aterrizar y descargar los suministros. Puede ser local (una colonia) o
 * global (todas las colonias de la red).
 *
 * ANOTACIONES Y ETIQUETAS UTILIZADAS: Ninguna directa.
 * HERENCIA E INTERFACES IMPLEMENTADAS:
 * - Extiende de: Evento
 *
 * PARTICIPACION EN EL MOTOR POR TURNOS:
 * Aplica su efecto en la fase de eventos (paso 1). Si las infraestructuras
 * requeridas no estan operativas, los suministros no pueden descargarse
 * y el evento se pierde.
 * ============================================================================
 */
public class LlegadaDeSuministros extends Evento {

    /** Cantidad de repuestos que trae la nave. */
    private final double repuestosEntregados;

    /** Cantidad de comida que trae la nave. */
    private final double comidaEntregada;

    /** Cantidad de agua que trae la nave. */
    private final double aguaEntregada;

    /** Indica si el evento afecta a toda la red o solo a una colonia. */
    private final boolean global;

    /** Indica si los suministros fueron descargados exitosamente. */
    private boolean descargado;

    /**
     * Construye un evento de llegada de suministros.
     *
     * @param repuestosEntregados unidades de repuestos que llegan
     * @param comidaEntregada     kg de comida que llegan
     * @param aguaEntregada       litros de agua que llegan
     * @param global              true si afecta a toda la red colonial
     */
    public LlegadaDeSuministros(double repuestosEntregados,
                                 double comidaEntregada,
                                 double aguaEntregada,
                                 boolean global) {
        super(
            "Llegada de Suministros",
            "Una nave de suministros llega desde la Tierra con materiales vitales.",
            1  // los suministros no tienen severidad negativa
        );
        this.repuestosEntregados = repuestosEntregados;
        this.comidaEntregada     = comidaEntregada;
        this.aguaEntregada       = aguaEntregada;
        this.global              = global;
        this.descargado          = false;
    }

    /**
     * Aplica la llegada de suministros a la colonia.
     * Primero verifica que el Hangar y Centro de Comunicaciones esten activos.
     * Si no, los suministros no pueden ser descargados.
     *
     * @param colonia la colonia que recibe (o pierde) los suministros
     */
    @Override
    public void aplicar(Colonia colonia) {
        // Verificar que el Hangar este operativo para recibir la nave
        boolean hangarOperativo = colonia.getModulos().stream()
                .filter(m -> m instanceof Hangar)
                .anyMatch(m -> m.estaOperativo());

        // Verificar que el Centro de Comunicaciones este operativo para guiar la nave
        boolean comunicacionesActivas = colonia.getModulos().stream()
                .filter(m -> m instanceof CentroDeComunicaciones)
                .anyMatch(m -> m.estaOperativo());

        if (!hangarOperativo || !comunicacionesActivas) {
            // Los suministros no pueden descargarse: se pierden
            this.descargado = false;
            return;
        }

        // Descargar suministros al inventario de la colonia
        colonia.getInventario().agregar(TipoRecurso.REPUESTOS, repuestosEntregados);
        colonia.getInventario().agregar(TipoRecurso.COMIDA,    comidaEntregada);
        colonia.getInventario().agregar(TipoRecurso.AGUA,      aguaEntregada);
        this.descargado = true;
    }

    /**
     * Puede ser global (para toda la red) o local (para una colonia).
     *
     * @return true si afecta a toda la red colonial
     */
    @Override
    public boolean esGlobal() { return global; }

    /** @return true si los suministros fueron descargados exitosamente */
    public boolean isDescargado() { return descargado; }

    /** @return repuestos entregados */
    public double getRepuestosEntregados() { return repuestosEntregados; }

    /** @return comida entregada en kg */
    public double getComidaEntregada() { return comidaEntregada; }

    /** @return agua entregada en litros */
    public double getAguaEntregada() { return aguaEntregada; }
}
