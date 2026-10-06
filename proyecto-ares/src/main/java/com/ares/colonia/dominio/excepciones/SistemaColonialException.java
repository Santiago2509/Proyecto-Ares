package com.ares.colonia.dominio.excepciones;

/**
 * ============================================================================
 * ARCHIVO: SistemaColonialException.java
 * PAQUETE: com.ares.colonia.dominio.excepciones
 * ============================================================================
 *
 * PROPOSITO Y FUNCION PRINCIPAL:
 * Excepcion base de toda la jerarquia de errores de negocio del sistema
 * Proyecto Ares. Toda excepcion de dominio hereda de esta clase, lo que
 * permite capturarlas de forma general (catch SistemaColonialException)
 * o especifica segun el sub-tipo, y traducirlas en un unico punto de
 * manejo de errores (handler centralizado en la capa de servicio o REST).
 *
 * ANOTACIONES Y ETIQUETAS UTILIZADAS:
 * - Ninguna anotacion especifica; es una excepcion pura de dominio.
 *
 * HERENCIA E INTERFACES IMPLEMENTADAS:
 * - Extiende de: RuntimeException (excepcion no comprobada)
 * - Implementa: Ninguna
 *
 * PARTICIPACION EN EL MOTOR POR TURNOS:
 * Esta excepcion no participa directamente en el avance del sol, pero
 * es lanzada por cualquier operacion de negocio que viole las reglas
 * del dominio durante el procesamiento de un turno.
 * ============================================================================
 */
public class SistemaColonialException extends RuntimeException {

    /** Codigo de error legible para logs y respuestas de API. */
    private final String codigo;

    /**
     * Construye la excepcion con un mensaje de error y un codigo identificador.
     *
     * @param codigo  codigo corto del error (ej. "RECURSO_AGOTADO")
     * @param mensaje descripcion del problema
     */
    public SistemaColonialException(String codigo, String mensaje) {
        super(mensaje);
        this.codigo = codigo;
    }

    /**
     * Construye la excepcion encadenando la causa original.
     *
     * @param codigo  codigo corto del error
     * @param mensaje descripcion del problema
     * @param causa   excepcion que origino este error
     */
    public SistemaColonialException(String codigo, String mensaje, Throwable causa) {
        super(mensaje, causa);
        this.codigo = codigo;
    }

    /**
     * Retorna el codigo identificador del error de negocio.
     *
     * @return codigo de error (nunca nulo)
     */
    public String getCodigo() {
        return codigo;
    }
}
