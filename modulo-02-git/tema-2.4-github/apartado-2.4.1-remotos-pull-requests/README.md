# Ejercicio 2.4 — GitHub, remotos y Pull Requests

Módulo 2. Uso de VS Code con sistemas de control de versiones · Tema 2.4. GitHub · Apartado 2.4.1. Remotos y Pull Requests

Duración estimada: 1 hora.

## 1. Introducción

Hasta ahora todo el trabajo ha estado en tu equipo. Para colaborar hace falta un **repositorio remoto** compartido. En este ejercicio:

- conectas el Gestor de tareas con un repositorio **tuyo** en GitHub;
- publicas ramas;
- propones cambios mediante una **Pull Request** (PR), los revisas y los integras;
- actualizas tu copia local.

Es el flujo de trabajo habitual en la mayoría de equipos de desarrollo.

> **Dos tipos de repositorio en este curso:**
> - El **repositorio de materiales del curso** es **solo de lectura** para ti: lo clonas y lo actualizas con `git pull --ff-only`. **Nunca** se hace `push` ni se abren Pull Requests contra él.
> - Tus **repositorios de prácticas** son tuyos: los creas en **tu cuenta** de GitHub y en ellos haces `push`, ramas y Pull Requests.

## 2. Objetivos

Al terminar este ejercicio serás capaz de:

- Explicar qué es un remoto, qué es `origin` y las diferencias entre HTTPS y SSH.
- Autenticarte en GitHub desde VS Code sin exponer credenciales.
- Publicar un repositorio y sus ramas desde VS Code.
- Usar **Fetch**, **Pull**, **Push** y **Sync**, e interpretar los indicadores de la Status Bar.
- Entender las ramas remotas y el *upstream*.
- Crear, revisar, comentar e integrar una Pull Request.
- Actualizar tu copia local tras la integración.

## 3. Conceptos fundamentales

### Remoto y `origin`

Un **remoto** es una copia del repositorio en otro lugar (GitHub, un servidor, otra carpeta), identificada por un **nombre** y una **URL**. `origin` es el nombre que se da por convención al remoto principal: el repositorio del que se clonó o en el que se publicó.

```text
        Tu equipo                                GitHub (origin)
  ┌──────────────────────┐   push  ─────▶   ┌───────────────────┐
  │ main                 │                  │ main              │
  │ feature/x            │   ◀───── fetch   │ feature/x         │
  │ origin/main (copia)  │          pull    └───────────────────┘
  └──────────────────────┘
```

### Fetch, Pull y Push

| Operación | Qué hace | ¿Modifica tus archivos? |
|---|---|---|
| **Fetch** | Descarga los commits nuevos del remoto y actualiza las **ramas remotas** (`origin/main`) | No |
| **Pull** | Fetch y, después, integra los cambios en tu rama actual | Sí |
| **Push** | Envía tus commits al remoto | No en local; actualiza el remoto |
| **Sync** (VS Code) | Pull y, después, Push | Sí |

### Ramas remotas y *upstream*

- **Rama remota.** `origin/main` es la copia local de lo que había en `main` del remoto la última vez que hiciste fetch. No se edita directamente.
- **Upstream.** Es la rama remota asociada a tu rama local. Con *upstream* configurado, `git pull` y `git push` saben con qué rama trabajar, y VS Code puede mostrar cuántos commits vas por delante o por detrás. Se configura la primera vez que publicas la rama (`git push -u`).

### HTTPS y SSH

| | HTTPS | SSH |
|---|---|---|
| URL | `https://github.com/usuario/gestor-tareas.git` | `git@github.com:usuario/gestor-tareas.git` |
| Autenticación | Inicio de sesión en GitHub desde VS Code o con el gestor de credenciales de Git. **No** se usa la contraseña de la cuenta. | Par de claves SSH. La clave pública se registra en GitHub. |
| Configuración | Ninguna previa | Generar y registrar las claves |
| En este curso | **Recomendado** | Opcional |

Para **leer** un repositorio público, como el de materiales del curso, no hace falta autenticarse. Para **escribir** (`push`) en tus repositorios, sí.

