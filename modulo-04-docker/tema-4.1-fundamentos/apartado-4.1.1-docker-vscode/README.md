# Ejercicio 4.1 — Docker desde VS Code

Módulo 4. Contenerización en VS Code · Tema 4.1. Fundamentos · Apartado 4.1.1. Docker y VS Code

Duración estimada: 1 hora.

## 1. Introducción

Un contenedor empaqueta una aplicación con todo lo que necesita para ejecutarse: sistema base, bibliotecas, entorno de ejecución y configuración. La misma imagen se ejecuta igual en el portátil de cualquier desarrollador, en un servidor de integración o en producción.

Este módulo usa Docker para dos fines:

- **Ejecutar aplicaciones Java**: empaquetar la API de tareas del módulo 3 en una imagen y arrancarla en un contenedor (apartados 4.2 y 4.3).
- **Desarrollar dentro de un contenedor**: abrir el proyecto en un entorno con Java y Maven ya preparados, sin instalarlos en el equipo (apartado 4.4).

En este primer apartado aprenderás los conceptos básicos y a gestionar contenedores desde VS Code con la extensión **Container Tools**, junto con los comandos equivalentes. No es un curso de administración de Docker: solo se usa lo necesario para el desarrollo Java.

## 2. Objetivos

Al terminar serás capaz de:

- Explicar qué son una imagen, un contenedor, un registro, un volumen y una red.
- Diferenciar un contenedor de una máquina virtual.
- Comprobar que Docker funciona en tu equipo.
- Descargar una imagen, crear un contenedor y publicar un puerto.
- Inspeccionar el contenedor, ver sus logs y abrir una terminal dentro de él desde VS Code.
- Detener y eliminar **solo** los recursos que has creado.

## 3. Conceptos fundamentales

### Máquina virtual y contenedor

| | Máquina virtual | Contenedor |
|---|---|---|
| Qué virtualiza | Un equipo completo, con su propio sistema operativo | Solo el espacio de usuario: comparte el núcleo (*kernel*) del sistema anfitrión |
| Arranque | Minutos | Segundos o menos |
| Tamaño | Gigabytes | Desde unos pocos megabytes |
| Uso típico | Ejecutar otro sistema operativo completo | Ejecutar una aplicación y sus dependencias |

Los contenedores de este curso son de **Linux**. En Windows funcionan gracias a una máquina virtual ligera de Linux que gestiona Docker Desktop mediante WSL 2.

### Piezas de Docker

| Concepto | Qué es | Analogía Java |
|---|---|---|
| **Imagen** | Plantilla de solo lectura con el sistema de archivos y la configuración de arranque. Se identifica por nombre y etiqueta: `nginx:1.30-alpine`. | Una clase |
| **Contenedor** | Instancia en ejecución (o detenida) de una imagen, con su propia capa de escritura | Un objeto creado a partir de la clase |
| **Dockerfile** | Archivo de texto con las instrucciones para construir una imagen. Se trabaja en el apartado 4.2. | El código fuente |
| **Registro** | Servidor que almacena imágenes. **Docker Hub** es el registro público por defecto. | Maven Central |
| **Volumen** | Almacenamiento que sobrevive al contenedor. Puede ser un **volumen con nombre**, gestionado por Docker, o un **montaje** (*bind mount*) de una carpeta del equipo. | — |
| **Red** | Red virtual que comunica contenedores entre sí. Se trabaja en el apartado 4.3. | — |
| **Puerto publicado** | Puerto del equipo que reenvía al puerto de un contenedor: `-p 127.0.0.1:18070:80` | — |

Al borrar un contenedor se pierde lo que se escribió **dentro** de él. Lo que esté en un volumen o en una carpeta montada, no.

### Etiquetas de imagen

La etiqueta indica la versión: `nginx:1.30-alpine` es nginx 1.30 sobre Alpine Linux. **Evita `latest`** o no poner etiqueta, que equivale a `latest`: cambia con cada publicación y dos personas pueden obtener imágenes distintas con el mismo comando. En el curso todas las imágenes tienen una etiqueta concreta.

### Docker Engine y Docker Desktop

| Producto | Qué es | Dónde se usa |
|---|---|---|
| **Docker Engine** | El servicio (*daemon*) `dockerd` que ejecuta los contenedores, más el cliente `docker` | Linux, de forma nativa |
| **Docker Desktop** | Aplicación de escritorio que incluye Docker Engine dentro de una máquina virtual Linux, el cliente, Compose, Buildx y una interfaz gráfica | Windows y macOS (también existe para Linux) |

