# Ejercicio 1.2.1 — Atajos clave para programadores Java

Módulo 1. Introducción a VS Code · Tema 1.2. Shortcuts para mejorar la productividad · Apartado 1.2.1. Atajos clave para programadores Java

## 1. Introducción

Un desarrollador pasa buena parte del día navegando, buscando y modificando código existente, más que escribiendo código nuevo. Cada vez que se recurre al ratón para abrir un archivo, buscar una clase o duplicar una línea se pierden unos segundos y, sobre todo, se interrumpe la concentración. Los atajos de teclado reducen esas interrupciones.

Para quien viene de Eclipse o IntelliJ IDEA, el reto no es aprender a usar atajos, sino **reaprender las combinaciones**. La mayoría de las acciones existen en los tres entornos, pero con teclas distintas. Algunas combinaciones idénticas hacen cosas diferentes:

- `Ctrl + D` borra la línea en Eclipse, duplica la línea en IntelliJ IDEA y selecciona la siguiente coincidencia en VS Code.
- `F3` abre la declaración en Eclipse y busca la siguiente coincidencia en VS Code.

Este ejercicio recorre los atajos de VS Code más útiles para Java y los practica sobre un pequeño inventario de productos. Al final hay una tabla de equivalencias con Eclipse e IntelliJ IDEA. La personalización de atajos se trata en el apartado 1.2.2.

### Antes de empezar

- **Sistema operativo.** Los atajos indicados son los de **VS Code en Windows**, que también son los que se usan al trabajar con WSL, porque la ventana de VS Code se ejecuta en Windows. En Linux nativo algunos cambian. La tabla del apartado 8 indica las diferencias más habituales.
- **Distribución del teclado.** Los atajos que usan símbolos (`/`, `[`, `]`, `\`, `` ` ``) dependen de ella. En teclado español pueden aparecer con otra tecla. Hay una forma segura de saber el atajo real en tu equipo: abrir el menú correspondiente (**Edit**, **Selection**, **View**, **Go**, **Run**…), donde cada opción muestra su atajo a la derecha, o buscarlo en la Command Palette (`Ctrl + Shift + P`).
- **Teclas de función en portátiles.** En muchos portátiles las teclas `F1` a `F12` requieren pulsar también `Fn`.
- **Atajos personalizados.** Si tu VS Code tiene atajos personalizados o una extensión de *keymap* (por ejemplo, para imitar Eclipse o IntelliJ IDEA), algunas combinaciones no coincidirán con las de este documento.

## 2. Navegación entre archivos y clases

Abre VS Code en la raíz del repositorio del curso. El código del ejercicio está en `modulo-01-vscode/tema-1.2-shortcuts/apartado-1.2.1-atajos-java/src/curso/vscode/`:

| Archivo | Contenido |
|---|---|
| `Producto.java` | Identificador, nombre, precio (`BigDecimal`) y cantidad. Getters, `setCantidad`, `calcularValorStock` y `toString`. |
| `Inventario.java` | Lista de productos con `registrarProducto`, `buscarProducto`, `actualizarStock`, `calcularValorTotal`, `productosConStockBajo` y `listarProductos`. |
| `MainInventario.java` | Método `main` que registra cuatro productos, los consulta, actualiza existencias y muestra resultados. |

| Atajo | Comando | Menú | Qué hace |
|---|---|---|---|
| `Ctrl + P` | Go to File | **Go > Go to File...** | Abre un archivo escribiendo parte de su nombre |
| `Ctrl + T` | Go to Symbol in Workspace | **Go > Go to Symbol in Workspace...** | Busca clases, métodos y campos en todo el proyecto |
| `Ctrl + Shift + O` | Go to Symbol in Editor | **Go > Go to Symbol in Editor...** | Lista los métodos y campos del archivo actual |
| `F12` | Go to Definition | **Go > Go to Definition** | Salta a la declaración del elemento bajo el cursor |
| `Alt + F12` | Peek Definition | Clic derecho > **Peek > Peek Definition** | Muestra la declaración incrustada, sin cambiar de archivo |
| `Alt + ←` | Go Back | **Go > Back** | Vuelve a la posición anterior del cursor (incluso en otro archivo) |
| `Alt + →` | Go Forward | **Go > Forward** | Deshace el último *Go Back* |
| `Ctrl + Tab` | Open Previous Editor | **Go > Switch Editor** | Cambia entre los archivos abiertos, ordenados por uso reciente |
| `Ctrl + G` | Go to Line | **Go > Go to Line/Column...** | Salta a un número de línea |
| `Ctrl + F12` | Go to Implementations | **Go > Go to Implementations** | Salta a las implementaciones de una interfaz o método abstracto |

Detalles útiles:

