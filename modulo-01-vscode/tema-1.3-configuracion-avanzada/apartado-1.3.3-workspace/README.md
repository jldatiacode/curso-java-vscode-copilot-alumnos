# Ejercicio 1.3.3 — Configuración del workspace

Módulo 1. Introducción a VS Code · Tema 1.3. Configuración avanzada de VS Code · Apartado 1.3.3. Configuración del workspace

> **Antes de empezar:** si aún no lo has hecho en este tema, actualiza tu copia del repositorio desde su raíz con `git status` (sin cambios pendientes) y `git pull --ff-only`, como se explica al principio del ejercicio 1.3.1.

## 1. Introducción

En el ejercicio 1.1.3 se vio que VS Code guarda las preferencias en varios ámbitos (usuario, remoto, *workspace*) y que el más específico prevalece. Este ejercicio profundiza en esos niveles y en todo lo que permite adaptar VS Code a cada proyecto:

- distinguir lo que es **personal** de lo que se **comparte con el equipo**;
- configurar Java por proyecto;
- trabajar con varias carpetas a la vez;
- aislar entornos con perfiles;
- definir cómo se ejecuta un programa.

### Conceptos

| Concepto | Qué es | Cuándo existe |
|---|---|---|
| **Folder** | Una carpeta abierta con **File > Open Folder...** o `code <carpeta>` | Siempre que se trabaja con una sola carpeta |
| **Workspace** | Lo que está abierto en la ventana: una carpeta o un conjunto de carpetas | En cuanto se abre una carpeta. Una ventana sin carpeta no tiene *workspace*. |
| **Multi-root Workspace** | Varias carpetas en la misma ventana, definidas en un archivo `.code-workspace` | Solo si se añaden carpetas (**File > Add Folder to Workspace...**) o se abre un `.code-workspace` |
| **User Settings** | Preferencias de tu usuario, para todas las ventanas | Siempre |
| **Remote Settings** | Preferencias del entorno remoto: WSL, SSH, contenedores | Solo cuando VS Code está conectado a un entorno remoto (`WSL: Ubuntu` en la Status Bar) |
| **Workspace Settings** | Preferencias de lo que está abierto en la ventana | Con una carpeta, se guardan en `<carpeta>/.vscode/settings.json`. Con un multi-root, dentro del archivo `.code-workspace`. |
| **Folder Settings** | Preferencias de **una** de las carpetas de un multi-root | Solo en multi-root. Se guardan en `<carpeta>/.vscode/settings.json`. |
| **Profile** | Conjunto con nombre de preferencias de usuario, atajos, fragmentos de código y extensiones | Siempre hay uno activo: **Default**, si no has creado otros |

### Precedencia

De menor a mayor prioridad: el último gana.

```text
Default (valores de VS Code)
  → User (del perfil activo)
    → Remote (si hay conexión remota)
      → Workspace
        → Folder (solo en multi-root)
```

Hay dos matices importantes:

1. **Las preferencias específicas de lenguaje** (`"[java]": { ... }`) **prevalecen sobre las generales, aunque estén en un nivel inferior**. Un `"[java]": {"editor.fontSize": 20}` en **User** gana a un `"editor.fontSize": 18` en **Workspace** para los archivos Java. Se comprueba en la actividad 4.
2. **No todas las preferencias admiten todos los niveles.** Algunas solo tienen sentido para toda la aplicación o para la máquina. VS Code las muestra atenuadas, con un aviso, si se escriben en un nivel no permitido.

## 2. Configuración mediante la interfaz

Abre Settings con **File > Preferences > Settings** (`Ctrl + ,`).

| Operación | Cómo |
|---|---|
| **Alternar User y Workspace** | Pestañas bajo el buscador: **User**, **Remote [WSL: Ubuntu]** (si aplica), **Workspace**. En multi-root aparece además una lista para elegir **Folder**. |
| **Buscar una propiedad** | Por nombre visible (`font size`) o identificador (`editor.fontSize`) |
| **Modificarla** | En la pestaña del nivel deseado: el cambio se guarda en el `settings.json` de ese nivel |
| **Restaurarla** | Engranaje junto a la propiedad > **Reset Setting**, en la pestaña del nivel donde se cambió |
| **Ver si está sobrescrita** | Bajo el nombre aparecen avisos como *Modified in: Workspace* o *Also modified in: User*. Si cambias un valor en **User** y la propiedad está fijada en **Workspace**, en esta ventana no notarás el cambio. |
| **Ver las modificadas** | Filtro `@modified`, en cada pestaña |

Otros filtros útiles del buscador:

| Filtro | Muestra |
|---|---|
| `@modified` | Preferencias con valor distinto del predeterminado en esa pestaña |
| `@ext:redhat.java` | Preferencias de la extensión de Java |
| `@lang:java` | Preferencias que se aplicarán **solo a Java**. Lo que cambies con este filtro se guarda dentro de `"[java]"`. |
| `@id:editor.fontSize` | Una propiedad concreta por su identificador |

## 3. settings.json

### Cómo abrirlo

