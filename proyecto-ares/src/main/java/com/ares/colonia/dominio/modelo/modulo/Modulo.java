package com.ares.colonia.dominio.modelo.modulo;

import com.ares.colonia.dominio.etiquetas.Critico;
import com.ares.colonia.dominio.etiquetas.Etiqueta;
import com.ares.colonia.dominio.excepciones.ModuloAveriadoException;
import com.ares.colonia.dominio.interfaces.Consumidor;
import com.ares.colonia.dominio.interfaces.Reparable;

import java.util.EnumSet;
import java.util.Set;
import java.util.UUID;

/**
 * ============================================================================
 * ARCHIVO: Modulo.java
 * PAQUETE: com.ares.colonia.dominio.modelo.modulo
 * ============================================================================
 *
 * PROPOSITO Y FUNCION PRINCIPAL:
 * Clase abstracta que representa cualquier estructura fisica de una colonia
 * marciana: habitat, laboratorio, invernadero, planta de energia, hangar,
 * enfermeria o centro de comunicaciones. Define el estado comun (desgaste,
 * nivel, etiquetas) y obliga a cada subclase a implementar su calculo de
 * eficiencia, consumo y tasa de desgaste propios.
 *
 * ANOTACIONES Y ETIQUETAS UTILIZADAS:
 * - @Critico en calcularEficiencia(): indica que el metodo es critico para
 *   el motor de turnos y su falla debe manejarse con prioridad maxima.
 *
 * HERENCIA E INTERFACES IMPLEMENTADAS:
 * - Extiende de: Ninguna (es raiz de la jerarquia de modulos)
 * - Implementa: Reparable (todo modulo puede ser reparado por un Ingeniero)
 *               Consumidor (consume recursos en cada turno)
 *
 * PARTICIPACION EN EL MOTOR POR TURNOS:
 * En la fase de modulos (paso 2), el motor llama secuencialmente a:
 *   1. consumirPorTurno() -> debita del inventario
 *   2. calcularEficiencia() -> determina cuanto produce realmente
 *   3. desgastePorTurno() -> incrementa el nivel de desgaste
 * Si el desgaste alcanza 1.0 (100%), el modulo se averia y lanza ModuloAveriadoException.
 * ============================================================================
 */
public abstract class Modulo implements Reparable, Consumidor {

    /** Identificador unico del modulo. */
    private final String id;

    /** Nombre descriptivo del modulo (ej. "Invernadero Alpha"). */
    private final String nombre;

    /**
     * Nivel de desgaste acumulado (0.0 = nuevo, 1.0 = destruido).
     * Se incrementa cada turno segun la tasa de desgaste de cada subclase.
     */
    private double nivelDesgaste;

    /**
     * Nivel de mejora del modulo (1 = basico, maximo segun config).
     * Afecta directamente la produccion y capacidad del modulo.
     */
    private int nivel;

    /** Etiquetas semanticas del modulo (ESENCIAL, PELIGROSO, EXPERIMENTAL, etc.). */
    private final Set<Etiqueta> etiquetas;

    /** Estado operativo del modulo. */
    private EstadoModulo estado;

    /** Maxima tasa de desgaste antes de considerar el modulo averiado. */
    protected static final double DESGASTE_MAXIMO = 1.0;

    /** Umbral de desgaste en el que el modulo empieza a perder eficiencia. */
    protected static final double UMBRAL_DESGASTE_EFICIENCIA = 0.5;

    /**
     * Construye un modulo con sus datos basicos.
     * El modulo inicia en nivel 1, desgaste 0 y estado OPERATIVO.
     *
     * @param nombre nombre descriptivo del modulo
     * @throws IllegalArgumentException si el nombre es nulo o vacio
     */
    protected Modulo(String nombre) {
        if (nombre == null || nombre.isBlank())
            throw new IllegalArgumentException("El nombre del modulo no puede ser nulo o vacio.");
        this.id           = UUID.randomUUID().toString();
        this.nombre       = nombre;
        this.nivelDesgaste = 0.0;
        this.nivel        = 1;
        this.etiquetas    = EnumSet.noneOf(Etiqueta.class);
        this.estado       = EstadoModulo.OPERATIVO;
    }

    // =========================================================================
    // METODOS ABSTRACTOS — cada tipo de modulo los implementa segun su funcion
    // =========================================================================

