# Ejercicio 1.1.1 — Interfaz de usuario de VS Code

Módulo 1. Introducción a VS Code · Tema 1.1. Conceptos básicos · Apartado 1.1.1. Interfaz

## Introducción

VS Code no es un IDE monolítico como Eclipse o IntelliJ IDEA, sino un editor ligero que se convierte en entorno de desarrollo Java gracias a sus extensiones. Su interfaz se organiza en unas pocas áreas fijas que se reutilizan para cualquier lenguaje:

- **Activity Bar**: barra vertical de iconos en el lateral izquierdo.
- **Side Bar**: panel lateral que muestra la vista seleccionada en la Activity Bar (Explorer, Search, Run and Debug, Extensions…).
- **Editor**: zona central donde se abren los archivos, con pestañas y posibilidad de dividirse.
- **Panel inferior**: Terminal, Problems, Output y Debug Console.
- **Status Bar**: barra inferior con información de contexto (conexión remota, lenguaje, posición del cursor, codificación…).
- **Command Palette**: buscador de comandos que da acceso a casi cualquier acción del editor.

Quien viene de Eclipse o IntelliJ IDEA está acostumbrado a perspectivas, vistas acopladas y menús muy extensos. En VS Code no hay perspectivas ni proyectos en el sentido de Eclipse: se abre una **carpeta** y las extensiones detectan qué contiene. Conocer bien estas áreas desde el principio evita la sensación de que «faltan cosas» y permite encontrar rápidamente el equivalente de cada herramienta habitual.

## Objetivo

Familiarizarse con las principales áreas de VS Code utilizando el código Java proporcionado en esta práctica, sin modificar su funcionalidad.

## Requisitos previos

- VS Code instalado en Windows 11.
- Conexión de VS Code con WSL (Ubuntu) operativa.
- Java 21 configurado en Ubuntu (`java -version` y `javac -version` muestran la versión 21).
- Extension Pack for Java instalado en el lado de WSL.

Este ejercicio no repite los procedimientos de instalación. No necesita Maven ni ninguna dependencia externa.

## Estructura de la práctica

```text
modulo-01-vscode/
└── tema-1.1-conceptos-basicos/
    └── apartado-1.1.1-interfaz/
        ├── README.md
        └── src/
            └── curso/
                └── vscode/
                    └── HolaVSCode.java
```

La clase `HolaVSCode` pertenece al paquete `curso.vscode`, por eso está en `src/curso/vscode/`.

## Paso 1. Abrir el ejercicio

VS Code se abre desde la **raíz del repositorio del curso**, es decir, la carpeta que contiene `modulo-01-vscode/`. Así todas las prácticas del curso quedan accesibles desde la misma ventana.

Abre una terminal de Ubuntu (WSL), sitúate en la raíz del repositorio y ejecuta `code .`. Sustituye `<ruta-del-repositorio>` por la ubicación donde lo tengas:

```bash
cd <ruta-del-repositorio>
code .
```

El comando `code .` abre VS Code en Windows conectado a WSL, con la carpeta actual como raíz de trabajo.

No hace falta ninguna configuración adicional. Al abrir `HolaVSCode.java`, la extensión de Java detecta, a partir de la línea `package curso.vscode;`, que `src/` es la carpeta de código fuente de esta práctica.

Si VS Code pregunta si confías en los autores de la carpeta, responde que sí (*Yes, I trust the authors*). Sin esa confirmación, el modo restringido desactiva parte de las funciones de las extensiones.

## Paso 2. Activity Bar

La Activity Bar es la columna de iconos del lateral izquierdo. Cada icono cambia el contenido de la Side Bar. Pulsar de nuevo el icono activo oculta la Side Bar.

Localiza y abre estas cuatro vistas:

| Vista | Para qué sirve | Atajo |
|---|---|---|
| **Explorer** | Ver y gestionar los archivos de la carpeta abierta | `Ctrl + Shift + E` |
| **Search** | Buscar y reemplazar texto en todos los archivos | `Ctrl + Shift + F` |
| **Run and Debug** | Lanzar y depurar programas | `Ctrl + Shift + D` |
| **Extensions** | Instalar, desactivar y configurar extensiones | `Ctrl + Shift + X` |

Prueba cada una:

