# Ejercicio 1.3.1 — Temas y estilos

Módulo 1. Introducción a VS Code · Tema 1.3. Configuración avanzada de VS Code · Apartado 1.3.1. Temas y estilos

## Antes de empezar: actualizar el repositorio

El material del tema 1.3 se ha añadido al repositorio después de que lo clonaras. Antes de trabajar, descárgalo. **No hace falta volver a clonar.**

1. Abre una terminal en la **raíz de tu repositorio local** del curso, la carpeta que contiene `modulo-01-vscode/`. En VS Code, la Terminal integrada ya se abre ahí: **Terminal > New Terminal**.
2. Comprueba que no tienes cambios pendientes:

   ```bash
   git status
   ```

   El resultado debe indicar que no hay nada que confirmar (*nothing to commit, working tree clean*). Si aparecen archivos modificados por las prácticas anteriores, deshaz esos cambios antes de seguir, por ejemplo con `File: Revert File` en VS Code. Si no sabes cómo resolverlo, consúltalo antes de continuar.
3. Descarga las novedades:

   ```bash
   git pull --ff-only
   ```

   El comando es idéntico en PowerShell y en Bash. Al terminar aparece la carpeta `modulo-01-vscode/tema-1.3-configuracion-avanzada/`.

   `--ff-only` solo actualiza si tu copia no tiene cambios propios que mezclar. Si el comando se niega a actualizar, no lo fuerces y consúltalo. Git se trabaja en detalle en el módulo 2.

## 1. Introducción

En el tema 1.1 se ajustaron las preferencias básicas del editor: tamaño de fuente, tabulación, Word Wrap, Minimap, espacios y guardado automático. Este apartado se centra en la **personalización visual** y profundiza en las opciones que más influyen en la comodidad y la accesibilidad.

Conviene distinguir dos tipos de ajustes:

| Tipo | Qué cambia | Ejemplos | ¿Afecta al código o al equipo? |
|---|---|---|---|
| **Personalización visual** | Cómo **ves** el editor | Tema de color, iconos, fuente, espaciado, zoom, Minimap | No. Es una preferencia personal. |
| **Configuración funcional** | Cómo **se comporta** el editor y cómo **se guarda** el código | Tabulación, formato al guardar, codificación, fin de línea | Sí. Puede cambiar los archivos y conviene acordarla con el equipo. |

Los ajustes visuales se guardan normalmente a nivel de **usuario** y no se comparten. Un compañero puede trabajar con tema claro y otro con tema oscuro sobre el mismo proyecto sin ningún problema. La configuración compartida se trata en el apartado 1.3.3.

La apariencia también es una cuestión de **productividad y accesibilidad**:

- un tamaño de fuente e interlineado adecuados reducen el cansancio visual en sesiones largas;
- los temas de alto contraste ayudan a personas con baja visión;
- las ayudas visuales (colores de llaves, *Sticky Scroll*, *Breadcrumbs*) facilitan orientarse en clases Java largas.

### Archivos que se usan

Este apartado no tiene código propio. Las prácticas usan las clases del ejercicio 1.2.1, por ejemplo:

- `modulo-01-vscode/tema-1.2-shortcuts/apartado-1.2.1-atajos-java/src/curso/vscode/MainInventario.java`
- `modulo-01-vscode/tema-1.2-shortcuts/apartado-1.2.1-atajos-java/src/curso/vscode/Inventario.java`

No se modifican: solo se observan con distintas configuraciones visuales.

## 2. Temas de color

Un **tema de color** (*Color Theme*) define los colores de toda la interfaz (barras laterales, pestañas, panel, Status Bar) y los del código en el editor (palabras clave, cadenas, comentarios, nombres de clases…).

### Cambiar el tema

Usa cualquiera de estas opciones:

- **File > Preferences > Theme > Color Theme**.
- `Ctrl + K` y, a continuación, `Ctrl + T`.
- Command Palette > `Preferences: Color Theme`.

