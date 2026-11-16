# Ejercicio 1.1.2 — Manejo de archivos y proyectos

Módulo 1. Introducción a VS Code · Tema 1.1. Conceptos básicos · Apartado 1.1.2. Manejo de archivos y proyectos

## 1. Introducción

### Cómo organiza VS Code el trabajo

VS Code trabaja con **carpetas**, no con proyectos. Al ejecutar `code .` o usar **File > Open Folder...**, la carpeta abierta pasa a ser el *workspace*: todo lo que contiene aparece en el Explorer y es lo que abarcan la búsqueda, los ajustes de carpeta y las extensiones.

El concepto de **proyecto Java** lo aporta la extensión de Java (Extension Pack for Java), que analiza el contenido de la carpeta:

- Si encuentra un `pom.xml` (Maven) o un `build.gradle` (Gradle), importa el proyecto a partir de ese archivo.
- Si no hay herramienta de construcción, como en este curso hasta ahora, trata la carpeta como una **carpeta sin gestionar** (*unmanaged folder*). Deduce las carpetas de código fuente a partir de la línea `package` de cada clase que se abre.

Los **paquetes Java** siguen funcionando igual que en cualquier IDE: cada parte del nombre del paquete es una carpeta. El paquete `curso.vscode` corresponde a la ruta `curso/vscode/` dentro de la carpeta de código fuente `src/`.

### Diferencias con Eclipse e IntelliJ IDEA

| Concepto | Eclipse | IntelliJ IDEA | VS Code |
|---|---|---|---|
| Unidad de trabajo | *Workspace* con varios proyectos importados | Proyecto (carpeta `.idea`) con uno o varios módulos | Carpeta abierta (o varias, en un *multi-root workspace*) |
| Definición del proyecto | Archivos `.project` y `.classpath` | Carpeta `.idea` y archivos `.iml` | `pom.xml` o `build.gradle` si existen. Si no, la extensión de Java deduce las carpetas de código fuente. |
| Vista de paquetes | *Package Explorer* | Ventana *Project*, vista *Packages* | Explorer (vista de archivos) y sección **Java Projects** (vista de paquetes) |
| Creación de clases | Asistente *New Class* | *New > Java Class* | Archivo nuevo `.java` en el Explorer o menú **New Java File** de la extensión |

El Explorer de VS Code muestra **archivos y carpetas reales**, no una vista lógica. Lo que se ve en el Explorer es exactamente lo que hay en disco.

## 2. Objetivos

Al terminar este ejercicio sabrás utilizar VS Code para:

- Crear, renombrar, copiar, mover y eliminar archivos y carpetas desde el Explorer.
- Abrir archivos en pestañas de vista previa y en pestañas fijas, dividir el editor y comparar archivos.
- Cerrar archivos y volver a abrir los que acabas de cerrar.
- Localizar archivos, texto y símbolos con Quick Open, Search, Go to Symbol y Go to Symbol in Workspace.
- Relacionar paquetes Java con carpetas y crear clases en el paquete correcto.
- Navegar entre clases con Go to Definition y Find All References.
- Renombrar con seguridad mediante **Rename Symbol** (`F2`) y entender en qué se diferencia de renombrar un archivo.

## 3. Estructura del ejercicio

### Material preparado

```text
modulo-01-vscode/
└── tema-1.1-conceptos-basicos/
    └── apartado-1.1.2-manejo-archivos/
        ├── README.md
        └── src/
            └── curso/
                └── vscode/
                    ├── Alumno.java
                    ├── GestionAlumnos.java
                    └── MainGestionAlumnos.java
```

| Clase | Qué contiene |
|---|---|
| `Alumno` | Datos de un alumno: identificador, nombre y correo. Constructor, getters y `toString()`. |
| `GestionAlumnos` | Lista de alumnos en memoria con los métodos `registrarAlumno`, `buscarPorId`, `listarAlumnos` y `contarAlumnos`. |
| `MainGestionAlumnos` | Método `main` que registra tres alumnos ficticios, muestra el listado, busca uno por identificador y muestra el total. |

Las tres clases empiezan con `package curso.vscode;`. Por eso están en `src/curso/vscode/`:

- `src/` es la carpeta de código fuente.
- `curso/vscode/` reproduce el nombre del paquete, carpeta a carpeta.

Si la línea `package` y la ruta no coinciden, el compilador y la extensión de Java muestran un error.

### Lo que harás tú

