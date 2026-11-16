# Ejercicio 1.1.3 — Configuración inicial

Módulo 1. Introducción a VS Code · Tema 1.1. Conceptos básicos · Apartado 1.1.3. Configuración inicial

## 1. Introducción

VS Code funciona sin configurar nada, pero sus valores por defecto están pensados para cualquier lenguaje, no específicamente para Java. Ajustar unas pocas preferencias desde el principio tiene ventajas claras:

- **Código legible y homogéneo**: la indentación, el tamaño de tabulación y el formato automático hacen que todo el equipo vea y guarde el código de la misma manera, como ocurría con los perfiles de formato de Eclipse o IntelliJ IDEA.
- **Menos errores invisibles**: mostrar los espacios en blanco y saber si se usan tabulaciones o espacios evita mezclas que luego aparecen como cambios inesperados.
- **Un entorno Java correcto**: la extensión de Java debe reconocer el JDK 21 instalado. Si usa otra versión, el editor puede marcar errores o permitir sintaxis que luego no compila.
- **Saber dónde se guarda cada ajuste**: VS Code distingue entre preferencias del usuario, del entorno remoto (WSL) y de cada carpeta. Conocer esa diferencia evita que un cambio pensado para un proyecto afecte a todos los demás.

Este ejercicio no repite la instalación de VS Code, WSL, Java ni las extensiones. Se parte del entorno ya preparado.

## 2. Objetivos

Al terminar este ejercicio sabrás:

- Abrir las preferencias de VS Code, tanto en el editor visual (*Settings*) como en `settings.json`.
- Ajustar el tamaño de fuente, la tabulación, la inserción de espacios, el ajuste de línea (*Word Wrap*), el minimapa, la visualización de espacios en blanco y el guardado automático.
- Localizar las preferencias de la extensión de Java y comprobar que el proyecto usa Java 21.
- Identificar las extensiones Java activas y el estado del servidor de lenguaje Java.
- Formatear un documento o una selección, y activar el formato al guardar.
- Distinguir entre configuración de usuario, de entorno remoto, de *workspace* y por lenguaje.
- Restaurar los valores por defecto de cualquier preferencia que hayas cambiado.

### Material preparado

```text
modulo-01-vscode/
└── tema-1.1-conceptos-basicos/
    └── apartado-1.1.3-configuracion-inicial/
        ├── README.md
        └── src/
            └── curso/
                └── vscode/
                    └── ConfiguracionInicial.java
```

La clase `ConfiguracionInicial` (paquete `curso.vscode`) ya compila y está formateada. Contiene lo necesario para observar el efecto de cada preferencia:

- varios niveles de indentación;
- un bucle `for` que recorre una lista de preferencias;
- un método auxiliar (`mostrarPreferencia`);
- una línea de texto muy larga, la variable `descripcionLarga`, para probar *Word Wrap*.

### Lo que harás tú

No hay que programar. Cambiarás preferencias de VS Code y modificarás temporalmente el formato de la clase, siempre de forma reversible. Al terminar, la clase debe quedar como estaba. Las preferencias quedarán como tú decidas conservarlas.

### Antes de empezar

1. Abre VS Code desde la raíz del repositorio del curso (`cd <ruta-del-repositorio>` y `code .`).
2. Abre `modulo-01-vscode/tema-1.1-conceptos-basicos/apartado-1.1.3-configuracion-inicial/src/curso/vscode/ConfiguracionInicial.java`. Lo más rápido es `Ctrl + P` (**Go > Go to File...**) y escribir `ConfiguracionInicial`.
3. Anota los valores actuales de las preferencias que vas a tocar, por si quieres volver a ellos. El apartado 7 explica cómo restaurarlas.

## 3. Preferencias básicas del editor

### Abrir Settings

Usa cualquiera de estas opciones:

- **File > Preferences > Settings**.
- `Ctrl + ,`.
- Command Palette (**View > Command Palette...**) > `Preferences: Open Settings (UI)`.

Se abre el editor visual de preferencias:

- **Buscador**: en la parte superior. Admite el nombre visible de la preferencia (por ejemplo, `font size`) o su identificador (por ejemplo, `editor.fontSize`).
- **Pestañas de ámbito**: debajo del buscador. Al trabajar con WSL aparecen tres:
  - **User**: tus preferencias en Windows, para todas las ventanas.
  - **Remote [WSL: Ubuntu]**: preferencias del entorno WSL.
  - **Workspace**: preferencias de la carpeta abierta.
