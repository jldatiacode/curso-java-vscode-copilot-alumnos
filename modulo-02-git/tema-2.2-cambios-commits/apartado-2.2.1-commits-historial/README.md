# Ejercicio 2.2 — Gestión de cambios, commits e historial

Módulo 2. Uso de VS Code con sistemas de control de versiones · Tema 2.2. Cambios y commits · Apartado 2.2.1. Commits e historial

Duración estimada: 1 hora.

## 1. Introducción

En el ejercicio 2.1 se inicializó un repositorio, pero todavía no se guardó ninguna versión. Ahora se aplica el ciclo de trabajo diario con Git:

```text
editar → revisar las diferencias → preparar (stage) → confirmar (commit) → consultar el historial
```

Hacer commits no consiste en "guardar de vez en cuando". Cada commit debe contener **un cambio con un único objetivo** y un mensaje que lo explique. Así, el historial cuenta la evolución del proyecto y es fácil revisar, comparar o deshacer un cambio concreto. Para lograrlo, VS Code permite revisar cada línea modificada y elegir exactamente qué entra en cada commit.

## 2. Objetivos

Al terminar este ejercicio serás capaz de:

- Revisar los cambios línea a línea en el **Diff Editor**.
- Preparar y retirar cambios del área de preparación (**Stage Changes** / **Unstage Changes**), por archivo y por líneas.
- Crear commits con objetivos diferenciados y mensajes descriptivos.
- Consultar el historial con **Source Control Graph** y **Timeline**.
- Comparar versiones de un archivo.
- Descartar cambios no confirmados.
- Distinguir *restore*, *revert* y *reset*.

## 3. Conceptos fundamentales

### Working tree y staging

- **Working tree** (directorio de trabajo): tus archivos tal como están ahora. En Source Control aparecen en la lista **Changes**.
- **Staging** (área de preparación o *index*): los cambios elegidos para el próximo commit. Aparecen en **Staged Changes**.

Un mismo archivo puede tener **una parte preparada y otra no**: aparece entonces en las dos listas.

### Commit y mensaje

Un buen mensaje de commit:

- **Resume el objetivo en una línea corta**, preferiblemente de menos de 72 caracteres, en infinitivo o imperativo: *Mostrar el número de tareas en cada listado*.
- **Explica el qué y el porqué, no el cómo**: el código ya muestra el cómo.
- **Opcionalmente, añade un cuerpo** tras una línea en blanco, con más detalle.

Mensajes como *cambios*, *arreglos* o *wip* no ayudan a nadie a entender el historial.

### Historial

Cada commit apunta al anterior. `HEAD` es el commit en el que estás: el último de la rama actual. `HEAD~1` es el anterior, `HEAD~2` el anterior a ese, y así sucesivamente.

### Restore, Revert y Reset

| Operación | Qué hace | ¿Modifica el historial? | En VS Code | Comando |
|---|---|---|---|---|
| **Restore** | Descarta cambios **no confirmados** de un archivo y lo devuelve a la última versión confirmada | No | **Discard Changes** | `git restore <archivo>` |
| **Restore --staged** | Saca un archivo del área de preparación **sin perder los cambios** | No | **Unstage Changes** | `git restore --staged <archivo>` |
| **Revert** | Crea un **commit nuevo** que deshace los cambios de un commit anterior | Añade un commit, no borra nada | Sin botón propio: se usa la terminal | `git revert <commit>` |
| **Reset** | Mueve la rama a otro commit. Con `--soft` conserva los cambios preparados; con `--hard` los **destruye**. | Sí: "quita" commits de la rama | **Undo Last Commit** (equivale a `--soft` del último commit) | `git reset --soft HEAD~1` |

Reglas prácticas:

- Para deshacer algo **aún no confirmado**, usa **Restore**.
- Para deshacer un commit **ya compartido** con otros, usa **Revert**: no reescribe el historial.
- **Reset** solo con commits locales que no has compartido. En este curso solo se usa **Undo Last Commit**, que no pierde trabajo. **No se usa `git reset --hard`**, porque borra cambios sin posibilidad de recuperarlos.

## 4. Preparación del entorno

### 4.1. Punto de partida

Este ejercicio parte del **Gestor de tareas en su versión inicial**, la plantilla del apartado 2.1 (`modulo-02-git/tema-2.1-fundamentos/apartado-2.1.1-source-control/plantilla`), en una carpeta de prácticas **fuera del repositorio del curso** y con un repositorio Git inicializado.

Elige una opción:

**Opción A. Continuar desde el ejercicio 2.1.** Si terminaste el 2.1, abre `~/practicas-git/gestor-tareas` en VS Code. Debe tener el repositorio inicializado y ningún commit.

**Opción B. Empezar desde cero.** Actualiza el repositorio del curso (`git status` y `git pull --ff-only` en su raíz) y, desde esa raíz, ejecuta:

**Windows (PowerShell):**

```powershell
New-Item -ItemType Directory -Force "$HOME/practicas-git" | Out-Null
Copy-Item -Recurse modulo-02-git/tema-2.1-fundamentos/apartado-2.1.1-source-control/plantilla "$HOME/practicas-git/gestor-tareas-2.2"
Set-Location "$HOME/practicas-git/gestor-tareas-2.2"
git init -b main
code .
```

**Linux o WSL (Bash):**

```bash
mkdir -p ~/practicas-git
cp -r modulo-02-git/tema-2.1-fundamentos/apartado-2.1.1-source-control/plantilla ~/practicas-git/gestor-tareas-2.2
cd ~/practicas-git/gestor-tareas-2.2
git init -b main
code .
```

En los dos casos, trabaja en la **ventana de prácticas** que se abre. Si el destino ya existe, bórralo o cambia el nombre.

### 4.2. Configurar tu identidad en Git

Cada commit registra el nombre y el correo del autor. Comprueba si ya están configurados:

```bash
git config --global user.name
git config --global user.email
```

Si no muestran nada, configúralos. El comando es el mismo en PowerShell y Bash:

```bash
git config --global user.name "Tu Nombre"
git config --global user.email "tu.correo@example.com"
```

- **Alcance.** `--global` guarda la identidad para todos tus repositorios de este equipo.
- **Windows y WSL.** Tienen configuraciones **independientes**: si usas los dos, configúrala en ambos.
- **Privacidad.** El correo quedará en el historial, que puede llegar a ser público. En el ejercicio 2.4 se explica cómo usar el correo privado que proporciona GitHub.

### 4.3. Recursos del ejercicio

`recursos/` contiene la versión final esperada de los dos archivos que vas a modificar:

- `recursos/Main.java.txt`
- `recursos/EstadoTarea.java.txt`

Llevan la extensión `.txt` para que VS Code no los trate como código del curso. Sirven para comparar tu trabajo o recuperarte si te equivocas (apartado 8). No hace falta usarlos si sigues los pasos.

## 5. La interfaz de VS Code

### Source Control

| Elemento | Uso |
|---|---|
| Lista **Changes** | Cambios sin preparar. Clic en un archivo: abre el Diff Editor. |
| Icono **+** de un archivo (**Stage Changes**) | Lo pasa a **Staged Changes** |
| Icono **+** de la cabecera **Changes** (**Stage All Changes**) | Prepara todos los cambios |
| Icono **−** de un archivo preparado (**Unstage Changes**) | Lo devuelve a **Changes** sin perder nada |
| Icono de flecha curva (**Discard Changes**) | Descarta los cambios del archivo, previa confirmación |
| Cuadro de mensaje y botón **Commit** | Confirma lo que hay en **Staged Changes** |
| Flecha junto a **Commit** | Variantes: **Commit (Amend)**, **Commit All**… |
| **Source Control Graph** | Historial de commits y ramas. Clic en un commit: muestra sus archivos y sus cambios. |

Si pulsas **Commit** sin nada preparado, VS Code pregunta si quieres preparar **todos** los cambios y confirmarlos. Responde que no cuando quieras commits separados.

### Diff Editor

Al hacer clic en un archivo modificado se abre una comparación:

- **A la izquierda**, la versión confirmada (`HEAD`) o la preparada. **A la derecha**, tu versión actual.
- Las líneas eliminadas se resaltan en rojo y las añadidas en verde.
- Los iconos de la parte superior permiten pasar al cambio siguiente o anterior y alternar entre vista **lado a lado** e **integrada** (*inline*).
- **Stage Selected Ranges:** selecciona unas líneas en el lado derecho, haz clic derecho y elige **Stage Selected Ranges**. Solo esas líneas pasan al área de preparación. Equivale a `git add -p` en la terminal.

### Timeline

En el Explorer, la sección **Timeline** muestra la historia del **archivo abierto**:

- los commits que lo modificaron (*Git History*);
- los guardados locales de VS Code (*Local History*).

Clic en una entrada: compara esa versión con la anterior. Clic derecho: **Compare with Selected** y otras opciones.

## 6. Comandos Git equivalentes

