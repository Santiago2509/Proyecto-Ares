package com.ares.colonia.dominio.interfaces;

import com.ares.colonia.dominio.modelo.recurso.TipoRecurso;
import java.util.Map;

/**
 * ============================================================================
 * ARCHIVO: Productor.java
 * PAQUETE: com.ares.colonia.dominio.interfaces
 * ============================================================================
 *
 * PROPOSITO Y FUNCION PRINCIPAL:
 * Contrato para los modulos que generan recursos en cada turno.
 * Permite al motor de simulacion preguntar a cada modulo cuanto produce
 * sin conocer su tipo concreto (polimorfismo).
 *
 * ANOTACIONES Y ETIQUETAS UTILIZADAS: Ninguna.
 * HERENCIA E INTERFACES IMPLEMENTADAS: Ninguna.
 * PARTICIPACION EN EL MOTOR POR TURNOS:
 * Invocada en la fase de produccion (paso 2) para acreditar recursos
 * al inventario de la colonia.
 * ============================================================================
 */
public interface Productor {

    /**
     * Calcula y retorna los recursos que este elemento produce en un turno.
     * La produccion es proporcional a la eficiencia calculada del modulo.
     *
     * @return mapa de TipoRecurso -> cantidad producida
     */
    Map<TipoRecurso, Double> producirPorTurno();
}
