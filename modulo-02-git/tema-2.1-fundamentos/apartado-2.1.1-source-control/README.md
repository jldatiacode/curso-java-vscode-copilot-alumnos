# Ejercicio 2.1 — Fundamentos de Git y Source Control

Módulo 2. Uso de VS Code con sistemas de control de versiones · Tema 2.1. Fundamentos · Apartado 2.1.1. Source Control

Duración estimada: 1 hora.

## 1. Introducción

Git es el sistema de control de versiones más utilizado. Permite guardar versiones sucesivas de un proyecto, saber quién cambió qué y cuándo, volver atrás y trabajar en paralelo con otras personas sin pisarse.

En este módulo se usa Git **desde VS Code**, priorizando las acciones visuales de la vista **Source Control**. Para cada acción se indica también el comando Git equivalente: así se entiende lo que ocurre por debajo y se sabe resolver cualquier situación desde la terminal.

Todo el módulo trabaja sobre una pequeña aplicación Java, el **Gestor de tareas**, que irá evolucionando en cada ejercicio. Su punto de partida está en la carpeta `plantilla/` de este apartado.

> **Regla del módulo: las prácticas de Git se hacen fuera del repositorio del curso.**
> El repositorio del curso, el que contiene `modulo-01-vscode/`, `modulo-02-git/`, etc., solo se usa para **leer y actualizar** materiales con `git pull`. Las prácticas de commits, ramas y merges se hacen en una **copia de la plantilla, en otra carpeta** y con **un repositorio Git propio**. Si hicieras commits en el repositorio del curso, las siguientes actualizaciones (`git pull`) podrían fallar.

## 2. Objetivos

Al terminar este ejercicio serás capaz de:

- Explicar qué es Git, qué es GitHub y la diferencia entre repositorio local y remoto.
- Distinguir directorio de trabajo, área de preparación (*staging*) y repositorio.
- Inicializar un repositorio desde la vista **Source Control** de VS Code.
- Interpretar los indicadores visuales de archivos nuevos, modificados, eliminados e ignorados.
- Entender el papel de `.gitignore` y comprobar qué archivos se ignoran y por qué.
- Relacionar las acciones visuales con `git init`, `git status` y `git check-ignore`.

## 3. Conceptos fundamentales

### Git y GitHub

| | Git | GitHub |
|---|---|---|
| Qué es | Programa de control de versiones que se instala en tu equipo | Servicio web que aloja repositorios Git |
| Dónde funciona | En local, sin conexión | En Internet |
| Para qué | Guardar versiones, crear ramas, combinar cambios | Compartir repositorios, colaborar (Pull Requests, revisiones, *issues*) |
| ¿Necesita cuenta? | No | Sí, para escribir en repositorios |

Git funciona perfectamente sin GitHub. En este ejercicio solo se usa Git en local. GitHub aparece en el ejercicio 2.4.

### Repositorio local y remoto

- **Repositorio local:** la carpeta del proyecto con una subcarpeta oculta `.git`, donde Git guarda todo el historial. Está en tu equipo.
- **Repositorio remoto:** otra copia del repositorio, normalmente en un servidor como GitHub, con la que se sincronizan los cambios (`push` y `pull`).

### Las tres zonas de Git

```text
  Directorio de trabajo        Área de preparación          Repositorio (.git)
  (working directory)          (staging area / index)       (historial de commits)

  Archivos que editas   ──add──▶  Cambios elegidos   ──commit──▶  Versión guardada
                                  para el siguiente
                                  commit
```

- **Directorio de trabajo:** los archivos tal como los ves y editas.
- **Área de preparación:** la lista de cambios que entrarán en el próximo commit. Permite elegir qué se confirma y qué no.
- **Repositorio:** el historial de commits guardado en `.git`.

### Commits y versiones

Un **commit** es una fotografía del proyecto en un momento dado, con:

- un identificador único (*hash*), por ejemplo `3f2a9c1`;
- el autor y la fecha;
- un mensaje que explica el cambio;
- una referencia al commit anterior.

La sucesión de commits forma el **historial** del proyecto.

### Archivos con y sin seguimiento