### Pull Request

Una **Pull Request** es una propuesta para integrar una rama en otra, normalmente una rama de funcionalidad en `main`, dentro de GitHub. Permite:

- ver las diferencias;
- comentar líneas concretas y revisar el código;
- ejecutar comprobaciones automáticas;
- integrar los cambios con un clic, cuando están aprobados.

Es la forma estándar de revisar código en equipo.

## 4. Preparación del entorno

### 4.1. Cuenta de GitHub

Necesitas una cuenta en github.com. Si no tienes, o no puedes usarla en clase, sigue la **alternativa local** del apartado 7.2, que reproduce todo salvo la Pull Request.

**Privacidad del correo:** el correo de `git config user.email` queda en cada commit. Si tus repositorios van a ser públicos, en GitHub > **Settings > Emails** activa **Keep my email addresses private** y usa como `user.email` la dirección `...@users.noreply.github.com` que GitHub te indica.

### 4.2. Punto de partida

Usa un repositorio local del Gestor de tareas **con al menos un commit en `main`**:

- **Opción A, recomendada:** tu repositorio del ejercicio 2.3 (`~/practicas-git/gestor-tareas-2.3`), con las ramas ya integradas en `main`. Comprueba con `git status` que no tiene cambios pendientes y que estás en `main`.
- **Opción B:** una copia nueva de la plantilla del apartado 2.1. Desde la raíz del repositorio del curso, después de actualizarlo con `git pull --ff-only`:

**Windows (PowerShell):**

```powershell
New-Item -ItemType Directory -Force "$HOME/practicas-git" | Out-Null
Copy-Item -Recurse modulo-02-git/tema-2.1-fundamentos/apartado-2.1.1-source-control/plantilla "$HOME/practicas-git/gestor-tareas-2.4"
Set-Location "$HOME/practicas-git/gestor-tareas-2.4"
git init -b main
git add .
git commit -m "Versión inicial del gestor de tareas"
code .
```

**Linux o WSL (Bash):**

```bash
mkdir -p ~/practicas-git
cp -r modulo-02-git/tema-2.1-fundamentos/apartado-2.1.1-source-control/plantilla ~/practicas-git/gestor-tareas-2.4
cd ~/practicas-git/gestor-tareas-2.4
git init -b main
git add .
git commit -m "Versión inicial del gestor de tareas"
code .
```

El repositorio local **no debe tener ningún remoto** todavía: `git remote -v` no debe mostrar nada. Si muestra el repositorio del curso, estás en la carpeta equivocada.

### 4.3. Extensión GitHub Pull Requests (opcional)

VS Code incluye de serie la integración básica con GitHub: iniciar sesión, publicar repositorios y abrir la página de creación de una PR. Para crear, revisar y fusionar PR **sin salir de VS Code**, existe la extensión **GitHub Pull Requests**, publicada por GitHub.

Instalarla es opcional: en la vista **Extensions**, busca `GitHub Pull Requests` y comprueba que el editor es **GitHub**. Todos los pasos de este ejercicio se pueden hacer también desde la web de GitHub.

### 4.4. Recursos del ejercicio

`recursos/plantilla-pull-request.md` es una plantilla para la descripción de las Pull Requests: qué cambia, por qué, cómo probarlo y una lista de comprobaciones. Copia su contenido al crear la PR.

## 5. La interfaz de VS Code

| Elemento | Uso |
|---|---|
| Botón **Publish Branch** en Source Control | Sin remoto: crea el repositorio en GitHub y publica la rama. Con remoto: publica la rama y configura su *upstream*. |
| Opciones **Publish to GitHub private repository** / **public repository** | Aparecen al publicar un repositorio sin remoto |
| Indicador de sincronización en la Status Bar (junto a la rama) | Muestra `↓n ↑m`: commits por recibir y por enviar. Clic: **Sync**. |
| Botón **Sync Changes** en Source Control | Hace pull y push |
| Command Palette > `Git: Fetch` / `Git: Pull` / `Git: Push` | Operaciones individuales |
| Command Palette > `Git: Fetch (Prune)` | Fetch que, además, elimina las ramas remotas borradas en el servidor |
| Command Palette > `Git: Add Remote...` | Añade un remoto con nombre y URL |
| Icono **Accounts** de la Activity Bar | Cuentas con sesión iniciada; permite cerrar la sesión de GitHub |
| Extensión GitHub Pull Requests (opcional) | Vista **GitHub** con las PR del repositorio, botón **Create Pull Request**, revisión de archivos y **Merge Pull Request** |

