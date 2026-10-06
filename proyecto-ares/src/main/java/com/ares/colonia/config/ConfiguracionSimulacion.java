package com.ares.colonia.config;

/**
 * ============================================================================
 * ARCHIVO: ConfiguracionSimulacion.java
 * PAQUETE: com.ares.colonia.config
 * ============================================================================
 *
 * PROPOSITO Y FUNCION PRINCIPAL:
 * Clase de configuracion centralizada que define los parametros base de la
 * simulacion: capacidades de almacenamiento, stocks iniciales de recursos,
 * tasas de produccion y niveles de dificultad. Permite cambiar el comportamiento
 * de toda la simulacion desde un unico punto, siguiendo el principio DRY.
 *
 * ANOTACIONES Y ETIQUETAS UTILIZADAS: Ninguna.
 * HERENCIA E INTERFACES IMPLEMENTADAS: Ninguna (clase de constantes).
 * PARTICIPACION EN EL MOTOR POR TURNOS:
 * Las fabricas (FabricaColonia) consultan esta clase para inicializar las
 * colonias con los stocks y capacidades correctas segun la dificultad.
 * ============================================================================
 */
public final class ConfiguracionSimulacion {

    /** Nombre de la red colonial de demostracion. */
    public static final String NOMBRE_RED = "Red Ares - Primera Oleada";

    // =========================================================================
    // NIVELES DE DIFICULTAD
    // =========================================================================

    /** Multiplicador de recursos iniciales segun dificultad FACIL. */
    public static final double FACTOR_DIFICULTAD_FACIL   = 2.0;

    /** Multiplicador de recursos iniciales segun dificultad NORMAL. */
    public static final double FACTOR_DIFICULTAD_NORMAL  = 1.0;

    /** Multiplicador de recursos iniciales segun dificultad DIFICIL. */
    public static final double FACTOR_DIFICULTAD_DIFICIL = 0.5;

    // =========================================================================
    // CAPACIDADES MAXIMAS DE ALMACENAMIENTO
    // =========================================================================

    public static final double CAPACIDAD_MAX_OXIGENO   = 1000.0; // litros
    public static final double CAPACIDAD_MAX_AGUA      = 2000.0; // litros
    public static final double CAPACIDAD_MAX_ENERGIA   = 1500.0; // unidades
    public static final double CAPACIDAD_MAX_COMIDA    = 1000.0; // kg
    public static final double CAPACIDAD_MAX_REPUESTOS = 500.0;  // unidades

    // =========================================================================
    // STOCKS INICIALES (para dificultad NORMAL)
    // =========================================================================

    public static final double STOCK_INICIAL_OXIGENO   = 500.0;
    public static final double STOCK_INICIAL_AGUA      = 800.0;
    public static final double STOCK_INICIAL_ENERGIA   = 600.0;
    public static final double STOCK_INICIAL_COMIDA    = 400.0;
    public static final double STOCK_INICIAL_REPUESTOS = 150.0;

    // =========================================================================
    // UMBRALES CRITICOS
    // =========================================================================

    public static final double UMBRAL_CRITICO_OXIGENO   = 100.0;
    public static final double UMBRAL_CRITICO_AGUA      = 200.0;
    public static final double UMBRAL_CRITICO_ENERGIA   = 100.0;
    public static final double UMBRAL_CRITICO_COMIDA    = 100.0;
    public static final double UMBRAL_CRITICO_REPUESTOS = 20.0;

    // =========================================================================
    // PARAMETROS DEL MOTOR
    // =========================================================================

    /** Numero de sols que dura el escenario de demostracion por defecto. */
    public static final int SOLS_ESCENARIO_DEMO = 10;

    /** Numero de sols requeridos para completar la Terraformacion. */
    public static final int SOLS_TERRAFORMACION = 100;

    // Clase de utilidad: constructor privado para evitar instanciacion
    private ConfiguracionSimulacion() {}
}
