# Ejercicio 3.5 — API REST con Spring Boot

Módulo 3. Integración de desarrollos en Java · Tema 3.5. Spring Boot · Apartado 3.5.1. API REST con Spring

Duración estimada: 2 horas y 30 minutos.

## 1. Introducción

En el apartado 3.4 hubo que descargar Tomcat, empaquetar un WAR, copiarlo y arrancar el servidor. **Spring Boot** simplifica ese proceso:

- la aplicación incluye su propio servidor (Tomcat embebido);
- se configura sola a partir de las dependencias;
- se ejecuta como cualquier programa Java, con **Run**.

Este apartado desarrolla una **API REST de tareas** y se centra en las herramientas de VS Code para Spring Boot:

- crear proyectos;
- ejecutarlos desde el **Spring Boot Dashboard**;
- depurar peticiones;
- probar los *endpoints*;
- el reinicio automático con DevTools.

No es un curso de Spring: se usan solo las piezas imprescindibles de una API REST.

## 2. Objetivos

Al terminar serás capaz de:

- Explicar qué aporta Spring Boot y qué son los *starters*.
- Crear un proyecto con Spring Initializr desde VS Code y reconocer su estructura.
- Leer un controlador REST: `@RestController`, `@GetMapping`, `@PostMapping`, `@PatchMapping`, `@PathVariable`, `@RequestBody`.
- Explicar la inyección de dependencias por constructor y el papel de `application.properties`.
- Ejecutar, parar y depurar la aplicación desde VS Code.
- Probar los *endpoints* con `curl`, PowerShell y archivos `.http`, e interpretar los códigos HTTP.
- Ejecutar las pruebas de la API con MockMvc.

## 3. Conceptos fundamentales

### Spring Boot

| Sin Spring Boot (apartado 3.4) | Con Spring Boot |
|---|---|
| Instalar y configurar un servidor | Tomcat embebido en la aplicación |
| WAR desplegado en el servidor | JAR ejecutable: `java -jar` |
| Elegir versiones compatibles de cada biblioteca | El *parent* `spring-boot-starter-parent` fija versiones probadas juntas |
| Configurar cada pieza | **Autoconfiguración**: si está `spring-boot-starter-webmvc`, configura Spring MVC, Jackson y Tomcat |

### *Starters*

Un *starter* es una dependencia que agrupa todo lo necesario para una función:

| *Starter* | Aporta |
|---|---|
| `spring-boot-starter-webmvc` | Spring MVC, Jackson (JSON) y Tomcat embebido |
| `spring-boot-starter-validation` | Bean Validation (`@NotBlank`, `@Size`, `@NotNull`) |
| `spring-boot-devtools` | Reinicio automático al recompilar, solo en desarrollo |
| `spring-boot-starter-webmvc-test` | Pruebas de Spring MVC con MockMvc. Incluye `spring-boot-starter-test`: JUnit, AssertJ y Mockito |

En Spring Boot 4 cada tecnología tiene su *starter* de pruebas (`spring-boot-starter-<tecnología>-test`). Por eso el proyecto declara `spring-boot-starter-webmvc-test` en lugar del genérico `spring-boot-starter-test`, que llega de forma transitiva.

### Versión de Spring Boot

Este proyecto usa **Spring Boot 4.1.2**, la versión estable que propone Spring Initializr. Requiere Java 17 o posterior (el curso usa Java 21) y Maven 3.6.3 o posterior. Se apoya en:

- Spring Framework 7;
- Jakarta EE 11: paquetes `jakarta.*` y Tomcat 11 embebido;
- Jackson 3 para el JSON;
- JUnit 6 (JUnit Jupiter) para las pruebas, con las mismas anotaciones que JUnit 5 (`@Test`…).

Si encuentras ejemplos de Spring Boot 3 en internet, la diferencia más visible está en el `pom.xml`: `spring-boot-starter-web` se llama ahora `spring-boot-starter-webmvc`. El nombre antiguo sigue funcionando, pero está obsoleto.

