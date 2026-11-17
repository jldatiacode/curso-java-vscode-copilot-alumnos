# Ejercicio 2.5 — Práctica integradora

Módulo 2. Uso de VS Code con sistemas de control de versiones · Tema 2.5. Práctica integradora · Apartado 2.5.1. Flujo completo

Duración estimada: 30 minutos.

## 1. Introducción

Esta práctica reúne todo el módulo en un único flujo de trabajo, el mismo que seguirías en un equipo real para incorporar una mejora:

```text
main actualizado → rama de funcionalidad → cambio → revisión → commit → push → Pull Request → merge → main actualizado
```

Esta vez no hay instrucciones línea a línea. El enunciado indica **qué** hay que conseguir y tú decides **cómo** hacerlo con las herramientas de los ejercicios anteriores.

## 2. Objetivos

Demostrar que sabes, de forma autónoma:

- partir de un repositorio limpio y actualizado;
- trabajar en una rama de funcionalidad;
- implementar, compilar y probar un cambio Java pequeño;
- revisar y preparar solo los cambios necesarios;
- crear un commit con un mensaje descriptivo;
- publicar la rama, abrir una Pull Request, revisarla e integrarla;
- dejar `main` actualizada en local.

## 3. Conceptos fundamentales

No hay conceptos nuevos. Esta práctica aplica los de los ejercicios anteriores:

| Concepto | Ejercicio |
|---|---|
| Copia de la plantilla fuera del repositorio del curso, Source Control, `.gitignore` | 2.1 |
| Diff Editor, preparación selectiva, commits y mensajes | 2.2 |
| Ramas y merge | 2.3 |
| Remotos, push, Pull Requests y actualización local | 2.4 |

## 4. Preparación del entorno

Necesitas un repositorio local del Gestor de tareas **conectado a un remoto**, con `main` publicada y sin cambios pendientes.

- **Opción A, recomendada:** tu repositorio del ejercicio 2.4, conectado a tu repositorio `gestor-tareas` de GitHub.
- **Opción B:** si no lo tienes, prepara uno nuevo:
  1. copia la plantilla del apartado 2.1 y crea el commit inicial, como en el apartado 4.2 (opción B) del ejercicio 2.4, con la carpeta `gestor-tareas-2.5`;
  2. publícalo en un repositorio **nuevo y vacío** de tu cuenta de GitHub, por ejemplo `gestor-tareas-2.5`, como en el paso 2 del ejercicio 2.4;
  3. si no usas GitHub, conéctalo a un remoto *bare* local, como en el apartado 7.2 del ejercicio 2.4.

`recursos/lista-revision.md` contiene la lista de comprobaciones que debes repasar antes de integrar tu Pull Request.

## 5. Interfaz de VS Code y comandos

Usa las acciones de VS Code que ya conoces. Como referencia rápida:

| Necesitas | VS Code | Comando equivalente |
|---|---|---|
| Ver el estado | Source Control | `git status` |
| Actualizar `main` | Rama `main` en la Status Bar, y **Sync Changes** / `Git: Pull` | `git switch main` y `git pull` |
| Crear la rama | Rama de la Status Bar > **Create new branch...** | `git switch -c feature/tareas-completadas` |
| Revisar el cambio | Clic en el archivo en **Changes** (Diff Editor) | `git diff` |
| Preparar | **+** (**Stage Changes**) | `git add <archivo>` |
| Confirmar | Mensaje y **Commit** | `git commit -m "..."` |
| Publicar la rama | **Publish Branch** | `git push -u origin feature/tareas-completadas` |
| Abrir la PR | Web de GitHub, `Create PR` o extensión GitHub Pull Requests | — |
| Integrar la PR | **Merge pull request** en GitHub, o **Merge Pull Request** en la extensión | — |
| Actualizar `main` tras el merge | Cambiar a `main`, `Git: Fetch (Prune)` y **Sync Changes** | `git switch main`, `git fetch --prune` y `git pull` |

## 6. Enunciado

### Mejora que hay que implementar

**Añadir una operación que permita consultar las tareas completadas.**

Requisitos funcionales:

1. `GestorTareas` debe ofrecer un método público que devuelva la lista de tareas en estado `COMPLETADA`. Nombre recomendado: `tareasCompletadas()`. Debe seguir el estilo del método `tareasPendientes()` y llevar su comentario de documentación.
2. `Main` debe mostrar, **después** de la sección de tareas pendientes, una sección con el título `Tareas completadas`, usando el método `mostrarTareas` que ya existe.
3. El resto del comportamiento no cambia.

### Pasos que debes seguir

1. **Preparar el entorno.** Abre tu repositorio de prácticas en VS Code, en su propia ventana.
2. **Comprobar el estado de Git.** Debes estar en `main`, sin cambios pendientes y actualizado con el remoto. Haz pull si hace falta.
3. **Crear la rama** `feature/tareas-completadas` a partir de `main`.
4. **Implementar el cambio** en `GestorTareas.java` y `Main.java`.
5. **Compilar y ejecutar.** La compilación con `-Xlint:all` no debe mostrar avisos.

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

