# Ejercicio 3.2 — Maven y gestión de dependencias

Módulo 3. Integración de desarrollos en Java · Tema 3.2. Maven · Apartado 3.2.1. Maven y dependencias

Duración estimada: 2 horas.

## 1. Introducción

En el apartado 3.1 se compiló a mano con `javac`. En un proyecto real eso no escala:

- hay que descargar bibliotecas de terceros y sus propias dependencias;
- hay que ejecutar las pruebas;
- hay que empaquetar el resultado;
- todo debe hacerse igual en cualquier equipo.

**Maven** automatiza todo eso a partir de un único archivo, `pom.xml`.

Este apartado se centra en **trabajar con Maven desde VS Code**: la vista **Maven**, la ejecución de fases y objetivos, la gestión de dependencias y el diagnóstico de problemas, con el comando equivalente para cada acción.

## 2. Objetivos

Al terminar serás capaz de:

- Explicar la estructura estándar de un proyecto Maven y las partes de `pom.xml`.
- Identificar un artefacto por sus coordenadas y gestionar dependencias.
- Ejecutar las fases del ciclo de vida desde la vista **Maven** y desde la terminal.
- Entender qué son los *plugins* y los perfiles.
- Recargar un proyecto tras cambiar `pom.xml` y diagnosticar problemas de dependencias.

## 3. Conceptos fundamentales

### Qué es Maven

Maven es una herramienta de **construcción y gestión de dependencias**. Se basa en convenciones:

- si el código está donde Maven espera, no hay que configurar casi nada;
- el proyecto declara **qué necesita**, y Maven decide **cómo construirlo**.

### Estructura estándar

```text
proyecto/
├── pom.xml                         configuración del proyecto
├── src/main/java/                  código de la aplicación
├── src/main/resources/             recursos (properties, plantillas…)
├── src/test/java/                  código de pruebas
└── target/                         salida generada (no se versiona)
```

### `pom.xml`

| Sección | Contenido |
|---|---|
| `groupId`, `artifactId`, `version` | **Coordenadas**: identifican el proyecto. Aquí: `curso.java:gestor-tareas-maven:1.0.0-SNAPSHOT`. `SNAPSHOT` indica una versión en desarrollo. |
| `packaging` | Tipo de artefacto: `jar` (este proyecto), `war` (apartado 3.4), `pom`… |
| `properties` | Variables: versión de Java (`maven.compiler.release`), codificación, versiones de dependencias |
| `dependencies` | Bibliotecas que necesita el proyecto, cada una con sus coordenadas y su **alcance** (*scope*) |
| `dependencyManagement` | Versiones gestionadas de forma centralizada. Aquí, la BOM de JUnit. |
| `build/plugins` | *Plugins* que hacen el trabajo: compilar, probar, empaquetar, ejecutar |
| `profiles` | Variantes de configuración que se activan a petición |

### Dependencias y alcances

| Alcance | Disponible en | Ejemplo |
|---|---|---|
| `compile` (por defecto) | Compilación, pruebas y ejecución | `commons-lang3` |
| `test` | Solo en las pruebas | `junit-jupiter` |
| `provided` | Compilación, pero la aporta el entorno de ejecución | API Servlet (apartado 3.4) |
| `runtime` | Solo en ejecución | *Drivers* JDBC |

Las dependencias de tus dependencias, las **transitivas**, se descargan automáticamente. `junit-jupiter` arrastra, por ejemplo, `junit-jupiter-api` y `opentest4j`.

### Repositorios

Maven descarga los artefactos de **Maven Central** (`repo.maven.apache.org`) y los guarda en el **repositorio local**, `.m2/repository` en tu carpeta personal. La primera construcción necesita Internet y tarda más; las siguientes reutilizan lo descargado.

### Ciclo de vida, fases y *plugins*

Las fases se ejecutan **en orden**: pedir una fase ejecuta todas las anteriores.

```text
validate → compile → test → package → verify → install → deploy
```

