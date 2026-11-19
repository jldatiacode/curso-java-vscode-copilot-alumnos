# Imagen del CONTENEDOR DE DESARROLLO (no es la imagen de ejecución de la API).
#
# Parte de la imagen oficial de Dev Containers para Java 21, que ya incluye:
#   - Microsoft Build of OpenJDK 21 (JDK completo: compilador, depurador…);
#   - el usuario sin privilegios vscode;
#   - Git y utilidades de línea de órdenes.
# Solo le falta Maven, que se instala desde los paquetes firmados de Debian.
FROM mcr.microsoft.com/devcontainers/java:3-21-trixie

RUN apt-get update \
    && export DEBIAN_FRONTEND=noninteractive \
    && apt-get install -y --no-install-recommends maven \
    && rm -rf /var/lib/apt/lists/*
