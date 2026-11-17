# Ejercicio 2.3 — Ramas, merges y resolución de conflictos

Módulo 2. Uso de VS Code con sistemas de control de versiones · Tema 2.3. Ramas y conflictos · Apartado 2.3.1. Ramas y merges

Duración estimada: 1 hora y 30 minutos.

## 1. Introducción

Las **ramas** permiten desarrollar varias cosas a la vez sin que se mezclen: cada funcionalidad avanza en su propia rama y, cuando está terminada, se **integra** (*merge*) en la rama principal. Git combina automáticamente los cambios que no se solapan. Cuando dos ramas modifican **la misma línea** de forma distinta, Git no puede decidir y se produce un **conflicto**, que hay que resolver a mano.

En este ejercicio se crean dos ramas sobre el Gestor de tareas y se integran:

- la primera **sin conflicto**;
- la segunda con un **conflicto provocado a propósito** y totalmente reproducible, que se resuelve con el **Merge Editor** de VS Code.

## 2. Objetivos

Al terminar este ejercicio serás capaz de:

- Explicar qué es una rama y el papel de `main`.
- Crear ramas y cambiar entre ellas desde VS Code.
- Diferenciar `git checkout` y `git switch`.
- Integrar ramas con *merge* y distinguir *fast-forward* de *merge commit*.
- Reconocer un conflicto, entender sus marcas y resolverlo con el Merge Editor.
- Decidir entre aceptar un lado, aceptar ambos o combinar los cambios a mano, y comprobar que el código compila.
- Abortar un merge de forma segura.

## 3. Conceptos fundamentales

### Qué es una rama

Una rama es simplemente un **puntero con nombre a un commit**. Al hacer un commit en una rama, el puntero avanza al nuevo commit. Crear una rama no copia archivos: es instantáneo.

```text
        A ─── B             ← main
               \
                C ─── D     ← feature/x
```

`HEAD` indica la rama en la que estás. Al **cambiar de rama**, Git sustituye los archivos de tu carpeta por los de esa rama.

### La rama principal `main`

`main` contiene la versión estable e integrada del proyecto. El trabajo nuevo se hace en ramas de funcionalidad, con nombres como `feature/descripcion-tarea`, y se integra en `main` cuando está listo.

### `checkout` y `switch`

| Comando | Para qué sirve |
|---|---|
| `git checkout <rama>` | Comando antiguo y polivalente: cambia de rama, pero también restaura archivos (`git checkout -- archivo`). Su doble uso provoca errores. |
| `git switch <rama>` | Solo cambia de rama (desde Git 2.23). `git switch -c <rama>` crea una rama y cambia a ella. |
| `git restore <archivo>` | Solo restaura archivos. Es la otra mitad de lo que hacía `checkout`. |

VS Code llama a la acción **Checkout to...**, pero el efecto es el de `git switch`.

### Merge: fast-forward y merge commit

Al integrar la rama `B` en `A` (estando en `A`) pueden darse dos casos:

- **Fast-forward:** `A` no ha avanzado desde que se creó `B`. Git simplemente mueve el puntero de `A` hasta el último commit de `B`. No se crea ningún commit nuevo.

  ```text
  Antes:  A ─── B  ← main        Después:  A ─── B ─── C  ← main, feature
                 \
                  C  ← feature
  ```

- **Merge commit:** las dos ramas han avanzado. Git crea un **commit de merge** con dos padres, que une ambas historias.

  ```text
  A ─── B ─── D ─────── M  ← main
         \             /
          C ───────────    ← feature
  ```

### Conflictos

Si las dos ramas cambiaron **las mismas líneas** de un archivo, Git detiene el merge y marca la zona en conflicto:

```text
<<<<<<< HEAD
versión de la rama actual (Current)
=======
versión de la rama que se integra (Incoming)
>>>>>>> nombre-de-la-rama
```