El código ya está preparado y compila. En este ejercicio no hay que programar nada nuevo: todas las operaciones de la práctica son temporales y se deshacen al final. Al terminar, la carpeta debe quedar exactamente como estaba.

### Antes de empezar

1. Abre VS Code desde la raíz del repositorio del curso, como en el ejercicio 1.1.1:

   ```bash
   cd <ruta-del-repositorio>
   code .
   ```

2. En el Explorer, despliega `modulo-01-vscode` > `tema-1.1-conceptos-basicos` > `apartado-1.1.2-manejo-archivos`.
3. Comprueba que el guardado automático está desactivado: en el menú **File**, la opción **Auto Save** no debe estar marcada. Así podrás deshacer cualquier cambio antes de guardarlo.

## 4. Crear archivos y carpetas desde Explorer

Todas las operaciones de este apartado se hacen desde el Explorer (menú **View > Explorer** o `Ctrl + Shift + E`).

### Botones de la cabecera del Explorer

Al pasar el ratón sobre el nombre de la carpeta abierta aparecen cuatro iconos:

- **New File...**: crea un archivo nuevo.
- **New Folder...**: crea una carpeta nueva.
- **Refresh Explorer**: vuelve a leer el disco.
- **Collapse Folders in Explorer**: pliega todas las carpetas.

El archivo o la carpeta se crea **dentro de la carpeta seleccionada** en ese momento. Si hay un archivo seleccionado, se crea en la misma carpeta que ese archivo.

### Menú contextual

Al hacer clic derecho sobre un archivo o una carpeta del Explorer aparece el menú contextual. Es la forma más fiable de trabajar, porque no depende de atajos de teclado. Las opciones principales son:

| Opción | Qué hace |
|---|---|
| **New File...** / **New Folder...** | Crea un archivo o una carpeta dentro de la carpeta seleccionada |
| **New Java File** | Submenú de la extensión de Java: *Class...*, *Interface...*, *Enum...*, *Record...*, etc. Solo aparece sobre carpetas. |
| **Open to the Side** | Abre el archivo en una columna nueva del editor |
| **Find in Folder...** | Busca texto solo dentro de esa carpeta |
| **Select for Compare** / **Compare with Selected** | Compara dos archivos (ver apartado 5) |
| **Cut** / **Copy** / **Paste** | Mueve o copia archivos y carpetas |
| **Copy Path** / **Copy Relative Path** | Copia la ruta completa o la ruta relativa a la raíz |
| **Rename...** | Cambia el nombre (también con `F2`) |
| **Delete** | Elimina el archivo o la carpeta (también con la tecla `Supr`) |
| **Run Java** / **Debug Java** | Ejecuta o depura la clase. Solo aparece en archivos `.java`. |

### Operaciones básicas

- **New File**: selecciona la carpeta de destino, pulsa **New File...** (icono o menú contextual), escribe el nombre con su extensión y pulsa `Enter`. Si escribes una ruta como `docs/notas.txt`, VS Code crea también las carpetas intermedias.
- **New Folder**: igual que el anterior, con **New Folder...**.
- **Rename**: selecciona el elemento y pulsa `F2`, o usa **Rename...** en el menú contextual. Escribe el nuevo nombre y pulsa `Enter`. Con `Esc` se cancela.
- **Delete**: selecciona el elemento y pulsa `Supr`, o usa **Delete** en el menú contextual. VS Code pide confirmación. Al trabajar en WSL, el elemento puede borrarse de forma permanente, sin pasar por la Papelera de Windows. Lee el mensaje de confirmación antes de aceptar.
- **Copy y Paste**: selecciona el elemento, usa **Copy** (`Ctrl + C`), selecciona la carpeta de destino y usa **Paste** (`Ctrl + V`). Si pegas en la misma carpeta, VS Code añade `copy` al nombre (por ejemplo, `notas copy.txt`).
- **Cut y Paste**: igual, pero con **Cut** (`Ctrl + X`). El elemento se mueve en lugar de copiarse.
- **Drag and Drop**: arrastra el elemento con el ratón y suéltalo sobre la carpeta de destino. Por defecto, VS Code pide confirmación antes de mover. Si lo sueltas mientras mantienes pulsada la tecla `Ctrl`, se copia en lugar de moverse.

### Advertencias al trabajar con clases Java

Un archivo `.java` no es un archivo cualquiera. Su nombre y su ubicación forman parte del código:

- **El nombre del archivo debe coincidir con el de la clase pública.** Si renombras `Alumno.java` a `Estudiante.java` desde el Explorer, la clase sigue llamándose `Alumno` y el código deja de compilar, salvo que la extensión de Java lo corrija.
- **La carpeta debe coincidir con el paquete.** Si mueves `Alumno.java` a otra carpeta, la línea `package curso.vscode;` deja de corresponder a su ubicación.
- **La extensión de Java intenta ayudar, pero hay que revisar el resultado.** Cuando renombras o mueves un `.java` desde el Explorer, la extensión de Java intenta actualizar el nombre de la clase, la línea `package` y las referencias. También puede renombrar el archivo al guardar si no coincide con la clase pública. El comportamiento exacto depende de la versión. Comprueba siempre el resultado en el editor y en el panel **Problems**.
- **Copiar y pegar un `.java` en el mismo paquete crea un conflicto.** `Alumno copy.java` contendría otra clase `Alumno` en el mismo paquete.
- **Para cambiar el nombre de una clase, usa Rename Symbol (`F2`) desde el editor.** Se explica en el apartado 7.

En la práctica guiada, las operaciones de crear, renombrar, mover y eliminar se hacen con un archivo de texto, no con las clases Java.

## 5. Abrir y gestionar archivos

### Abrir archivos desde Explorer

- **Un clic** abre el archivo en una **pestaña de vista previa**.
- **Doble clic** abre el archivo en una **pestaña fija**.
- **Open to the Side** (menú contextual) lo abre en una columna nueva, junto al editor actual.

### Pestañas de vista previa y pestañas fijas

- **Vista previa**: el nombre de la pestaña aparece en *cursiva*. Si abres otro archivo con un clic, ocupa esta misma pestaña. Sirve para echar un vistazo rápido sin acumular pestañas.
- **Fija**: el nombre aparece en letra normal y la pestaña permanece abierta. Una pestaña de vista previa pasa a ser fija en cuanto haces doble clic en ella, haces doble clic en el archivo del Explorer o empiezas a editar el archivo.
- **Anclada** (*Pin*): es una opción distinta. Con clic derecho en la pestaña > **Pin**, la pestaña se desplaza a la izquierda y no se cierra con **Close All** ni **Close Others**.

### Abrir varios archivos

Abre con doble clic `Alumno.java`, `GestionAlumnos.java` y `MainGestionAlumnos.java`. Las tres pestañas quedan fijas, una junto a otra.

- Para pasar de una pestaña a otra, haz clic en ella o usa `Ctrl + Tab`. Este atajo muestra la lista de archivos abiertos mientras mantienes `Ctrl` pulsado.
- En la sección **Open Editors** del Explorer aparece la lista de todos los archivos abiertos. Si no la ves, actívala desde el menú **...** de la cabecera del Explorer.

### Dividir el editor

- Con un archivo abierto, usa el menú **View > Editor Layout > Split Right**, o el icono **Split Editor Right** de la esquina superior derecha de las pestañas.
- Arrastra una pestaña hacia el borde derecho o inferior del editor para crear una columna o una fila con ese archivo.
- Ejemplo: deja `MainGestionAlumnos.java` a la izquierda y `GestionAlumnos.java` a la derecha. Así ves a la vez la llamada a `buscarPorId` y su implementación.

### Comparar archivos

VS Code compara dos archivos en un editor de diferencias, con los cambios resaltados:

1. En el Explorer, haz clic derecho sobre el primer archivo y elige **Select for Compare**.
2. Haz clic derecho sobre el segundo archivo y elige **Compare with Selected**.

Otras comparaciones útiles, desde la Command Palette (**View > Command Palette...**):

- `File: Compare Active File With...`: compara el archivo activo con otro que eliges de una lista.
- `File: Compare Active File with Saved`: muestra los cambios aún no guardados del archivo activo.

Para descartar todos los cambios no guardados de un archivo, ejecuta `File: Revert File` desde la Command Palette. El archivo vuelve a su última versión guardada.

### Cerrar y reabrir archivos

- **Cerrar una pestaña**: haz clic en la `x` de la pestaña, usa **File > Close Editor** o pulsa `Ctrl + W`.
- **Cerrar varias**: clic derecho en una pestaña > **Close Others**, **Close to the Right** o **Close All**.
- **Reabrir la última pestaña cerrada**: ejecuta `View: Reopen Closed Editor` desde la Command Palette o pulsa `Ctrl + Shift + T`.
- **Archivos recientes**: menú **File > Open Recent**.

