package com.ares.colonia.dominio.personal;

import com.ares.colonia.dominio.modelo.modulo.Enfermeria;
import com.ares.colonia.dominio.modelo.modulo.Modulo;

/**
 * ============================================================================
 * ARCHIVO: Medico.java
 * PAQUETE: com.ares.colonia.dominio.personal
 * ============================================================================
 *
 * PROPOSITO Y FUNCION PRINCIPAL:
 * Especialidad de tripulante con habilidades medicas. Mejora la recuperacion
 * de vitalidad de otros tripulantes y aporta el maximo bono en la Enfermeria.
 * Un medico asignado a la enfermeria puede reducir el impacto de un BroteDeEnfermedad.
 *
 * ANOTACIONES Y ETIQUETAS UTILIZADAS: Ninguna.
 * HERENCIA E INTERFACES IMPLEMENTADAS:
 * - Extiende de: Tripulante
 * PARTICIPACION EN EL MOTOR POR TURNOS:
 * En la fase de vitalidad, el medico aplica tratamiento a tripulantes CRITICOS
 * si esta asignado a la Enfermeria.
 * ============================================================================
 */
public class Medico extends Tripulante {

    /** Especializacion medica del doctor. */
    private final String especializacion;

    public Medico(String nombre, int edad, String especializacion) {
        super(nombre, edad);
        this.especializacion = especializacion;
    }

    @Override
    public SignosVitales calcularVitalidad() {
        SignosVitales sv = getSignosVitales();
        // Los medicos tienen mejor gestion del estres por su entrenamiento
        double nuevoEstres = Math.max(0, sv.estres() - 1); // reduccion de estres
        return new SignosVitales(
                sv.salud(), sv.moral() + 0.5, consumoDeOxigeno(),
                sv.hidratacion() - 1.5, sv.nutricion() - 2.0,
                sv.fatiga() + 2, nuevoEstres, sv.radiacion() + 0.05
        );
    }

    @Override
    public double consumoDeOxigeno() { return 1.8; }

    /** Maximo bono en Enfermeria (+30%); minimo en otros modulos. */
    @Override
    public double bonoEn(Modulo modulo) {
        return (modulo instanceof Enfermeria) ? 0.30 : 0.0;
    }

    public String getEspecializacion() { return especializacion; }
}