| Fase u objetivo | Qué hace en este proyecto |
|---|---|
| `clean` (ciclo propio) | Borra `target/` |
| `compile` | Compila `src/main/java` en `target/classes` |
| `test` | Compila y ejecuta las pruebas de `src/test/java` (*plugin* Surefire) |
| `package` | Genera `target/gestor-tareas-maven-1.0.0-SNAPSHOT.jar` |
| `install` | Copia el JAR al repositorio local, para usarlo desde otros proyectos |

Cada fase la ejecutan uno o varios **plugins**, como `maven-compiler-plugin` o `maven-surefire-plugin`. Un *plugin* también puede ofrecer **objetivos** sueltos, con la sintaxis `plugin:objetivo`, como `dependency:tree` o `exec:java`.

Este `pom.xml` fija la versión de cada *plugin*. Si no se fijan, Maven usa versiones por defecto que dependen de la versión de Maven instalada. Algunas son tan antiguas que, por ejemplo, no ejecutan las pruebas de JUnit 5.

### Perfiles

Un **perfil** es un bloque de configuración que se activa a petición con `-P nombre`. Este proyecto incluye el perfil `rapido`, que activa `skipTests`: `mvn package -P rapido` empaqueta **sin** ejecutar las pruebas. Es solo una introducción: los perfiles se usan, por ejemplo, para configuraciones distintas por entorno.

## 4. Preparación del entorno

### 4.1. Instalar y comprobar Maven

La extensión **Maven for Java** es una interfaz gráfica: **no incluye Maven**. Necesitas Maven instalado en tu equipo, **versión 3.6.3 o posterior**. Se recomienda una versión 3.9.x o más reciente de la web de Apache Maven.

Comprueba si ya lo tienes, en una terminal nueva:

```bash
mvn --version
```

**Resultado esperado:** la versión de Maven y, en la línea `Java version`, **21**.

```text
Apache Maven 3.x.x
Maven home: ...
Java version: 21..., vendor: ..., runtime: ...
```

Si el comando no existe:

- **Windows:**
  1. Descarga el archivo binario `apache-maven-<versión>-bin.zip` desde la web oficial de Apache Maven.
  2. Descomprímelo en una carpeta sin espacios, por ejemplo `C:\herramientas\apache-maven-<versión>`.
  3. Añade su subcarpeta `bin` a la variable `Path` de tu usuario (**Configuración del sistema > Variables de entorno**).
  4. **Cierra y vuelve a abrir VS Code** y las terminales.
- **Ubuntu o WSL:** `sudo apt install maven`. La versión de Ubuntu 24.04 es la 3.8.7, compatible con este curso.
- **Sin acceso al PATH:** en VS Code, la preferencia `maven.executable.path` permite indicar la ruta completa del ejecutable `mvn`. Solo la usará la extensión, no la terminal.

Si `Java version` no es 21: Maven usa la variable `JAVA_HOME` si está definida y, si no, el `java` del `PATH`. Corrige `JAVA_HOME` o el `PATH` (ejercicio 1.3.2).

### 4.2. Actualizar el repositorio del curso y copiar el proyecto

En la raíz del repositorio del curso, ejecuta `git status` (sin cambios pendientes) y `git pull --ff-only`. Después copia el proyecto **fuera del repositorio del curso**:

**Windows (PowerShell):**

```powershell
New-Item -ItemType Directory -Force "$HOME/practicas-java" | Out-Null
Copy-Item -Recurse modulo-03-java/tema-3.2-maven/apartado-3.2.1-maven-dependencias/proyecto "$HOME/practicas-java/maven-3.2"
code "$HOME/practicas-java/maven-3.2"
```

**Linux o WSL (Bash):**

```bash
mkdir -p ~/practicas-java
cp -r modulo-03-java/tema-3.2-maven/apartado-3.2.1-maven-dependencias/proyecto ~/practicas-java/maven-3.2
code ~/practicas-java/maven-3.2
```

Para restaurar el proyecto original, borra la copia y vuelve a copiarla.

