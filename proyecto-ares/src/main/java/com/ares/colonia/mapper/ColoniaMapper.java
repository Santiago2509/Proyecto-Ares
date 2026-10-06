package com.ares.colonia.mapper;

import com.ares.colonia.dominio.colonia.Colonia;
import com.ares.colonia.dominio.modelo.mision.EstadoMision;
import com.ares.colonia.dominio.modelo.modulo.Modulo;
import com.ares.colonia.dominio.modelo.recurso.TipoRecurso;
import com.ares.colonia.dominio.personal.Tripulante;
import com.ares.colonia.dto.ColoniaDTO;
import com.ares.colonia.dto.TripulanteDTO;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * ============================================================================
 * ARCHIVO: ColoniaMapper.java
 * PAQUETE: com.ares.colonia.mapper
 * ============================================================================
 *
 * PROPOSITO Y FUNCION PRINCIPAL:
 * Clase de utilidad que convierte entidades de dominio (Colonia, Tripulante)
 * a sus correspondientes DTOs de transferencia. Centraliza la logica de
 * mapeo para que no contamine ni la capa de dominio ni la de servicio.
 *
 * ANOTACIONES Y ETIQUETAS UTILIZADAS: Ninguna.
 * HERENCIA E INTERFACES IMPLEMENTADAS: Ninguna.
 * PARTICIPACION EN EL MOTOR POR TURNOS:
 * No participa en el motor; es invocado por los servicios al generar reportes.
 * ============================================================================
 */
public class ColoniaMapper {

    /**
     * Convierte una entidad Colonia en su ColoniaDTO correspondiente.
     *
     * @param colonia la entidad a convertir
     * @return ColoniaDTO con el estado actual de la colonia
     */
    public static ColoniaDTO toDTO(Colonia colonia) {
        // Convertir recursos del inventario
        Map<String, Double> recursos = new HashMap<>();
        for (TipoRecurso tipo : TipoRecurso.values()) {
            recursos.put(tipo.getNombre(), colonia.getInventario().getCantidad(tipo));
        }

        // Convertir tripulantes a DTOs
        List<TripulanteDTO> tripulanteDTOs = colonia.getTripulantes().stream()
                .map(ColoniaMapper::toDTO)
                .collect(Collectors.toList());

        // Contar modulos operativos
        long modulosOperativos = colonia.getModulos().stream()
                .filter(Modulo::estaOperativo)
                .count();

        // Contar misiones en curso
        long misionesEnCurso = colonia.getMisiones().stream()
                .filter(m -> m.getEstado() == EstadoMision.EN_CURSO)
                .count();

        return new ColoniaDTO(
                colonia.getId(),
                colonia.getNombre(),
                colonia.getSolActual(),
                colonia.getTripulantesVivos(),
                (int) modulosOperativos,
                (int) misionesEnCurso,
                recursos,
                tripulanteDTOs
        );
    }

    /**
     * Convierte una entidad Tripulante en su TripulanteDTO correspondiente.
     *
     * @param tripulante la entidad a convertir
     * @return TripulanteDTO con el estado vital del tripulante
     */
    public static TripulanteDTO toDTO(Tripulante tripulante) {
        String moduloNombre = tripulante.getModuloAsignado() != null
                ? tripulante.getModuloAsignado().getNombre()
                : "Sin asignar";
        return new TripulanteDTO(
                tripulante.getId(),
                tripulante.getNombre(),
                tripulante.getEdad(),
                tripulante.getClass().getSimpleName(),
                tripulante.getEstadoVital(),
                tripulante.getSignosVitales().salud(),
                tripulante.getSignosVitales().fatiga(),
                tripulante.getSignosVitales().moral(),
                moduloNombre,
                tripulante.getSolsActivo()
        );
    }
}
