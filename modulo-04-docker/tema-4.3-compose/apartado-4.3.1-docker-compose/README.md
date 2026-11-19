# Ejercicio 4.3 — Docker Compose y servicios

Módulo 4. Contenerización en VS Code · Tema 4.3. Compose · Apartado 4.3.1. Docker Compose

Duración estimada: 1 hora.

## 1. Introducción

En el apartado 4.2 la API se arrancaba con un `docker run` largo: nombre, puerto, variables de entorno… Con varias aplicaciones que colaboran, esos comandos se multiplican y hay que recordarlos todos.

**Docker Compose** describe en un archivo, `compose.yaml`, todos los servicios de una aplicación y cómo se relacionan. Con un solo comando se construyen, se arrancan, se conectan en red y se detienen juntos. El archivo se guarda en Git con el código, de modo que cualquier desarrollador levanta el mismo entorno.

La composición de esta práctica tiene dos servicios:

| Servicio | Imagen | Función |
|---|---|---|
| `api` | Construida con el Dockerfile del apartado 4.2 | La API de tareas, publicada en `127.0.0.1:18081` |
| `monitor` | `alpine:3.24` | Cada 10 segundos consulta `GET /api/estado` **por la red interna** y escribe el resultado en su log. No publica ningún puerto. |

El monitor no tiene lógica de negocio. Sirve para comprobar lo que Compose aporta: la red entre servicios, la resolución de nombres, el orden de arranque con *healthchecks* y los logs de varios contenedores.

## 2. Objetivos

Al terminar serás capaz de:

- Leer un `compose.yaml` e identificar servicios, imágenes, construcción, puertos, variables, redes y *healthchecks*.
- Levantar, consultar y detener una composición desde VS Code y desde la terminal.
- Explicar cómo se comunican los contenedores por nombre de servicio.
- Cambiar la configuración de un servicio y recrearlo.
- Diferenciar `stop`, `down` y `down -v`, y limpiar solo los recursos de la práctica.

## 3. Conceptos fundamentales

### Compose V2: `docker compose`

El comando actual es **`docker compose`**, con espacio: un *plugin* del cliente de Docker, incluido en Docker Desktop y en los paquetes oficiales de Docker Engine.

El antiguo `docker-compose`, con guion, era un programa independiente escrito en Python (Compose V1). Dejó de recibir actualizaciones en 2023. Si lo encuentras en documentación antigua, sustitúyelo por `docker compose`: los subcomandos son los mismos.

El nombre recomendado del archivo es `compose.yaml`. También se reconoce el antiguo `docker-compose.yml`.

### `docker run` frente a `docker compose up`

| | `docker run` | `docker compose up` |
|---|---|---|
| Alcance | Un contenedor | Todos los servicios del archivo |
| Configuración | Opciones en la línea de órdenes | `compose.yaml`, versionado con el código |
| Red | La red `bridge` por defecto, sin resolución de nombres entre contenedores | Una red propia del proyecto, en la que cada servicio se encuentra por su nombre |
| Construcción | Aparte, con `docker build` | `--build` construye las imágenes que lo necesiten |
| Orden de arranque | Manual | `depends_on`, incluso esperando a que un servicio esté sano |
| Nombres | Los que indiques | `<proyecto>-<servicio>-<n>`, por ejemplo `curso-vscode-m4-compose-api-1` |

### Elementos de `compose.yaml`

| Elemento | Significado | En el proyecto |
|---|---|---|
| `name` | Nombre del **proyecto** Compose: prefijo de contenedores y redes | `curso-vscode-m4-compose` |
| `services` | Contenedores de la aplicación | `api` y `monitor` |
| `build` | Construir la imagen con un Dockerfile; `.` es la carpeta del contexto | `api` |
| `image` | Imagen que se usa; con `build`, el nombre que recibe la imagen construida | `curso-m4/tareas-compose:1.0`, `alpine:3.24` |
| `ports` | Puertos publicados en el equipo: `"IP:host:contenedor"` | `"127.0.0.1:18081:8080"` |
| `environment` | Variables de entorno | `APP_MENSAJE`, `URL_API`, `INTERVALO` |
| `networks` | Redes a las que se conecta cada servicio | `tareas` |
| `healthcheck` | Comando que Docker ejecuta periódicamente para saber si el servicio está **sano** | `curl` a `/api/estado` |
| `depends_on` | Orden de arranque. Con `condition: service_healthy`, espera a que el otro servicio esté sano. | `monitor` depende de `api` |
| `volumes` | Volúmenes con nombre o montajes de carpetas del equipo | No se usan en esta práctica |
| `user`, `init` | Usuario del proceso y proceso *init* para gestionar señales | `monitor` |

