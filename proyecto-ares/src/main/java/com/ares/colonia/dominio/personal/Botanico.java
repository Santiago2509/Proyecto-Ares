package com.ares.colonia.dominio.personal;

import com.ares.colonia.dominio.modelo.modulo.Invernadero;
import com.ares.colonia.dominio.modelo.modulo.Modulo;

/**
 * ============================================================================
 * ARCHIVO: Botanico.java
 * PAQUETE: com.ares.colonia.dominio.personal
 * ============================================================================
 *
 * PROPOSITO Y FUNCION PRINCIPAL:
 * Especialidad de tripulante con conocimientos en botanica marciana e
 * hidroponia. Aumenta significativamente la produccion de comida en el
 * Invernadero y ayuda a mantener el suministro de oxigeno.
 *
 * ANOTACIONES Y ETIQUETAS UTILIZADAS: Ninguna.
 * HERENCIA E INTERFACES IMPLEMENTADAS:
 * - Extiende de: Tripulante
 * PARTICIPACION EN EL MOTOR POR TURNOS:
 * Su bono se aplica en Invernadero.calcularEficiencia() y aumenta la
 * produccion de comida y oxigeno de la colonia.
 * ============================================================================
 */
public class Botanico extends Tripulante {

    /** Especializacion en tipo de cultivo marciano. */
    private final String especializacionCultivo;

    public Botanico(String nombre, int edad, String especializacionCultivo) {
        super(nombre, edad);
        this.especializacionCultivo = especializacionCultivo;
    }

    @Override
    public SignosVitales calcularVitalidad() {
        SignosVitales sv = getSignosVitales();
        // Los botanicos tienen buena salud mental por trabajar con plantas (bono moral)
        double nuevaMoral = Math.min(100, sv.moral() + 1.0);
        return new SignosVitales(
                sv.salud(), nuevaMoral, consumoDeOxigeno(),
                sv.hidratacion() - 1.5, sv.nutricion() - 2.0,
                sv.fatiga() + 2, sv.estres() + 1, sv.radiacion() + 0.05
        );
    }

    @Override
    public double consumoDeOxigeno() { return 1.8; }

    /** Maximo bono en Invernadero (+25%). */
    @Override
    public double bonoEn(Modulo modulo) {
        return (modulo instanceof Invernadero) ? 0.25 : 0.0;
    }

    public String getEspecializacionCultivo() { return especializacionCultivo; }
}
