# Ejercicio 3.1 — Java 21 y gestión de proyectos

Módulo 3. Integración de desarrollos en Java · Tema 3.1. Proyectos Java · Apartado 3.1.1. Entorno y proyectos

Duración estimada: 1 hora y 30 minutos.

## 1. Introducción

En el módulo 1 se configuró VS Code y se practicaron los atajos. Este módulo se centra en **desarrollar aplicaciones Java profesionales**: proyectos, Maven, depuración, pruebas, Jakarta EE y Spring Boot.

Este primer apartado presenta las herramientas Java de VS Code desde el punto de vista de la **gestión de proyectos**:

- cómo reconoce VS Code un proyecto;
- qué JDK usa;
- cómo se organizan paquetes y clases;
- cómo se genera y refactoriza código.

Se trabaja con un proyecto Java 21 **sin herramienta de construcción**, para centrarse en el editor. Maven llega en el apartado 3.2.

El proyecto es una versión en Java estándar del **Gestor de tareas** del módulo 2, con algunos rasgos de Java moderno: expresiones `switch`, `record`, `var` y *streams*.

## 2. Objetivos

Al terminar serás capaz de:

- Explicar qué aporta cada extensión del Extension Pack for Java y qué es el Java Language Server.
- Identificar y cambiar el JDK de un proyecto con `Java: Configure Java Runtime`.
- Abrir o importar proyectos existentes y saber qué tipos reconoce VS Code.
- Explorar paquetes y clases con **Java Projects**.
- Crear clases e interfaces, generar constructores y métodos de acceso, refactorizar y organizar *imports*.
- Diagnosticar los problemas de configuración más habituales.

## 3. Conceptos fundamentales

### Extensiones Java de VS Code

| Extensión | Editor | Para qué sirve | ¿Necesaria en este módulo? |
|---|---|---|---|
| **Extension Pack for Java** | Microsoft | Paquete que instala las seis extensiones siguientes de una vez | Sí, en todos los apartados |
| Language Support for Java(TM) by Red Hat | Red Hat | Servidor de lenguaje: errores, autocompletado, navegación, refactorización y formato | Incluida en el pack |
| Debugger for Java | Microsoft | Ejecutar y depurar (**Run**, **Debug**, `launch.json`) | Incluida en el pack |
| Test Runner for Java | Microsoft | Ejecutar y depurar pruebas JUnit y TestNG (vista **Testing**) | Incluida en el pack. Se usa desde el 3.2. |
| Maven for Java | Microsoft | Vista **Maven**: ciclo de vida, dependencias y *plugins* | Incluida en el pack. Se usa desde el 3.2. |
| Gradle for Java | Microsoft | Lo mismo para Gradle | Incluida en el pack. No se usa en el curso. |
| Project Manager for Java | Microsoft | Vista **Java Projects** y asistentes para crear clases y paquetes | Incluida en el pack |
| **Spring Boot Extension Pack** | VMware | Paquete con las tres siguientes | Solo en los apartados 3.5 y 3.6 |
| Spring Boot Tools | VMware | Soporte de Spring Boot: `application.properties`, navegación por *beans* y *endpoints* | Incluida en el Spring Boot pack |
| Spring Initializr Java Support | Microsoft | Crear proyectos Spring Boot desde VS Code | Incluida en el Spring Boot pack |
| Spring Boot Dashboard | Microsoft | Vista para arrancar, parar y depurar aplicaciones Spring Boot | Incluida en el Spring Boot pack |

En este apartado basta con el **Extension Pack for Java**, que se instaló en el módulo 1. El curso **no instala extensiones automáticamente**: cada apartado indica cuáles necesita.

### Java Language Server

La extensión de Red Hat ejecuta en segundo plano un **servidor de lenguaje**, basado en el compilador de Eclipse (JDT), que analiza continuamente el código. VS Code le pregunta qué errores hay, qué sugerir, dónde está una declaración o cómo renombrar un símbolo. Por eso la mayoría de funciones Java dependen de que el servidor esté **listo**: indicador `{}` de la Status Bar y canal *Language Support for Java* del panel **Output**.

