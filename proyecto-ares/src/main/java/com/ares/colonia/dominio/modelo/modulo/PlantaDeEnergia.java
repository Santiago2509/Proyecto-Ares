package com.ares.colonia.dominio.modelo.modulo;

import com.ares.colonia.dominio.etiquetas.Critico;
import com.ares.colonia.dominio.excepciones.ModuloAveriadoException;
import com.ares.colonia.dominio.interfaces.Productor;
import com.ares.colonia.dominio.modelo.recurso.TipoRecurso;

import java.util.Map;

/**
 * ============================================================================
 * ARCHIVO: PlantaDeEnergia.java
 * PAQUETE: com.ares.colonia.dominio.modelo.modulo
 * ============================================================================
 *
 * PROPOSITO Y FUNCION PRINCIPAL:
 * Modulo que genera la energia que alimenta a todos los demas modulos y
 * unidades de la colonia. Es el modulo mas critico del sistema: si falla,
 * todos los demas modulos reducen su eficiencia hasta detenerse.
 * Es el modulo con la mayor tasa de desgaste por turno.
 *
 * ANOTACIONES Y ETIQUETAS UTILIZADAS:
 * - @Critico en calcularEficiencia(): la energia es el recurso mas vital.
 *
 * HERENCIA E INTERFACES IMPLEMENTADAS:
 * - Extiende de: Modulo
 * - Implementa: Productor (produce energia cada turno)
 *
 * PARTICIPACION EN EL MOTOR POR TURNOS:
 * Primera en ser procesada en la fase de modulos para que los demas
 * modulos puedan calcular su eficiencia en funcion de la energia disponible.
 * ============================================================================
 */
@Critico(motivo = "Sin energia, toda la colonia falla en cascada")
public class PlantaDeEnergia extends Modulo implements Productor {

    /** Produccion base de energia por turno (kWh) cuando esta al 100% de eficiencia. */
    private static final double PRODUCCION_BASE_ENERGIA = 500.0;

    /** Tasa de desgaste por turno: la mas alta del sistema. */
    private static final double TASA_DESGASTE = 0.03;

    /** Numero de ingenieros asignados actualmente. Afecta la eficiencia. */
    private int ingenierosAsignados;

    /**
     * Construye una Planta de Energia con su nombre.
     *
     * @param nombre nombre descriptivo (ej. "Planta Solar Alfa")
     */
    public PlantaDeEnergia(String nombre) {
        super(nombre);
        this.ingenierosAsignados = 0;
    }

    /**
     * Calcula la eficiencia de la planta segun el desgaste y los ingenieros asignados.
     * - Base: 1.0 (100%)
     * - Penalizacion por desgaste: si desgaste > 50%, eficiencia = 1 - (desgaste - 0.5)
     * - Bono por ingenieros: +10% por ingeniero, maximo +30%
     *
     * @return eficiencia entre 0.0 y 1.3
     * @throws ModuloAveriadoException si el desgaste es >= 100%
     */
    @Override
    @Critico(motivo = "Determina el suministro electrico de toda la colonia")
    public double calcularEficiencia() {
        // Si la planta esta averiada, no puede producir nada
        if (!estaOperativo()) throw new ModuloAveriadoException(getNombre());

        double eficiencia = 1.0;

        // Penalizacion por desgaste: comienza a reducirse al superar el 50%
        if (getNivelDesgaste() > UMBRAL_DESGASTE_EFICIENCIA) {
            eficiencia -= (getNivelDesgaste() - UMBRAL_DESGASTE_EFICIENCIA);
        }

        // Bono por ingenieros asignados: cada ingeniero aporta 10%, maximo 3 ingenieros
        eficiencia += Math.min(ingenierosAsignados, 3) * 0.10;

        // Garantizar que la eficiencia este dentro del rango valido
        return Math.max(0.0, Math.min(1.3, eficiencia));
    }

    /**
     * Tasa de desgaste de la planta de energia: la mas alta del sistema
     * porque trabaja continuamente bajo alta carga.
     *
     * @return tasa de desgaste por turno (0.03 = 3% por sol)
     */
    @Override
    public double desgastePorTurno() {
        // Desgaste adicional si no hay ingenieros de mantenimiento
        return ingenierosAsignados == 0 ? TASA_DESGASTE * 1.5 : TASA_DESGASTE;
    }

    /**
     * Produce energia en funcion de la eficiencia actual.
     * La produccion es: PRODUCCION_BASE * eficiencia * nivel_modulo.
     *
     * @return mapa con la energia producida en este turno
     */
    @Override
    public Map<TipoRecurso, Double> producirPorTurno() {
        double produccion = PRODUCCION_BASE_ENERGIA * calcularEficiencia() * getNivel();
        return Map.of(TipoRecurso.ENERGIA, produccion);
    }

    /**
     * La planta consume repuestos para su mantenimiento continuo.
     *
     * @return mapa con los repuestos consumidos en este turno
     */
    @Override
    public Map<TipoRecurso, Double> consumirPorTurno() {
        // Consume mas repuestos si hay ingenieros haciendo mantenimiento activo
        double repuestos = 2.0 + (ingenierosAsignados * 0.5);
        return Map.of(TipoRecurso.REPUESTOS, repuestos);
    }

    /**
     * Asigna ingenieros a la planta para mantenimiento.
     *
     * @param cantidad numero de ingenieros a asignar
     */
    public void setIngenierosAsignados(int cantidad) {
        this.ingenierosAsignados = Math.max(0, cantidad);
    }

    /** @return numero de ingenieros asignados actualmente */
    public int getIngenierosAsignados() { return ingenierosAsignados; }
}
