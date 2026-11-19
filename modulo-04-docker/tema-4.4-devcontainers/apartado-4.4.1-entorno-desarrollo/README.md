# Ejercicio 4.4 — Desarrollo con Dev Containers

Módulo 4. Contenerización en VS Code · Tema 4.4. Dev Containers · Apartado 4.4.1. Entorno de desarrollo

Duración estimada: 1 hora y 30 minutos.

## 1. Introducción

Incorporarse a un proyecto Java suele empezar por instalar la versión correcta del JDK, Maven, las extensiones del IDE y la configuración del equipo. Cuando cada desarrollador tiene versiones distintas, aparecen los problemas de «en mi máquina funciona».

Un **Dev Container** (contenedor de desarrollo) resuelve esto: el proyecto incluye, en la carpeta `.devcontainer/`, la descripción de su entorno de desarrollo. VS Code construye un contenedor con ese entorno y **se conecta a él**:

- el editor sigue en tu equipo;
- el terminal, la compilación, las pruebas, la ejecución y el depurador funcionan **dentro del contenedor**.

En este apartado abrirás la API de tareas en un Dev Container con Java 21 y Maven, y trabajarás como en el módulo 3: pruebas, ejecución, depuración y recarga con DevTools. Nada de eso necesitará Java ni Maven en tu equipo.

## 2. Objetivos

Al terminar serás capaz de:

- Diferenciar un contenedor de desarrollo de un contenedor de ejecución.
- Leer y explicar `.devcontainer/devcontainer.json` y su Dockerfile.
- Abrir un proyecto con **Reopen in Container** y saber qué ocurre en cada fase.
- Distinguir las extensiones que se ejecutan en tu equipo de las que se ejecutan en el contenedor.
- Ejecutar Maven y JUnit, arrancar Spring Boot y depurar una petición dentro del contenedor.
- Acceder desde tu navegador al puerto reenviado.
- Explicar dónde está el código fuente y qué se pierde al reconstruir el contenedor.
- Diferenciar **Reopen**, **Rebuild** y **Rebuild Without Cache**.

## 3. Conceptos fundamentales

### Contenedor de desarrollo y contenedor de ejecución

| | Contenedor de **ejecución** (apartados 4.2 y 4.3) | Contenedor de **desarrollo** (este apartado) |
|---|---|---|
| Para qué | Ejecutar la aplicación ya compilada | Programar, compilar, probar y depurar |
| Contenido | JRE y el JAR | JDK completo, Maven, Git, extensiones de VS Code |
| Código fuente | **No** está | Está **montado** desde tu equipo |
| Usuario | `app`, sin privilegios | `vscode`, sin privilegios |
| Tamaño | Lo más pequeño posible (unos 120 MB) | Grande (unos 700 MB de descarga) |
| Vida | Se recrea con cada versión | Se mantiene mientras trabajas en el proyecto |
| Se describe en | `Dockerfile` | `.devcontainer/devcontainer.json` y su Dockerfile |
| Lo usa | Servidores, Compose, otros equipos | Los desarrolladores del proyecto |

Son complementarios: un proyecto puede tener los dos, como en el apartado 4.5.

### Cómo funciona

```text
Tu equipo                                     Contenedor de desarrollo
┌──────────────────────────┐                  ┌──────────────────────────────────────┐
│ VS Code (interfaz)        │  <───────────>   │ VS Code Server                        │
│  extensión Dev Containers │                  │  extensiones Java y Spring            │
│  temas, atajos            │                  │  JDK 21, Maven, terminal, depurador   │
│                           │                  │                                       │
│ carpeta devcontainer-4.4 ─┼── montaje ─────> │ /workspaces/devcontainer-4.4          │
│ localhost:8080 <──────────┼── reenvío ────── │ Spring Boot en el puerto 8080         │
└──────────────────────────┘                  └──────────────────────────────────────┘
```