Si una pestaña muestra un **círculo** en lugar de la `x`, el archivo tiene cambios sin guardar. Al cerrarla, VS Code pregunta si quieres guardar.

## 6. Buscar y localizar archivos

| Acción | Atajo | Alternativa desde menú |
|---|---|---|
| Buscar archivos por nombre (*Quick Open*) | `Ctrl + P` | **Go > Go to File...** |
| Buscar texto en todos los archivos | `Ctrl + Shift + F` | **View > Search** o **Edit > Find in Files** |
| Símbolos del archivo actual | `Ctrl + Shift + O` | **Go > Go to Symbol in Editor...** |
| Símbolos de todo el *workspace* | `Ctrl + T` | **Go > Go to Symbol in Workspace...** |
| Buscar y reemplazar en el archivo actual | `Ctrl + H` | **Edit > Replace** |
| Buscar y reemplazar en varios archivos | `Ctrl + Shift + H` | **Edit > Replace in Files** |

### Quick Open: `Ctrl + P`

Escribe parte del nombre de un archivo. Por ejemplo, con `gestion` aparecen `GestionAlumnos.java` y `MainGestionAlumnos.java`. Bajo cada resultado se muestra su ruta, útil cuando hay archivos con nombres parecidos en distintas prácticas.

La misma caja admite prefijos:

- `>`: ejecuta comandos (equivale a la Command Palette).
- `@`: muestra los símbolos del archivo actual.
- `#`: busca símbolos en el *workspace*.
- `:`: salta a un número de línea.

### Buscar texto: `Ctrl + Shift + F`

La vista **Search** busca en todos los archivos de la carpeta abierta. Como está abierta la raíz del repositorio, conviene **acotar la búsqueda**:

- Despliega los detalles de búsqueda con el icono **...** (*Toggle Search Details*) y escribe la carpeta en **files to include**. Por ejemplo: `modulo-01-vscode/tema-1.1-conceptos-basicos/apartado-1.1.2-manejo-archivos`.
- O bien haz clic derecho sobre la carpeta del ejercicio en el Explorer y elige **Find in Folder...**. El campo *files to include* se rellena solo.

Los iconos del campo de búsqueda activan opciones adicionales:

- **Match Case**: distingue mayúsculas de minúsculas.
- **Match Whole Word**: busca solo palabras completas.
- **Use Regular Expression**: interpreta el texto como expresión regular.

### Símbolos del archivo: `Ctrl + Shift + O`

Con `GestionAlumnos.java` abierto, muestra la lista de campos y métodos de la clase: `alumnos`, `registrarAlumno`, `buscarPorId`, etc. Al elegir uno, el cursor salta a él. Si escribes `:` después de `@`, los símbolos se agrupan por tipo.

La sección **Outline** del Explorer muestra la misma información de forma permanente.

### Símbolos del workspace: `Ctrl + T`

Busca clases y métodos por nombre en todo el *workspace*, sin necesidad de saber en qué archivo están. Por ejemplo, `buscarPorId` o `Alumno`.

> **Requiere el soporte de Java.** Esta búsqueda la resuelve la extensión de Java. Si la Status Bar indica que la extensión aún se está cargando, espera a que termine. Si no aparece una clase, abre antes cualquier archivo de su práctica: así la extensión incorpora esa carpeta `src/` al proyecto.

### Buscar y reemplazar

- **En el archivo actual**: `Ctrl + H` (o **Edit > Replace**) abre la caja de búsqueda con un segundo campo para el texto nuevo. El botón **Replace** sustituye la coincidencia actual y **Replace All** sustituye todas las del archivo.
- **En varios archivos**: en la vista **Search**, despliega el campo **Replace** con la flecha situada a la izquierda del campo de búsqueda.
  - Al hacer clic en un resultado, VS Code muestra una **vista previa** con el antes y el después.
  - Puedes reemplazar un resultado concreto (icono junto al resultado), todos los de un archivo o todos los de la búsqueda (**Replace All**).

Reemplazar texto no entiende de Java: cambia texto, aunque esté en un comentario, en una cadena o en parte de otro nombre. Para renombrar elementos de código se usa **Rename Symbol** (apartado 7).

## 7. Trabajar con paquetes Java

### Relación entre paquetes y directorios