- **Search**: escribe `mostrarBienvenida` y comprueba que aparecen dos resultados en `HolaVSCode.java`, la llamada y la declaración. Haz clic en uno de ellos para saltar a esa línea.
- **Run and Debug**: con `HolaVSCode.java` abierto en el editor, la vista muestra el botón *Run and Debug*. No hace falta crear un archivo `launch.json`. La extensión de Java detecta automáticamente las clases con método `main` y puede ejecutarlas directamente. El enlace para crear `launch.json` solo es necesario cuando se quiere una configuración de lanzamiento personalizada (argumentos, variables de entorno, etc.). No lo crees en este ejercicio.
- **Extensions**: escribe `Extension Pack for Java` en el buscador. Con VS Code conectado a WSL, la lista separa las extensiones instaladas en local (Windows) de las instaladas en `WSL: Ubuntu`. Las extensiones de Java deben aparecer en el apartado de WSL.

## Paso 3. Explorer

Vuelve al Explorer (`Ctrl + Shift + E`).

1. En la parte superior aparece en mayúsculas el nombre de la carpeta abierta, la raíz del repositorio.
2. Expande `modulo-01-vscode`, después `tema-1.1-conceptos-basicos` y, por último, `apartado-1.1.1-interfaz`. Ahí están el `README.md` de la práctica y la carpeta `src`.
3. Expande `src`. Por defecto, VS Code compacta las carpetas que solo contienen otra carpeta y muestra `curso/vscode` en una única línea.
4. Ese recorrido de carpetas corresponde al paquete `curso.vscode` declarado en la primera línea de código de la clase.
5. Dentro está `HolaVSCode.java`.

Para plegar de golpe todas las carpetas expandidas, usa el icono *Collapse Folders in Explorer* de la cabecera del Explorer.

Fíjate también en la sección **Java Projects**, en la parte inferior del Explorer. La añade la extensión de Java y muestra el contenido en forma de proyecto: paquetes, clases y biblioteca del JDK. Es lo más parecido al *Package Explorer* de Eclipse.

Las secciones **Outline** y **Timeline** también están en el Explorer. Outline muestra la estructura de la clase abierta: el método `main` y el método `mostrarBienvenida`.

## Paso 4. Editor

1. Abre `HolaVSCode.java` con un clic en el Explorer. La pestaña aparece en *cursiva*: es una **vista previa** que se reemplaza al abrir otro archivo. Haz doble clic en la pestaña o en el archivo para fijarla.
2. Navega por el código:
   - `Ctrl + G` lleva a un número de línea concreto.
   - `Ctrl + Shift + O` muestra los símbolos de la clase (métodos y variables) y permite saltar a ellos.
   - Sitúa el cursor sobre la llamada a `mostrarBienvenida` en `main` y pulsa `F12` (*Go to Definition*). El editor salta a la declaración del método.
   - `Alt + ←` vuelve a la posición anterior.
3. Abre la Command Palette (`Ctrl + Shift + P`), escribe `View: Split Editor` y ejecútalo. La misma clase aparece en dos columnas. También puedes usar el icono de dividir editor, en la esquina superior derecha de la zona de pestañas.
4. Coloca en cada columna una parte distinta de la clase, por ejemplo `main` a la izquierda y `mostrarBienvenida` a la derecha.
5. Cierra la columna sobrante con la `x` de su pestaña o con `Ctrl + W`.

Sobre el método `main` aparecen los enlaces **Run | Debug**. Se llaman *CodeLens* y los añade la extensión de Java. Se usan en el paso 8.

## Paso 5. Panel inferior

Abre y cierra el panel inferior con `Ctrl + J`, o desde el menú **View > Appearance > Panel**. Tiene varias pestañas, cada una con un propósito distinto:

| Pestaña | Qué muestra | Atajo |
|---|---|---|
| **Terminal** | Una shell de Ubuntu (bash) ya situada en la carpeta abierta | Depende del teclado (ver nota) |
| **Problems** | Errores y avisos de compilación detectados en los archivos | `Ctrl + Shift + M` |
| **Output** | Registros internos de VS Code y de cada extensión, por canales | `Ctrl + Shift + U` |
| **Debug Console** | Mensajes del depurador, evaluación de expresiones y, según la configuración, la salida de los programas Java | `Ctrl + Shift + Y` |

