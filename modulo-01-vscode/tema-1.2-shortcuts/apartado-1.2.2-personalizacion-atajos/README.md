# Ejercicio 1.2.2 — Personalización de atajos

Módulo 1. Introducción a VS Code · Tema 1.2. Shortcuts para mejorar la productividad · Apartado 1.2.2. Personalización de atajos

## 1. Introducción

En VS Code, cada acción es un **comando** con un identificador único. Por ejemplo, `editor.action.deleteLines` es "Delete Line". Un atajo de teclado (*keybinding*) es una regla que asocia una combinación de teclas a un comando, opcionalmente solo en determinadas circunstancias.

Todas esas reglas son configurables:

- se puede cambiar la combinación de cualquier comando;
- se puede asignar una combinación a un comando que no tiene ninguna;
- se puede eliminar una asignación;
- se puede volver a los valores por defecto en cualquier momento.

Al llegar desde Eclipse o IntelliJ IDEA, la memoria muscular juega en contra: `Ctrl + D`, `Ctrl + 1` o `F3` hacen en VS Code algo distinto de lo que se espera. Hay dos estrategias:

- **Aprender los atajos de VS Code** y personalizar solo unos pocos que se usan a diario.
- **Imitar el IDE anterior**, con una extensión de *keymap* o con personalizaciones propias.

Este ejercicio enseña las herramientas para cualquiera de las dos, y cómo deshacer los cambios.

### Material del ejercicio

```text
apartado-1.2.2-personalizacion-atajos/
├── README.md
└── ejemplos/
    └── keybindings-ejemplo.json
```

`ejemplos/keybindings-ejemplo.json` contiene combinaciones **ilustrativas**. VS Code no lo lee: es un archivo de consulta. Tus atajos reales están en tu propio `keybindings.json`, que solo cambiarás durante la práctica y que dejarás como estaba al terminar.

Los atajos que se citan son los de **VS Code en Windows**, que también se usan al trabajar con WSL.

## 2. Keyboard Shortcuts

### Abrir el editor de atajos

Usa cualquiera de estas opciones:

- **File > Preferences > Keyboard Shortcuts**.
- `Ctrl + K` y, a continuación, `Ctrl + S`.
- Command Palette (`Ctrl + Shift + P`) > `Preferences: Open Keyboard Shortcuts`.

El editor muestra una tabla con cuatro columnas:

| Columna | Significado |
|---|---|
| **Command** | Nombre del comando. Al pasar el ratón se ve su identificador, por ejemplo `editor.action.deleteLines`. |
| **Keybinding** | Combinación asignada. Si un comando tiene varias, aparece una fila por cada una. |
| **When** | Condición en la que el atajo está activo (ver más abajo) |
| **Source** | Origen de la regla: `Default` (VS Code), `User` (tuya), `Extension` (de una extensión) o `System` |

### Buscar comandos

Escribe en el buscador parte del nombre o del identificador del comando:

- `delete line` → **Delete Line**, `Ctrl + Shift + K`.
- `organize imports` → **Organize Imports**, `Shift + Alt + O`.
- `editor.action.rename` → **Rename Symbol**, `F2`.

Filtros útiles:

| Filtro | Muestra |
|---|---|
| `@source:user` | Solo tus personalizaciones |
| `@source:default` | Solo las asignaciones de VS Code |
| `@source:extension` | Solo las asignaciones aportadas por extensiones |
| `"ctrl+d"` (entre comillas) | Las reglas que usan exactamente esa combinación |

### Buscar por combinación

Para saber qué hace una combinación:

1. Pulsa el icono de teclado (**Record Keys**) a la derecha del buscador, o `Alt + K` con el buscador activo.
2. Pulsa la combinación, por ejemplo `Ctrl + D`.
3. La tabla muestra todos los comandos asignados a esa combinación.
4. Vuelve a pulsar el icono para salir del modo de grabación.

Grabar la combinación evita los problemas de nombres de teclas: en teclado español no siempre es evidente cómo se llama la tecla que se pulsa.

### Identificar conflictos

Una misma combinación puede estar asignada a varios comandos. No siempre es un conflicto real: lo habitual es que cada regla tenga una condición **When** distinta y que nunca se cumplan a la vez.

