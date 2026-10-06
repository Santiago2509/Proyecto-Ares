package com.ares.colonia.dominio.colonia;

import com.ares.colonia.dominio.excepciones.CapacidadExcedidaException;
import com.ares.colonia.dominio.excepciones.RecursoAgotadoException;
import com.ares.colonia.dominio.modelo.recurso.Recurso;
import com.ares.colonia.dominio.modelo.recurso.TipoRecurso;

import java.util.Collections;
import java.util.EnumMap;
import java.util.Map;

/**
 * ============================================================================
 * ARCHIVO: Inventario.java
 * PAQUETE: com.ares.colonia.dominio.colonia
 * ============================================================================
 *
 * PROPOSITO Y FUNCION PRINCIPAL:
 * Clase generica que administra el almacenamiento de recursos de una colonia.
 * Indexa los recursos por TipoRecurso, controla las operaciones de agregar
 * y consumir, y expone metodos de consulta para el motor de turnos y los
 * reportes. Actua como el "almacen central" de la colonia.
 *
 * ANOTACIONES Y ETIQUETAS UTILIZADAS: Ninguna.
 * HERENCIA E INTERFACES IMPLEMENTADAS:
 * - Extiende de: Ninguna
 * - Implementa: Ninguna
 *
 * PARTICIPACION EN EL MOTOR POR TURNOS:
 * En la fase de modulos (paso 2), el motor invoca agregar() y consumirSeguro()
 * para reflejar la produccion y el consumo de cada modulo y tripulante.
 * ============================================================================
 */
public class Inventario {

    /**
     * Mapa de recursos indexado por tipo. Permite acceso O(1) a cada recurso.
     * Se usa EnumMap por eficiencia con claves enum.
     */
    private final Map<TipoRecurso, Recurso> recursos;

    /**
     * Construye un inventario vacio listo para recibir recursos.
     */
    public Inventario() {
        this.recursos = new EnumMap<>(TipoRecurso.class);
    }

    /**
     * Registra un recurso en el inventario. Si ya existe un recurso del mismo
     * tipo, lanza una excepcion para evitar duplicados.
     *
     * @param recurso el recurso a registrar
     * @throws IllegalArgumentException si ya existe un recurso de ese tipo
     */
    public void registrar(Recurso recurso) {
        if (recursos.containsKey(recurso.getTipo()))
            throw new IllegalArgumentException(
                "Ya existe un recurso de tipo " + recurso.getTipo() + " en este inventario.");
        recursos.put(recurso.getTipo(), recurso);
    }

    /**
     * Agrega cantidad a un recurso del inventario.
     * Si el tipo no esta registrado o se supera la capacidad, registra un warning.
     *
     * @param tipo     tipo de recurso
     * @param cantidad cantidad a agregar (positiva)
     * @throws CapacidadExcedidaException si se supera la capacidad maxima
     */
    public void agregar(TipoRecurso tipo, double cantidad) {
        Recurso r = recursos.get(tipo);
        if (r == null) return; // tipo no registrado, ignorar silenciosamente
        // Verificar si la cantidad supera la capacidad maxima
        double nuevaCantidad = r.getCantidad() + cantidad;
        if (nuevaCantidad > r.getCapacidadMaxima())
            throw new CapacidadExcedidaException(tipo.getNombre(),
                    r.getCapacidadMaxima());
        r.agregar(cantidad);
    }

    /**
     * Consume una cantidad de un recurso del inventario.
     * Lanza excepcion si no hay suficiente stock.
     *
     * @param tipo     tipo de recurso
     * @param cantidad cantidad a consumir
     * @throws RecursoAgotadoException si no hay suficiente stock
     */
    public void consumir(TipoRecurso tipo, double cantidad) {
        Recurso r = recursos.get(tipo);
        if (r == null) return;
        if (r.getCantidad() < cantidad)
            throw new RecursoAgotadoException(tipo.getNombre());
        r.consumir(cantidad);
    }

    /**
     * Consume lo que haya disponible sin lanzar excepcion si no hay suficiente.
     * Util para eventos que deben aplicar su efecto aunque no haya stock.
     *
     * @param tipo     tipo de recurso
     * @param cantidad cantidad maxima a consumir
     * @return cantidad realmente consumida
     */
    public double consumirSeguro(TipoRecurso tipo, double cantidad) {
        Recurso r = recursos.get(tipo);
        if (r == null) return 0;
        double consumido = Math.min(cantidad, r.getCantidad());
        if (consumido > 0) r.consumir(consumido);
        return consumido;
    }

    /**
     * Retorna la cantidad actual de un tipo de recurso.
     *
     * @param tipo tipo de recurso
     * @return cantidad disponible, o 0 si no esta registrado
     */
    public double getCantidad(TipoRecurso tipo) {
        Recurso r = recursos.get(tipo);
        return r == null ? 0.0 : r.getCantidad();
    }

    /**
     * Verifica si un recurso esta en estado critico.
     *
     * @param tipo tipo de recurso
     * @return true si la cantidad esta por debajo del umbral critico
     */
    public boolean estaCritico(TipoRecurso tipo) {
        Recurso r = recursos.get(tipo);
        return r != null && r.estaCritico();
    }

    /**
     * Ejecuta actualizarPorTurno() en todos los recursos registrados.
     * Llamado por el motor al inicio de la fase de produccion.
     */
    public void actualizarPorTurno() {
        recursos.values().forEach(Recurso::actualizarPorTurno);
    }

    /**
     * Retorna una vista inmutable del mapa de recursos.
     *
     * @return mapa inmutable de TipoRecurso -> Recurso
     */
    public Map<TipoRecurso, Recurso> getRecursos() {
        return Collections.unmodifiableMap(recursos);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("Inventario:\n");
        recursos.values().forEach(r -> sb.append("  ").append(r).append("\n"));
        return sb.toString();
    }
}
