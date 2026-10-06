package com.ares.colonia.config;

import com.ares.colonia.dominio.colonia.Colonia;
import com.ares.colonia.dominio.colonia.Inventario;
import com.ares.colonia.dominio.modelo.modulo.*;
import com.ares.colonia.dominio.modelo.recurso.*;
import com.ares.colonia.dominio.modelo.unidad.Dron;
import com.ares.colonia.dominio.modelo.unidad.RobotMinero;
import com.ares.colonia.dominio.modelo.unidad.Rover;
import com.ares.colonia.dominio.personal.*;

/**
 * ============================================================================
 * ARCHIVO: FabricaColonia.java
 * PAQUETE: com.ares.colonia.config
 * ============================================================================
 *
 * PROPOSITO Y FUNCION PRINCIPAL:
 * Fabrica que crea colonias predefinidas para el escenario de demostracion.
 * Aplica el patron Factory para encapsular la logica de construccion de una
 * colonia completa (modulos, tripulacion, unidades e inventario inicial) y
 * mantener el Main limpio y legible. Consulta ConfiguracionSimulacion para
 * los valores de stock y capacidad.
 *
 * ANOTACIONES Y ETIQUETAS UTILIZADAS: Ninguna.
 * HERENCIA E INTERFACES IMPLEMENTADAS: Ninguna.
 * PARTICIPACION EN EL MOTOR POR TURNOS:
 * Solo se usa en la configuracion inicial; no participa en el ciclo de turnos.
 * ============================================================================
 */
public class FabricaColonia {

    /**
     * Crea una colonia de demostracion completamente equipada con todos los
     * tipos de modulos, una tripulacion variada y un inventario inicial
     * configurado segun los parametros de ConfiguracionSimulacion.
     *
     * @param nombre nombre de la colonia
     * @param factorDificultad multiplicador de recursos (1.0 = normal)
     * @return la colonia construida y lista para la simulacion
     */
    public static Colonia crearColoniaDemostracion(String nombre, double factorDificultad) {
        Colonia colonia = new Colonia(nombre);

        // ----- INVENTARIO INICIAL -----
        inicializarInventario(colonia.getInventario(), factorDificultad);

        // ----- MODULOS -----
        colonia.agregarModulo(new Habitacional("Hab-Alpha", 10));
        colonia.agregarModulo(new Laboratorio("Lab-Ares"));
        colonia.agregarModulo(new Invernadero("Invernadero-1"));
        colonia.agregarModulo(new PlantaDeEnergia("Planta-Principal"));
        colonia.agregarModulo(new Hangar("Hangar-Delta"));
        colonia.agregarModulo(new Enfermeria("Enfermeria-Central"));
        colonia.agregarModulo(new CentroDeComunicaciones("CComun-Ares"));

        // ----- TRIPULACION -----
        colonia.agregarTripulante(new Ingeniero("Elena Vasquez", 34, 4));
        colonia.agregarTripulante(new Medico("Dr. Raj Patel", 45, "Cirujano"));
        colonia.agregarTripulante(new Cientifico("Sofia Chen", 31, "Astrofisica"));
        colonia.agregarTripulante(new Piloto("Marcus Torres", 38, 4));
        colonia.agregarTripulante(new Botanico("Lena Kovacs", 29, "Hidroponia"));
        colonia.agregarTripulante(new Ingeniero("James Okafor", 41, 3));

        // ----- UNIDADES -----
        colonia.agregarUnidad(new Rover("Rover-01", 50.0, 500.0));
        colonia.agregarUnidad(new Dron("Dron-Scout-A", 15.0, 0.75));
        colonia.agregarUnidad(new RobotMinero("Minero-Delta", 80.0, 10.0));

        return colonia;
    }

    /**
     * Inicializa el inventario de una colonia con los recursos base.
     *
     * @param inventario      el inventario a poblar
     * @param factorDificultad multiplicador de cantidad inicial
     */
    private static void inicializarInventario(Inventario inventario, double factorDificultad) {
        inventario.registrar(new RecursoRenovable(
                TipoRecurso.OXIGENO,
                ConfiguracionSimulacion.STOCK_INICIAL_OXIGENO * factorDificultad,
                ConfiguracionSimulacion.CAPACIDAD_MAX_OXIGENO,
                ConfiguracionSimulacion.UMBRAL_CRITICO_OXIGENO));

        inventario.registrar(new RecursoRenovable(
                TipoRecurso.AGUA,
                ConfiguracionSimulacion.STOCK_INICIAL_AGUA * factorDificultad,
                ConfiguracionSimulacion.CAPACIDAD_MAX_AGUA,
                ConfiguracionSimulacion.UMBRAL_CRITICO_AGUA));

        inventario.registrar(new RecursoRenovable(
                TipoRecurso.ENERGIA,
                ConfiguracionSimulacion.STOCK_INICIAL_ENERGIA * factorDificultad,
                ConfiguracionSimulacion.CAPACIDAD_MAX_ENERGIA,
                ConfiguracionSimulacion.UMBRAL_CRITICO_ENERGIA));

        inventario.registrar(new RecursoPerecedero(
                TipoRecurso.COMIDA,
                ConfiguracionSimulacion.STOCK_INICIAL_COMIDA * factorDificultad,
                ConfiguracionSimulacion.CAPACIDAD_MAX_COMIDA,
                ConfiguracionSimulacion.UMBRAL_CRITICO_COMIDA,
                0.02)); // 2% de degradacion por turno

        inventario.registrar(new RecursoFinito(
                TipoRecurso.REPUESTOS,
                ConfiguracionSimulacion.STOCK_INICIAL_REPUESTOS * factorDificultad,
                ConfiguracionSimulacion.CAPACIDAD_MAX_REPUESTOS,
                ConfiguracionSimulacion.UMBRAL_CRITICO_REPUESTOS));
    }
}