El cliente `docker` envía órdenes al *daemon*. Por eso `docker info` muestra dos bloques: **Client** y **Server**. Si el *daemon* no está en marcha, el cliente responde, pero falla todo lo demás.

Docker Desktop es gratuito para uso personal, educativo y empresas pequeñas. En empresas grandes requiere una suscripción de pago: si lo usas en el trabajo, consulta las condiciones de tu organización.

### Las cuatro situaciones habituales

| Tu entorno | Dónde está Docker | Qué hace VS Code |
|---|---|---|
| **Windows con Docker Desktop**, VS Code en Windows | Docker Desktop, con el motor WSL 2 | Container Tools usa el cliente `docker` de Windows |
| **Windows con VS Code conectado a WSL** y Docker Desktop | Docker Desktop, con la integración WSL activada para tu distribución (**Settings > Resources > WSL integration**) | Container Tools se ejecuta en WSL y usa el cliente `docker` de la distribución |
| **WSL con Docker Engine instalado dentro de la distribución**, sin Docker Desktop | Docker Engine en la distribución | Igual que el caso anterior. Docker solo está disponible dentro de WSL, no desde PowerShell. |
| **Linux** | Docker Engine | Container Tools usa el cliente `docker` local |

No mezcles en la misma distribución de WSL la integración de Docker Desktop y un Docker Engine instalado dentro: tendrías dos *daemons* y dos conjuntos de contenedores distintos.

### Docker en Linux y el grupo `docker`

En Linux, el *daemon* solo admite órdenes de `root` y de los usuarios del grupo `docker`. Muchas guías recomiendan añadir tu usuario a ese grupo para no escribir `sudo`. Antes de hacerlo, ten en cuenta que **pertenecer al grupo `docker` equivale a tener privilegios de administrador en el equipo**: quien pueda ejecutar `docker` puede montar cualquier carpeta del sistema en un contenedor y modificarla como `root`.

En un equipo personal de desarrollo es una práctica habitual y aceptada. En un equipo compartido o corporativo, consulta antes con el responsable del sistema. Las alternativas son usar `sudo docker ...` o el modo *rootless* de Docker.

## 4. Preparación del entorno

### 4.1. Instalación y comprobación

Necesitas una de las instalaciones de la tabla anterior. Si todavía no tienes Docker:

- **Windows**: instala Docker Desktop desde la web oficial de Docker y deja activado el motor WSL 2.
- **Linux**: instala Docker Engine siguiendo la guía oficial de tu distribución, que incluye el *plugin* de Compose.

Comprueba la instalación en una terminal. En Windows, usa PowerShell si VS Code trabaja en Windows, o la terminal de WSL si VS Code está conectado a WSL:

```bash
docker --version
docker info
docker compose version
```

**Resultado esperado** (las versiones pueden ser posteriores):

```text
Docker version 29.4.1, build 055a478
Client: ...
Server:
 Containers: ...
 Server Version: 29.4.1
Docker Compose version v5.1.3
```

Si `docker info` termina con un error como `Cannot connect to the Docker daemon` o `error during connect`, el cliente funciona pero el *daemon* no: arranca Docker Desktop o, en Linux, el servicio `docker`.

### 4.2. Extensión Container Tools

Instala desde la vista **Extensions** la extensión **Container Tools** (`ms-azuretools.vscode-containers`, editor **Microsoft**).

Container Tools es la sucesora de la antigua extensión **Docker** (`ms-azuretools.vscode-docker`). Si tenías instalada la antigua, VS Code te propondrá migrar.

La extensión necesita el cliente `docker` en el mismo entorno en el que trabaja VS Code. Con VS Code conectado a WSL, la extensión se instala y se ejecuta dentro de WSL: VS Code lo indica en el botón de instalación (**Install in WSL: Ubuntu**).

### 4.3. Actualizar el material y copiar la práctica

En la raíz de tu repositorio del curso comprueba que no tienes cambios pendientes y actualízalo. No vuelvas a clonarlo:

```bash
git status
git pull --ff-only
```

Si `git status` muestra cambios, no ejecutes el `pull`: guárdalos fuera del repositorio o descártalos antes.

Después copia la práctica **fuera del repositorio del curso**. Las demás prácticas del módulo se copian en la misma carpeta, `practicas-docker`.