**Autenticación.** La primera vez que publiques, VS Code pedirá permiso para iniciar sesión en GitHub y abrirá el navegador para autorizarlo. No escribas tu contraseña de GitHub en VS Code ni en la terminal. En la Terminal integrada de VS Code, los comandos `git push` y `git pull` usan esa misma sesión, también con WSL.

## 6. Comandos Git equivalentes

| Acción en VS Code | Comando Git |
|---|---|
| Ver remotos | `git remote -v` |
| `Git: Add Remote...` | `git remote add origin <URL>` |
| **Publish Branch** (primera vez) | `git push -u origin <rama>` |
| **Push** | `git push` |
| **Fetch** | `git fetch` |
| **Fetch (Prune)** | `git fetch --prune` |
| **Pull** | `git pull` |
| **Sync** | `git pull` y `git push` |
| Ver ramas y su *upstream* | `git branch -vv` |
| Ver ramas remotas | `git branch -a` |
| Clonar un repositorio | `git clone <URL> <carpeta>` |

Crear el repositorio en GitHub y gestionar las Pull Requests no son operaciones de Git, sino de GitHub. Se hacen en la web, desde VS Code con la extensión o con la herramienta `gh` de GitHub, que no se usa en este curso.

## 7. Práctica guiada

### 7.1. Con GitHub

#### Paso 1. Acceder a GitHub

Abre github.com en el navegador e inicia sesión con tu cuenta.

#### Paso 2. Crear el repositorio remoto y conectarlo

Elige **una** de las dos formas.

**Forma A. Desde VS Code (recomendada).**

1. En la ventana de prácticas, abre **Source Control** y pulsa **Publish Branch**.
2. Elige **Publish to GitHub private repository**. Si VS Code te propone un nombre, cámbialo por `gestor-tareas`.
3. Autoriza a VS Code en el navegador si te lo pide.

VS Code crea el repositorio en tu cuenta, añade el remoto `origin` y publica `main`. Al terminar, ofrece abrirlo en GitHub.

**Forma B. Desde la web de GitHub.**

1. En github.com: **New repository**. Nombre: `gestor-tareas`. Visibilidad: **Private**. **No** añadas README, `.gitignore` ni licencia: el repositorio debe estar vacío. Pulsa **Create repository**.
2. Copia la URL HTTPS que muestra GitHub: `https://github.com/<tu-usuario>/gestor-tareas.git`.
3. En VS Code: Command Palette > `Git: Add Remote...`. Pega la URL y llama al remoto `origin`.
4. En Source Control, pulsa **Publish Branch**.

Comandos equivalentes de la forma B:

```bash
git remote add origin https://github.com/<tu-usuario>/gestor-tareas.git
git push -u origin main
```

**Resultado esperado:**

- `git remote -v` muestra `origin` con la URL de tu repositorio (dos líneas: *fetch* y *push*).
- En GitHub, el repositorio contiene `README.md`, `.gitignore` y `src/`, pero no `out/`.
- La Status Bar muestra la rama `main` sincronizada.

#### Paso 3. Publicar la rama principal y comprobar el upstream

Si usaste la forma A, `main` ya está publicada. Compruébalo:

```bash
git status
git branch -vv
```

**Resultado esperado:** `Your branch is up to date with 'origin/main'.` y, en `git branch -vv`, `main` aparece asociada a `[origin/main]`.

#### Paso 4. Crear una rama funcional

Desde `main`, crea la rama `feature/titulo-version`: rama de la Status Bar > **Create new branch...**

#### Paso 5. Realizar un cambio pequeño

