# Ejercicio 3.3 — Depuración avanzada y pruebas con JUnit

Módulo 3. Integración de desarrollos en Java · Tema 3.3. Depuración y pruebas · Apartado 3.3.1. Depuración y JUnit

Duración estimada: 2 horas.

## 1. Introducción

En el tema 1.2 se vieron los controles básicos de depuración: `F5`, `F9`, `F10`, `F11`. Este apartado profundiza en las herramientas que más tiempo ahorran al buscar un error:

- breakpoints condicionales y logpoints;
- inspección de variables y expresiones;
- pila de llamadas;
- configuraciones de lanzamiento.

También se trabajan las **pruebas unitarias con JUnit 5** desde la vista **Testing** de VS Code: ejecutarlas, interpretar los fallos y depurarlas.

El proyecto es un Gestor de tareas en Maven con **20 tareas de ejemplo siempre iguales**. Así, cada valor que observes en el depurador es predecible y se puede comprobar con este README.

## 2. Objetivos

Al terminar serás capaz de:

- Usar breakpoints normales, condicionales, por número de pasadas y logpoints.
- Inspeccionar **Variables**, **Watch** y **Call Stack**, y evaluar expresiones en la **Debug Console**.
- Moverte con *Step Over*, *Step Into*, *Step Out* y *Continue*.
- Crear y usar configuraciones en `launch.json`, incluida la conexión a un proceso Maven.
- Escribir e interpretar pruebas JUnit 5 y ejecutarlas, individualmente o todas, desde VS Code y con Maven.
- Depurar una prueba y localizar la causa de un fallo.

## 3. Conceptos fundamentales

### Tipos de breakpoint

| Tipo | Se detiene… | Uso típico |
|---|---|---|
| **Breakpoint** | Siempre que se llega a la línea | Inspeccionar un punto concreto |
| **Condicional** (*Expression*) | Solo si una expresión Java es verdadera, por ejemplo `tarea.getId() == 14` | Bucles con muchas iteraciones, un caso concreto |
| **Por pasadas** (*Hit Count*) | A partir de la enésima vez que se llega a la línea | "Algo falla en la décima iteración" |
| **Logpoint** (*Log Message*) | **Nunca**: escribe un mensaje en la Debug Console y continúa | Trazas temporales sin tocar el código ni recompilar |
| **De excepción** | Cuando se lanza una excepción, capturada o no | Localizar el origen de una excepción |

### Vistas de depuración

En la vista **Run and Debug** (`Ctrl + Shift + D`), durante una sesión:

| Sección | Contenido |
|---|---|
| **Variables** | Variables locales, parámetros y `this` del punto actual. Se pueden desplegar objetos y modificar valores. |
| **Watch** | Expresiones que tú añades y se reevalúan en cada paso |
| **Call Stack** | Cadena de llamadas que ha llevado hasta el punto actual. Clic en un nivel para ver sus variables. |
| **Breakpoints** | Lista de breakpoints, con casillas para activarlos o desactivarlos, y las opciones de excepciones |

La **Debug Console** del panel inferior evalúa expresiones Java en el contexto del punto detenido y muestra los mensajes de los logpoints.

### JUnit 5

| Elemento | Para qué sirve |
|---|---|
| `@Test` | Marca un método como prueba |
| `@BeforeEach` | Se ejecuta antes de cada prueba: prepara datos limpios |
| `@DisplayName` | Nombre legible en los informes y en la vista Testing |
| `@ParameterizedTest` y `@CsvSource` | La misma prueba con varios juegos de datos |
| `@Disabled` | Desactiva una prueba, que aparece como omitida (*skipped*) |
| `assertEquals`, `assertTrue` | Comprueban un resultado |
| `assertThrows` | Comprueba que se lanza una excepción |
| `assertAll` | Agrupa comprobaciones y las informa todas, aunque falle alguna |

Una prueba puede terminar de tres formas:

- **Correcta.**
- **Fallo** (*failure*): una aserción no se cumple.
- **Error**: se lanza una excepción no esperada.

### Proyecto y datos de ejemplo

```text
proyecto/
├── pom.xml                                  JUnit 5 (BOM), Surefire, exec-maven-plugin
├── src/main/java/curso/java/
│   ├── EstadoTarea.java                     regla de negocio: puedeCambiarA()
│   ├── Tarea.java                           id, título, prioridad, horas, estado
│   ├── ServicioTareas.java                  carga de datos, cambios de estado y cálculos
│   └── MainDepuracion.java                  programa para depurar
└── src/test/java/curso/java/
    ├── ServicioTareasTest.java              11 pruebas (5 + 6 parametrizadas)
    └── EscenarioDidacticoTest.java          1 prueba desactivada que falla a propósito
```

Las tareas de ejemplo siguen estas reglas, de modo que todos los valores se pueden predecir:

- la tarea *i* tiene prioridad `(i % 5) + 1` y `(i % 4) + 1` horas;
- las tareas múltiplo de 3 empiezan completadas;
- `MainDepuracion` pasa la tarea 1 a `EN_PROGRESO` y completa la 2.

**Regla de negocio:** una tarea `COMPLETADA` no puede cambiar de estado. Cualquier otra puede pasar a un estado distinto del actual.

## 4. Preparación del entorno

Requisitos:

- JDK 21;
- Maven 3.6.3 o posterior (apartado 3.2);
- Extension Pack for Java, que incluye **Debugger for Java** y **Test Runner for Java**.

En la raíz del repositorio del curso, ejecuta `git status` (sin cambios pendientes) y `git pull --ff-only`. Después copia el proyecto **fuera del repositorio del curso**:

**Windows (PowerShell):**

```powershell
New-Item -ItemType Directory -Force "$HOME/practicas-java" | Out-Null
Copy-Item -Recurse modulo-03-java/tema-3.3-debugging-testing/apartado-3.3.1-depuracion-junit/proyecto "$HOME/practicas-java/depuracion-3.3"
code "$HOME/practicas-java/depuracion-3.3"
```

**Linux o WSL (Bash):**

```bash
mkdir -p ~/practicas-java
cp -r modulo-03-java/tema-3.3-debugging-testing/apartado-3.3.1-depuracion-junit/proyecto ~/practicas-java/depuracion-3.3
code ~/practicas-java/depuracion-3.3
```

Comprueba que todo funciona antes de empezar:

```bash
mvn clean test
```

**Resultado esperado:**

```text
[WARNING] Tests run: 1, Failures: 0, Errors: 0, Skipped: 1, ... -- in curso.java.EscenarioDidacticoTest
[INFO] Tests run: 12, Failures: 0, Errors: 0, Skipped: 1
[INFO] BUILD SUCCESS
```

La prueba omitida es el escenario didáctico, desactivado a propósito.

Para **restaurar** el proyecto en cualquier momento, borra la copia y vuelve a copiarla.

## 5. Herramientas de VS Code utilizadas

| Acción | Cómo |
|---|---|
| Breakpoint | Clic en el margen izquierdo, junto al número de línea, o `F9` |
| Breakpoint condicional o logpoint | Clic derecho en el margen > **Add Conditional Breakpoint...** o **Add Logpoint...**. En el cuadro, el desplegable elige **Expression**, **Hit Count** o **Log Message**. |
| Editar un breakpoint | Clic derecho sobre el punto rojo > **Edit Breakpoint** |
| Breakpoints de excepción | Sección **Breakpoints** > casillas **Caught Exceptions** / **Uncaught Exceptions** |
| Iniciar depuración | **Debug** sobre `main`, `F5`, o configuración de `launch.json` en Run and Debug |
| Pasos | Barra de depuración: *Continue* (`F5`), *Step Over* (`F10`), *Step Into* (`F11`), *Step Out* (`Shift + F11`), *Restart*, *Stop* |
| Añadir una expresión vigilada | Sección **Watch** > **+**, o clic derecho sobre una variable > **Add to Watch** |
| Evaluar una expresión | Escribir en la **Debug Console**, o pasar el ratón sobre una variable |
| Vista **Testing** | Icono de matraz de la Activity Bar: árbol de clases y pruebas, con botones **Run Tests** y **Debug Tests** |
| Ejecutar una prueba desde el editor | Icono ▷ junto a cada `@Test` y cada clase. Clic derecho sobre él > **Debug Test**. |
| Resultados | **Test Results** en el panel inferior, con el mensaje de error y la pila |