**Windows (PowerShell):**

```powershell
New-Item -ItemType Directory -Force "$HOME/practicas-docker" | Out-Null
Copy-Item -Recurse modulo-04-docker/tema-4.1-fundamentos/apartado-4.1.1-docker-vscode/proyecto "$HOME/practicas-docker/docker-4.1"
code "$HOME/practicas-docker/docker-4.1"
```

**Linux o WSL (Bash):**

```bash
mkdir -p ~/practicas-docker
cp -r modulo-04-docker/tema-4.1-fundamentos/apartado-4.1.1-docker-vscode/proyecto ~/practicas-docker/docker-4.1
code ~/practicas-docker/docker-4.1
```

La práctica solo contiene `html/index.html`, la página que servirá el contenedor.

### 4.4. Nombres y puertos de la práctica

En tu equipo puede haber otros contenedores, de otros cursos o proyectos. Para no confundirlos:

- todos los recursos del módulo empiezan por `curso-m4`;
- los puertos del host son del rango 18000 y se publican solo en `127.0.0.1`, de modo que solo son accesibles desde tu equipo.

Comprueba que no hay ya un contenedor con el nombre de la práctica:

```bash
docker ps -a --filter name=curso-m4
```

**Resultado esperado:** solo la línea de cabecera.

## 5. La vista Containers de VS Code

Container Tools añade un icono **Containers** a la barra de actividad. La vista tiene estas secciones:

| Sección | Muestra | Acciones con clic derecho |
|---|---|---|
| **Containers** | Contenedores en marcha y detenidos | **View Logs**, **Attach Shell**, **Inspect**, **Open in External Browser**, **Stop**, **Start**, **Restart**, **Remove...** |
| **Images** | Imágenes descargadas o construidas | **Run**, **Run Interactive**, **Inspect**, **Pull**, **Tag...**, **Remove...** |
| **Registries** | Registros conectados, como Docker Hub | Buscar y descargar imágenes |
| **Networks** | Redes de Docker | **Inspect**, **Remove...** |
| **Volumes** | Volúmenes con nombre | **Inspect**, **Remove...** |
| **Docker Contexts** | A qué *daemon* se conecta el cliente | Cambiar de contexto |

Al desplegar un contenedor en marcha aparece **Files**, con su sistema de archivos: puedes abrir un archivo (**Open**) o descargarlo (**Download...**).

Las mismas acciones están en la Command Palette (`Ctrl + Shift + P`) con el prefijo `Containers:` o `Container Images:`, por ejemplo `Containers: View Logs` o `Container Images: Pull`.

**Cuidado con las acciones de limpieza masiva:** los comandos **Prune...** (`Containers: Prune...`, `Container Images: Prune...`, `Container Volumes: Prune...`, `Containers: Prune System...`) eliminan **todos** los recursos sin uso del equipo, también los de otros proyectos. No los uses en esta práctica.

## 6. Ejemplo de configuración: el comando `docker run`

```bash
docker run -d --name curso-m4-web -p 127.0.0.1:18070:80 -v "$PWD/html:/usr/share/nginx/html:ro" nginx:1.30-alpine
```

| Parte | Significado |
|---|---|
| `-d` | En segundo plano (*detached*): devuelve el control de la terminal |
| `--name curso-m4-web` | Nombre del contenedor. Sin él, Docker inventa uno, como `goofy_bhaskara`. |
| `-p 127.0.0.1:18070:80` | Publica el puerto **80 del contenedor** en el puerto **18070 del equipo**, solo para conexiones locales |
| `-v "$PWD/html:/usr/share/nginx/html:ro"` | Monta la carpeta `html` de tu copia en la carpeta que sirve nginx. `ro` (*read-only*): el contenedor no puede modificarla. |
| `nginx:1.30-alpine` | Imagen oficial de nginx 1.30 sobre Alpine Linux (unos 30 MB de descarga) |

nginx es un servidor web muy ligero. Se usa aquí porque permite ver puertos, logs y montajes sin compilar nada. En el apartado 4.2 el contenedor será la API de tareas.

## 7. Práctica guiada

Trabaja en la terminal de VS Code, en la carpeta de tu copia (`docker-4.1`).

### Paso 1. Comprobar Docker

Ejecuta los comandos del punto 4.1. Abre después la vista **Containers**: debe mostrar las secciones sin errores. Si Docker no responde, la vista muestra un aviso con un enlace a la ayuda.