El merge queda **a medias** hasta que se resuelven todos los conflictos y se confirma el resultado. Mientras tanto, siempre se puede **abortar** y volver exactamente al estado anterior.

## 4. Preparación del entorno

### 4.1. Punto de partida reproducible

Para que el conflicto y las salidas coincidan exactamente con este README, este ejercicio parte de una **copia nueva de la plantilla del apartado 2.1** (`modulo-02-git/tema-2.1-fundamentos/apartado-2.1.1-source-control/plantilla`), con un único commit inicial.

Requisitos:

- haber completado la preparación del ejercicio 2.1: Git instalado y repositorio del curso actualizado con `git status` y `git pull --ff-only`;
- tener tu identidad de Git configurada, como en el ejercicio 2.2.

Desde la **raíz del repositorio del curso**:

**Windows (PowerShell):**

```powershell
New-Item -ItemType Directory -Force "$HOME/practicas-git" | Out-Null
Copy-Item -Recurse modulo-02-git/tema-2.1-fundamentos/apartado-2.1.1-source-control/plantilla "$HOME/practicas-git/gestor-tareas-2.3"
Set-Location "$HOME/practicas-git/gestor-tareas-2.3"
git init -b main
git add .
git commit -m "Versión inicial del gestor de tareas"
code .
```

**Linux o WSL (Bash):**

```bash
mkdir -p ~/practicas-git
cp -r modulo-02-git/tema-2.1-fundamentos/apartado-2.1.1-source-control/plantilla ~/practicas-git/gestor-tareas-2.3
cd ~/practicas-git/gestor-tareas-2.3
git init -b main
git add .
git commit -m "Versión inicial del gestor de tareas"
code .
```

**Resultado esperado:** se abre la **ventana de prácticas**, la Status Bar muestra `main` y **Source Control Graph** muestra un commit. Si el destino ya existe, bórralo o usa otro nombre.

### 4.2. Recursos del ejercicio

`recursos/` contiene el contenido esperado de los archivos en cada momento, con extensión `.txt` para que VS Code no los trate como código:

| Carpeta | Archivos | Momento |
|---|---|---|
| `recursos/rama-descripcion/` | `Tarea.java.txt` | Tras el paso 3 |
| `recursos/rama-formato/` | `Tarea.java.txt`, `Main.java.txt` | Tras el paso 6 |
| `recursos/tras-el-merge/` | `Tarea.java.txt`, `Main.java.txt` | Tras resolver el conflicto (paso 12) |

Úsalos para comparar con tu trabajo (**Select for Compare** / **Compare with Selected**) o para copiar su contenido si te equivocas al editar.

## 5. La interfaz de VS Code

| Elemento | Uso |
|---|---|
| **Rama en la Status Bar** (abajo a la izquierda) | Clic: lista de ramas, con **Create new branch...** y la opción de cambiar a otra rama |
| Command Palette > `Git: Create Branch...` | Crea una rama a partir de la actual y cambia a ella |
| Command Palette > `Git: Checkout to...` | Cambia a otra rama (equivale a `git switch`) |
| Command Palette > `Git: Merge...` | Integra la rama elegida **en la rama actual** |
| Command Palette > `Git: Abort Merge` | Cancela un merge a medias y vuelve al estado anterior |
| Command Palette > `Git: Delete Branch...` | Borra una rama ya integrada |
| **Source Control Graph** | Dibuja las ramas y los merges |
| Sección **Merge Changes** de Source Control | Archivos con conflictos pendientes, marcados con **!** |

### Resolver conflictos en VS Code

Al abrir un archivo con conflicto, VS Code resalta cada zona y muestra encima unas acciones en línea:

- **Accept Current Change**: se queda con la versión de la rama actual.
- **Accept Incoming Change**: se queda con la versión de la rama que se integra.
- **Accept Both Changes**: conserva las dos versiones, una detrás de otra.
- **Compare Changes**: las muestra lado a lado.