## 6. Explicaciones técnicas

### `launch.json`

Para depurar con opciones fijas se crea `.vscode/launch.json` en el proyecto. `ejemplos/launch.json`, en la carpeta de este apartado, contiene dos configuraciones:

- **Depurar MainDepuracion** (`request: launch`): lanza la clase principal del proyecto `gestor-tareas-depuracion`, el `artifactId` del `pom.xml`.
- **Adjuntar a las pruebas de Maven (puerto 5005)** (`request: attach`): se conecta a una JVM que ya está esperando un depurador.

### Depurar una aplicación Maven

- **Desde VS Code**, **Debug** o `F5` usan el *classpath* que la extensión obtiene de `pom.xml`. Es la forma habitual.
- **Para depurar exactamente lo que ejecuta Maven**, se lanza Maven en modo de espera y se adjunta el depurador:

  ```bash
  mvn test -Dmaven.surefire.debug
  ```

  Maven muestra `Listening for transport dt_socket at address: 5005` y se queda esperando. En VS Code, lanza la configuración **Adjuntar a las pruebas de Maven (puerto 5005)**: las pruebas se ejecutan y se detienen en tus breakpoints. En PowerShell el comando es idéntico.

## 7. Práctica guiada

Antes de empezar, copia la configuración de ejemplo al proyecto:

1. Crea la carpeta `.vscode` en la raíz de tu copia.
2. Crea dentro el archivo `launch.json`.
3. Pega el contenido de `ejemplos/launch.json`, que está en la carpeta de este apartado en el repositorio del curso.

`.vscode/` es configuración personal: el `.gitignore` del proyecto la excluye.

### Paso 1. Ejecutar las pruebas desde VS Code

1. Abre la vista **Testing**. Espera a que aparezcan `ServicioTareasTest` y `EscenarioDidacticoTest`.
2. Pulsa **Run Tests** en la parte superior de la vista para ejecutarlas todas.
3. Despliega `transicionesDeEstado`: aparece un resultado por cada línea de `@CsvSource`.
4. Abre `ServicioTareasTest.java` y pulsa el ▷ junto a `calculaLasHorasPendientes` para ejecutar solo esa prueba.

**Resultado esperado:** todas en verde salvo `reabrirUnaTareaCompletada`, que aparece como omitida. Las pruebas muestran su `@DisplayName`, por ejemplo *Suma las horas de las tareas no completadas*.

### Paso 2. Colocar un breakpoint

1. En `ServicioTareas.java`, pon un breakpoint en la **línea 63**: `total += tarea.getHorasEstimadas();`, dentro de `calcularHorasPendientes()`.
2. En la vista Run and Debug, elige **Depurar MainDepuracion** y pulsa `F5`.

**Resultado esperado:** la ejecución se detiene en la línea 63, resaltada.

### Paso 3. Inspeccionar variables y la pila de llamadas

1. En **Variables**: `total` vale `0` y `tarea` es la tarea 1. Despliega `tarea` para ver `id`, `titulo`, `prioridad`, `horasEstimadas` y `estado` (`EN_PROGRESO`).
2. En **Call Stack** aparecen `ServicioTareas.calcularHorasPendientes` y, debajo, `MainDepuracion.main`. Haz clic en `main` para ver sus variables (`servicio`) y vuelve al nivel superior.
3. En **Watch**, añade `total + tarea.getHorasEstimadas()`.
4. En la **Debug Console**, escribe `tareas.size()` y pulsa `Enter`.