### Paso 2. Ejecutar Java sin instalarlo

```bash
docker run --rm eclipse-temurin:21-jre-noble java -version
```

**Resultado esperado:** Docker descarga la imagen la primera vez, unos 100 MB, y muestra:

```text
openjdk version "21.0.13" 2026-10-20 LTS
OpenJDK Runtime Environment Temurin-21.0.13+9 (build 21.0.13+9-LTS)
```

La versión exacta puede ser posterior: la etiqueta `21-jre-noble` recibe las actualizaciones de seguridad de Java 21. Has ejecutado Java 21 aunque no esté instalado en tu sistema. `--rm` elimina el contenedor al terminar.

No borres esta imagen: es la base de la imagen de la API en el apartado 4.2.

### Paso 3. Descargar una imagen pública

**VS Code:** Command Palette > `Container Images: Pull` y escribe `nginx:1.30-alpine`.

**Terminal:**

```bash
docker pull nginx:1.30-alpine
```

### Paso 4. Consultar las imágenes

**VS Code:** sección **Images**. Despliega `nginx` y verás la etiqueta `1.30-alpine`.

**Terminal:**

```bash
docker images nginx
```

**Resultado esperado:** una línea con `nginx:1.30-alpine` y su tamaño.

### Paso 5. Crear y ejecutar el contenedor

**Linux o WSL (Bash):**

```bash
docker run -d --name curso-m4-web -p 127.0.0.1:18070:80 -v "$PWD/html:/usr/share/nginx/html:ro" nginx:1.30-alpine
```

**Windows (PowerShell):**

```powershell
docker run -d --name curso-m4-web -p 127.0.0.1:18070:80 -v "${PWD}/html:/usr/share/nginx/html:ro" nginx:1.30-alpine
```

Docker muestra el identificador completo del contenedor nuevo.

Desde VS Code también puedes crear contenedores con clic derecho sobre la imagen > **Run**, pero ese comando no permite indicar el nombre, el puerto local ni el montaje. Para controlarlos se usa `docker run` o, como en el apartado 4.3, Docker Compose.

### Paso 6. Mostrar los contenedores activos

**VS Code:** en la sección **Containers** aparece `curso-m4-web` con un icono verde (en marcha).

**Terminal:**

```bash
docker ps
```

**Resultado esperado:**

```text
CONTAINER ID   IMAGE               ...   STATUS         PORTS                     NAMES
6630c9c4dd3a   nginx:1.30-alpine   ...   Up 5 seconds   127.0.0.1:18070->80/tcp   curso-m4-web
```

Abre `http://localhost:18070` en el navegador, o usa clic derecho sobre el contenedor > **Open in External Browser**. Debe aparecer la página **Hola desde un contenedor**.

### Paso 7. Inspeccionar el contenedor

**VS Code:** clic derecho sobre `curso-m4-web` > **Inspect**. Se abre un JSON con toda su configuración. Busca:

- `"State"` > `"Status": "running"`;
- `"Mounts"`: el montaje de tu carpeta `html` con `"RW": false`;
- `"PortBindings"`: `"HostIp": "127.0.0.1"` y `"HostPort": "18070"`.

Despliega también el contenedor en la vista y abre **Files** > `usr` > `share` > `nginx` > `html` > `index.html`: es tu archivo, visto desde dentro del contenedor.

**Terminal:**

```bash
docker inspect curso-m4-web
docker inspect -f "{{.State.Status}}" curso-m4-web
```

### Paso 8. Consultar los logs

Recarga la página del navegador un par de veces.

**VS Code:** clic derecho > **View Logs**. Se abre una terminal que sigue el registro en directo.

**Terminal:**

```bash
docker logs curso-m4-web
docker logs -f curso-m4-web
```

Con `-f` se queda esperando nuevas líneas, como **View Logs**. Sal con `Ctrl + C`: el contenedor sigue en marcha.

**Resultado esperado:** los mensajes de arranque de nginx y una línea por petición:

```text
172.17.0.1 - - [15/Nov/2026:07:11:28 +0000] "GET / HTTP/1.1" 200 766 "-" "Mozilla/5.0 ..." "-"
```

Los logs de un contenedor son lo que su proceso principal escribe en la salida estándar. En el apartado 4.2 verás así los mensajes de Spring Boot.

