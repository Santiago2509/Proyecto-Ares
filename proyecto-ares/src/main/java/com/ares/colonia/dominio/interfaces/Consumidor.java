package com.ares.colonia.dominio.interfaces;

import com.ares.colonia.dominio.modelo.recurso.TipoRecurso;
import java.util.Map;

/**
 * ============================================================================
 * ARCHIVO: Consumidor.java
 * PAQUETE: com.ares.colonia.dominio.interfaces
 * ============================================================================
 *
 * PROPOSITO Y FUNCION PRINCIPAL:
 * Contrato para los modulos y tripulantes que consumen recursos cada turno.
 * Permite al motor descontar el consumo del inventario de forma uniforme.
 *
 * ANOTACIONES Y ETIQUETAS UTILIZADAS: Ninguna.
 * HERENCIA E INTERFACES IMPLEMENTADAS: Ninguna.
 * PARTICIPACION EN EL MOTOR POR TURNOS:
 * Invocada en la fase de consumo (paso 2) para debitar recursos del inventario.
 * ============================================================================
 */
public interface Consumidor {

    /**
     * Calcula y retorna los recursos que este elemento consume en un turno.
     *
     * @return mapa de TipoRecurso -> cantidad consumida
     */
    Map<TipoRecurso, Double> consumirPorTurno();
}
