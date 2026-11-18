# Ejercicio 3.4 — Aplicaciones Jakarta EE

Módulo 3. Integración de desarrollos en Java · Tema 3.4. Jakarta EE · Apartado 3.4.1. Aplicaciones Jakarta

Duración estimada: 2 horas.

## 1. Introducción

Muchas aplicaciones Java empresariales son **aplicaciones web** que se empaquetan como **WAR** y se despliegan en un servidor o contenedor (Tomcat, WildFly, Payara, Open Liberty…). Quien viene de Eclipse o IntelliJ IDEA suele hacerlo con un servidor integrado en el IDE.

Este apartado muestra cómo trabajar con ese modelo desde VS Code:

- compilar y empaquetar con Maven;
- desplegar en **Apache Tomcat**;
- probar por HTTP;
- depurar el servidor en marcha.

No es un curso de Jakarta EE: se usa la pieza más básica, el **Servlet**, para centrarse en el flujo de trabajo.

## 2. Objetivos

Al terminar serás capaz de:

- Explicar la evolución de Java EE a Jakarta EE y el cambio de `javax.*` a `jakarta.*`.
- Reconocer la estructura de una aplicación web Maven y de un WAR.
- Elegir una versión de Tomcat compatible con la API Servlet del proyecto.
- Empaquetar, desplegar y redesplegar un WAR, y comprobarlo por HTTP.
- Depurar una aplicación desplegada adjuntando el depurador de VS Code a Tomcat.
- Distinguir claramente **compilar**, **empaquetar** y **desplegar**.

## 3. Conceptos fundamentales

### De Java EE a Jakarta EE

| Versión | Año | Paquetes | API Servlet | Tomcat compatible |
|---|---|---|---|---|
| Java EE 8 (Oracle) | 2017 | `javax.*` | 4.0 | 9.0 |
| Jakarta EE 8 (Eclipse Foundation) | 2019 | `javax.*` | 4.0 | 9.0 |
| Jakarta EE 9 | 2020 | **`jakarta.*`** | 5.0 | 10.0 |
| **Jakarta EE 10** | 2022 | `jakarta.*` | **6.0** | **10.1** |
| Jakarta EE 11 | 2025 | `jakarta.*` | 6.1 | 11.0 |

Al pasar Java EE a la Eclipse Foundation, la marca "Java" no podía seguir en los nombres de paquete. Desde **Jakarta EE 9** todas las API cambiaron de `javax.servlet` a `jakarta.servlet`, de `javax.persistence` a `jakarta.persistence`, etc.

Consecuencia práctica: **una aplicación con `javax.*` no funciona en Tomcat 10 o posterior**, y una con `jakarta.*` no funciona en Tomcat 9. El contenedor no encuentra las clases que espera. Migrar implica cambiar los `import` y las dependencias. Tomcat ofrece además una herramienta de migración automática.

**Este proyecto usa Jakarta Servlet 6.0 (Jakarta EE 10) con Apache Tomcat 10.1.** Es la misma combinación que usa internamente Spring Boot 3, en el apartado 3.5.

### Aplicación web y Servlets

Un **Servlet** es una clase que atiende peticiones HTTP. Se registra en una ruta, por ejemplo con `@WebServlet("/api/tareas")`, y sobrescribe `doGet`, `doPost`, etc. El **contenedor Servlet** (Tomcat):

- recibe las peticiones;
- elige el Servlet según la URL;
- gestiona los hilos;
- llama al método correspondiente.

### Estructura del proyecto y del WAR

```text
proyecto/                                   (Maven, packaging war)
├── pom.xml
├── src/main/java/curso/java/web/
│   ├── TareasServlet.java       GET /api/tareas → JSON
│   ├── EstadoServlet.java       GET /api/estado → JSON
│   ├── InicioAplicacion.java    @WebListener: crea el repositorio al desplegar
│   ├── RepositorioTareas.java   tareas en memoria
│   ├── Tarea.java, EstadoTarea.java
│   └── Json.java                conversión mínima a JSON
├── src/main/webapp/
│   ├── index.html               página de inicio
│   └── WEB-INF/web.xml          descriptor de despliegue (opcional con anotaciones)
└── src/test/java/curso/java/web/JsonTest.java
```