| Elemento | Valor en este ejercicio |
|---|---|
| Carpeta de código fuente | `src/` |
| Paquete | `curso.vscode` |
| Carpeta del paquete | `src/curso/vscode/` |
| Nombre completo de la clase | `curso.vscode.Alumno` |
| Archivo | `src/curso/vscode/Alumno.java` |

En el Explorer, VS Code compacta las carpetas que solo contienen otra carpeta, así que `curso/vscode` aparece en una sola línea. En la sección **Java Projects** se ven los mismos archivos organizados por paquetes, como en el *Package Explorer* de Eclipse.

Para comprobar qué carpetas reconoce la extensión como código fuente, ejecuta `Java: List All Java Source Paths` desde la Command Palette. Después de abrir alguna clase de este ejercicio, debe aparecer la carpeta `src` de `apartado-1.1.2-manejo-archivos`.

### Crear clases desde el Explorer

Hay dos formas, y las dos **requieren el soporte de Java**:

1. **New Java File**: haz clic derecho sobre la carpeta `src/curso/vscode` y elige **New Java File > Class...**. Escribe el nombre de la clase (sin `.java`). La extensión crea el archivo con la línea `package` y la declaración de la clase.
2. **New File...**: crea un archivo con extensión `.java` en la carpeta del paquete, por ejemplo `ClasePrueba.java`. Al abrirlo vacío, la extensión de Java rellena automáticamente la línea `package curso.vscode;` y el esqueleto de la clase.

Si quieres probarlo, crea `ClasePrueba.java`, observa el contenido generado y **elimínalo** después (clic derecho > **Delete**). No dejes clases de prueba en la carpeta: todas las prácticas del curso usan el paquete `curso.vscode`, y una clase con el mismo nombre en dos prácticas provocaría errores de clase duplicada.

### Localizar declaraciones de clases

- `Ctrl + T` (**Go > Go to Symbol in Workspace...**) y escribe el nombre de la clase.
- `Ctrl + P` (**Go > Go to File...**) y escribe el nombre del archivo.
- En **Java Projects**, despliega el paquete `curso.vscode`.

### Go to Definition

Sitúa el cursor sobre un nombre (una clase, un método o una variable) y usa cualquiera de estas opciones:

- `F12`, o clic derecho > **Go to Definition**.
- `Ctrl` + clic sobre el nombre.
- **Peek Definition** (`Alt + F12`, o clic derecho > **Peek > Peek Definition**): muestra la definición en una ventana incrustada, sin cambiar de archivo.

Para volver a donde estabas, usa **Go > Back** (`Alt + ←`).

Para ver dónde se usa un elemento, utiliza **Go to References** (`Shift + F12`) o **Find All References** (`Shift + Alt + F12`). Las dos opciones están también en el menú contextual del editor. Find All References abre los resultados en una vista lateral (*References*).

> **Requiere el soporte de Java.** Go to Definition, Peek y Find All References los proporciona la extensión de Java. Sin ella, VS Code solo ofrece búsquedas de texto.

### Renombrar clases y métodos con F2

**Rename Symbol** (`F2`, o clic derecho > **Rename Symbol**) es una **refactorización**: cambia el nombre en la declaración y en todas las referencias del código.

- Sobre un **método** (por ejemplo, `buscarPorId`), actualiza la declaración en `GestionAlumnos` y la llamada en `MainGestionAlumnos`.
- Sobre el nombre de una **clase** (por ejemplo, `Alumno` en `public class Alumno`), cambia la declaración y todos sus usos, y además renombra el archivo `Alumno.java`.
- Con `Shift + Enter` en lugar de `Enter`, VS Code muestra una **vista previa** de todos los cambios antes de aplicarlos.

> **Requiere el soporte de Java.** Si la extensión no está lista, `F2` no encuentra el símbolo o solo cambia el texto del archivo actual.

### Renombrar un archivo frente a refactorizar una clase

| | Renombrar el archivo (Explorer, `F2` en el Explorer) | Refactorizar la clase (editor, `F2` sobre el nombre) |
|---|---|---|
| Qué se cambia | El nombre del archivo en disco | El nombre de la clase en el código |
| Referencias en otras clases | Solo si la extensión de Java lo detecta y lo corrige | Se actualizan todas |
| Nombre del archivo | Cambia | Cambia también, para seguir coincidiendo con la clase |
| Riesgo | Clase y archivo pueden quedar con nombres distintos y el código deja de compilar | Bajo, y se puede revisar antes con la vista previa |