1. **Reopen in Container** lee `devcontainer.json`, construye la imagen y crea el contenedor.
2. Monta la carpeta del proyecto en `/workspaces/<carpeta>`: los archivos **siguen en tu disco**.
3. Instala en el contenedor un componente de VS Code (VS Code Server) y las extensiones indicadas.
4. Ejecuta `postCreateCommand`.
5. La ventana de VS Code queda conectada al contenedor. La esquina inferior izquierda lo indica.

### Qué necesitas y qué no

| En tu equipo | ¿Hace falta? | Por qué |
|---|---|---|
| Docker | **Sí** | Construye y ejecuta el contenedor |
| VS Code y la extensión **Dev Containers** | **Sí** | Es la interfaz y la que gestiona el contenedor |
| JDK 21 | No | Está en la imagen de desarrollo |
| Maven | No | Está en la imagen de desarrollo |
| Extensiones de Java y Spring | No | Se instalan dentro del contenedor |

### Extensiones locales y remotas

Una ventana conectada a un contenedor usa extensiones de dos lugares:

- **En tu equipo (UI)**: las que solo afectan a la interfaz, como temas, iconos o mapas de teclado, y la propia extensión Dev Containers.
- **En el contenedor (workspace)**: las que trabajan con el código y las herramientas, como el soporte de Java, el depurador, Maven, Spring Boot o Git. Se instalan en el contenedor la primera vez.

En la vista **Extensions** aparecen secciones separadas: **LOCAL - INSTALLED** y **DEV CONTAINER: ... - INSTALLED**. Las de `customizations.vscode.extensions` se instalan solas. Otras extensiones que tengas en tu equipo muestran el botón **Install in Dev Container**.

### `devcontainer.json`

```jsonc
{
    "name": "Java 21 y Maven - Curso VS Code",
    "build": { "dockerfile": "Dockerfile" },
    "containerEnv": { "APP_MENSAJE": "API de tareas en el contenedor de desarrollo" },
    "customizations": {
        "vscode": {
            "extensions": [
                "redhat.java",
                "vscjava.vscode-java-debug",
                "vscjava.vscode-java-test",
                "vscjava.vscode-maven",
                "vscjava.vscode-java-dependency",
                "vmware.vscode-boot-dev-pack"
            ]
        }
    },
    "forwardPorts": [8080],
    "portsAttributes": { "8080": { "label": "API de tareas", "onAutoForward": "notify" } },
    "postCreateCommand": "java -version && mvn -version",
    "remoteUser": "vscode"
}
```

| Propiedad | Significado |
|---|---|
| `name` | Nombre del entorno, que aparece en la barra de estado |
| `image` o `build` | La imagen del entorno: una ya publicada (`image`) o construida con un Dockerfile (`build`) |
| `features` | Componentes adicionales que se instalan sobre la imagen (ver más abajo). Este proyecto no los usa. |
| `containerEnv` | Variables de entorno del contenedor. **Nunca** contraseñas ni *tokens*. |
| `customizations.vscode.extensions` | Extensiones que se instalan **dentro** del contenedor, por su identificador |
| `customizations.vscode.settings` | Configuración de VS Code para este entorno. No se usa aquí. |
| `forwardPorts` | Puertos del contenedor que VS Code reenvía a `localhost` de tu equipo |
| `portsAttributes` | Etiqueta y comportamiento de cada puerto en la vista **Ports** |
| `postCreateCommand` | Comando que se ejecuta **una vez**, al crear el contenedor |
| `remoteUser` | Usuario con el que trabaja VS Code en el contenedor |
| `workspaceFolder` | Carpeta del contenedor donde se monta el proyecto. Por defecto, `/workspaces/<nombre de la carpeta>`. |

Los identificadores de las extensiones se ven en la vista **Extensions**: clic en la extensión, junto a su nombre. También se puede añadir una desde su menú con **Add to devcontainer.json**.

`devcontainer.json` admite comentarios (`//`) aunque tenga extensión `.json`: es JSON con comentarios (JSONC). Es una especificación abierta (*Development Containers*, containers.dev), que también usan otras herramientas como GitHub Codespaces o IntelliJ IDEA.