Se abre una lista. Al moverte con las flechas, el tema se **previsualiza** al instante. `Enter` lo aplica y `Esc` vuelve al tema anterior sin cambiar nada.

### Temas integrados

VS Code incluye estos temas sin instalar nada. Están agrupados en la lista como claros, oscuros y de alto contraste:

| Tipo | Temas incluidos |
|---|---|
| Oscuros | Dark 2026 (predeterminado), Dark Modern, Dark+, Dark (Visual Studio), Abyss, Kimbie Dark, Monokai, Monokai Dimmed, Red, Solarized Dark, Tomorrow Night Blue |
| Claros | Light 2026 (predeterminado claro), Light Modern, Light+, Light (Visual Studio), Quiet Light, Solarized Light |
| Alto contraste | Dark High Contrast, Light High Contrast |

En `settings.json` el tema se identifica por su **identificador**, que no siempre coincide con el nombre de la lista:

- **Dark High Contrast** se guarda como `"Default High Contrast"`.
- **Dark (Visual Studio)** se guarda como `"Visual Studio Dark"`.
- En versiones anteriores de VS Code, los temas *Modern* y *+* se llamaban `"Default Dark Modern"`, `"Default Dark+"`, etc. Las versiones actuales los convierten automáticamente a `"Dark Modern"`, `"Dark+"`, etc.

### Buscar más temas

- En la lista de temas, la opción **Browse Additional Color Themes...** muestra temas del Marketplace con vista previa.
- En la vista **Extensions**, busca `@category:themes`.

**En este curso no se instalan temas**: los integrados son suficientes para la práctica.

### Seguir el modo claro u oscuro del sistema

Con `window.autoDetectColorScheme` activado, VS Code alterna automáticamente entre dos temas según el modo claro u oscuro de Windows o de tu escritorio Linux. Los temas se eligen en `workbench.preferredDarkColorTheme` y `workbench.preferredLightColorTheme`.

### Tema de interfaz frente a colores del editor

El tema colorea las dos cosas, pero se pueden retocar por separado sin cambiar de tema:

| Ajuste | Afecta a |
|---|---|
| `workbench.colorCustomizations` | Colores de la **interfaz**: barras, pestañas, Status Bar… |
| `editor.tokenColorCustomizations` | Colores del **código**: comentarios, cadenas, palabras clave… |
| `editor.semanticHighlighting.enabled` | Coloreado **semántico**. La extensión de Java distingue, por ejemplo, campos de variables locales o métodos estáticos. Por defecto lo decide el tema. |

Estos retoques son avanzados y no se usan en la práctica. Basta con saber que existen.

### Restaurar el tema

Vuelve a abrir **Color Theme** y elige el tema que tenías. Para volver al predeterminado, busca `workbench.colorTheme` en Settings y usa **Reset Setting** en el engranaje.

## 3. Iconos

VS Code tiene dos tipos de temas de iconos:

| | File Icon Theme | Product Icon Theme |
|---|---|---|
| Qué cambia | Iconos de **archivos y carpetas** en el Explorer, las pestañas y Quick Open | Iconos de la **propia interfaz**: Activity Bar, botones de vistas, acciones… |
| Menú | **File > Preferences > Theme > File Icon Theme** | **File > Preferences > Theme > Product Icon Theme** |
| Command Palette | `Preferences: File Icon Theme` | `Preferences: Product Icon Theme` |
| Integrados | Seti (predeterminado), Minimal, VS Code Modern Icons y *None* | Default |
| Propiedad | `workbench.iconTheme` (`"vs-seti"`, `"vs-minimal"`, `"vscode-modern-icons"`) | `workbench.productIconTheme` (`"Default"`) |

Con un buen tema de iconos de archivos se distinguen de un vistazo los `.java`, los `.md` y los `.json` en el Explorer. El tema de iconos de producto es puramente estético.

**Seleccionar y restaurar:** se hace igual que con el tema de color. La lista previsualiza, `Enter` aplica y `Esc` cancela. Para volver al predeterminado, elige **Seti (Visual Studio Code)** o usa **Reset Setting** sobre `workbench.iconTheme`.