### 4.3. Extensiones

Basta con el **Extension Pack for Java**, que incluye **Maven for Java** y **Test Runner for Java**.

Opcionalmente, la extensión **XML** de Red Hat valida y autocompleta `pom.xml` según su esquema.

## 5. Herramientas de VS Code utilizadas

| Herramienta | Dónde | Uso |
|---|---|---|
| Vista **Maven** | Sección del Explorer | Proyectos Maven, con **Lifecycle**, **Plugins** y **Dependencies** |
| Clic derecho sobre una fase de **Lifecycle** > **Run** | Vista Maven | Ejecuta `mvn <fase>` en una terminal |
| Clic derecho sobre el proyecto | Vista Maven | **Run Maven Commands...**, **Custom...**, **Show Effective POM**, **Show Dependencies**, **Add a dependency...**, **Favorites...** |
| `Maven: History...` | Command Palette | Repite un comando Maven ya ejecutado |
| Aviso *A build file was modified...* | Notificación | Sincroniza el proyecto Java tras cambiar `pom.xml` |
| `Java: Reload Projects` | Clic derecho sobre `pom.xml`, o Command Palette | Recarga el proyecto a mano |
| **Testing** | Activity Bar | Pruebas JUnit del proyecto (Test Runner for Java) |
| **Problems** y **Output** (canal *Maven for Java*) | Panel inferior | Errores de compilación y de dependencias |
| **Java Projects** > **Maven Dependencies** | Explorer | JAR de cada dependencia y sus clases |

La vista Maven ejecuta exactamente los mismos comandos `mvn` en la Terminal integrada: lo que ves allí es lo que harías a mano.

## 6. Explicaciones técnicas

### Cómo importa VS Code un proyecto Maven

Al abrir una carpeta con `pom.xml`, la extensión de Java lo importa: lee las dependencias, configura el *classpath* y la versión de Java (`maven.compiler.release`) y descarga lo que falte.

Cuando modificas `pom.xml`, VS Code avisa (*A build file was modified. Do you want to synchronize the Java classpath/configuration?*). Responde **Yes**, o **Always** para que lo haga solo. También puedes usar `Java: Reload Projects`.

### Ejecutar la aplicación de forma reproducible

`mvn package` genera un JAR que **solo contiene tus clases**, no las dependencias. Por eso `java -jar target/gestor-tareas-maven-1.0.0-SNAPSHOT.jar` falla con:

```text
Exception in thread "main" java.lang.NoClassDefFoundError: org/apache/commons/lang3/StringUtils
```

Para ejecutarla con las dependencias en el *classpath*:

- **Desde VS Code:** **Run** sobre `main`. La extensión de Java usa el *classpath* de Maven.
- **Con Maven:** `mvn compile exec:java`. El *plugin* `exec-maven-plugin` ya está configurado con la clase principal.

En el apartado 3.5, Spring Boot resuelve esto generando un JAR que incluye todas las dependencias.

### Diagnóstico de dependencias

- **`mvn dependency:tree`** muestra el árbol de dependencias directas y transitivas. Sirve para saber de dónde viene una biblioteca o detectar versiones duplicadas.
- **Show Dependencies**, en la vista Maven, muestra el mismo árbol de forma gráfica.
- **Show Effective POM** muestra el `pom.xml` completo, con lo heredado y los valores por defecto.
- Si una dependencia no se puede descargar, el error aparece en la terminal y en **Problems**, sobre `pom.xml`.

## 7. Práctica guiada

### Paso 1. Abrir el proyecto Maven

Abre la copia (`maven-3.2`). Espera a que la extensión de Java termine de importar: indicador de la Status Bar y canal *Language Support for Java* en **Output**. La primera vez puede tardar mientras se descargan las dependencias.

**Resultado esperado:** la sección **Maven** del Explorer muestra `gestor-tareas-maven` y el panel **Problems** está vacío.

### Paso 2. Explorar la estructura