Recomendación: para cambiar el nombre de una clase Java, usa **siempre** Rename Symbol desde el editor.

### Evitar errores al mover clases entre paquetes

- Antes de mover una clase, comprueba quién la usa con **Find All References**.
- Si la mueves desde el Explorer, revisa después la línea `package` de la clase movida y los `import` de las clases que la usan.
- Mira el panel **Problems** (**View > Problems**). Si aparece un error del tipo *The declared package does not match the expected package*, la línea `package` no coincide con la carpeta.
- No muevas clases entre prácticas distintas del curso: cada práctica es independiente.

## 8. Práctica guiada

Sigue las actividades en orden. Todas son temporales: al terminar, la carpeta del ejercicio debe quedar igual que al principio.

### Actividad 1. Localizar las tres clases

1. Pulsa `Ctrl + P` (o **Go > Go to File...**) y escribe `Alumno`. Comprueba en la ruta de cada resultado que la clase es la de `apartado-1.1.2-manejo-archivos` y ábrela.
2. Repite con `GestionAlumnos` y `MainGestionAlumnos`.
3. Localiza las tres clases también en el Explorer y en la sección **Java Projects**, dentro del paquete `curso.vscode`.

**Resultado esperado:** las tres clases están abiertas en pestañas y sabes llegar a ellas por tres caminos distintos.

### Actividad 2. Navegar desde una llamada hasta su implementación

1. En `MainGestionAlumnos.java`, sitúa el cursor sobre `buscarPorId` en la línea `gestion.buscarPorId(idBuscado)`.
2. Pulsa `F12` (o clic derecho > **Go to Definition**).
3. Vuelve con **Go > Back** (`Alt + ←`).
4. Haz lo mismo con `new Alumno(...)`: el editor salta al constructor de `Alumno`.
5. Prueba **Peek Definition** sobre `contarAlumnos`.

**Resultado esperado:** el editor salta a `GestionAlumnos.java` y se sitúa en el método `buscarPorId`. Con Peek, la definición aparece incrustada sin cambiar de archivo.

### Actividad 3. Buscar referencias a GestionAlumnos

1. Abre `GestionAlumnos.java` y sitúa el cursor sobre el nombre de la clase, en `public class GestionAlumnos`.
2. Haz clic derecho > **Find All References**.

**Resultado esperado:** la vista *References* muestra los usos en `MainGestionAlumnos.java`, en la línea `GestionAlumnos gestion = new GestionAlumnos();`, que contiene el tipo de la variable y la llamada al constructor. Según la versión de la extensión, la lista también puede incluir la propia declaración de la clase. Al hacer clic en un resultado, el editor salta a esa línea.

### Actividad 4. Crear temporalmente un archivo de texto

1. En el Explorer, selecciona la carpeta `apartado-1.1.2-manejo-archivos`. Selecciona la carpeta del apartado, no `src`.
2. Usa **New File...** y escribe `notas.txt`.
3. Escribe en el archivo estas dos líneas y guárdalo con **File > Save** (`Ctrl + S`):

   ```text
   Archivo temporal de la práctica 1.1.2.
   Se eliminará al terminar.
   ```

**Resultado esperado:** `notas.txt` aparece en el Explorer al mismo nivel que `README.md` y `src`.

### Actividad 5. Renombrar el archivo y moverlo a otra carpeta

1. Selecciona `notas.txt` y pulsa `F2` (o clic derecho > **Rename...**). Cambia el nombre a `notas-practica.txt`.
2. Con la carpeta `apartado-1.1.2-manejo-archivos` seleccionada, usa **New Folder...** y crea la carpeta `temporal`.
3. Arrastra `notas-practica.txt` sobre la carpeta `temporal` y confirma el movimiento.
4. Alternativa sin ratón: selecciona el archivo, usa **Cut**, selecciona la carpeta `temporal` y usa **Paste**.

**Resultado esperado:** el archivo está en `apartado-1.1.2-manejo-archivos/temporal/notas-practica.txt`. Si la pestaña del archivo seguía abierta, VS Code actualiza su nombre automáticamente.

### Actividad 6. Eliminar los elementos temporales

1. Haz clic derecho sobre la carpeta `temporal` y elige **Delete**.
2. Lee el mensaje de confirmación y acepta. Se elimina la carpeta junto con su contenido.