### Red y nombres de servicio

Compose crea la red `curso-vscode-m4-compose_tareas` y conecta en ella los dos contenedores. Dentro de la red, **cada servicio se resuelve por su nombre**:

```text
Equipo (host)                               Red curso-vscode-m4-compose_tareas
                                         ┌──────────────────────────────────────┐
navegador / curl                         │  api (8080)  <───  monitor            │
http://localhost:18081  ───────────────> │              http://api:8080          │
     puerto publicado 127.0.0.1:18081    └──────────────────────────────────────┘
```

- **Desde tu equipo** se usa el puerto **publicado**: `localhost:18081`.
- **Entre contenedores** se usa el **nombre del servicio** y el puerto **interno**: `api:8080`. No hace falta publicar nada.
- Dentro de un contenedor, `localhost` es **ese mismo contenedor**, no tu equipo ni otro contenedor.

### *Healthchecks* y `depends_on`

Que un contenedor esté **en marcha** (*running*) no significa que la aplicación responda: Spring Boot tarda unos segundos en arrancar. El *healthcheck* de `api` ejecuta dentro del contenedor:

```text
curl -fsS -o /dev/null http://localhost:8080/api/estado
```

Mientras falla, el estado es `health: starting`; cuando responde, pasa a `healthy`. Con `condition: service_healthy`, Compose no arranca el monitor hasta que la API está sana.

### Volúmenes en Compose

Esta práctica no los necesita, porque la API guarda las tareas en memoria. Como referencia:

```yaml
services:
  base-de-datos:
    volumes:
      - datos:/var/lib/datos          # Volumen con nombre: lo gestiona Docker
      - ./config:/etc/servicio:ro     # Montaje de una carpeta del proyecto, solo lectura
volumes:
  datos:
```

Los **volúmenes con nombre** sobreviven a `docker compose down`. Solo se borran con `down -v` o con `docker volume rm`.

### `stop`, `down` y `down -v`

| Comando | Contenedores | Red | Volúmenes con nombre | Imágenes |
|---|---|---|---|---|
| `docker compose stop` | Se detienen, **no** se eliminan | Se conserva | Se conservan | Se conservan |
| `docker compose down` | Se detienen **y se eliminan** | Se elimina | Se conservan | Se conservan |
| `docker compose down -v` | Se detienen y se eliminan | Se elimina | **Se eliminan** | Se conservan |

`down -v` **borra los datos persistentes** de los volúmenes del proyecto, por ejemplo una base de datos de desarrollo, y no se pueden recuperar. Úsalo solo cuando quieras empezar de cero y sepas qué contienen esos volúmenes. En esta práctica no hace falta.

## 4. Preparación del entorno

### 4.1. Requisitos

- Docker con Compose V2: `docker compose version` muestra `v2.x` o posterior.
- Container Tools en VS Code.
- La imagen `maven:3.9.16-eclipse-temurin-21-noble` del apartado 4.2, si la conservas. Si no, se descargará.

### 4.2. Actualizar el material y copiar el proyecto

En la raíz de tu repositorio del curso, con `git status` sin cambios pendientes:

```bash
git status
git pull --ff-only
```

**Windows (PowerShell):**

```powershell
New-Item -ItemType Directory -Force "$HOME/practicas-docker" | Out-Null
Copy-Item -Recurse modulo-04-docker/tema-4.3-compose/apartado-4.3.1-docker-compose/proyecto "$HOME/practicas-docker/compose-4.3"
code "$HOME/practicas-docker/compose-4.3"
```

**Linux o WSL (Bash):**

```bash
mkdir -p ~/practicas-docker
cp -r modulo-04-docker/tema-4.3-compose/apartado-4.3.1-docker-compose/proyecto ~/practicas-docker/compose-4.3
code ~/practicas-docker/compose-4.3
```