1. Recorre `src/main/java/curso/java` (las cuatro clases) y `src/test/java/curso/java` (`ServicioTareasTest`).
2. En **Java Projects**, despliega **Maven Dependencies**: aparecen `commons-lang3` y los JAR de JUnit.
3. Abre `ServicioTareas.java` y haz `Ctrl` + clic sobre `StringUtils`. Se abre la clase de la biblioteca, en solo lectura.

### Paso 3. Identificar las dependencias

1. Abre `pom.xml` y localiza las coordenadas, `properties`, `dependencyManagement`, `dependencies`, los *plugins* y el perfil `rapido`.
2. En la vista Maven, clic derecho sobre el proyecto > **Show Dependencies**.
3. En la terminal: `mvn dependency:tree`.

**Resultado esperado:**

```text
[INFO] curso.java:gestor-tareas-maven:jar:1.0.0-SNAPSHOT
[INFO] +- org.apache.commons:commons-lang3:jar:3.22.0:compile
[INFO] \- org.junit.jupiter:junit-jupiter:jar:5.14.4:test
[INFO]    +- org.junit.jupiter:junit-jupiter-api:jar:5.14.4:test
[INFO]    |  +- org.opentest4j:opentest4j:jar:1.3.0:test
[INFO]    |  +- org.junit.platform:junit-platform-commons:jar:1.14.4:test
[INFO]    |  \- org.apiguardian:apiguardian-api:jar:1.1.2:test
[INFO]    +- org.junit.jupiter:junit-jupiter-params:jar:5.14.4:test
[INFO]    \- org.junit.jupiter:junit-jupiter-engine:jar:5.14.4:test
[INFO]       \- org.junit.platform:junit-platform-engine:jar:1.14.4:test
```

`commons-lang3` es una dependencia directa de alcance `compile`. Todo lo de JUnit tiene alcance `test` y no llegará al JAR final.

### Paso 4. Modificar `pom.xml`

1. Cambia el nombre del proyecto: `<name>Gestor de tareas con Maven</name>` por `<name>Gestor de tareas con Maven (curso)</name>`. Guarda.
2. Si aparece el aviso de sincronización, responde **Yes**.
3. Ejecuta `mvn validate` desde la terminal: la línea `Building ...` muestra el nombre nuevo.

**Resultado esperado:** `[INFO] Building Gestor de tareas con Maven (curso) 1.0.0-SNAPSHOT`.

### Paso 5. Ejecutar `compile`

En la vista Maven: **Lifecycle** > clic derecho sobre **compile** > **Run**. Equivale a `mvn compile`.

**Resultado esperado:** `BUILD SUCCESS` y la carpeta `target/classes` con los `.class`.

### Paso 6. Ejecutar `test`

1. En la vista Maven: **Lifecycle** > **test** > **Run** (`mvn test`).
2. Abre también la vista **Testing** (icono de matraz) y pulsa **Run Tests**.

**Resultado esperado:**

```text
[INFO] Tests run: 3, Failures: 0, Errors: 0, Skipped: 0
[INFO] BUILD SUCCESS
```

En la vista **Testing**, las tres pruebas de `ServicioTareasTest` aparecen en verde.

### Paso 7. Ejecutar `package`

**Lifecycle** > **package** > **Run** (`mvn package`).

**Resultado esperado:** `Building jar: .../target/gestor-tareas-maven-1.0.0-SNAPSHOT.jar` y `BUILD SUCCESS`. Las pruebas se vuelven a ejecutar, porque `test` es una fase anterior a `package`.

Prueba también el perfil: `mvn package -P rapido` muestra `Tests are skipped.`

### Paso 8. Examinar `target/`

Abre `target/` en el Explorer. Aparece atenuada porque `.gitignore` la excluye. Contiene:

| Elemento | Contenido |
|---|---|
| `classes/` | Clases compiladas de la aplicación |
| `test-classes/` | Clases compiladas de las pruebas |
| `surefire-reports/` | Informes de las pruebas |
| `gestor-tareas-maven-1.0.0-SNAPSHOT.jar` | El artefacto empaquetado |

