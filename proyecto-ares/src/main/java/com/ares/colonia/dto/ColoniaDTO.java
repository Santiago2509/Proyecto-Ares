package com.ares.colonia.dto;

import java.util.List;
import java.util.Map;

/**
 * ============================================================================
 * ARCHIVO: ColoniaDTO.java
 * PAQUETE: com.ares.colonia.dto
 * ============================================================================
 *
 * PROPOSITO Y FUNCION PRINCIPAL:
 * Record de transferencia de datos que representa el estado completo de una
 * colonia en un sol determinado. Incluye un resumen de recursos, tripulantes
 * y modulos activos para generar reportes comparativos en RedColonial.
 *
 * ANOTACIONES Y ETIQUETAS UTILIZADAS: Ninguna.
 * HERENCIA E INTERFACES IMPLEMENTADAS: Ninguna (es un record).
 * PARTICIPACION EN EL MOTOR POR TURNOS:
 * No participa en el motor; es una vista de lectura para reportes comparativos.
 * ============================================================================
 */
public record ColoniaDTO(
    /** Identificador unico de la colonia. */
    String id,
    /** Nombre de la colonia. */
    String nombre,
    /** Sol actual en que se tomo la instantanea. */
    int solActual,
    /** Numero de tripulantes vivos. */
    int tripulantesVivos,
    /** Numero de modulos operativos. */
    int modulosOperativos,
    /** Numero de misiones en curso. */
    int misionesEnCurso,
    /** Mapa de tipo de recurso (nombre) -> cantidad actual. */
    Map<String, Double> recursos,
    /** Lista de DTOs de tripulantes. */
    List<TripulanteDTO> tripulantes
) {}
