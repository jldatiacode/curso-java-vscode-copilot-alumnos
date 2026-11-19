# Ejercicio 3.6 — Práctica integradora

Módulo 3. Integración de desarrollos en Java · Tema 3.6. Práctica integradora · Apartado 3.6.1. Aplicación completa

Duración estimada: 1 hora.

## 1. Introducción

Esta práctica reúne lo trabajado en el módulo sobre una API REST de Spring Boot ya funcional:

- proyectos Java 21;
- Maven;
- pruebas con JUnit;
- depuración;
- Spring Boot desde VS Code.

Tienes que **añadir una mejora**: consultar las tareas filtrando por su estado.

No hay instrucciones línea a línea. El enunciado define **qué** debe hacer la API y los criterios con los que se comprobará. Tú decides **cómo** implementarlo, con las herramientas de VS Code que ya conoces.

## 2. Objetivos

Demostrar que sabes, de forma autónoma:

- orientarte en un proyecto Maven de Spring Boot desconocido;
- localizar dónde hay que hacer un cambio;
- implementar una mejora en el controlador y el servicio;
- escribir pruebas que la verifiquen y ejecutarlas con Maven y desde VS Code;
- arrancar la aplicación, probar la API y depurar una petición.

## 3. Conceptos fundamentales

No hay conceptos nuevos. La práctica aplica:

| Concepto | Apartado |
|---|---|
| Navegación, refactorización, generación de código | 3.1 |
| `pom.xml`, `mvn test`, `mvn package`, vista Maven | 3.2 |
| JUnit 5, vista Testing, breakpoints, depuración de peticiones | 3.3 |
| Spring Boot: controlador, servicio, inyección, Problem Details, `curl` y PowerShell | 3.5 |

Concepto que conviene recordar: en una petición como `GET /api/tareas?estado=PENDIENTE`, `estado` es un **parámetro de consulta** (*query parameter*), distinto de una variable de ruta como `{id}`.

## 4. Preparación del entorno

Requisitos: JDK 21, Maven 3.6.3 o posterior, Extension Pack for Java y Spring Boot Extension Pack. El puerto 8080 debe estar libre.

El proyecto usa **Spring Boot 4.1.2**, igual que el apartado 3.5, con los *starters* `spring-boot-starter-webmvc`, `spring-boot-starter-validation`, `spring-boot-devtools` y `spring-boot-starter-webmvc-test`.

En la raíz del repositorio del curso, ejecuta `git status` (sin cambios pendientes) y `git pull --ff-only`. Después copia el proyecto **fuera del repositorio del curso**:

**Windows (PowerShell):**

```powershell
New-Item -ItemType Directory -Force "$HOME/practicas-java" | Out-Null
Copy-Item -Recurse modulo-03-java/tema-3.6-practica-integradora/apartado-3.6.1-aplicacion-completa/proyecto "$HOME/practicas-java/integradora-3.6"
code "$HOME/practicas-java/integradora-3.6"
```

**Linux o WSL (Bash):**

```bash
mkdir -p ~/practicas-java
cp -r modulo-03-java/tema-3.6-practica-integradora/apartado-3.6.1-aplicacion-completa/proyecto ~/practicas-java/integradora-3.6
code ~/practicas-java/integradora-3.6
```

Si quieres llevar un historial de tu trabajo, puedes inicializar Git en tu copia, como en el módulo 2. Así podrás ver tus cambios en el Diff Editor.

Comprueba el punto de partida:

```bash
mvn test
```

**Resultado esperado:** `Tests run: 12, Failures: 0, Errors: 0, Skipped: 0` y `BUILD SUCCESS`.

## 5. Herramientas de VS Code utilizadas