Además aparece el botón **Resolve in Merge Editor**, que abre el **Merge Editor**:

| Zona | Contenido |
|---|---|
| Arriba a la izquierda: **Incoming** | Versión de la rama que se integra (`feature/formato-listado`) |
| Arriba a la derecha: **Current** | Versión de la rama actual (`main`) |
| Abajo: **Result** | El archivo final, **editable**. Es lo que se guardará. |

En cada conflicto, las casillas **Accept Incoming** y **Accept Current** llevan esa versión al resultado; marcar las dos equivale a **Accept Combination**. Cuando el resultado es correcto, el botón **Complete Merge** guarda el archivo y lo marca como resuelto.

> **Aceptar un lado o combinar.** Aceptar solo un lado **descarta** el cambio del otro. Aceptar ambos pone las dos líneas una detrás de otra, lo que en código **rara vez es correcto**. Lo habitual es partir de uno de los lados y **editar el resultado** para que recoja la intención de los dos cambios.

## 6. Comandos Git equivalentes

| Acción en VS Code | Comando Git |
|---|---|
| **Create new branch...** | `git switch -c <rama>` |
| Cambiar de rama (**Checkout to...**) | `git switch <rama>` |
| Ver ramas | `git branch` |
| **Git: Merge...** | `git merge <rama>` |
| **Git: Abort Merge** | `git merge --abort` |
| Archivos en conflicto (**Merge Changes**) | `git status` → *Unmerged paths* |
| **Complete Merge** | `git add <archivo>` |
| Commit del merge | `git commit --no-edit` (mensaje por defecto) |
| **Source Control Graph** | `git log --oneline --graph --all` |
| **Git: Delete Branch...** | `git branch -d <rama>` |

## 7. Práctica guiada

Trabaja en la ventana de prácticas (`gestor-tareas-2.3`).

### Paso 1. Crear la rama `feature/descripcion-tarea`

1. Haz clic en `main` en la Status Bar y elige **Create new branch...**.
2. Escribe `feature/descripcion-tarea` y pulsa `Enter`.

**Resultado esperado:** la Status Bar muestra `feature/descripcion-tarea`. Comando equivalente: `git switch -c feature/descripcion-tarea`.

### Paso 2. Modificar el resumen de la tarea

En `src/curso/git/Tarea.java`, dentro del método `resumen()`, sustituye esta línea:

```java
        return "#" + id + " " + titulo + " [" + estado + "]";
```

por esta otra:

```java
        return "#" + id + " " + titulo + " [" + estado + "] - " + descripcion;
```

Guarda. **Objetivo:** el resumen de cada tarea incluye su descripción.

### Paso 3. Confirmar el cambio

En Source Control, prepara `Tarea.java` (**+**) y confirma con el mensaje `Mostrar la descripción en el resumen de la tarea`.

**Resultado esperado:** **Source Control Graph** muestra dos commits. Tu `Tarea.java` coincide con `recursos/rama-descripcion/Tarea.java.txt`.

### Paso 4. Volver a `main`

Haz clic en la rama de la Status Bar y elige `main`.

**Resultado esperado:** la línea del paso 2 vuelve a ser la original, porque en `main` ese cambio todavía no existe. El cambio sigue a salvo en su rama. Comando equivalente: `git switch main`.

### Paso 5. Crear la rama `feature/formato-listado`

Desde `main`, crea la rama `feature/formato-listado`, como en el paso 1.

**Resultado esperado:** la Status Bar muestra `feature/formato-listado`. Esta rama nace de `main`, así que **no** contiene el cambio de la descripción.

### Paso 6. Hacer los cambios de formato y confirmarlos

1. En `src/curso/git/Tarea.java`, sustituye la misma línea del método `resumen()`:

   ```java
           return "#" + id + " " + titulo + " [" + estado + "]";
   ```

   por:

   ```java
           return String.format("#%-2d %-22s [%s]", id, titulo, estado);
   ```