### Anotaciones del proyecto

| Anotación | Dónde | Significado |
|---|---|---|
| `@SpringBootApplication` | `AplicacionTareas` | Clase principal: autoconfiguración y búsqueda de componentes en `curso.springboot` y subpaquetes |
| `@RestController` | `TareaController` | Clase que atiende peticiones HTTP. Lo que devuelve se convierte en JSON. |
| `@RequestMapping("/api/tareas")` | `TareaController` | Ruta base de todos sus métodos |
| `@GetMapping`, `@PostMapping`, `@PatchMapping` | Métodos del controlador | Método HTTP y ruta que atiende cada uno |
| `@PathVariable` | Parámetro `id` | Valor tomado de la URL (`/api/tareas/{id}`) |
| `@RequestBody` | Parámetro de datos | Cuerpo JSON de la petición convertido en objeto |
| `@Valid` | Junto a `@RequestBody` | Aplica las restricciones del DTO (`@NotBlank`…). Si fallan, responde 400. |
| `@Service` | `TareaService` | Componente de negocio que Spring crea y comparte |
| `@RestControllerAdvice` y `@ExceptionHandler` | `ManejadorErrores` | Convierte excepciones en respuestas HTTP |

### Inyección de dependencias

`TareaController` necesita un `TareaService`, pero no lo crea con `new`: lo recibe en su **constructor**, y Spring se lo proporciona porque `TareaService` es un `@Service`. Así cada clase declara lo que necesita y es fácil sustituirlo en las pruebas.

### Arquitectura del proyecto

```text
src/main/java/curso/springboot/
├── AplicacionTareas.java           @SpringBootApplication, método main
├── controller/
│   ├── TareaController.java        endpoints REST
│   └── ManejadorErrores.java       excepciones de negocio → 409
├── service/
│   └── TareaService.java           lógica y almacenamiento en memoria
├── model/
│   ├── Tarea.java                  record: id, titulo, descripcion, estado
│   └── EstadoTarea.java
└── dto/
    ├── CrearTareaRequest.java      cuerpo de POST (validado)
    └── CambiarEstadoRequest.java   cuerpo de PATCH (validado)
src/main/resources/application.properties
src/test/java/curso/springboot/      pruebas del servicio y de la API (MockMvc)
peticiones/                          tareas.http y cuerpos JSON para curl
```

### *Endpoints* y códigos HTTP

| Método | *Endpoint* | Acción | Respuestas |
|---|---|---|---|
| GET | `/api/tareas` | Listar | 200 |
| GET | `/api/tareas/{id}` | Consultar | 200; 404 si no existe |
| POST | `/api/tareas` | Crear (`{"titulo": ..., "descripcion": ...}`) | 201 con cabecera `Location`; 400 si falta el título |
| PATCH | `/api/tareas/{id}/estado` | Cambiar estado (`{"estado": "EN_PROGRESO"}`) | 200; 404 si no existe; 409 si está completada; 400 si el estado no existe |

Los errores se devuelven en formato estándar **Problem Details** (RFC 9457), activado en `application.properties` con `spring.mvc.problemdetails.enabled=true`.

### `application.properties`

Contiene la configuración de la aplicación: nombre, puerto (`server.port=8080`) y formato de los errores. Spring Boot Tools ofrece autocompletado y documentación de cada propiedad al escribir.

**Nunca** se guardan aquí contraseñas ni *tokens*: se pasan como variables de entorno.

## 4. Preparación del entorno

### 4.1. Extensiones

| Extensión | ¿Necesaria? |
|---|---|
| Extension Pack for Java | Sí (instalada desde el módulo 1) |
| **Spring Boot Extension Pack** (VMware) | Sí. Instala **Spring Boot Tools**, **Spring Initializr Java Support** y **Spring Boot Dashboard**. |
| **REST Client** (Huachao Mao) | Opcional, para ejecutar `peticiones/tareas.http` desde el editor |

