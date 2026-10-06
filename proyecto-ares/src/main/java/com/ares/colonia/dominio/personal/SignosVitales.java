package com.ares.colonia.dominio.personal;

/**
 * ============================================================================
 * ARCHIVO: SignosVitales.java
 * PAQUETE: com.ares.colonia.dominio.personal
 * ============================================================================
 *
 * PROPOSITO Y FUNCION PRINCIPAL:
 * Objeto de valor (record inmutable) que encapsula todos los indicadores
 * biometricos de un tripulante en un momento dado. Al ser un record,
 * garantiza la inmutabilidad: en cada turno se crea una nueva instancia
 * con los valores actualizados, preservando el historial.
 *
 * ANOTACIONES Y ETIQUETAS UTILIZADAS: Ninguna.
 * HERENCIA E INTERFACES IMPLEMENTADAS: Ninguna (record no hereda de clases).
 * PARTICIPACION EN EL MOTOR POR TURNOS:
 * La clase Tripulante produce una nueva instancia de SignosVitales en
 * cada avance de turno (sol). El EstadoVital se recalcula a partir de
 * estos valores actualizados.
 * ============================================================================
 *
 * @param salud          porcentaje de salud fisica (0.0 - 100.0)
 * @param moral          porcentaje de salud mental/moral (0.0 - 100.0)
 * @param oxigenoConsumido litros de oxigeno consumidos (acumulado en el turno)
 * @param hidratacion    nivel de hidratacion (0.0 - 100.0)
 * @param nutricion      nivel de nutricion (0.0 - 100.0)
 * @param fatiga         nivel de fatiga acumulada (0.0 - 100.0; alto = negativo)
 * @param estres         nivel de estres psicologico (0.0 - 100.0; alto = negativo)
 * @param radiacion      dosis de radiacion acumulada en sieverts (aumenta con el tiempo)
 */
public record SignosVitales(
        double salud,
        double moral,
        double oxigenoConsumido,
        double hidratacion,
        double nutricion,
        double fatiga,
        double estres,
        double radiacion
) {
    /**
     * Constructor compacto con validaciones de negocio.
     * Garantiza que los porcentajes no excedan sus rangos validos.
     */
    public SignosVitales {
        // Clampear valores al rango [0, 100] para indicadores porcentuales
        salud         = Math.max(0, Math.min(100, salud));
        moral         = Math.max(0, Math.min(100, moral));
        hidratacion   = Math.max(0, Math.min(100, hidratacion));
        nutricion     = Math.max(0, Math.min(100, nutricion));
        fatiga        = Math.max(0, Math.min(100, fatiga));
        estres        = Math.max(0, Math.min(100, estres));
        // La radiacion es acumulativa y no tiene limite superior (escala de peligro)
        radiacion     = Math.max(0, radiacion);
        oxigenoConsumido = Math.max(0, oxigenoConsumido);
    }

    /**
     * Crea una instancia con signos vitales en plena salud (valores iniciales optimos).
     *
     * @return SignosVitales con todos los indicadores al maximo saludable
     */
    public static SignosVitales inicial() {
        return new SignosVitales(100, 80, 0, 100, 100, 0, 0, 0);
    }

    /**
     * Calcula el EstadoVital a partir de los valores actuales aplicando
     * las reglas de negocio del dominio.
     *
     * @return el EstadoVital correspondiente a estos signos vitales
     */
    public EstadoVital calcularEstado() {
        // Regla: el tripulante fallece si la salud llega a 0
        if (salud <= 0) return EstadoVital.FALLECIDO;

        // Regla: CRITICO si salud, hidratacion o nutricion cae por debajo del 25%
        if (salud < 25 || hidratacion < 25 || nutricion < 25) return EstadoVital.CRITICO;

        // Regla: OPTIMO si todos los indicadores positivos superan el 70%
        if (salud >= 70 && moral >= 70 && hidratacion >= 70 && nutricion >= 70) return EstadoVital.OPTIMO;

        // En cualquier otro caso: ESTABLE
        return EstadoVital.ESTABLE;
    }
}