    /**
     * Calcula la eficiencia actual del modulo (0.0 a 1.0).
     * La eficiencia depende del tipo de modulo: personal asignado,
     * recursos disponibles y nivel de desgaste. Una eficiencia de 1.0
     * significa produccion al 100% de capacidad.
     *
     * @return eficiencia entre 0.0 (sin produccion) y 1.0 (maxima produccion)
     * @throws ModuloAveriadoException si el desgaste es >= DESGASTE_MAXIMO
     */
    @Critico(motivo = "Determina la produccion de recursos vitales de la colonia")
    public abstract double calcularEficiencia();

    /**
     * Retorna la tasa de desgaste que sufre el modulo en un turno normal.
     * La PlantaDeEnergia tiene la mayor tasa; el Habitacional la menor.
     *
     * @return incremento de desgaste por turno (valor entre 0.0 y 0.1)
     */
    public abstract double desgastePorTurno();

    // =========================================================================
    // LOGICA DEL MOTOR DE TURNOS
    // =========================================================================

    /**
     * Aplica el desgaste del turno actual al modulo.
     * Si el desgaste alcanza el maximo, cambia el estado a AVERIADO
     * y lanza ModuloAveriadoException.
     *
     * @throws ModuloAveriadoException si el desgaste supera o iguala DESGASTE_MAXIMO
     */
    public void aplicarDesgaste() {
        // Incrementar el desgaste acumulado con la tasa propia del modulo
        this.nivelDesgaste = Math.min(DESGASTE_MAXIMO, this.nivelDesgaste + desgastePorTurno());

        // Regla de negocio: si el desgaste llega al maximo, el modulo se averia
        if (this.nivelDesgaste >= DESGASTE_MAXIMO) {
            this.estado = EstadoModulo.AVERIADO;
            throw new ModuloAveriadoException(this.nombre);
        }

        // Regla de negocio: si el desgaste supera el 75%, el modulo entra en mantenimiento
        if (this.nivelDesgaste >= 0.75) {
            this.estado = EstadoModulo.EN_MANTENIMIENTO;
        }
    }

    // =========================================================================
    // IMPLEMENTACION DE Reparable
    // =========================================================================

    /**
     * {@inheritDoc}
     * Reduce el nivel de desgaste del modulo en la cantidad especificada.
     * Si el desgaste llega a 0, el estado vuelve a OPERATIVO.
     *
     * @param cantidadReparacion puntos de reparacion a aplicar
     */
    @Override
    public void reparar(double cantidadReparacion) {
        // Reducir el desgaste sin ir por debajo de cero
        this.nivelDesgaste = Math.max(0.0, this.nivelDesgaste - cantidadReparacion);

        // Restaurar el estado operativo si el desgaste queda bajo control
        if (this.nivelDesgaste < 0.75) {
            this.estado = EstadoModulo.OPERATIVO;
        }
    }

    @Override
    public double getNivelDesgaste() { return nivelDesgaste; }

    // =========================================================================
    // GETTERS Y ETIQUETAS
    // =========================================================================

    /** @return identificador unico del modulo */
    public String getId() { return id; }

    /** @return nombre descriptivo del modulo */
    public String getNombre() { return nombre; }

    /** @return nivel de mejora actual del modulo */
    public int getNivel() { return nivel; }

    /**
     * Mejora el nivel del modulo. Solo puede hacerlo un Ingeniero con suficientes repuestos.
     *
     * @param nuevoNivel el nuevo nivel (debe ser mayor al actual)
     */
    public void setNivel(int nuevoNivel) {
        if (nuevoNivel <= this.nivel)
            throw new IllegalArgumentException("El nuevo nivel debe ser mayor al actual.");
        this.nivel = nuevoNivel;
    }

    /** @return estado operativo actual del modulo */
    public EstadoModulo getEstado() { return estado; }

    /** @return true si el modulo esta operativo y puede funcionar */
    public boolean estaOperativo() { return estado == EstadoModulo.OPERATIVO; }

    /** @return conjunto inmutable de etiquetas del modulo */
    public Set<Etiqueta> getEtiquetas() { return Set.copyOf(etiquetas); }

    /**
     * Agrega una etiqueta semantica al modulo.
     *
     * @param etiqueta la etiqueta a agregar
     */
    public void agregarEtiqueta(Etiqueta etiqueta) { etiquetas.add(etiqueta); }

    /**
     * Verifica si el modulo tiene una etiqueta especifica.
     *
     * @param etiqueta la etiqueta a verificar
     * @return true si el modulo tiene la etiqueta
     */
    public boolean tieneEtiqueta(Etiqueta etiqueta) { return etiquetas.contains(etiqueta); }

    @Override
    public String toString() {
        return String.format("Modulo[%s | %s | Desgaste: %.1f%% | Estado: %s]",
                nombre, getClass().getSimpleName(), nivelDesgaste * 100, estado);
    }
}