- **`Ctrl + P`** admite prefijos:
  - `@` busca símbolos del archivo, como `Ctrl + Shift + O`;
  - `#` busca símbolos del proyecto, como `Ctrl + T`;
  - `:` salta a una línea, como `Ctrl + G`.
- **`Ctrl + Tab`**: mantén `Ctrl` pulsado y pulsa `Tab` varias veces para recorrer la lista. Al soltar `Ctrl` se abre el archivo seleccionado.
- **`Ctrl + Click`** sobre un nombre equivale a `F12`.

> **Requieren la extensión de Java:** `Ctrl + T`, `Ctrl + Shift + O`, `F12`, `Alt + F12` y `Ctrl + F12`. VS Code proporciona el atajo, pero es la extensión de Java la que analiza el código para saber dónde está cada clase o método. Sin ella, solo funcionan `Ctrl + P`, `Ctrl + G`, `Ctrl + Tab` y la navegación hacia atrás y hacia delante.

## 3. Búsqueda

| Atajo | Comando | Menú | Ámbito |
|---|---|---|---|
| `Ctrl + F` | Find | **Edit > Find** | Archivo actual |
| `Ctrl + H` | Replace | **Edit > Replace** | Archivo actual |
| `F3` / `Shift + F3` | Find Next / Find Previous | **Edit > Find Next** / **Find Previous**, si aparecen; si no, Command Palette | Siguiente o anterior coincidencia |
| `Ctrl + Shift + F` | Find in Files | **Edit > Find in Files** | Todos los archivos de la carpeta abierta |
| `Ctrl + Shift + H` | Replace in Files | **Edit > Replace in Files** | Todos los archivos de la carpeta abierta |
| `Shift + F12` | Go to References | Clic derecho > **Go to References** | Usos del elemento bajo el cursor, en una ventana incrustada |
| `Shift + Alt + F12` | Find All References | Clic derecho > **Find All References** | Usos del elemento, en la vista lateral *References* |

- **Opciones de la caja de búsqueda.** Sus iconos activan **Match Case**, **Match Whole Word**, **Use Regular Expression** y, en `Ctrl + H`, **Find in Selection** (`Alt + L`), que limita el reemplazo al texto seleccionado.
- **Limitar `Ctrl + Shift + F`.** Como está abierta la raíz del repositorio, la búsqueda abarca todo el curso. Para buscar solo en este ejercicio, haz clic derecho sobre su carpeta `src` en el Explorer y elige **Find in Folder...**.
- **Texto frente a referencias.** Buscar texto encuentra palabras. Buscar referencias (**Go to References**, **Find All References**) encuentra **usos reales** del método o la clase, aunque haya otros métodos o comentarios con el mismo nombre.

> **Requiere la extensión de Java:** la búsqueda de referencias. La búsqueda de texto es de VS Code y funciona siempre.

## 4. Edición rápida

| Acción | Atajo (Windows) | Menú |
|---|---|---|
| Duplicar línea hacia abajo / arriba | `Shift + Alt + ↓` / `Shift + Alt + ↑` | **Selection > Copy Line Down** / **Copy Line Up** |
| Mover línea hacia abajo / arriba | `Alt + ↓` / `Alt + ↑` | **Selection > Move Line Down** / **Move Line Up** |
| Eliminar línea | `Ctrl + Shift + K` | Command Palette > `Delete Line` |
| Insertar línea debajo | `Ctrl + Enter` | — |
| Seleccionar la siguiente coincidencia | `Ctrl + D` | **Selection > Add Next Occurrence** |
| Seleccionar todas las coincidencias | `Ctrl + Shift + L` | **Selection > Select All Occurrences** |
| Cambiar todas las apariciones de la palabra | `Ctrl + F2` | Clic derecho > **Change All Occurrences** |
| Añadir cursor encima / debajo | `Ctrl + Alt + ↑` / `Ctrl + Alt + ↓` | **Selection > Add Cursor Above** / **Add Cursor Below** |
| Añadir cursor con el ratón | `Alt` + clic | — |
| Selección en columna | `Shift + Alt` + arrastrar | **Selection > Column Selection Mode** |
| Deshacer el último cursor añadido | `Ctrl + U` | — |
| Ampliar la selección | `Shift + Alt + →` | **Selection > Expand Selection** |
| Comentar / descomentar líneas | `Ctrl + /` (depende del teclado) | **Edit > Toggle Line Comment** |
| Comentario de bloque | `Shift + Alt + A` | **Edit > Toggle Block Comment** |
| Indentar / desindentar | `Tab` / `Shift + Tab` con líneas seleccionadas, o `Ctrl + ]` / `Ctrl + [` (depende del teclado) | Command Palette > `Indent Line` / `Outdent Line` |
| Salir de los cursores múltiples | `Esc` | — |