- **Árbol de categorías**: a la izquierda (*Text Editor*, *Workbench*, *Extensions*…).

Las preferencias que has cambiado muestran una **barra azul** a la izquierda. Al pasar el ratón por encima de una preferencia aparece un icono de engranaje con la opción **Reset Setting**, que restaura su valor por defecto.

### Preferencias que se trabajan en este ejercicio

| Preferencia | Identificador | Valor por defecto | Qué hace | Acceso rápido |
|---|---|---|---|---|
| Tamaño de fuente | `editor.fontSize` | `14` | Tamaño del texto en el editor | Solo desde Settings |
| Tamaño de tabulación | `editor.tabSize` | `4` | Número de espacios que equivale a una tabulación | Status Bar: indicador `Spaces: 4` o `Tab Size: 4` |
| Insertar espacios | `editor.insertSpaces` | `true` | Al pulsar `Tab`, inserta espacios en lugar de un carácter de tabulación | Status Bar: el mismo indicador |
| Detectar indentación | `editor.detectIndentation` | `true` | Ajusta automáticamente las dos anteriores según el contenido de cada archivo | Solo desde Settings |
| Ajuste de línea | `editor.wordWrap` | `"off"` | Parte visualmente las líneas largas para que quepan en la ventana | **View > Word Wrap** (`Alt + Z`) |
| Minimapa | `editor.minimap.enabled` | `true` | Miniatura del archivo en el lateral derecho | **View > Appearance > Minimap** |
| Mostrar espacios en blanco | `editor.renderWhitespace` | `"selection"` | Dibuja un punto por cada espacio y una flecha por cada tabulación | **View > Appearance > Render Whitespace** |
| Guardado automático | `files.autoSave` | `"off"` | Guarda los archivos sin necesidad de `Ctrl + S` | **File > Auto Save** |

Detalles de cada una:

- **Tamaño de fuente.** Cambia solo el texto del editor. Es distinto del zoom de la ventana (**View > Appearance > Zoom In / Zoom Out**), que amplía toda la interfaz.
- **Tamaño de tabulación e inserción de espacios.** En Java lo habitual es indentar con **4 espacios**. Con `editor.detectIndentation` activado, VS Code puede ignorar tus valores en un archivo concreto si detecta que ese archivo usa otra indentación. El indicador de la Status Bar muestra siempre lo que se está aplicando al archivo activo.
- **Word Wrap.** Es un ajuste **solo visual**: no inserta saltos de línea en el archivo. Valores posibles:
  - `"off"`: no ajusta.
  - `"on"`: ajusta al ancho de la ventana.
  - `"wordWrapColumn"`: ajusta en la columna indicada en `editor.wordWrapColumn`.
  - `"bounded"`: ajusta en el valor menor de los dos anteriores.
- **Minimap.** Útil en archivos largos para orientarse. En pantallas pequeñas puede ocultarse para ganar espacio.
- **Render Whitespace.** Valores posibles:
  - `"none"`: nunca muestra los espacios.
  - `"boundary"`: muestra los espacios salvo los simples entre palabras.
  - `"selection"`: solo en el texto seleccionado.
  - `"trailing"`: solo los espacios al final de línea.
  - `"all"`: todos.
- **Auto Save.** Valores posibles:
  - `"off"`: desactivado.
  - `"afterDelay"`: guarda tras una pausa, configurable en `files.autoSaveDelay` (1000 ms por defecto). Es el valor que activa **File > Auto Save**.
  - `"onFocusChange"`: guarda al pasar a otro archivo.
  - `"onWindowChange"`: guarda al cambiar de ventana.

## 4. Configuración específica del lenguaje Java

### Localizar las preferencias de Java

- En Settings, escribe `java` en el buscador. También puedes desplegar en el árbol **Extensions > Java** y las demás categorías de las extensiones Java.
- Desde la vista **Extensions**, haz clic en el engranaje de *Language Support for Java(TM) by Red Hat* y elige **Settings**. Se muestran solo las preferencias de esa extensión.
- Desde la Command Palette, `Java: Open Project Settings` abre una página con la configuración Java del proyecto: JDK, *classpath*, formateador, etc.

### Comprobar que Java 21 está reconocido