El proyecto es la API del apartado 4.2, con su Dockerfile, más `compose.yaml`.

### 4.3. Nombres y puertos

| Recurso | Nombre |
|---|---|
| Proyecto Compose | `curso-vscode-m4-compose` |
| Contenedores | `curso-vscode-m4-compose-api-1`, `curso-vscode-m4-compose-monitor-1` |
| Red | `curso-vscode-m4-compose_tareas` |
| Imagen construida | `curso-m4/tareas-compose:1.0` |
| Puerto | **18081** del equipo → 8080 de `api` |

Comprueba que el puerto 18081 está libre, como en el apartado 4.2:

**Linux o WSL (Bash):**

```bash
ss -ltn | grep ':18081 ' || echo "18081 libre"
```

**Windows (PowerShell):**

```powershell
if (Get-NetTCPConnection -LocalPort 18081 -State Listen -ErrorAction SilentlyContinue) { "18081 ocupado" } else { "18081 libre" }
```

Si está ocupado, cambia **solo el número del medio** en `ports` de `compose.yaml`, por ejemplo `"127.0.0.1:18091:8080"`, y en `peticiones/tareas.http`.

## 5. Docker Compose en VS Code

| Acción | VS Code |
|---|---|
| Editar `compose.yaml` | Container Tools: autocompletado de claves y avisos de errores |
| Levantar | Clic derecho sobre `compose.yaml` > **Compose Up**, o `Containers: Compose Up` |
| Levantar algunos servicios | **Compose Up - Select Services** |
| Ver los servicios | Vista **Containers**: los contenedores aparecen **agrupados** bajo el nombre del proyecto |
| Logs de todos los servicios | Clic derecho sobre el grupo del proyecto > **Compose Logs** |
| Logs de un servicio | Clic derecho sobre su contenedor > **View Logs** |
| Detener / arrancar | Clic derecho sobre el grupo > **Compose Stop** / **Compose Start** |
| Reiniciar | **Compose Restart** |
| Eliminar | Clic derecho sobre `compose.yaml` o sobre el grupo > **Compose Down** |

**Compose Up** de VS Code ejecuta `docker compose ... up -d --build` en una terminal: puedes ver el comando exacto en ella.

## 6. El `compose.yaml` del proyecto

Ábrelo en VS Code. Los comentarios explican cada clave. Los puntos clave:

```yaml
name: curso-vscode-m4-compose

services:
  api:
    build: .
    image: curso-m4/tareas-compose:1.0
    environment:
      APP_MENSAJE: "API de tareas gestionada con Docker Compose"
    ports:
      - "127.0.0.1:18081:8080"
    healthcheck:
      test: ["CMD", "curl", "-fsS", "-o", "/dev/null", "http://localhost:8080/api/estado"]
      ...
    networks:
      - tareas

  monitor:
    image: alpine:3.24
    user: "65534:65534"
    init: true
    environment:
      URL_API: "http://api:8080/api/estado"
      INTERVALO: "10"
    command: [...]     # Bucle: wget a $URL_API y sleep $INTERVALO
    depends_on:
      api:
        condition: service_healthy
    networks:
      - tareas

networks:
  tareas:
```

- **`APP_MENSAJE`** sustituye al valor del `ENV` del Dockerfile: la variable de Compose tiene prioridad.
- **`user: "65534:65534"`** ejecuta el monitor con el usuario `nobody`: no necesita privilegios.
- **`init: true`** añade un proceso *init* que reenvía la señal de parada al *shell* del monitor. Sin él, Docker esperaría 10 segundos y lo terminaría a la fuerza.
- **`$$`** en `command` evita que Compose sustituya las variables: las resuelve el *shell* dentro del contenedor.

## 7. Práctica guiada

Trabaja en la terminal de VS Code, en tu copia `compose-4.3`.

### Paso 1. Examinar la configuración

Lee `compose.yaml` y responde:

- ¿Qué servicio se construye y cuál usa una imagen pública?
- ¿Qué servicio publica un puerto en tu equipo? ¿Cuál es el puerto del equipo y cuál el del contenedor?
- ¿Con qué dirección llega el monitor a la API? ¿Por qué no usa `localhost:18081`?