**Resultado esperado:**

- en **Watch**, la expresión vale `2`;
- en la Debug Console, `tareas.size()` devuelve `20`.

### Paso 4. Ejecutar paso a paso

1. Pulsa `F5` (*Continue*): se detiene en la siguiente tarea pendiente, la 4, con `total` = `2`.
2. Quita el breakpoint de la línea 63 con un clic en el punto rojo y pon otro en `MainDepuracion.java`, **línea 15**: `int horas = servicio.calcularHorasPendientes();`.
3. Pulsa **Restart** en la barra de depuración. Se detiene en la línea 15.
4. Pulsa `F11` (*Step Into*): entras en `calcularHorasPendientes()`.
5. Pulsa `F10` (*Step Over*) varias veces y observa cómo cambian `tarea` y `total`.
6. Pulsa `Shift + F11` (*Step Out*): vuelves a `main`, con el método terminado.
7. Pulsa `F10` una vez más: en **Variables** aparece `horas` = `30`.
8. Pulsa `F5` hasta que el programa termine.

**Resultado esperado:** la salida del programa en la terminal es:

```text
Tareas: 20
Horas pendientes: 30
Porcentaje completado: 35.0 %
Tarea más prioritaria: #5 Tarea 5 (prioridad 1, 2 h, PENDIENTE)
```

### Paso 5. Añadir un breakpoint condicional

1. Quita los breakpoints anteriores (sección **Breakpoints** > icono **Remove All Breakpoints**).
2. En `ServicioTareas.java`, línea 63, haz clic derecho en el margen > **Add Conditional Breakpoint...** Elige **Expression** y escribe `tarea.getId() == 14`. Pulsa `Enter`.
3. Depura de nuevo **Depurar MainDepuracion**.

**Resultado esperado:** la ejecución se detiene **una sola vez**, con `tarea` = tarea 14 y `total` = `19`. Es la suma de las horas pendientes de las tareas 1 a 13, salvo las completadas.

Prueba también **Hit Count**: edita el breakpoint (clic derecho > **Edit Breakpoint**), cambia a **Hit Count** y escribe `5`. Se detiene en la quinta pasada, la tarea 8.

¿Por qué no `tarea.getId() == 15`? La tarea 15 es múltiplo de 3 y está completada, así que nunca llega a la línea 63. Un breakpoint condicional sobre una línea que no se ejecuta nunca se detiene.

### Paso 6. Añadir un logpoint

1. Quita el breakpoint condicional.
2. En la línea 63, clic derecho > **Add Logpoint...** y escribe:

   ```text
   Tarea {tarea.getId()}: +{tarea.getHorasEstimadas()} h, acumulado {total}
   ```

   Lo que va entre llaves se evalúa como expresión Java.
3. Depura **Depurar MainDepuracion**.

**Resultado esperado:** el programa **no se detiene**. La Debug Console muestra 13 líneas, una por tarea pendiente, empezando por:

```text
Tarea 1: +2 h, acumulado 0
Tarea 4: +1 h, acumulado 2
Tarea 5: +2 h, acumulado 3
```

No se ha modificado ni recompilado el código: los logpoints sustituyen a los `System.out.println` temporales.

### Paso 7. Evaluar una expresión

1. Quita el logpoint y pon un breakpoint en `ServicioTareas.java`, **línea 89**: `double porcentaje = completadas * 100.0 / tareas.size();`.
2. Depura. Al detenerse, pulsa `F10` una vez.
3. En la Debug Console evalúa:
   - `completadas`
   - `porcentaje`
   - `completadas * 100 / tareas.size()`

**Resultado esperado:** `7`, `35.0` y `35`. La última es una división entera, porque no lleva `100.0`. Evaluar variantes de una expresión sin cambiar el código es una forma rápida de comprobar hipótesis.