- **Ver los comandos que comparten combinación:** clic derecho sobre una fila > **Show Same Keybindings**.
- **Avisos al asignar:** al asignar una combinación, el diálogo indica cuántos comandos existentes la tienen ya. Con un clic en ese aviso se ven cuáles.
- **Qué regla gana:** si dos reglas se cumplen a la vez, gana la de mayor prioridad. Las reglas de `User` tienen prioridad sobre las de extensiones y sobre las de VS Code. Por eso una personalización tuya puede "tapar" sin querer un atajo que usabas.

### Consultar las condiciones `when`

La columna **When** contiene una expresión con **claves de contexto**. Algunas habituales:

| Clave de contexto | Se cumple cuando… |
|---|---|
| `editorTextFocus` | El foco está en el texto de un editor |
| `textInputFocus` | El foco está en cualquier campo de texto, incluido el editor |
| `editorReadonly` | El archivo es de solo lectura |
| `editorLangId == java` | El archivo activo es Java |
| `inDebugMode` | Hay una sesión de depuración activa |
| `debugState == 'stopped'` | La depuración está detenida en un breakpoint |
| `findWidgetVisible` | La caja de búsqueda del editor está abierta |

Se combinan con `&&` (y), `||` (o) y `!` (negación). Ejemplo: `textInputFocus && !editorReadonly` significa "con el foco en un texto editable".

Para ver qué claves de contexto están activas en un momento dado, ejecuta `Developer: Inspect Context Keys` desde la Command Palette y haz clic en la zona que te interese. El resultado aparece en las herramientas de desarrollo de VS Code. Es una herramienta avanzada, útil cuando un atajo "no hace nada".

## 3. Personalización

Todas estas operaciones se hacen desde el editor de atajos, con clic derecho sobre la fila del comando:

| Operación | Cómo | Resultado |
|---|---|---|
| **Cambiar una combinación** | Doble clic en la fila, o clic derecho > **Change Keybinding...**. Pulsa la nueva combinación y `Enter`. | La combinación anterior deja de funcionar y la nueva la sustituye |
| **Crear una combinación propia** | Clic derecho > **Add Keybinding...**. Pulsa la combinación y `Enter`. | Se añade otra combinación. Si el comando ya tenía una, se conservan las dos. |
| **Eliminar una asignación** | Clic derecho > **Remove Keybinding**, o tecla `Supr` | El comando se queda sin esa combinación |
| **Restaurar el valor por defecto** | Clic derecho > **Reset Keybinding** (solo aparece en filas modificadas) | Se borran tus reglas para ese comando y vuelve la asignación de VS Code |
| **Cambiar la condición** | Clic derecho > **Change When Expression** | El atajo solo actúa en el contexto indicado |

Mientras se graba una combinación, `Esc` cancela sin cambiar nada.

### Resolver conflictos

Si la combinación que quieres ya está asignada a otro comando, tienes estas opciones:

1. **Elegir otra combinación libre.** Es lo más sencillo. Las combinaciones con `Ctrl + Shift + Alt` suelen estar libres.
2. **Limitar tu regla con `when`.** Por ejemplo, que solo actúe con `editorLangId == java`. El otro comando sigue funcionando en los demás archivos.
3. **Eliminar la asignación del otro comando** (**Remove Keybinding** sobre su fila), si nunca lo usas.

> **Teclado español:** evita combinaciones `Ctrl + Alt + letra`. En Windows, `Ctrl + Alt` equivale a `AltGr`, que se usa para escribir caracteres como `@`, `#` o `€`. Algunas combinaciones dejarían de escribir esos caracteres.

### Dónde se guardan los cambios

Las personalizaciones se guardan en el archivo `keybindings.json` de tu **usuario**:

- **Afectan a todas las ventanas** de VS Code, no solo a la carpeta abierta.
- **Con WSL** se guardan en Windows, porque la interfaz de VS Code se ejecuta en Windows.
- **Con perfiles** de VS Code (**File > Preferences > Profiles**), cada perfil tiene su propio `keybindings.json`.

No existen atajos por carpeta o por proyecto. Por eso cualquier cambio que hagas en esta práctica afecta a todo tu VS Code y conviene restaurarlo al terminar.

## 4. keybindings.json

### Cómo abrirlo

- Desde el editor de atajos: icono **Open Keyboard Shortcuts (JSON)**, en la esquina superior derecha. Es un icono de archivo con una flecha.
- Desde la Command Palette: `Preferences: Open Keyboard Shortcuts (JSON)`.

