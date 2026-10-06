package com.ares.colonia.dominio.personal;

import com.ares.colonia.dominio.etiquetas.Auditable;
import com.ares.colonia.dominio.excepciones.TripulanteCriticoException;
import com.ares.colonia.dominio.excepciones.TripulanteFallecidoException;
import com.ares.colonia.dominio.interfaces.Consumidor;
import com.ares.colonia.dominio.modelo.modulo.Modulo;
import com.ares.colonia.dominio.modelo.recurso.TipoRecurso;
import com.ares.colonia.dominio.seguridad.Rol;
import com.ares.colonia.dominio.seguridad.RolTripulante;

import java.util.Map;
import java.util.Objects;
import java.util.UUID;

/**
 * ============================================================================
 * ARCHIVO: Tripulante.java
 * PAQUETE: com.ares.colonia.dominio.personal
 * ============================================================================
 *
 * PROPOSITO Y FUNCION PRINCIPAL:
 * Clase abstracta que representa a cualquier miembro de la tripulacion de
 * una colonia marciana. Define el estado vital comun (SignosVitales, EstadoVital),
 * el rol por composicion, y los contratos abstractos que cada especialidad
 * (Ingeniero, Medico, Cientifico, Piloto, Botanico) debe implementar para
 * definir su comportamiento particular en el motor de simulacion.
 *
 * ANOTACIONES Y ETIQUETAS UTILIZADAS:
 * - @Auditable en actualizarVitalidad(): la actualizacion de vitalidad
 *   queda registrada en el historial cuando el estado cambia a CRITICO o FALLECIDO.
 *
 * HERENCIA E INTERFACES IMPLEMENTADAS:
 * - Extiende de: Ninguna (es raiz de la jerarquia de tripulantes)
 * - Implementa: Consumidor (cada tripulante consume oxigeno, agua y comida por turno)
 *
 * PARTICIPACION EN EL MOTOR POR TURNOS:
 * En la fase de vitalidad (paso 3), el motor invoca actualizarVitalidad()
 * en cada tripulante. Este metodo recalcula los signos vitales, determina
 * el nuevo EstadoVital y lanza las excepciones correspondientes si el
 * tripulante entra en estado CRITICO o FALLECIDO.
 * ============================================================================
 */
public abstract class Tripulante implements Consumidor {

    /** Identificador unico del tripulante (UUID generado al crear la instancia). */
    private final String id;

    /** Nombre completo del tripulante. */
    private final String nombre;

    /** Edad del tripulante en anos terrestres. */
    private final int edad;

    /**
     * Signos vitales actuales. Es un record inmutable; se reemplaza en cada
     * llamada a actualizarVitalidad() con una nueva instancia actualizada.
     */
    private SignosVitales signosVitales;

    /** Estado vital calculado a partir de los signos vitales actuales. */
    private EstadoVital estadoVital;

    /**
     * Rol del tripulante por composicion. Determina sus permisos en el sistema.
     * Por defecto, todo tripulante tiene RolTripulante (nivel minimo).
     */
    private Rol rol;

    /** Modulo al que esta asignado actualmente (puede ser null si no esta asignado). */
    private Modulo moduloAsignado;

    /** Numero de sols (turnos) que lleva activo en la colonia. */
    private int solsActivo;

    /**
     * Construye un tripulante con sus datos basicos.
     * Los signos vitales se inicializan al estado optimo de inicio.
     *
     * @param nombre nombre completo del tripulante
     * @param edad   edad en anos terrestres (debe ser positiva)
     * @throws IllegalArgumentException si el nombre es nulo o la edad no es positiva
     */
    protected Tripulante(String nombre, int edad) {
        // Validaciones en constructor: dominio rico
        if (nombre == null || nombre.isBlank())
            throw new IllegalArgumentException("El nombre del tripulante no puede ser nulo o vacio.");
        if (edad <= 0)
            throw new IllegalArgumentException("La edad debe ser un valor positivo.");

        this.id            = UUID.randomUUID().toString();
        this.nombre        = nombre;
        this.edad          = edad;
        this.signosVitales = SignosVitales.inicial(); // inicio en estado optimo
        this.estadoVital   = EstadoVital.OPTIMO;
        this.rol           = new RolTripulante();     // rol minimo por defecto
        this.solsActivo    = 0;
    }

    // =========================================================================
    // METODOS ABSTRACTOS — cada especialidad los implementa a su manera
    // =========================================================================

    /**
     * Calcula y retorna los signos vitales actualizados para el turno actual.
     * Cada especialidad tiene modificadores propios (ej. el Piloto acumula
     * mas radiacion, el Medico tiene mejor recuperacion de salud).
     *
     * @return nueva instancia de SignosVitales con los valores del turno
     */
    public abstract SignosVitales calcularVitalidad();