### Paso 2. Validar `compose.yaml`

```bash
docker compose config
```

**Resultado esperado:** la configuración completa, con los valores por defecto añadidos: por ejemplo, el nombre real de la red, `curso-vscode-m4-compose_tareas`. Si el archivo tiene un error de sintaxis, el comando lo indica con la línea.

Para solo comprobar que es válido, sin mostrar nada: `docker compose config -q`.

### Paso 3. Identificar los servicios

```bash
docker compose config --services
```

**Resultado esperado:** `api` y `monitor`.

### Paso 4. Levantar los servicios

**VS Code:** clic derecho sobre `compose.yaml` > **Compose Up**.

**Terminal:**

```bash
docker compose up -d --build
```

**Resultado esperado:** la primera vez se construye la imagen, uno o dos minutos. Después:

```text
 Network curso-vscode-m4-compose_tareas  Created
 Container curso-vscode-m4-compose-api-1  Started
 Container curso-vscode-m4-compose-api-1  Waiting
 Container curso-vscode-m4-compose-api-1  Healthy
 Container curso-vscode-m4-compose-monitor-1  Started
```

Observa el orden: el monitor no arranca hasta que la API está **Healthy**.

### Paso 5. Consultar los contenedores

**VS Code:** en la vista **Containers**, despliega el grupo `curso-vscode-m4-compose`.

**Terminal:**

```bash
docker compose ps
```

**Resultado esperado:**

```text
NAME                                IMAGE                         ...   SERVICE   STATUS                    PORTS
curso-vscode-m4-compose-api-1       curso-m4/tareas-compose:1.0   ...   api       Up 30 seconds (healthy)   127.0.0.1:18081->8080/tcp
curso-vscode-m4-compose-monitor-1   alpine:3.24                   ...   monitor   Up 20 seconds
```

El monitor no tiene columna `PORTS`: no es accesible desde tu equipo, y no lo necesita.

### Paso 6. Examinar los logs

**VS Code:** clic derecho sobre el grupo > **Compose Logs**. Para un solo servicio, clic derecho sobre su contenedor > **View Logs**.

**Terminal:**

```bash
docker compose logs api
docker compose logs -f monitor
```

Sal de `-f` con `Ctrl + C`.

**Resultado esperado:** en `api`, el arranque de Spring Boot. En `monitor`, una línea cada 10 segundos:

```text
monitor-1  | 06:59:03 {"estado":"OK","mensaje":"API de tareas gestionada con Docker Compose","tareas":3,"host":"4cfb044a480b","java":"21.0.13"}
```

Cada línea lleva el prefijo del servicio. `docker compose logs`, sin servicio, mezcla los de todos.

### Paso 7. Comprobar la red interna

```bash
docker network ls --filter name=curso-vscode-m4-compose
docker compose exec monitor getent hosts api
docker compose exec monitor wget -qO- http://api:8080/api/tareas/1
docker compose exec monitor wget -qO- -T 3 http://localhost:8080/api/estado
```

**Resultado esperado:**

1. La red `curso-vscode-m4-compose_tareas`.
2. `172.24.0.2   api`: la IP interna de la API. En tu equipo será otra; por eso se usa el nombre y nunca la IP.
3. La tarea 1 en JSON: el monitor llega a la API por su nombre.
4. `Connection refused`: dentro del monitor, `localhost` es el propio monitor.

`docker compose exec <servicio>` es el equivalente de `docker exec` usando el nombre del servicio. En VS Code: **Attach Shell** sobre el contenedor del monitor.

### Paso 8. Realizar peticiones HTTP

**Linux o WSL (Bash):**

```bash
curl -s http://localhost:18081/api/estado
curl -s -i -X POST -H "Content-Type: application/json" -d @peticiones/crear-tarea.json http://localhost:18081/api/tareas
```

**Windows (PowerShell):**

```powershell
Invoke-RestMethod http://localhost:18081/api/estado
Invoke-RestMethod -Uri http://localhost:18081/api/tareas -Method Post -ContentType "application/json" -Body (Get-Content -Raw peticiones/crear-tarea.json)
```

