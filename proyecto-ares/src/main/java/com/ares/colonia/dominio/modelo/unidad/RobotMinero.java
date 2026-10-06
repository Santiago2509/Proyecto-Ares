package com.ares.colonia.dominio.modelo.unidad;

import com.ares.colonia.dominio.modelo.recurso.TipoRecurso;

import java.util.Map;

/**
 * ============================================================================
 * ARCHIVO: RobotMinero.java
 * PAQUETE: com.ares.colonia.dominio.modelo.unidad
 * ============================================================================
 *
 * PROPOSITO Y FUNCION PRINCIPAL:
 * Unidad robotica industrial especializada en extraccion de recursos del
 * subsuelo marciano: agua subterranea y materiales para repuestos. Es la
 * principal fuente de abastecimiento de agua y repuestos independiente
 * de los suministros de la Tierra. Su debilidad es su lentitud y el alto
 * desgaste que acumula por el trabajo pesado.
 *
 * ANOTACIONES Y ETIQUETAS UTILIZADAS: Ninguna directa.
 * HERENCIA E INTERFACES IMPLEMENTADAS:
 * - Extiende de: Unidad
 * - Implementa: Reparable (heredado de Unidad)
 *
 * PARTICIPACION EN EL MOTOR POR TURNOS:
 * Cuando esta activo, ejecutarTarea() devuelve los recursos extraidos
 * que la Colonia debe agregar al inventario. Su tasa de extraccion
 * aumenta con menos desgaste acumulado.
 * ============================================================================
 */
public class RobotMinero extends Unidad {

    /** Tasa base de extraccion de agua por turno (en litros). */
    private final double tasaExtraccionAgua;

    /** Tasa base de extraccion de materiales para repuestos por turno. */
    private final double tasaExtraccionRepuestos;

    /**
     * Construye un RobotMinero con su nombre y tasas de extraccion.
     *
     * @param nombre                  nombre del robot (ej. "Minero-Delta")
     * @param tasaExtraccionAgua      litros de agua extraidos por turno
     * @param tasaExtraccionRepuestos unidades de repuesto extraidas por turno
     */
    public RobotMinero(String nombre, double tasaExtraccionAgua, double tasaExtraccionRepuestos) {
        super(nombre);
        this.tasaExtraccionAgua      = Math.max(0.0, tasaExtraccionAgua);
        this.tasaExtraccionRepuestos = Math.max(0.0, tasaExtraccionRepuestos);
    }

    /**
     * Ejecuta la tarea de mineria: extrae agua y repuestos del subsuelo.
     * La extraccion se reduce proporcionalmente con el nivel de desgaste
     * del robot (regla de negocio: mas desgaste = menos eficiencia).
     *
     * @return descripcion de lo extraido en el turno
     */
    @Override
    public String ejecutarTarea() {
        if (!isDisponible()) {
            return getNombre() + " no esta disponible para minar.";
        }
        // La eficiencia de extraccion cae con el desgaste
        double factorEficiencia = 1.0 - (getNivelDesgaste() * 0.5); // hasta 50% menos al maximo desgaste
        double aguaExtraida      = tasaExtraccionAgua      * factorEficiencia;
        double repuestosExtraidos = tasaExtraccionRepuestos * factorEficiencia;

        // El trabajo pesado genera alto desgaste
        aplicarDesgaste(0.07); // 7% de desgaste por turno de mineria
        consumirEnergia(20.0); // consume energia de su bateria

        return String.format(
                "%s extrajo %.1f L de agua y %.1f unidades de repuestos. Desgaste: %.1f%%",
                getNombre(), aguaExtraida, repuestosExtraidos, getNivelDesgaste() * 100);
    }

    /**
     * Calcula los recursos que el RobotMinero consume del inventario colonial.
     * Consume energia y repuestos para su propio mantenimiento.
     *
     * @return mapa de TipoRecurso -> cantidad consumida
     */
    @Override
    public Map<TipoRecurso, Double> calcularConsumo() {
        // Alto consumo de energia y repuestos por ser maquinaria pesada
        return Map.of(
                TipoRecurso.ENERGIA,   18.0,  // motores de excavacion
                TipoRecurso.REPUESTOS, 1.0    // auto-mantenimiento minimo
        );
    }

    /**
     * Calcula la produccion real de agua en este turno, ajustada por desgaste.
     *
     * @return litros de agua producidos
     */
    public double calcularProduccionAgua() {
        double factorEficiencia = 1.0 - (getNivelDesgaste() * 0.5);
        return tasaExtraccionAgua * factorEficiencia;
    }

    /**
     * Calcula la produccion real de repuestos en este turno, ajustada por desgaste.
     *
     * @return unidades de repuestos producidas
     */
    public double calcularProduccionRepuestos() {
        double factorEficiencia = 1.0 - (getNivelDesgaste() * 0.5);
        return tasaExtraccionRepuestos * factorEficiencia;
    }

    /** @return tasa base de extraccion de agua en L/turno */
    public double getTasaExtraccionAgua() { return tasaExtraccionAgua; }

    /** @return tasa base de extraccion de repuestos por turno */
    public double getTasaExtraccionRepuestos() { return tasaExtraccionRepuestos; }
}