### Paso 8. Depurar una prueba

1. Quita los breakpoints y pon uno en `ServicioTareas.java` en la línea `if (!tarea.getEstado().puedeCambiarA(destino)) {`, dentro de `cambiarEstado`.
2. En la vista **Testing**, clic derecho sobre `noPermiteReabrirUnaTareaCompletada` > **Debug Test**.
3. Al detenerse, en la Debug Console evalúa `tarea.getEstado().puedeCambiarA(destino)`.
4. Pulsa `F10`: entra en el `throw`.
5. Pulsa `F5` para terminar.

**Resultado esperado:**

- La evaluación devuelve `false`.
- En **Call Stack** se ve la cadena: la prueba llama a `cambiarEstado` a través de `assertThrows`.
- La prueba termina en verde, porque esperaba la excepción.

### Paso 9. Introducir temporalmente un error y detectarlo

1. En `EstadoTarea.java`, sustituye la línea:

   ```java
           if (this == COMPLETADA) {
   ```

   por:

   ```java
           if (this == EN_PROGRESO) {
   ```

2. En la vista **Testing**, pulsa **Run Tests**. También puedes ejecutar `mvn test`.

**Resultado esperado:** fallan **4 pruebas**:

- `noPermiteReabrirUnaTareaCompletada`: *Expected java.lang.IllegalStateException to be thrown, but nothing was thrown.*;
- tres casos de `transicionesDeEstado` (*expected: <true> but was: <false>* o al revés).

Con `mvn test`:

```text
[ERROR] Tests run: 12, Failures: 4, Errors: 0, Skipped: 1
[INFO] BUILD FAILURE
```

Haz clic en una prueba fallida para ver, en **Test Results**, la diferencia entre el valor esperado y el obtenido, y la línea exacta. Si quieres ver el fallo por dentro, depura la prueba como en el paso 8.

### Paso 10. Restaurar el código

1. Vuelve a dejar la línea como estaba: `if (this == COMPLETADA) {`. Puedes usar `Ctrl + Z` o `File: Revert File`.
2. Ejecuta de nuevo las pruebas.

**Resultado esperado:** `Tests run: 12, Failures: 0, Errors: 0, Skipped: 1` y todo en verde.

### Paso 11 (opcional). El escenario didáctico desactivado

1. En `EscenarioDidacticoTest.java`, borra la línea `@Disabled(...)` y ejecuta esa prueba.
2. **Resultado esperado:** termina con **error**, no con fallo: `IllegalStateException: La tarea 3 no puede pasar de COMPLETADA a PENDIENTE`. La prueba asume una regla de negocio que no es la real.
3. Restaura la línea `@Disabled(...)` con `Ctrl + Z`.

### Paso 12 (opcional). Adjuntar el depurador a Maven

1. Pon un breakpoint en la línea 63 de `ServicioTareas.java`.
2. En la terminal: `mvn test -Dmaven.surefire.debug`. Espera al mensaje `Listening for transport dt_socket at address: 5005`.
3. Lanza la configuración **Adjuntar a las pruebas de Maven (puerto 5005)**.

**Resultado esperado:** la ejecución de las pruebas por Maven se detiene en tu breakpoint. Al continuar, Maven termina con `BUILD SUCCESS`.

## 8. Resultados esperados

| Comprobación | Resultado |
|---|---|
| `mvn clean test` | `Tests run: 12, Failures: 0, Errors: 0, Skipped: 1`, `BUILD SUCCESS` |
| Primera parada en la línea 63 | `total` = 0, tarea 1 |
| Breakpoint condicional `tarea.getId() == 14` | Una parada, `total` = 19 |
| Logpoint | 13 mensajes en la Debug Console, sin detenerse |
| Expresiones en la línea 89 | `completadas` = 7, `porcentaje` = 35.0 |
| Error temporal en `EstadoTarea` | 4 pruebas fallidas |
| Tras restaurar | Todo en verde |

