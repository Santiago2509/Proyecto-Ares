package com.ares.colonia.dominio.etiquetas;

import java.lang.annotation.*;

/**
 * ============================================================================
 * ARCHIVO: Experimental.java
 * PAQUETE: com.ares.colonia.dominio.etiquetas
 * ============================================================================
 *
 * PROPOSITO Y FUNCION PRINCIPAL:
 * Anotacion de marcado para clases o metodos que implementan tecnologias
 * en fase de prueba. Los elementos @Experimental tienen una probabilidad
 * adicional de falla en cada turno y sus resultados pueden ser impredecibles.
 *
 * ANOTACIONES Y ETIQUETAS UTILIZADAS:
 * - @Target: TIPO y METODO.
 * - @Retention: RUNTIME.
 * HERENCIA E INTERFACES IMPLEMENTADAS: Ninguna.
 * PARTICIPACION EN EL MOTOR POR TURNOS:
 * EventoService incrementa la probabilidad de FallaElectrica en modulos
 * experimentales durante el calculo de eventos aleatorios.
 * ============================================================================
 */
@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Experimental {
    /** Version de la tecnologia en prueba. */
    String version() default "0.1-alpha";
    /** Probabilidad de falla adicional por turno (0.0 - 1.0). */
    double probabilidadFalla() default 0.05;
}
