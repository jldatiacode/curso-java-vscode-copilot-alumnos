# Ejercicio 4.2 — Dockerizar una aplicación Spring Boot

Módulo 4. Contenerización en VS Code · Tema 4.2. Dockerfile · Apartado 4.2.1. Dockerizar aplicaciones Java

Duración estimada: 1 hora y 30 minutos.

## 1. Introducción

En el apartado 3.5 la API de tareas se ejecutaba con `java -jar`, lo que exige tener Java 21 instalado en cada equipo y servidor. En este apartado la empaquetarás en una **imagen Docker**: un artefacto que contiene el JAR y también el entorno de ejecución de Java. Cualquier equipo con Docker podrá ejecutarla igual.

El proyecto es la misma API del apartado 3.5, en el paquete `curso.docker`, con un *endpoint* más: `GET /api/estado`, que sirve para comprobar que el contenedor responde.

## 2. Objetivos

Al terminar serás capaz de:

- Leer y explicar un Dockerfile multietapa para una aplicación Spring Boot.
- Diferenciar la construcción de la imagen (`docker build`) de su ejecución (`docker run`).
- Construir la imagen desde VS Code y desde la terminal.
- Ejecutar la API en un contenedor, publicar su puerto y probarla.
- Configurar la aplicación con variables de entorno sin reconstruir la imagen.
- Aprovechar la caché de capas al reconstruir.
- Diagnosticar problemas con los logs y `docker inspect`.
- Aplicar buenas prácticas básicas de seguridad en las imágenes.

## 3. Conceptos fundamentales

### Construir frente a ejecutar

| Fase | Comando | Entrada | Resultado |
|---|---|---|---|
| **Construcción** (*build*) | `docker build -t curso-m4/tareas-api:1.0 .` | Dockerfile y **contexto** (los archivos de la carpeta) | Una imagen |
| **Ejecución** (*run*) | `docker run ... curso-m4/tareas-api:1.0` | Una imagen y opciones de ejecución (puertos, variables) | Un contenedor |

Una imagen se construye una vez y se ejecuta tantas veces como haga falta, con configuraciones distintas.

### Instrucciones del Dockerfile

| Instrucción | Qué hace | En el proyecto |
|---|---|---|
| `FROM` | Imagen de partida. Con `AS` se da nombre a una etapa. | `maven:3.9.16-eclipse-temurin-21-noble AS construccion` y `eclipse-temurin:21-jre-noble` |
| `WORKDIR` | Carpeta de trabajo para las instrucciones siguientes, y al arrancar | `/proyecto` y `/app` |
| `COPY` | Copia archivos del contexto, o de otra etapa con `--from`, a la imagen | `pom.xml`, `src` y el JAR |
| `RUN` | Ejecuta un comando **al construir** la imagen | `mvn ... package`, crear el usuario |
| `ENV` | Variable de entorno con un valor por defecto, disponible al ejecutar | `APP_MENSAJE` |
| `EXPOSE` | **Documenta** el puerto en el que escucha la aplicación. No lo publica. | `8080` |
| `USER` | Usuario con el que se ejecutan el resto de instrucciones y el contenedor | `app` |
| `ENTRYPOINT` | Comando que se ejecuta **al arrancar** el contenedor | `java -jar app.jar` |

### Construcción multietapa

El Dockerfile tiene dos etapas:

```text
Etapa "construccion"                      Etapa final (la imagen que se ejecuta)
maven:3.9.16-eclipse-temurin-21-noble     eclipse-temurin:21-jre-noble
  Maven + JDK 21                            Solo el JRE 21
  pom.xml, src/                             usuario app
  mvn package -> tareas-docker.jar  ---->   /app/app.jar
```

Solo la última etapa forma la imagen final. Maven, el JDK, el código fuente y el repositorio de dependencias se quedan en la etapa de construcción. Resultado:

| Imagen | Tamaño de descarga |
|---|---|
| `maven:3.9.16-eclipse-temurin-21-noble` (construcción) | unos 250 MB |
| `curso-m4/tareas-api:1.0` (final) | unos 120 MB, de los que 22 MB son el JAR |

La imagen final es más pequeña y tiene menos superficie de ataque: un atacante que entrara en el contenedor no encontraría compiladores ni herramientas de construcción.

### Imágenes base elegidas