Instálalas desde la vista **Extensions**: busca `Spring Boot Extension Pack` y comprueba que el editor es **VMware**. El curso no las instala automáticamente.

### 4.2. Copiar el proyecto

Requisitos: JDK 21 y Maven 3.6.3 o posterior. La primera compilación descarga las dependencias de Spring Boot, unos 30 MB.

En la raíz del repositorio del curso, ejecuta `git status` (sin cambios pendientes) y `git pull --ff-only`. Después:

**Windows (PowerShell):**

```powershell
New-Item -ItemType Directory -Force "$HOME/practicas-java" | Out-Null
Copy-Item -Recurse modulo-03-java/tema-3.5-spring-boot/apartado-3.5.1-api-rest-spring/proyecto "$HOME/practicas-java/spring-3.5"
code "$HOME/practicas-java/spring-3.5"
```

**Linux o WSL (Bash):**

```bash
mkdir -p ~/practicas-java
cp -r modulo-03-java/tema-3.5-spring-boot/apartado-3.5.1-api-rest-spring/proyecto ~/practicas-java/spring-3.5
code ~/practicas-java/spring-3.5
```

El puerto **8080** debe estar libre. Si tienes en marcha el Tomcat del apartado 3.4, detenlo.

## 5. Herramientas de VS Code utilizadas

| Herramienta | Uso |
|---|---|
| Command Palette > `Spring Initializr: Create a Maven Project...` | Crear un proyecto Spring Boot nuevo |
| **Spring Boot Dashboard** (Activity Bar o Explorer) | Lista de aplicaciones Spring Boot con botones **Start**, **Debug** y **Stop** |
| **Run** / **Debug** sobre `main` de `AplicacionTareas` | Arrancar o depurar sin el Dashboard |
| Autocompletado en `application.properties` | Propiedades de Spring Boot con su documentación (Spring Boot Tools) |
| Vista **Testing** | Pruebas JUnit y MockMvc |
| Vista **Maven** | `test`, `package` y objetivo `spring-boot:run` |
| REST Client (opcional) | Enlace **Send Request** sobre cada petición de un archivo `.http` |
| **Simple Browser** | Ver respuestas GET dentro de VS Code |

## 6. Explicaciones técnicas

### Formas de ejecutar la aplicación