### Paso 9. Abrir una terminal dentro del contenedor

**VS Code:** clic derecho > **Attach Shell**.

**Terminal:**

```bash
docker exec -it curso-m4-web sh
```

Dentro del contenedor, ejecuta:

```sh
nginx -v
cat /etc/os-release
ls /usr/share/nginx/html
touch /usr/share/nginx/html/prueba.txt
exit
```

**Resultado esperado:**

- `nginx version: nginx/1.30.6`, o una versión 1.30 posterior;
- el sistema es **Alpine Linux**, aunque tu equipo sea Windows o Ubuntu;
- `touch` falla con `Read-only file system`, porque el montaje es de solo lectura.

`docker exec` ejecuta un proceso **adicional** en un contenedor que ya está en marcha. `-it` lo hace interactivo. Al salir con `exit`, el contenedor sigue funcionando.

### Paso 10. Comprobar el montaje

En VS Code, edita `html/index.html` y cambia el título `Hola desde un contenedor` por otro texto. Guarda y recarga el navegador.

**Resultado esperado:** aparece el texto nuevo sin reiniciar el contenedor. El contenedor lee el archivo de tu carpeta, no una copia.

### Paso 11. Detener el contenedor

**VS Code:** clic derecho > **Stop**. El icono pasa a gris.

**Terminal:**

```bash
docker stop curso-m4-web
docker ps
docker ps -a --filter name=curso-m4
```

**Resultado esperado:** `docker ps` ya no lo muestra; `docker ps -a` sí, con estado `Exited (0)`. Un contenedor detenido conserva su configuración y su capa de escritura: se puede volver a arrancar con **Start** o `docker start curso-m4-web`.

### Paso 12. Eliminar el contenedor

**VS Code:** clic derecho > **Remove...** y confirma.

**Terminal:**

```bash
docker rm curso-m4-web
docker ps -a --filter name=curso-m4
```

**Resultado esperado:** el contenedor desaparece de la vista y del listado. Tu carpeta `html` sigue intacta: estaba en tu equipo, no dentro del contenedor.

### Paso 13. Redes y volúmenes

Observa las secciones **Networks** y **Volumes** y compáralas con:

```bash
docker network ls
docker volume ls
```

Las redes `bridge`, `host` y `none` las crea Docker. Puede haber otras redes y volúmenes de otros proyectos de tu equipo: **no los elimines**. En el apartado 4.3, Docker Compose creará una red propia para la práctica.

## 8. Comandos equivalentes

| Acción | VS Code (Container Tools) | Terminal |
|---|---|---|
| Comprobar Docker | Vista **Containers** sin errores | `docker info` |
| Descargar una imagen | `Container Images: Pull` | `docker pull nginx:1.30-alpine` |
| Ver imágenes | Sección **Images** | `docker images` |
| Crear y arrancar un contenedor | Clic derecho en la imagen > **Run** (sin opciones) | `docker run -d --name ... -p ... imagen` |
| Ver contenedores | Sección **Containers** | `docker ps` / `docker ps -a` |
| Inspeccionar | **Inspect** | `docker inspect curso-m4-web` |
| Ver logs | **View Logs** | `docker logs -f curso-m4-web` |
| Terminal dentro | **Attach Shell** | `docker exec -it curso-m4-web sh` |
| Ver archivos | **Files** > **Open** | `docker exec curso-m4-web ls /ruta` |
| Detener / arrancar | **Stop** / **Start** | `docker stop` / `docker start` |
| Eliminar contenedor | **Remove...** | `docker rm curso-m4-web` |
| Eliminar imagen | Sección **Images** > **Remove...** | `docker rmi nginx:1.30-alpine` |

## 9. Resultados esperados

| Comprobación | Resultado |
|---|---|
| `docker info` | Bloques **Client** y **Server**, sin errores |
| `docker run --rm eclipse-temurin:21-jre-noble java -version` | `openjdk version "21.0.x"` |
| `docker ps` | `curso-m4-web` con `127.0.0.1:18070->80/tcp` |
| `http://localhost:18070` | Página **Hola desde un contenedor** |
| **View Logs** | Una línea por petición del navegador |
| `touch` en el contenedor | `Read-only file system` |
| Cambio en `index.html` | Visible al recargar, sin reiniciar |
| Tras **Remove...** | `docker ps -a --filter name=curso-m4` vacío |

## 10. Problemas frecuentes

