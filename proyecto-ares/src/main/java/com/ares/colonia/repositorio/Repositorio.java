package com.ares.colonia.repositorio;

import java.util.List;
import java.util.Optional;

/**
 * ============================================================================
 * ARCHIVO: Repositorio.java
 * PAQUETE: com.ares.colonia.repositorio
 * ============================================================================
 *
 * PROPOSITO Y FUNCION PRINCIPAL:
 * Interfaz generica que abstrae la persistencia de cualquier entidad del sistema.
 * Sigue el patron Repository para desacoplar la logica de negocio del mecanismo
 * de almacenamiento concreto (JSON, base de datos, memoria). Cualquier
 * implementacion (InMemorioRepositorio, JsonRepositorio) debe cumplir este contrato.
 *
 * ANOTACIONES Y ETIQUETAS UTILIZADAS: Ninguna.
 * HERENCIA E INTERFACES IMPLEMENTADAS:
 * - Extiende de: Ninguna
 * - Implementa: Ninguna (es interfaz)
 *
 * PARTICIPACION EN EL MOTOR POR TURNOS:
 * Los servicios usan el repositorio para persistir el estado de la simulacion
 * al final de cada turno y para cargar el estado al iniciar la partida.
 * ============================================================================
 *
 * @param <T>  tipo de la entidad gestionada
 */
public interface Repositorio<T> {

    /**
     * Guarda o actualiza una entidad en el repositorio.
     *
     * @param entidad la entidad a persistir (no puede ser nula)
     */
    void guardar(T entidad);

    /**
     * Busca una entidad por su identificador unico.
     *
     * @param id identificador de la entidad
     * @return Optional con la entidad si existe, o empty si no se encuentra
     */
    Optional<T> buscarPorId(String id);

    /**
     * Retorna todas las entidades almacenadas en el repositorio.
     *
     * @return lista inmutable de todas las entidades
     */
    List<T> listarTodas();

    /**
     * Elimina la entidad con el identificador dado.
     *
     * @param id identificador de la entidad a eliminar
     * @return true si la entidad existia y fue eliminada, false si no se encontro
     */
    boolean eliminar(String id);

    /**
     * Verifica si existe una entidad con el identificador dado.
     *
     * @param id identificador a verificar
     * @return true si existe una entidad con ese id
     */
    default boolean existe(String id) {
        return buscarPorId(id).isPresent();
    }
}