| Acción en VS Code | Comando Git |
|---|---|
| Ver cambios (lista **Changes**) | `git status` |
| Diff Editor de un archivo sin preparar | `git diff <archivo>` |
| Diff de lo preparado | `git diff --staged` |
| **Stage Changes** | `git add <archivo>` |
| **Stage All Changes** | `git add .` |
| **Stage Selected Ranges** | `git add -p <archivo>` |
| **Unstage Changes** | `git restore --staged <archivo>` |
| **Discard Changes** | `git restore <archivo>` |
| **Commit** | `git commit -m "Mensaje"` |
| **Undo Last Commit** | `git reset --soft HEAD~1` |
| **Commit (Amend)** | `git commit --amend` |
| Source Control Graph | `git log --oneline --graph` |
| Ver un commit | `git show <commit>` |
| Timeline de un archivo | `git log --oneline -- <archivo>` |
| Comparar dos versiones | `git diff <commit1> <commit2>` |

## 7. Práctica guiada

Trabaja en la **ventana de prácticas**, no en la del repositorio del curso.

### Paso 1. Crear el commit inicial

1. En **Source Control**, todos los archivos aparecen en **Changes** con la letra **U**.
2. Pulsa el **+** de la cabecera **Changes** (**Stage All Changes**). Los archivos pasan a **Staged Changes** con la letra **A**.
3. Escribe el mensaje `Versión inicial del gestor de tareas` y pulsa **Commit**.

**Resultado esperado:** las listas quedan vacías y **Source Control Graph** muestra un commit.

Comandos equivalentes: `git add .` y `git commit -m "Versión inicial del gestor de tareas"`.

Si VS Code muestra un error sobre `user.name` y `user.email`, configura tu identidad (apartado 4.2) y repite el paso.

### Paso 2. Modificar el método que lista tareas

Abre `src/curso/git/Main.java` y localiza el método `mostrarTareas`. Sustituye estas dos líneas:

```java
        System.out.println(titulo + ":");
        for (Tarea tarea : tareas) {
```

por estas cinco:

```java
        System.out.println(titulo + " (" + tareas.size() + "):");
        if (tareas.isEmpty()) {
            System.out.println("  (ninguna)");
        }
        for (Tarea tarea : tareas) {
```

Guarda el archivo.

**Objetivo de este cambio:** cada listado muestra cuántas tareas contiene e indica `(ninguna)` si está vacío.

### Paso 3. Añadir una funcionalidad pequeña

Abre `src/curso/git/EstadoTarea.java` y sustituye el bloque `enum` completo:

```java
public enum EstadoTarea {
    PENDIENTE, EN_PROGRESO, COMPLETADA
}
```

por:

```java
public enum EstadoTarea {
    PENDIENTE("Pendiente"), EN_PROGRESO("En progreso"), COMPLETADA("Completada");

    // Texto que se muestra al usuario en lugar del nombre de la constante.
    private final String etiqueta;

    EstadoTarea(String etiqueta) {
        this.etiqueta = etiqueta;
    }

    @Override
    public String toString() {
        return etiqueta;
    }
}
```

Guarda el archivo.

**Objetivo de este cambio:** los estados se muestran con texto legible (`En progreso`) en lugar del nombre de la constante (`EN_PROGRESO`).

### Paso 4. Comprobar los cambios en Source Control

**Resultado esperado:** la lista **Changes** contiene `EstadoTarea.java` y `Main.java`, con la letra **M**. En la terminal, `git status` muestra:

```text
On branch main
Changes not staged for commit:
  (use "git add <file>..." to update what will be committed)
  (use "git restore <file>..." to discard changes in working directory)
	modified:   src/curso/git/EstadoTarea.java
	modified:   src/curso/git/Main.java

no changes added to commit (use "git add" and/or "git commit -a")
```

### Paso 5. Revisar las diferencias línea a línea

1. Haz clic en `Main.java` en **Changes**. Se abre el Diff Editor: a la izquierda una línea eliminada (`titulo + ":"`) y a la derecha cuatro añadidas.
2. Alterna entre la vista lado a lado y la integrada con el icono de la barra superior.
3. Haz lo mismo con `EstadoTarea.java`.
4. Compila y ejecuta para comprobar que todo funciona:

   **PowerShell:**

   ```powershell
   javac --release 21 -Xlint:all -d out (Get-ChildItem src/curso/git/*.java).FullName
   java -cp out curso.git.Main
   ```

   **Bash:**

   ```bash
   javac --release 21 -Xlint:all -d out src/curso/git/*.java
   java -cp out curso.git.Main
   ```

**Resultado esperado de la ejecución:**

