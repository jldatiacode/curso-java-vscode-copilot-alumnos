# Gestor de tareas

Aplicación Java de consola para practicar Git y GitHub desde VS Code.

## Estructura

```text
.
├── README.md
├── .gitignore
└── src/curso/git/
    ├── EstadoTarea.java    Estados: PENDIENTE, EN_PROGRESO, COMPLETADA
    ├── Tarea.java          Identificador, título, descripción y estado
    ├── GestorTareas.java   Crear, listar, buscar, cambiar estado y consultar pendientes
    └── Main.java           Demostración desde la consola
```

## Compilar y ejecutar

Desde la carpeta raíz de este proyecto (la que contiene este `README.md`).

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

Desde VS Code: abre `src/curso/git/Main.java` y pulsa **Run** sobre el método `main`.

La carpeta `out/` contiene archivos generados. Está excluida en `.gitignore` y nunca debe subirse al repositorio.