| Necesitas | Herramienta |
|---|---|
| Entender la estructura | **Java Projects**, **Outline**, `Ctrl + T`, `F12` |
| Localizar el *endpoint* de listado | `Ctrl + Shift + F` (`@GetMapping`) o Spring Boot Tools (símbolos `@` en `Ctrl + T`) |
| Escribir y ejecutar pruebas | Vista **Testing**, icono ▷ junto a cada prueba, `mvn test` |
| Arrancar y depurar | **Spring Boot Dashboard**, o **Run**/**Debug** sobre `main` |
| Probar la API | `curl` (Bash), `Invoke-RestMethod` o `curl.exe` (PowerShell), `peticiones/tareas.http` con REST Client |

## 6. Enunciado

### Estado inicial

La API tiene los mismos *endpoints* que en el apartado 3.5 y arranca con cuatro tareas:

| id | Título | Estado |
|---|---|---|
| 1 | Crear el proyecto Spring Boot | COMPLETADA |
| 2 | Probar los endpoints | EN_PROGRESO |
| 3 | Depurar una petición | PENDIENTE |
| 4 | Filtrar tareas por estado | PENDIENTE |

### Mejora: consultar tareas por estado

El *endpoint* de listado debe aceptar un parámetro de consulta **opcional** `estado`:

```http
GET /api/tareas?estado=PENDIENTE
```

| Petición | Respuesta esperada |
|---|---|
| `GET /api/tareas` (sin parámetro) | 200 con **todas** las tareas, como hasta ahora |
| `GET /api/tareas?estado=PENDIENTE` | 200 con **solo** las tareas pendientes: la 3 y la 4 |
| `GET /api/tareas?estado=COMPLETADA` | 200 con solo la tarea 1 |
| `GET /api/tareas?estado=EN_PROGRESO` | 200 con solo la tarea 2 |
| `GET /api/tareas?estado=CANCELADA` (no existe) | **Error controlado**: código 4xx, normalmente 400, con cuerpo JSON en formato Problem Details. Nunca un 500 ni un listado. |

Restricciones:

- No se deben añadir dependencias al `pom.xml`: todo lo necesario ya está.
- Las 12 pruebas existentes deben seguir pasando.
- La lógica de filtrado debe estar en el **servicio**. El controlador solo recibe el parámetro y delega.

### Pasos

1. **Abrir el proyecto Maven** y esperar a que termine la importación.
2. **Identificar la arquitectura:** qué hay en `controller`, `service`, `model` y `dto`.
3. **Localizar el controlador** y el método que atiende `GET /api/tareas`.
4. **Implementar el filtrado por estado**: en el servicio, la consulta; en el controlador, el parámetro opcional.
5. **Añadir pruebas** que comprueben los casos fundamentales:
   - sin parámetro;
   - con un estado válido;
   - con un estado inválido.

   Puedes crear una clase nueva en `src/test/java/curso/springboot/controller/` siguiendo el estilo de `TareaControllerTest`.
6. **Ejecutar `mvn test`.** Deben pasar todas, las 12 originales y las tuyas.
7. **Arrancar Spring Boot** desde VS Code.
8. **Consultar el *endpoint*** con `curl`, PowerShell o `peticiones/tareas.http`. Ese archivo ya incluye las peticiones de la mejora.
9. **Depurar una petición:** breakpoint en el método del controlador y petición con `?estado=PENDIENTE`. Comprueba en **Variables** el valor que recibe el parámetro y sigue con `F11` hasta el servicio.
10. **Comprobar la respuesta y el código HTTP** de cada caso de la tabla.

### Pistas

- Un parámetro de consulta se recibe en el método del controlador con una anotación de Spring MVC para *request params*. Puede declararse como no obligatorio.
- Spring puede convertir el texto del parámetro **directamente** al tipo del *enum*. Si el texto no corresponde a ningún valor, Spring responde por sí mismo con un error 400. Comprueba qué formato tiene ese error con `spring.mvc.problemdetails.enabled=true`.
- En las pruebas con MockMvc, `get("/api/tareas").param("estado", "PENDIENTE")` añade el parámetro. Para comprobar que **todas** las tareas devueltas tienen un estado, mira los *matchers* de Hamcrest para colecciones aplicados a `jsonPath("$[*].estado", ...)`.
- Las pruebas comparten la aplicación en memoria con las de `TareaControllerTest`, que crean tareas. Evita comprobar un número exacto de tareas cuando no sea necesario.

### Peticiones de comprobación

**Bash:**

```bash
curl -s "http://localhost:8080/api/tareas?estado=PENDIENTE"
curl -s -i "http://localhost:8080/api/tareas?estado=CANCELADA"
```

**PowerShell:**

```powershell
Invoke-RestMethod "http://localhost:8080/api/tareas?estado=PENDIENTE"
curl.exe -s -i "http://localhost:8080/api/tareas?estado=CANCELADA"
```

Las comillas alrededor de la URL son necesarias: en Bash, `?` es un carácter especial, y en PowerShell conviene para que la URL llegue entera.

## 7. Práctica guiada: lista de verificación

Usa esta lista como guía. Marca cada punto al completarlo:

- [ ] El proyecto se importa sin errores en **Problems**.
- [ ] He localizado el método de listado en el controlador y el servicio que usa.
- [ ] El servicio tiene una consulta por estado.
- [ ] El controlador acepta `estado` como parámetro opcional y delega en el servicio.
- [ ] He añadido pruebas para: sin parámetro, estado válido y estado inválido.
- [ ] `mvn test` termina con `BUILD SUCCESS` y más de 12 pruebas.
- [ ] La aplicación arranca y responde a las peticiones de la tabla.
- [ ] He depurado una petición y he visto el valor del parámetro en **Variables**.
- [ ] `mvn package` genera el JAR.

## 8. Resultados esperados

- `GET /api/tareas?estado=PENDIENTE` → **200**:

  ```text
  [{"id":3,"titulo":"Depurar una petición","descripcion":"Breakpoint en el controlador","estado":"PENDIENTE"},{"id":4,"titulo":"Filtrar tareas por estado","descripcion":"Mejora de la práctica integradora","estado":"PENDIENTE"}]
  ```

- `GET /api/tareas?estado=CANCELADA` → **400**, cabecera `Content-Type: application/problem+json` y un cuerpo con `"status":400` y un `detail` que explica que el valor no es válido.
- `GET /api/tareas` → **200** con las cuatro tareas, igual que antes de la mejora.
- `mvn test` → todas las pruebas correctas, más de 12.

Los resultados de esta tabla suponen la aplicación recién arrancada. Si has creado o modificado tareas, los listados lo reflejarán.

## 9. Criterios de aceptación

| Criterio | Cómo se comprueba |
|---|---|
| Sin parámetro, se devuelven todas las tareas | `GET /api/tareas` → 200 con las 4 tareas iniciales |
| Con un estado válido, se filtra correctamente | `?estado=PENDIENTE`, `COMPLETADA` y `EN_PROGRESO` → 200 y solo tareas de ese estado |
| Un estado inválido produce un error controlado | `?estado=CANCELADA` → 4xx (normalmente 400) con JSON, nunca 500 ni listado |
| Las pruebas comprueban los casos fundamentales | Pruebas nuevas para sin parámetro, estado válido y estado inválido |
| El proyecto compila | `mvn package` → `BUILD SUCCESS` |
| La API funciona | Peticiones reales con la aplicación arrancada |
| Sin dependencias innecesarias | `pom.xml` con las mismas dependencias que la plantilla |

## 10. Problemas frecuentes

| Problema | Solución |
|---|---|
| Con `?estado=PENDIENTE` se devuelven todas las tareas | El parámetro no llega al método: revisa la anotación y que el nombre coincida con `estado` |
| `?estado=CANCELADA` devuelve 500 | Estás convirtiendo el texto a mano y la excepción no está controlada. Deja que Spring convierta el parámetro, o gestiona el error. |
| Sin parámetro, 400 *Required parameter 'estado' is not present* | El parámetro está declarado como obligatorio |
| Tus pruebas fallan al ejecutarlas todas juntas, pero pasan solas | Dependen del número exacto de tareas, y otras pruebas crean tareas. Comprueba propiedades de los elementos en vez de contar. |
| `Port 8080 was already in use` | Otra instancia sigue en marcha: detenla desde el Dashboard o con `Ctrl + C` |
| `zsh: no matches found` o error con `?` en la terminal | Pon la URL entre comillas |

## 11. Comprobaciones finales

Comprueba que has sido capaz de:

- [ ] Orientarte en el proyecto con **Java Projects**, **Outline** y la navegación de VS Code.
- [ ] Implementar el filtrado en el servicio y exponerlo en el controlador con un parámetro opcional.
- [ ] Escribir pruebas MockMvc para los casos válidos e inválidos y ejecutarlas con Maven y desde la vista **Testing**.
- [ ] Arrancar la aplicación, probar la API desde Bash o PowerShell y depurar una petición.
- [ ] Cumplir todos los criterios de aceptación del apartado 9.

### Comparativa con Eclipse e IntelliJ IDEA

El flujo de esta práctica es el mismo en los tres entornos:

> localizar → implementar → probar → ejecutar → depurar

Cambian las herramientas, que se compararon en los apartados 3.1 a 3.5:

| Paso | Eclipse | IntelliJ IDEA | VS Code |
|---|---|---|---|
| Localizar el *endpoint* | **Open Type**, búsqueda | **Endpoints** (Ultimate), **Go to Class** | `Ctrl + T`, `Ctrl + Shift + F`, Spring Boot Tools |
| Ejecutar las pruebas | **Run As > JUnit Test** | Icono ▷ | Vista **Testing** |
| Arrancar | **Boot Dashboard** | **Run** / **Services** | **Spring Boot Dashboard** |
| Probar la API | Herramientas externas | **HTTP Client** (Ultimate) | `curl`, PowerShell, REST Client |