| Forma | Comando o acción | Cuándo |
|---|---|---|
| VS Code | **Run**/**Debug** sobre `main`, o **Spring Boot Dashboard** | Desarrollo diario. Permite depurar y usar DevTools. |
| Maven | `mvn spring-boot:run` | Sin IDE. `Ctrl + C` para parar. |
| JAR ejecutable | `mvn package` y `java -jar target/gestor-tareas-api-1.0.0-SNAPSHOT.jar` | Como en producción. El JAR incluye todas las dependencias. |

Al contrario que en el apartado 3.2, `java -jar` **sí funciona**: `spring-boot-maven-plugin` *reempaqueta* el JAR con las dependencias en `BOOT-INF/lib`.

### DevTools y recarga

Con `spring-boot-devtools`, al **recompilar** una clase la aplicación se reinicia sola en uno o dos segundos. En VS Code, guardar un archivo Java lo recompila automáticamente, así que basta con **guardar**.

DevTools solo actúa en desarrollo: se desactiva al ejecutar el JAR con `java -jar`. En el registro, el hilo de arranque se llama `restartedMain` cuando DevTools está activo.

### Peticiones HTTP desde PowerShell y Bash

- **Bash:** `curl` es el programa curl.
- **Windows PowerShell 5.1:** `curl` es un **alias** de `Invoke-WebRequest`, que tiene otra sintaxis. Usa:
  - **`Invoke-RestMethod`**, el *cmdlet* nativo, que convierte el JSON en objetos de PowerShell;
  - o **`curl.exe`**, escrito así, con la extensión. Es el curl incluido en Windows 10 y 11.
- **Cuerpos JSON:** en PowerShell, escribir JSON con comillas dentro de un argumento para `curl.exe` da problemas de escapado. Por eso los cuerpos están en archivos (`peticiones/*.json`) y se envían con `-d "@archivo"`, que funciona igual en los dos *shells*.

## 7. Práctica guiada

### Paso 1. Crear un proyecto con Spring Initializr (demostración)

1. Command Palette > `Spring Initializr: Create a Maven Project...`
2. Elige:
   - la versión de Spring Boot que proponga;
   - **Java** como lenguaje;
   - *group* `curso.demo`, *artifact* `demo`;
   - empaquetado **Jar**;
   - Java **21**;
   - dependencia **Spring Web**.
3. Elige como destino una carpeta de prácticas, **fuera del repositorio del curso**, y ábrela cuando VS Code lo ofrezca.
4. Observa:
   - la clase `DemoApplication` con `@SpringBootApplication`;
   - `application.properties`;
   - el `pom.xml` con `spring-boot-starter-parent`;
   - el *wrapper* de Maven (`mvnw`, `mvnw.cmd`), que permite compilar sin tener Maven instalado.

**Resultado esperado:** un proyecto Spring Boot completo en segundos, con la misma versión (4.1.2, salvo que Spring haya publicado otra posterior) y la misma estructura que la plantilla del curso. Initializr añade además un *starter* de pruebas por cada tecnología elegida (`spring-boot-starter-webmvc-test`, y `spring-boot-starter-validation-test` si eliges **Validation**). Cierra esa ventana y continúa con la plantilla del curso (`spring-3.5`).

### Paso 2. Explorar la estructura

En tu copia `spring-3.5`:

1. Recorre los paquetes `controller`, `service`, `model` y `dto` y relaciónalos con la tabla del apartado 3.
2. Abre `pom.xml`: el *parent*, los cuatro *starters* sin versión (la fija el *parent*) y `spring-boot-maven-plugin`.
3. Abre `application.properties` y escribe en una línea nueva `server.`. Aparecen sugerencias con documentación, que proporciona Spring Boot Tools. Borra lo escrito.
4. En `TareaController`, observa el constructor que recibe `TareaService`.

### Paso 3. Ejecutar desde VS Code

1. Abre el **Spring Boot Dashboard**. Aparece `gestor-tareas-api`.
2. Pulsa **Start** (▷) sobre la aplicación. Alternativa: **Run** sobre el `main` de `AplicacionTareas`.

**Resultado esperado:** en la terminal o en la Debug Console aparecen:

```text
 :: Spring Boot ::                (v4.1.2)
...
Tomcat started on port 8080 (http) with context path '/'
Started AplicacionTareas in 2.4 seconds (process running for 3.0)
```

### Paso 4. Consultar los *endpoints*

**Bash:**

```bash
curl -s http://localhost:8080/api/tareas
curl -s http://localhost:8080/api/tareas/2
curl -s -i http://localhost:8080/api/tareas/999
```

**PowerShell:**

```powershell
Invoke-RestMethod http://localhost:8080/api/tareas
Invoke-RestMethod http://localhost:8080/api/tareas/2
curl.exe -s -i http://localhost:8080/api/tareas/999
```

También puedes abrir `http://localhost:8080/api/tareas` en el navegador, o en `Simple Browser: Show`.

**Resultado esperado:**

```text
GET /api/tareas → 200
[{"id":1,"titulo":"Crear el proyecto Spring Boot","descripcion":"Plantilla del curso","estado":"COMPLETADA"},
 {"id":2,"titulo":"Probar los endpoints","descripcion":"Con curl o con un archivo .http","estado":"EN_PROGRESO"},
 {"id":3,"titulo":"Depurar una petición","descripcion":"Breakpoint en el controlador","estado":"PENDIENTE"}]

GET /api/tareas/999 → 404
{"detail":"No existe la tarea 999","instance":"/api/tareas/999","status":404,"title":"Not Found"}
```

`Invoke-RestMethod` muestra el JSON como una tabla de PowerShell. Con un 404, lanza un error que incluye el código.

El error 404 llega en formato **Problem Details** (`Content-Type: application/problem+json`). En Spring Boot 4 sus campos aparecen en orden alfabético y no se incluye `"type"` cuando el error es genérico (`about:blank`).

### Paso 5. Crear una tarea

Desde la raíz del proyecto, que es donde está la carpeta `peticiones/`:

**Bash:**

```bash
curl -s -i -X POST -H "Content-Type: application/json" -d @peticiones/crear-tarea.json http://localhost:8080/api/tareas
curl -s -i -X POST -H "Content-Type: application/json" -d @peticiones/crear-tarea-sin-titulo.json http://localhost:8080/api/tareas
```

**PowerShell:**

```powershell
Invoke-RestMethod -Uri http://localhost:8080/api/tareas -Method Post -ContentType "application/json" -Body (Get-Content -Raw peticiones/crear-tarea.json)
curl.exe -s -i -X POST -H "Content-Type: application/json" -d "@peticiones/crear-tarea-sin-titulo.json" http://localhost:8080/api/tareas
```

**Resultado esperado:**

- **Primera petición:** `201 Created`, cabecera `Location: /api/tareas/4` y el cuerpo `{"id":4,"titulo":"Preparar la demo","descripcion":"Creada con curl","estado":"PENDIENTE"}`.
- **Segunda petición, sin título:** `400 Bad Request` con un *Problem Details* (`"detail":"Invalid request content."`, `"title":"Bad Request"`). La validación `@NotBlank` de `CrearTareaRequest` ha rechazado la petición antes de llegar al servicio.

### Paso 6. Modificar el estado

**Bash:**

```bash
curl -s -X PATCH -H "Content-Type: application/json" -d @peticiones/cambiar-estado.json http://localhost:8080/api/tareas/3/estado
curl -s -i -X PATCH -H "Content-Type: application/json" -d '{"estado":"PENDIENTE"}' http://localhost:8080/api/tareas/1/estado
```

**PowerShell:**

```powershell
Invoke-RestMethod -Uri http://localhost:8080/api/tareas/3/estado -Method Patch -ContentType "application/json" -Body (@{ estado = "EN_PROGRESO" } | ConvertTo-Json)
Invoke-RestMethod -Uri http://localhost:8080/api/tareas/1/estado -Method Patch -ContentType "application/json" -Body (@{ estado = "PENDIENTE" } | ConvertTo-Json)
```

**Resultado esperado:**

- **Tarea 3:** `200` y la tarea con `"estado":"EN_PROGRESO"`.
- **Tarea 1:** `409 Conflict`, porque está completada:

  ```text
  {"detail":"La tarea 1 está completada y no puede cambiar de estado","instance":"/api/tareas/1/estado","status":409,"title":"Cambio de estado no permitido"}
  ```

- **Estado inexistente** (por ejemplo `{"estado":"CANCELADA"}`): `400` con `"detail":"Failed to read request"`. Jackson no puede convertir el texto en un valor de `EstadoTarea`.

`@{ estado = "..." } | ConvertTo-Json` construye el JSON en PowerShell sin problemas de comillas.

Con la extensión **REST Client** puedes repetir todas estas peticiones desde `peticiones/tareas.http`, con **Send Request** sobre cada una.

### Paso 7. Colocar un breakpoint en el controlador

1. Detén la aplicación (**Stop** en el Dashboard).
2. En `TareaController.java`, pon un breakpoint en la línea `return servicio.buscar(id).orElseThrow(() -> noEncontrada(id));` del método `consultar`.
3. Arranca en modo depuración: **Debug** en el Dashboard, o **Debug** sobre `main`.

### Paso 8. Depurar una solicitud

1. Ejecuta `curl -s http://localhost:8080/api/tareas/2`, o `Invoke-RestMethod http://localhost:8080/api/tareas/2`.
2. VS Code se detiene en el breakpoint. La terminal de `curl` queda esperando.
3. Observa:
   - en **Variables**, `id` = `2` y `this.servicio`;
   - en **Call Stack**, un hilo `http-nio-8080-exec-N` y, por debajo, las clases de Spring MVC que han llamado a tu método.
4. Pulsa `F11` para entrar en `TareaService.buscar` y `Shift + F11` para volver.
5. Pulsa `F5`: la petición termina y `curl` muestra la tarea.

### Paso 9. Modificar un *endpoint* con recarga automática

1. Con la aplicación en marcha, ya sea con **Run** o con **Debug**, cambia en `TareaController` el mensaje del 404:

   ```java
           return new ResponseStatusException(HttpStatus.NOT_FOUND, "No existe la tarea " + id);
   ```

   por:

   ```java
           return new ResponseStatusException(HttpStatus.NOT_FOUND, "La tarea " + id + " no existe");
   ```

2. Guarda. En el registro aparece un nuevo arranque (`Started AplicacionTareas ...`) en uno o dos segundos: es **DevTools**.
3. Consulta de nuevo `/api/tareas/999`.

**Resultado esperado:** `"detail":"La tarea 999 no existe"`, **sin haber reiniciado la aplicación a mano**. Si no ocurre, comprueba que la compilación automática está activa (`java.autobuild.enabled`, activada por defecto).

Deja el mensaje como prefieras. Si lo cambias, las pruebas no se ven afectadas, porque solo comprueban el código 404.

### Paso 10. Ejecutar las pruebas

1. Detén la aplicación.
2. En la vista **Testing**, ejecuta todas las pruebas, o `mvn test`.
3. Abre `TareaControllerTest` y localiza las pruebas de cada código HTTP (200, 201, 400, 404, 409).

**Resultado esperado:**

```text
[INFO] Tests run: 12, Failures: 0, Errors: 0, Skipped: 0
[INFO] BUILD SUCCESS
```

- **`TareaControllerTest`** usa `@SpringBootTest` y **MockMvc**: arranca la aplicación completa, con controlador, validación, servicio y manejo de errores, pero **sin servidor real**. Las peticiones se simulan en memoria.
- **`TareaServiceTest`** prueba el servicio sin Spring.

### Paso 11 (opcional). Empaquetar y ejecutar el JAR

```bash
mvn package
java -jar target/gestor-tareas-api-1.0.0-SNAPSHOT.jar
```

El comando es igual en PowerShell. Comprueba con el paso 4 que funciona y detén con `Ctrl + C`. El JAR ocupa unos 22 MB porque incluye Spring, Jackson y Tomcat.

## 8. Resultados esperados

| Comprobación | Resultado |
|---|---|
| Arranque | `Tomcat started on port 8080` y `Started AplicacionTareas` |
| GET lista / GET 2 / GET 999 | 200 / 200 / 404 |
| POST válido / sin título | 201 con `Location` / 400 |
| PATCH tarea 3 / tarea 1 / estado inexistente / tarea 999 | 200 / 409 / 400 / 404 |
| Depuración | Parada en `consultar` con `id` = 2 |
| DevTools | Reinicio automático al guardar |
| `mvn test` | 12 pruebas correctas |
| `mvn package` | JAR ejecutable con `java -jar` |

## 9. Problemas frecuentes

| Problema | Solución |
|---|---|
| `Web server failed to start. Port 8080 was already in use.` | Otra aplicación usa el 8080: otra ejecución de esta API, el Tomcat del 3.4… Detenla, o cambia `server.port` en `application.properties`. |
| No aparece la aplicación en el Spring Boot Dashboard | Falta el Spring Boot Extension Pack o el proyecto no ha terminado de importarse. Usa **Run** sobre `main` mientras tanto. |
| `curl` en PowerShell pide parámetros o devuelve un objeto extraño | En Windows PowerShell 5.1, `curl` es `Invoke-WebRequest`: usa `curl.exe` o `Invoke-RestMethod` |
| 415 *Unsupported Media Type* | Falta la cabecera `Content-Type: application/json` |
| 400 al enviar JSON escrito a mano en PowerShell | Problema de comillas: usa `-d "@archivo.json"` o `ConvertTo-Json` |
| DevTools no reinicia | La aplicación se ejecuta con `java -jar` (DevTools desactivado) o la compilación automática está desactivada |
| Un ejemplo de internet usa `spring-boot-starter-web` | Es el nombre de Spring Boot 3, obsoleto en Spring Boot 4: usa `spring-boot-starter-webmvc` (y `spring-boot-starter-webmvc-test` para las pruebas con MockMvc) |
| `package org.springframework.boot.test.autoconfigure.web.servlet does not exist` | Importación de Spring Boot 3. En Spring Boot 4, `@AutoConfigureMockMvc` está en `org.springframework.boot.webmvc.test.autoconfigure` |
| Después de cerrar VS Code la API sigue respondiendo | Quedó un proceso en marcha: **Stop** en el Dashboard o `Ctrl + C` en su terminal antes de cerrar |

## 10. Comparativa con Eclipse e IntelliJ IDEA

| Acción | Eclipse (Spring Tools for Eclipse) | IntelliJ IDEA | VS Code |
|---|---|---|---|
| Crear proyecto | **New > Spring Starter Project** (Initializr) | **New Project > Spring Boot** (Initializr; asistente completo en Ultimate) | `Spring Initializr: Create a Maven Project...` |
| Ejecutar y parar | **Boot Dashboard** | **Services** o configuración Spring Boot | **Spring Boot Dashboard** o **Run** |
| Autocompletado de `application.properties` | Sí (Spring Tools) | Sí (Ultimate; limitado en Community) | Sí (Spring Boot Tools) |
| Recarga en caliente | DevTools al guardar | DevTools; requiere activar la compilación automática | DevTools al guardar (compilación automática activada) |
| Probar *endpoints* | Navegador o herramientas externas | **HTTP Client** integrado (archivos `.http`; Ultimate) | Extensión REST Client (archivos `.http`), `curl` |
| Depurar | **Debug** en Boot Dashboard | **Debug** | **Debug** en el Dashboard o sobre `main` |

Spring Tools para Eclipse y para VS Code los desarrolla el mismo equipo y comparten el motor (Spring Boot Language Server). Por eso las ayudas de Spring son muy parecidas en los dos.

## 11. Comprobaciones finales

Comprueba que eres capaz de:

- [ ] Explicar qué aporta Spring Boot frente al despliegue manual del apartado 3.4 y qué es un *starter*.
- [ ] Crear un proyecto con Spring Initializr desde VS Code y reconocer los *starters* de Spring Boot 4.
- [ ] Identificar en el código `@SpringBootApplication`, `@RestController`, los *mappings*, `@PathVariable`, `@RequestBody` y `@Valid`.
- [ ] Explicar la inyección de dependencias por constructor.
- [ ] Arrancar, parar y depurar la aplicación desde el Spring Boot Dashboard o con **Run**/**Debug**.
- [ ] Probar los *endpoints* con `curl` (Bash), `Invoke-RestMethod` y `curl.exe` (PowerShell) o un archivo `.http`.
- [ ] Interpretar las respuestas 200, 201, 400, 404 y 409.
- [ ] Depurar una petición desde el controlador hasta el servicio.
- [ ] Aprovechar DevTools para ver los cambios sin reiniciar a mano.
- [ ] Ejecutar las pruebas con MockMvc y empaquetar un JAR ejecutable.