- **Sin seguimiento** (*untracked*): archivos que existen en la carpeta, pero que Git todavía no controla. Recién inicializado un repositorio, **todos** los archivos están así.
- **Con seguimiento** (*tracked*): archivos incluidos en algún commit o preparados para el siguiente.
- **Ignorados**: archivos que Git no debe controlar nunca, según las reglas de `.gitignore`.

### El papel de `.gitignore`

`.gitignore` es un archivo de texto con patrones de archivos y carpetas que Git debe ignorar. Se usa para lo que **no debe guardarse en el repositorio**:

- **Archivos generados:** `.class`, carpetas `out/`, `target/`, `bin/`. Se pueden volver a generar compilando y cambian en cada compilación.
- **Configuración personal del editor:** por ejemplo, `.vscode/` en este proyecto.
- **Archivos de sistema:** `Thumbs.db`, `.DS_Store`.
- **Secretos:** `.env` con contraseñas o *tokens*.

`.gitignore` sí se guarda en el repositorio, para que todo el equipo comparta las mismas reglas.

## 4. Preparación del entorno

### 4.1. Comprobar que Git está instalado

En una terminal (PowerShell o Bash):

```bash
git --version
```

Debe mostrar una versión, por ejemplo `git version 2.4x.x`. Si el comando no existe, instala Git:

- **Windows:** Git para Windows, desde git-scm.com.
- **Ubuntu o WSL:** `sudo apt install git`.

Si trabajas con VS Code conectado a WSL, Git debe estar instalado **dentro de WSL**. La instalación de Windows no sirve dentro de WSL, y al revés.

### 4.2. Actualizar el repositorio del curso

1. Abre VS Code en la **raíz del repositorio del curso**, la carpeta que contiene `modulo-02-git/`.
2. Abre la Terminal integrada (**Terminal > New Terminal**) y comprueba que no hay cambios pendientes:

   ```bash
   git status
   ```

   El resultado debe indicar *nothing to commit, working tree clean*.
3. Descarga las novedades:

   ```bash
   git pull --ff-only
   ```

Si `git status` muestra cambios, deshazlos antes de continuar, por ejemplo con `File: Revert File`, o consúltalo. No fuerces la actualización.

### 4.3. Copiar la plantilla a una carpeta de prácticas

Copia la plantilla **fuera del repositorio del curso**, en una carpeta `practicas-git` de tu carpeta personal. Ejecuta los comandos desde la raíz del repositorio del curso.

**Windows (PowerShell):**

```powershell
New-Item -ItemType Directory -Force "$HOME/practicas-git" | Out-Null
Copy-Item -Recurse modulo-02-git/tema-2.1-fundamentos/apartado-2.1.1-source-control/plantilla "$HOME/practicas-git/gestor-tareas"
Get-ChildItem -Force "$HOME/practicas-git/gestor-tareas"
```

**Linux o WSL (Bash):**

```bash
mkdir -p ~/practicas-git
cp -r modulo-02-git/tema-2.1-fundamentos/apartado-2.1.1-source-control/plantilla ~/practicas-git/gestor-tareas
ls -A ~/practicas-git/gestor-tareas
```

**Resultado esperado:** la carpeta `gestor-tareas` contiene `.gitignore`, `README.md` y `src`.

- **Si la carpeta de destino ya existe**, por ejemplo porque repites la práctica, bórrala antes o elige otro nombre (`gestor-tareas-2`). Si no, la copia quedaría anidada dentro de ella.
- **Con WSL**, ejecuta los comandos de Bash en la terminal de WSL. La carpeta quedará en tu carpeta personal de Linux (`~`), que es donde VS Code conectado a WSL trabaja mejor.

### 4.4. Abrir la carpeta de prácticas en otra ventana

Abre la carpeta en una **ventana nueva** de VS Code:

- **Windows (PowerShell):** `code "$HOME/practicas-git/gestor-tareas"`
- **Linux o WSL (Bash):** `code ~/practicas-git/gestor-tareas`
- O bien **File > New Window** y, en ella, **File > Open Folder...**.

Si VS Code pregunta si confías en los autores de la carpeta, responde que sí.