**Nota sobre el atajo de la Terminal.** El atajo depende de la distribución del teclado. Con teclado inglés (EE. UU.) es `` Ctrl + ` ``. Con teclado español suele ser `Ctrl + ñ`, aunque puede variar según la configuración. La alternativa que funciona siempre es el menú **Terminal > New Terminal**. Para consultar el atajo de tu equipo, abre la Command Palette y busca `View: Toggle Terminal`: el atajo asignado aparece junto al comando.

Comprueba cada una:

- **Terminal**: ejecuta `pwd` y verifica que la ruta corresponde a la raíz del repositorio, la carpeta abierta en VS Code.
- **Problems**: borra temporalmente el `;` final de la línea `String nombreAlumno = ...`. En unos segundos aparece un error en Problems y una línea ondulada roja en el editor. Haz clic en el error para saltar a la línea y **restaura el `;`** (`Ctrl + Z`). El panel vuelve a quedar vacío.
- **Output**: en el desplegable de la derecha del panel, selecciona el canal **Language Support for Java**. Ahí se ve la actividad del servidor de lenguaje Java.
- **Debug Console**: de momento estará vacía. Muestra los mensajes del depurador y, según la configuración de la extensión de Java, también puede mostrar la salida de los programas (ver paso 8).

## Paso 6. Status Bar

La Status Bar es la barra inferior de la ventana. Con `HolaVSCode.java` abierto en el editor, identifica estos elementos:

- **Conexión WSL**: en el extremo izquierdo aparece un indicador con el texto `WSL: Ubuntu`. Indica que VS Code trabaja dentro de Ubuntu, aunque la ventana se muestre en Windows. Al hacer clic se abren las opciones de conexión remota.
- **Lenguaje**: en la parte derecha aparece `Java`. Es el modo de lenguaje del archivo activo. Al hacer clic se puede cambiar manualmente. Junto a él, la extensión de Java muestra un indicador de estado (icono `{}`) que informa de si el proyecto está cargado.
- **Posición del cursor**: aparece con el formato `Ln <línea>, Col <columna>`. Mueve el cursor por el código con el ratón o las flechas y observa cómo cambian los valores. Al hacer clic se abre *Go to Line*.
- **Codificación**: aparece `UTF-8`. Junto a ella se muestra el tipo de fin de línea, `LF` en archivos creados en Linux.

## Paso 7. Command Palette

La Command Palette es el punto de acceso común a casi todas las acciones de VS Code y de sus extensiones. Se abre con `Ctrl + Shift + P` o con `F1`.

Ejecuta estos comandos escribiendo parte de su nombre:

1. `View: Toggle Minimap`: oculta o muestra el minimapa del editor. Ejecútalo dos veces para dejarlo como estaba.
2. `Preferences: Color Theme`: muestra los temas disponibles. Recórrelos con las flechas y pulsa `Esc` para no cambiar el tema.
3. `View: Toggle Word Wrap`: activa o desactiva el ajuste de líneas largas. Observa la línea de `nombreCurso`.
4. `Java: Configure Java Runtime`: muestra el JDK que usa la extensión. Comprueba que es Java 21.

Si borras el carácter `>` inicial, la misma caja busca archivos por nombre. Es el equivalente a `Ctrl + P` (*Quick Open*). Prueba a escribir `Hola` y abrir la clase.

## Paso 8. Ejecutar el programa

### Desde VS Code

Con `HolaVSCode.java` abierto, usa cualquiera de estas opciones:

- Haz clic en **Run**, encima del método `main`.
- Usa el botón ▷ (*Run Java*) de la esquina superior derecha del editor.

La salida del programa aparece en el panel inferior, en una de estas dos pestañas:

- **Terminal**: es el comportamiento habitual. La extensión de Java lanza el programa en la terminal integrada.
- **Debug Console**: aparece aquí si la opción `java.debug.settings.console` está configurada como `internalConsole`, o si la configuración de lanzamiento usada así lo indica.

Si no ves la salida en una pestaña, revisa la otra. En ambos casos, la salida esperada es:

```text
Hola, Laura Martín.
Te damos la bienvenida al curso: Migración a VS Code y uso de GitHub Copilot para desarrolladores Java
Programa ejecutado correctamente desde VS Code.
```

### Desde la terminal

Como alternativa, se puede compilar y ejecutar con las herramientas del JDK desde la Terminal integrada.

**Opción A. Desde la carpeta del ejercicio (recomendada).** La Terminal se abre en la raíz del repositorio, así que primero hay que entrar en la carpeta de la práctica:

```bash
cd modulo-01-vscode/tema-1.1-conceptos-basicos/apartado-1.1.1-interfaz
javac --release 21 -Xlint:all -d out src/curso/vscode/HolaVSCode.java
java -cp out curso.vscode.HolaVSCode
```

- `javac --release 21` compila para Java 21 y `-Xlint:all` activa todas las advertencias del compilador. Esta clase no debe generar ninguna.
- `-d out` deja el `.class` en la carpeta `out/` del ejercicio, respetando la estructura del paquete (`out/curso/vscode/HolaVSCode.class`).
- `java -cp out` indica que las clases se buscan en `out/` y ejecuta la clase por su nombre completo, con el paquete incluido: `curso.vscode.HolaVSCode`.

Para volver después a la raíz del repositorio, ejecuta `cd -`.

**Opción B. Desde la raíz del repositorio.** Se usan las mismas órdenes con rutas relativas a la carpeta del ejercicio. La variable `EJ` evita repetir la ruta completa:

```bash
EJ=modulo-01-vscode/tema-1.1-conceptos-basicos/apartado-1.1.1-interfaz
javac --release 21 -Xlint:all -d "$EJ/out" "$EJ/src/curso/vscode/HolaVSCode.java"
java -cp "$EJ/out" curso.vscode.HolaVSCode
```

En los dos casos, los `.class` quedan en la carpeta `out/` de la propia práctica, de modo que cada ejercicio mantiene sus archivos compilados separados de los demás.

La salida debe ser idéntica a la obtenida desde VS Code, y en este caso siempre aparece en la Terminal. La carpeta `out/` contiene archivos generados que no deben subirse al repositorio. Ya está excluida en el `.gitignore`.

## Paso 9. Comprobaciones finales

Comprueba que eres capaz de:

- [ ] Abrir la raíz del repositorio desde WSL con `code .` y ver `WSL: Ubuntu` en la Status Bar.
- [ ] Cambiar entre Explorer, Search, Run and Debug y Extensions desde la Activity Bar y con sus atajos.
- [ ] Localizar `HolaVSCode.java` en el Explorer y relacionar sus carpetas con el paquete `curso.vscode`.
- [ ] Saltar a la definición de un método con `F12` y dividir el editor en dos columnas.
- [ ] Abrir el panel inferior y explicar qué muestran Terminal, Problems, Output y Debug Console.
- [ ] Identificar en la Status Bar el lenguaje, la posición del cursor y la codificación del archivo.
- [ ] Ejecutar un comando desde la Command Palette.
- [ ] Ejecutar el programa desde VS Code y desde la terminal con `javac` y `java`, con la misma salida en ambos casos.

## Equivalencias con Eclipse e IntelliJ IDEA

| Función | Eclipse | IntelliJ IDEA | VS Code | Observaciones |
|---|---|---|---|---|
| Explorador de archivos/proyectos | *Package Explorer* / *Project Explorer* | Ventana *Project* (`Alt + 1`) | **Explorer** (`Ctrl + Shift + E`) y sección **Java Projects** | VS Code abre carpetas, no proyectos. La vista por paquetes la aporta la sección Java Projects de la extensión. |
| Editor de código | Editor con pestañas | Editor con pestañas | **Editor** con pestañas y grupos divididos | Equivalente. VS Code añade la pestaña de vista previa (cursiva), que se reemplaza al abrir otro archivo. |
| Ventana de ejecución | Vista *Console* | Ventana *Run* (`Alt + 4`) | **Terminal** integrada o **Debug Console** | No es exactamente equivalente. Según la configuración, la salida va a la terminal integrada (una shell real de Ubuntu, no una consola específica) o a la Debug Console. |
| Depuración | Perspectiva *Debug* | Ventana *Debug* (`Alt + 5`) | Vista **Run and Debug** y **Debug Console** | VS Code no cambia de perspectiva. Variables, *watches* y pila de llamadas aparecen en la Side Bar y la barra de depuración flota sobre el editor. |
| Búsqueda | *Search* (`Ctrl + H`) | *Find in Files* (`Ctrl + Shift + F`) y *Search Everywhere* (`Shift` dos veces) | **Search** (`Ctrl + Shift + F`), **Quick Open** (`Ctrl + P`) y símbolos (`Ctrl + T`) | La búsqueda de texto es equivalente. No hay un único buscador universal: archivos, símbolos y comandos se buscan desde la misma caja con prefijos distintos. |
| Terminal | Vista *Terminal* (según la distribución) | Ventana *Terminal* (`Alt + F12`) | **Terminal** del panel inferior (menú *Terminal > New Terminal*) | Equivalente. En VS Code con WSL, la terminal es bash dentro de Ubuntu. |
| Configuración | *Window > Preferences* (por *workspace*) | *Settings* (`Ctrl + Alt + S`) y carpeta `.idea` del proyecto | **Settings** (`Ctrl + ,`) a nivel de usuario o de carpeta (`.vscode/settings.json`) | Concepto similar con otra organización. La configuración de VS Code se guarda en JSON y puede versionarse por carpeta. |
| Extensiones/plugins | *Eclipse Marketplace* | *Plugins* (Marketplace de JetBrains) | **Extensions** (`Ctrl + Shift + X`) | En VS Code el soporte de Java depende por completo de las extensiones. Con WSL, las extensiones se instalan en local o en el servidor remoto. |
| Acceso rápido a acciones | *Quick Access* (`Ctrl + 3`) | *Find Action* (`Ctrl + Shift + A`) | **Command Palette** (`Ctrl + Shift + P`) | Equivalente en propósito. En VS Code es el mecanismo principal de trabajo. |