## 4. Apariencia del editor

| Opción | Propiedad | Valor por defecto | Acceso rápido |
|---|---|---|---|
| Tamaño de fuente | `editor.fontSize` | `14` | Settings |
| Familia de fuente | `editor.fontFamily` | Windows: `Consolas, 'Courier New', monospace` | Settings |
| Ligaduras tipográficas | `editor.fontLigatures` | `false` | Settings |
| Espaciado entre líneas | `editor.lineHeight` | `0` (automático) | Settings |
| Minimap | `editor.minimap.enabled` | `true` | **View > Appearance > Minimap** |
| Breadcrumbs | `breadcrumbs.enabled` | `true` | **View > Appearance > Breadcrumbs** |
| Word Wrap | `editor.wordWrap` | `"off"` | **View > Word Wrap** (`Alt + Z`) |
| Espacios en blanco | `editor.renderWhitespace` | `"selection"` | **View > Appearance > Render Whitespace** |
| Colores de llaves | `editor.bracketPairColorization.enabled` | `true` | Settings |
| Sticky Scroll | `editor.stickyScroll.enabled` | `true` | **View > Appearance > Sticky Scroll** |
| Zoom de la ventana | `window.zoomLevel` | `0` | **View > Appearance > Zoom In / Zoom Out / Reset Zoom** |

Word Wrap, Minimap y Render Whitespace se explicaron en el ejercicio 1.1.3. Aquí se tratan las opciones nuevas.

- **Familia de fuente.** Es una lista separada por comas: VS Code usa la primera fuente instalada y, si no existe, pasa a la siguiente. Al trabajar con WSL se usan las fuentes de **Windows**, porque la interfaz se ejecuta en Windows. Usa siempre fuentes **monoespaciadas**, en las que todos los caracteres ocupan lo mismo, para que la indentación se alinee.
- **Ligaduras tipográficas.** Algunas fuentes de programación, como *Cascadia Code* o *Fira Code*, dibujan combinaciones como `->`, `==`, `!=` o `>=` como un solo símbolo. Es solo una representación visual: el archivo sigue conteniendo los dos caracteres. Requiere `editor.fontLigatures: true` y una fuente que las tenga. Con *Consolas*, la fuente por defecto en Windows, no se aprecian cambios.
- **Espaciado entre líneas.** `0` calcula la altura automáticamente. Un número mayor que 8 se interpreta como **píxeles** (por ejemplo, `22`). Un número entre 1 y 8 se interpreta como **multiplicador** del tamaño de fuente (por ejemplo, `1.5`).
- **Breadcrumbs.** Barra situada sobre el editor que muestra la ruta del archivo y la posición del cursor dentro del código, por ejemplo `curso > vscode > Inventario.java > Inventario > calcularValorTotal()`. Cada elemento es un menú desplegable para saltar a otra carpeta, clase o método.
- **Colores de llaves.** Cada nivel de `(`, `[` y `{` se colorea de forma distinta, lo que permite ver qué llave cierra cada bloque. Con `editor.guides.bracketPairs` se dibujan además líneas verticales entre las parejas.
- **Sticky Scroll.** Al desplazarte por un método largo, su declaración, y la de la clase, quedan "pegadas" en la parte superior del editor. Así siempre se sabe dentro de qué método se está.
- **Zoom de la ventana.** Amplía **toda** la interfaz, no solo el código: menús, barras laterales, panel. Es distinto de `editor.fontSize`. Cada paso de zoom equivale aproximadamente a un 20 % más o menos. `Ctrl + =` y `Ctrl + -` dependen del teclado; el menú **View > Appearance** funciona siempre.

### Opciones más útiles en proyectos Java grandes

