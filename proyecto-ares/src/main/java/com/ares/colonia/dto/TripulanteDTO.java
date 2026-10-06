package com.ares.colonia.dto;

import com.ares.colonia.dominio.personal.EstadoVital;

/**
 * ============================================================================
 * ARCHIVO: TripulanteDTO.java
 * PAQUETE: com.ares.colonia.dto
 * ============================================================================
 *
 * PROPOSITO Y FUNCION PRINCIPAL:
 * Record de transferencia de datos que representa el estado de un tripulante
 * para su consumo externo (reportes, API). Encapsula los datos mas relevantes
 * sin exponer la entidad de dominio directamente, siguiendo el patron DTO.
 *
 * ANOTACIONES Y ETIQUETAS UTILIZADAS: Ninguna.
 * HERENCIA E INTERFACES IMPLEMENTADAS: Ninguna (es un record).
 * PARTICIPACION EN EL MOTOR POR TURNOS:
 * No participa en el motor; es solo una vista de lectura para reportes.
 * ============================================================================
 */
public record TripulanteDTO(
    /** Identificador unico del tripulante. */
    String id,
    /** Nombre completo del tripulante. */
    String nombre,
    /** Edad en anos terrestres. */
    int edad,
    /** Especialidad (Ingeniero, Medico, Cientifico, Piloto, Botanico). */
    String especialidad,
    /** Estado vital calculado. */
    EstadoVital estadoVital,
    /** Nivel de salud actual (0-100). */
    double salud,
    /** Nivel de fatiga actual (0-100). */
    double fatiga,
    /** Nivel de moral actual (0-100). */
    double moral,
    /** Nombre del modulo al que esta asignado, o "Sin asignar". */
    String moduloAsignado,
    /** Numero de sols activo en la colonia. */
    int solsActivo
) {}