- **`Ctrl + /` y teclado español.** `/` se escribe con `Shift + 7`, así que VS Code asigna el comando a otra tecla física. El menú **Edit > Toggle Line Comment** muestra la combinación real de tu equipo.
- **Cursores múltiples frente a selección múltiple.**
  - Con **selección múltiple** (`Ctrl + D`, `Ctrl + Shift + L`), cada coincidencia queda seleccionada y lo que escribes reemplaza a todas.
  - Con **cursores múltiples** (`Ctrl + Alt + ↓`, `Alt` + clic), hay varios puntos de inserción y lo que escribes aparece en todos.
- **`Ctrl + F2` frente a Rename Symbol.** `Ctrl + F2` cambia todas las apariciones del **texto** en el archivo actual. Para renombrar un elemento de Java en todo el proyecto se usa **Rename Symbol** (apartado 5).

Todas estas acciones son de VS Code y funcionan con cualquier lenguaje.

## 5. Refactorización Java

| Acción | Atajo | Menú | Requiere la extensión de Java |
|---|---|---|---|
| Rename Symbol | `F2` | Clic derecho > **Rename Symbol** | Sí, para renombrar en todos los archivos |
| Quick Fix | `Ctrl + .` | Bombilla junto a la línea, o clic derecho > **Quick Fix...** | Sí, para las correcciones de Java |
| Refactor | `Ctrl + Shift + R` | Clic derecho > **Refactor...** | Sí |
| Source Action | — | Clic derecho > **Source Action...** | Sí |
| Organize Imports | `Shift + Alt + O` | Clic derecho > **Source Action...** > **Organize Imports** | Sí |
| Format Document | `Shift + Alt + F` | Clic derecho > **Format Document** | Sí, para Java (usa el formateador de la extensión) |
| Format Selection | `Ctrl + K`, `Ctrl + F` | Clic derecho > **Format Selection** | Sí, para Java |
| Autocompletar | `Ctrl + Space` | — | Sí, para las sugerencias de Java |
| Ayuda de parámetros | `Ctrl + Shift + Space` | — | Sí |

**Code Actions** es el nombre general de todo lo que ofrecen la bombilla, `Ctrl + .`, **Refactor...** y **Source Action...**:

- **Quick Fix**: corrige un error o un aviso, por ejemplo importar una clase o crear un método que no existe.
- **Refactor**: reorganiza el código sin cambiar su comportamiento. Incluye extraer una variable, un método o una constante, convertir a expresión lambda, etc.
- **Source Action**: genera o reorganiza código. Incluye generar getters y setters, constructores, `toString`, `equals` y `hashCode`, y organizar imports.

Las opciones que aparecen dependen de dónde esté el cursor y de qué haya seleccionado. VS Code proporciona el mecanismo, pero **todas las acciones específicas de Java las aporta la extensión de Java**. Si la extensión no está lista (el indicador de Java de la Status Bar sigue cargando), la bombilla no ofrece nada útil.

## 6. Ejecución y depuración

| Acción | Atajo | Menú |
|---|---|---|
| Ejecutar sin depurar | `Ctrl + F5` | **Run > Run Without Debugging** |
| Iniciar depuración | `F5` | **Run > Start Debugging** |
| Activar o desactivar un breakpoint | `F9` | **Run > Toggle Breakpoint** |
| Step Over (ejecutar la línea sin entrar en los métodos) | `F10` | **Run > Step Over** |
| Step Into (entrar en el método llamado) | `F11` | **Run > Step Into** |
| Step Out (terminar el método actual y volver al llamador) | `Shift + F11` | **Run > Step Out** |
| Continue (seguir hasta el siguiente breakpoint) | `F5` | **Run > Continue** |
| Detener la depuración | `Shift + F5` | **Run > Stop Debugging** |
| Reiniciar la depuración | `Ctrl + Shift + F5` | **Run > Restart Debugging** |

- **Archivo activo.** Sin un archivo `launch.json`, `F5` y `Ctrl + F5` ejecutan la clase del **archivo activo**. Ten abierto `MainInventario.java` antes de pulsarlos. Si el archivo activo no tiene método `main`, VS Code puede pedirte que elijas una clase entre las del proyecto.
- **Breakpoints.** También se ponen y quitan con un clic en el margen izquierdo, junto al número de línea, donde aparece un punto rojo. **Run > Disable All Breakpoints** los desactiva todos sin borrarlos.
- **Barra de depuración.** Durante la depuración aparece una barra flotante con los botones *Continue*, *Step Over*, *Step Into*, *Step Out*, *Restart* y *Stop*. La vista **Run and Debug** muestra variables, expresiones vigiladas (*Watch*) y la pila de llamadas (*Call Stack*).