| Nivel | Command Palette | Archivo |
|---|---|---|
| User | `Preferences: Open User Settings (JSON)` | Perfil de usuario de VS Code (en Windows, aunque trabajes con WSL) |
| Remote | `Preferences: Open Remote Settings (JSON) (WSL: Ubuntu)` | Dentro de WSL, en la instalación de VS Code Server |
| Workspace | `Preferences: Open Workspace Settings (JSON)` | `.vscode/settings.json` de la carpeta abierta, o el `.code-workspace` |
| Folder | `Preferences: Open Folder Settings (JSON)` | `.vscode/settings.json` de la carpeta elegida (multi-root) |

También puedes usar el icono **Open Settings (JSON)** de la esquina superior derecha de Settings, que abre el archivo de la pestaña activa.

### Estructura

Es un único objeto JSON con pares `"propiedad": valor`. Las preferencias de un lenguaje se agrupan en una clave con el nombre del lenguaje entre corchetes:

```json
{
  "editor.tabSize": 4,
  "editor.insertSpaces": true,
  "editor.formatOnSave": true,
  "[java]": {
    "editor.tabSize": 4,
    "editor.formatOnSave": true
  }
}
```

- **Propiedades globales.** Las de primer nivel (`editor.tabSize`…) se aplican a todos los lenguajes.
- **Propiedades por lenguaje.** Las de `"[java]"` solo a los archivos Java, y prevalecen sobre las globales. En este ejemplo, `editor.formatOnSave` está activado para todos los lenguajes. Lo normal es activarlo **solo** dentro de `"[java]"`, para no reformatear otros archivos sin querer.

### Comentarios JSONC

Los `settings.json` de VS Code son **JSONC** (JSON con comentarios): admiten comentarios `//` y `/* */`, y comas finales. Sirven para documentar por qué se ha configurado algo. Esto no se aplica a cualquier `.json`: por eso los archivos de `ejemplos/` de este ejercicio son JSON estricto, sin comentarios.

### Validación del esquema

VS Code conoce todas las propiedades válidas, incluidas las de las extensiones instaladas, y valida el archivo mientras escribes:

- `Ctrl + Space` sugiere propiedades y valores.
- Una propiedad desconocida se subraya con el aviso *Unknown Configuration Setting*. Puede ser un error de escritura o una propiedad de una extensión que no tienes instalada.
- Un valor de tipo incorrecto, como un texto donde se espera un número, se marca como error.
- Al pasar el ratón sobre una propiedad se ve su descripción y su valor por defecto.

### Personal frente a compartido

| Preferencias **personales** (User) | Configuración **compartida** (Workspace, en el repositorio) |
|---|---|
| Tema, iconos, fuente, zoom, Minimap | Tabulación e indentación del proyecto |
| Perfil y fuente de la terminal | Formato al guardar y formateador |
| Atajos de teclado | Codificación y fin de línea de los archivos |
| Rutas de los JDK instalados en tu equipo | Carpetas de código fuente o de librerías del proyecto, con rutas relativas |
| Auto Save | Exclusión de carpetas generadas (`out/`, `target/`) |

Regla práctica: si afecta a **cómo se guarda o se interpreta el código**, se comparte. Si afecta a **cómo lo ves o lo usas tú**, es personal.

## 4. Configuración específica de Java

### Dos JDK distintos

La extensión de Java maneja dos JDK que conviene no confundir:

| | JDK de las herramientas | JDK del proyecto |
|---|---|---|
| Para qué | Ejecutar el servidor de lenguaje Java, que analiza el código y ofrece autocompletado, errores, refactorización… | Compilar, validar y ejecutar **tu** código |
| Cuál es | Un JRE **incluido en la extensión**, de una versión reciente | El JDK 21 que has instalado |
| Propiedad | `java.jdt.ls.java.home` (normalmente no se toca) | `java.configuration.runtimes`, o la versión indicada en `pom.xml` / `build.gradle` |
| ¿Afecta a tu código? | No | Sí: determina qué versión del lenguaje se acepta |

### Gestión de varios JDK: `java.configuration.runtimes`

Si tienes varios JDK instalados, decláralos en la configuración de **usuario** (o **Remote**, si trabajas en WSL) y marca uno como predeterminado. El predeterminado se usa en las carpetas sin Maven ni Gradle, como las de este curso.

Las rutas de estos ejemplos son ilustrativas: sustitúyelas por las de tu equipo.

