# Lista de revisión de la práctica integradora

Repasa esta lista antes de integrar tu Pull Request.

## Rama y commits

- [ ] El trabajo está en la rama `feature/tareas-completadas`, creada desde `main` actualizado.
- [ ] Cada commit tiene un mensaje descriptivo que explica el objetivo del cambio.
- [ ] No hay commits con mensajes genéricos como `cambios`, `prueba` o `wip`.

## Código

- [ ] El programa compila con `javac --release 21 -Xlint:all` sin errores ni avisos.
- [ ] La salida muestra una sección `Tareas completadas:` con las tareas en estado `COMPLETADA`.
- [ ] Las secciones anteriores de la salida siguen apareciendo igual que antes.
- [ ] El código nuevo sigue el estilo del existente: nombres en español, comentario de documentación del método nuevo.

## Contenido de la Pull Request

- [ ] La pestaña **Files changed** muestra solo los archivos necesarios.
- [ ] No se incluyen `out/`, archivos `.class` ni configuración personal (`.vscode/`).
- [ ] No se incluyen contraseñas, tokens ni otros secretos.
- [ ] La descripción explica qué cambia, por qué y cómo probarlo.

## Después del merge

- [ ] La Pull Request aparece como **Merged**.
- [ ] Tu `main` local está actualizada (`git status`: *up to date with 'origin/main'*).
- [ ] Al compilar y ejecutar desde `main`, aparece la sección `Tareas completadas:`.