**Resultado esperado:** en `apartado-1.1.2-manejo-archivos` solo quedan `README.md` y `src`.

### Actividad 7. Renombrar un método con F2 y deshacer el cambio

> **Requiere el soporte de Java.** Antes de empezar, comprueba en la Status Bar que la extensión de Java ha terminado de cargar.

1. En `GestionAlumnos.java`, sitúa el cursor sobre el nombre del método `buscarPorId`.
2. Pulsa `F2` (o clic derecho > **Rename Symbol**) y escribe `buscarAlumnoPorId`.
3. Pulsa `Shift + Enter` para ver la vista previa de los cambios. Revisa que aparecen los dos archivos afectados y aplica el cambio.
4. Abre `MainGestionAlumnos.java`: la llamada ahora es `gestion.buscarAlumnoPorId(idBuscado)`.
5. **Deshaz el cambio con la misma herramienta.** Sitúa el cursor sobre `buscarAlumnoPorId`, pulsa `F2` y escribe de nuevo `buscarPorId`.
6. Si alguna pestaña muestra el círculo de cambios sin guardar, usa **File > Save All**.

**Resultado esperado:** tras el paso 3, el nombre ha cambiado en la declaración y en la llamada. Tras el paso 5, los dos archivos vuelven a usar `buscarPorId`.

Deshacer con un segundo `F2` es más seguro que con `Ctrl + Z`, porque la refactorización afecta a varios archivos a la vez.

### Actividad 8. Buscar un texto y sustituirlo

1. Haz clic derecho sobre la carpeta `src` de este ejercicio y elige **Find in Folder...**. Así la búsqueda se limita al código y no incluye este README.
2. Busca `Listado de alumnos registrados`. Debe aparecer un único resultado, en `MainGestionAlumnos.java`.
3. Despliega el campo **Replace** con la flecha de la izquierda y escribe `Relación de alumnos registrados`.
4. Haz clic en el resultado para ver la vista previa del cambio.
5. Aplica el reemplazo con el icono **Replace** del resultado, o con **Replace All**.
6. Abre `MainGestionAlumnos.java` y comprueba que el texto ha cambiado.
7. **Restaura el texto original con la misma herramienta.** Busca `Relación de alumnos registrados` y reemplázalo por `Listado de alumnos registrados`.
8. Usa **File > Save All**.

**Resultado esperado:** al ejecutar el programa (apartado 9), la primera línea vuelve a ser `Listado de alumnos registrados:`.

### Comprobación final de la práctica

- En el Explorer, la carpeta del ejercicio contiene solo `README.md` y `src/curso/vscode/` con las tres clases.
- El panel **Problems** no muestra errores para estas clases.
- El programa se ejecuta y muestra la salida esperada del apartado 9.

## 9. Compilación y ejecución

### Desde VS Code

Abre `MainGestionAlumnos.java` y haz clic en **Run** sobre el método `main`. También puedes usar el botón **Run Java** de la esquina superior derecha del editor, o clic derecho sobre el archivo en el Explorer > **Run Java**.

La salida aparece en la **Terminal** o en la **Debug Console**, según la configuración, como se vio en el ejercicio 1.1.1.

### Desde la terminal, en la carpeta del ejercicio

La Terminal integrada se abre en la raíz del repositorio. Entra primero en la carpeta de la práctica:

```bash
cd modulo-01-vscode/tema-1.1-conceptos-basicos/apartado-1.1.2-manejo-archivos
javac --release 21 -Xlint:all -d out src/curso/vscode/*.java
java -cp out curso.vscode.MainGestionAlumnos
```

- `src/curso/vscode/*.java` compila a la vez las tres clases del paquete. `MainGestionAlumnos` necesita las otras dos.
- Equivale a escribir los tres archivos: `src/curso/vscode/Alumno.java src/curso/vscode/GestionAlumnos.java src/curso/vscode/MainGestionAlumnos.java`.
- Solo se ejecuta la clase que contiene `main`, por su nombre completo: `curso.vscode.MainGestionAlumnos`.

Para volver a la raíz del repositorio: `cd -`.

### Desde la terminal, en la raíz del repositorio

```bash
EJ=modulo-01-vscode/tema-1.1-conceptos-basicos/apartado-1.1.2-manejo-archivos
javac --release 21 -Xlint:all -d "$EJ/out" "$EJ"/src/curso/vscode/*.java
java -cp "$EJ/out" curso.vscode.MainGestionAlumnos
```