Ejecución y depuración las aporta la extensión **Debugger for Java**. Aquí solo se practican los controles básicos. La depuración en profundidad (breakpoints condicionales, evaluación de expresiones, configuraciones de lanzamiento) se trabaja en el módulo 3.

## 7. Práctica guiada

**Condiciones de la práctica:**

- Todo el código ya está preparado y funciona; no hay que programar nada nuevo.
- Todas las modificaciones son temporales. Al final de cada actividad se indica cómo deshacerla.
- Mantén **desactivado** el guardado automático (**File > Auto Save** sin marcar). Así ningún cambio se guarda por accidente.

Recursos para deshacer:

- `Ctrl + Z` deshace el último cambio en el archivo activo.
- `File: Revert File`, desde la Command Palette, descarta todos los cambios no guardados del archivo activo.
- `File: Compare Active File with Saved` muestra si queda algún cambio pendiente.

### Actividad 1. Abrir archivos con Ctrl + P

1. Pulsa `Ctrl + P` y escribe `Inventario`.
2. En la lista aparecen `Inventario.java` y `MainInventario.java`. Comprueba en la ruta gris que están en `apartado-1.2.1-atajos-java` y abre `Inventario.java`.
3. Repite con `Producto` y con `MainInventario`.

**Resultado esperado:** los tres archivos están abiertos en pestañas.

### Actividad 2. Recorrer símbolos del archivo y del proyecto

1. En `Inventario.java`, pulsa `Ctrl + Shift + O`, escribe `valor` y pulsa `Enter`.
2. Pulsa `Ctrl + T`, escribe `calcularValorStock` y elige el método de `Producto`.
3. Pulsa `Ctrl + Tab` varias veces, manteniendo `Ctrl` pulsado, y suelta sobre `MainInventario.java`.

**Resultado esperado:**

- Paso 1: el cursor salta a `calcularValorTotal`.
- Paso 2: se abre `Producto.java` en ese método.
- Paso 3: vuelves a `MainInventario.java` sin usar el ratón.

### Actividad 3. Ir a la definición y volver

1. En `MainInventario.java`, sitúa el cursor sobre `registrarProducto`, en cualquiera de las cuatro llamadas, y pulsa `F12`.
2. Pulsa `Alt + ←` para volver.
3. Sitúa el cursor sobre `getPrecio`, dentro del método `mostrarProducto`, y pulsa `Alt + F12`. Cierra la ventana incrustada con `Esc`.
4. Sobre `actualizarStock`, pulsa `F12` y, ya en `Inventario.java`, pulsa otra vez `F12` sobre `buscarProducto`. Después pulsa `Alt + ←` dos veces.

**Resultado esperado:**

- Paso 1: el editor salta a `Inventario.java`, al método `registrarProducto`.
- Paso 2: vuelves exactamente a la llamada de partida.
- Paso 3: el método `getPrecio` de `Producto` aparece incrustado, sin cambiar de archivo.
- Paso 4: los dos saltos te llevan al método `buscarProducto` de `Inventario.java`. Con dos `Alt + ←` vuelves a la línea de partida de `MainInventario.java`.

### Actividad 4. Buscar referencias

1. Abre `Producto.java` y sitúa el cursor sobre el nombre del método `getCantidad`.
2. Pulsa `Shift + F12` (**Go to References**).
3. Cierra con `Esc` y pulsa `Shift + Alt + F12` (**Find All References**).

**Resultado esperado:** aparecen los usos de `getCantidad`:

- uno en `Inventario.java`, en `productosConStockBajo`;
- dos en `MainInventario.java`, en el listado de stock bajo y en `mostrarProducto`.

Con **Find All References**, la lista queda en la vista lateral *References* y se puede recorrer.

### Actividad 5. Buscar y reemplazar texto (reversible)

1. En `MainInventario.java`, pulsa `Ctrl + F` y busca `uds.`. Recorre las coincidencias con `Enter` y `Shift + Enter`.
2. Pulsa `Ctrl + H`, escribe `unidades` como texto de reemplazo y pulsa **Replace All** (`Ctrl + Alt + Enter`).
3. Comprueba los cambios y deshazlos con `Ctrl + Z`.
4. Haz clic derecho sobre la carpeta `src` del ejercicio, elige **Find in Folder...** y busca `BigDecimal`.

**Resultado esperado:**

- Paso 1: `uds.` aparece 2 veces.
- Paso 3: tras deshacer, el archivo vuelve a su estado original.
- Paso 4: hay resultados en los tres archivos, agrupados por archivo.

### Actividad 6. Duplicar, mover y eliminar líneas (reversible)