Hay que distinguir dos JDK:

- **El JDK del proyecto** compila y valida tu código. **Debe ser Java 21.**
- **El entorno de ejecución de la propia extensión.** La extensión de Java incluye un JRE propio, de una versión más reciente, que solo usa para funcionar internamente. Es normal que no sea Java 21 y no afecta a tu código.

Para comprobar el JDK del proyecto:

1. Abre `ConfiguracionInicial.java` y espera a que la extensión termine de cargar.
2. Ejecuta `Java: Configure Java Runtime` desde la Command Palette. Se abre la página **Java Runtime**. En la sección del proyecto, comprueba que el JDK asignado corresponde a **Java 21**, por ejemplo `JavaSE-21` o una ruta como `/usr/lib/jvm/java-21-openjdk-amd64`.
3. En la sección **Java Projects** del Explorer, despliega el proyecto. El nodo **JRE System Library** debe indicar `JavaSE-21`.
4. En la Terminal integrada, `java -version` y `javac -version` deben mostrar la versión 21. Es el JDK que usan los comandos del apartado 8.

Si en los pasos 2 o 3 aparece una versión distinta de la 21, **no la cambies en este ejercicio**. Coméntalo al formador.

### Identificar las extensiones Java activas

1. Abre la vista **Extensions** (**View > Extensions**).
2. Escribe `@installed java` en el buscador.
3. Comprueba que aparecen en la sección **WSL: Ubuntu - Installed**, no solo en la sección local. *Extension Pack for Java* agrupa estas extensiones:

| Extensión | Para qué sirve |
|---|---|
| Language Support for Java(TM) by Red Hat | Servidor de lenguaje: errores, autocompletado, navegación, refactorización y formato |
| Debugger for Java | Ejecutar y depurar |
| Test Runner for Java | Ejecutar pruebas |
| Maven for Java / Gradle for Java | Integración con herramientas de construcción (se usarán más adelante en el curso) |
| Project Manager for Java | Sección **Java Projects** y asistentes para crear clases y paquetes |

Con `@enabled` y `@disabled` puedes filtrar las extensiones activas e inactivas.

### Comprobar el estado del servidor de lenguaje

Todas las funciones inteligentes de Java dependen del **servidor de lenguaje Java**. Para saber si está funcionando:

- **Status Bar**: con un archivo `.java` activo, a la derecha aparece el indicador de la extensión de Java (icono `{}` junto a `Java`). Al pasar el ratón o hacer clic se muestra su estado, por ejemplo si está cargando o listo.
- **Output**: en **View > Output**, elige el canal *Language Support for Java* en el desplegable. Muestra la actividad del servidor.
- **Command Palette**:
  - `Java: Show Build Job Status`: muestra las tareas en curso.
  - `Java: Open Java Language Server Log File`: abre el registro detallado.
- **Si el servidor queda en mal estado**, la Command Palette ofrece `Java: Restart Java Language Server` y, como último recurso, `Java: Clean Java Language Server Workspace`. Este segundo comando borra la caché de la extensión.

## 5. Formato del código

### Format Document

Reformatea todo el archivo según las reglas del formateador de Java:

- Menú contextual del editor > **Format Document**.
- Command Palette > `Format Document`.
- Atajo `Shift + Alt + F`.

La primera vez, si hay más de un formateador disponible, VS Code pregunta cuál usar. Para Java, elige *Language Support for Java(TM) by Red Hat*.

### Format Selection

Reformatea solo el texto seleccionado:

- Menú contextual del editor > **Format Selection**.
- Command Palette > `Format Selection`.
- Atajo `Ctrl + K`, seguido de `Ctrl + F`.

Útil para no tocar el resto de un archivo ajeno.

### Format On Save

Con `editor.formatOnSave` activado, VS Code formatea el archivo cada vez que lo guardas.

- Es cómodo, pero aplicado a todos los lenguajes puede reformatear archivos que no esperabas. Es mejor activarlo solo para Java, con la configuración por lenguaje del apartado 6.
- El formato al guardar **no se aplica** cuando el guardado lo hace Auto Save en modo `afterDelay`. Solo se aplica al guardar manualmente o en los modos `onFocusChange` y `onWindowChange`.

### Tabulaciones frente a espacios

- Una **tabulación** es un único carácter cuyo ancho visual depende del editor.
- Con **espacios**, el ancho es siempre el mismo en cualquier editor.