| Imagen | Por qué |
|---|---|
| `maven:3.9.16-eclipse-temurin-21-noble` | Imagen oficial de Maven con Eclipse Temurin 21 (JDK) sobre Ubuntu 24.04. Versión concreta de Maven y de Java. |
| `eclipse-temurin:21-jre-noble` | Imagen oficial de Eclipse Temurin: solo el entorno de ejecución de Java 21, sobre la misma Ubuntu 24.04. |

Ninguna usa `latest`. La etiqueta `21-jre-noble` se actualiza con las correcciones de seguridad de Java 21 (21.0.13, 21.0.14…), pero nunca cambia de versión principal.

### Capas y caché

Cada instrucción del Dockerfile genera una **capa**. Al reconstruir, Docker reutiliza las capas cuyas entradas no han cambiado (**caché**) y vuelve a ejecutar la primera que cambia y todas las siguientes. Por eso el orden importa:

```dockerfile
COPY pom.xml .
RUN mvn -B -q dependency:go-offline   # Solo se repite si cambia el pom.xml
COPY src ./src
RUN mvn -B -q package -DskipTests     # Se repite si cambia el código
```

Si cambias una clase Java, las dependencias (decenas de megabytes) no se vuelven a descargar.

### `.dockerignore`

Al construir, Docker envía al *daemon* la carpeta del proyecto: el **contexto**. `.dockerignore` excluye lo que no hace falta, con la misma sintaxis que `.gitignore`:

- **`target/`**: la imagen compila su propio JAR y no debe depender de lo que haya en tu equipo;
- **`.git/` y la configuración de los editores**: no hacen falta y pueden ocupar mucho;
- **`.env` y `*.log`**: pueden contener datos sensibles.

### Variables de entorno

Spring Boot lee la configuración de varias fuentes. Una variable de entorno tiene prioridad sobre `application.properties`. La propiedad `app.mensaje` se puede sustituir con la variable `APP_MENSAJE`:

| Origen | Valor de `app.mensaje` |
|---|---|
| `application.properties` | `API de tareas en ejecución` |
| `ENV` del Dockerfile (por defecto en la imagen) | `API de tareas en un contenedor Docker` |
| `docker run -e APP_MENSAJE="..."` | El que indiques al ejecutar |

Así se cambia la configuración **sin reconstruir la imagen**. Lo mismo vale para `SERVER_PORT` o cualquier otra propiedad.

### Buenas prácticas de seguridad aplicadas

| Práctica | En el Dockerfile |
|---|---|
| Imágenes base oficiales y con versión | `maven:3.9.16-...` y `eclipse-temurin:21-jre-noble` |
| Sin herramientas de construcción en la imagen final | Construcción multietapa |
| Usuario sin privilegios | `useradd ... app` y `USER app` |
| Sin secretos en la imagen | Nada sensible en `ENV`, `ARG` ni `COPY` |
| Sin archivos innecesarios | `.dockerignore` |
| Puerto solo para el equipo local | `-p 127.0.0.1:18080:8080` al ejecutar |

**Nunca** escribas contraseñas o *tokens* en un `ENV` o un `ARG`, ni copies archivos `.env` a la imagen: cualquiera que tenga la imagen puede leerlos con `docker inspect` o `docker history`. Los secretos se pasan al ejecutar, desde un gestor de secretos o variables del entorno de despliegue.

## 4. Preparación del entorno

### 4.1. Requisitos

- Docker y Container Tools funcionando, como en el apartado 4.1.
- JDK 21 y Maven, solo para el paso 2 (comparar con la construcción local). La imagen **no** usa el Java de tu equipo.
- Conexión a Internet la primera vez: se descargan la imagen de Maven (unos 250 MB) y las dependencias del proyecto.

### 4.2. Actualizar el material y copiar el proyecto

En la raíz de tu repositorio del curso, con `git status` sin cambios pendientes:

```bash
git status
git pull --ff-only
```

**Windows (PowerShell):**

```powershell
New-Item -ItemType Directory -Force "$HOME/practicas-docker" | Out-Null
Copy-Item -Recurse modulo-04-docker/tema-4.2-dockerfile/apartado-4.2.1-dockerizar-java/proyecto "$HOME/practicas-docker/tareas-4.2"
code "$HOME/practicas-docker/tareas-4.2"
```

**Linux o WSL (Bash):**

```bash
mkdir -p ~/practicas-docker
cp -r modulo-04-docker/tema-4.2-dockerfile/apartado-4.2.1-dockerizar-java/proyecto ~/practicas-docker/tareas-4.2
code ~/practicas-docker/tareas-4.2
```