O con **REST Client** desde `peticiones/tareas.http`.

**Resultado esperado:** `"mensaje":"API de tareas gestionada con Docker Compose"` y `201` con la tarea 4. En el log del monitor, la siguiente línea muestra `"tareas":4`.

### Paso 9. Cambiar una variable de configuración

En `compose.yaml`, cambia el valor de `APP_MENSAJE` del servicio `api`:

```yaml
      APP_MENSAJE: "Mensaje cambiado en compose.yaml"
```

Guarda.

### Paso 10. Recrear el servicio afectado

**Terminal:**

```bash
docker compose up -d
```

En VS Code, **Compose Up** hace lo mismo.

**Resultado esperado:**

```text
 Container curso-vscode-m4-compose-api-1  Recreate
 Container curso-vscode-m4-compose-api-1  Recreated
 Container curso-vscode-m4-compose-monitor-1  Running
 ...
 Container curso-vscode-m4-compose-api-1  Healthy
```

Compose compara la configuración con la de los contenedores existentes y **solo recrea el que ha cambiado**. No hace falta `--build`: el código no ha cambiado.

Mira los logs del monitor (`docker compose logs -f monitor`). Durante unos segundos puede mostrar `API no disponible`, mientras la API arranca. Después:

```text
monitor-1  | 06:59:45 {"estado":"OK","mensaje":"Mensaje cambiado en compose.yaml","tareas":3,"host":"2346ac59591c",...}
```

Observa tres cambios:

- **El mensaje**: es el nuevo.
- **El `host`**: es otro contenedor.
- **`"tareas":3`**: la tarea creada en el paso 8 se ha perdido, porque estaba en la memoria del contenedor anterior.

Recrear un contenedor descarta su estado. Por eso los datos que deben persistir se guardan en una base de datos con volúmenes, nunca dentro del contenedor.

### Paso 11. Detener los servicios

**VS Code:** clic derecho sobre el grupo > **Compose Stop**.

**Terminal:**

```bash
docker compose stop
docker compose ps -a
```

**Resultado esperado:** los dos contenedores en estado `Exited`, pero **siguen existiendo**, y también la red. `docker compose start`, o **Compose Start**, los vuelve a arrancar tal como estaban.

### Paso 12. Eliminar los recursos de la práctica

**VS Code:** clic derecho sobre `compose.yaml` > **Compose Down**.

**Terminal:**

```bash
docker compose down
docker compose ps -a
docker network ls --filter name=curso-vscode-m4-compose
```

**Resultado esperado:**

```text
 Container curso-vscode-m4-compose-monitor-1  Removed
 Container curso-vscode-m4-compose-api-1  Removed
 Network curso-vscode-m4-compose_tareas  Removed
```

Los dos listados posteriores salen vacíos. `down` solo afecta al proyecto `curso-vscode-m4-compose`: no toca contenedores, redes ni volúmenes de otros proyectos. La imagen construida se conserva: ver el apartado 13.

## 8. Comandos equivalentes

| Acción | VS Code | Terminal |
|---|---|---|
| Validar | Avisos en el editor | `docker compose config` |
| Levantar y construir | **Compose Up** | `docker compose up -d --build` |
| Estado | Grupo del proyecto en **Containers** | `docker compose ps` |
| Logs | **Compose Logs** / **View Logs** | `docker compose logs -f [servicio]` |
| Terminal en un servicio | **Attach Shell** | `docker compose exec <servicio> sh` |
| Aplicar cambios de configuración | **Compose Up** | `docker compose up -d` |
| Reiniciar sin recrear | **Compose Restart** | `docker compose restart [servicio]` |
| Detener sin eliminar | **Compose Stop** | `docker compose stop` |
| Arrancar lo detenido | **Compose Start** | `docker compose start` |
| Detener y eliminar | **Compose Down** | `docker compose down` |

`docker compose restart` reinicia los contenedores existentes, pero **no** aplica los cambios de `compose.yaml`. Para eso hay que recrearlos con `docker compose up -d`.

## 9. Resultados esperados