`mvn clean`, o **Lifecycle** > **clean**, borra `target/` por completo. Se puede regenerar en cualquier momento.

### Paso 9. Ejecutar la aplicación

1. **Desde VS Code:** abre `Main.java` y pulsa **Run**.
2. **Con Maven:** `mvn compile exec:java`. En la vista Maven, clic derecho sobre el proyecto > **Custom...** y escribe `compile exec:java`.
3. Comprueba que `java -jar target/gestor-tareas-maven-1.0.0-SNAPSHOT.jar` falla con `NoClassDefFoundError`, y explica por qué (apartado 6).

**Resultado esperado** de los pasos 1 y 2:

```text
== Gestor de tareas (Maven) ==
#1  Instalar maven           COMPLETADA  Descargar Maven y añadir su...
#2  Revisar el pom.xml       EN_PROGRESO Coordenadas, dependencias y...
#3  Ejecutar mvn package     PENDIENTE   Generar el JAR en target
```

Los títulos se han normalizado y las descripciones largas se han abreviado con `StringUtils`.

Con `mvn compile exec:java` la salida aparece entre los mensajes `[INFO]` de Maven. Añade `-q` para ocultarlos.

### Paso 10. Resolver un problema controlado de dependencias

1. En `pom.xml`, cambia `<commons-lang3.version>3.22.0</commons-lang3.version>` por `<commons-lang3.version>3.99.0</commons-lang3.version>`, una versión que **no existe**, y guarda.
2. Ejecuta `mvn compile`.
3. Observa también el panel **Problems** y el árbol de **Maven Dependencies**.
4. **Restaura** la versión `3.22.0`, guarda, acepta la sincronización (o ejecuta `Java: Reload Projects`) y vuelve a ejecutar `mvn compile`.

**Resultado esperado:**

- Paso 2:

  ```text
  [INFO] BUILD FAILURE
  [ERROR] Failed to execute goal on project gestor-tareas-maven: Could not resolve dependencies for project curso.java:gestor-tareas-maven:jar:1.0.0-SNAPSHOT: Could not find artifact org.apache.commons:commons-lang3:jar:3.99.0 in central (https://repo.maven.apache.org/maven2)
  ```

  Si repites el paso, Maven responde de otra forma: *commons-lang3:jar:3.99.0 was not found in https://repo.maven.apache.org/maven2 during a previous attempt. This failure was cached in the local repository...* Maven recuerda los artefactos que no encontró y no vuelve a buscarlos durante un tiempo. `mvn -U compile` fuerza la comprobación.
- Paso 3: VS Code marca el error en `pom.xml` y las clases que usan `StringUtils` dejan de resolverse.
- Paso 4: vuelve `BUILD SUCCESS`.

**Variante:** borra el bloque `<dependency>` de `commons-lang3` y compila. El error pasa a ser de compilación (`package org.apache.commons.lang3 does not exist`) en lugar de resolución. Restaura el bloque con `Ctrl + Z` o copiando de nuevo el proyecto.

## 8. Resultados esperados

| Comprobación | Resultado |
|---|---|
| `mvn --version` | Maven 3.6.3 o posterior, con Java 21 |
| `mvn clean test` | `Tests run: 3, Failures: 0, Errors: 0, Skipped: 0` y `BUILD SUCCESS` |
| `mvn package` | `target/gestor-tareas-maven-1.0.0-SNAPSHOT.jar` |
| `mvn dependency:tree` | El árbol del paso 3 |
| `mvn compile exec:java` | La salida del paso 9 |
| Versión inexistente | `Could not find artifact ...:3.99.0` |

## 9. Problemas frecuentes