### Imagen de desarrollo y Dockerfile de desarrollo

`.devcontainer/Dockerfile`:

```dockerfile
FROM mcr.microsoft.com/devcontainers/java:3-21-trixie

RUN apt-get update \
    && export DEBIAN_FRONTEND=noninteractive \
    && apt-get install -y --no-install-recommends maven \
    && rm -rf /var/lib/apt/lists/*
```

- **`mcr.microsoft.com/devcontainers/java:3-21-trixie`**: imagen oficial del proyecto Dev Containers, publicada por Microsoft. Incluye Microsoft Build of OpenJDK 21, el usuario `vscode`, Git y utilidades. La etiqueta fija la versión 3 de la imagen, Java 21 y Debian 13 (*trixie*).
- **Maven** no viene en la imagen. Se instala con el paquete **firmado** de Debian: Maven 3.9.9, que usa el JDK 21 de la imagen.

Si el proyecto no necesitara nada más que la imagen, bastaría con `"image": "mcr.microsoft.com/devcontainers/java:3-21-trixie"` en lugar de `build`.

### *Features*

Una *feature* es un componente reutilizable que se añade a la imagen desde `devcontainer.json`, sin escribir un Dockerfile. Por ejemplo, la *feature* oficial de Java puede instalar Maven:

```jsonc
"features": {
    "ghcr.io/devcontainers/features/java:1": {
        "version": "none",
        "installMaven": "true"
    }
}
```

Son cómodas, pero cada *feature* es código que se descarga y se **ejecuta al construir** el contenedor. La de Java, en concreto, descarga e instala SDKMAN desde Internet. Antes de usar una *feature*, revisa quién la publica y qué ejecuta. En este proyecto se ha preferido un Dockerfile de dos líneas con un paquete firmado de Debian, más fácil de auditar.

### Ciclo de vida

| Momento | Propiedad | Se ejecuta |
|---|---|---|
| Antes de crear, en **tu equipo** | `initializeCommand` | Cada vez que se abre |
| Al crear el contenedor | `onCreateCommand`, `postCreateCommand` | **Una vez** por contenedor |
| Al arrancar el contenedor | `postStartCommand` | Cada arranque |
| Al conectar VS Code | `postAttachCommand` | Cada conexión |

### Reopen, Rebuild y Rebuild Without Cache

| Comando | Qué hace | Cuándo |
|---|---|---|
| **Dev Containers: Reopen in Container** | Abre la carpeta en su contenedor. Si ya existe, lo **reutiliza**: arranque en segundos. | Uso diario |
| **Dev Containers: Reopen Folder Locally** | Vuelve a abrir la carpeta en tu equipo. El contenedor queda detenido. | Al terminar |
| **Dev Containers: Rebuild Container** | Reconstruye la imagen, usando la caché de Docker, y crea un contenedor **nuevo** | Tras cambiar `devcontainer.json` o el Dockerfile |
| **Dev Containers: Rebuild Container Without Cache** | Igual, pero sin caché: lo descarga e instala todo de nuevo | Para obtener actualizaciones o si algo se ha corrompido |

Al reconstruir se pierde **lo que estaba dentro del contenedor**: paquetes instalados a mano, el repositorio de Maven (`~/.m2`), el historial de la terminal. **No** se pierde el código, porque está en tu disco.

### Windows, Linux y WSL

| Entorno | Dónde está el proyecto | Observaciones |
|---|---|---|
| **Linux** | Disco local | El montaje es directo y rápido. VS Code ajusta el UID del usuario `vscode` al tuyo, de modo que los archivos creados en el contenedor, como `target/`, son tuyos. |
| **Windows con Docker Desktop**, VS Code en Windows | Disco de Windows (`C:\Users\...`) | Funciona, pero el acceso a los archivos desde el contenedor Linux es más lento: Maven compila más despacio que en local. |
| **Windows con WSL**, VS Code conectado a WSL | Sistema de archivos de WSL (`~/...`) | **Recomendado en Windows**: el montaje es tan rápido como en Linux. Ejecuta **Reopen in Container** desde la ventana de VS Code conectada a WSL. |