En `src/curso/git/Main.java`, sustituye:

```java
        System.out.println("== Gestor de tareas ==");
```

por:

```java
        System.out.println("== Gestor de tareas v1.1 ==");
```

Guarda, compila y ejecuta para comprobarlo:

- **PowerShell:** `javac --release 21 -Xlint:all -d out (Get-ChildItem src/curso/git/*.java).FullName`, y después `java -cp out curso.git.Main`.
- **Bash:** `javac --release 21 -Xlint:all -d out src/curso/git/*.java`, y después `java -cp out curso.git.Main`.

**Resultado esperado:** la primera línea de la salida es `== Gestor de tareas v1.1 ==`.

#### Paso 6. Crear un commit

Prepara `Main.java` y confirma con el mensaje `Mostrar la versión en el título de la aplicación`.

#### Paso 7. Publicar la rama

En Source Control, pulsa **Publish Branch**, o el icono de nube junto a la rama en la Status Bar.

**Resultado esperado:** `git branch -vv` muestra `feature/titulo-version` asociada a `[origin/feature/titulo-version]`, y en GitHub aparece la nueva rama. Comando equivalente: `git push -u origin feature/titulo-version`.

#### Paso 8. Abrir una Pull Request

Elige una forma:

- **Desde la web de GitHub.** En tu repositorio aparece el aviso de que `feature/titulo-version` tiene cambios recientes. Pulsa **Compare & pull request**. Comprueba que la base es `main` y la rama comparada `feature/titulo-version`. Título: `Mostrar la versión en el título de la aplicación`. En la descripción, pega `recursos/plantilla-pull-request.md` y complétala. Pulsa **Create pull request**.
- **Desde VS Code, sin extensión.** Command Palette > `Create PR`. Abre en el navegador la página de creación de la PR. Continúa como en la forma web.
- **Desde VS Code, con la extensión GitHub Pull Requests.** En la vista **GitHub** (o en Source Control), usa **Create Pull Request**: elige `main` como base, escribe el título y la descripción y pulsa **Create**.

**Resultado esperado:** la PR aparece abierta en la pestaña **Pull requests** de tu repositorio.

#### Paso 9. Revisar las diferencias y comentar

1. En la PR, abre la pestaña **Files changed**. Solo debe aparecer `src/curso/git/Main.java`, con **una línea eliminada y una añadida**.
2. Pasa el ratón por la línea añadida, pulsa **+** y escribe un comentario de revisión, por ejemplo `Correcto: el título muestra la versión.` Pulsa **Add single comment**.
3. Con la extensión, puedes hacer lo mismo desde VS Code: abre la PR en la vista **GitHub** y comenta sobre las líneas del diff.

GitHub no permite **aprobar** una PR propia; sí comentarla. En un equipo, la revisaría y aprobaría otra persona (**Review changes > Approve**).

#### Paso 10. Integrar la Pull Request

1. En la pestaña **Conversation**, pulsa **Merge pull request** y después **Confirm merge**.
2. Pulsa **Delete branch** para borrar la rama del remoto, porque ya está integrada.

Con la extensión, el botón **Merge Pull Request** de la vista de la PR hace lo mismo.

**Resultado esperado:** la PR aparece como **Merged** y `main`, en GitHub, contiene el cambio y un *merge commit*.

#### Paso 11. Actualizar la copia local

1. En VS Code, cambia a `main` con la rama de la Status Bar.
2. Command Palette > `Git: Fetch (Prune)`. La Status Bar indica commits por recibir (`↓`).
3. Pulsa **Sync Changes**, o Command Palette > `Git: Pull`.
4. Borra la rama local ya integrada: `Git: Delete Branch...` > `feature/titulo-version`.

**Resultado esperado:**

- Tras el fetch, `git status` indica `Your branch is behind 'origin/main' by 2 commits, and can be fast-forwarded.`: el commit de tu rama y el de merge.
- Tras el pull, `main` contiene el cambio y `git log --oneline --graph` muestra el merge de la PR.
- `git branch -a` ya no muestra `feature/titulo-version` ni `origin/feature/titulo-version`.