| Problema | Causa y solución |
|---|---|
| `Cannot connect to the Docker daemon` / `error during connect` | El *daemon* no está en marcha. Arranca Docker Desktop o, en Linux, `sudo systemctl start docker`. |
| `permission denied while trying to connect to the Docker daemon socket` (Linux) | Tu usuario no tiene permiso sobre el *daemon*. Usa `sudo` o valora el grupo `docker` (lee antes el apartado 3). |
| `docker: command not found` en WSL con Docker Desktop | Falta activar la integración con tu distribución en **Settings > Resources > WSL integration** |
| La vista **Containers** muestra un error, pero la terminal funciona | La extensión se ejecuta en otro entorno: por ejemplo en Windows, con Docker solo en WSL. Abre la carpeta con VS Code conectado a WSL. |
| `Conflict. The container name "/curso-m4-web" is already in use` | Ya existe un contenedor con ese nombre, quizá detenido. Elimínalo (paso 12) o usa otro nombre. |
| `Bind for 127.0.0.1:18070 failed: port is already allocated` | Otro proceso o contenedor usa el puerto. Cambia el **primer** número, el del equipo: `-p 127.0.0.1:18071:80`, y abre `http://localhost:18071`. El segundo número, el del contenedor, no cambia. |
| El navegador muestra `403 Forbidden` | La carpeta montada no contiene `index.html`. Ejecuta `docker run` desde la carpeta `docker-4.1`, que contiene `html/`. |
| En PowerShell, `-v` no monta la carpeta | Usa `"${PWD}/html:..."` con comillas dobles, y comprueba en Docker Desktop que la unidad se puede compartir (**Settings > Resources > File sharing**, si aparece) |
| `docker pull` falla con `toomanyrequests` | Docker Hub limita las descargas anónimas. Espera o inicia sesión con `docker login`; tu contraseña no se guarda en ningún archivo del curso. |

## 11. Comparación con Eclipse e IntelliJ IDEA

| Función | Eclipse | IntelliJ IDEA | VS Code |
|---|---|---|---|
| Gestión de contenedores | *Plugin* **Docker Tooling** (Linux Tools), poco mantenido; no viene en las distribuciones habituales | Integración Docker en la ventana **Services**: incluida en Ultimate; en la versión gratuita, mediante el *plugin* Docker | Extensión **Container Tools** de Microsoft |
| Ver logs y abrir una terminal | Sí, con el *plugin* | Sí | **View Logs** y **Attach Shell** |
| Explorar archivos del contenedor | Limitado | Sí | **Files** en la vista |
| Edición de Dockerfile y Compose con ayudas | Básica | Completa | Autocompletado y validación de Container Tools |

En los tres casos, el IDE usa el mismo Docker instalado en el equipo: lo que se hace desde el IDE se ve con `docker ps`, y al revés.

## 12. Comprobaciones finales

Comprueba que eres capaz de:

- [ ] Explicar la diferencia entre imagen y contenedor, y entre contenedor y máquina virtual.
- [ ] Decir qué hacen Docker Engine y Docker Desktop, y cuál usas tú.
- [ ] Explicar por qué hay que evitar la etiqueta `latest`.
- [ ] Descargar una imagen y crear un contenedor con nombre, puerto local y montaje de solo lectura.
- [ ] Distinguir el puerto del equipo del puerto del contenedor en `-p 127.0.0.1:18070:80`.
- [ ] Inspeccionar el contenedor, ver sus logs y abrir una terminal dentro, desde VS Code y desde la terminal.
- [ ] Explicar qué se pierde al borrar un contenedor y qué no.
- [ ] Detener y eliminar únicamente el contenedor de la práctica.

## 13. Limpieza segura

Si no lo hiciste en los pasos 11 y 12:

```bash
docker stop curso-m4-web
docker rm curso-m4-web
```

Opcionalmente, elimina la imagen de nginx, que no se usa en el resto del módulo:

```bash
docker rmi nginx:1.30-alpine
```

**Conserva** `eclipse-temurin:21-jre-noble`: se usa en el apartado 4.2.

**No ejecutes** comandos de limpieza global como `docker system prune`, `docker container prune`, `docker image prune` o `docker volume prune`, ni sus equivalentes **Prune...** de VS Code. Eliminan recursos de **todos** los proyectos del equipo, no solo los de esta práctica, y lo que borran no se puede recuperar. Elimina siempre los recursos por su nombre.