El servidor puede funcionar en dos modos:

- **Lightweight:** solo sintaxis, sin compilar el proyecto.
- **Standard:** análisis completo.

Por defecto (`java.server.launchMode: Hybrid`) empieza en ligero y pasa a estándar cuando abres un proyecto. Si la Status Bar muestra un icono de "modo ligero", haz clic en él para pasar a **Standard**.

### Tipos de proyecto que reconoce VS Code

| Lo que hay en la carpeta | Cómo lo trata la extensión de Java |
|---|---|
| `pom.xml` | Proyecto **Maven**: lo importa con sus dependencias (apartado 3.2) |
| `build.gradle` | Proyecto **Gradle** |
| `.project` y `.classpath` de Eclipse | Proyecto **Eclipse**: lo importa directamente |
| Configuración de IntelliJ IDEA (`.idea/`, `*.iml`) | **No** la importa: hay que usar Maven, Gradle o trabajar como carpeta sin gestionar |
| Solo archivos `.java` | **Carpeta sin gestionar** (*unmanaged folder*): deduce las carpetas de código a partir de las líneas `package` |

Este apartado usa una carpeta sin gestionar.

### JDK del proyecto

Como se vio en el ejercicio 1.3.3, la extensión usa su propio JRE para funcionar y un **JDK del proyecto** para compilar tu código. En carpetas sin gestionar, ese JDK es el predeterminado de `java.configuration.runtimes`, o el que detecte la extensión. Debe ser Java 21.

## 4. Preparación del entorno

Requisitos: JDK 21 y Extension Pack for Java, configurados en el módulo 1.

### 4.1. Actualizar el repositorio del curso

En la raíz del repositorio del curso:

```bash
git status
git pull --ff-only
```

`git status` debe indicar que no hay cambios pendientes.

### 4.2. Copiar el proyecto a una carpeta de prácticas

Vas a crear y modificar código. Trabaja en una **copia fuera del repositorio del curso**, para no interferir con sus actualizaciones. Desde la raíz del repositorio del curso:

**Windows (PowerShell):**

```powershell
New-Item -ItemType Directory -Force "$HOME/practicas-java" | Out-Null
Copy-Item -Recurse modulo-03-java/tema-3.1-proyectos-java/apartado-3.1.1-entorno-proyectos/proyecto "$HOME/practicas-java/proyectos-3.1"
code "$HOME/practicas-java/proyectos-3.1"
```

**Linux o WSL (Bash):**

```bash
mkdir -p ~/practicas-java
cp -r modulo-03-java/tema-3.1-proyectos-java/apartado-3.1.1-entorno-proyectos/proyecto ~/practicas-java/proyectos-3.1
code ~/practicas-java/proyectos-3.1
```

Si la carpeta de destino ya existe, bórrala o usa otro nombre. Para **restaurar** el proyecto original en cualquier momento, cierra la ventana, borra la copia y vuelve a copiarla.

> **No abras los proyectos del módulo 3 desde la ventana del repositorio del curso.** Al abrir la raíz del curso, la extensión de Java intenta importar todos los proyectos Maven que encuentra (apartados 3.2 a 3.6), lo que puede tardar y descargar dependencias. Ábrelos siempre en su propia ventana, desde tu copia.

### 4.3. Estructura del proyecto

```text
proyectos-3.1/
├── .gitignore
└── src/curso/java/
    ├── EstadoTarea.java      enum con switch de Java 21
    ├── Tarea.java            entidad
    ├── ServicioTareas.java   crear, listar, buscar, cambiar estado y recuentos; record Resumen
    └── Main.java             demostración
```

## 5. Herramientas de VS Code utilizadas