1. En `MainInventario.java`, sitúa el cursor en la línea que registra el `"Monitor"`.
2. Pulsa `Shift + Alt + ↓`: la línea se duplica.
3. Con el cursor en la copia, pulsa `Alt + ↑` dos veces: la línea sube.
4. Pulsa `Ctrl + Shift + K`: la línea se elimina.
5. Ejecuta `File: Compare Active File with Saved` desde la Command Palette.

**Resultado esperado:** la comparación del paso 5 no muestra diferencias. Duplicar y eliminar se compensan, así que el archivo queda como estaba. Si hubiera alguna diferencia, usa `File: Revert File`.

### Actividad 7. Selección múltiple y cursores múltiples (reversible)

1. En `MainInventario.java`, haz doble clic sobre la palabra `registrarProducto` de la primera llamada.
2. Pulsa `Ctrl + D` tres veces: quedan seleccionadas las cuatro llamadas. Escribe `anadirProducto`: cambian las cuatro a la vez, y el código deja de compilar porque ese método no existe. Pulsa `Esc` y deshaz con `Ctrl + Z`.
3. Sitúa el cursor al principio de la línea del `"Teclado"` y pulsa `Ctrl + Alt + ↓` tres veces: hay un cursor en cada una de las cuatro líneas de registro.
4. Escribe `// `: las cuatro líneas quedan comentadas. Pulsa `Esc` y deshaz con `Ctrl + Z`.
5. Prueba también a hacer `Alt` + clic en dos o tres sitios distintos y escribir un carácter. Después deshaz los cambios.

**Resultado esperado:** cada edición se aplica en todas las posiciones a la vez y, tras deshacer, el archivo queda como al principio. Compruébalo con `File: Compare Active File with Saved`.

### Actividad 8. Comentar e indentar (reversible)

1. En `Inventario.java`, selecciona las líneas del método `listarProductos` completo, desde el comentario `/**` hasta la llave de cierre.
2. Usa **Edit > Toggle Line Comment** y fíjate en el atajo que muestra el menú. Todas las líneas quedan comentadas con `//`. Repite para descomentarlas.
3. Con las mismas líneas seleccionadas, pulsa `Tab` dos veces y después `Shift + Tab` dos veces.
4. Usa **Edit > Toggle Block Comment** (`Shift + Alt + A`) y deshaz con `Ctrl + Z`.

**Resultado esperado:** las líneas se comentan, se descomentan, se indentan y se desindentan. Al terminar, `File: Compare Active File with Saved` no muestra diferencias.

### Actividad 9. Renombrar un método con F2 (reversible)

1. En `Inventario.java`, sitúa el cursor sobre `calcularValorTotal` y pulsa `F2`.
2. Escribe `calcularValorInventario` y pulsa `Shift + Enter` para ver la vista previa: aparecen `Inventario.java` y `MainInventario.java`. Aplica el cambio.
3. Comprueba en `MainInventario.java` que la llamada ha cambiado.
4. Deshaz el cambio con otro `F2`: sobre `calcularValorInventario`, escribe de nuevo `calcularValorTotal`.
5. Si alguna pestaña muestra el círculo de cambios sin guardar, usa **File > Save All**.

**Resultado esperado:** el nombre cambia en la declaración y en la llamada, y vuelve al original con el segundo `F2`.

### Actividad 10. Quick Fix y Organize Imports (reversible)

1. En `Inventario.java`, borra la línea `import java.util.Optional;`. En unos segundos, `Optional` aparece subrayado en rojo.
2. Sitúa el cursor sobre uno de los `Optional` subrayados y pulsa `Ctrl + .`. Elige **Import 'Optional' (java.util)**.
3. Añade una línea `import java.util.Map;` debajo de los demás imports. Aparece un aviso amarillo, porque el import no se usa.
4. Pulsa `Shift + Alt + O` (**Organize Imports**).
5. Ejecuta `File: Compare Active File with Saved`.

**Resultado esperado:**

- Paso 2: el import de `Optional` vuelve a su sitio y el error desaparece.
- Paso 4: el import de `Map` desaparece.
- Paso 5: no hay diferencias. Si las hubiera, usa `File: Revert File`.

### Actividad 11. Refactorizar y formatear (reversible)

1. En `MainInventario.java`, selecciona la expresión `inventario.calcularValorTotal()` de la última línea de `main`.
2. Pulsa `Ctrl + Shift + R` (**Refactor...**) y elige **Extract to local variable**. La extensión crea una variable con ese valor y la usa en el `println`. Deshaz con `Ctrl + Z`.
3. Selecciona las líneas del bucle `for` de `main` y pulsa `Shift + Tab` hasta dejarlas pegadas al margen.
4. Pulsa `Shift + Alt + F` (**Format Document**).