- **Sticky Scroll y Breadcrumbs.** Las clases Java suelen ser largas y con muchos métodos; estas dos opciones indican en todo momento la clase y el método en que se está.
- **Colores de llaves y guías.** Ayudan con bloques anidados: `if` dentro de `for` dentro de `try`, *lambdas*, clases anónimas.
- **Minimap.** Ofrece una visión rápida de la longitud de la clase y de dónde están los errores, que se marcan en color. En pantallas pequeñas suele compensar más ocultarlo.
- **Word Wrap.** Normalmente desactivado en Java, porque las líneas largas se corrigen con el formateador. Es útil en archivos `.md` o en mensajes largos.
- **Tamaño de fuente y espaciado.** Si se ve poco código a la vez, reducir el interlineado aporta más que reducir la fuente.

## 5. Configuración de apariencia mediante settings.json

Todo lo anterior se puede escribir directamente en el `settings.json` de **usuario**: Command Palette > `Preferences: Open User Settings (JSON)`. Ejemplo con los valores de VS Code 1.146:

```json
{
  "workbench.colorTheme": "Dark Modern",
  "editor.fontSize": 14,
  "editor.lineHeight": 22,
  "editor.minimap.enabled": false,
  "editor.wordWrap": "on",
  "editor.bracketPairColorization.enabled": true,
  "editor.stickyScroll.enabled": true
}
```

Otras propiedades de este apartado:

```json
{
  "workbench.iconTheme": "vs-minimal",
  "workbench.productIconTheme": "Default",
  "editor.fontFamily": "'Cascadia Code', Consolas, 'Courier New', monospace",
  "editor.fontLigatures": true,
  "editor.renderWhitespace": "boundary",
  "editor.guides.bracketPairs": "active",
  "breadcrumbs.enabled": true,
  "window.zoomLevel": 1
}
```

Precauciones:

- **No reemplaces tu archivo.** Si ya tiene contenido, añade solo las líneas que quieras, separadas por comas, dentro de las llaves existentes.
- **Errores de escritura.** VS Code valida el archivo mientras escribes. Una propiedad que no existe aparece subrayada con el aviso *Unknown Configuration Setting*, y un valor no válido también se marca. Pasa el ratón sobre la propiedad para ver su descripción y los valores admitidos.
- **Ámbito.** Las preferencias visuales van en **User**. No tiene sentido imponerlas al equipo en la configuración del proyecto.

## 6. Práctica guiada

Antes de empezar:

1. Abre el editor de Settings (**File > Preferences > Settings**), pestaña **User**, y escribe `@modified` en el buscador.
2. **Anota** las preferencias que aparecen. Son tus personalizaciones actuales, y al final solo debes restaurar lo que cambies en esta práctica.
3. Abre en el editor `MainInventario.java` e `Inventario.java` del ejercicio 1.2.1 (`Ctrl + P`).

### Actividad 1. Cambiar entre temas claros y oscuros

1. Abre **File > Preferences > Theme > Color Theme**.
2. Recorre la lista con las flechas sin pulsar `Enter`. Observa el editor y la interfaz con **Light Modern**, **Dark Modern**, **Solarized Light** y **Dark High Contrast**.
3. Elige **Light Modern** con `Enter`.
4. Vuelve a abrir la lista, elige **Dark Modern** y compara cómo se ven las palabras clave (`public`, `for`, `return`) y las cadenas.

**Resultado esperado:** la previsualización cambia al instante. Con `Enter` el tema queda aplicado y aparece en Settings como `workbench.colorTheme`.

**Deshacer:** al final de la práctica (actividad 6).

### Actividad 2. Modificar los iconos

1. Abre **File > Preferences > Theme > File Icon Theme**.
2. Elige **Minimal (Visual Studio Code)** y observa el Explorer: los iconos de `.java`, `.md` y las carpetas son mucho más simples.
3. Prueba **VS Code Modern Icons** y después **None**.
4. Abre **File > Preferences > Theme > Product Icon Theme** y observa que solo hay **Default**: los iconos de la interfaz son los mismos.

**Resultado esperado:** solo cambian los iconos de archivos y carpetas. Los de la Activity Bar se mantienen.