2. En `src/curso/git/Main.java`, dentro del método `mostrarTareas`, sustituye:

   ```java
               System.out.println("  " + tarea.resumen());
   ```

   por:

   ```java
               System.out.println("  - " + tarea.resumen());
   ```

3. Guarda, prepara los dos archivos y confirma con el mensaje `Alinear el formato del listado de tareas`.

**Objetivo:** los títulos aparecen alineados en columnas y cada tarea va precedida de un guion. Tus archivos coinciden con los de `recursos/rama-formato/`.

Las dos ramas han modificado **la misma línea** de `Tarea.java` de formas incompatibles. El conflicto está preparado.

### Paso 7. Integrar la primera rama, sin conflictos

1. Cambia a `main`.
2. Command Palette > `Git: Merge...` y elige `feature/descripcion-tarea`.

**Resultado esperado:** el merge termina sin preguntas. Es un **fast-forward**: `main` no había avanzado, así que solo se ha movido su puntero. **Source Control Graph** muestra `main` en el commit de la descripción. En la terminal, el comando equivalente `git merge feature/descripcion-tarea` muestra:

```text
Updating f3857f3..20df6f1
Fast-forward
 src/curso/git/Tarea.java | 2 +-
 1 file changed, 1 insertion(+), 1 deletion(-)
```

Los identificadores serán distintos en tu equipo.

### Paso 8. Comprobar la aplicación tras el primer merge

Compila y ejecuta:

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

**Resultado esperado:** las tareas muestran su descripción, por ejemplo `#1 Instalar JDK 21 [PENDIENTE] - Comprobar java -version`.

### Paso 9. Provocar el conflicto

En `main`, Command Palette > `Git: Merge...` y elige `feature/formato-listado`.

**Resultado esperado:**

- VS Code avisa de que hay conflictos.
- En Source Control aparece la sección **Merge Changes** con `Tarea.java` marcado con **!**.
- `Main.java` aparece en **Staged Changes**: su cambio no chocaba con nada y Git lo ha combinado solo.

En la terminal, `git status` muestra:

```text
On branch main
You have unmerged paths.
  (fix conflicts and run "git commit")
  (use "git merge --abort" to abort the merge)

Changes to be committed:
	modified:   src/curso/git/Main.java

Unmerged paths:
  (use "git add <file>..." to mark resolution)
	both modified:   src/curso/git/Tarea.java

```

Según el idioma de tu sistema, los mensajes pueden aparecer en español.

### Paso 10. Examinar el conflicto

Abre `src/curso/git/Tarea.java`. En el método `resumen()` verás:

```text
<<<<<<< HEAD
        return "#" + id + " " + titulo + " [" + estado + "] - " + descripcion;
=======
        return String.format("#%-2d %-22s [%s]", id, titulo, estado);
>>>>>>> feature/formato-listado
```

- Entre `<<<<<<< HEAD` y `=======` está la versión de `main` (**Current**), que ya incluye la descripción.
- Entre `=======` y `>>>>>>>` está la de `feature/formato-listado` (**Incoming**), con el nuevo formato.

Mientras el archivo tenga estas marcas, **no compila**.

### Paso 11. Analizar las opciones antes de decidir

| Opción | Resultado en `resumen()` | ¿Correcto? |
|---|---|---|
| **Accept Current** | Se queda la descripción, sin el formato | Compila, pero se pierde el trabajo de la rama de formato |
| **Accept Incoming** | Se queda el formato, sin la descripción | Compila, pero se pierde el trabajo de la rama de descripción |
| **Accept Both / Accept Combination** | Dos `return` seguidos | **No compila**: `error: unreachable statement` |
| **Combinar a mano** | Formato **y** descripción en una sola línea | Sí: recoge la intención de las dos ramas |

### Paso 12. Resolver el conflicto con el Merge Editor