Windows (en `settings.json`, las `\` se escriben dobles):

```json
{
  "java.configuration.runtimes": [
    { "name": "JavaSE-21", "path": "C:\\ruta\\a\\jdk-21", "default": true },
    { "name": "JavaSE-17", "path": "C:\\ruta\\a\\jdk-17" }
  ]
}
```

Linux o WSL (por ejemplo, con los paquetes OpenJDK de Ubuntu):

```json
{
  "java.configuration.runtimes": [
    { "name": "JavaSE-21", "path": "/usr/lib/jvm/java-21-openjdk-amd64", "default": true },
    { "name": "JavaSE-17", "path": "/usr/lib/jvm/java-17-openjdk-amd64" }
  ]
}
```

- **Nombre.** `name` debe ser un identificador de entorno de ejecución: `JavaSE-17`, `JavaSE-21`, `JavaSE-25`… VS Code los sugiere al escribir.
- **Ruta.** `path` es la carpeta del JDK, la que contiene `bin/`, no la ruta del ejecutable `java`.
- **Ventana conectada a WSL.** Las rutas deben ser **de Linux** y la propiedad debe ir en **Remote Settings**. Una ruta de Windows (`C:\...`) no sirve dentro de WSL, y una de Linux no sirve en Windows.
- **Por qué no se comparte.** Al ser rutas absolutas de tu equipo, esta propiedad **no debe compartirse** en el `settings.json` del proyecto.

### Comprobar el runtime que usa VS Code

- Command Palette > `Java: Configure Java Runtime`: muestra los JDK detectados y el que usa cada proyecto.
- Sección **Java Projects** del Explorer: el nodo **JRE System Library** indica la versión, por ejemplo `JavaSE-21`.
- Command Palette > `Java: Open Project Settings`: configuración Java del proyecto activo.

Si no ves Java 21, **no cambies nada sin consultarlo**: comprueba primero que el JDK 21 está instalado donde VS Code ejecuta la extensión (Windows o WSL).

### Formato de código Java

El formateador de la extensión se configuró en el ejercicio 1.1.3. A nivel de proyecto interesan estas propiedades:

- `java.format.settings.url`: ruta a un perfil de formato de Eclipse (archivo XML) **relativa al proyecto**, para que todo el equipo formatee igual.
- `"[java]": { "editor.defaultFormatter": "redhat.java", "editor.formatOnSave": true }`: formatear al guardar, solo en Java.
- Command Palette > `Java: Open Java Formatter Settings with Preview`: editor visual del perfil de formato.

## 5. Multi-root Workspace

Un **workspace multi-root** reúne varias carpetas independientes en una sola ventana. Se define en un archivo con extensión `.code-workspace`:

```json
{
  "folders": [
    { "path": "apartado-1.3.3-workspace" },
    { "path": "apartado-1.3.2-terminales" }
  ],
  "settings": {}
}
```

- **`folders`.** Lista de carpetas, preferiblemente con rutas **relativas** a la ubicación del archivo.
- **`settings`.** Son las **Workspace Settings** del multi-root. Se aplican a todas las carpetas, salvo que una carpeta tenga sus propias **Folder Settings**.
- **Otras claves.** El archivo puede contener también `launch` (configuraciones de ejecución) y `extensions` (extensiones recomendadas).

| | Abrir una carpeta | Abrir un workspace multi-root |
|---|---|---|
| Cómo | **File > Open Folder...**, `code <carpeta>` | **File > Open Workspace from File...**, `code <archivo>.code-workspace` |
| Configuración de workspace | `.vscode/settings.json` de la carpeta | Dentro del `.code-workspace` |
| Folder Settings | No existen | `.vscode/settings.json` de cada carpeta |
| Explorer | Muestra la carpeta | Muestra cada carpeta como raíz independiente |
| Barra de título | Nombre de la carpeta | Nombre del workspace seguido de `(Workspace)` |

**Cuándo resulta útil:**

- trabajar a la vez en un servicio y en la librería que usa, sin juntarlos en una carpeta;
- tener juntos el *backend* y el *frontend*;
- tener a mano la documentación del proyecto junto al código.

Para un único proyecto Java no hace falta.

**Crear, guardar y volver a abrir:**

1. Con una carpeta abierta, **File > Add Folder to Workspace...** y elige otra carpeta. La ventana pasa a ser un workspace *Untitled*.
2. **File > Save Workspace As...** y guarda el archivo `.code-workspace`.
3. Para volver a abrirlo: **File > Open Workspace from File...** o **File > Open Recent**.
4. Para cerrarlo: **File > Close Workspace**.

**Precauciones:**

- **Dónde guardar el archivo.** Si lo guardas dentro de un repositorio, puede acabar compartido. En el repositorio del curso, el `.gitignore` excluye los archivos `*.code-workspace`, pero en otros proyectos puede no ser así.
- **Configuración en los `settings` del workspace.** Pon ahí solo lo que corresponda a ese conjunto de carpetas, nunca preferencias personales.
- **Rutas.** Usa rutas relativas en `folders`. Una ruta absoluta impide que el archivo funcione en otro equipo o en otro sistema operativo.

## 6. Perfiles de VS Code

Un **perfil** agrupa:

- preferencias de usuario;
- atajos de teclado;
- fragmentos de código (*snippets*);
- tareas;
- extensiones;
- estado de la interfaz.

Permite tener, por ejemplo, un perfil para Java y otro para escribir documentación, cada uno con sus extensiones y su aspecto, y cambiar de uno a otro sin reconfigurar nada.

| Operación | Cómo |
|---|---|
| **Abrir la gestión de perfiles** | **File > Preferences > Profiles**, o engranaje (**Manage**) de la Activity Bar > **Profiles** |
| **Crear** | En el editor de perfiles, **New Profile**: nombre, icono y, en **Copy from**, el punto de partida. Puede ser un perfil vacío, una copia de otro perfil o una plantilla. Puedes elegir qué partes copiar. |
| **Cambiar de perfil** | Engranaje (**Manage**) > **Profiles** > elegir perfil, o Command Palette > `Profiles: Switch Profile` |
| **Saber cuál está activo** | El nombre aparece al pasar el ratón por el engranaje (**Manage**) y en la lista de perfiles |
| **Exportar** | En el editor de perfiles, menú **...** del perfil > **Export...**: a un archivo local (`.code-profile`) o a un *gist* de GitHub |
| **Importar** | **Import Profile...** desde el editor de perfiles, eligiendo el archivo `.code-profile` |
| **Eliminar** | Cambia antes a otro perfil. Después, menú **...** del perfil > **Delete**. |

- **Aislamiento.** Las preferencias de **User** y las extensiones pertenecen al perfil. Un perfil vacío no tiene la extensión de Java hasta que se instala en él. La configuración de **Workspace** pertenece a la carpeta, no al perfil: se aplica sea cual sea el perfil activo.
- **Perfil asociado a una carpeta.** Si cambias de perfil con una carpeta abierta, VS Code recuerda esa elección para esa carpeta y la volverá a usar al abrirla. Para deshacerlo, vuelve a cambiar a **Default** con la carpeta abierta.
- **Plantillas.** Al crear un perfil, VS Code ofrece plantillas (por ejemplo, para Java) que **instalan extensiones**. En este curso no se usan: se parte de una copia del perfil actual.

## 7. Configuración de ejecución

Sin configuración, **Run** (`Ctrl + F5`) ejecuta la clase activa sin argumentos y en la carpeta abierta. Para fijar argumentos, propiedades de la JVM, variables o directorio de trabajo, se usa una **configuración de lanzamiento** en `.vscode/launch.json`.

**Cómo crearla:**

- Vista **Run and Debug** (`Ctrl + Shift + D`) > enlace **create a launch.json file** > **Java**. La extensión genera una configuración por cada clase con `main` que encuentra.
- O bien crea el archivo `.vscode/launch.json` a mano, con el contenido que necesites.

Ejemplo, el mismo de `ejemplos/launch-ejemplo.json`, pensado para la carpeta de este ejercicio abierta sola:

```json
{
  "version": "0.2.0",
  "configurations": [
    {
      "type": "java",
      "name": "ConfiguracionWorkspace (ejemplo del curso)",
      "request": "launch",
      "mainClass": "curso.vscode.ConfiguracionWorkspace",
      "args": ["argumento-1", "argumento con espacios"],
      "vmArgs": "-Dcurso.entorno=vscode",
      "cwd": "${workspaceFolder}",
      "env": { "CURSO_MODO": "launch-json" },
      "console": "integratedTerminal"
    },
    {
      "type": "java",
      "name": "Archivo Java actual",
      "request": "launch",
      "mainClass": "${file}"
    }
  ]
}
```

| Propiedad | Significado |
|---|---|
| `type`, `request` | `"java"` y `"launch"`: lanzar un programa Java |
| `name` | Nombre que aparece en el desplegable de **Run and Debug** |
| `mainClass` | Clase principal con su paquete. `"${file}"` usa el archivo activo. Es la única propiedad obligatoria. |
| `args` | Argumentos del programa (lista o texto) |
| `vmArgs` | Opciones de la JVM, como `-D...` o `-Xmx...` |
| `cwd` | Directorio de trabajo. `${workspaceFolder}` es la carpeta abierta. |
| `env` | Variables de entorno solo para esta ejecución |
| `console` | Dónde se ve la salida: `integratedTerminal` (por defecto), `internalConsole` (Debug Console) o `externalTerminal` |

- **Selección de la clase principal.** Si un proyecto tiene varias clases con `main`, cada configuración indica la suya en `mainClass`. El desplegable de **Run and Debug** permite elegir cuál lanzar.
- **Variables no sensibles.** En `env` y `vmArgs` se ponen **solo valores no sensibles**, porque `launch.json` puede acabar compartido. Para secretos existen archivos de entorno locales, `envFile`, que **nunca** se suben al repositorio.
- **Depuración.** La depuración en profundidad (breakpoints condicionales, *watch*, configuraciones *attach*) se trabaja en el módulo 3.

## 8. Configuración compartida en Git

**Qué compartir:**

- **`.vscode/settings.json`** con la configuración **funcional** del proyecto: indentación, formato al guardar, codificación (`files.encoding`), fin de línea (`files.eol`), exclusiones (`files.exclude`), carpetas de código Java con rutas relativas y perfil de formato con ruta relativa. `ejemplos/settings-ejemplo.json` es un ejemplo de este tipo de archivo.
- **`.vscode/extensions.json`** con las extensiones **recomendadas**. VS Code las sugiere al abrir el proyecto, pero no las instala sin permiso:

  ```json
  { "recommendations": ["vscjava.vscode-java-pack"] }
  ```

- **`.vscode/launch.json`**, si las configuraciones son útiles para todos y no contienen rutas absolutas ni secretos.

**Qué es personal:** tema, fuentes, zoom, atajos, perfiles de terminal, Auto Save y rutas de JDK (`java.configuration.runtimes`).

**Qué no debe incluirse nunca:**

- archivos `.env` o `envFile` con credenciales, y variables con secretos dentro de `launch.json`;
- `settings.json` con rutas absolutas o nombres de usuario (`C:\Users\...`, `/home/...`);
- archivos `.code-workspace` personales;
- carpetas generadas (`out/`, `target/`).

**Riesgos de las rutas absolutas:** dejan de funcionar en otro equipo, revelan datos del tuyo (nombre de usuario, estructura de carpetas) y obligan a cada compañero a "arreglar" el archivo, lo que genera cambios continuos en Git.

**Windows frente a Linux:**

- **Fin de línea.** Windows usa `CRLF` y Linux `LF`. Fija `"files.eol": "\n"` en el proyecto para que VS Code cree los archivos nuevos igual en todos los equipos. Git tiene además sus propias opciones, que se ven en el módulo 2.
- **Rutas.** Usa `/` en las rutas relativas: VS Code y Java las aceptan en los dos sistemas. Evita `\`.
- **Mayúsculas en los nombres de archivo.** Linux distingue `Alumno.java` de `alumno.java`; Windows no. Un error de este tipo puede funcionar en un equipo y fallar en otro.
- **Comandos de terminal.** Las tareas o comandos escritos para PowerShell no funcionan en Bash, y al revés.
- **Perfiles de terminal y JDK.** Sus nombres y rutas cambian entre sistemas. Son personales.

**Buenas prácticas en equipo:**

- acordar qué se comparte y revisarlo como cualquier otro código;
- mantener el `settings.json` del proyecto pequeño y comentado, porque es JSONC;
- recomendar extensiones en lugar de exigirlas;
- comprobar con `git status` que no se suben archivos de configuración personal por accidente.

En el repositorio del curso, el `.gitignore` excluye la carpeta `.vscode/` salvo `settings.json`, `extensions.json`, `launch.json` y `tasks.json`, y también excluye `*.code-workspace`. Así, lo que crees en `.vscode/` durante esta práctica aparecerá en `git status` como archivo nuevo **sin seguimiento**. Por eso la última actividad lo elimina.

## 9. Práctica guiada

Durante la práctica, las configuraciones de *workspace* se crean **solo dentro de la carpeta de este ejercicio** (`apartado-1.3.3-workspace`), abriéndola en una ventana aparte. **Nunca se crean en la raíz del repositorio**, para no afectar a los demás ejercicios.

Al empezar, en la ventana habitual (raíz del repositorio), abre Settings, pestaña **User**, filtro `@modified`, y **anota** lo que aparece.

### Actividad 1. Identificar los niveles de configuración

1. En la ventana de la raíz del repositorio, abre Settings y observa las pestañas: **User**, **Workspace** y, si trabajas con WSL, **Remote [WSL: Ubuntu]**.
2. Abre la carpeta del ejercicio en una **ventana nueva**, con una de estas dos opciones:
   - **File > New Window**; en la nueva ventana, **File > Open Folder...** y elige `modulo-01-vscode/tema-1.3-configuracion-avanzada/apartado-1.3.3-workspace`.
   - Desde la Terminal integrada de la raíz: `code modulo-01-vscode/tema-1.3-configuracion-avanzada/apartado-1.3.3-workspace`. El comando es igual en PowerShell y Bash.
3. Si VS Code pregunta si confías en los autores, responde que sí.
4. En la nueva ventana, ejecuta `Preferences: Open Workspace Settings (JSON)`.

**Resultado esperado:**

- Paso 2: el Explorer muestra solo `ejemplos`, `src` y `README.md`.
- Paso 4: VS Code abre un `.vscode/settings.json` vacío (`{}`) dentro de la carpeta del ejercicio. Según la versión, el archivo puede crearse ya en disco y verse la carpeta `.vscode` en el Explorer. No pasa nada: se usará en la actividad 3 y se elimina en la actividad 10. Ciérralo sin escribir nada.

A partir de aquí, trabaja en esta **ventana del ejercicio**, salvo que se indique otra cosa.

**Deshacer:** nada que deshacer todavía.

### Actividad 2. Cambiar una propiedad de usuario

1. En Settings, pestaña **User**, busca `editor.fontSize` y pon `16`.
2. Comprueba que el cambio se ve en las **dos** ventanas: la de la raíz y la del ejercicio.

**Resultado esperado:** las preferencias de usuario afectan a todas las ventanas.

**Deshacer:** en la actividad 10, o ya mismo con **Reset Setting**.

### Actividad 3. Cambiar una propiedad de workspace

1. En la ventana del ejercicio, Settings, pestaña **Workspace**, busca `editor.fontSize` y pon `18`.
2. Observa en el Explorer la nueva carpeta `.vscode` con `settings.json`. Ábrelo: contiene `"editor.fontSize": 18`.

**Resultado esperado:** solo la ventana del ejercicio usa 18. La de la raíz sigue en 16.

**Deshacer:** en la actividad 10, borrando la carpeta `.vscode` del ejercicio.

### Actividad 4. Comprobar la precedencia

1. En la ventana del ejercicio, Settings, pestaña **User**: busca `editor.fontSize`. Bajo la propiedad aparece un aviso de que también está modificada en **Workspace**.
2. Cambia en **User** el valor a `12`. En esta ventana **no cambia nada**, porque Workspace prevalece. En la ventana de la raíz sí cambia.
3. Abre `Preferences: Open User Settings (JSON)` y añade, dentro de las llaves, este bloque. Si hay otras propiedades, sepáralo con una coma.

   ```json
   "[java]": {
     "editor.fontSize": 20
   }
   ```

4. Abre `src/curso/vscode/ConfiguracionWorkspace.java` y `README.md` en la ventana del ejercicio y compara el tamaño de letra.
5. **Borra** el bloque `"[java]"` que acabas de añadir en el `settings.json` de usuario y guarda.

**Resultado esperado:**

- Paso 2: la ventana del ejercicio sigue en 18 y la de la raíz pasa a 12.
- Paso 4: el archivo Java se ve con tamaño 20 y el README con 18. La preferencia de lenguaje de **User** prevalece sobre la general de **Workspace**.
- Paso 5: el Java vuelve a 18.

**Deshacer:** el paso 5 ya elimina el bloque. El valor de usuario se restaura en la actividad 10.

### Actividad 5. Crear una configuración específica de Java en el workspace

1. Abre `ejemplos/settings-ejemplo.json` y, al lado, el `.vscode/settings.json` del ejercicio (`Preferences: Open Workspace Settings (JSON)`).
2. Copia en `.vscode/settings.json` todas las propiedades del ejemplo, conservando `"editor.fontSize": 18`. Guarda. Las propiedades son:
   - indentación con 4 espacios;
   - fin de línea `\n`;
   - ocultar `out/`;
   - `java.project.sourcePaths`;
   - bloque `"[java]"` con formateador y formato al guardar.
3. En `ConfiguracionWorkspace.java`, selecciona el cuerpo del método `contarOpcionesDeLanzamiento` y pulsa `Shift + Tab` hasta dejarlo pegado al margen.
4. Guarda con **File > Save** (`Ctrl + S`).
5. Abre `README.md`, añade un espacio al final de cualquier línea y guarda. Después deshaz con `Ctrl + Z` y vuelve a guardar.

**Resultado esperado:**

- Paso 4: al guardar, el método recupera su indentación sin hacer nada más. Es *Format On Save* en acción, solo para Java.
- Paso 5: al guardar el README, el espacio final desaparece (`files.trimTrailingWhitespace`), pero el README no se reformatea.
- Al terminar, `ConfiguracionWorkspace.java` está exactamente como al principio. Compruébalo con `File: Compare Active File with Saved`. Si tienes dudas, `git status` desde la raíz del repositorio no debe mostrarlo como modificado.

**Deshacer:** en la actividad 10.

### Actividad 6. Consultar el JDK

1. En la ventana del ejercicio, ejecuta `Java: Configure Java Runtime`. Localiza el JDK asignado al proyecto de esta carpeta.
2. En **Java Projects**, despliega el proyecto y localiza **JRE System Library**.
3. En Settings, pestaña **User** (y **Remote** si usas WSL), busca `java.configuration.runtimes` y observa si tiene valor. **No lo cambies.**
4. Ejecuta `Java: Open Project Settings` y revisa la sección del JDK.

**Resultado esperado:** el proyecto usa Java 21 (`JavaSE-21`). Sabes dónde se configuraría otro JDK si fuera necesario.

**Deshacer:** nada que deshacer.

### Actividad 7. Crear un workspace con varias carpetas

1. En la ventana del ejercicio, **File > Add Folder to Workspace...** y elige la carpeta hermana `apartado-1.3.2-terminales`. No muevas ni copies nada.
2. Observa el Explorer (dos raíces) y la barra de título (*Untitled (Workspace)*).
3. **File > Save Workspace As...**: guárdalo en la carpeta del tema, `modulo-01-vscode/tema-1.3-configuracion-avanzada/`, con el nombre `tema-1.3.code-workspace`.
4. Abre ese archivo (**File > Open File...**) y comprueba que las rutas de `folders` son relativas.
5. Abre Settings: aparecen **User**, **Workspace** y **Folder**, con una lista para elegir la carpeta. En **Workspace** pon `editor.fontSize` a `14`.
6. Abre `README.md` de cada carpeta.
7. Cierra el workspace con **File > Close Workspace** y vuelve a abrir la carpeta del ejercicio con **File > Open Recent**.

**Resultado esperado:**

- Paso 4: el archivo contiene `"path": "apartado-1.3.3-workspace"` y `"path": "apartado-1.3.2-terminales"`. Tras el paso 5 incluye además `"settings": { "editor.fontSize": 14 }`.
- Paso 6: el README de `apartado-1.3.2-terminales` se ve con 14 (Workspace). El de `apartado-1.3.3-workspace` se ve con 18, porque su `.vscode/settings.json` actúa como **Folder Settings** y prevalece.

**Deshacer:** en la actividad 10 se borra `tema-1.3.code-workspace`. Ya está excluido del control de versiones, pero no debe quedar en tu copia.

### Actividad 8. Crear un perfil de Java

1. Abre **File > Preferences > Profiles**.
2. Pulsa **New Profile**. Nombre: `Java - Curso`. En **Copy from**, elige el perfil actual (**Default**). Así se copian sus preferencias y extensiones y no se instala nada nuevo. Pulsa **Create**.
3. Activa el perfil `Java - Curso` en la ventana del ejercicio.
4. En este perfil, cambia el tema de color a **Light Modern** (**File > Preferences > Theme > Color Theme**).
5. Vuelve a la ventana de la raíz: sigue con el perfil **Default** y su tema.
6. Opcional: en el editor de perfiles, menú **...** de `Java - Curso` > **Export...** > a un archivo local, **fuera del repositorio** (por ejemplo, en tu carpeta de Documentos).
7. En la ventana del ejercicio, vuelve al perfil **Default**: el tema vuelve a ser el original.

**Resultado esperado:** cada perfil tiene sus propias preferencias de usuario. El cambio de tema solo afecta a `Java - Curso`. Las preferencias de **Workspace** del ejercicio se mantienen en los dos perfiles.

**Deshacer:** en la actividad 10 se elimina el perfil y el archivo exportado.

### Actividad 9. Probar una configuración de ejecución

1. En la ventana del ejercicio, con el perfil **Default**, crea el archivo `.vscode/launch.json`, en la misma carpeta `.vscode` que `settings.json`, con el contenido de `ejemplos/launch-ejemplo.json`. Puedes copiarlo y pegarlo entero.
2. Abre la vista **Run and Debug** (`Ctrl + Shift + D`). En el desplegable superior aparecen las dos configuraciones.
3. Elige **ConfiguracionWorkspace (ejemplo del curso)** y pulsa **Run > Run Without Debugging** (`Ctrl + F5`).
4. Elige **Archivo Java actual**, abre `ConfiguracionWorkspace.java` y ejecútalo de nuevo con `Ctrl + F5`.

**Resultado esperado:**

- Paso 3: la salida, en la Terminal, muestra los valores de `launch.json`:

  ```text
  == Configuración de ejecución ==
  Directorio de trabajo:                     <ruta>/apartado-1.3.3-workspace
  Encuentra ejemplos/settings-ejemplo.json:  sí
  Argumentos (args):                         [argumento-1, argumento con espacios]
  Propiedad curso.entorno (vmArgs):          vscode
  Variable CURSO_MODO (env):                 launch-json
  Versión de Java:                           21...

  Opciones de lanzamiento detectadas: 3 de 3
  Se ha aplicado una configuración de lanzamiento completa.
  ```

- Paso 4: no hay argumentos, propiedades ni variables (`0 de 3`), pero el directorio de trabajo sigue siendo la carpeta abierta.

**Deshacer:** en la actividad 10, junto con la carpeta `.vscode`.

### Actividad 10. Restaurar el entorno original

1. **Perfil.** Comprueba que la ventana del ejercicio usa el perfil **Default**. En **File > Preferences > Profiles**, elimina `Java - Curso` (menú **...** > **Delete**). Borra el archivo `.code-profile` exportado, si lo creaste.
2. **Configuración del ejercicio.** En el Explorer de la ventana del ejercicio, haz clic derecho sobre la carpeta `.vscode` > **Delete**. Con ella se eliminan `settings.json` y `launch.json`.
3. **Workspace.** En la ventana de la raíz, borra `modulo-01-vscode/tema-1.3-configuracion-avanzada/tema-1.3.code-workspace` (clic derecho > **Delete**).
4. **Usuario.** En Settings, pestaña **User**, filtro `@modified`: restaura `editor.fontSize` con **Reset Setting**. Comprueba que no queda ningún bloque `"[java]"` añadido por ti.
5. Cierra la ventana del ejercicio.
6. En la Terminal integrada de la raíz del repositorio, ejecuta `git status`.

**Resultado esperado:**

- `@modified` muestra exactamente las preferencias que anotaste al empezar.
- `git status` indica que no hay cambios (*nothing to commit, working tree clean*). Si aparece `.vscode/` o algún archivo del ejercicio, revisa los pasos anteriores.

## 10. Compilación y ejecución desde la terminal

Desde la **raíz del repositorio**:

**Windows (PowerShell):**

```powershell
$EJ = "modulo-01-vscode/tema-1.3-configuracion-avanzada/apartado-1.3.3-workspace"
javac --release 21 -Xlint:all -d "$EJ/out" "$EJ/src/curso/vscode/ConfiguracionWorkspace.java"
java -cp "$EJ/out" curso.vscode.ConfiguracionWorkspace
```

**Linux o WSL (Bash):**

```bash
EJ=modulo-01-vscode/tema-1.3-configuracion-avanzada/apartado-1.3.3-workspace
javac --release 21 -Xlint:all -d "$EJ/out" "$EJ/src/curso/vscode/ConfiguracionWorkspace.java"
java -cp "$EJ/out" curso.vscode.ConfiguracionWorkspace
```

La salida muestra el directorio de trabajo (la raíz del repositorio), `Encuentra ejemplos/settings-ejemplo.json: no` y `0 de 3`: sin `launch.json` no hay argumentos, propiedades ni variables.

Para reproducir a mano lo que hace `launch-ejemplo.json`, ejecuta desde la carpeta del ejercicio, después de compilar:

**Windows (PowerShell):**

```powershell
Set-Location $EJ
$env:CURSO_MODO = "launch-json"
java "-Dcurso.entorno=vscode" -cp out curso.vscode.ConfiguracionWorkspace argumento-1 "argumento con espacios"
Remove-Item Env:CURSO_MODO
Set-Location ../../..
```

**Linux o WSL (Bash):**

```bash
cd "$EJ"
CURSO_MODO=launch-json java -Dcurso.entorno=vscode -cp out curso.vscode.ConfiguracionWorkspace argumento-1 "argumento con espacios"
cd ../../..
```

El resultado es el mismo que en la actividad 9: `Encuentra ...: sí` y `3 de 3`.

- **Comillas en PowerShell.** `"-Dcurso.entorno=vscode"` va entre comillas: sin ellas, Windows PowerShell parte el argumento en dos, como se explicó en el ejercicio 1.3.2.
- **Variable solo para un comando en Bash.** La forma `CURSO_MODO=launch-json java ...` define la variable solo para ese comando, así que no hace falta eliminarla después.

## 11. Comparativa con Eclipse e IntelliJ IDEA

| Aspecto | Eclipse | IntelliJ IDEA | VS Code |
|---|---|---|---|
| Proyectos y *workspaces* | Un *workspace* (carpeta de metadatos) con varios proyectos importados | Un proyecto por ventana, con uno o varios módulos | Una carpeta por ventana, o un multi-root (`.code-workspace`) con varias |
| Preferencias globales | **Window > Preferences**, guardadas **por workspace** de Eclipse | **Settings** del IDE, comunes a todos los proyectos | **User Settings** del perfil activo |
| Configuración por proyecto | **Project > Properties** (carpeta `.settings/`) | Carpeta `.idea/` del proyecto | **Workspace Settings** (`.vscode/settings.json`) y **Folder Settings** en multi-root |
| JDK | **Installed JREs** global y **Java Build Path** por proyecto | **Project Structure > SDK** y nivel de lenguaje por módulo | `java.configuration.runtimes` (usuario o remoto) o la versión de Maven/Gradle. Consulta con **Java: Configure Java Runtime**. |
| Perfiles | No existen. Se usan *workspaces* distintos o se exportan preferencias. | No como tal. Se exportan e importan ajustes, o se sincronizan. | **Profiles** con preferencias, atajos, extensiones y estado propios, exportables |
| Configuraciones de ejecución | **Run Configurations** (diálogo), guardables como `.launch` en el proyecto | **Run/Debug Configurations**, compartibles en `.run/` o `.idea/` | `.vscode/launch.json`, editable como texto |
| Configuración compartida | `.settings/` y `.launch` en el repositorio | `.idea/` (parte compartible, parte personal) o `.run/` | `.vscode/settings.json`, `extensions.json`, `launch.json` y `tasks.json`, en JSONC legible |
| Precedencia | Proyecto sobre *workspace* de Eclipse | Proyecto sobre IDE | Default → User → Remote → Workspace → Folder, con preferencias de lenguaje por encima |

La gran diferencia es que en VS Code **toda la configuración es texto** (JSON/JSONC) con validación y autocompletado: se lee, se comenta, se revisa en Git y se copia entre proyectos con facilidad.

## 12. Comprobaciones finales

Comprueba que eres capaz de:

- [ ] Explicar los conceptos de Folder, Workspace, Multi-root, User, Remote, Workspace y Folder Settings y Profile, e indicar cuándo existe cada nivel.
- [ ] Explicar el orden de precedencia y por qué una preferencia `"[java]"` de usuario puede ganar a una general de *workspace*.
- [ ] Cambiar, localizar y restaurar una propiedad en cada nivel desde Settings, usando `@modified`, `@lang:java` y los avisos de *Modified in*.
- [ ] Abrir y editar el `settings.json` de cada nivel, usar comentarios JSONC y aprovechar la validación del esquema.
- [ ] Distinguir preferencias personales de configuración compartible y justificar cada caso.
- [ ] Explicar la diferencia entre el JDK de las herramientas y el del proyecto, declarar varios JDK con `java.configuration.runtimes` y comprobar cuál usa el proyecto.
- [ ] Crear, guardar, reabrir y cerrar un workspace multi-root con rutas relativas.
- [ ] Crear un perfil, cambiar de perfil, exportarlo y eliminarlo, y explicar qué aísla.
- [ ] Crear un `launch.json` con `mainClass`, `args`, `vmArgs`, `cwd` y `env`, y ejecutarlo desde **Run and Debug**.
- [ ] Decidir qué archivos de `.vscode/` compartir en Git y evitar rutas absolutas, secretos y diferencias entre Windows y Linux.
- [ ] Dejar el entorno exactamente como estaba, comprobándolo con `@modified` y `git status`.