| Comprobación | Resultado |
|---|---|
| `docker compose config --services` | `api` y `monitor` |
| `docker compose up -d --build` | `api` Healthy y después `monitor` Started |
| `docker compose ps` | `api` con `(healthy)` y `127.0.0.1:18081->8080/tcp`; `monitor` sin puertos |
| Logs del monitor | Una línea cada 10 s con el JSON de `/api/estado` |
| `getent hosts api` desde el monitor | La IP interna de `api` |
| `localhost:8080` desde el monitor | `Connection refused` |
| Tras cambiar `APP_MENSAJE` y `up -d` | Solo se recrea `api`; mensaje nuevo, `host` nuevo y 3 tareas |
| `stop` | Contenedores `Exited`, red conservada |
| `down` | Contenedores y red eliminados; imagen conservada |

## 10. Problemas frecuentes

| Problema | Causa y solución |
|---|---|
| `no configuration file provided: not found` | No estás en la carpeta que contiene `compose.yaml`. Usa `cd`, o indica el archivo con `-f ruta/compose.yaml`. |
| `yaml: line N: ...` o `services.api.ports must be a list` | Error de sangría o de sintaxis YAML. Usa espacios, nunca tabuladores, y revisa los avisos de VS Code. |
| `port is already allocated` | El 18081 está ocupado. Cambia el puerto del equipo en `ports` (apartado 4.3). |
| `dependency failed to start: container ... is unhealthy` | El *healthcheck* de `api` no ha respondido a tiempo. Mira `docker compose logs api`. |
| El monitor muestra `API no disponible` todo el rato | La API no arranca, o has cambiado `URL_API`. Debe ser `http://api:8080/...`: nombre del servicio y puerto **interno**. |
| `docker-compose: command not found` | Es el comando antiguo (V1). Usa `docker compose`. |
| El cambio de `APP_MENSAJE` no se aplica | Has usado `docker compose restart`, que no recrea. Usa `docker compose up -d`. |
| El cambio en el código Java no se aplica | Las imágenes no se reconstruyen solas: `docker compose up -d --build`. |
| `docker compose stop` tarda 10 segundos con un servicio propio | El proceso principal no atiende la señal de parada: usa la forma JSON en `ENTRYPOINT` o `command`, o `init: true`. |

## 11. Comparación con Eclipse e IntelliJ IDEA

| Función | Eclipse | IntelliJ IDEA | VS Code |
|---|---|---|---|
| Editor de `compose.yaml` | Editor YAML genérico | Autocompletado y validación del esquema de Compose | Autocompletado y validación de Container Tools |
| Levantar la composición | Desde la terminal | ▷ junto a `services` o configuración **Docker Compose** | **Compose Up** con clic derecho |
| Ver los servicios | Vista **Docker Explorer** del *plugin* | Ventana **Services**, agrupada por proyecto | Vista **Containers**, agrupada por proyecto |
| Logs por servicio | Sí, con el *plugin* | Sí | **View Logs** / **Compose Logs** |

## 12. Comprobaciones finales

Comprueba que eres capaz de:

- [ ] Explicar qué aporta Compose frente a varios `docker run`.
- [ ] Identificar en `compose.yaml` servicios, construcción, imagen, puertos, variables, red y *healthcheck*.
- [ ] Explicar por qué el monitor usa `api:8080` y tu navegador `localhost:18081`.
- [ ] Explicar para qué sirve `depends_on` con `condition: service_healthy`.
- [ ] Levantar, consultar y ver los logs de la composición desde VS Code y desde la terminal.
- [ ] Aplicar un cambio de configuración recreando solo el servicio afectado.
- [ ] Explicar por qué se pierden las tareas al recrear la API.
- [ ] Diferenciar `stop`, `down` y `down -v`, y saber cuándo **no** usar `down -v`.

## 13. Limpieza segura

Desde la carpeta `compose-4.3`:

```bash
docker compose down
docker rmi curso-m4/tareas-compose:1.0
```

Si ya no la necesitas, elimina también la imagen del monitor:

```bash
docker rmi alpine:3.24
```

`docker compose down` solo actúa sobre el proyecto `curso-vscode-m4-compose`, por el `name` del archivo. No uses `down -v` en otros proyectos sin saber qué volúmenes borraría, ni comandos de limpieza global como `docker system prune` o `docker volume prune`.