```text
== Gestor de tareas ==

Todas las tareas (4):
  #1 Instalar JDK 21 [Pendiente]
  #2 Configurar VS Code [Pendiente]
  #3 Clonar el repositorio [Pendiente]
  #4 Primer commit [Pendiente]

Cambio de estado de la tarea 99: no existe

Tareas después de los cambios (4):
  #1 Instalar JDK 21 [Completada]
  #2 Configurar VS Code [En progreso]
  #3 Clonar el repositorio [Pendiente]
  #4 Primer commit [Pendiente]

Tareas pendientes (2):
  #3 Clonar el repositorio [Pendiente]
  #4 Primer commit [Pendiente]
```

La carpeta `out/` no aparece en Source Control porque está en `.gitignore`.

### Paso 6. Preparar solo el primer cambio

Los dos cambios tienen objetivos distintos, así que irán en **commits distintos**.

1. Pulsa el **+** junto a `Main.java` (**Stage Changes**). Solo ese archivo pasa a **Staged Changes**.
2. Pulsa el **−** junto a `Main.java` (**Unstage Changes**) para ver que vuelve a **Changes** sin perder nada. Vuelve a prepararlo con **+**.

**Resultado esperado:** `Main.java` está en **Staged Changes** y `EstadoTarea.java` sigue en **Changes**. `git status` muestra:

```text
On branch main
Changes to be committed:
  (use "git restore --staged <file>..." to unstage)
	modified:   src/curso/git/Main.java

Changes not staged for commit:
  (use "git add <file>..." to update what will be committed)
  (use "git restore <file>..." to discard changes in working directory)
	modified:   src/curso/git/EstadoTarea.java
```

### Paso 7. Crear los dos commits

1. Mensaje `Mostrar el número de tareas en cada listado` y **Commit**. Solo se confirma `Main.java`.
2. Prepara `EstadoTarea.java` con su **+**.
3. Mensaje `Mostrar los estados de las tareas con texto legible` y **Commit**.

**Resultado esperado:** las listas quedan vacías. Comandos equivalentes:

```bash
git add src/curso/git/Main.java
git commit -m "Mostrar el número de tareas en cada listado"
git add src/curso/git/EstadoTarea.java
git commit -m "Mostrar los estados de las tareas con texto legible"
```

### Paso 8. Consultar el historial

1. En **Source Control Graph**, observa los tres commits, del más reciente al más antiguo.
2. Haz clic en `Mostrar el número de tareas en cada listado`: solo aparece `Main.java`. Haz clic en el archivo para ver exactamente ese cambio.
3. Abre `Main.java` y, en el Explorer, despliega **Timeline**: aparecen los dos commits que modificaron este archivo (el inicial y el del listado), pero no el de los estados.
4. En la terminal:

   ```bash
   git log --oneline
   ```

**Resultado esperado:** tres líneas como estas. Los identificadores serán distintos en tu equipo.

```text
8ac9c13 Mostrar los estados de las tareas con texto legible
5cefc18 Mostrar el número de tareas en cada listado
012e3af Versión inicial del gestor de tareas
```

### Paso 9. Comparar versiones

1. En **Timeline** de `Main.java`, haz clic en el commit `Mostrar el número de tareas en cada listado`. Se abre la comparación con la versión anterior.
2. Compara la versión inicial con la actual del proyecto:

   ```bash
   git diff --stat HEAD~2 HEAD
   ```

**Resultado esperado:**

```text
 src/curso/git/EstadoTarea.java | 14 +++++++++++++-
 src/curso/git/Main.java        |  5 ++++-
 2 files changed, 17 insertions(+), 2 deletions(-)
```

Sin `--stat`, se ven las líneas concretas: `git diff HEAD~2 HEAD`. Pulsa `q` para salir si la salida se muestra paginada.

### Paso 10. Deshacer una modificación temporal sin afectar a los commits

1. En `src/curso/git/Tarea.java`, en el método `resumen()`, cambia `"#"` por `"Tarea "` y guarda. `Tarea.java` aparece en **Changes** con **M**.
2. Revísalo en el Diff Editor.
3. Pulsa el icono **Discard Changes** (flecha curva) junto a `Tarea.java` y confirma.

**Resultado esperado:** el archivo vuelve a su última versión confirmada y desaparece de **Changes**. `git status` indica *nothing to commit, working tree clean*. Los tres commits siguen intactos en `git log --oneline`.

Comando equivalente: `git restore src/curso/git/Tarea.java`.

**Discard Changes no se puede deshacer** desde Git: los cambios descartados no estaban en ningún commit. VS Code pide confirmación por ese motivo. Como último recurso, **Timeline** puede conservar una copia en *Local History*.