**Deshacer:** vuelve a **Seti (Visual Studio Code)**, o espera a la actividad 6.

### Actividad 3. Ajustar la fuente, el tamaño y el espaciado

1. En Settings (pestaña **User**), busca `editor.fontSize` y pon `16`.
2. Busca `editor.lineHeight` y pon `24`. Después prueba `1.2` y fíjate en la diferencia entre píxeles y multiplicador.
3. Busca `editor.fontFamily` y añade una fuente al principio de la lista, por ejemplo `'Cascadia Code', ` delante del valor actual. Si esa fuente no está instalada, no verás cambios: VS Code usa la siguiente de la lista.
4. Busca `editor.fontLigatures` y actívala. En `MainInventario.java`, mira la *lambda* `producto -> ...` del método `mostrarProducto`.
5. Usa **View > Appearance > Zoom In** dos veces y luego **Reset Zoom**.

**Resultado esperado:**

- La fuente y el interlineado cambian solo en el editor.
- Con una fuente con ligaduras, `->` se ve como una única flecha.
- El zoom amplía toda la interfaz y **Reset Zoom** la devuelve a su tamaño.

**Deshacer:** en la actividad 6.

### Actividad 4. Configurar Minimap y Breadcrumbs

1. En `Inventario.java`, observa el Minimap a la derecha y haz clic en una zona para saltar a ella.
2. Desactívalo con **View > Appearance > Minimap**.
3. Observa la barra de Breadcrumbs sobre el editor. Sitúa el cursor dentro de `calcularValorTotal` y comprueba que la barra termina en ese método.
4. Haz clic en el último elemento de los Breadcrumbs y elige `listarProductos` en la lista desplegable.
5. Desactiva y vuelve a activar los Breadcrumbs con **View > Appearance > Breadcrumbs**.
6. Con el cursor al final de `Inventario.java`, desplázate hacia arriba poco a poco y observa cómo *Sticky Scroll* mantiene visible la línea `public class Inventario {` y la del método en el que estás.

**Resultado esperado:** el Minimap desaparece y la barra de Breadcrumbs permite saltar entre métodos.

**Deshacer:** vuelve a activar el Minimap con el mismo menú.

### Actividad 5. Activar Word Wrap y la visualización de espacios

1. En `MainInventario.java`, localiza la línea más larga de `main`, la que imprime "Actualizar stock del producto 3 a 12 unidades". Reduce el ancho de la ventana o abre la barra lateral hasta que la línea no quepa.
2. Activa **View > Word Wrap** (`Alt + Z`).
3. Activa **View > Appearance > Render Whitespace**.
4. En Settings, busca `editor.renderWhitespace` y prueba los valores `boundary` y `trailing`.
5. Busca `editor.guides.bracketPairs` y elige `active`. Sitúa el cursor dentro del bucle `for` de `main` y observa las guías de color.

**Resultado esperado:**

- La línea larga se parte visualmente sin cambiar el archivo.
- Los espacios se muestran como puntos.
- Aparece una línea vertical de color que une la llave de apertura y la de cierre del bloque actual.

**Deshacer:** desactiva Word Wrap y Render Whitespace con los mismos menús, o en la actividad 6.

### Actividad 6. Restaurar las preferencias originales

1. En Settings (pestaña **User**), escribe `@modified` en el buscador.
2. Compara la lista con la que anotaste al empezar.
3. Para cada preferencia nueva o cambiada en esta práctica, haz clic en el engranaje y elige **Reset Setting**. Probablemente estarán `workbench.colorTheme`, `workbench.iconTheme`, `editor.fontSize`, `editor.lineHeight`, `editor.fontFamily`, `editor.fontLigatures`, `editor.minimap.enabled`, `editor.wordWrap`, `editor.renderWhitespace` y `editor.guides.bracketPairs`.
4. Si tenías un tema distinto del predeterminado, vuelve a elegirlo en **Color Theme**.
5. Comprueba que **View > Appearance > Reset Zoom** deja el zoom normal.