Si nunca has personalizado atajos, el archivo contiene una lista vacía (`[]`) y quizá un comentario. La lista completa de atajos de VS Code se consulta, en solo lectura, con `Preferences: Open Default Keyboard Shortcuts (JSON)`.

### Estructura

El archivo es una **lista** (`[ ... ]`) de reglas. Cada regla es un objeto con estas propiedades:

| Propiedad | Obligatoria | Descripción |
|---|---|---|
| `key` | Sí | Combinación en minúsculas, separada por `+`: `"ctrl+shift+alt+u"`. Modificadores: `ctrl`, `shift`, `alt`, `win` (tecla Windows). Para combinaciones en dos pasos (acordes), separa los pasos con un espacio: `"ctrl+k ctrl+s"`. |
| `command` | Sí | Identificador del comando. Con un `-` delante (`"-editor.action.addSelectionToNextFindMatch"`), la regla **elimina** una asignación existente. |
| `when` | No | Condición de activación (claves de contexto) |
| `args` | No | Argumentos para el comando. Solo algunos comandos los aceptan, por ejemplo `editor.action.insertSnippet`. |

A diferencia de un `.json` normal, `keybindings.json` **admite comentarios** con `//`. VS Code lo trata como JSON con comentarios.

### Editar visualmente o editar el JSON

| | Editor visual (Keyboard Shortcuts) | `keybindings.json` |
|---|---|---|
| Facilidad | Más sencillo: se graba la combinación pulsándola | Hay que escribir el nombre de las teclas y el identificador del comando |
| Conflictos | Avisa al asignar | No avisa. Hay que comprobarlos después en el editor visual. |
| Reglas de eliminación | Las crea automáticamente al cambiar un atajo | Hay que escribirlas a mano (`-comando`) |
| `args` | No permite definirlos | Es la única forma de pasar argumentos |
| Comentarios y orden | No | Sí: se pueden documentar y agrupar reglas |
| Compartir o reutilizar | No directamente | Se puede copiar el archivo o fragmentos |

Los dos métodos modifican el **mismo archivo**: lo que se cambia en uno aparece en el otro.

Al **cambiar** un atajo desde el editor visual, VS Code escribe **dos reglas**: una que asigna la nueva combinación y otra que elimina la antigua. Es exactamente lo que verás en la práctica guiada.

### Ejemplos comentados

Esta es la versión comentada de `ejemplos/keybindings-ejemplo.json`. El archivo del ejercicio no lleva comentarios porque, al ser un `.json` normal, VS Code los marcaría como error. Si quieres usar alguna regla, copia **solo esa regla** en tu `keybindings.json`.

```jsonc
[
    // 1. Combinación propia para un comando que no tiene ninguna:
    //    pasar a mayúsculas el texto seleccionado.
    //    Ctrl+Shift+Alt+U está libre en VS Code por defecto.
    {
        "key": "ctrl+shift+alt+u",
        "command": "editor.action.transformToUppercase",
        "when": "editorTextFocus && !editorReadonly"
    },

    // 2. Uso de "args": insertar un fragmento de código, solo en archivos Java.
    //    ${1:mensaje} es el primer punto de edición; $0, la posición final del cursor.
    {
        "key": "ctrl+shift+alt+p",
        "command": "editor.action.insertSnippet",
        "when": "editorTextFocus && !editorReadonly && editorLangId == java",
        "args": {
            "snippet": "System.out.println(${1:mensaje});$0"
        }
    },

    // 3 y 4. Imitar Eclipse: Ctrl+D elimina la línea.
    //    Por defecto, Ctrl+D selecciona la siguiente coincidencia. La regla 4
    //    (con "-" delante del comando) elimina esa asignación para evitar el conflicto.
    {
        "key": "ctrl+d",
        "command": "editor.action.deleteLines",
        "when": "textInputFocus && !editorReadonly"
    },
    {
        "key": "ctrl+d",
        "command": "-editor.action.addSelectionToNextFindMatch",
        "when": "editorFocus"
    },

    // 5. Imitar Eclipse: Ctrl+1 abre Quick Fix, solo en archivos Java.
    //    En el resto de archivos, Ctrl+1 sigue enfocando el primer grupo de editores.
    {
        "key": "ctrl+1",
        "command": "editor.action.quickFix",
        "when": "editorTextFocus && !editorReadonly && editorLangId == java"
    },

    // 6. Imitar Eclipse: Ctrl+Shift+O organiza imports, solo en archivos Java.
    //    Inconveniente: en archivos Java se pierde "Go to Symbol in Editor"
    //    (Ctrl+Shift+O por defecto); seguiría disponible con Ctrl+P y "@".
    {
        "key": "ctrl+shift+o",
        "command": "editor.action.organizeImports",
        "when": "editorTextFocus && !editorReadonly && editorLangId == java"
    },

    // 7. Imitar Eclipse: Ctrl+F11 ejecuta sin depurar (en VS Code, Ctrl+F5).
    {
        "key": "ctrl+f11",
        "command": "workbench.action.debug.run",
        "when": "editorLangId == java"
    }
]
```