| Problema | Solución |
|---|---|
| `mvn: command not found` / `'mvn' no se reconoce...` | Maven no está instalado o su carpeta `bin` no está en el `PATH`. Revisa el apartado 4.1 y reinicia VS Code. |
| La vista Maven indica que no encuentra `mvn` | Lo mismo, o configura `maven.executable.path` |
| `mvn --version` muestra otra versión de Java | Corrige `JAVA_HOME` o el `PATH` (ejercicio 1.3.2) |
| `release version 21 not supported` | Maven se ejecuta con un JDK anterior a 21: mismo caso que el anterior |
| `requires Maven version 3.9.0` u otra versión mínima | Tu Maven es demasiado antiguo para un *plugin*. Este proyecto funciona desde Maven 3.6.3; actualiza Maven si usas una versión anterior. |
| La primera construcción tarda mucho o falla por red | Maven necesita Internet para descargar dependencias. En redes corporativas puede hacer falta configurar un *proxy* en `.m2/settings.xml`. |
| *was not found ... during a previous attempt. This failure was cached...* | Maven guardó un fallo anterior en caché. Corrige la versión o fuerza la comprobación con `mvn -U <fase>`. |
| El editor marca errores en clases de una dependencia recién añadida | El proyecto no se ha sincronizado: `Java: Reload Projects` |
| `NoClassDefFoundError` al ejecutar el JAR | El JAR no incluye las dependencias: usa **Run** o `mvn compile exec:java` |
| `No tests to run` o las pruebas no se ejecutan | *Plugin* Surefire antiguo: comprueba que `pom.xml` declara `maven-surefire-plugin` 3.x |

## 10. Comparativa con Eclipse e IntelliJ IDEA

| Acción | Eclipse (m2e) | IntelliJ IDEA | VS Code |
|---|---|---|---|
| Importar proyecto Maven | **File > Import > Existing Maven Projects** | **File > Open** sobre `pom.xml` | **File > Open Folder...**: importación automática |
| Ejecutar fases | **Run As > Maven build...** (*Goals*) | Ventana **Maven** > **Lifecycle** | Vista **Maven** > **Lifecycle** > **Run** |
| Objetivos personalizados | **Run As > Maven build...** | Ventana **Maven** > **Execute Maven Goal** | **Custom...** o **Run Maven Commands...** |
| Sincronizar tras cambiar `pom.xml` | **Maven > Update Project** (`Alt + F5`) | **Reload All Maven Projects** (icono de recarga) | Aviso de sincronización o `Java: Reload Projects` |
| Árbol de dependencias | Pestaña **Dependency Hierarchy** del editor del POM | **Show Dependencies** (diagrama) | **Show Dependencies** o `mvn dependency:tree` |
| POM efectivo | Pestaña **Effective POM** | **Show Effective POM** | **Show Effective POM** |
| Añadir dependencia | Pestaña **Dependencies** > **Add** | **Generate** > **Add dependency** en el POM | **Add a dependency...** (busca en Maven Central) |
| Maven utilizado | Maven integrado (*embedded*) o uno externo | Maven integrado (*bundled*) o uno externo | El Maven **instalado** (o el *wrapper* `mvnw` del proyecto) |

Eclipse e IntelliJ IDEA incluyen un Maven propio. VS Code usa el que tengas instalado. Por eso aquí es imprescindible comprobar `mvn --version`.

## 11. Comprobaciones finales

Comprueba que eres capaz de:

- [ ] Comprobar la instalación de Maven y su versión de Java con `mvn --version`, y saber qué hacer si no está en el `PATH`.
- [ ] Explicar la estructura estándar de un proyecto Maven y las secciones de `pom.xml`.
- [ ] Explicar las coordenadas, las dependencias directas y transitivas y los alcances `compile` y `test`.
- [ ] Ejecutar `clean`, `compile`, `test` y `package` desde la vista Maven y desde la terminal.
- [ ] Explicar qué hay en `target/` y por qué no se versiona.
- [ ] Usar un perfil con `-P`.
- [ ] Ejecutar la aplicación desde VS Code y con `mvn compile exec:java`, y explicar por qué falla `java -jar`.
- [ ] Consultar el árbol de dependencias y el POM efectivo.
- [ ] Diagnosticar y corregir una dependencia que no se puede resolver.