En los dos casos, los `.class` se generan en la carpeta `out/` del ejercicio. Esa carpeta está excluida en el `.gitignore` y puede borrarse sin problema.

### Salida esperada

```text
Listado de alumnos registrados:
  Alumno [id=1, nombre=Laura Martín, correo=laura.martin@example.com]
  Alumno [id=2, nombre=Carlos Ruiz, correo=carlos.ruiz@example.com]
  Alumno [id=3, nombre=Elena Gómez, correo=elena.gomez@example.com]

Búsqueda del alumno con id 2:
  Encontrado: Carlos Ruiz

Total de alumnos: 3
```

## 10. Comparación con Eclipse e IntelliJ IDEA

| Operación | Eclipse | IntelliJ IDEA | VS Code | Observaciones |
|---|---|---|---|---|
| Crear clases | **File > New > Class** (asistente) | **New > Java Class** en la ventana *Project* | **New Java File > Class...** en el Explorer, o **New File...** con nombre `.java` | En VS Code depende de la extensión de Java. El asistente es más sencillo que el de Eclipse: no ofrece superclase, interfaces ni *stubs*. |
| Renombrar | **Refactor > Rename** | **Refactor > Rename** | **Rename Symbol** (`F2`) en el editor | Equivalente para clases, métodos y variables. Renombrar desde el Explorer de VS Code es una operación de archivo, no una refactorización completa. |
| Mover | **Refactor > Move** | **Refactor > Move** | Arrastrar o **Cut** y **Paste** en el Explorer | No es exactamente equivalente. VS Code no tiene un asistente de movimiento: la extensión de Java intenta actualizar el paquete y las referencias, pero hay que revisar el resultado. |
| Buscar archivos | **Open Resource** | **Go to File** | **Quick Open** (`Ctrl + P`, **Go > Go to File...**) | Equivalente. |
| Buscar símbolos | **Open Type** y **Quick Outline** | **Go to Class**, **Go to Symbol** y **File Structure** | **Go to Symbol in Workspace** (`Ctrl + T`) y **Go to Symbol in Editor** (`Ctrl + Shift + O`) | Equivalente. En VS Code requiere el soporte de Java. |
| Buscar referencias | **References > Workspace** | **Find Usages** | **Find All References** (`Shift + Alt + F12`) y **Go to References** (`Shift + F12`) | Equivalente. En VS Code requiere el soporte de Java. |
| Abrir proyectos | **File > Import** o **Open Projects from File System** | **File > Open** (detecta `.idea`, `pom.xml`, etc.) | **File > Open Folder...** o `code .` | VS Code no importa nada: abre la carpeta y la extensión de Java detecta su estructura. No hay *workspace* de Eclipse ni carpeta `.idea`. |
| Comparar archivos | **Compare With > Each Other** | **Compare Files** | **Select for Compare** y **Compare with Selected** | Equivalente. VS Code añade **Compare Active File with Saved** para ver los cambios no guardados. |

## 11. Comprobaciones finales

Comprueba que eres capaz de:

- [ ] Crear, renombrar, mover, copiar y eliminar archivos y carpetas desde el Explorer, con el menú contextual y con los iconos de la cabecera.
- [ ] Explicar por qué no conviene renombrar ni mover clases Java desde el Explorer sin revisar después el resultado.
- [ ] Distinguir una pestaña de vista previa de una pestaña fija, y fijar una pestaña.
- [ ] Dividir el editor y comparar dos archivos.
- [ ] Cerrar un archivo y volver a abrirlo con `View: Reopen Closed Editor`.
- [ ] Localizar un archivo con `Ctrl + P` y un símbolo con `Ctrl + Shift + O` y `Ctrl + T`.
- [ ] Buscar texto solo dentro de la carpeta de un ejercicio con **Find in Folder...**.
- [ ] Relacionar el paquete `curso.vscode` con la carpeta `src/curso/vscode/`.
- [ ] Ir a la definición de un método con `F12` y volver con **Go > Back**.
- [ ] Encontrar todas las referencias a una clase con **Find All References**.
- [ ] Renombrar un método con `F2`, revisar la vista previa y deshacer el cambio.
- [ ] Dejar la carpeta del ejercicio exactamente como estaba al empezar.
- [ ] Compilar y ejecutar `MainGestionAlumnos` desde VS Code y desde la terminal.
