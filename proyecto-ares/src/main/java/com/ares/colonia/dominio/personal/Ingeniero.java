package com.ares.colonia.dominio.personal;

import com.ares.colonia.dominio.modelo.modulo.Hangar;
import com.ares.colonia.dominio.modelo.modulo.Modulo;
import com.ares.colonia.dominio.modelo.modulo.PlantaDeEnergia;
import com.ares.colonia.dominio.modelo.recurso.TipoRecurso;
import com.ares.colonia.dominio.interfaces.Reparable;

import java.util.Map;

/**
 * ============================================================================
 * ARCHIVO: Ingeniero.java
 * PAQUETE: com.ares.colonia.dominio.personal
 * ============================================================================
 *
 * PROPOSITO Y FUNCION PRINCIPAL:
 * Especialidad de tripulante con habilidades en mantenimiento, reparacion
 * y operacion de sistemas mecanicos y energeticos. Aporta bono de eficiencia
 * en PlantaDeEnergia y Hangar, y puede reparar modulos y unidades usando
 * repuestos del inventario.
 *
 * ANOTACIONES Y ETIQUETAS UTILIZADAS: Ninguna directa.
 * HERENCIA E INTERFACES IMPLEMENTADAS:
 * - Extiende de: Tripulante
 * PARTICIPACION EN EL MOTOR POR TURNOS:
 * Sus reparaciones se ejecutan durante la fase de modulos si hay repuestos
 * disponibles. Su bono se aplica en calcularEficiencia() de PlantaDeEnergia y Hangar.
 * ============================================================================
 */
public class Ingeniero extends Tripulante {

    /** Nivel de habilidad tecnica (1-5). Afecta la cantidad reparada por turno. */
    private final int nivelHabilidad;

    public Ingeniero(String nombre, int edad, int nivelHabilidad) {
        super(nombre, edad);
        this.nivelHabilidad = Math.max(1, Math.min(5, nivelHabilidad));
    }

    /**
     * Calcula los signos vitales actualizados del Ingeniero.
     * Los Ingenieros acumulan fatiga extra al reparar en turnos de emergencia.
     *
     * @return nuevos SignosVitales para este turno
     */
    @Override
    public SignosVitales calcularVitalidad() {
        SignosVitales sv = getSignosVitales();
        // La fatiga aumenta 3 puntos por turno; el estres aumenta 2
        double nuevaFatiga = Math.min(100, sv.fatiga() + 3);
        double nuevoEstres = Math.min(100, sv.estres() + 2);
        // Salud se reduce muy lento si fatiga > 80
        double nuevaSalud  = sv.salud() - (sv.fatiga() > 80 ? 1.0 : 0.0);
        return new SignosVitales(
                nuevaSalud, sv.moral(), consumoDeOxigeno(),
                sv.hidratacion() - 1.5, sv.nutricion() - 2.0,
                nuevaFatiga, nuevoEstres, sv.radiacion() + 0.1
        );
    }

    @Override
    public double consumoDeOxigeno() { return 1.8; } // consumo estandar

    /**
     * El Ingeniero aporta bono en PlantaDeEnergia (+20%) y Hangar (+15%).
     *
     * @param modulo el modulo asignado
     * @return bono de eficiencia
     */
    @Override
    public double bonoEn(Modulo modulo) {
        if (modulo instanceof PlantaDeEnergia) return 0.20;
        if (modulo instanceof Hangar)           return 0.15;
        return 0.0; // sin bono en otros modulos
    }

    /**
     * Repara un elemento Reparable usando repuestos del inventario.
     * La cantidad reparada depende del nivel de habilidad del ingeniero.
     *
     * @param objetivo el modulo o unidad a reparar
     * @return puntos de reparacion aplicados
     */
    public double reparar(Reparable objetivo) {
        double cantidadReparacion = nivelHabilidad * 0.05; // 5-25% por turno
        objetivo.reparar(cantidadReparacion);
        return cantidadReparacion;
    }

    public int getNivelHabilidad() { return nivelHabilidad; }
}
