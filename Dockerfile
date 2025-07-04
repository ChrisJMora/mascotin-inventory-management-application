# Etapa 1: Compilar la aplicación con Maven
FROM maven:3.9.6-eclipse-temurin-21 AS builder

WORKDIR /app

# Copiar el descriptor del proyecto y preparar dependencias
COPY pom.xml .
RUN mvn dependency:go-offline -B

# Copiar el código fuente y compilar el WAR en modo producción
COPY src ./src
RUN mvn clean package -Pprod -B --no-transfer-progress


# Etapa 2: Imagen final de ejecución basada en Tomcat oficial
FROM tomcat:9.0-jdk21-temurin AS runtime

# Establecer variables de entorno
ENV JAVA_OPTS="-XX:+UseContainerSupport -XX:MaxRAMPercentage=75.0 -XX:+UseG1GC -XX:G1HeapRegionSize=16m -XX:+UseStringDeduplication"

# Limpiar aplicaciones por defecto de Tomcat
RUN rm -rf /usr/local/tomcat/webapps/*

# Copiar el WAR generado desde la etapa de construcción
COPY --from=builder /app/target/inventorymanagementapplication.war /usr/local/tomcat/webapps/ROOT.war

# Copiar archivo de configuración específico para producción
COPY context-prod.xml /usr/local/tomcat/conf/context.xml

# Copiar el script wait-for-it para espera activa
COPY wait-for-it.sh /usr/local/bin/wait-for-it
RUN chmod +x /usr/local/bin/wait-for-it

# Healthcheck para validar la disponibilidad de la aplicación
HEALTHCHECK --interval=30s --timeout=10s --start-period=60s --retries=3 \
  CMD curl -f http://localhost:8080/ || exit 1

# Exponer el puerto HTTP de Tomcat
EXPOSE 8080

# Configurar el ENTRYPOINT y CMD para esperar a MySQL antes de iniciar Tomcat
CMD ["wait-for-it", "db:3306", "--", "catalina.sh", "run"]
