package com.ares.colonia.dominio.personal;

import com.ares.colonia.dominio.modelo.modulo.Laboratorio;
import com.ares.colonia.dominio.modelo.modulo.Modulo;

/**
 * ============================================================================
 * ARCHIVO: Cientifico.java
 * PAQUETE: com.ares.colonia.dominio.personal
 * ============================================================================
 *
 * PROPOSITO Y FUNCION PRINCIPAL:
 * Especialidad de tripulante enfocada en la investigacion. Genera puntos
 * de investigacion adicionales y aporta el maximo bono al Laboratorio.
 * Los cientificos son imprescindibles para desbloquear tecnologias
 * que mejoran la eficiencia de la colonia.
 *
 * ANOTACIONES Y ETIQUETAS UTILIZADAS: Ninguna.
 * HERENCIA E INTERFACES IMPLEMENTADAS:
 * - Extiende de: Tripulante
 * PARTICIPACION EN EL MOTOR POR TURNOS:
 * Su bono se aplica en Laboratorio.generarPuntosInvestigacion().
 * ============================================================================
 */
public class Cientifico extends Tripulante {

    /** Campo de investigacion principal del cientifico. */
    private final String campoInvestigacion;

    public Cientifico(String nombre, int edad, String campoInvestigacion) {
        super(nombre, edad);
        this.campoInvestigacion = campoInvestigacion;
    }

    @Override
    public SignosVitales calcularVitalidad() {
        SignosVitales sv = getSignosVitales();
        // Los cientificos acumulan mas estres intelectual
        return new SignosVitales(
                sv.salud(), sv.moral(), consumoDeOxigeno(),
                sv.hidratacion() - 1.5, sv.nutricion() - 2.0,
                sv.fatiga() + 2, sv.estres() + 3, sv.radiacion() + 0.05
        );
    }

    @Override
    public double consumoDeOxigeno() { return 1.8; }

    /** Bono alto en Laboratorio (+25%). */
    @Override
    public double bonoEn(Modulo modulo) {
        return (modulo instanceof Laboratorio) ? 0.25 : 0.0;
    }

    public String getCampoInvestigacion() { return campoInvestigacion; }
}