A partir de aquí, la **ventana de prácticas** es la del gestor de tareas. La ventana del repositorio del curso solo se usa para leer los README.

> No abras ni ejecutes los archivos de `plantilla/` desde la ventana del repositorio del curso. Trabaja siempre con tu copia.

## 5. La vista Source Control de VS Code

Se abre con el icono de ramas de la Activity Bar, con **View > Source Control** o con `Ctrl + Shift + G`.

| Situación | Lo que muestra |
|---|---|
| Carpeta sin repositorio Git | Los botones **Initialize Repository** y **Publish to GitHub** |
| Repositorio con cambios | Lista **Changes** (cambios sin preparar) y, si los hay, **Staged Changes** (preparados). Encima, el cuadro del mensaje y el botón **Commit**. |
| Historial | Sección **Source Control Graph** (o **Graph**), con los commits y las ramas |

En la **Status Bar**, a la izquierda, aparece la **rama actual** (por ejemplo, `main`) en cuanto hay un repositorio.

### Indicadores de estado

VS Code marca cada archivo con una letra y un color, tanto en Source Control como en el Explorer:

| Letra | Significado | Comando Git que lo muestra |
|---|---|---|
| **U** | *Untracked*: nuevo, sin seguimiento | `git status` → *Untracked files* |
| **A** | *Added*: nuevo y preparado para el commit | `git status` → *new file* |
| **M** | *Modified*: modificado | `git status` → *modified* |
| **D** | *Deleted*: eliminado | `git status` → *deleted* |
| **R** | *Renamed*: renombrado | `git status` → *renamed* |
| **!** | Conflicto: hay que resolverlo (ejercicio 2.3) | `git status` → *both modified* |
| Gris atenuado | Ignorado por `.gitignore` | `git status --ignored` |

Las carpetas heredan el color de su contenido: una carpeta con un archivo modificado se ve del color de "modificado".

## 6. Comandos Git equivalentes

| Acción en VS Code | Comando Git | Qué hace |
|---|---|---|
| **Initialize Repository** | `git init -b main` | Crea la carpeta `.git` y la rama `main` |
| Ver la vista Source Control | `git status` | Muestra rama, cambios preparados, sin preparar y sin seguimiento |
| Archivos atenuados en el Explorer | `git status --ignored` | Muestra también los ignorados |
| Pasar el ratón por un archivo atenuado | `git check-ignore -v <ruta>` | Indica qué regla de `.gitignore` ignora ese archivo |

VS Code crea siempre la rama `main` al inicializar, porque su preferencia `git.defaultBranchName` vale `main`. Al usar `git init` en la terminal, la rama depende de tu configuración de Git, que puede ser `master`. Por eso los comandos del curso usan `git init -b main`.

## 7. Práctica guiada

Antes de empezar, asegúrate de haber completado los pasos 4.2 a 4.4: repositorio del curso actualizado, plantilla copiada y carpeta de prácticas abierta en una ventana propia.

### Paso 1. Comprobar que la carpeta no es todavía un repositorio

En la ventana de prácticas, abre la Terminal integrada y ejecuta:

```bash
git status
```

**Resultado esperado:** `fatal: not a git repository (or any of the parent directories): .git`. Según el idioma de tu sistema, el mensaje puede salir en español.

Si `git status` muestra ramas y cambios, la carpeta está **dentro de otro repositorio**, probablemente el del curso. Cierra la ventana, borra la copia y vuelve a copiarla fuera, siguiendo el paso 4.3.

### Paso 2. Inicializar Git desde Source Control

1. Abre **Source Control** (`Ctrl + Shift + G`).
2. Pulsa **Initialize Repository**.

**Resultado esperado:**

- La vista muestra la lista **Changes** con `.gitignore`, `README.md` y los cuatro `.java`, todos con la letra **U**.
- La Status Bar muestra la rama `main`.
- En el Explorer, los archivos aparecen en verde con **U**.

Comando equivalente: `git init -b main`.

### Paso 3. Comprobar los archivos sin seguimiento

En la terminal:

```bash
git status
```

**Resultado esperado:**

```text
On branch main

No commits yet

Untracked files:
  (use "git add <file>..." to include in what will be committed)
	.gitignore
	README.md
	src/

nothing added to commit but untracked files present (use "git add" to track)
```

