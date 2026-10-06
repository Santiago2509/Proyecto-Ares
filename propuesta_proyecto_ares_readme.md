# PROPUESTA DE PROYECTO FINAL: Proyecto Ares
## Sistema de Administración de Colonias Humanas en Marte

**Asignatura:** Programación Orientada a Objetos  
**Tecnología:** Java · Backend  
**Integrantes:**
- Edwin Santiago Urbina Ruiz
- Daniel Eduardo Perez  

**Fecha:** Octubre de 2026

---

## 1. Introducción
La exploración y el eventual asentamiento humano en Marte plantean un problema de gestión sin precedentes: un grupo de personas debe sobrevivir en un entorno hostil donde cada recurso es limitado, cada equipo puede fallar y cada decisión afecta la vida de la tripulación. Administrar una colonia de ese tipo exige coordinar infraestructura, personal, suministros y emergencias de manera simultánea.

Proyecto Ares es un sistema backend desarrollado en Java que simula la administración de una red de colonias humanas en Marte, por turnos. El sistema modela los módulos de cada colonia (hábitats, laboratorios, invernaderos, plantas de energía, hangares, enfermerías y centros de comunicaciones), a su tripulación con su vitalidad individual, los recursos vitales que se producen y consumen en cada turno, las unidades robóticas y los eventos y misiones que ponen a prueba la capacidad de respuesta de la colonia.

El dominio fue elegido porque presenta jerarquías naturales: un invernadero y una planta de energía son ambos módulos, pero producen, consumen y se desgastan de formas distintas; un médico y un piloto son ambos tripulantes, pero aportan y resisten de manera diferente. Esto permite aplicar los pilares de la programación orientada a objetos (abstracción, herencia, polimorfismo y encapsulamiento) como consecuencia directa del problema y no como un ejercicio forzado. El proyecto se desarrolla únicamente como backend, con el propósito de concentrar el esfuerzo en el diseño del dominio y en la aplicación de buenas prácticas de ingeniería de software.

## 2. Objetivo general
Diseñar e implementar en Java un sistema backend para la administración de una red de colonias humanas en Marte, aplicando de forma rigurosa los principios de la programación orientada a objetos —clases abstractas, polimorfismo, jerarquía de excepciones e interfaces— junto con buenas prácticas de diseño y calidad de software.

## 3. Objetivos específicos
- Modelar el dominio mediante jerarquías de clases abstractas (`Modulo`, `Tripulante`, `Recurso`, `Unidad`, `Evento`, `Mision` y `Rol`) que encapsulen el comportamiento común y obliguen a las subclases a definir su comportamiento particular.
- Aplicar polimorfismo en el cálculo de la eficiencia de los módulos, la vitalidad de los tripulantes, el efecto de los eventos y la ejecución de las misiones, de modo que el motor de simulación opere sin conocer el tipo concreto de cada objeto.
- Diseñar una jerarquía de excepciones propia que represente de forma precisa los errores de negocio (recursos, módulos, tripulación, misiones, unidades y seguridad).
- Gestionar varias colonias dentro de una red colonial, permitiendo transferencias de recursos, misiones de rescate entre colonias y reportes comparativos.
- Implementar un esquema de roles y permisos (líder de colonia, administrador de colonia, administrador general y tripulante) con control de acceso mediante anotaciones personalizadas.
- Registrar y calcular la vitalidad de cada tripulante en cada turno, generando alertas cuando su estado sea crítico.
- Aplicar buenas prácticas: arquitectura en capas, dominio rico, validación en constructores, programación contra interfaces, patrones de diseño, registro de eventos (*logging*) y pruebas unitarias con JUnit.

## 4. Alcance

### 4.1 Incluye
- Simulación por turnos de una red de colonias, cada turno equivalente a un sol marciano.
- Gestión de módulos, tripulantes, recursos, unidades, eventos y misiones.
- Roles y permisos con acceso diferenciado a la información y a las operaciones.
- Vitalidad individual de cada tripulante y reportes por colonia y por red.
- Persistencia de la partida en archivos JSON.
- Pruebas unitarias de las reglas de negocio y de las excepciones.

### 4.2 No incluye
- Interfaz gráfica: el sistema se entrega como backend, con un escenario de demostración por turnos que genera reportes.