## 9. Problemas frecuentes

| Problema | Solución |
|---|---|
| La vista Testing está vacía | Espera a que termine la importación Maven; si persiste, `Java: Reload Projects` o `Test: Refresh Tests` |
| *Run* y *Debug* no aparecen sobre `main` | El servidor de lenguaje no está listo o el proyecto no se ha importado: revisa **Problems** y el canal *Language Support for Java* |
| El breakpoint aparece como un círculo gris hueco | La línea no tiene código ejecutable, o la clase no forma parte del proyecto importado |
| El breakpoint condicional no se detiene nunca | La condición no se cumple nunca en esa línea (paso 5) o tiene un error de sintaxis. VS Code avisa en la Debug Console. |
| `Could not find or load main class` al usar `launch.json` | `mainClass` o `projectName` no coinciden con la clase y el `artifactId` |
| El *attach* al puerto 5005 falla | Maven aún no ha mostrado *Listening...*, o el puerto está ocupado por una sesión anterior. Termina el proceso con `Ctrl + C` en su terminal. |
| `No tests found` con Maven | El *plugin* Surefire es antiguo. Este `pom.xml` fija la 3.6.1, así que no debería ocurrir. |

## 10. Comparativa con Eclipse e IntelliJ IDEA

| Acción | Eclipse | IntelliJ IDEA | VS Code |
|---|---|---|---|
| Breakpoint condicional | Propiedades del breakpoint > **Conditional** | Clic derecho en el breakpoint > **Condition** | **Add Conditional Breakpoint...** > **Expression** |
| Por pasadas | Propiedades > **Hit count** | **More** > **Pass count** | **Hit Count** |
| Trazas sin parar | Breakpoint con condición que imprime y devuelve `false` | Breakpoint sin **Suspend** y con **Evaluate and log** | **Logpoint** (*Log Message*) |
| Variables y expresiones vigiladas | Vistas **Variables** y **Expressions** | Pestaña **Variables** y **Watches** | Secciones **Variables** y **Watch** |
| Evaluar una expresión | Vista **Display** o **Inspect** (`Ctrl + Shift + I`) | **Evaluate Expression** (`Alt + F8`) | **Debug Console** |
| Pila de llamadas | Vista **Debug** | Pestaña **Frames** | Sección **Call Stack** |
| Configuraciones de ejecución | **Debug Configurations...** | **Run/Debug Configurations** | `.vscode/launch.json` |
| Depuración remota | **Remote Java Application** | Configuración **Remote JVM Debug** | Configuración `request: attach` |
| Ejecutar pruebas | **Run As > JUnit Test**, vista **JUnit** | Icono ▷ junto a la prueba, ventana **Run** | Vista **Testing** o icono ▷ |
| Depurar una prueba | **Debug As > JUnit Test** | Icono ▷ > **Debug** | **Debug Test** |

La principal diferencia está en las configuraciones de ejecución. En Eclipse e IntelliJ IDEA se gestionan en diálogos. En VS Code son texto JSON, que se puede revisar y compartir.

## 11. Comprobaciones finales

Comprueba que eres capaz de:

- [ ] Ejecutar todas las pruebas, una clase o una sola prueba desde la vista **Testing** y con `mvn test`.
- [ ] Interpretar una prueba correcta, fallida, con error y omitida.
- [ ] Poner breakpoints normales, condicionales y por número de pasadas, y logpoints.
- [ ] Inspeccionar **Variables**, **Watch** y **Call Stack**, y evaluar expresiones en la **Debug Console**.
- [ ] Usar *Step Over*, *Step Into*, *Step Out* y *Continue* con intención.
- [ ] Crear `.vscode/launch.json` con configuraciones `launch` y `attach`.
- [ ] Depurar una prueba y adjuntar el depurador a Maven.
- [ ] Introducir un error, detectarlo con las pruebas y restaurar el código.