En todos los casos el contenedor es **Linux**: rutas con `/` y sensibilidad a mayúsculas en los nombres de archivo.

### Si Docker no está disponible

**Reopen in Container** falla con un aviso como **Docker returned an error. Make sure the Docker daemon is running...** o, si no encuentra el programa, **The 'docker' command was not found**. Arranca Docker Desktop, o el servicio de Docker en Linux, y repite. La carpeta se puede seguir abriendo en local con **Reopen Folder Locally**.

## 4. Preparación del entorno

### 4.1. Requisitos

- Docker funcionando, como en el apartado 4.1.
- VS Code con la extensión **Dev Containers** (`ms-vscode-remote.remote-containers`, editor **Microsoft**). Compruébalo en la vista **Extensions**.
- Si trabajas en Windows con WSL, la extensión **WSL**, y VS Code conectado a WSL.
- Unos 3 GB libres en disco y conexión a Internet la primera vez: la imagen de desarrollo ocupa unos 2,3 GB una vez descomprimida.
- **No** necesitas Java ni Maven en tu equipo para este apartado.

### 4.2. Actualizar el material y copiar el proyecto

En la raíz de tu repositorio del curso, con `git status` sin cambios pendientes:

```bash
git status
git pull --ff-only
```

**Windows (PowerShell):**

```powershell
New-Item -ItemType Directory -Force "$HOME/practicas-docker" | Out-Null
Copy-Item -Recurse modulo-04-docker/tema-4.4-devcontainers/apartado-4.4.1-entorno-desarrollo/proyecto "$HOME/practicas-docker/devcontainer-4.4"
code "$HOME/practicas-docker/devcontainer-4.4"
```

**Linux o WSL (Bash):**

```bash
mkdir -p ~/practicas-docker
cp -r modulo-04-docker/tema-4.4-devcontainers/apartado-4.4.1-entorno-desarrollo/proyecto ~/practicas-docker/devcontainer-4.4
code ~/practicas-docker/devcontainer-4.4
```

Abre siempre la carpeta `devcontainer-4.4` como raíz de VS Code: **Reopen in Container** busca `.devcontainer/` en la carpeta abierta.

### 4.3. Puerto

La aplicación escucha en el 8080 dentro del contenedor y VS Code lo reenvía al 8080 de tu equipo. Si el 8080 está ocupado en tu equipo, por ejemplo por la API del apartado 3.5, VS Code elige otro puerto local automáticamente y lo indica en la vista **Ports**.

## 5. Herramientas de VS Code

| Necesitas | Herramienta |
|---|---|
| Abrir en el contenedor | Notificación **Reopen in Container**, o `Dev Containers: Reopen in Container` |
| Saber dónde estás | Indicador de la esquina inferior izquierda: `Dev Container: Java 21 y Maven - Curso VS Code` |
| Menú de conexión | Clic en ese indicador: **Reopen Folder Locally**, **Rebuild Container**… |
| Ver el registro de creación | Enlace **show log** de la notificación de progreso |
| Puertos reenviados | Vista **Ports** (panel inferior) |
| Extensiones del contenedor | Vista **Extensions**, sección **DEV CONTAINER** |
| Pruebas, ejecución y depuración | Igual que en el módulo 3: **Testing**, **Spring Boot Dashboard**, **Run and Debug** |

## 6. Ejemplos de configuración

El proyecto contiene:

```text
devcontainer-4.4/
├── .devcontainer/
│   ├── devcontainer.json   Entorno de desarrollo (explicado en el apartado 3)
│   └── Dockerfile          Imagen de desarrollo: Java 21 + Maven
├── peticiones/tareas.http
├── pom.xml                 La API de tareas, con DevTools
└── src/
```

