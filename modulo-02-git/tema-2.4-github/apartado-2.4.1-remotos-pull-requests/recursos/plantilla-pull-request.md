## Qué cambia

<!-- Describe el cambio en una o dos frases. -->

## Por qué

<!-- Motivo del cambio: qué problema resuelve o qué mejora aporta. -->

## Cómo probarlo

<!-- Pasos para comprobarlo. Por ejemplo: -->
1. Compilar: `javac --release 21 -Xlint:all -d out src/curso/git/*.java` (en PowerShell: `javac --release 21 -Xlint:all -d out (Get-ChildItem src/curso/git/*.java).FullName`).
2. Ejecutar: `java -cp out curso.git.Main`.
3. Comprobar que: <!-- resultado esperado -->

## Comprobaciones

- [ ] El programa compila con Java 21 sin avisos.
- [ ] La salida es la esperada.
- [ ] La Pull Request solo contiene los cambios de esta funcionalidad.
- [ ] No se incluyen archivos compilados (`out/`, `*.class`) ni configuración personal.
- [ ] No se incluyen contraseñas, tokens ni otros secretos.