1. Con `Tarea.java` abierto, pulsa **Resolve in Merge Editor**.
2. Observa **Incoming** (arriba a la izquierda), **Current** (arriba a la derecha) y **Result** (abajo).
3. En **Incoming**, marca **Accept Incoming**: el resultado toma la línea con `String.format`.
4. Edita la línea en **Result** para que quede exactamente así:

   ```java
           return String.format("#%-2d %-22s [%s] - %s", id, titulo, estado, descripcion);
   ```

5. Comprueba que en **Result** no queda ninguna marca `<<<<<<<`, `=======` ni `>>>>>>>`.
6. Pulsa **Complete Merge**. `Tarea.java` pasa de **Merge Changes** a **Staged Changes**.
7. En Source Control, el mensaje del commit ya está escrito (`Merge branch 'feature/formato-listado'`). Pulsa **Commit**.

**Resultado esperado:** tu `Tarea.java` coincide con `recursos/tras-el-merge/Tarea.java.txt` y tu `Main.java` con `recursos/tras-el-merge/Main.java.txt`.

Comandos equivalentes: editar el archivo, `git add src/curso/git/Tarea.java` y `git commit --no-edit`.

Si prefieres no usar el Merge Editor, puedes editar el archivo directamente: borra las tres líneas de marcas y deja solo la línea combinada. El resultado es el mismo.

### Paso 13. Comprobar el estado Git

En la terminal:

```bash
git status
git log --oneline --graph
```

**Resultado esperado:** `git status` indica *nothing to commit, working tree clean* y el historial muestra el **merge commit** con sus dos ramas:

```text
*   12e3fc5 Merge branch 'feature/formato-listado'
|\
| * bef8eee Alinear el formato del listado de tareas
* | 20df6f1 Mostrar la descripción en el resumen de la tarea
|/
* f3857f3 Versión inicial del gestor de tareas
```

**Source Control Graph** muestra lo mismo de forma gráfica. El primer merge, de tipo *fast-forward*, no dejó ningún commit propio. El segundo sí, porque las dos ramas habían avanzado.

### Paso 14. Compilar y ejecutar tras el merge

Compila y ejecuta con los comandos del paso 8.

**Resultado esperado:**

```text
== Gestor de tareas ==

Todas las tareas:
  - #1  Instalar JDK 21        [PENDIENTE] - Comprobar java -version
  - #2  Configurar VS Code     [PENDIENTE] - Instalar el Extension Pack for Java
  - #3  Clonar el repositorio  [PENDIENTE] - Usar git clone con HTTPS
  - #4  Primer commit          [PENDIENTE] - Confirmar la versión inicial del proyecto

Cambio de estado de la tarea 99: no existe

Tareas después de los cambios:
  - #1  Instalar JDK 21        [COMPLETADA] - Comprobar java -version
  - #2  Configurar VS Code     [EN_PROGRESO] - Instalar el Extension Pack for Java
  - #3  Clonar el repositorio  [PENDIENTE] - Usar git clone con HTTPS
  - #4  Primer commit          [PENDIENTE] - Confirmar la versión inicial del proyecto

Tareas pendientes:
  - #3  Clonar el repositorio  [PENDIENTE] - Usar git clone con HTTPS
  - #4  Primer commit          [PENDIENTE] - Confirmar la versión inicial del proyecto
```

El resultado incluye los tres cambios: la descripción, el formato alineado y el guion.

### Paso 15 (opcional). Borrar las ramas integradas

Command Palette > `Git: Delete Branch...` y elige `feature/descripcion-tarea`. Repite con `feature/formato-listado`. Como ya están integradas en `main`, no se pierde nada. Comando equivalente: `git branch -d <rama>`.

## 8. Resolución de errores frecuentes y recuperación