## 5. Descripción general del sistema
La `RedColonial` es el contenedor principal del sistema y agrupa a una o varias Colonias. Cada colonia posee sus propios módulos, tripulantes, inventario de recursos, unidades y misiones activas. Los eventos pueden ser locales (afectan a una colonia) o globales (afectan a toda la red).

```text
RedColonial
 └── Colonia (varias)
      ├── Modulos            → Modulo (abstracta)
      ├── Tripulantes        → Tripulante (abstracta), cada uno con un Rol
      ├── Inventario         → Recursos
      ├── Unidades           → Unidad (abstracta)
      └── Misiones activas   → Mision (abstracta)
Eventos → Evento (abstracta), locales o globales
```

### 5.1 Ciclo de cada turno
1. **Eventos:** se generan y se aplican los eventos del turno.
2. **Módulos:** producen y consumen recursos, y se calcula su eficiencia y su desgaste.
3. **Vitalidad:** se recalculan los signos vitales de cada tripulante.
4. **Misiones:** avanzan las misiones en curso y se resuelven las finalizadas.
5. **Alertas:** se notifican los estados críticos mediante el patrón *Observer*.
6. **Reportes:** se actualiza el estado de cada colonia y de la red.

## 6. Estructura del proyecto
```text
com.ares.colonia
├── dominio
│   ├── colonia       → RedColonial, Colonia
│   ├── modelo        → Modulo, Recurso, Unidad, Mision, Evento y subclases
│   ├── personal      → Tripulante y especialidades, SignosVitales, EstadoVital
│   ├── seguridad     → Rol, LiderDeColonia, AdministradorDeColonia,
│   │                   AdministradorGeneral, Permiso, @RequierePermiso
│   ├── interfaces    → Reparable, Productor, Consumidor, Observable
│   ├── etiquetas     → @Critico, @Experimental, @Auditable, enum Etiqueta
│   └── excepciones   → SistemaColonialException y su jerarquía
├── servicio          → ColoniaService, SimulacionService, MisionService, EventoService
├── repositorio       → Repositorio<T> (interfaz genérica) + implementación en JSON
├── dto               → records de entrada y salida
├── mapper            → conversión entidad ↔ DTO
├── config            → fábricas, dificultad y parámetros
└── Main              → escenario de demostración
```

## 7. Clases del sistema

### 7.1 Colonias
| Clase | Responsabilidad |
|---|---|
| `RedColonial` | Contiene todas las colonias. Coordina los turnos, las transferencias de recursos entre colonias, los eventos globales y los reportes comparativos. |
| `Colonia` | Administra sus módulos, tripulantes, inventario, unidades y misiones. Aplica las reglas de producción, consumo y vitalidad en cada turno. |

### 7.2 Módulos (clase abstracta `Modulo`)
La clase base define identificador, nombre, nivel de desgaste, estado (patrón *State*) y etiquetas, y obliga a cada subclase a implementar su propio comportamiento.

| Clase | Función | Consume | La eficiencia depende de |
|---|---|---|---|
| `Habitacional` | Aloja a la tripulación y sostiene su moral. | Energía, oxígeno y agua por ocupante | Nivel de ocupación (cae con la sobrepoblación) |
| `Laboratorio` | Genera puntos de investigación. | Mucha energía | Científicos asignados y energía disponible |
| `Invernadero` | Produce comida y oxígeno. | Agua y energía | Botánicos asignados y agua disponible |
| `PlantaDeEnergia` | Produce la energía de la colonia. | Repuestos | Ingenieros asignados y desgaste (es el módulo que más se gasta) |
| `Hangar` | Aloja y mantiene las unidades. | Repuestos y energía | Pilotos e ingenieros |
| `Enfermeria` | Recupera la vitalidad de los tripulantes. | Energía y repuestos | Médicos presentes |
| `CentroDeComunicaciones` | Mantiene contacto con la Tierra y con otras colonias. | Poca energía | Desgaste (si cae, hay aislamiento y baja la moral) |

### 7.3 Tripulantes (clase abstracta `Tripulante`)
Cada tripulante posee signos vitales (salud, moral, oxígeno consumido, hidratación, nutrición, fatiga, estrés y radiación acumulada), un estado vital calculated y un rol dentro de la colonia.