El WAR generado (`target/gestor-tareas-web.war`) es un ZIP con esta estructura:

```text
index.html
WEB-INF/web.xml
WEB-INF/classes/curso/java/web/*.class
WEB-INF/lib/            (JAR de dependencias; aquí no hay ninguno)
META-INF/MANIFEST.MF
```

### Maven para aplicaciones web

- `<packaging>war</packaging>`: `mvn package` genera un WAR mediante `maven-war-plugin`.
- `jakarta.servlet-api` con **`<scope>provided</scope>`**: se usa para compilar, pero **no** se incluye en el WAR, porque la aporta Tomcat. Si se incluyera, podría entrar en conflicto con la del servidor.
- `<finalName>gestor-tareas-web</finalName>`: nombre del WAR. Tomcat usa ese nombre como **ruta de contexto**: `http://localhost:8080/gestor-tareas-web/`.

### Compilar, empaquetar y desplegar

| Paso | Herramienta | Resultado | ¿Prueba que la aplicación funciona? |
|---|---|---|---|
| Compilar | `mvn compile` | `.class` en `target/classes` | No: solo que el código es correcto para el compilador |
| Empaquetar | `mvn package` | `target/gestor-tareas-web.war` | No: solo que el WAR se ha generado |
| Desplegar | Copiar el WAR a Tomcat | Aplicación en marcha | Sí, al probarla por HTTP |

Un WAR puede generarse sin errores y **fallar al desplegarse**, por ejemplo por usar `javax.*` en Tomcat 10. Por eso la práctica termina siempre con una petición HTTP real.

## 4. Preparación del entorno

### 4.1. Requisitos

- JDK 21, Maven 3.6.3 o posterior y el Extension Pack for Java.
- **Apache Tomcat 10.1.x** (validado con 10.1.61). No hacen falta extensiones de VS Code adicionales.

### 4.2. Descargar Apache Tomcat

1. En la web oficial de Apache Tomcat, sección **Download > Tomcat 10**, localiza la última versión **10.1.x**.
2. En **Binary Distributions > Core**, descarga:
   - **Windows:** el archivo **zip** o **64-bit Windows zip**. No hace falta el instalador.
   - **Linux o WSL:** el archivo **tar.gz**.
3. Opcional, pero recomendable: comprueba el archivo con el **sha512** publicado junto a la descarga:
   - **PowerShell:** `Get-FileHash -Algorithm SHA512 apache-tomcat-10.1.*.zip`
   - **Bash:** `sha512sum apache-tomcat-10.1.*.tar.gz`