En Java se suele indentar con **4 espacios**, que es lo que usan los archivos del curso.

El indicador de la Status Bar (`Spaces: 4` o `Tab Size: 4`) muestra lo que usa el archivo activo. Al hacer clic se ofrecen estas opciones:

- **Indent Using Spaces** / **Indent Using Tabs**: cambian lo que se insertará a partir de ahora.
- **Convert Indentation to Spaces** / **Convert Indentation to Tabs**: convierten la indentación existente.

Con **Render Whitespace** en `"all"` se ve la diferencia: un punto por cada espacio, una flecha por cada tabulación.

### El formateador de Java

El formato lo aplica la extensión *Language Support for Java(TM) by Red Hat*, que incorpora el **mismo motor de formato que Eclipse**:

- **Indentación:** toma el tamaño de tabulación y la inserción de espacios de las preferencias del editor.
- **Resto de reglas:** usa el perfil por defecto de Eclipse. Incluye la posición de llaves, los espacios alrededor de operadores, el corte de líneas largas y el ajuste de comentarios Javadoc.
- **Personalización:** `Java: Open Java Formatter Settings with Preview` abre un editor visual de reglas.
- **Perfil de Eclipse:** la preferencia `java.format.settings.url` permite usar un perfil exportado desde Eclipse (un archivo XML). Facilita la migración: un equipo que venga de Eclipse puede mantener exactamente el mismo formato.

> **Requiere el soporte de Java.** Si la extensión no está lista, **Format Document** no hace nada o VS Code indica que no hay formateador para Java.

### Actividad de formato (temporal)

1. En `ConfiguracionInicial.java`, selecciona las líneas del bucle `for`, desde `for (String preferencia : preferencias) {` hasta `numero++;`.
2. Pulsa `Shift + Tab` varias veces, o ejecuta `Outdent Line` desde la Command Palette, hasta que esas líneas queden pegadas al margen izquierdo.
3. Desordena también la firma del método auxiliar: en `private static void mostrarPreferencia(int numero, String clave, String categoria) {`, borra los espacios que hay después de las comas y el que hay antes de `{`.
4. Ejecuta **Format Document** (clic derecho > **Format Document**).

**Resultado esperado:** el bucle recupera sus tres niveles de indentación y la firma del método vuelve a tener los espacios correctos. El archivo queda exactamente como al principio.

Para comprobarlo, ejecuta `File: Compare Active File with Saved`: no debe mostrar diferencias. Si quedara algún cambio, `File: Revert File` vuelve a la versión guardada.

Para que el resultado sea exactamente el original, `editor.tabSize` debe ser `4` y `editor.insertSpaces` debe estar activado (valores por defecto). Con otro tamaño de tabulación, el formateador reindentaría todo el archivo con ese tamaño.

## 6. Configuración mediante settings.json

Todo lo que se cambia en el editor visual de Settings se guarda en archivos `settings.json`. Para editarlos directamente:

- Icono **Open Settings (JSON)** en la esquina superior derecha de Settings (respeta la pestaña de ámbito seleccionada).
- Command Palette:
  - `Preferences: Open User Settings (JSON)`.
  - `Preferences: Open Remote Settings (JSON) (WSL: Ubuntu)`.
  - `Preferences: Open Workspace Settings (JSON)`.

### Ámbitos de configuración

| Ámbito | Dónde se guarda | A qué afecta |
|---|---|---|
| **User** (usuario) | Perfil de VS Code en Windows | A **todas** las ventanas y carpetas que abras, en local o en WSL |
| **Remote [WSL: Ubuntu]** | Dentro de Ubuntu, en la instalación de VS Code Server | A todas las ventanas conectadas a WSL Ubuntu. Tiene prioridad sobre User. |
| **Workspace** | Archivo `.vscode/settings.json` dentro de la carpeta abierta | **Solo a esa carpeta**. Tiene prioridad sobre User y Remote. |
| **Por lenguaje** | Bloque `"[java]": { ... }` dentro de cualquiera de los anteriores | Solo a los archivos de ese lenguaje, dentro del ámbito donde se escriba |

El ámbito más específico gana: lo que se define en Workspace prevalece sobre User, y lo que se define para `[java]` prevalece sobre la preferencia general.