6. **Revisar las diferencias** de cada archivo en el Diff Editor. Solo debe haber líneas añadidas relacionadas con la mejora.
7. **Preparar únicamente** los archivos de la mejora. Comprueba que `out/` no aparece.
8. **Crear un commit** con un mensaje descriptivo, por ejemplo `Consultar las tareas completadas`.
9. **Publicar la rama** en tu repositorio remoto.
10. **Abrir una Pull Request** de `feature/tareas-completadas` hacia `main`. Usa como descripción la plantilla del ejercicio 2.4 o, al menos, explica qué cambia y cómo probarlo.
11. **Revisarla** con `recursos/lista-revision.md`, comprobando la pestaña **Files changed**, y **realizar el merge**.
12. **Actualizar `main`** en local y borrar la rama ya integrada.

Si usas un remoto *bare* local en lugar de GitHub, en los pasos 10 y 11 sustituye la Pull Request por la revisión con `git diff --stat main origin/feature/tareas-completadas` y el merge local descrito en el apartado 7.2 del ejercicio 2.4.

## 7. Resultados esperados

### Salida del programa

Si partes de la plantilla del apartado 2.1 sin otros cambios, la salida termina así:

```text
Tareas pendientes:
  #3 Clonar el repositorio [PENDIENTE]
  #4 Primer commit [PENDIENTE]

Tareas completadas:
  #1 Instalar JDK 21 [COMPLETADA]
```

Si tu repositorio incluye los cambios de ejercicios anteriores (formato alineado, descripción, título `v1.1`, tarea `#5`…), el formato de las líneas será el de tu versión. Lo importante es que aparezca la sección `Tareas completadas:` y que contenga **solo** la tarea `#1`, la única que el programa marca como completada.

### Estado de Git al terminar

- `git status` en `main`: *Your branch is up to date with 'origin/main'* y *nothing to commit, working tree clean*.
- `git log --oneline --graph` muestra tu commit y el merge de la Pull Request.
- En GitHub, la Pull Request aparece como **Merged** y su pestaña **Files changed** muestra solo `GestorTareas.java` y `Main.java`.

## 8. Criterios de aceptación

| Criterio | Cómo comprobarlo |
|---|---|
| El programa compila con Java 21 | `javac --release 21 -Xlint:all ...` sin errores ni avisos |
| La nueva funcionalidad muestra correctamente las tareas completadas | Sección `Tareas completadas:` con la tarea `#1` y ninguna otra |
| Los cambios se han hecho en una rama funcional | La PR parte de `feature/tareas-completadas` |
| El commit tiene un mensaje descriptivo | Pestaña **Commits** de la PR, o `git log --oneline` |
| No se han incluido archivos compilados | **Files changed** no contiene `out/` ni `.class`. `git ls-files` tampoco. |
| La PR contiene solo los cambios esperados | **Files changed**: `GestorTareas.java` y `Main.java`, solo con líneas añadidas para la mejora |
| `main` incorpora la mejora al terminar | En `main` local, actualizada, compila y muestra la sección nueva |

## 9. Resolución de errores frecuentes

| Problema | Solución |
|---|---|
| Has empezado a programar en `main` sin crear la rama | Si no has confirmado: crea la rama ahora (**Create new branch...**) y los cambios te acompañan. Si ya confirmaste en `main` y no hiciste push: **Undo Last Commit**, crea la rama y confirma allí. |
| La sección sale vacía | El método compara con el estado equivocado, o se llama antes de cambiar los estados. Revísalo con el Diff Editor. |
| La PR muestra más archivos de los esperados | Has confirmado cambios ajenos. Corrige en la rama con un commit nuevo y haz push: la PR se actualiza. |
| `rejected ... (fetch first)` al publicar | Alguien, o tú desde la web, cambió el remoto. Haz pull en la rama y vuelve a publicar. Sin `--force`. |
| Tras el merge, tu `main` local no tiene el cambio | Cambia a `main` y haz **Sync Changes** o `git pull` |

## 10. Comparativa con Eclipse e IntelliJ IDEA

El flujo es idéntico en los tres entornos; cambian los menús:

| Paso | Eclipse (EGit) | IntelliJ IDEA | VS Code |
|---|---|---|---|
| Actualizar `main` | **Team > Pull** | **Update Project** (`Ctrl + T`) | **Sync Changes** / `Git: Pull` |
| Crear rama | **Team > Switch To > New Branch...** | Widget de ramas > **New Branch** | Rama de la Status Bar > **Create new branch...** |
| Revisar y confirmar | **Git Staging** | Ventana **Commit** (`Ctrl + K`) | **Source Control** y Diff Editor |
| Publicar rama | **Team > Push Branch...** | **Push** (`Ctrl + Shift + K`) | **Publish Branch** |
| Pull Request | Web de GitHub | Ventana **Pull Requests** | Web, `Create PR` o extensión GitHub Pull Requests |

## 11. Comprobaciones finales

Comprueba que has sido capaz de:

- [ ] Empezar desde un `main` limpio y actualizado.
- [ ] Crear la rama `feature/tareas-completadas` y trabajar en ella.
- [ ] Implementar la consulta de tareas completadas siguiendo el estilo del código existente.
- [ ] Compilar sin avisos y obtener la salida esperada.
- [ ] Revisar las diferencias y preparar solo los archivos de la mejora.
- [ ] Crear un commit con un mensaje descriptivo.
- [ ] Publicar la rama, abrir una Pull Request y revisarla con la lista de `recursos/`.
- [ ] Integrar la Pull Request y actualizar `main` en local.
- [ ] Cumplir todos los criterios de aceptación del apartado 8.