4. Descomprímelo **fuera del repositorio del curso**, por ejemplo en `C:\herramientas\` (Windows) o en `~/servidores/` (Linux o WSL).

En los comandos siguientes, `CATALINA_HOME` es la carpeta de Tomcat, por ejemplo `.../apache-tomcat-10.1.61`.

### 4.3. `JAVA_HOME`

Tomcat necesita saber qué Java usar:

- **Windows:** **exige** la variable `JAVA_HOME` (o `JRE_HOME`). Si no está definida, `catalina.bat` termina con *Neither the JAVA_HOME nor the JRE_HOME environment variable is defined*.
- **Linux:** si no está definida, usa el `java` del `PATH`.

Para definirla solo en la terminal actual:

- **PowerShell:** `$env:JAVA_HOME = "C:\ruta\al\jdk-21"`
- **Bash:** `export JAVA_HOME=/usr/lib/jvm/java-21-openjdk-amd64` (ruta típica en Ubuntu)

### 4.4. Copiar el proyecto

En la raíz del repositorio del curso, ejecuta `git status` (sin cambios pendientes) y `git pull --ff-only`. Después:

**Windows (PowerShell):**

```powershell
New-Item -ItemType Directory -Force "$HOME/practicas-java" | Out-Null
Copy-Item -Recurse modulo-03-java/tema-3.4-jakarta-ee/apartado-3.4.1-aplicaciones-jakarta/proyecto "$HOME/practicas-java/jakarta-3.4"
code "$HOME/practicas-java/jakarta-3.4"
```

**Linux o WSL (Bash):**

```bash
mkdir -p ~/practicas-java
cp -r modulo-03-java/tema-3.4-jakarta-ee/apartado-3.4.1-aplicaciones-jakarta/proyecto ~/practicas-java/jakarta-3.4
code ~/practicas-java/jakarta-3.4
```

## 5. Herramientas de VS Code utilizadas

| Herramienta | Uso en este apartado |
|---|---|
| Vista **Maven** | `clean`, `compile` y `package` |
| **Java Projects** > **Maven Dependencies** | Comprobar que `jakarta.servlet-api` está disponible para compilar |
| Terminal integrada | Arrancar y parar Tomcat, en una terminal dedicada |
| **Run and Debug** con `launch.json` (`request: attach`) | Depurar la aplicación desplegada en Tomcat |
| **Simple Browser** (Command Palette > `Simple Browser: Show`) o navegador | Probar las URL |

### Integración de servidores en VS Code

VS Code no trae de serie la gestión de servidores de aplicaciones que tienen Eclipse (vista **Servers**) o IntelliJ IDEA Ultimate. Las alternativas son:

| Opción | Requiere | Estado |
|---|---|---|
| **Tomcat por línea de comandos + *attach* del depurador** (este ejercicio) | Nada adicional | Funciona igual en cualquier sistema y explica lo que ocurre |
| Extensión **Community Server Connectors** (Red Hat) | Instalarla. Añade la vista **Servers**: arrancar y parar Tomcat y desplegar desde el editor. | Mantenida, en fase *preview* |
| Extensión **Tomcat for Java** | Instalarla | Sin actualizaciones desde 2021: no se recomienda |
| Spring Boot con servidor embebido (apartado 3.5) | — | La aplicación incluye su propio Tomcat y se ejecuta con **Run** |

Este ejercicio usa la primera opción. Si instalas Community Server Connectors, el flujo es el mismo, pero con botones.

## 6. Explicaciones técnicas

### Despliegue en Tomcat

- **Desplegar** consiste en copiar el WAR a `CATALINA_HOME/webapps/`. Tomcat lo descomprime y arranca la aplicación.
- Con Tomcat en marcha, **redesplegar** es copiar de nuevo el WAR: Tomcat detecta el cambio y reinicia la aplicación (*autodeploy*).
- `catalina.sh run` / `catalina.bat run` ejecutan Tomcat **en primer plano**, en la terminal: los mensajes se ven en directo y `Ctrl + C` lo detiene. Es la forma recomendada en esta práctica, porque no deja procesos en segundo plano.
- Puertos por defecto:
  - `8080` para HTTP;
  - `8005` para apagado;
  - `8000` para depuración (JPDA), configurable.

### Depuración de la aplicación desplegada

Tomcat se arranca en modo **JPDA** (`catalina jpda run`): la JVM abre un puerto de depuración, por defecto `localhost:8000`. VS Code se **adjunta** a ese puerto con una configuración `attach` (`ejemplos/launch-tomcat.json`) y se detiene en los breakpoints de tus Servlets cuando llega una petición.

## 7. Práctica guiada

### Paso 1. Abrir el proyecto Maven

Abre la copia y espera a que termine la importación.

**Resultado esperado:** la vista **Maven** muestra `gestor-tareas-web` y **Problems** está vacío.

### Paso 2. Examinar `pom.xml`

Localiza `<packaging>war</packaging>`, `<finalName>`, la dependencia `jakarta.servlet-api` con `scope` `provided` y el *plugin* `maven-war-plugin`.

### Paso 3. Identificar la API Servlet

1. Abre `TareasServlet.java` y observa los `import jakarta.servlet...` y la anotación `@WebServlet("/api/tareas")`.
2. Haz `Ctrl` + clic sobre `HttpServlet`: se abre la clase de `jakarta.servlet-api-6.0.0.jar`.
3. Abre `InicioAplicacion.java`: `@WebListener` hace que Tomcat lo ejecute al desplegar.

### Paso 4. Compilar

Vista Maven > **Lifecycle** > **compile** (`mvn compile`).

**Resultado esperado:** `BUILD SUCCESS`.

### Paso 5. Empaquetar como WAR

Vista Maven > **Lifecycle** > **package** (`mvn package`).

**Resultado esperado:** `Tests run: 3, Failures: 0`, `Building war: .../target/gestor-tareas-web.war` y `BUILD SUCCESS`.

Abre el WAR para ver su contenido:

- **PowerShell:** `tar -tf target/gestor-tareas-web.war`
- **Bash:** `unzip -l target/gestor-tareas-web.war`

Debe contener `index.html`, `WEB-INF/web.xml` y `WEB-INF/classes/curso/java/web/*.class`, y **ningún** JAR de la API Servlet.

### Paso 6. Configurar el servidor

En una **terminal nueva** de VS Code, que dedicarás a Tomcat, define las variables. Sustituye las rutas por las tuyas.

**PowerShell:**

```powershell
$env:JAVA_HOME = "C:\ruta\al\jdk-21"
$env:CATALINA_HOME = "C:\herramientas\apache-tomcat-10.1.61"
```

**Bash:**

```bash
export CATALINA_HOME=~/servidores/apache-tomcat-10.1.61
```

### Paso 7. Desplegar y arrancar

Desde la raíz de tu copia del proyecto, en esa misma terminal:

**PowerShell:**

```powershell
Copy-Item target/gestor-tareas-web.war "$env:CATALINA_HOME/webapps/"
& "$env:CATALINA_HOME/bin/catalina.bat" run
```

**Bash:**

```bash
cp target/gestor-tareas-web.war "$CATALINA_HOME/webapps/"
"$CATALINA_HOME/bin/catalina.sh" run
```

**Resultado esperado:** entre los mensajes de arranque aparecen:

- `Deploying web application archive [.../webapps/gestor-tareas-web.war]`;
- `Gestor de tareas web iniciado`;
- `Server startup in [...] milliseconds`.

### Paso 8. Acceder desde el navegador

Abre en el navegador, o con `Simple Browser: Show`:

- `http://localhost:8080/gestor-tareas-web/`: página de inicio.
- `http://localhost:8080/gestor-tareas-web/api/tareas`
- `http://localhost:8080/gestor-tareas-web/api/estado`

Desde **otra** terminal también puedes usar:

- **PowerShell:** `Invoke-RestMethod http://localhost:8080/gestor-tareas-web/api/tareas`, o `curl.exe -s http://localhost:8080/gestor-tareas-web/api/estado`
- **Bash:** `curl -s http://localhost:8080/gestor-tareas-web/api/estado`

**Resultado esperado:**

```text
GET /gestor-tareas-web/api/tareas → 200, application/json;charset=UTF-8
[{"id":1,"titulo":"Instalar Apache Tomcat","estado":"COMPLETADA"},{"id":2,"titulo":"Desplegar el WAR","estado":"EN_PROGRESO"},{"id":3,"titulo":"Probar los endpoints","estado":"PENDIENTE"}]

GET /gestor-tareas-web/api/estado → 200
{"estado":"OK","servidor":"Apache Tomcat/10.1.61","servlet":"6.0","tareas":3}
```

`servlet` confirma que el contenedor implementa Servlet 6.0. Una ruta inexistente, como `/gestor-tareas-web/api/no-existe`, devuelve **404**.

Si Tomcat se ejecuta en WSL, también puedes abrir esas URL desde el navegador de Windows: WSL reenvía `localhost` automáticamente.

### Paso 9. Modificar la respuesta del Servlet

En `EstadoServlet.java`, sustituye el comienzo del JSON:

```java
        String json = "{\"estado\":\"OK\",\"servidor\":" + Json.texto(servidor) + ",\"servlet\":"
```

por:

```java
        String json = "{\"estado\":\"OK\",\"mensaje\":\"Servicio de tareas operativo\",\"servidor\":"
                + Json.texto(servidor) + ",\"servlet\":"
```

Guarda y vuelve a empaquetar con `mvn package`, desde la vista Maven o desde **otra** terminal: la de Tomcat sigue ocupada.

### Paso 10. Volver a desplegar

Con Tomcat en marcha, copia el WAR nuevo a `webapps/` con el mismo comando del paso 7, **solo la línea de copia**. En la terminal de Tomcat aparecen `Undeploying context [/gestor-tareas-web]` y `Deploying web application archive ...`. Recarga `/api/estado`.

**Resultado esperado:**

```text
{"estado":"OK","mensaje":"Servicio de tareas operativo","servidor":"Apache Tomcat/10.1.61","servlet":"6.0","tareas":3}
```

Detén Tomcat con `Ctrl + C` en su terminal.

### Paso 11. Depurar la aplicación desplegada

1. Copia `ejemplos/launch-tomcat.json`, de la carpeta de este apartado, como `.vscode/launch.json` en tu copia del proyecto.
2. Arranca Tomcat en modo depuración, en la terminal de Tomcat:

   **PowerShell:**

   ```powershell
   $env:JPDA_ADDRESS = "localhost:8000"
   & "$env:CATALINA_HOME/bin/catalina.bat" jpda run
   ```

   **Bash:**

   ```bash
   JPDA_ADDRESS=localhost:8000 "$CATALINA_HOME/bin/catalina.sh" jpda run
   ```

   La primera línea de la salida debe ser `Listening for transport dt_socket at address: 8000`.
3. Pon un breakpoint en `TareasServlet.java`, en la línea `respuesta.setContentType("application/json");`.
4. En Run and Debug, lanza **Adjuntar a Tomcat (JPDA, puerto 8000)**.
5. Abre `http://localhost:8080/gestor-tareas-web/api/tareas` en el navegador.

**Resultado esperado:**

- VS Code se detiene en el breakpoint.
- En **Call Stack** aparece un hilo de Tomcat, como `http-nio-8080-exec-2`.
- En **Variables**, `repositorio` está disponible, y en la Debug Console `repositorio.contar()` devuelve `3`.
- El navegador espera hasta que pulsas `F5` (*Continue*). Entonces recibe la respuesta.

Al terminar, desconecta el depurador (**Disconnect**) y detén Tomcat con `Ctrl + C`.

**Si el puerto 8000 está ocupado** por otro programa, Tomcat no arranca en modo depuración. Usa otro puerto, por ejemplo `localhost:8001`, en `JPDA_ADDRESS` y en `port` de `launch.json`.

### Paso 12. Limpiar

- Detén Tomcat (`Ctrl + C`).
- Opcionalmente, borra `CATALINA_HOME/webapps/gestor-tareas-web.war` y la carpeta `gestor-tareas-web` que Tomcat creó al desplegar.

## 8. Resultados esperados

| Comprobación | Resultado |
|---|---|
| `mvn package` | 3 pruebas correctas y `target/gestor-tareas-web.war` (unos 10 KB) sin JAR de la API Servlet |
| `GET /api/tareas` | 200, JSON con 3 tareas |
| `GET /api/estado` | 200, `"servidor":"Apache Tomcat/10.1.x"`, `"servlet":"6.0"` |
| Ruta inexistente | 404 |
| Redespliegue | `/api/estado` incluye `"mensaje"` |
| Depuración | Parada en `TareasServlet` dentro de un hilo de Tomcat |

## 9. Problemas frecuentes

| Problema | Solución |
|---|---|
| *Neither the JAVA_HOME nor the JRE_HOME environment variable is defined* | Define `JAVA_HOME` en esa terminal (paso 6) |
| `Address already in use` o Tomcat no arranca | El puerto 8080 está ocupado, por ejemplo por otro Tomcat o por la aplicación del apartado 3.5. Detén el otro proceso o cambia el puerto en `conf/server.xml`. |
| No arranca en modo depuración | El puerto 8000 está ocupado: usa `JPDA_ADDRESS=localhost:8001` y ajusta `launch.json` |
| 404 en `/gestor-tareas-web/api/tareas` | La aplicación no se ha desplegado: revisa los mensajes de Tomcat y que el WAR esté en `webapps/` con ese nombre exacto |
| `ClassNotFoundException: javax.servlet...` al desplegar otra aplicación | Es una aplicación `javax.*`, para Tomcat 9. Usa Tomcat 9 o migra a `jakarta.*`. |
| El WAR contiene `jakarta.servlet-api-*.jar` | Falta `<scope>provided</scope>` en la dependencia |
| El *attach* conecta, pero no se detiene | El WAR desplegado no corresponde al código actual: vuelve a empaquetar y desplegar |
| Tras cerrar VS Code, Tomcat sigue en marcha | Si lo arrancaste con `startup` en lugar de `run`, detenlo con `shutdown.sh` / `shutdown.bat` |

## 10. Comparativa con Eclipse e IntelliJ IDEA

| Aspecto | Eclipse (Enterprise Java and Web) | IntelliJ IDEA | VS Code |
|---|---|---|---|
| Proyecto web | *Dynamic Web Project* o Maven `war` | Maven `war` con la faceta *Web* | Maven `war` |
| Gestión del servidor | Vista **Servers**: añadir Tomcat, arrancar, parar | **Run Configurations > Tomcat Server** (solo en Ultimate) | Línea de comandos, o extensión Community Server Connectors |
| Despliegue | **Add and Remove...** en Servers, publicación automática | Artefacto *exploded* desplegado al arrancar | Copiar el WAR a `webapps/`, o mediante la extensión |
| Redespliegue al guardar | Automático (*publish*) | **Update classes and resources** | Volver a empaquetar y copiar. Con la extensión, `Publish`. |
| Depuración | **Debug on Server** | Botón **Debug** de la configuración Tomcat | Tomcat en modo JPDA y configuración `attach` |
| Versiones Jakarta | Selección del *runtime* | Selección del servidor | Determinada por la dependencia del `pom.xml` y el Tomcat descargado |

En VS Code el proceso es más explícito. A cambio, se entiende exactamente qué ocurre: empaquetar, copiar, arrancar y adjuntar. Además, es idéntico al de un servidor real o un sistema de integración continua.

## 11. Comprobaciones finales

Comprueba que eres capaz de:

- [ ] Explicar la relación entre Java EE, Jakarta EE, `javax.*` y `jakarta.*`, y qué Tomcat corresponde a cada API Servlet.
- [ ] Describir la estructura de una aplicación web Maven y de un WAR.
- [ ] Explicar por qué la API Servlet usa `scope` `provided`.
- [ ] Empaquetar el WAR, desplegarlo en Tomcat y probarlo por HTTP.
- [ ] Modificar un Servlet y redesplegar.
- [ ] Depurar la aplicación desplegada con Tomcat en modo JPDA y una configuración `attach`.
- [ ] Distinguir compilar, empaquetar y desplegar, y no dar por buena una aplicación hasta probarla por HTTP.
- [ ] Explicar las alternativas para integrar servidores en VS Code.