Algunas preferencias, como el tamaño de fuente o el tema de color, son personales y tiene sentido dejarlas en **User**. Otras, como el formato o la indentación, afectan a cómo se guarda el código. Conviene compartirlas a nivel de **Workspace** para que todo el equipo use las mismas.

### Ejemplo 1. Preferencias de usuario

Contenido de ejemplo para el `settings.json` de **User**, con las preferencias del apartado 3. Si el archivo ya contiene otras preferencias, añade solo las líneas que te interesen, separadas por comas, dentro de las llaves existentes.

```json
{
  "editor.fontSize": 16,
  "editor.tabSize": 4,
  "editor.insertSpaces": true,
  "editor.wordWrap": "on",
  "editor.minimap.enabled": false,
  "editor.renderWhitespace": "all",
  "files.autoSave": "afterDelay",
  "files.autoSaveDelay": 1000
}
```

Ámbito: **global**. Afecta a todas las carpetas y a todos los lenguajes.

### Ejemplo 2. Preferencias específicas de Java

Se pueden añadir al `settings.json` de **User** para aplicarlas a todos los archivos Java, sin afectar a otros lenguajes:

```json
{
  "[java]": {
    "editor.tabSize": 4,
    "editor.insertSpaces": true,
    "editor.detectIndentation": false,
    "editor.formatOnSave": true,
    "editor.defaultFormatter": "redhat.java"
  }
}
```

Ámbito: **global, pero solo para Java**. `redhat.java` es el identificador de la extensión *Language Support for Java(TM) by Red Hat*.

### Ejemplo 3. Preferencias de un proyecto (workspace)

Así sería el archivo `.vscode/settings.json` de un proyecto Java de equipo que quiera fijar el formato para todos sus miembros:

```json
{
  "[java]": {
    "editor.formatOnSave": true
  },
  "java.format.settings.url": "formato/eclipse-formatter.xml"
}
```

Ámbito: **solo ese proyecto**. El archivo se guarda dentro de la carpeta del proyecto y puede versionarse junto con el código. La ruta del perfil de formato es relativa a la carpeta del proyecto. Es un ejemplo ilustrativo: el archivo `formato/eclipse-formatter.xml` no existe en este curso.

> **No crees `.vscode/settings.json` en la raíz del repositorio del curso.** Afectaría a todas las prácticas a la vez. En este ejercicio, las preferencias se cambian solo a nivel de **User**.

## 7. Práctica guiada

Sigue los pasos en orden, con `ConfiguracionInicial.java` abierto en el editor.

### Paso 1. Abrir las preferencias

Abre Settings con **File > Preferences > Settings** (o `Ctrl + ,`). Comprueba que estás en la pestaña **User**.

**Resultado esperado:** ves el buscador, las pestañas *User*, *Remote [WSL: Ubuntu]* y *Workspace*, y el árbol de categorías.

### Paso 2. Modificar el tamaño de fuente

1. Busca `editor.fontSize`.
2. Cambia el valor a `16`.
3. Vuelve a la pestaña de `ConfiguracionInicial.java`.

**Resultado esperado:** el texto del editor se ve más grande y el resto de la interfaz no cambia. En Settings, la preferencia muestra la barra azul de "modificada".

### Paso 3. Activar Word Wrap

1. Localiza en el código la línea `String descripcionLarga = "..."`. Sin ajuste, se sale por la derecha de la ventana.
2. Activa el ajuste con **View > Word Wrap** (o `Alt + Z`).
3. Para que quede como preferencia permanente, busca `editor.wordWrap` en Settings y elige `on`.

**Resultado esperado:** la línea larga se muestra partida en varias líneas visuales. El número de línea a la izquierda no cambia, porque el archivo sigue teniendo una sola línea.

### Paso 4. Configurar la visualización de espacios

1. Busca `editor.renderWhitespace` y elige `all`.
2. Observa el código.

**Resultado esperado:** aparece un punto tenue por cada espacio. La indentación del bucle `for` muestra 4 puntos por nivel y no hay flechas, porque el archivo no contiene tabulaciones.

### Paso 5. Comprobar la indentación