Comandos equivalentes:

```bash
git switch main
git fetch --prune
git pull
git branch -d feature/titulo-version
```

### 7.2. Alternativa local sin GitHub (remoto *bare*)

Si no puedes usar GitHub, puedes practicar **push, fetch y pull** con un remoto que es solo una carpeta de tu equipo. Un repositorio *bare* no tiene archivos de trabajo, solo el historial: es lo mismo que guarda GitHub en sus servidores.

> **Esta alternativa no reproduce una Pull Request real.** No hay interfaz web, revisión ni comentarios. La integración que se hace aquí, un merge local seguido de push, es lo que GitHub hace internamente al pulsar **Merge pull request**.

Desde la carpeta de tu proyecto (por ejemplo, `gestor-tareas-2.4`):

**Windows (PowerShell):**

```powershell
git init --bare -b main "$HOME/practicas-git/remoto-gestor-tareas.git"
git remote add origin "$HOME/practicas-git/remoto-gestor-tareas.git"
git push -u origin main
git clone "$HOME/practicas-git/remoto-gestor-tareas.git" "$HOME/practicas-git/companero"
```

**Linux o WSL (Bash):**

```bash
git init --bare -b main ~/practicas-git/remoto-gestor-tareas.git
git remote add origin ~/practicas-git/remoto-gestor-tareas.git
git push -u origin main
git clone ~/practicas-git/remoto-gestor-tareas.git ~/practicas-git/companero
```

El `-b main` de `git init --bare` es imprescindible. Sin él, el remoto puede quedar apuntando a una rama `master` inexistente y el clon del compañero aparecería vacío.

`companero` simula la copia de otra persona. Ábrela en otra ventana de VS Code (`code` seguido de la ruta) y sigue estos pasos:

1. **Compañero:** en `src/curso/git/Main.java`, debajo de la línea `gestor.crearTarea("Primer commit", ...)`, añade:

   ```java
           gestor.crearTarea("Revisar el remoto", "Comprobar fetch y pull");
   ```

   Confirma con `Añadir una tarea para comprobar fetch y pull` y pulsa **Sync Changes** (o `git push`).
2. **Tú:** `Git: Fetch`. La Status Bar muestra `↓1` y `git status` indica `Your branch is behind 'origin/main' by 1 commit, and can be fast-forwarded.`. Pulsa **Sync Changes** (o `git pull`). Ahora tu `Main.java` tiene la nueva tarea.
3. **Tú:** sigue los pasos 4 a 7 del apartado 7.1: rama `feature/titulo-version`, cambio del título, commit y **Publish Branch**.
4. **Compañero (equivalente local a revisar e integrar la PR):**

   ```bash
   git fetch
   git diff --stat main origin/feature/titulo-version
   git merge --no-ff -m "Integrar feature/titulo-version" origin/feature/titulo-version
   git push
   git push origin --delete feature/titulo-version
   ```

   El `diff --stat` debe mostrar solo `src/curso/git/Main.java | 2 +-`.
5. **Tú:** sigue el paso 11 del apartado 7.1. `git fetch --prune` indica que vas 2 commits por detrás y que la rama remota se ha borrado. Tras el pull, el título es `== Gestor de tareas v1.1 ==` y la lista incluye la tarea `#5 Revisar el remoto`.

Al terminar puedes borrar las carpetas `remoto-gestor-tareas.git` y `companero`.

## 8. Resolución de errores frecuentes

