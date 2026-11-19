# Imagen de la API de tareas, construida en dos etapas.
#
#   docker build -t curso-m4/tareas-api:1.0 .
#
# Etapa 1 (construccion): Maven y el JDK 21 compilan y empaquetan el JAR.
# Etapa 2 (ejecución): solo el JRE 21 y el JAR. Maven, el JDK y el código
# fuente se quedan en la etapa 1 y no llegan a la imagen final.

# ---------------------------------------------------------------------------
# Etapa 1: construcción
# ---------------------------------------------------------------------------
# Imagen oficial de Maven con Eclipse Temurin 21 (JDK) sobre Ubuntu 24.04.
# Etiqueta con versión concreta: todos obtienen el mismo Maven y el mismo JDK.
FROM maven:3.9.16-eclipse-temurin-21-noble AS construccion

WORKDIR /proyecto

# Primero solo el pom.xml: Maven descarga las dependencias en una capa propia.
# Docker reutiliza esa capa (caché) mientras el pom.xml no cambie, aunque
# cambie el código fuente.
COPY pom.xml .
RUN mvn -B -q dependency:go-offline

# Después el código fuente, que es lo que cambia con más frecuencia.
COPY src ./src

# Las pruebas ya se ejecutan antes, con mvn test; aquí solo se empaqueta.
RUN mvn -B -q package -DskipTests

# ---------------------------------------------------------------------------
# Etapa 2: ejecución
# ---------------------------------------------------------------------------
# Solo el entorno de ejecución de Java 21 (JRE), sin compilador ni Maven.
FROM eclipse-temurin:21-jre-noble

# Usuario sin privilegios para ejecutar la aplicación (no root).
RUN groupadd --system app && useradd --system --gid app --no-create-home app

WORKDIR /app

# Del resultado de la etapa 1 solo se copia el JAR.
COPY --from=construccion /proyecto/target/tareas-docker.jar app.jar

# Configuración por defecto de la imagen. Se puede cambiar al ejecutar el
# contenedor con -e APP_MENSAJE="..." sin reconstruir la imagen.
# Nunca se escriben aquí contraseñas ni tokens: quedarían dentro de la imagen.
ENV APP_MENSAJE="API de tareas en un contenedor Docker"

# Documenta el puerto en el que escucha la aplicación DENTRO del contenedor.
# No lo publica: eso se hace con docker run -p.
EXPOSE 8080

USER app

# Forma JSON (exec): java es el proceso principal del contenedor y recibe
# directamente la señal de parada de docker stop.
ENTRYPOINT ["java", "-jar", "app.jar"]