**Resultado esperado:** `@modified` muestra exactamente las preferencias que tenías al empezar. Las clases del ejercicio 1.2.1 no tienen cambios: ninguna pestaña muestra el círculo de "sin guardar".

## 7. Comparación con Eclipse e IntelliJ IDEA

| Aspecto | Eclipse | IntelliJ IDEA | VS Code |
|---|---|---|---|
| Tema de interfaz | **Window > Preferences > General > Appearance** (*Theme*: Light, Dark, Classic…) | **Settings > Appearance & Behavior > Appearance** (*Theme*) | **File > Preferences > Theme > Color Theme**. Un único tema para interfaz y código. |
| Colores del código | **General > Appearance > Colors and Fonts** y **Java > Editor > Syntax Coloring** | **Settings > Editor > Color Scheme** (independiente del tema) | Incluidos en el tema. Se retocan con `editor.tokenColorCustomizations`. |
| Temas adicionales | Eclipse Marketplace (por ejemplo, *Darkest Dark*) | Plugins de tema | Extensiones de tema (**Browse Additional Color Themes...**) |
| Iconos | Fijos (algunos plugins los cambian) | **Appearance** y plugins de iconos | **File Icon Theme** y **Product Icon Theme** |
| Fuente del editor | **Colors and Fonts > Basic > Text Font** | **Editor > Font** (incluye ligaduras e interlineado) | `editor.fontFamily`, `editor.fontSize`, `editor.lineHeight`, `editor.fontLigatures` |
| Mapa del archivo | No incluido (plugins) | No incluido de serie (plugins) | **Minimap** integrado |
| Ruta y posición en el código | **Breadcrumb** (**Toggle Breadcrumb**) | **Breadcrumbs** en la parte inferior o superior | **Breadcrumbs** sobre el editor |
| Cabecera fija del método | **Sticky Scrolling** (desde la versión 2024-06) | **Sticky Lines** | **Sticky Scroll** |
| Colores de llaves | Plugins | Plugins (*Rainbow Brackets*) | Integrado (`editor.bracketPairColorization.enabled`) |
| Ajuste de línea | **Toggle Word Wrap** (`Alt + Shift + Y`) | **View > Active Editor > Soft-Wrap** | **View > Word Wrap** (`Alt + Z`) |
| Mostrar espacios | **Show Whitespace Characters** | **Editor > General > Appearance > Show whitespaces** | **View > Appearance > Render Whitespace** |
| Zoom de la interfaz | Zoom del editor (`Ctrl + =` / `Ctrl + -`) | **View > Appearance > Zoom IDE** (versiones recientes) | **View > Appearance > Zoom In / Zoom Out** |
| Dónde se guarda | Preferencias del *workspace* de Eclipse | Configuración del IDE (exportable) | `settings.json` de usuario, legible y editable |

## 8. Comprobaciones finales

Comprueba que eres capaz de:

- [ ] Actualizar tu repositorio local con `git pull --ff-only` desde su raíz, tras comprobar con `git status` que no hay cambios pendientes.
- [ ] Explicar la diferencia entre personalización visual y configuración funcional, y por qué la primera no se comparte con el equipo.
- [ ] Previsualizar, aplicar y restaurar un tema de color, e identificar temas claros, oscuros y de alto contraste.
- [ ] Distinguir entre File Icon Theme y Product Icon Theme y cambiar cada uno.
- [ ] Ajustar el tamaño y la familia de fuente, el interlineado y las ligaduras, y explicar la diferencia entre `editor.fontSize` y el zoom de la ventana.
- [ ] Activar y desactivar Minimap, Breadcrumbs, Sticky Scroll, Word Wrap y la visualización de espacios desde el menú **View**.
- [ ] Usar los Breadcrumbs y Sticky Scroll para orientarte en una clase Java.
- [ ] Escribir estas preferencias en el `settings.json` de usuario y detectar una propiedad mal escrita.
- [ ] Restaurar todas las preferencias cambiadas con `@modified` y **Reset Setting**.