**Resultado esperado:** el bucle recupera su indentación y el archivo vuelve a estar exactamente como al principio. El código del ejercicio ya está formateado con el formateador de Java por defecto. Compruébalo con `File: Compare Active File with Saved`.

### Actividad 12. Ejecutar y depurar

1. Con `MainInventario.java` activo, pulsa `Ctrl + F5`. Comprueba la salida con la del apartado 9.
2. Sitúa el cursor en la línea `boolean actualizado = inventario.actualizarStock(3, 12);` y pulsa `F9`. Aparece un punto rojo en el margen.
3. Pulsa `F5`. La ejecución se detiene en esa línea, resaltada en amarillo.
4. Pulsa `F11` (**Step Into**): entras en `actualizarStock`, en `Inventario.java`.
5. Pulsa `F10` (**Step Over**) dos veces y observa en la vista **Run and Debug** las variables `id`, `nuevaCantidad` y `encontrado`.
6. Pulsa `Shift + F11` (**Step Out**): vuelves a `MainInventario.java`.
7. Pulsa `F5` (**Continue**): el programa termina.
8. Quita el breakpoint con `F9` sobre la misma línea.

**Resultado esperado:** la salida de `Ctrl + F5` coincide con la del apartado 9. Durante la depuración, la ejecución se detiene, entra en el método, vuelve y termina con la misma salida. Al final no queda ningún breakpoint. En la vista **Run and Debug**, la sección *Breakpoints* está vacía.

### Comprobación final

- Ninguna pestaña muestra cambios sin guardar.
- `File: Compare Active File with Saved` no muestra diferencias en los tres archivos.
- El programa sigue produciendo la salida del apartado 9.

## 8. Comparativa con Eclipse e IntelliJ IDEA

Atajos predeterminados en Windows: VS Code 1.146, Eclipse con su *keymap* por defecto e IntelliJ IDEA con el *keymap* **Windows**.