## 8. Resolución de errores frecuentes y recuperación

| Situación | Qué hacer |
|---|---|
| *Make sure you configure your user.name and user.email* | Configura tu identidad (apartado 4.2) y repite el commit |
| Has confirmado los dos cambios en un solo commit | Si no lo has compartido: **Undo Last Commit** (Command Palette > `Git: Undo Last Commit`). Los cambios vuelven a **Staged Changes**. Pulsa **−** en `EstadoTarea.java` y repite los commits por separado. |
| Mensaje de commit con una errata | Si es el último commit y no lo has compartido: flecha junto a **Commit** > **Commit (Amend)**, o `git commit --amend -m "Mensaje corregido"` |
| Has preparado el archivo equivocado | **Unstage Changes** (−). No se pierde nada. |
| Un archivo no compila tras editarlo | Compáralo con el de `recursos/`: selecciona tu archivo en el Explorer > **Select for Compare** y el `.txt` correspondiente > **Compare with Selected** |
| Quieres recuperar el contenido esperado de un archivo | Copia el contenido de `recursos/<archivo>.java.txt` sobre tu archivo y guarda |
| Quieres repetir la práctica desde cero | Cierra la ventana, borra la carpeta de prácticas y vuelve a seguir la opción B del apartado 4.1, con otro nombre si lo prefieres |
| Aparece `out/` o un `.class` en **Changes** | Falta `.gitignore` o se ha modificado. Compruébalo antes de confirmar. |
| Avisos *LF will be replaced by CRLF* en Windows | Son informativos. Git para Windows convierte los finales de línea; no afecta a la práctica. |

**No uses `git reset --hard`** para "volver atrás": borra los cambios no confirmados sin posibilidad de recuperarlos. Los recursos de este apartado permiten recuperarse sin ese comando.

## 9. Comparativa con Eclipse e IntelliJ IDEA

| Acción | Eclipse (EGit) | IntelliJ IDEA | VS Code |
|---|---|---|---|
| Ver cambios | Vista **Git Staging**: *Unstaged Changes* / *Staged Changes* | Ventana **Commit** (`Alt + 0`), con casillas por archivo | **Source Control**: **Changes** / **Staged Changes** |
| Preparar un archivo | Arrastrar a *Staged Changes*, o **Add to Index** | Marcar la casilla del archivo | **+** (**Stage Changes**) |
| Preparar líneas sueltas | **Compare** y *Add to Index* por bloques | Casillas por bloque en el diff de la ventana Commit | **Stage Selected Ranges** en el Diff Editor |
| Diferencias | **Compare With > HEAD Revision** | Diff con `Ctrl + D` | Diff Editor (clic en el archivo) |
| Commit | Mensaje y **Commit** en Git Staging (`Ctrl + #`) | Mensaje y **Commit** (`Ctrl + K`) | Mensaje y **Commit** (`Ctrl + Enter` en el cuadro de mensaje) |
| Corregir el último commit | **Amend** en Git Staging | Casilla **Amend** | **Commit (Amend)** |
| Historial | Vista **History** | Pestaña **Log** de la ventana **Git** | **Source Control Graph** y **Timeline** |
| Historia de un archivo | **Team > Show in History** | **Git > Show History** | **Timeline** |
| Descartar cambios | **Replace With > HEAD Revision** | **Rollback** (`Ctrl + Alt + Z`) | **Discard Changes** |
| Deshacer el último commit | **Reset** (Soft) desde History | **Undo Commit** | **Undo Last Commit** |
| Revertir un commit | **Revert Commit** desde History | **Revert Commit** desde Log | `git revert <commit>` en la terminal |

## 10. Comprobaciones finales

Comprueba que eres capaz de:

- [ ] Configurar y comprobar tu identidad de Git (`user.name`, `user.email`).
- [ ] Revisar un cambio en el Diff Editor e interpretar líneas añadidas y eliminadas.
- [ ] Preparar y retirar archivos del área de preparación, y explicar qué es **Stage Selected Ranges**.
- [ ] Crear commits separados para cambios con objetivos distintos, con mensajes descriptivos.
- [ ] Consultar el historial con **Source Control Graph**, **Timeline** y `git log --oneline`.
- [ ] Comparar dos versiones de un archivo o del proyecto.
- [ ] Descartar un cambio no confirmado y saber que no se puede recuperar desde Git.
- [ ] Explicar la diferencia entre *restore*, *revert* y *reset*, y cuándo usar cada uno.
- [ ] Recuperarte de un commit incorrecto con **Undo Last Commit** sin perder trabajo.