Git agrupa la carpeta `src/` porque todo su contenido está sin seguimiento. VS Code, en cambio, muestra cada archivo por separado. Todavía **no hay commits**: eso se hará en el ejercicio 2.2.

### Paso 4. Examinar `.gitignore`

Abre `.gitignore` y lee sus reglas:

```text
out/        → carpeta de compilados
bin/        → carpeta de compilados de otros entornos
*.class     → cualquier archivo compilado de Java
.vscode/    → configuración personal del editor
.DS_Store, Thumbs.db → archivos de sistema
.env        → variables de entorno locales (pueden contener secretos)
```

Las líneas que empiezan por `#` son comentarios.

### Paso 5. Compilar y ejecutar la aplicación

Desde la Terminal integrada de la ventana de prácticas, en la raíz del proyecto:

**Windows (PowerShell):**

```powershell
javac --release 21 -Xlint:all -d out (Get-ChildItem src/curso/git/*.java).FullName
java -cp out curso.git.Main
```

**Linux o WSL (Bash):**

```bash
javac --release 21 -Xlint:all -d out src/curso/git/*.java
java -cp out curso.git.Main
```

También puedes abrir `src/curso/git/Main.java` y pulsar **Run**.

**Resultado esperado:**

```text
== Gestor de tareas ==

Todas las tareas:
  #1 Instalar JDK 21 [PENDIENTE]
  #2 Configurar VS Code [PENDIENTE]
  #3 Clonar el repositorio [PENDIENTE]
  #4 Primer commit [PENDIENTE]

Cambio de estado de la tarea 99: no existe

Tareas después de los cambios:
  #1 Instalar JDK 21 [COMPLETADA]
  #2 Configurar VS Code [EN_PROGRESO]
  #3 Clonar el repositorio [PENDIENTE]
  #4 Primer commit [PENDIENTE]

Tareas pendientes:
  #3 Clonar el repositorio [PENDIENTE]
  #4 Primer commit [PENDIENTE]
```

### Paso 6. Comprobar qué archivos generados no deben subirse

1. En el Explorer aparece la carpeta `out/`, **atenuada** (gris). Pasa el ratón por encima: el aviso indica que está ignorada.
2. En **Source Control**, la lista **Changes** **no** ha cambiado: `out/` no aparece.
3. En la terminal:

   ```bash
   git status --ignored
   git check-ignore -v out/curso/git/Main.class
   ```

**Resultado esperado:**

- `git status --ignored` añade una sección *Ignored files* con `out/`.
- `git check-ignore -v` responde con la regla que se aplica, por ejemplo:

  ```text
  .gitignore:2:out/	out/curso/git/Main.class
  ```

  Es decir: archivo `.gitignore`, línea 2, patrón `out/`.

### Paso 7. Comprobar el efecto de `.gitignore` (temporal)

1. Crea en la raíz del proyecto un archivo `.env` con el contenido `CLAVE_DE_PRUEBA=no-es-real`.
2. Observa que aparece atenuado en el Explorer y **no** aparece en Source Control.
3. Ejecuta `git check-ignore -v .env`: indica la regla `.env` de `.gitignore`.
4. Crea también un archivo `notas.txt`: aparece con la letra **U**, porque ninguna regla lo ignora.
5. Borra `.env` y `notas.txt` (clic derecho > **Delete**).

**Resultado esperado:** los archivos que encajan con `.gitignore` nunca aparecen como cambios. Los demás sí.

### Paso 8. Relacionar lo visual con los comandos

Completa mentalmente esta correspondencia, comprobándola en tu pantalla:

| Lo que has visto en VS Code | Comando |
|---|---|
| Botón **Initialize Repository** | `git init -b main` |
| Archivos con **U** en Source Control | `git status` → *Untracked files* |
| `out/` y `.env` atenuados en el Explorer | `git status --ignored`, `git check-ignore -v` |
| `main` en la Status Bar | `git status` → *On branch main* |

### Al terminar

Deja la carpeta de prácticas tal como está: **repositorio inicializado, sin commits** y con la carpeta `out/`. El ejercicio 2.2 continúa desde aquí.