    /**
     * Retorna el consumo de oxigeno en litros por turno, propio de la especialidad.
     * El Piloto consume mas oxigeno al operar fuera de la colonia.
     *
     * @return litros de oxigeno consumidos por turno
     */
    public abstract double consumoDeOxigeno();

    /**
     * Calcula el bono de eficiencia que este tripulante aporta al modulo dado.
     * Retorna 0.0 si el tripulante no tiene afinidad con ese tipo de modulo.
     *
     * @param modulo el modulo en el que esta asignado
     * @return bono de eficiencia (0.0 = sin bono, 1.0 = +100% eficiencia)
     */
    public abstract double bonoEn(Modulo modulo);

    // =========================================================================
    // LOGICA DEL MOTOR DE TURNOS
    // =========================================================================

    /**
     * Actualiza los signos vitales del tripulante en el turno actual.
     * Invocado por el motor de simulacion en la fase de vitalidad (paso 3).
     * Si el estado cambia a CRITICO o FALLECIDO, lanza la excepcion correspondiente.
     *
     * @throws TripulanteCriticoException  si el nuevo estado es CRITICO
     * @throws TripulanteFallecidoException si el nuevo estado es FALLECIDO
     */
    @Auditable(operacion = "Actualizacion de vitalidad por turno")
    public void actualizarVitalidad() {
        // Calcular nuevos signos vitales segun la especialidad
        this.signosVitales = calcularVitalidad();

        // Determinar el nuevo estado vital a partir de los signos calculados
        EstadoVital nuevoEstado = signosVitales.calcularEstado();

        // Detectar cambio de estado para lanzar alertas
        if (nuevoEstado == EstadoVital.FALLECIDO) {
            this.estadoVital = EstadoVital.FALLECIDO;
            // El tripulante fallecio: notificar y remover de la colonia
            throw new TripulanteFallecidoException(this.nombre);
        }

        if (nuevoEstado == EstadoVital.CRITICO && this.estadoVital != EstadoVital.CRITICO) {
            // Solo lanzar alerta la primera vez que entra en estado critico
            this.estadoVital = EstadoVital.CRITICO;
            throw new TripulanteCriticoException(this.nombre);
        }

        this.estadoVital = nuevoEstado;
        this.solsActivo++;
    }

    /**
     * Implementacion de Consumidor: retorna los recursos que consume
     * este tripulante por turno (oxigeno, agua, comida).
     *
     * @return mapa de recursos consumidos por turno
     */
    @Override
    public Map<TipoRecurso, Double> consumirPorTurno() {
        return Map.of(
                TipoRecurso.OXIGENO, consumoDeOxigeno(),  // variable segun especialidad
                TipoRecurso.AGUA,    2.5,                  // 2.5 L de agua por sol
                TipoRecurso.COMIDA,  2.0                   // 2.0 kg de comida por sol
        );
    }

    // =========================================================================
    // GETTERS Y SETTERS
    // =========================================================================

    /** @return identificador unico del tripulante */
    public String getId() { return id; }

    /** @return nombre completo del tripulante */
    public String getNombre() { return nombre; }

    /** @return edad en anos terrestres */
    public int getEdad() { return edad; }

    /** @return signos vitales actuales (record inmutable) */
    public SignosVitales getSignosVitales() { return signosVitales; }

    /** @return estado vital actual calculado */
    public EstadoVital getEstadoVital() { return estadoVital; }

    /** @return rol actual del tripulante */
    public Rol getRol() { return rol; }

    /**
     * Asigna un nuevo rol al tripulante. Solo el AdministradorGeneral
     * o LiderDeColonia puede cambiar roles.
     *
     * @param rol el nuevo rol a asignar (no puede ser nulo)
     */
    public void setRol(Rol rol) {
        this.rol = Objects.requireNonNull(rol, "El rol no puede ser nulo.");
    }

    /** @return modulo al que esta asignado actualmente, o null si no tiene */
    public Modulo getModuloAsignado() { return moduloAsignado; }

    /**
     * Asigna el tripulante a un modulo de la colonia.
     *
     * @param modulo el modulo destino (puede ser null para desasignar)
     */
    public void setModuloAsignado(Modulo modulo) { this.moduloAsignado = modulo; }

    /** @return numero de sols que lleva activo en la colonia */
    public int getSolsActivo() { return solsActivo; }

    /** @return true si el tripulante puede realizar tareas en este turno */
    public boolean esOperativo() { return estadoVital.esOperativo(); }

    @Override
    public String toString() {
        return String.format("Tripulante[%s | %s | Estado: %s | Rol: %s]",
                nombre, getClass().getSimpleName(), estadoVital, rol.getNombre());
    }
}
