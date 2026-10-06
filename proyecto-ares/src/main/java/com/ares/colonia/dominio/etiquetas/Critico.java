package com.ares.colonia.dominio.etiquetas;

import java.lang.annotation.*;

/**
 * ============================================================================
 * ARCHIVO: Critico.java
 * PAQUETE: com.ares.colonia.dominio.etiquetas
 * ============================================================================
 *
 * PROPOSITO Y FUNCION PRINCIPAL:
 * Anotacion de marcado para clases, metodos o campos que son criticos para
 * la supervivencia de la colonia. Los elementos anotados con @Critico son
 * priorizados por el motor de simulacion durante la asignacion de recursos
 * y generan alertas de mayor severidad cuando fallan.
 *
 * ANOTACIONES Y ETIQUETAS UTILIZADAS:
 * - @Target: aplicable a TIPO, METODO y CAMPO.
 * - @Retention: RUNTIME (consultada via reflexion por SimulacionService).
 * - @Documented: aparece en el Javadoc generado.
 *
 * HERENCIA E INTERFACES IMPLEMENTADAS:
 * - Extiende de: java.lang.annotation.Annotation (implicito)
 * PARTICIPACION EN EL MOTOR POR TURNOS:
 * SimulacionService escanea los modulos anotados @Critico para garantizar
 * que reciben energia incluso en escasez extrema.
 * ============================================================================
 */
@Target({ElementType.TYPE, ElementType.METHOD, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Critico {
    /** Descripcion opcional del motivo por el que es critico. */
    String motivo() default "Elemento critico para la supervivencia de la colonia";
}