| Clase | Bono en | Particularidad |
|---|---|---|
| `Ingeniero` | `PlantaDeEnergia` y `Hangar` | Puede reparar módulos y unidades. |
| `Medico` | `Enfermeria` | Mejora la recuperación de la vitalidad de otros tripulantes. |
| `Cientifico` | `Laboratorio` | Genera puntos de investigación. |
| `Piloto` | `Hangar` y misiones | Acumula más radiación al operar fuera de la colonia. |
| `Botanico` | `Invernadero` | Aumenta la producción de comida. |

### 7.4 Recursos (clase abstracta `Recurso`)
Los recursos se agrupan en tres familias según su comportamiento. El inventario de cada colonia los indexa mediante el enumerado `TipoRecurso`, y todos se producen y consumen en cada turno con umbrales críticos que disparan alertas.

| Clase | Recursos | Comportamiento |
|---|---|---|
| `RecursoRenovable` | Energía, oxígeno, agua | Se regeneran gracias a los módulos productores. |
| `RecursoPerecedero` | Comida | Se pierde con el paso del tiempo si no se consume. |
| `RecursoFinito` | Repuestos | Solo llegan mediante suministros o minería. |

### 7.5 Unidades (clase abstracta `Unidad`)
Las unidades implementan la interfaz `Reparable` y se asignan a misiones de exploración o de reparación.

| Clase | Fortaleza | Debilidad |
|---|---|---|
| `Rover` | Explora terreno y tiene buena capacidad de carga. | Lento y con alto consumo de energía. |
| `Dron` | Rápido; detecta eventos y mapea el terreno. | Corto alcance y frágil. |
| `RobotMinero` | Extrae agua y repuestos. | Lento y con mucho desgaste. |

### 7.6 Eventos (clase abstracta `Evento`)
Cada evento implementa `aplicar(Colonia)` y `esGlobal()`, y afecta de forma polimórfica a módulos, tripulantes y recursos.

| Clase | Alcance | Efecto |
|---|---|---|
| `TormentaDeArena` | Global | Reduce la energía solar, desgasta invernaderos y hangares y daña unidades. |
| `FallaElectrica` | Local | Baja la planta de energía y arrastra a los demás módulos. |
| `Meteorito` | Local | Daña un módulo al azar y pone en riesgo a los tripulantes que están dentro. |
| `BrotedeEnfermedad` | Local | Reduce la vitalidad y es contagioso; la enfermería lo mitiga. |
| `LlegadaDeSuministros` | Local o global | Suma recursos (requiere hangar y comunicaciones operativos). |
| `SenalMisteriosa` | Global | Abre una misión de investigación y afecta la moral. |

### 7.7 Misiones (clase abstracta `Mision`)
Cada misión implementa `validarRequisitos(Colonia)` y `ejecutar()`, y tiene duración, riesgo y recompensa.

| Clase | Requiere | Resultado |
|---|---|---|
| `Exploracion` | Rover o dron y piloto | Descubre recursos o eventos. |
| `ConstruccionDeModulo` | Ingenieros y repuestos | Agrega un nuevo módulo a la colonia. |
| `Investigacion` | Laboratorio y científicos | Desbloquea tecnologías. |
| `Rescate` | Unidades y médicos (puede ser entre colonias) | Salva tripulantes o módulos. |
| `Terraformacion` | Muchos recursos y larga duración | Objetivo final de victoria. |

### 7.8 Roles y permisos (clase abstracta `Rol`)
Cada tripulante tiene un rol por composición, de modo que un ingeniero puede ser a la vez líder de colonia sin alterar la jerarquía de tripulantes. El acceso se controla con la anotación `@RequierePermiso(Permiso.X)` sobre los métodos de servicio, la cual se evalúa por reflexión y lanza `AccesoDenegadoException` si el rol no posee el permiso.

| Rol | Alcance | Responsabilidades |
|---|---|---|
| `LiderDeColonia` | Toda su colonia | Declarar emergencia, aprobar misiones, asignar tripulantes y ver la vitalidad de todos. |
| `AdministradorDeColonia` | Toda su colonia | Gestionar módulos y recursos, registrar tripulantes y generar reportes. |
| `AdministradorGeneral` | Toda la red | Ver y comparar todas las colonias y aprobar transferencias. |
| `RolTripulante` | Solo lo propio | Consultar su propia vitalidad y sus tareas asignadas. |

