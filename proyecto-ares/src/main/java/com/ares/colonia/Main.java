package com.ares.colonia;

import com.ares.colonia.config.ConfiguracionSimulacion;
import com.ares.colonia.config.FabricaColonia;
import com.ares.colonia.dominio.colonia.Colonia;
import com.ares.colonia.dominio.colonia.RedColonial;
import com.ares.colonia.servicio.EventoService;
import com.ares.colonia.servicio.MisionService;
import com.ares.colonia.servicio.SimulacionService;

/**
 * ============================================================================
 * ARCHIVO: Main.java
 * PAQUETE: com.ares.colonia
 * ============================================================================
 *
 * PROPOSITO Y FUNCION PRINCIPAL:
 * Clase de entrada (Entry Point) del sistema. Configura el escenario de
 * demostracion de Proyecto Ares: crea la red colonial, instancia colonias
 * preconfiguradas mediante la fabrica, inyecta los servicios necesarios
 * y lanza el motor de simulacion por turnos.
 *
 * ANOTACIONES Y ETIQUETAS UTILIZADAS: Ninguna.
 * HERENCIA E INTERFACES IMPLEMENTADAS: Ninguna.
 * PARTICIPACION EN EL MOTOR POR TURNOS:
 * Inicializa el SimulacionService y ejecuta un numero definido de sols.
 * ============================================================================
 */
public class Main {

    public static void main(String[] args) {
        System.out.println("==================================================");
        System.out.println("    INICIANDO PROYECTO ARES - SIMULACION MARTE    ");
        System.out.println("==================================================\n");

        // 1. Crear la red colonial principal
        RedColonial redAres = new RedColonial(ConfiguracionSimulacion.NOMBRE_RED);

        // 2. Crear colonias mediante el patron Factory y agregarlas a la red
        // Colonia 1: Dificultad Normal
        Colonia coloniaAlpha = FabricaColonia.crearColoniaDemostracion("Colonia Alpha", 
                ConfiguracionSimulacion.FACTOR_DIFICULTAD_NORMAL);
        
        // Colonia 2: Dificultad Dificil (menos recursos)
        Colonia coloniaBeta = FabricaColonia.crearColoniaDemostracion("Colonia Beta", 
                ConfiguracionSimulacion.FACTOR_DIFICULTAD_DIFICIL);

        redAres.agregarColonia(coloniaAlpha);
        redAres.agregarColonia(coloniaBeta);

        // 3. Inicializar los servicios de dominio (Inyeccion de dependencias manual)
        EventoService eventoService = new EventoService();
        MisionService misionService = new MisionService();
        SimulacionService simulacionService = new SimulacionService(redAres, eventoService, misionService);

        // 4. Ejecutar el escenario de demostracion
        System.out.println("\n--- CONFIGURACION COMPLETADA. INICIANDO MOTOR DE TURNOS ---\n");
        
        // Ejecuta la simulacion por el numero de sols definidos en config (ej. 10 sols)
        simulacionService.ejecutarSimulacion(ConfiguracionSimulacion.SOLS_ESCENARIO_DEMO);
        
        System.out.println("\n==================================================");
        System.out.println("    SIMULACION FINALIZADA                         ");
        System.out.println("==================================================");
    }
}
