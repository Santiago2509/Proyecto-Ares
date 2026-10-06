package com.ares.colonia.repositorio;

import java.util.*;

/**
 * ============================================================================
 * ARCHIVO: RepositorioEnMemoria.java
 * PAQUETE: com.ares.colonia.repositorio
 * ============================================================================
 *
 * PROPOSITO Y FUNCION PRINCIPAL:
 * Implementacion en memoria del Repositorio generico. Almacena entidades en
 * un HashMap cuya clave es el ID de la entidad. Util para pruebas unitarias
 * y para el escenario de demostracion sin necesidad de archivos JSON.
 *
 * ANOTACIONES Y ETIQUETAS UTILIZADAS: Ninguna.
 * HERENCIA E INTERFACES IMPLEMENTADAS:
 * - Implementa: Repositorio<T>
 *
 * PARTICIPACION EN EL MOTOR POR TURNOS:
 * Permite guardar y recuperar el estado de colonias, misiones y eventos
 * durante la simulacion en memoria sin persistencia real.
 * ============================================================================
 *
 * @param <T>  tipo de la entidad; debe exponer un metodo getId()
 */
public class RepositorioEnMemoria<T> implements Repositorio<T> {

    /**
     * Almacen interno de entidades. La clave es el ID de la entidad
     * convertido a String.
     */
    private final Map<String, T> almacen;

    /**
     * Funcion extractora de ID: permite obtener el id de cualquier entidad
     * sin requerir una interfaz con getId().
     */
    private final java.util.function.Function<T, String> extractorId;

    /**
     * Construye un repositorio en memoria con un extractor de ID.
     *
     * @param extractorId funcion que obtiene el id de una entidad (ej. Colonia::getId)
     */
    public RepositorioEnMemoria(java.util.function.Function<T, String> extractorId) {
        this.almacen     = new LinkedHashMap<>(); // mantiene el orden de insercion
        this.extractorId = extractorId;
    }

    /**
     * Guarda o actualiza la entidad. El ID se extrae con el extractor configurado.
     *
     * @param entidad la entidad a persistir
     */
    @Override
    public void guardar(T entidad) {
        if (entidad == null)
            throw new IllegalArgumentException("No se puede guardar una entidad nula.");
        String id = extractorId.apply(entidad);
        almacen.put(id, entidad);
    }

    /**
     * Busca una entidad por su ID.
     *
     * @param id id de la entidad
     * @return Optional con la entidad si existe
     */
    @Override
    public Optional<T> buscarPorId(String id) {
        return Optional.ofNullable(almacen.get(id));
    }

    /**
     * Lista todas las entidades almacenadas.
     *
     * @return lista inmutable de todas las entidades
     */
    @Override
    public List<T> listarTodas() {
        return Collections.unmodifiableList(new ArrayList<>(almacen.values()));
    }

    /**
     * Elimina la entidad con el ID dado.
     *
     * @param id id de la entidad a eliminar
     * @return true si fue eliminada, false si no existia
     */
    @Override
    public boolean eliminar(String id) {
        return almacen.remove(id) != null;
    }

    /** @return numero de entidades almacenadas */
    public int size() { return almacen.size(); }
}