| Acción | VS Code | Eclipse | IntelliJ IDEA | Observaciones |
|---|---|---|---|---|
| Abrir archivo por nombre | `Ctrl + P` | `Ctrl + Shift + R` | `Ctrl + Shift + N` | — |
| Buscar clase o símbolo | `Ctrl + T` | `Ctrl + Shift + T` (tipos) | `Ctrl + N` (clases), `Ctrl + Alt + Shift + N` (símbolos) | En Eclipse, `Ctrl + T` muestra la jerarquía de tipos. En VS Code requiere la extensión de Java. |
| Estructura del archivo | `Ctrl + Shift + O` | `Ctrl + O` | `Ctrl + F12` | En Eclipse, `Ctrl + Shift + O` organiza imports |
| Buscar en todas partes | `Ctrl + P` con prefijos (`>`, `@`, `#`, `:`) | `Ctrl + 3` | `Shift` dos veces | VS Code no tiene un buscador único: usa prefijos en la misma caja |
| Ir a la declaración | `F12` o `Ctrl + Click` | `F3` o `Ctrl + Click` | `Ctrl + B` o `Ctrl + Click` | En VS Code, `F3` es "buscar siguiente" |
| Ver la declaración sin saltar | `Alt + F12` | Pasar el ratón con `Shift` | `Ctrl + Shift + I` | — |
| Ir a la implementación | `Ctrl + F12` | `Ctrl + T` sobre el método | `Ctrl + Alt + B` | — |
| Volver / avanzar | `Alt + ←` / `Alt + →` | `Alt + ←` / `Alt + →` | `Ctrl + Alt + ←` / `Ctrl + Alt + →` | En Linux nativo, VS Code usa `Ctrl + Alt + -` para volver |
| Cambiar entre archivos abiertos | `Ctrl + Tab` | `Ctrl + F6` o `Ctrl + E` | `Ctrl + Tab` o `Ctrl + E` (recientes) | — |
| Ir a línea | `Ctrl + G` | `Ctrl + L` | `Ctrl + G` | — |
| Buscar en el archivo | `Ctrl + F` | `Ctrl + F` | `Ctrl + F` | — |
| Reemplazar en el archivo | `Ctrl + H` | `Ctrl + F` (mismo diálogo) | `Ctrl + R` | En Eclipse, `Ctrl + H` abre la búsqueda en el *workspace* |
| Buscar en archivos | `Ctrl + Shift + F` | `Ctrl + H` | `Ctrl + Shift + F` | En Eclipse, `Ctrl + Shift + F` formatea el código |
| Reemplazar en archivos | `Ctrl + Shift + H` | `Ctrl + H` > *Replace...* | `Ctrl + Shift + R` | En VS Code, `Ctrl + Shift + R` abre *Refactor* |
| Buscar referencias | `Shift + Alt + F12` o `Shift + F12` | `Ctrl + Shift + G` | `Alt + F7` | En VS Code requiere la extensión de Java |
| Duplicar línea | `Shift + Alt + ↓` | `Ctrl + Alt + ↓` | `Ctrl + D` | En Linux nativo, VS Code usa `Ctrl + Shift + Alt + ↓`. En VS Code, `Ctrl + Alt + ↓` añade un cursor. |
| Mover línea | `Alt + ↑` / `Alt + ↓` | `Alt + ↑` / `Alt + ↓` | `Alt + Shift + ↑` / `Alt + Shift + ↓` | — |
| Eliminar línea | `Ctrl + Shift + K` | `Ctrl + D` | `Ctrl + Y` | En VS Code, `Ctrl + D` selecciona la siguiente coincidencia y `Ctrl + Y` rehace |
| Siguiente coincidencia en selección múltiple | `Ctrl + D` | — | `Alt + J` | Eclipse no tiene cursores múltiples. Ofrece selección en bloque (`Alt + Shift + A`). |
| Todas las coincidencias | `Ctrl + Shift + L` | — | `Ctrl + Alt + Shift + J` | — |
| Añadir cursor | `Ctrl + Alt + ↑ / ↓` o `Alt` + clic | — | `Alt + Shift` + clic, o `Ctrl` dos veces + flecha | En Linux nativo, VS Code usa `Shift + Alt + ↑ / ↓` |
| Comentar línea | `Ctrl + /` | `Ctrl + /` | `Ctrl + /` | Depende del teclado en los tres. Consulta el menú. |
| Comentario de bloque | `Shift + Alt + A` | `Ctrl + Shift + /` | `Ctrl + Shift + /` | En Linux nativo, VS Code usa `Ctrl + Shift + A` |
| Corregir indentación | `Shift + Alt + F` (formatea todo) | `Ctrl + I` | `Ctrl + Alt + I` | VS Code no tiene "solo indentación". Usa formatear documento o selección. |
| Renombrar | `F2` | `Alt + Shift + R` | `Shift + F6` | En VS Code requiere la extensión de Java para renombrar en varios archivos |
| Corrección rápida | `Ctrl + .` | `Ctrl + 1` | `Alt + Enter` | En VS Code, `Ctrl + 1` enfoca el primer grupo de editores |
| Menú de refactorización | `Ctrl + Shift + R` | `Alt + Shift + T` | `Ctrl + Alt + Shift + T` | — |
| Generar código (getters, toString…) | Clic derecho > **Source Action...** | `Alt + Shift + S` | `Alt + Insert` | En VS Code requiere la extensión de Java |
| Organizar imports | `Shift + Alt + O` | `Ctrl + Shift + O` | `Ctrl + Alt + O` | — |
| Formatear código | `Shift + Alt + F` | `Ctrl + Shift + F` | `Ctrl + Alt + L` | En Linux nativo, VS Code usa `Ctrl + Shift + I` |
| Autocompletar | `Ctrl + Space` | `Ctrl + Space` | `Ctrl + Space` | — |
| Paleta de comandos / acciones | `Ctrl + Shift + P` | `Ctrl + 3` | `Ctrl + Shift + A` | — |
| Ejecutar | `Ctrl + F5` | `Ctrl + F11` | `Shift + F10` | En VS Code, `Shift + F10` abre el menú contextual |
| Depurar | `F5` | `F11` | `Shift + F9` | En VS Code, `F11` es *Step Into* durante la depuración y pantalla completa fuera de ella |
| Activar o desactivar breakpoint | `F9` | `Ctrl + Shift + B` | `Ctrl + F8` | En VS Code, `Ctrl + Shift + B` lanza la tarea de compilación |
| Step Over | `F10` | `F6` | `F8` | — |
| Step Into | `F11` | `F5` | `F7` | En VS Code, `F5` es iniciar o continuar |
| Step Out | `Shift + F11` | `F7` | `Shift + F8` | — |
| Continuar | `F5` | `F8` | `F9` | — |
| Detener | `Shift + F5` | `Ctrl + F2` | `Ctrl + F2` | — |
| Cerrar editor | `Ctrl + F4` o `Ctrl + W` | `Ctrl + W` | `Ctrl + F4` | — |
| Reabrir editor cerrado | `Ctrl + Shift + T` | — | — | En Eclipse, `Ctrl + Shift + T` abre un tipo |
| Mostrar u ocultar el panel lateral | `Ctrl + B` | `Ctrl + M` (maximizar editor) | `Ctrl + Shift + F12` (ocultar ventanas) | No son equivalentes exactos |
| Terminal integrado | `` Ctrl + ` `` (depende del teclado) | — | `Alt + F12` | En teclado español, `Ctrl + ñ`. Alternativa: **Terminal > New Terminal**. |

## 9. Ejecución

Compila las tres clases juntas: `MainInventario` necesita `Inventario` y `Producto`. Los comandos se ejecutan desde la **raíz del repositorio del curso**, que es la carpeta abierta en VS Code y la carpeta en la que se abre la Terminal integrada.

**Windows (PowerShell):**

```powershell
$EJ = "modulo-01-vscode/tema-1.2-shortcuts/apartado-1.2.1-atajos-java"
$SRC = "$EJ/src/curso/vscode"
javac --release 21 -Xlint:all -d "$EJ/out" "$SRC/Producto.java" "$SRC/Inventario.java" "$SRC/MainInventario.java"
java -cp "$EJ/out" curso.vscode.MainInventario
```

**Linux o WSL (Bash):**

```bash
EJ=modulo-01-vscode/tema-1.2-shortcuts/apartado-1.2.1-atajos-java
SRC="$EJ/src/curso/vscode"
javac --release 21 -Xlint:all -d "$EJ/out" "$SRC/Producto.java" "$SRC/Inventario.java" "$SRC/MainInventario.java"
java -cp "$EJ/out" curso.vscode.MainInventario
```

En ambos casos:

- **Compilación.** `javac` no muestra ningún mensaje si todo va bien.
- **Archivos generados.** Los `.class` se generan en la carpeta `out/` del ejercicio, excluida en el `.gitignore`.
- **Desde VS Code.** Basta con abrir `MainInventario.java` y pulsar `Ctrl + F5` o hacer clic en **Run** sobre el método `main`.

Salida esperada:

```text
Listado de productos:
  Producto [id=1, nombre=Teclado, precio=25.50, cantidad=10]
  Producto [id=2, nombre=Ratón, precio=12.90, cantidad=25]
  Producto [id=3, nombre=Monitor, precio=189.00, cantidad=4]
  Producto [id=4, nombre=Portátil, precio=899.99, cantidad=2]