| Situación | Qué hacer |
|---|---|
| El merge ha quedado a medias y no sabes cómo seguir | Command Palette > `Git: Abort Merge` (o `git merge --abort`). Vuelves exactamente al estado anterior al merge y puedes repetir el paso 9. |
| Has pulsado **Accept Both** y no compila | Edita la zona y deja solo la línea combinada del paso 12. Si ya pulsaste **Complete Merge** pero no hiciste commit, edita, guarda y vuelve a preparar el archivo. |
| No aparecen conflictos al hacer el segundo merge | Una de las dos ramas no modificó la línea exacta. Compara tus archivos con `recursos/rama-descripcion/` y `recursos/rama-formato/` cambiando a cada rama. |
| VS Code no deja cambiar de rama | Tienes cambios sin confirmar que se perderían. Confírmalos o descártalos antes de cambiar de rama. |
| Has hecho el cambio en la rama equivocada, sin confirmar | Los cambios no confirmados viajan contigo al crear una rama nueva. Crea la rama correcta (**Create new branch...**) y confirma allí. |
| `error: unreachable statement` al compilar | Hay dos `return` seguidos en `resumen()`: deja solo la línea combinada |
| Algo ha salido mal y prefieres empezar de nuevo | Cierra la ventana, borra la carpeta `gestor-tareas-2.3` y repite la preparación del apartado 4.1 |

Este ejercicio **no usa comandos destructivos** (`git reset --hard`, `git push --force`). `git merge --abort` es seguro: solo cancela el merge en curso.

## 9. Comparativa con Eclipse e IntelliJ IDEA

| Acción | Eclipse (EGit) | IntelliJ IDEA | VS Code |
|---|---|---|---|
| Crear rama | **Team > Switch To > New Branch...** | Widget de ramas > **New Branch** | Rama de la Status Bar > **Create new branch...** |
| Cambiar de rama | **Team > Switch To** | Widget de ramas > **Checkout** | Rama de la Status Bar, o **Checkout to...** |
| Merge | **Team > Merge...** | Widget de ramas > **Merge into Current** | `Git: Merge...` |
| Ver conflictos | Decorador rojo en el Package Explorer; vista **Git Staging** | Diálogo **Conflicts** con la lista de archivos | Sección **Merge Changes** con **!** |
| Herramienta de resolución | **Merge Tool** (tres paneles) | **Merge** (tres paneles: *Yours*, *Result*, *Theirs*) | Acciones en línea y **Merge Editor** (*Incoming*, *Current*, *Result*) |
| Marcar como resuelto | **Add to Index** | Automático al aplicar el resultado | **Complete Merge** |
| Abortar el merge | **Reset** (*Hard*) al commit anterior | **Abort** en el diálogo de merge | `Git: Abort Merge` |
| Visualizar ramas | Vista **History** | Pestaña **Log** | **Source Control Graph** |

IntelliJ IDEA ofrece una resolución automática más agresiva (*magic wand*) que combina automáticamente los cambios no conflictivos dentro de una misma zona. VS Code y Eclipse dejan más trabajo manual. En los tres casos, la responsabilidad de que el resultado **compile y sea correcto** es del desarrollador.

## 10. Comprobaciones finales

Comprueba que eres capaz de:

- [ ] Explicar qué es una rama, qué representa `main` y qué es `HEAD`.
- [ ] Crear una rama y cambiar entre ramas desde la Status Bar, y explicar la diferencia entre `checkout` y `switch`.
- [ ] Integrar una rama con `Git: Merge...` y reconocer un *fast-forward*.
- [ ] Explicar cuándo se crea un *merge commit*.
- [ ] Identificar un conflicto en Source Control, en `git status` y por sus marcas en el archivo.
- [ ] Usar el Merge Editor y explicar la diferencia entre aceptar un lado, aceptar ambos y combinar a mano.
- [ ] Completar el merge, confirmar el resultado y comprobar que compila y funciona.
- [ ] Abortar un merge a medias con `Git: Abort Merge`.
- [ ] Interpretar el gráfico de ramas en **Source Control Graph** y en `git log --oneline --graph`.