## 8. Errores frecuentes

| Problema | Causa | Solución |
|---|---|---|
| `git: command not found` / `'git' no se reconoce...` | Git no está instalado, o no lo está en ese entorno (Windows o WSL) | Instálalo, según el apartado 4.1 |
| Source Control no muestra **Initialize Repository**, sino una lista de cambios del curso | La copia está dentro del repositorio del curso | Borra la copia y cópiala en `~/practicas-git` (apartado 4.3) |
| VS Code pregunta si quieres abrir el repositorio de una carpeta superior | La carpeta de prácticas está dentro de otro repositorio | Responde que no y mueve la copia fuera |
| La copia quedó en `gestor-tareas/plantilla/` | La carpeta de destino ya existía | Borra `gestor-tareas` y repite la copia |
| No aparece `.gitignore` en la copia | En el Explorer de archivos de Windows o en `ls` sin `-A`, los archivos que empiezan por punto pueden no verse | Compruébalo con `Get-ChildItem -Force` o `ls -A`. En VS Code se ven siempre. |
| La rama se llama `master` | Repositorio creado con `git init` sin `-b main` | Command Palette > `Git: Rename Branch...` > `main`, o `git branch -m main` tras el primer commit |
| `out/` aparece como cambio | El `.gitignore` no se copió o se modificó | Comprueba que existe en la raíz y que contiene `out/` |

## 9. Comparativa con Eclipse e IntelliJ IDEA

| Aspecto | Eclipse (EGit) | IntelliJ IDEA | VS Code |
|---|---|---|---|
| Soporte de Git | Plugin EGit, incluido en las distribuciones habituales | Integrado | Integrado (extensión Git de serie) |
| Vista principal | **Git Staging** y **Git Repositories** | Ventana **Commit** (`Alt + 0`) y ventana **Git** (`Alt + 9`) | Vista **Source Control** (`Ctrl + Shift + G`) |
| Inicializar repositorio | **Team > Share Project... > Git** | **VCS > Create Git Repository...** | **Initialize Repository** en Source Control |
| Indicadores de archivos | Decoradores en el Package Explorer: `>` (modificado), `?` (sin seguimiento), iconos | Colores del nombre: verde (nuevo), azul (modificado), rojo (sin seguimiento), gris/oliva (ignorado) | Letras `U`, `A`, `M`, `D`, `R`, `!` y colores, en Source Control y en el Explorer |
| Rama actual | Junto al nombre del proyecto | Esquina inferior derecha, o superior en las versiones recientes | Status Bar, a la izquierda |
| Archivos ignorados | `.gitignore` y **Team > Ignore** | `.gitignore` y **Add to .gitignore** | `.gitignore`. Clic derecho en un archivo de Source Control > **Add to .gitignore**. |
| Terminal para comandos Git | Vista **Terminal** | Ventana **Terminal** | Terminal integrada, en la carpeta del repositorio |

La principal diferencia está en el área de preparación. VS Code y Eclipse (vista Git Staging) la muestran explícitamente, como listas **Changes** y **Staged Changes**. IntelliJ IDEA la oculta por defecto y trabaja con casillas en la ventana **Commit**.

## 10. Comprobaciones finales

Comprueba que eres capaz de:

- [ ] Explicar la diferencia entre Git y GitHub y entre repositorio local y remoto.
- [ ] Describir el directorio de trabajo, el área de preparación y el repositorio.
- [ ] Explicar por qué las prácticas se hacen en una copia fuera del repositorio del curso, y comprobar con `git status` que una carpeta no está dentro de otro repositorio.
- [ ] Inicializar un repositorio desde **Source Control** y saber que equivale a `git init -b main`.
- [ ] Interpretar las letras **U**, **A**, **M**, **D**, **R** y **!**, y reconocer un archivo ignorado.
- [ ] Explicar para qué sirve `.gitignore` y qué tipos de archivos deben ignorarse en un proyecto Java.
- [ ] Usar `git status`, `git status --ignored` y `git check-ignore -v`.
- [ ] Compilar y ejecutar el gestor de tareas y comprobar que `out/` no aparece como cambio.