### 7.9 Clases abstractas y sus métodos
| Clase abstracta | Métodos abstractos |
|---|---|
| `Modulo` | `calcularEficiencia()`, `consumoPorTurno()`, `desgastePorTurno()` |
| `Tripulante` | `calcularVitalidad()`, `consumoDeOxigeno()`, `bonoEn(Modulo)` |
| `Recurso` | `actualizarPorTurno()` |
| `Unidad` | `ejecutarTarea()`, `calcularConsumo()` |
| `Evento` | `aplicar(Colonia)`, `esGlobal()` |
| `Mision` | `validarRequisitos(Colonia)`, `ejecutar()` |
| `Rol` | `permisos()`, `puede(Permiso)` |

## 8. Objetos de valor, enumerados e interfaces
| Elemento | Tipo | Descripción |
|---|---|---|
| `SignosVitales` | `record` | Salud, moral, oxígeno consumido, hidratación, nutrición, fatiga, estrés y radiación de un tripulante. |
| `EstadoVital` | `enum` | OPTIMO, ESTABLE, CRITICO y FALLECIDO, calculado a partir de los signos vitales. |
| `TipoRecurso` | `enum` | Clave del inventario: oxígeno, agua, energía, comida y repuestos. |
| `Permiso` | `enum` | Operaciones autorizables, como VER_VITALIDAD o APROBAR_MISION. |
| `Etiqueta` | `enum` | Marcas para filtrar entidades, como PELIGROSO, ESENCIAL o REPARABLE. |
| `Reparable` | `interface` | Contrato para módulos y unidades que pueden ser reparados. |
| `Productor` / `Consumidor` | `interface` | Contratos para quienes producen o consumen recursos en cada turno. |
| `Observable` | `interface` | Soporta el sistema de alertas (patrón Observer). |
| `Inventario<T extends Recurso>` | clase genérica | Almacena y controla los recursos de una colonia. |
| `Repositorio<T>` | `interface` genérica | Abstrae la persistencia de las entidades. |

## 9. Jerarquía de excepciones
Todas las excepciones heredan de `SistemaColonialException`, lo que permite capturarlas de forma general o específica y traducirlas en un único punto de manejo de errores.

```text
SistemaColonialException
├── ColoniaException
│   ├── ColoniaNoEncontradaException
│   └── TransferenciaInvalidaException
├── RecursoException
│   ├── RecursoAgotadoException
│   └── CapacidadExcedidaException
├── ModuloException
│   ├── ModuloAveriadoException
│   └── ModuloIncompatibleException
├── TripulacionException
│   ├── TripulanteCriticoException
│   ├── TripulanteFallecidoException
│   └── MoralCriticaException
├── MisionException
│   ├── MisionImposibleException
│   └── EquipoInsuficienteException
├── UnidadException
│   └── UnidadNoDisponibleException
└── SeguridadException
    ├── AccesoDenegadoException
    └── PermisoInsuficienteException
```
REGLA DE DOCUMENTACIÓN Y COMENTARIOS OBLIGATORIA
En todos y cada uno de los archivos Java que generes o modifiques, debes incluir al inicio un bloque de documentación exhaustivo con la siguiente plantilla exacta:

Java
/**
 * ============================================================================
 * ARCHIVO: [NombreDelArchivo.java]
 * PAQUETE: [com.ares.colonia...]
 * ============================================================================
 * 
 * 📌 PROPÓSITO Y FUNCIÓN PRINCIPAL:
 * Explica detalladamente qué hace esta clase, interfaz o enum dentro del sistema
 * Proyecto Ares y cuál es su responsabilidad específica en el dominio.
 * 
 * 🏷️ ANOTACIONES Y ETIQUETAS UTILIZADAS:
 * - Enumera y explica la función de cada anotación presente (@RequierePermiso,
 *   @Critico, @Experimental, @Auditable, etc.). Si no utiliza, indicarlo.
 * 
 * 🔗 HERENCIA E INTERFACES IMPLEMENTADAS:
 * - Extiende de: [ClasePadre / Ninguna]
 * - Implementa: [Interfaces / Ninguna]
 * 
 * 🔄 PARTICIPACIÓN EN EL MOTOR POR TURNOS:
 * Explica cómo actúa o muta el estado de este objeto cuando se avanza un Sol (turno).
 * ============================================================================
 */
Requisitos adicionales de código:

Métodos y Constructores: Comenta cada método indicando su responsabilidad, parámetros (@param), retorno (@return) y las excepciones de negocio que puede lanzar (@throws).

Lógica interna: Añade comentarios de línea explicando las reglas de negocio clave aplicadas en bloques de código complejos.