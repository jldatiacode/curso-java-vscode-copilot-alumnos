# Ejercicio 1.3.2 — Integración de terminales

Módulo 1. Introducción a VS Code · Tema 1.3. Configuración avanzada de VS Code · Apartado 1.3.2. Integración de terminales

> **Antes de empezar:** si aún no lo has hecho en este tema, actualiza tu copia del repositorio desde su raíz con `git status` (sin cambios pendientes) y `git pull --ff-only`, como se explica al principio del ejercicio 1.3.1.

## 1. Introducción

La **terminal integrada** es una consola de comandos real (PowerShell, Command Prompt, Bash…) que se ejecuta dentro de la ventana de VS Code, en el panel inferior. Hace lo mismo que una terminal externa, pero con estas ventajas:

| | Terminal integrada | Terminal externa (Windows Terminal, consola de Linux…) |
|---|---|---|
| Directorio inicial | La carpeta abierta en VS Code | Normalmente, la carpeta personal |
| Cambio de contexto | No hay que salir del editor | Hay que cambiar de ventana |
| Enlaces | Las rutas y los errores con número de línea se abren en el editor con `Ctrl` + clic | Solo texto |
| Integración con VS Code | Ejecutar texto seleccionado, historial de comandos, decoraciones de éxito o error | No |
| Con WSL | Si VS Code está conectado a WSL, la terminal se abre **dentro de Linux** | Depende de qué consola abras |
| Variables de entorno | Hereda las de VS Code al arrancar y añade algunas, como `TERM_PROGRAM=vscode` | Las de la sesión del sistema |

En este apartado se usa la terminal para lo que un desarrollador Java hace a diario:

- comprobar qué Java está activo;
- compilar y ejecutar programas;
- pasar argumentos;
- consultar variables de entorno.

### Material del ejercicio

```text
apartado-1.3.2-terminales/
├── README.md
└── src/curso/vscode/DiagnosticoEntorno.java
```

`DiagnosticoEntorno` muestra:

- la versión y la ubicación de Java;
- el sistema operativo, la arquitectura y el entorno detectado (Windows, Linux o WSL, y si se ejecuta en la terminal de VS Code);
- el directorio de trabajo y los separadores de rutas;
- algunas variables de entorno (`JAVA_HOME`, `TERM_PROGRAM`, `SHELL`, `ComSpec`, `WSL_DISTRO_NAME`, `CURSO_MODO`, `GITHUB_TOKEN`) y un resumen de `PATH`;
- los argumentos recibidos.

**El programa nunca muestra el valor de variables cuyo nombre sugiere una credencial** (`TOKEN`, `SECRET`, `PASSWORD`, `KEY`…). De esas solo indica si están definidas y cuántas hay. Es una práctica recomendable para cualquier herramienta de diagnóstico.

## 2. Abrir y gestionar terminales

| Acción | Menú o botón | Atajo (Windows) |
|---|---|---|
| Nueva terminal | **Terminal > New Terminal** | `` Ctrl + Shift + ` `` (depende del teclado) |
| Mostrar u ocultar el panel de la terminal | **View > Terminal** | `` Ctrl + ` `` (en teclado español, `Ctrl + ñ`) |
| Dividir la terminal | **Terminal > Split Terminal**, o icono de dividir en la barra del panel | `Ctrl + Shift + 5` |
| Cambiar entre terminales | Lista de terminales a la derecha del panel | `Ctrl + PageUp` / `Ctrl + PageDown` con el foco en la terminal |
| Cambiar entre terminales divididas | Clic en la mitad deseada | `Alt + ←` / `Alt + →` con el foco en la terminal |
| Cerrar una terminal | Icono de papelera (**Kill Terminal**) de esa terminal | Escribir `exit` en la terminal |
| Reiniciar una terminal | Command Palette > `Terminal: Relaunch Active Terminal` | — |
| Limpiar la pantalla | Command Palette > `Terminal: Clear` | Comando `clear` (Bash), `Clear-Host` o `cls` (PowerShell) |
| Ejecutar el texto seleccionado del editor | Command Palette > `Terminal: Run Selected Text In Active Terminal` | — |
| Repetir un comando reciente | Command Palette > `Terminal: Run Recent Command...` | — |