En PowerShell, `Copy-Item` también copia los archivos ocultos `.dockerignore` y `.gitignore`. Compruébalo en el **Explorer** de VS Code.

### 4.3. Nombres y puertos

| Recurso | Nombre |
|---|---|
| Imagen | `curso-m4/tareas-api` con las etiquetas `1.0` y `1.1` |
| Contenedor | `curso-m4-tareas` |
| Puerto | **18080** del equipo → **8080** del contenedor |

Spring Boot escucha en el **8080 dentro del contenedor**. En tu equipo se publica en el **18080** para no chocar con otra aplicación que use el 8080, por ejemplo la del apartado 3.5. Comprueba que el 18080 está libre:

**Linux o WSL (Bash):**

```bash
ss -ltn | grep ':18080 ' || echo "18080 libre"
```

**Windows (PowerShell):**

```powershell
if (Get-NetTCPConnection -LocalPort 18080 -State Listen -ErrorAction SilentlyContinue) { "18080 ocupado" } else { "18080 libre" }
```

Si está ocupado, elige otro, como 18090, y úsalo en lugar de 18080 en todos los comandos y en `peticiones/tareas.http`. El número del contenedor, 8080, no cambia.

## 5. Herramientas de VS Code

| Necesitas | Herramienta |
|---|---|
| Editar el Dockerfile | Container Tools: colores, autocompletado de instrucciones y avisos de problemas |
| Construir la imagen | Clic derecho en `Dockerfile` > **Build Image...**, o `Container Images: Build Image...` |
| Ver la imagen | Vista **Containers** > sección **Images** |
| Ver logs y entrar | **View Logs**, **Attach Shell**, **Inspect** |
| Probar la API | Terminal (`curl`, `Invoke-RestMethod`) o **REST Client** con `peticiones/tareas.http` |

Container Tools también ofrece `Containers: Add Docker Files to Workspace...`, que genera un Dockerfile de partida. En este apartado no se usa: el proyecto ya incluye uno, comentado línea a línea.

## 6. El Dockerfile del proyecto

Ábrelo en VS Code y léelo junto con esta tabla:

```dockerfile
FROM maven:3.9.16-eclipse-temurin-21-noble AS construccion
WORKDIR /proyecto
COPY pom.xml .
RUN mvn -B -q dependency:go-offline
COPY src ./src
RUN mvn -B -q package -DskipTests

FROM eclipse-temurin:21-jre-noble
RUN groupadd --system app && useradd --system --gid app --no-create-home app
WORKDIR /app
COPY --from=construccion /proyecto/target/tareas-docker.jar app.jar
ENV APP_MENSAJE="API de tareas en un contenedor Docker"
EXPOSE 8080
USER app
ENTRYPOINT ["java", "-jar", "app.jar"]
```

- **`-DskipTests`**: las pruebas se ejecutan antes, con `mvn test` o desde la vista Testing. Repetirlas en cada construcción de la imagen la haría más lenta.
- **`tareas-docker.jar`**: el `pom.xml` fija ese nombre con `<finalName>`, para que el Dockerfile no dependa de la versión.
- **`ENTRYPOINT` en forma JSON**: Java es el proceso principal (PID 1) y recibe la señal de parada de `docker stop`. Spring Boot cierra ordenadamente.

## 7. Práctica guiada

Trabaja en la terminal de VS Code, en tu copia `tareas-4.2`.

### Paso 1. Examinar el proyecto

1. Abre `pom.xml`: es el del apartado 3.5, con Spring Boot 4.1.2, sin DevTools y con `<finalName>`.
2. Abre `EstadoController`: el *endpoint* `GET /api/estado` devuelve `mensaje` (de `app.mensaje`), `host` y la versión de Java.
3. Abre `src/main/resources/application.properties` y localiza `app.mensaje`.

### Paso 2. Construir el JAR en tu equipo

```bash
mvn package
```

**Resultado esperado:** `Tests run: 13, Failures: 0` y `BUILD SUCCESS`, y el archivo `target/tareas-docker.jar`.

Este JAR es el que se ejecutaba con `java -jar` en el módulo 3. La imagen **no lo usa**: `.dockerignore` excluye `target/` y la imagen compila el suyo dentro de la etapa `construccion`. Así la imagen no depende del Java ni del Maven de tu equipo.

