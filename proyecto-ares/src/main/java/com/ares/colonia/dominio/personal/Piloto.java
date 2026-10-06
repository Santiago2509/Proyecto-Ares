package com.ares.colonia.dominio.personal;

import com.ares.colonia.dominio.modelo.modulo.Hangar;
import com.ares.colonia.dominio.modelo.modulo.Modulo;

/**
 * ============================================================================
 * ARCHIVO: Piloto.java
 * PAQUETE: com.ares.colonia.dominio.personal
 * ============================================================================
 *
 * PROPOSITO Y FUNCION PRINCIPAL:
 * Especialidad de tripulante especializada en operacion de unidades y
 * vehiculos. Aporta bono en el Hangar y en misiones de exploracion.
 * Acumula mas radiacion que el resto al operar fuera de la colonia.
 *
 * ANOTACIONES Y ETIQUETAS UTILIZADAS: Ninguna.
 * HERENCIA E INTERFACES IMPLEMENTADAS:
 * - Extiende de: Tripulante
 * PARTICIPACION EN EL MOTOR POR TURNOS:
 * En misiones de exploracion, la radiacion acumulada sube mas rapido.
 * ============================================================================
 */
public class Piloto extends Tripulante {

    /** Horas de vuelo/operacion acumuladas (experiencia). */
    private double horasDeOperacion;

    public Piloto(String nombre, int edad, double horasDeOperacion) {
        super(nombre, edad);
        this.horasDeOperacion = Math.max(0, horasDeOperacion);
    }

    @Override
    public SignosVitales calcularVitalidad() {
        SignosVitales sv = getSignosVitales();
        // Los pilotos acumulan mas radiacion al operar fuera de la colonia
        double radiacionExtra = esOperativo() ? 0.3 : 0.1;
        return new SignosVitales(
                sv.salud(), sv.moral(), consumoDeOxigeno(),
                sv.hidratacion() - 2.0, sv.nutricion() - 2.0,
                sv.fatiga() + 4, sv.estres() + 2, sv.radiacion() + radiacionExtra
        );
    }

    @Override
    public double consumoDeOxigeno() { return 2.2; } // mayor consumo por operaciones exteriores

    /** Bono en Hangar (+20%) y en misiones (calculado por MisionService). */
    @Override
    public double bonoEn(Modulo modulo) {
        return (modulo instanceof Hangar) ? 0.20 : 0.0;
    }

    /** Incrementa las horas de operacion al completar una mision. */
    public void agregarHorasOperacion(double horas) {
        this.horasDeOperacion += horas;
    }

    public double getHorasDeOperacion() { return horasDeOperacion; }
}