1. Mira el indicador de indentación en la Status Bar con `ConfiguracionInicial.java` activo.
2. Haz clic en él y observa las opciones (**Indent Using Spaces**, **Indent Using Tabs**, **Convert Indentation to Spaces**…). Cierra el menú con `Esc` sin elegir nada.
3. Sitúa el cursor al principio de una línea vacía dentro de `main` y pulsa `Tab` una vez. Con Render Whitespace en `all` verás que se insertan 4 puntos (espacios). Deshaz con `Ctrl + Z`.

**Resultado esperado:** el indicador muestra `Spaces: 4`. Si muestra otro valor, no lo cambies todavía y coméntalo al formador: el paso 7 necesita `Spaces: 4`.

### Paso 6. Activar temporalmente Auto Save

1. Activa **File > Auto Save**. La opción queda marcada y `files.autoSave` pasa a `afterDelay`.
2. Escribe un espacio al final de cualquier línea de comentario. La pestaña muestra durante un instante el círculo de "sin guardar" y, tras un segundo, se guarda sola.
3. Borra ese espacio y espera a que se guarde de nuevo.
4. **Desactiva Auto Save** con **File > Auto Save**.

**Resultado esperado:** con Auto Save activo, el archivo se guarda solo. Al terminar el paso, la opción queda desmarcada y el archivo no tiene cambios: `File: Compare Active File with Saved` no muestra diferencias.

### Paso 7. Aplicar Format Document

Realiza la **actividad de formato** del apartado 5: desordena la indentación del bucle `for` y la firma de `mostrarPreferencia`, y ejecuta **Format Document**. Después guarda con **File > Save**.

**Resultado esperado:** el código recupera exactamente su formato original.

### Paso 8. Comprobar Java 21

1. Ejecuta `Java: Configure Java Runtime` desde la Command Palette y revisa el JDK del proyecto.
2. Despliega **JRE System Library** en la sección **Java Projects** del Explorer.
3. En la Terminal integrada, ejecuta `java -version` y `javac -version`.

**Resultado esperado:** las tres comprobaciones indican Java 21.

### Paso 9. Restaurar las preferencias que no quieras conservar

1. En Settings (pestaña **User**), escribe `@modified` en el buscador. Aparecen todas las preferencias que difieren de su valor por defecto.
2. Para cada una que quieras descartar, pasa el ratón por encima, haz clic en el engranaje y elige **Reset Setting**.
3. Como alternativa, abre `Preferences: Open User Settings (JSON)` y borra las líneas correspondientes.

**Resultado esperado:** `@modified` solo muestra las preferencias que has decidido conservar. `ConfiguracionInicial.java` está guardado y sin cambios respecto al original. No se ha creado ninguna carpeta `.vscode/` en el repositorio.

Antes de este ejercicio puede que ya tuvieras preferencias personalizadas. Restaura solo las que has cambiado tú en esta práctica.

## 8. Compilación y ejecución

### Desde VS Code

Abre `ConfiguracionInicial.java` y haz clic en **Run** sobre el método `main`, o usa el botón **Run Java** de la esquina superior derecha del editor. La salida aparece en la **Terminal** o en la **Debug Console**, según la configuración.

### Desde la terminal, en la carpeta del ejercicio

La Terminal integrada se abre en la raíz del repositorio. Entra primero en la carpeta de la práctica:

```bash
cd modulo-01-vscode/tema-1.1-conceptos-basicos/apartado-1.1.3-configuracion-inicial
javac --release 21 -Xlint:all -d out src/curso/vscode/ConfiguracionInicial.java
java -cp out curso.vscode.ConfiguracionInicial
```

Para volver a la raíz del repositorio: `cd -`.

### Desde la terminal, en la raíz del repositorio

```bash
EJ=modulo-01-vscode/tema-1.1-conceptos-basicos/apartado-1.1.3-configuracion-inicial
javac --release 21 -Xlint:all -d "$EJ/out" "$EJ/src/curso/vscode/ConfiguracionInicial.java"
java -cp "$EJ/out" curso.vscode.ConfiguracionInicial
```

Los `.class` se generan en la carpeta `out/` del ejercicio, excluida en el `.gitignore`.

### Salida esperada

```text
Editor: Visual Studio Code
Tamaño de tabulación recomendado: 4
VS Code permite ajustar el tamaño de fuente, la tabulación, el ajuste de línea, el minimapa, la visualización de espacios y el guardado automático, tanto para todos los proyectos como para uno en concreto.

Preferencias que se revisan en este ejercicio:
  1. editor.fontSize (editor)
  2. editor.tabSize (editor)
  3. editor.insertSpaces (editor)
  4. editor.wordWrap (editor)
  5. editor.minimap.enabled (editor)
  6. editor.renderWhitespace (editor)
  7. files.autoSave (archivos)

Total de preferencias: 7
```