| Herramienta | Dónde | Uso |
|---|---|---|
| **Java Projects** | Sección del Explorer | Vista lógica: proyecto, carpetas de código, paquetes, clases y **JRE System Library** |
| Botón **+** de Java Projects, o clic derecho sobre un paquete | Java Projects | **New...** > **Class**, **Interface**, **Enum**, **Record**, **Package** |
| **New Java File** | Clic derecho sobre una carpeta del Explorer | Asistente para clase, interfaz, enum o record |
| `Java: Configure Java Runtime` | Command Palette | JDK de cada proyecto y JDK detectados |
| **Source Action...** | Clic derecho en el editor | **Generate Constructors...**, **Generate Getters and Setters...**, **Generate toString()...**, **Generate hashCode() and equals()...**, **Organize Imports** |
| **Refactor...** (`Ctrl + Shift + R`) | Clic derecho en el editor | Renombrar, extraer método, variable o constante, cambiar firma… |
| Bombilla / `Ctrl + .` | Editor | Correcciones rápidas: crear clase o método que falta, importar… |
| `Ctrl + Space` | Editor | Autocompletado. Incluye plantillas como `sysout`, `main`, `fori`. |
| `F12`, `Ctrl + F12`, `Shift + F12`, `Ctrl + T` | Editor | Definición, implementaciones, referencias, símbolos (tema 1.2) |
| **Outline** | Sección del Explorer | Estructura de la clase abierta |
| `Java: Clean Java Language Server Workspace` | Command Palette | Reinicia el análisis cuando el servidor queda en mal estado |

## 6. Explicaciones técnicas

### Paquetes y carpetas

En una carpeta sin gestionar, la extensión deduce que `src/` es la carpeta de código porque `src/curso/java/Tarea.java` declara `package curso.java;`. Al crear una clase desde **Java Projects** sobre el paquete `curso.java`, el archivo se crea en `src/curso/java/` con la línea `package` ya escrita.

### Generación de código

En lugar de escribir a mano constructores, *getters* o `toString()`, la extensión los genera a partir de los campos:

- **Source Action... > Generate Constructors...**: elige qué campos recibe el constructor.
- **Source Action... > Generate Getters and Setters...**: elige los campos. Los campos `final` solo tienen *getter*.
- **Source Action... > Generate toString()...**: elige campos y formato.

Para clases que solo transportan datos, Java ofrece los **records**: `ServicioTareas.Resumen` es un ejemplo. Un *record* genera automáticamente el constructor, los métodos de acceso, `equals`, `hashCode` y `toString`.

### Refactorización

Las refactorizaciones de la extensión modifican **todas las referencias** del proyecto, no solo el texto:

- **Rename Symbol** (`F2`): renombra variables, métodos, clases (y su archivo) y paquetes.
- **Extract to method**: convierte el código seleccionado en un método nuevo, con los parámetros necesarios.
- **Extract to local variable / constant**: convierte una expresión en variable o constante.
- **Organize Imports** (`Shift + Alt + O`): añade los *imports* que faltan y quita los que sobran.

## 7. Práctica guiada

Trabaja en la ventana de tu copia (`proyectos-3.1`).

### Paso 1. Abrir el proyecto y esperar al servidor de lenguaje

1. Abre `src/curso/java/Main.java`.
2. Espera a que el indicador `{}` de la Status Bar deje de mostrar actividad. Si aparece el modo ligero, pulsa sobre él para pasar a **Standard**.
3. Abre la sección **Java Projects** del Explorer.

**Resultado esperado:** **Java Projects** muestra el proyecto con `src`, el paquete `curso.java` con sus cuatro clases y **JRE System Library**. El panel **Problems** está vacío.

### Paso 2. Identificar el JDK

1. Despliega **JRE System Library** en **Java Projects**.
2. Ejecuta `Java: Configure Java Runtime` y localiza el JDK del proyecto.
3. En la Terminal integrada, ejecuta `java -version`.

**Resultado esperado:** las tres comprobaciones indican Java 21 (`JavaSE-21`). Si no es así, revisa el apartado 9.

### Paso 3. Explorar los paquetes