No hay `Dockerfile` en la raíz: en este apartado no se construye la imagen de ejecución. En el apartado 4.5 conviven los dos.

## 7. Práctica guiada

### Paso 1. Comprobar Docker

En una terminal de tu equipo:

```bash
docker info
```

Debe mostrar el bloque **Server** sin errores.

### Paso 2. Comprobar la extensión Dev Containers

En la vista **Extensions**, busca `Dev Containers` y comprueba que está instalada y que el editor es **Microsoft**. Abre la Command Palette y escribe `Dev Containers:` para ver sus comandos.

### Paso 3. Abrir el proyecto

Abre la carpeta `devcontainer-4.4` en VS Code (punto 4.2). Aparece una notificación, **Folder contains a Dev Container configuration file.**, con el botón **Reopen in Container**.

Todavía no lo pulses.

### Paso 4. Examinar la configuración

Abre `.devcontainer/devcontainer.json` y `.devcontainer/Dockerfile` y relaciona cada propiedad con la tabla del apartado 3. Responde:

- ¿De qué imagen parte el entorno y qué se le añade?
- ¿Qué extensiones tendrás dentro del contenedor?
- ¿Qué comando se ejecutará al crearlo?

### Paso 5. Reabrir en el contenedor

Pulsa **Reopen in Container** en la notificación, o ejecuta `Dev Containers: Reopen in Container` desde la Command Palette.

### Paso 6. Esperar a que se prepare el entorno

La primera vez tarda varios minutos: descarga la imagen base (unos 700 MB), instala Maven, crea el contenedor e instala las extensiones.

Pulsa **show log** en la notificación de progreso (**Setting up Dev Containers**, **Connecting to Dev Container**…) para seguir el proceso en una terminal. Al final verás la salida de `postCreateCommand`:

```text
Running the postCreateCommand from devcontainer.json...
openjdk version "21.0.13.0.2" 2026-10-22 LTS
...
Apache Maven 3.9.9
```

**Resultado esperado:** la esquina inferior izquierda muestra `Dev Container: Java 21 y Maven - Curso VS Code`. El **Explorer** muestra los mismos archivos que antes.

### Paso 7. Comprobar Java 21