La línea larga se muestra entera o partida según el ancho de la terminal. Eso depende de la terminal, no de *Word Wrap*, que solo afecta al editor.

## 9. Comparación con Eclipse e IntelliJ IDEA

| Aspecto | Eclipse | IntelliJ IDEA | VS Code | Observaciones |
|---|---|---|---|---|
| Acceso a preferencias | **Window > Preferences** | **File > Settings** (`Ctrl + Alt + S`) | **File > Preferences > Settings** (`Ctrl + ,`) | VS Code ofrece además la edición directa en `settings.json`. |
| Opciones de editor | **General > Editors > Text Editors** y **Java > Editor** | **Editor > General**, **Editor > Font** | Settings > **Text Editor**, o `editor.*` en `settings.json` | Equivalente. En VS Code muchas opciones tienen además un acceso directo en el menú **View**. |
| Formato de código | **Source > Format** (`Ctrl + Shift + F`). Perfil en **Java > Code Style > Formatter** | **Code > Reformat Code** (`Ctrl + Alt + L`). Reglas en **Editor > Code Style > Java** | **Format Document** (`Shift + Alt + F`) y **Format Selection** | El formateador de VS Code para Java es el de Eclipse, así que puede importar un perfil de Eclipse con `java.format.settings.url`. |
| Guardado automático | Desactivado por defecto. Activable en **General > Editors > Autosave** | Activado por defecto (al cambiar de ventana o en reposo) | Desactivado por defecto. **File > Auto Save** o `files.autoSave` | Quien venga de IntelliJ notará que VS Code no guarda solo hasta que se active Auto Save. |
| Formato al guardar | **Java > Editor > Save Actions** | **Tools > Actions on Save** | `editor.formatOnSave`, mejor dentro de `"[java]"` | Equivalente en propósito. |
| Configuración del JDK | **Java > Installed JREs** y **Java Build Path** del proyecto | **File > Project Structure** (SDK del proyecto) | `Java: Configure Java Runtime` y la preferencia `java.configuration.runtimes` | En proyectos Maven o Gradle, la versión de Java la marca el archivo de construcción. En carpetas sin herramienta de construcción, la elige la extensión. |
| Preferencias por proyecto | **Project > Properties** (se guardan en `.settings/`) | Carpeta `.idea` del proyecto | `.vscode/settings.json` en la carpeta del proyecto (ámbito **Workspace**) | Concepto similar. En VS Code son archivos JSON legibles y fáciles de versionar. |
| Gestión de extensiones | **Help > Eclipse Marketplace** | **Settings > Plugins** | Vista **Extensions** (**View > Extensions**) | En VS Code con WSL, las extensiones de lenguaje se instalan en el lado de WSL, no en Windows. |

## 10. Comprobaciones finales

Comprueba que eres capaz de:

- [ ] Abrir Settings desde el menú y con `Ctrl + ,`, y buscar una preferencia por su nombre o su identificador.
- [ ] Cambiar el tamaño de fuente, activar Word Wrap, ocultar el minimapa y mostrar los espacios en blanco.
- [ ] Explicar la diferencia entre `editor.tabSize`, `editor.insertSpaces` y `editor.detectIndentation`, y consultar en la Status Bar la indentación del archivo activo.
- [ ] Activar y desactivar Auto Save, y explicar por qué no conviene combinar `afterDelay` con Format On Save.
- [ ] Formatear un documento y una selección, y comprobar con **Compare Active File with Saved** que el archivo no tiene cambios.
- [ ] Localizar las preferencias de la extensión de Java y comprobar que el proyecto usa Java 21.
- [ ] Identificar las extensiones Java instaladas en WSL y consultar el estado del servidor de lenguaje.
- [ ] Explicar la diferencia entre configuración de usuario, de entorno remoto, de *workspace* y por lenguaje, y decidir en qué ámbito va cada preferencia.
- [ ] Restaurar con `@modified` y **Reset Setting** las preferencias que no quieras conservar.
- [ ] Compilar y ejecutar `ConfiguracionInicial` desde VS Code y desde la terminal.