1. En **Java Projects**, despliega `curso.java` y abre `ServicioTareas`.
2. En **Outline**, localiza el *record* `Resumen` y los métodos.
3. Abre `EstadoTarea` y observa la expresión `switch` del método `descripcion()`. Borra temporalmente la línea `case COMPLETADA -> "Completada";`: el editor marca un error porque el `switch` ya no cubre todos los valores. Deshazlo con `Ctrl + Z`.

### Paso 4. Crear una clase nueva

1. En **Java Projects**, pasa el ratón sobre el paquete `curso.java` y pulsa **+**. También puedes hacer clic derecho sobre la carpeta `src/curso/java` del Explorer > **New Java File** > **Class...**
2. Elige **Class** y escribe `Responsable`.
3. Dentro de la clase, escribe dos campos:

   ```java
       private final String nombre;
       private final String correo;
   ```

**Resultado esperado:** se crea `src/curso/java/Responsable.java` con `package curso.java;` y la declaración de la clase. Los campos aparecen subrayados porque no se inicializan: lo resuelve el paso siguiente.

### Paso 5. Generar constructor y getters

1. Con el cursor dentro de la clase, haz clic derecho > **Source Action...** > **Generate Constructors...** Marca los dos campos y acepta.
2. Repite con **Source Action...** > **Generate Getters and Setters...** Como los campos son `final`, solo se ofrecen *getters*. Márcalos y acepta.
3. Opcional: **Generate toString()...**

**Resultado esperado:** la clase tiene un constructor `Responsable(String nombre, String correo)` y los métodos `getNombre()` y `getCorreo()`, sin errores.

### Paso 6. Navegar entre métodos

1. En `Main.java`, haz `Ctrl` + clic sobre `cambiarEstado`: salta a `ServicioTareas`.
2. Dentro de `cambiarEstado`, haz `F12` sobre `esFinal()`: salta a `EstadoTarea`.
3. Vuelve con `Alt + ←` dos veces.
4. Sobre `buscarPorId`, usa `Shift + F12` para ver dónde se usa.

**Resultado esperado:** `buscarPorId` se usa en `cambiarEstado`.

### Paso 7. Refactorizar

1. **Renombrar.** En `Main.java`, sitúa el cursor sobre la variable `reabierta`, pulsa `F2` y escribe `reaperturaPermitida`. Cambian la declaración y su uso.
2. **Extraer un método.** Selecciona en `Main.java` las tres líneas del bucle `for` que imprime las tareas (desde `for` hasta su `}`). Pulsa `Ctrl + Shift + R` > **Extract to method** y llama al método `mostrarTareas`.
3. **Organizar imports.** En `Responsable.java`, añade la línea `import java.util.List;` bajo `package`. Aparece un aviso de *import* no usado. Pulsa `Shift + Alt + O`: el *import* desaparece.

**Resultado esperado:** `Main` tiene un método `private static void mostrarTareas(ServicioTareas servicio)`, o similar, y `main` lo llama. El panel **Problems** sigue vacío.

### Paso 8. Ejecutar la aplicación

- **Desde VS Code:** en `Main.java`, pulsa **Run** sobre el método `main`.
- **Desde la terminal**, en la raíz de la copia:

  **PowerShell:**

  ```powershell
  javac --release 21 -Xlint:all -d out (Get-ChildItem -Recurse src -Filter *.java).FullName
  java -cp out curso.java.Main
  ```

  **Bash:**

  ```bash
  javac --release 21 -Xlint:all -d out $(find src -name '*.java')
  java -cp out curso.java.Main
  ```

  La compilación incluye todas las clases, también la nueva `Responsable`.

### Paso 9. Comprobar los resultados

Compara la salida con la del apartado 8, tanto la ejecución desde VS Code como la de la terminal.

## 8. Resultados esperados

La salida debe ser:

```text
== Tareas ==
#1  Instalar JDK 21           Completada
#2  Configurar VS Code        En progreso
#3  Crear el proyecto         Pendiente
#4  Ejecutar la aplicación    Pendiente

Reabrir una tarea completada: no permitido
Recuento por estado: {PENDIENTE=2, EN_PROGRESO=1, COMPLETADA=1}
Resumen: Resumen[total=4, pendientes=2, completadas=1]
```