Producto 3: Monitor, 4 uds. a 189.00 EUR
Producto 99: no existe

Actualizar stock del producto 3 a 12 unidades: correcto
Actualizar stock del producto 99: no existe

Listado de productos:
  Producto [id=1, nombre=Teclado, precio=25.50, cantidad=10]
  Producto [id=2, nombre=Ratón, precio=12.90, cantidad=25]
  Producto [id=3, nombre=Monitor, precio=189.00, cantidad=12]
  Producto [id=4, nombre=Portátil, precio=899.99, cantidad=2]

Productos con menos de 5 unidades:
  Portátil (2 uds.)
Valor total del inventario: 4645.48 EUR
```

Los precios usan `BigDecimal`, por lo que se muestran siempre con punto decimal, sea cual sea la configuración regional del sistema. Si en una consola de Windows los acentos se ven mal, el problema es la codificación de la consola, no el programa. En la Terminal integrada de VS Code se muestran correctamente.

## 10. Comprobaciones finales

Comprueba que eres capaz de hacer lo siguiente sin recurrir al ratón:

- [ ] Abrir cualquier clase con `Ctrl + P` y saltar a un método con `Ctrl + Shift + O` o `Ctrl + T`.
- [ ] Ir a la definición de un método con `F12`, verla sin salir del archivo con `Alt + F12` y volver con `Alt + ←`.
- [ ] Cambiar entre archivos abiertos con `Ctrl + Tab`.
- [ ] Buscar y reemplazar en un archivo (`Ctrl + F`, `Ctrl + H`) y en una carpeta (`Ctrl + Shift + F`, **Find in Folder...**).
- [ ] Encontrar todas las referencias a un método con `Shift + Alt + F12`.
- [ ] Duplicar, mover y eliminar líneas.
- [ ] Editar varias líneas a la vez con `Ctrl + D`, `Ctrl + Shift + L` y `Ctrl + Alt + ↓`.
- [ ] Comentar, descomentar, indentar y desindentar un bloque, y saber dónde consultar el atajo de comentar en tu teclado.
- [ ] Renombrar un método con `F2`, corregir un error con `Ctrl + .` y organizar imports con `Shift + Alt + O`.
- [ ] Formatear un archivo con `Shift + Alt + F`.
- [ ] Ejecutar con `Ctrl + F5`, depurar con `F5` y moverte con `F9`, `F10`, `F11` y `Shift + F11`.
- [ ] Identificar qué atajos dependen de la extensión de Java y cuáles del teclado.
- [ ] Encontrar en la tabla del apartado 8 el equivalente de tus atajos habituales de Eclipse o IntelliJ IDEA.
- [ ] Dejar el código exactamente como estaba al empezar.