Las reglas 3 a 7 muestran **cómo** imitar otro IDE y qué conflictos aparecen al hacerlo. No son una recomendación para el curso: los materiales usan los atajos por defecto de VS Code.

## 5. Migración desde otros IDE

Hay tres enfoques.

### Opción A. Extensión de keymap

VS Code tiene extensiones que reasignan de golpe muchos atajos para imitar otro IDE. Para verlas:

- Command Palette > `Preferences: Keymaps`, o `Ctrl + K` y, a continuación, `Ctrl + M`.
- O busca `keymap` en la vista **Extensions**.

Entre ellas están **Eclipse Keymap** e **IntelliJ IDEA Keybindings**. Comprueba el editor y el número de descargas antes de instalar cualquier extensión. En este curso **no se instala ninguna**: la elección queda para cada uno, una vez terminado el curso.

### Opción B. Personalizar solo algunos atajos

Se cambian a mano los cuatro o cinco atajos que más se echan de menos, como en los ejemplos del apartado 4, con condiciones `when` para limitar los conflictos.

### Opción C. Aprender los atajos de VS Code

No se cambia nada y se usa la tabla de equivalencias del apartado 1.2.1 como chuleta.

### Ventajas e inconvenientes

| | A. Keymap | B. Algunos atajos | C. Atajos de VS Code |
|---|---|---|---|
| Curva de aprendizaje inicial | Mínima | Baja | Mayor |
| Coherencia con documentación, tutoriales y compañeros | Baja: casi nada coincide | Media | Total |
| Coherencia con los materiales del curso | Baja | Media | Total |
| Conflictos con funciones propias de VS Code (cursores múltiples, Command Palette…) | Frecuentes. Algunas funciones quedan sin atajo. | Pocos, si se usa `when` | Ninguno |
| Mantenimiento | Depende de una extensión de terceros | Un archivo propio, fácil de revisar | Ninguno |
| Trabajo en equipos de otras personas | Hay que reinstalar la extensión | Hay que llevar el archivo | Funciona en cualquier VS Code |

Recomendación práctica: durante el curso, **opción C**, con alguna personalización puntual (opción B) si un atajo concreto resulta muy incómodo. Al terminar, cada uno puede decidir si un *keymap* le compensa.

## 6. Práctica guiada

**Antes de empezar:**

1. Abre el editor de atajos y escribe `@source:user` en el buscador. Anota las personalizaciones que ya tengas, si las hay: al final solo debes deshacer las que crees en esta práctica.
2. Abre `ejemplos/keybindings-ejemplo.json`. Servirá como archivo de pruebas: cualquier cambio que hagas en él se deshace con `Ctrl + Z` y no se guarda.

Mantén desactivado el guardado automático (**File > Auto Save** sin marcar).

### Actividad 1. Localizar un atajo

1. En el editor de atajos, busca `delete line`.
2. Observa la fila **Delete Line**: combinación, condición **When** y **Source**.
3. Activa **Record Keys** (icono de teclado o `Alt + K`) y pulsa `Ctrl + D`.

**Resultado esperado:**

- Paso 2: **Delete Line** aparece con `Ctrl + Shift + K`, condición `textInputFocus && !editorReadonly` y origen `Default`.
- Paso 3: aparece, entre otros, **Add Selection To Next Find Match**, el comando que usa `Ctrl + D` en el editor.

Desactiva **Record Keys** y borra el buscador.

### Actividad 2. Modificar un atajo

1. Busca de nuevo `delete line`.
2. Haz doble clic en la fila **Delete Line**.
3. Pulsa `Ctrl + Shift + Alt + K`. Antes de confirmar, comprueba que el diálogo no indica comandos existentes con esa combinación.
4. Pulsa `Enter`.

**Resultado esperado:** la fila muestra ahora `Ctrl + Shift + Alt + K` y el origen `User`.