Las refactorizaciones del paso 7 no cambian la salida: refactorizar es modificar la estructura del código **sin alterar su comportamiento**.

## 9. Problemas frecuentes

| Problema | Solución |
|---|---|
| **JRE System Library** no indica `JavaSE-21` | Ejecuta `Java: Configure Java Runtime` y comprueba que el JDK 21 está detectado. Si no aparece, decláralo en `java.configuration.runtimes` (ejercicio 1.3.3) y recarga la ventana con `Developer: Reload Window`. |
| No aparece **Java Projects**, o no hay autocompletado | El servidor está en modo ligero o cargando. Espera, o pulsa el indicador de la Status Bar para pasar a modo estándar. |
| *The declared package "curso.java" does not match the expected package* | El archivo está en una carpeta que no corresponde a su paquete. Muévelo a `src/curso/java/`. |
| Un archivo aparece como *non-project file* | Está fuera de las carpetas de código. Clic derecho sobre su carpeta > `Java: Add Folder to Java Source Path`. |
| Errores extraños que no desaparecen | `Java: Clean Java Language Server Workspace` y aceptar el reinicio |
| Clases duplicadas (*The type Tarea is already defined*) | Has abierto varias prácticas en la misma ventana, por ejemplo desde la raíz del curso. Abre solo tu copia. |
| **Source Action...** no ofrece *Generate Constructors...* | El cursor no está dentro de la clase o la clase tiene errores de sintaxis |

## 10. Comparativa con Eclipse e IntelliJ IDEA

| Acción | Eclipse | IntelliJ IDEA | VS Code |
|---|---|---|---|
| Vista de proyecto | **Package Explorer** | Ventana **Project** | **Java Projects** (y el Explorer de archivos) |
| JDK | **Installed JREs** y **Java Build Path** | **Project Structure > SDK** | `Java: Configure Java Runtime`, `java.configuration.runtimes` |
| Importar proyecto | **File > Import** (Maven, Gradle, Eclipse) | **File > Open** (Maven, Gradle, `.idea`) | **File > Open Folder...**: detecta Maven, Gradle y proyectos Eclipse |
| Nueva clase o interfaz | **File > New > Class / Interface** | **New > Java Class** | **+** en Java Projects o **New Java File** |
| Generar constructor y *getters* | **Source** (`Alt + Shift + S`) > **Generate...** | **Generate** (`Alt + Insert`) | **Source Action...** > **Generate...** |
| Refactorizar | **Refactor** (`Alt + Shift + T`) | **Refactor This** (`Ctrl + Alt + Shift + T`) | **Refactor...** (`Ctrl + Shift + R`) |
| Organizar *imports* | `Ctrl + Shift + O` | `Ctrl + Alt + O` | `Shift + Alt + O` |
| Motor de análisis | Compilador de Eclipse (JDT) | Analizador propio de JetBrains | Compilador de Eclipse (JDT) a través del servidor de lenguaje |

Como VS Code usa el mismo motor que Eclipse, los mensajes de error y las refactorizaciones resultarán familiares a quien venga de Eclipse.

## 11. Comprobaciones finales

Comprueba que eres capaz de:

- [ ] Explicar qué hace cada extensión del Extension Pack for Java y qué es el Java Language Server.
- [ ] Indicar qué tipos de proyecto importa VS Code y cómo trata una carpeta sin gestionar.
- [ ] Comprobar que el proyecto usa Java 21 en **JRE System Library** y con `Java: Configure Java Runtime`.
- [ ] Crear una clase desde **Java Projects** y generar su constructor y sus *getters*.
- [ ] Navegar a definiciones y referencias.
- [ ] Renombrar con `F2`, extraer un método y organizar *imports*.
- [ ] Ejecutar la aplicación desde VS Code y desde la terminal y obtener la salida esperada.
- [ ] Resolver los problemas de configuración más habituales.
- [ ] Restaurar el proyecto copiándolo de nuevo desde el repositorio del curso.