| Problema | Causa | Solución |
|---|---|---|
| VS Code pide una contraseña o un *token* en la terminal | La terminal no usa la sesión de VS Code (terminal externa o configuración desactivada) | Haz el push desde Source Control, o desde la Terminal integrada de VS Code. **No** pegues la contraseña de GitHub: no se acepta para Git. |
| `remote origin already exists` | Ya hay un remoto con ese nombre | Revisa `git remote -v`. Si apunta a otro sitio, cámbialo con `git remote set-url origin <URL>`. |
| `git remote -v` muestra el repositorio del curso | Estás en la carpeta del curso, no en la de prácticas | Cierra esa ventana y abre tu carpeta de prácticas. **No hagas push** al repositorio del curso. |
| `rejected ... (fetch first)` al hacer push | El remoto tiene commits que no tienes | Haz **Pull** (o **Sync**) y vuelve a hacer push. **Nunca** uses `git push --force`. |
| `Updates were rejected because the remote contains work...` al publicar | El repositorio de GitHub no estaba vacío (se creó con README) | Bórralo y créalo vacío, o haz `git pull origin main --allow-unrelated-histories`, resuelve y publica |
| La PR muestra archivos inesperados | Se confirmaron cambios ajenos a la funcionalidad | Corrige en la rama con un nuevo commit y vuelve a hacer push: la PR se actualiza sola |
| No aparece **Compare & pull request** | El aviso caduca pasado un tiempo | Pestaña **Pull requests** > **New pull request** y elige las ramas |
| El clon del compañero está vacío (alternativa local) | El remoto *bare* se creó sin `-b main` | Bórralo y créalo de nuevo con `git init --bare -b main` |

## 9. Seguridad

- **No escribas** contraseñas ni *tokens* en el código, en los README, en los mensajes de commit ni en la terminal compartida.
- **No compartas** tus credenciales. La sesión de GitHub en VS Code se gestiona con la autorización del navegador y se puede cerrar desde **Accounts**.
- Si un secreto llega a un repositorio, **revócalo** en el servicio correspondiente: borrarlo del repositorio no basta, porque queda en el historial.
- **No uses `git push --force`**: reescribe el historial remoto y puede borrar trabajo de otras personas.
- Mantén privados los repositorios de prácticas si no necesitas compartirlos.

## 10. Comparativa con Eclipse e IntelliJ IDEA

| Acción | Eclipse (EGit) | IntelliJ IDEA | VS Code |
|---|---|---|---|
| Configurar remotos | **Team > Remote > Configure Push/Fetch** | **Git > Manage Remotes...** | `Git: Add Remote...`, o **Publish Branch** |
| Publicar en GitHub | Crear el repositorio en la web y hacer push | **Git > GitHub > Share Project on GitHub** | **Publish to GitHub** |
| Fetch / Pull / Push | **Team > Fetch / Pull / Push** | **Fetch**, **Update Project** (`Ctrl + T`), **Push** (`Ctrl + Shift + K`) | **Fetch**, **Pull**, **Push**, **Sync Changes** |
| Estado respecto al remoto | Flechas en el nombre del proyecto (`↑1 ↓2`) | Widget de ramas con flechas | `↓n ↑m` en la Status Bar |
| Autenticación | Usuario y *token* en el diálogo de push, o claves SSH | Inicio de sesión en GitHub desde **Settings > Version Control > GitHub** | Inicio de sesión en GitHub desde el navegador (Accounts) |
| Pull Requests | No integradas de serie: web de GitHub | Ventana **Pull Requests**: crear, revisar y fusionar | Web de GitHub, `Create PR`, o extensión **GitHub Pull Requests** |

## 11. Comprobaciones finales

Comprueba que eres capaz de:

- [ ] Explicar qué es un remoto, qué representa `origin` y la diferencia entre `origin/main` y `main`.
- [ ] Explicar las diferencias entre HTTPS y SSH y autenticarte en GitHub desde VS Code sin escribir contraseñas.
- [ ] Publicar un repositorio local en un repositorio propio de GitHub y comprobarlo con `git remote -v`.
- [ ] Publicar una rama y explicar qué es el *upstream* (`git branch -vv`).
- [ ] Explicar la diferencia entre Fetch, Pull, Push y Sync, e interpretar `↓n ↑m`.
- [ ] Crear una Pull Request, revisar sus archivos, comentar una línea e integrarla.
- [ ] Actualizar `main` en local tras integrar una PR y borrar la rama ya integrada.
- [ ] Diferenciar el repositorio de materiales del curso, de solo lectura, de tus repositorios de prácticas.
- [ ] Si no usas GitHub: practicar push, fetch y pull con un remoto *bare* local.