### Actividad 3. Comprobar el cambio

1. Ve a la pestaña de `keybindings-ejemplo.json` y sitúa el cursor en cualquier línea.
2. Pulsa `Ctrl + Shift + Alt + K`: la línea se elimina. Deshaz con `Ctrl + Z`.
3. Pulsa `Ctrl + Shift + K`: ya no elimina la línea.
4. Comprueba con `File: Compare Active File with Saved` que el archivo no tiene cambios. Si los tuviera, usa `File: Revert File`.

**Resultado esperado:** el comando responde a la nueva combinación y no a la antigua.

### Actividad 4. Crear una combinación propia

1. En el editor de atajos, busca `transform to uppercase`. El comando **Transform to Uppercase** no tiene combinación asignada.
2. Clic derecho > **Add Keybinding...**, pulsa `Ctrl + Shift + Alt + U` y `Enter`.
3. En `keybindings-ejemplo.json`, haz doble clic sobre la palabra `command` para seleccionarla y pulsa `Ctrl + Shift + Alt + U`.
4. Deshaz con `Ctrl + Z`.

**Resultado esperado:** la palabra seleccionada pasa a `COMMAND` y, tras deshacer, vuelve a minúsculas.

### Actividad 5. Detectar un conflicto

1. En el editor de atajos, haz clic derecho sobre **Delete Line** > **Add Keybinding...**.
2. Pulsa `Ctrl + D` sin confirmar.
3. Lee el aviso del diálogo: indica que hay comandos existentes con esa combinación. Haz clic en él para verlos.
4. Pulsa `Esc` para cancelar sin asignar nada.
5. Como alternativa, busca `"ctrl+d"` (entre comillas) y, sobre cualquiera de las filas, usa clic derecho > **Show Same Keybindings**.

**Resultado esperado:** ves que `Ctrl + D` ya está en uso (por **Add Selection To Next Find Match**, entre otros) y entiendes por qué la regla 3 del ejemplo necesita la regla 4. No se ha añadido ninguna asignación.

### Actividad 6. Consultar el JSON

1. Pulsa el icono **Open Keyboard Shortcuts (JSON)** en la esquina superior derecha del editor de atajos.
2. Localiza las reglas creadas en esta práctica.
3. Compáralas con `ejemplos/keybindings-ejemplo.json`.
4. Cierra `keybindings.json` sin modificarlo.

**Resultado esperado:** además de las personalizaciones previas que tuvieras, aparecen tres reglas:

```jsonc
{ "key": "ctrl+shift+alt+k", "command": "editor.action.deleteLines", "when": "textInputFocus && !editorReadonly" },
{ "key": "ctrl+shift+k", "command": "-editor.action.deleteLines", "when": "textInputFocus && !editorReadonly" },
{ "key": "ctrl+shift+alt+u", "command": "editor.action.transformToUppercase" }
```

La segunda es la regla de eliminación que VS Code crea automáticamente al **cambiar** un atajo. La tercera puede incluir también una condición `when`, según la versión de VS Code. El orden y el formato de las propiedades pueden variar.

### Actividad 7. Restaurar los valores originales

1. En el editor de atajos, busca `@source:user`.
2. Sobre **Delete Line**, clic derecho > **Reset Keybinding**.
3. Sobre **Transform to Uppercase**, clic derecho > **Reset Keybinding**, o **Remove Keybinding**.
4. Comprueba de nuevo `@source:user` y el archivo `keybindings.json`.
5. En `keybindings-ejemplo.json`, comprueba que `Ctrl + Shift + K` vuelve a eliminar la línea y deshaz con `Ctrl + Z`.

**Resultado esperado:** `@source:user` muestra solo las personalizaciones que tenías antes de la práctica, o ninguna. **Delete Line** vuelve a `Ctrl + Shift + K` con origen `Default`, y en `keybindings.json` han desaparecido las tres reglas de la práctica. `keybindings-ejemplo.json` no tiene cambios sin guardar.

Si algo no se restaura desde el editor visual, abre `keybindings.json` y borra **solo** las reglas añadidas en esta práctica. Comprueba que el archivo sigue siendo una lista válida (`[ ... ]`), con comas entre reglas.

## 7. Comparativa Eclipse / IntelliJ IDEA / VS Code

### Gestión de atajos