### Paso 3. Examinar el Dockerfile y `.dockerignore`

Lee los dos archivos. Observa que Container Tools resalta las instrucciones y, al pasar el ratón sobre una, muestra su documentación.

Responde: ¿qué instrucciones se ejecutan al **construir** y cuáles al **arrancar** el contenedor?

### Paso 4. Construir la imagen

**VS Code:** clic derecho sobre `Dockerfile` > **Build Image...**. Cuando pida la etiqueta, escribe `curso-m4/tareas-api:1.0` y pulsa `Enter`.

**Terminal:**

```bash
docker build -t curso-m4/tareas-api:1.0 .
```

El punto final es el **contexto**: la carpeta actual.

**Resultado esperado:** la primera vez tarda uno o dos minutos, porque descarga la imagen de Maven y las dependencias. Al final aparece la línea `naming to docker.io/curso-m4/tareas-api:1.0`.

### Paso 5. Consultar la imagen

**VS Code:** sección **Images** > `curso-m4/tareas-api` > `1.0`. Clic derecho > **Inspect**: busca `"User": "app"`, `"ExposedPorts"` y `"Env"`, con `APP_MENSAJE`.

**Terminal:**

```bash
docker images curso-m4/tareas-api
docker history curso-m4/tareas-api:1.0
```

`docker history` muestra las capas de la imagen final. No aparece ninguna de Maven: son de la otra etapa.

### Paso 6. Ejecutar el contenedor publicando el puerto

```bash
docker run -d --name curso-m4-tareas -p 127.0.0.1:18080:8080 curso-m4/tareas-api:1.0
docker ps
```

**Resultado esperado:**

```text
CONTAINER ID   IMAGE                     ...   STATUS         PORTS                       NAMES
8b7bbcf256d6   curso-m4/tareas-api:1.0   ...   Up 3 seconds   127.0.0.1:18080->8080/tcp   curso-m4-tareas
```

`-p 127.0.0.1:18080:8080` significa: las conexiones al puerto 18080 de **este equipo** van al puerto 8080 del contenedor.

### Paso 7. Probar la API

**Linux o WSL (Bash):**

```bash
curl -s http://localhost:18080/api/estado
curl -s http://localhost:18080/api/tareas
curl -s -i http://localhost:18080/api/tareas/999
```

**Windows (PowerShell):**

```powershell
Invoke-RestMethod http://localhost:18080/api/estado
Invoke-RestMethod http://localhost:18080/api/tareas
curl.exe -s -i http://localhost:18080/api/tareas/999
```

También puedes usar `peticiones/tareas.http` con **REST Client**.

**Resultado esperado:**

```text
{"estado":"OK","mensaje":"API de tareas en un contenedor Docker","tareas":3,"host":"8b7bbcf256d6","java":"21.0.13"}

[{"id":1,"titulo":"Compilar la API con Maven",...},{"id":2,"titulo":"Construir la imagen Docker",...},
 {"id":3,"titulo":"Ejecutar la API en un contenedor",...}]

HTTP/1.1 404
{"detail":"No existe la tarea 999","instance":"/api/tareas/999","status":404,"title":"Not Found"}
```

Fíjate en dos campos de `/api/estado`:

- **`host`**: es el identificador corto del contenedor, el mismo que muestra `docker ps`;
- **`mensaje`**: viene del `ENV` del Dockerfile, no de `application.properties`.

### Paso 8. Consultar los logs

**VS Code:** clic derecho sobre `curso-m4-tareas` > **View Logs**.

**Terminal:**

```bash
docker logs curso-m4-tareas
```

**Resultado esperado:** el arranque normal de Spring Boot:

```text
 :: Spring Boot ::                (v4.1.2)
... Starting AplicacionTareas v1.0.0-SNAPSHOT using Java 21.0.13 with PID 1 (/app/app.jar started by app in /app)
... Tomcat started on port 8080 (http) with context path '/'
... Started AplicacionTareas in 1.8 seconds
```

`PID 1` y `started by app` confirman que Java es el proceso principal y que se ejecuta con el usuario sin privilegios.

### Paso 9. Comprobar qué hay dentro

**VS Code:** clic derecho > **Attach Shell**. **Terminal:** `docker exec -it curso-m4-tareas sh`.

Dentro del contenedor:

```sh
id
ls -l /app
java -version
mvn -version
exit
```

**Resultado esperado:** `uid=999(app)`, solo `app.jar` en `/app`, Java 21, y `mvn: not found`, porque Maven no está en la imagen final.