Abre una terminal (`` Ctrl + ` ``). Es una terminal **del contenedor**, aunque estés en Windows:

```bash
whoami
pwd
java -version
```

**Resultado esperado:**

```text
vscode
/workspaces/devcontainer-4.4
openjdk version "21.0.13.0.2" 2026-10-22 LTS
OpenJDK Runtime Environment Microsoft-... (build 21.0.13.0.2+1-LTS)
```

Es la distribución de Microsoft de OpenJDK, la de la imagen, no la que tengas en tu equipo.

### Paso 8. Comprobar Maven

```bash
mvn -version
echo $APP_MENSAJE
```

**Resultado esperado:** `Apache Maven 3.9.9` con `Java version: 21.0.13.0.2, vendor: Microsoft`, y el valor de `containerEnv`: `API de tareas en el contenedor de desarrollo`.

### Paso 9. Examinar las extensiones

Abre la vista **Extensions**:

- **LOCAL - INSTALLED**: las de tu equipo, como Dev Containers o tu tema de colores.
- **DEV CONTAINER: JAVA 21 Y MAVEN - CURSO VS CODE - INSTALLED**: las de `devcontainer.json`, ejecutándose en el contenedor.

Abre `TareaController.java`: el autocompletado, la navegación (`F12`) y los errores funcionan porque **Language Support for Java** se ejecuta dentro del contenedor.

### Paso 10. Ejecutar las pruebas

En la terminal del contenedor:

```bash
mvn test
```

O en la vista **Testing** > **Run Tests**.

**Resultado esperado:** la primera vez descarga las dependencias en el repositorio de Maven del contenedor. Después:

```text
[INFO] Tests run: 13, Failures: 0, Errors: 0, Skipped: 0
[INFO] BUILD SUCCESS
```

### Paso 11. Arrancar Spring Boot

Elige una forma:

- **Spring Boot Dashboard** > `tareas-devcontainer` > **Start**;
- **Run** sobre el `main` de `AplicacionTareas`;
- en la terminal, `mvn spring-boot:run`.

**Resultado esperado:** en el registro aparecen `Tomcat started on port 8080` y `Started AplicacionTareas` en el hilo `restartedMain`, porque DevTools está activo. VS Code muestra la notificación **Your application running on port 8080 is available** y el puerto aparece en la vista **Ports** con la etiqueta **API de tareas**.

### Paso 12. Acceder al puerto reenviado

Abre en el navegador de tu equipo `http://localhost:8080/api/estado`, o pulsa el icono del globo en la vista **Ports**.

**Resultado esperado:**

```json
{"estado":"OK","mensaje":"API de tareas en el contenedor de desarrollo","tareas":3,"host":"3b32f57244df","java":"21.0.13.0.2"}
```

`host` es el identificador del contenedor de desarrollo y `java` la versión de la imagen: tu navegador está hablando con una aplicación que se ejecuta dentro del contenedor.

Si la vista **Ports** indica otro puerto local, usa ese en la URL y en `peticiones/tareas.http`.

### Paso 13. Colocar un breakpoint

1. Detén la aplicación: **Stop** en el Dashboard, o `Ctrl + C` en su terminal.
2. Abre `controller/TareaController.java` y pon un *breakpoint* en la primera línea del método `consultar`.

### Paso 14. Depurar una petición

1. Arranca en modo depuración: **Debug** en el Spring Boot Dashboard, o **Debug** sobre el `main`.
2. Pide `http://localhost:8080/api/tareas/2` en el navegador o con REST Client.

**Resultado esperado:** la ejecución se detiene en el *breakpoint*. En **Variables**, `id` = `2`. **Call Stack** muestra un hilo `http-nio-8080-exec-...`. Continúa con `F5`: el navegador recibe la tarea 2.

El depurador funciona exactamente igual que en el módulo 3, pero la JVM depurada está dentro del contenedor.

### Paso 15. Modificar código con recarga automática

Con la aplicación en marcha, en `TareaController`, cambia:

```java
        return new ResponseStatusException(HttpStatus.NOT_FOUND, "No existe la tarea " + id);
```

por:

```java
        return new ResponseStatusException(HttpStatus.NOT_FOUND, "La tarea " + id + " no existe");
```

Guarda y pide `http://localhost:8080/api/tareas/999`.

**Resultado esperado:** en uno o dos segundos DevTools reinicia la aplicación dentro del contenedor y la respuesta incluye `"detail":"La tarea 999 no existe"`.

### Paso 16. Comprobar que el código está en tu equipo

Detén la aplicación. Abre una terminal **de tu equipo**, fuera de VS Code, en la carpeta `devcontainer-4.4`:

**Linux o WSL (Bash):**

```bash
grep -n "no existe" src/main/java/curso/docker/controller/TareaController.java
ls target
```

**Windows (PowerShell):**

```powershell
Select-String -Path src/main/java/curso/docker/controller/TareaController.java -Pattern "no existe"
Get-ChildItem target
```

**Resultado esperado:** el cambio del paso 15 está en tu disco, y también la carpeta `target/` que generó Maven dentro del contenedor. La carpeta del proyecto es la misma vista desde dos sitios: `devcontainer-4.4` en tu equipo y `/workspaces/devcontainer-4.4` en el contenedor.

### Paso 17. Cerrar y volver a abrir

1. Clic en el indicador de la esquina inferior izquierda > **Reopen Folder Locally**. La ventana vuelve a tu equipo y el contenedor se detiene.
2. Ejecuta de nuevo `Dev Containers: Reopen in Container`.

**Resultado esperado:** esta vez tarda solo unos segundos, porque reutiliza el contenedor. El cambio del paso 15 sigue ahí, y `mvn test` ya no descarga dependencias: el repositorio `~/.m2` del contenedor se ha conservado.

### Paso 18. Reconstruir el contenedor

Ejecuta `Dev Containers: Rebuild Container`.

**Resultado esperado:**

- VS Code crea un **contenedor nuevo**: la vista **Containers** de tu equipo muestra otro identificador, y `/api/estado` devolvería otro `host`.
- **El código y `target/` siguen intactos**, porque están en tu disco.
- **`~/.m2` está vacío**: el siguiente `mvn test` vuelve a descargar las dependencias, porque estaban dentro del contenedor anterior.
- `postCreateCommand` se ejecuta otra vez.

### Paso 19. Restaurar el entorno

Clic en el indicador > **Reopen Folder Locally**. Después, sigue el apartado 13 para eliminar el contenedor y las imágenes de desarrollo si no los vas a usar más.

## 8. Comandos equivalentes

Dev Containers es una función de VS Code. Lo que hace por debajo equivale aproximadamente a:

| Acción de VS Code | Equivalente aproximado |
|---|---|
| Construir la imagen del entorno | `docker build -f .devcontainer/Dockerfile .devcontainer` |
| Crear el contenedor con el proyecto montado | `docker run -v <carpeta>:/workspaces/<carpeta> ...` |
| Terminal del contenedor | `docker exec -it -u vscode <contenedor> bash` |
| Reenvío de puertos | Túnel de VS Code; no publica puertos con `-p` |

Para usar la misma configuración sin VS Code existe una CLI oficial, `@devcontainers/cli`:

```bash
devcontainer up --workspace-folder .
devcontainer exec --workspace-folder . mvn test
```

No es necesaria en el curso.

## 9. Resultados esperados

| Comprobación | Resultado |
|---|---|
| Reopen in Container | Indicador `Dev Container: Java 21 y Maven - Curso VS Code` |
| `whoami` / `pwd` | `vscode` / `/workspaces/devcontainer-4.4` |
| `java -version` | `21.0.13.0.2`, Microsoft |
| `mvn -version` | Apache Maven 3.9.9 |
| `mvn test` | 13 pruebas correctas |
| Ejecución | `Started AplicacionTareas` en `restartedMain`; puerto 8080 en **Ports** |
| `localhost:8080/api/estado` desde tu navegador | `"mensaje":"API de tareas en el contenedor de desarrollo"` |
| Depuración | Parada en `consultar` con `id` = 2 |
| DevTools | `"detail":"La tarea 999 no existe"` sin reiniciar a mano |
| Archivos en tu equipo | El cambio y `target/` visibles fuera del contenedor |
| Reopen | Rápido; se conservan código y `~/.m2` |
| Rebuild | Contenedor nuevo; se conserva el código, no `~/.m2` |

## 10. Problemas frecuentes

| Problema | Causa y solución |
|---|---|
| No aparece la notificación **Reopen in Container** | La carpeta abierta no es la que contiene `.devcontainer/`. Abre `devcontainer-4.4`, no una carpeta superior. También puedes usar la Command Palette. |
| **Docker returned an error** o **The 'docker' command was not found** | Docker no está en marcha o no está instalado en el entorno de VS Code (apartado 3, «Si Docker no está disponible») |
| La ventana se abre en un contenedor de recuperación (*recovery container*) | Ha fallado la construcción, normalmente por un error en el Dockerfile de desarrollo. Corrígelo en esa ventana y vuelve a ejecutar **Reopen in Container**. |
| La primera apertura tarda mucho | Es normal: descarga unos 700 MB. Sigue el progreso con **show log**. Las siguientes aperturas son rápidas. |
| `apt-get` falla al construir | Sin conexión con los repositorios de Debian. Comprueba la red y usa **Rebuild Container**. |
| Cambié `devcontainer.json` y no se aplica | Los cambios solo se aplican al crear el contenedor: **Rebuild Container**. VS Code suele avisarlo con una notificación. |
| Las extensiones de Java no funcionan dentro | Revisa la sección **DEV CONTAINER** de **Extensions**. Si falta alguna, **Install in Dev Container**, o añádela a `devcontainer.json` y reconstruye. |
| El navegador no conecta con `localhost:8080` | Mira la vista **Ports**: puede haberse reenviado a otro puerto local. Comprueba también que la aplicación está en marcha. |
| Maven compila muy despacio en Windows | El proyecto está en el disco de Windows. Trabaja con VS Code conectado a WSL y el proyecto en `~/practicas-docker`. |
| `Permission denied` al escribir en `target/` (Linux) | Los archivos se crearon con otro usuario, por ejemplo con `sudo`. En tu equipo, corrige el propietario de `target/`, o bórrala con `mvn clean` desde el contenedor. |

## 11. Comparación con Eclipse e IntelliJ IDEA

| Función | Eclipse | IntelliJ IDEA | VS Code |
|---|---|---|---|
| Abrir un proyecto en un contenedor de desarrollo | No hay soporte equivalente integrado | Soporte de Dev Containers: abre `devcontainer.json` con el backend del IDE dentro del contenedor (requiere la versión de pago) | Extensión **Dev Containers**, gratuita |
| Formato de configuración | — | `devcontainer.json`, el mismo estándar | `devcontainer.json` |
| Extensiones o *plugins* en el contenedor | — | *Plugins* del backend | `customizations.vscode.extensions` |
| Alternativa habitual | Instalar JDK y Maven en el equipo, o máquinas virtuales | Igual, o JetBrains Gateway | — |

Como el formato es un estándar abierto, un mismo `devcontainer.json` sirve en VS Code, en IntelliJ IDEA y en servicios en la nube como GitHub Codespaces. En Eclipse no hay soporte equivalente integrado.

## 12. Comprobaciones finales

Comprueba que eres capaz de:

- [ ] Explicar la diferencia entre el contenedor de desarrollo de este apartado y el de ejecución del 4.2.
- [ ] Explicar por qué no hace falta Java en tu equipo, pero sí Docker y VS Code.
- [ ] Explicar cada propiedad de `devcontainer.json` del proyecto.
- [ ] Explicar qué es una *feature* y qué hay que revisar antes de usarla.
- [ ] Abrir el proyecto con **Reopen in Container** e identificar que estás dentro.
- [ ] Distinguir las extensiones locales de las del contenedor.
- [ ] Ejecutar pruebas, arrancar y depurar la API dentro del contenedor, y acceder a ella desde tu navegador.
- [ ] Explicar dónde está el código y qué se pierde al reconstruir.
- [ ] Elegir entre **Reopen in Container**, **Rebuild Container** y **Rebuild Container Without Cache**.

## 13. Limpieza segura

1. Vuelve a abrir la carpeta en local: **Reopen Folder Locally**.
2. Localiza el contenedor de desarrollo. VS Code le pone un nombre aleatorio, pero lo marca con la carpeta del proyecto:

   ```bash
   docker ps -a --filter label=devcontainer.local_folder --format "{{.ID}}  {{.Names}}  {{.Image}}"
   ```

   El de esta práctica usa una imagen cuyo nombre empieza por `vsc-devcontainer-4.4-`. En la vista **Containers** también lo verás con esa imagen.

3. Elimina ese contenedor por su identificador, con **Remove...** en la vista o con:

   ```bash
   docker rm -f <ID>
   ```

4. Elimina las imágenes construidas para él:

   ```bash
   docker images --filter "reference=vsc-devcontainer-4.4-*"
   docker rmi <imagen>
   ```

5. Si no vas a usar más Dev Containers de Java, elimina también la imagen base, que ocupa unos 2,3 GB:

   ```bash
   docker rmi mcr.microsoft.com/devcontainers/java:3-21-trixie
   ```

   El apartado 4.5 la vuelve a usar: si vas a hacerlo a continuación, consérvala.

No elimines contenedores de Dev Containers de otros proyectos, ni uses `docker system prune` para «limpiar»: borraría recursos de todos los proyectos del equipo.