| Aspecto | Eclipse | IntelliJ IDEA | VS Code |
|---|---|---|---|
| Dónde se configuran | **Window > Preferences > General > Keys** | **File > Settings > Keymap** | **File > Preferences > Keyboard Shortcuts** |
| Buscar por combinación | Filtro por *Binding* en la tabla | Icono de búsqueda por atajo en **Keymap** | **Record Keys** (`Alt + K`) |
| Ver la lista de atajos | `Ctrl + Shift + L` | **Help > Keyboard Shortcuts PDF** | **Help > Keyboard Shortcuts Reference** (`Ctrl + K`, `Ctrl + R`) |
| Esquemas predefinidos | *Scheme*: Default, Emacs… | *Keymaps*: Windows, Eclipse, NetBeans, VS Code… | Extensiones de *keymap* (**Preferences: Keymaps**) |
| Contexto de activación | *When*: In Windows, Editing Java Source… | Por ventana o componente, no editable | Expresiones `when` editables con claves de contexto |
| Archivo de configuración | Preferencias del *workspace* | Archivos XML del *keymap* | `keybindings.json`, legible y editable |
| Restaurar | **Restore Defaults** | **Reset Changes** sobre el *keymap* | **Reset Keybinding** por comando, o borrar reglas de `keybindings.json` |

IntelliJ IDEA incluye de serie *keymaps* que imitan Eclipse y VS Code. VS Code necesita una extensión para imitar otros IDE.

### Mismas teclas, distinto significado

Son las combinaciones que más confusión generan en las primeras semanas:

| Combinación | Eclipse | IntelliJ IDEA | VS Code (por defecto) | Equivalente en VS Code de la acción de Eclipse |
|---|---|---|---|---|
| `Ctrl + D` | Eliminar línea | Duplicar línea | Seleccionar la siguiente coincidencia | `Ctrl + Shift + K` |
| `Ctrl + 1` | Quick Fix | Ir a marcador 1 | Enfocar el primer grupo de editores | `Ctrl + .` |
| `F3` | Abrir declaración | Buscar la siguiente coincidencia | Buscar la siguiente coincidencia | `F12` |
| `Ctrl + Shift + O` | Organizar imports | Abrir proyecto Gradle o Maven (según versión) | Ir a un símbolo del archivo | `Shift + Alt + O` |
| `Ctrl + Shift + R` | Abrir recurso | Reemplazar en archivos | Menú *Refactor* | `Ctrl + P` |
| `Ctrl + Shift + T` | Abrir tipo | Ir a test | Reabrir el editor cerrado | `Ctrl + T` |
| `Ctrl + Shift + F` | Formatear | Buscar en archivos | Buscar en archivos | `Shift + Alt + F` |
| `Ctrl + H` | Buscar en el *workspace* | Jerarquía de tipos | Reemplazar en el archivo | `Ctrl + Shift + F` |
| `Ctrl + Y` | Rehacer | Eliminar línea | Rehacer | — |
| `Ctrl + E` | Lista de editores abiertos | Archivos recientes | Ir a archivo | `Ctrl + Tab` |
| `F11` | Depurar | Alternar marcador | Pantalla completa (Step Into durante la depuración) | `F5` |
| `Ctrl + Alt + ↓` | Duplicar línea | — | Añadir cursor debajo | `Shift + Alt + ↓` |

## 8. Comprobaciones finales

Comprueba que eres capaz de:

- [ ] Abrir el editor de atajos desde el menú y con `Ctrl + K`, `Ctrl + S`.
- [ ] Buscar un comando por nombre o identificador y una combinación con **Record Keys**.
- [ ] Interpretar las columnas **When** y **Source**, y filtrar con `@source:user`.
- [ ] Cambiar la combinación de un comando, añadir una combinación nueva y eliminar una asignación.
- [ ] Detectar cuándo una combinación ya está en uso y elegir cómo resolver el conflicto.
- [ ] Abrir `keybindings.json`, reconocer las propiedades `key`, `command`, `when` y `args` e interpretar una regla de eliminación (`-comando`).
- [ ] Explicar por qué al cambiar un atajo aparecen dos reglas en `keybindings.json`.
- [ ] Restaurar un atajo con **Reset Keybinding** y comprobar que `@source:user` solo contiene tus personalizaciones previas.
- [ ] Valorar las ventajas e inconvenientes de instalar un *keymap* de Eclipse o IntelliJ IDEA.
- [ ] Indicar el equivalente en VS Code de `Ctrl + D`, `Ctrl + 1`, `F3` y `Ctrl + Shift + O` de Eclipse.