### Paso 10. Cambiar la configuración sin reconstruir

Elimina el contenedor y crea otro con una variable de entorno distinta:

```bash
docker rm -f curso-m4-tareas
docker run -d --name curso-m4-tareas -p 127.0.0.1:18080:8080 -e APP_MENSAJE="Hola desde la variable de entorno" curso-m4/tareas-api:1.0
```

Espera unos segundos y consulta de nuevo `/api/estado`.

**Resultado esperado:** `"mensaje":"Hola desde la variable de entorno"` y un `host` distinto, porque es otro contenedor creado con la **misma imagen**.

### Paso 11. Modificar la aplicación y reconstruir

1. En `EstadoController`, debajo de `estado.put("estado", "OK");`, añade:

   ```java
           estado.put("version", "1.1");
   ```

2. Guarda y construye una imagen con una **etiqueta nueva**:

   ```bash
   docker build -t curso-m4/tareas-api:1.1 .
   ```

   **Resultado esperado:** mucho más rápido que la primera vez. Los pasos hasta `dependency:go-offline` aparecen como `CACHED`. Solo se repiten `COPY src`, el empaquetado y la copia del JAR.

3. Sustituye el contenedor por uno de la versión 1.1:

   ```bash
   docker rm -f curso-m4-tareas
   docker run -d --name curso-m4-tareas -p 127.0.0.1:18080:8080 curso-m4/tareas-api:1.1
   ```

### Paso 12. Comprobar el cambio

Consulta `/api/estado`.

**Resultado esperado:** `{"estado":"OK","version":"1.1","mensaje":"API de tareas en un contenedor Docker",...}`.

En la sección **Images** hay ahora dos etiquetas, `1.0` y `1.1`. Etiquetar cada versión permite volver a la anterior con un `docker run` de `:1.0`.

### Paso 13. Detener y eliminar el contenedor

**VS Code:** clic derecho > **Stop** y después **Remove...**.

**Terminal:**

```bash
docker stop curso-m4-tareas
docker ps -a --filter name=curso-m4-tareas
docker rm curso-m4-tareas
```

**Resultado esperado:** `docker stop` tarda menos de un segundo y el estado es `Exited (143)`. 143 = 128 + 15, la señal de parada (SIGTERM): no es un error. En los logs, Spring Boot registra `Graceful shutdown complete`.

## 8. Comandos equivalentes

| Acción | VS Code | Terminal |
|---|---|---|
| Construir la imagen | Clic derecho en `Dockerfile` > **Build Image...** | `docker build -t curso-m4/tareas-api:1.0 .` |
| Ver capas | — | `docker history curso-m4/tareas-api:1.0` |
| Inspeccionar la imagen | **Images** > **Inspect** | `docker image inspect curso-m4/tareas-api:1.0` |
| Ejecutar | — (para indicar puerto y variables) | `docker run -d --name curso-m4-tareas -p 127.0.0.1:18080:8080 curso-m4/tareas-api:1.0` |
| Variable de entorno | — | `docker run ... -e APP_MENSAJE="..." ...` |
| Ver logs | **View Logs** | `docker logs -f curso-m4-tareas` |
| Inspeccionar el contenedor | **Inspect** | `docker inspect curso-m4-tareas` |
| Terminal dentro | **Attach Shell** | `docker exec -it curso-m4-tareas sh` |
| Detener y eliminar | **Stop**, **Remove...** | `docker stop` y `docker rm`, o `docker rm -f` |

## 9. Resultados esperados

| Comprobación | Resultado |
|---|---|
| `mvn package` | 13 pruebas correctas y `target/tareas-docker.jar` |
| `docker build` | Imagen `curso-m4/tareas-api:1.0` de unos 120 MB de descarga |
| `docker ps` | `127.0.0.1:18080->8080/tcp` |
| `GET /api/estado` | `"estado":"OK"`, `host` igual al ID del contenedor |
| `GET /api/tareas/999` | 404 con *Problem Details* |
| Logs | `started by app`, `Tomcat started on port 8080` |
| Dentro del contenedor | Usuario `app`, sin Maven |
| `-e APP_MENSAJE` | Mensaje cambiado sin reconstruir |
| Reconstrucción | Dependencias `CACHED`; `/api/estado` con `"version":"1.1"` |
| `docker stop` | `Exited (143)` y `Graceful shutdown complete` |