- **Varias terminales.** Cada terminal es un proceso independiente, con su propio directorio actual y sus propias variables de proceso. Puedes tener, por ejemplo, una terminal para compilar y otra para ejecutar.
- **Lista de terminales.** Con dos o más terminales, aparece a la derecha del panel. Cada entrada muestra el tipo de terminal (`pwsh`, `bash`…). Al dividir, las terminales quedan agrupadas.
- **Ocultar no es cerrar.** Ocultar el panel (`` Ctrl + ` ``) mantiene las terminales en marcha. Cerrarlas con la papelera o con `exit` termina el proceso y lo que estuviera ejecutándose.
- **`Ctrl + C`.** Detiene el programa que se está ejecutando en la terminal, por ejemplo un servidor. Con texto seleccionado, `Ctrl + C` copia en lugar de detener.

### Terminal, Output y Debug Console

En el panel inferior conviven varias pestañas que a menudo se confunden:

| Pestaña | Qué es | ¿Se puede escribir en ella? |
|---|---|---|
| **Terminal** | Una consola real del sistema operativo | Sí: cualquier comando |
| **Output** | Registros de VS Code y de sus extensiones, por canales; por ejemplo, *Language Support for Java* | No. Solo lectura. |
| **Debug Console** | Mensajes del depurador y evaluación de expresiones Java durante una depuración | Solo expresiones, mientras hay una sesión de depuración |

Al ejecutar un programa con **Run**, su salida aparece en la **Terminal** o en la **Debug Console** según la opción `java.debug.settings.console`, como se vio en el ejercicio 1.1.1.

## 3. Perfiles de terminal

Un **perfil** es un tipo de terminal: qué programa se ejecuta (el *shell*) y con qué opciones. Los perfiles disponibles **dependen del sistema operativo, de los programas instalados y de dónde se ejecuta la ventana de VS Code**:

| Situación | Perfiles habituales |
|---|---|
| VS Code en Windows, carpeta local | **PowerShell** (predeterminado), **Command Prompt**, **Git Bash** (si Git para Windows está instalado), **Ubuntu (WSL)** u otras distribuciones (si WSL está instalado) |
| VS Code en Windows conectado a WSL (`WSL: Ubuntu` en la Status Bar) | Los de **Linux**: **bash** (predeterminado), **sh** y otros que haya en la distribución. La terminal se abre dentro de Ubuntu. |
| VS Code en Linux | **bash** (o el *shell* configurado), **sh**, **zsh** si está instalado… |

- **No hace falta tener WSL.** Si trabajas solo en Windows, usa PowerShell: todos los comandos del curso tienen versión para PowerShell.
- **Perfil WSL frente a ventana conectada a WSL.** El perfil **Ubuntu (WSL)** en una ventana local de Windows abre una terminal Linux, pero VS Code sigue trabajando con los archivos desde Windows. Para desarrollar en WSL, el curso usa la **ventana conectada a WSL**, como se vio en el tema 1.1.

### Usar otro perfil sin cambiar la configuración

1. En la barra del panel de la terminal, pulsa la **flecha** junto al botón **+** (*Launch Profile...*).
2. Elige un perfil, por ejemplo **Command Prompt** en Windows o **sh** en Linux/WSL.
3. Se abre una terminal nueva de ese tipo. El perfil predeterminado **no cambia**: la próxima terminal que abras con **+** volverá a ser la habitual.

### Cambiar el perfil predeterminado

1. Pulsa la flecha junto a **+** y elige **Select Default Profile**, o ejecuta `Terminal: Select Default Profile` desde la Command Palette.
2. Elige el perfil.

VS Code guarda la elección en la configuración de usuario: `terminal.integrated.defaultProfile.windows` en Windows y `terminal.integrated.defaultProfile.linux` en Linux o WSL. Para **restaurar** el perfil original, busca esa propiedad en Settings y usa **Reset Setting**.

## 4. Configuración de terminal

| Propiedad | Valor por defecto | Qué hace | Dónde configurarla |
|---|---|---|---|
| `terminal.integrated.defaultProfile.windows` | `null` (PowerShell) | Perfil predeterminado en Windows | **User**. Depende de los programas instalados en cada equipo. |
| `terminal.integrated.defaultProfile.linux` | `null` (*shell* del usuario, normalmente `bash`) | Perfil predeterminado en Linux y WSL | **User** o **Remote** |
| `terminal.integrated.fontSize` | `14` | Tamaño de letra de la terminal, independiente del editor | **User**. Es una preferencia personal. |
| `terminal.integrated.cwd` | vacío (carpeta del *workspace*) | Directorio inicial de las terminales nuevas | **Workspace**, si el proyecto lo necesita, con ruta relativa |
| `terminal.integrated.scrollback` | `1000` | Líneas que se conservan al desplazarse hacia arriba | **User**. Más líneas consumen más memoria. |

Ejemplo para el `settings.json` de **usuario**:

```json
{
  "terminal.integrated.defaultProfile.windows": "PowerShell",
  "terminal.integrated.defaultProfile.linux": "bash",
  "terminal.integrated.fontSize": 14,
  "terminal.integrated.scrollback": 5000
}
```

Ejemplo para la configuración de un **workspace**:

```json
{
  "terminal.integrated.cwd": "${workspaceFolder}/src"
}
```

### Evitar configuraciones que dependan de un equipo concreto

- **Rutas relativas en `terminal.integrated.cwd`.** Una ruta relativa, como `"src"`, se interpreta respecto a la carpeta del *workspace*. La variable `${workspaceFolder}` se sustituye por la carpeta abierta. Las dos funcionan en cualquier equipo. Una ruta absoluta (`C:\Users\...` o `/home/...`) solo funciona en el equipo de quien la escribió.
- **Nombres de perfil en `defaultProfile`.** Dependen de lo que haya instalado. `"Git Bash"` no existe en un equipo sin Git para Windows y `"Ubuntu (WSL)"` no existe sin WSL. Por eso estas propiedades son personales y no deben compartirse en el proyecto.
- **Ajustes restringidos.** Los perfiles, el directorio inicial y las variables de entorno de la terminal (`terminal.integrated.env.*`) pueden ejecutar programas o cambiar su comportamiento. VS Code los **ignora en carpetas no confiables** (*Restricted Mode*). Por eso, al abrir una carpeta, pregunta si confías en sus autores.

La diferencia entre configuración de usuario y de *workspace* se trabaja en detalle en el ejercicio 1.3.3.

## 5. Comandos prácticos

Equivalencias básicas entre PowerShell y Bash:

| Operación | PowerShell | Bash |
|---|---|---|
| Mostrar el directorio actual | `Get-Location` (alias `pwd`) | `pwd` |
| Listar archivos | `Get-ChildItem` (alias `ls`, `dir`) | `ls` (detallado: `ls -l`) |
| Cambiar de directorio | `Set-Location carpeta` (alias `cd`) | `cd carpeta` |
| Subir un nivel | `cd ..` | `cd ..` |
| Volver al directorio anterior | `cd -` (PowerShell 7) o `Pop-Location` tras `Push-Location` | `cd -` |
| Mostrar una variable | `$env:JAVA_HOME` | `echo "$JAVA_HOME"` |
| Definir una variable para la sesión | `$env:CURSO_MODO = "terminal"` | `export CURSO_MODO=terminal` |
| Eliminar una variable de la sesión | `Remove-Item Env:CURSO_MODO` | `unset CURSO_MODO` |
| Ver las carpetas de PATH | `$env:PATH -split ';'` | `echo "$PATH" \| tr ':' '\n'` |
| Localizar el ejecutable `java` | `Get-Command java` o `where.exe java` | `command -v java` o `type -a java` |
| Consultar Java | `java -version` | `java -version` |
| Limpiar la pantalla | `Clear-Host` (alias `cls`) | `clear` |

Precauciones:

- **`where` en PowerShell.** En PowerShell, `where` es un alias de `Where-Object`. Para buscar ejecutables hay que escribir `where.exe`.
- **Separadores.** Windows separa carpetas con `\` y las entradas de `PATH` con `;`. Linux usa `/` y `:`. PowerShell y Java aceptan también `/` en Windows, por eso los comandos del curso usan siempre `/`.
- **Mayúsculas en los nombres de variables.** En Windows no se distinguen (`$env:path` es lo mismo que `$env:PATH`). En Linux sí: `$path` y `$PATH` son variables distintas.

### Compilar y ejecutar DiagnosticoEntorno

Desde la **raíz del repositorio**, que es el directorio inicial de la Terminal integrada:

**Windows (PowerShell):**

```powershell
$EJ = "modulo-01-vscode/tema-1.3-configuracion-avanzada/apartado-1.3.2-terminales"
javac --release 21 -Xlint:all -d "$EJ/out" "$EJ/src/curso/vscode/DiagnosticoEntorno.java"
java -cp "$EJ/out" curso.vscode.DiagnosticoEntorno
java -cp "$EJ/out" curso.vscode.DiagnosticoEntorno uno "dos palabras" 3
```

**Linux o WSL (Bash):**

```bash
EJ=modulo-01-vscode/tema-1.3-configuracion-avanzada/apartado-1.3.2-terminales
javac --release 21 -Xlint:all -d "$EJ/out" "$EJ/src/curso/vscode/DiagnosticoEntorno.java"
java -cp "$EJ/out" curso.vscode.DiagnosticoEntorno
java -cp "$EJ/out" curso.vscode.DiagnosticoEntorno uno "dos palabras" 3
```

Los `.class` se generan en la carpeta `out/` del ejercicio, excluida en el `.gitignore`.

> **PowerShell y los argumentos `-D`:** si en algún momento pasas a Java una propiedad con puntos, como `-Dcurso.entorno=terminal`, escríbela **entre comillas** en Windows PowerShell: `"-Dcurso.entorno=terminal"`. Sin comillas, PowerShell la parte en dos argumentos (`-Dcurso` y `.entorno=terminal`) y Java no la recibe bien. En Bash no hace falta. Es un error frecuente al usar Java o Maven desde PowerShell.

### Salida de ejemplo

Esta es la salida en la terminal integrada de VS Code conectada a WSL Ubuntu, con tres argumentos. En tu equipo serán distintas:

- las rutas y las versiones;
- el número de carpetas de `PATH`;
- el número de variables con nombre sensible.

La línea `PATH con Java` solo aparece si alguna carpeta de `PATH` contiene `java` o `jdk` en su nombre. Si `java` está en una carpeta genérica, como `/usr/bin`, no aparece.

```text

== Java ==
Versión:                       21.0.x+...
Fabricante:                    Ubuntu
Instalación (java.home):       /usr/lib/jvm/java-21-openjdk-amd64

== Sistema operativo ==
Nombre:                        Linux
Versión:                       6.x...-microsoft-standard-WSL2
Arquitectura:                  amd64
Entorno detectado:             Linux en WSL (Ubuntu), terminal integrada de VS Code

== Rutas ==
Directorio de trabajo:         <raíz de tu repositorio>
Separador de carpetas:         /
Separador de rutas (PATH):     :

== Variables de entorno ==
JAVA_HOME:                     /usr/lib/jvm/java-21-openjdk-amd64
TERM_PROGRAM:                  vscode
SHELL:                         /bin/bash
ComSpec:                       (no definida)
WSL_DISTRO_NAME:               Ubuntu
CURSO_MODO:                    (no definida)
GITHUB_TOKEN:                  (no definida)
PATH:                          N carpetas
  PATH con Java:               /usr/lib/jvm/java-21-openjdk-amd64/bin
Variables con nombre sensible: N (valores no mostrados)

== Argumentos ==
Número de argumentos:          3
args[0]:                       uno
args[1]:                       dos palabras
args[2]:                       3
```

Diferencias en Windows con PowerShell:

- **Sistema y entorno.** `Nombre` empieza por `Windows` y `Entorno detectado` indica `Windows, terminal integrada de VS Code`.
- **Separadores.** Son `\` y `;`.
- **Variables del sistema.** `ComSpec` está definida (es la ruta de `cmd.exe`), y `SHELL` y `WSL_DISTRO_NAME` no.
- **Java.** `java.home` y `JAVA_HOME` apuntan a la carpeta de instalación del JDK en Windows.

`"dos palabras"` llega como **un solo argumento** porque va entre comillas, tanto en PowerShell como en Bash. Sin comillas serían dos.

## 6. Variables de entorno

### PATH

`PATH` es la lista de carpetas en las que el sistema busca los programas. Al escribir `java`, se ejecuta el primer `java` que aparezca en esas carpetas, por orden. Si hay varios JDK instalados, **el orden de PATH decide cuál se usa**.

### JAVA_HOME

`JAVA_HOME` indica la carpeta de instalación de un JDK, la que contiene `bin/`. No la usa el sistema para ejecutar `java`, pero sí muchas herramientas, como Maven, Gradle o algunos servidores de aplicaciones, para saber qué Java usar.

`JAVA_HOME` y el `java` de `PATH` **pueden apuntar a JDK distintos** sin que nada avise de ello. Es una causa habitual de errores del tipo "compila en un sitio y en otro no".

### Variables de usuario, del sistema y de proceso

| Tipo | Dónde se definen | Alcance | Duración |
|---|---|---|---|
| **Del sistema o de usuario** (Windows) | **Configuración > Sistema > Información > Configuración avanzada del sistema > Variables de entorno** | Todos los programas que se inicien **después** del cambio | Permanentes |
| **De sesión** (Linux/WSL) | Archivos como `~/.bashrc` o `~/.profile` | Las terminales nuevas | Permanentes mientras estén en el archivo |
| **De proceso** | En la propia terminal: `$env:X = ...` o `export X=...` | Esa terminal y los programas que se lancen desde ella | Hasta cerrar la terminal |

Consecuencias prácticas:

- **Una variable de proceso no afecta al resto.** Si la defines en una terminal, no existe en otra terminal ni en el resto del sistema. Es ideal para pruebas, porque desaparece sola.
- **VS Code no ve los cambios permanentes hasta reiniciarse.** Las terminales integradas heredan las variables que tenía VS Code al arrancar. Si cambias `PATH` o `JAVA_HOME` en Windows, **cierra y vuelve a abrir VS Code** para que las terminales nuevas las vean. Abrir una terminal nueva no basta.
- **Con WSL, Linux tiene sus propias variables.** Las variables de Windows no se usan en la terminal de Linux, salvo `PATH`, al que WSL añade las carpetas de Windows al final. Por eso en WSL puedes encontrar en `PATH` rutas como `/mnt/c/...`.

### Comprobar qué Java se está usando

**PowerShell:**

```powershell
Get-Command java
where.exe java
$env:JAVA_HOME
java -version
```

**Bash:**

```bash
command -v java
type -a java
readlink -f "$(command -v java)"
echo "$JAVA_HOME"
java -version
```

- `where.exe java` y `type -a java` muestran **todos** los `java` de `PATH`, en orden. Se usa el primero.
- En Linux, `/usr/bin/java` suele ser un enlace. `readlink -f` muestra el JDK real al que apunta.
- `DiagnosticoEntorno` muestra `java.home`, que es la instalación del Java que **realmente** lo está ejecutando.

**En la práctica no se hacen cambios permanentes en el sistema.** Solo se usan variables de proceso, que desaparecen al cerrar la terminal.

## 7. Práctica guiada

Abre VS Code en la raíz del repositorio. En cada actividad se indica el comando de **PowerShell** (Windows) y de **Bash** (Linux o WSL). Usa el que corresponda a tu terminal.

### Actividad 1. Abrir una terminal

1. Usa **Terminal > New Terminal**.
2. Observa a la derecha del panel el nombre del perfil (`pwsh`, `powershell`, `bash`…).
3. Comprueba el directorio actual: `Get-Location` (PowerShell) o `pwd` (Bash).

**Resultado esperado:** la terminal se abre en la raíz del repositorio, la carpeta que contiene `modulo-01-vscode`.

### Actividad 2. Crear y dividir terminales

1. Pulsa **+** en la barra del panel para crear una segunda terminal. Aparece la lista de terminales a la derecha.
2. Cambia entre ellas con la lista, o con `Ctrl + PageUp` / `Ctrl + PageDown` mientras el cursor está en la terminal. Con el cursor en el editor, esos atajos cambian de pestaña de editor.
3. Con la segunda terminal activa, usa **Terminal > Split Terminal** (`Ctrl + Shift + 5`).
4. Escribe un comando distinto en cada mitad. Pasa de una a otra con `Alt + ←` / `Alt + →`.
5. Cierra la terminal dividida con su icono de papelera y la segunda terminal con `exit`.

**Resultado esperado:** puedes tener varias terminales a la vez, verlas en paralelo y cerrarlas. Al terminar, queda solo la terminal de la actividad 1.

### Actividad 3. Navegar por carpetas

1. Entra en la carpeta del ejercicio con `cd modulo-01-vscode/tema-1.3-configuracion-avanzada/apartado-1.3.2-terminales`. Pulsa `Tab` mientras escribes para que la terminal complete los nombres.
2. Lista el contenido: `Get-ChildItem` (PowerShell) o `ls -l` (Bash).
3. Entra en `src/curso/vscode` y lista de nuevo.
4. Vuelve a la raíz del repositorio con `cd ../../../../../..`. Comprueba que estás en ella con `Get-Location` o `pwd`.

**Resultado esperado:** en la carpeta del ejercicio aparecen `README.md` y `src`, y en `src/curso/vscode` aparece `DiagnosticoEntorno.java`. Al final vuelves a estar en la raíz.

### Actividad 4. Consultar Java

1. Ejecuta `java -version` y `javac -version`.
2. Localiza el ejecutable: `Get-Command java` (PowerShell) o `command -v java` y `readlink -f "$(command -v java)"` (Bash).
3. Consulta `JAVA_HOME`: `$env:JAVA_HOME` (PowerShell) o `echo "$JAVA_HOME"` (Bash).

**Resultado esperado:** `java` y `javac` indican la versión 21. Anota la ruta del ejecutable y la de `JAVA_HOME`, para compararlas en la actividad 5. Si `JAVA_HOME` está vacía, no es un error para este curso; todavía no se usa ninguna herramienta que la necesite.

### Actividad 5. Ejecutar la aplicación

1. Desde la raíz del repositorio, ejecuta las dos primeras líneas del bloque correspondiente del apartado 5: definición de `EJ` y compilación.
2. Ejecuta `java -cp "$EJ/out" curso.vscode.DiagnosticoEntorno`.
3. Compara `Instalación (java.home)` con lo que anotaste en la actividad 4.

**Resultado esperado:**

- `Versión` es 21.
- `Directorio de trabajo` es la raíz del repositorio.
- `Entorno detectado` termina en `terminal integrada de VS Code`.
- `java.home` corresponde al mismo JDK que el `java` localizado en la actividad 4.

### Actividad 6. Pasar argumentos

1. Ejecuta `java -cp "$EJ/out" curso.vscode.DiagnosticoEntorno uno "dos palabras" 3`.
2. Repite sin comillas: `... DiagnosticoEntorno uno dos palabras 3`.
3. Ejecuta el programa desde la carpeta `out` del ejercicio: `cd "$EJ"`, después `java -cp out curso.vscode.DiagnosticoEntorno`, y vuelve con `cd -` (Bash o PowerShell 7) o con `cd ../../..` (cualquier *shell*).

**Resultado esperado:**

- Paso 1: 3 argumentos, con `dos palabras` como uno solo.
- Paso 2: 4 argumentos.
- Paso 3: `Directorio de trabajo` cambia a la carpeta del ejercicio. El directorio de trabajo es aquel **desde el que se lanza** `java`, no donde está la clase.

### Actividad 7. Consultar y definir variables de entorno

1. Define una variable de proceso y ejecuta el programa:
   - **PowerShell:** `$env:CURSO_MODO = "terminal"`
   - **Bash:** `export CURSO_MODO=terminal`
2. Ejecuta `java -cp "$EJ/out" curso.vscode.DiagnosticoEntorno`. `CURSO_MODO` muestra `terminal`.
3. Define una variable con nombre sensible:
   - **PowerShell:** `$env:GITHUB_TOKEN = "abc"`
   - **Bash:** `export GITHUB_TOKEN=abc`

   Ejecuta de nuevo el programa. `GITHUB_TOKEN` aparece como `(definida, valor oculto)` y el contador de variables sensibles aumenta en uno.
4. Abre una **terminal nueva** y ejecuta allí el programa. Como no están definidas en esa terminal, ninguna de las dos variables aparece.
5. Elimina las variables en la primera terminal:
   - **PowerShell:** `Remove-Item Env:CURSO_MODO; Remove-Item Env:GITHUB_TOKEN`
   - **Bash:** `unset CURSO_MODO GITHUB_TOKEN`
6. Muestra las carpetas de `PATH`: `$env:PATH -split ';'` (PowerShell) o `echo "$PATH" | tr ':' '\n'` (Bash). Localiza la del JDK.

**Resultado esperado:** las variables de proceso solo existen en la terminal donde se definieron y desaparecen al eliminarlas o al cerrarla. El valor de `GITHUB_TOKEN` no se muestra en ningún momento.

### Actividad 8. Cambiar temporalmente el perfil de terminal y restaurarlo

1. **Usar otro perfil sin cambiar la configuración.** Pulsa la flecha junto a **+** y elige otro perfil: **Command Prompt** en Windows o **sh** en Linux/WSL. En esa terminal ejecuta `java -version`.
   - **Command Prompt:** ejecuta la clase, compilada en la actividad 5, con `java -cp modulo-01-vscode/tema-1.3-configuracion-avanzada/apartado-1.3.2-terminales/out curso.vscode.DiagnosticoEntorno`. La variable `$EJ` de PowerShell no existe en cmd. Observa en la salida que `ComSpec` está definida.
2. **Cambiar el predeterminado.** Pulsa la flecha junto a **+** > **Select Default Profile** y elige ese mismo perfil. Abre una terminal con **+**: es del nuevo tipo.
3. **Comprobar dónde se ha guardado.** En Settings (pestaña **User**), busca `terminal.integrated.defaultProfile`. La propiedad de tu sistema aparece modificada.
4. **Restaurar.** Usa **Reset Setting** sobre esa propiedad.
5. Abre una terminal con **+**: vuelve a ser la habitual. Cierra las terminales sobrantes.

**Resultado esperado:** el paso 1 no modifica ninguna preferencia. El paso 2 sí, y tras el paso 4 la configuración vuelve a su estado original.

### Al terminar

- No queda ninguna variable de proceso definida, porque se han eliminado o se ha cerrado su terminal.
- `terminal.integrated.defaultProfile.*` no aparece en `@modified` en Settings, salvo que ya lo tuvieras configurado antes.
- La carpeta `out/` del ejercicio puede quedarse: está excluida del control de versiones. Para borrarla, haz clic derecho sobre ella en el Explorer > **Delete**.

## 8. Comparación Eclipse / IntelliJ IDEA / VS Code

| Aspecto | Eclipse | IntelliJ IDEA | VS Code |
|---|---|---|---|
| Terminal integrada | Vista **Terminal** (*TM Terminal*, incluida en las distribuciones habituales), **Window > Show View > Terminal** | Ventana **Terminal** (`Alt + F12`) | **Terminal > New Terminal**, en el panel inferior |
| Varias terminales y división | Varias pestañas | Pestañas y división (**Split Right/Down**) | Varias terminales, lista lateral, división (`Ctrl + Shift + 5`) y terminales en el área de editores |
| Elección del *shell* | Al abrir la terminal (local, SSH, serie…) | **Settings > Tools > Terminal > Shell path** | Perfiles (**+** > *Launch Profile...*, **Select Default Profile**) |
| Configuración | **Preferences > Terminal** | **Settings > Tools > Terminal** | `terminal.integrated.*` en `settings.json` |
| Directorio inicial | El del proyecto seleccionado | La raíz del proyecto | La carpeta del *workspace*, o `terminal.integrated.cwd` |
| Salida de los programas | Vista **Console** (no es una terminal) | Ventana **Run** (no es una terminal) | Terminal integrada (por defecto) o Debug Console |
| Ejecutar un programa | Configuraciones de ejecución (**Run As**) | Configuraciones de ejecución (**Run**) | **Run**, `launch.json` (1.3.3) o comandos en la terminal |
| Integración con WSL | Limitada | Terminal WSL y proyectos en WSL (según versión) | Ventana conectada a WSL: terminal, archivos y extensiones dentro de Linux |
| Variables de entorno de la ejecución | En cada configuración de ejecución (pestaña **Environment**) | En cada configuración de ejecución | En la terminal (variables de proceso) o en `launch.json` (`env`) |

La diferencia principal está en la salida de los programas. En Eclipse e IntelliJ IDEA, la consola de ejecución es una vista propia del IDE. En VS Code, por defecto, los programas Java se ejecutan en una **terminal real**, así que se comportan exactamente igual que al lanzarlos a mano.

## 9. Comprobaciones finales

Comprueba que eres capaz de:

- [ ] Abrir, dividir, alternar, cerrar y reiniciar terminales integradas.
- [ ] Explicar la diferencia entre Terminal, Output y Debug Console.
- [ ] Indicar qué perfiles de terminal tienes disponibles y usar uno distinto sin cambiar el predeterminado.
- [ ] Cambiar el perfil predeterminado y restaurarlo con **Reset Setting**.
- [ ] Explicar qué propiedades `terminal.integrated.*` son personales y cuáles pueden ir en el *workspace*, y por qué hay que evitar rutas absolutas.
- [ ] Usar los comandos básicos de navegación y de variables en PowerShell y en Bash.
- [ ] Comprobar qué `java` se ejecuta, qué indica `JAVA_HOME` y si coinciden.
- [ ] Compilar y ejecutar `DiagnosticoEntorno` desde la terminal integrada, con y sin argumentos, y explicar el directorio de trabajo.
- [ ] Definir y eliminar una variable de entorno de proceso sin cambiar la configuración del sistema.
- [ ] Explicar por qué VS Code debe reiniciarse para ver cambios permanentes en `PATH` o `JAVA_HOME`.