## 10. Problemas frecuentes

| Problema | Causa y solución |
|---|---|
| `port is already allocated` | El 18080 está ocupado, quizá por el contenedor anterior. Comprueba `docker ps`; si es otro programa, usa otro puerto del equipo: `-p 127.0.0.1:18090:8080`. |
| `Conflict. The container name "/curso-m4-tareas" is already in use` | Ya existe, quizá detenido. `docker rm -f curso-m4-tareas` y repite. |
| `curl: (52) Empty reply from server` o conexión rechazada justo después de `docker run` | Spring Boot tarda uno o dos segundos en arrancar. Espera y repite, o mira **View Logs**. |
| El contenedor aparece como `Exited (1)` nada más arrancar | La aplicación ha fallado al arrancar. `docker logs curso-m4-tareas` muestra la excepción. |
| `docker build` falla en `dependency:go-offline` o `package` | Normalmente, falta de conexión con Maven Central. Comprueba la red y repite: los pasos correctos quedan en caché. |
| `COPY failed: ... target/tareas-docker.jar: not found` | Has cambiado `artifactId` o `<finalName>` en el `pom.xml` sin actualizar el Dockerfile. |
| El cambio del paso 11 no aparece | Has arrancado `:1.0` en lugar de `:1.1`, o no has guardado el archivo antes de `docker build`. |
| **Build Image...** propone una etiqueta como `tareas42:latest` | Es su valor por defecto: sustitúyelo por `curso-m4/tareas-api:1.0` |
| `exec format error` al ejecutar la imagen en otro equipo | La imagen se construyó para otra arquitectura (por ejemplo, ARM en un Mac). Constrúyela en el equipo de destino. |

## 11. Comparación con Eclipse e IntelliJ IDEA

| Función | Eclipse | IntelliJ IDEA | VS Code |
|---|---|---|---|
| Editor de Dockerfile | Básico (con el *plugin* Docker Tooling) | Completo, con autocompletado | Container Tools: autocompletado, documentación al pasar el ratón y avisos |
| Construir imagen | Desde el *plugin* | ▷ junto a `FROM` o configuración **Dockerfile** | **Build Image...** |
| Ejecutar con opciones | Configuración de lanzamiento | Configuración de ejecución **Dockerfile**, con puertos y variables | Terminal (`docker run`) o Compose (apartado 4.3) |

Con independencia del IDE, Spring Boot ofrece `mvn spring-boot:build-image`, que crea una imagen sin Dockerfile usando *Cloud Native Buildpacks*. Es cómoda, pero oculta los detalles. En el curso se usa un Dockerfile para entender cada paso y controlar las imágenes base.

## 12. Comprobaciones finales

Comprueba que eres capaz de:

- [ ] Explicar cada instrucción del Dockerfile y si actúa al construir o al ejecutar.
- [ ] Explicar por qué la imagen final no contiene Maven ni el código fuente.
- [ ] Ordenar las instrucciones para aprovechar la caché.
- [ ] Explicar para qué sirve `.dockerignore` y por qué excluye `target/`.
- [ ] Construir una imagen con una etiqueta concreta, desde VS Code y desde la terminal.
- [ ] Distinguir el puerto del contenedor (8080) del puerto publicado en el equipo (18080).
- [ ] Cambiar la configuración con `-e` sin reconstruir.
- [ ] Diagnosticar un contenedor con sus logs y `docker inspect`.
- [ ] Enumerar las buenas prácticas de seguridad aplicadas.

## 13. Limpieza segura

Elimina el contenedor, si existe, y las imágenes de la práctica:

```bash
docker rm -f curso-m4-tareas
docker rmi curso-m4/tareas-api:1.0 curso-m4/tareas-api:1.1
```

Puedes **conservar** `maven:3.9.16-eclipse-temurin-21-noble` y `eclipse-temurin:21-jre-noble`: las vuelven a usar los apartados 4.3 y 4.5. Si necesitas espacio, elimínalas por su nombre al terminar el módulo:

```bash
docker rmi maven:3.9.16-eclipse-temurin-21-noble eclipse-temurin:21-jre-noble
```

La caché de construcción ocupa espacio adicional. Puedes ver cuánto con `docker system df`. No la vacíes con `docker builder prune` ni `docker system prune` si tienes otros proyectos Docker en el equipo: afecta a todos.

En tu copia, `target/` se puede borrar sin problema: `mvn clean`.
