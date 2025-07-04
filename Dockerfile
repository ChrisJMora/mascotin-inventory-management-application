# Etapa 1: Compilar la aplicación
FROM maven:3.9.6-eclipse-temurin-21 AS builder

WORKDIR /app
COPY pom.xml .
RUN mvn dependency:go-offline -B
COPY src ./src
RUN mvn clean package -Pprod -B --no-transfer-progress

# Etapa 2: Imagen final de ejecución
FROM eclipse-temurin:21-jre-alpine AS runtime

# Instalar herramientas necesarias
RUN apk add --no-cache dumb-init curl netcat-openbsd

# Variables Tomcat
ENV TOMCAT_VERSION=9.0.85
ENV CATALINA_HOME=/opt/tomcat
ENV PATH=$CATALINA_HOME/bin:$PATH
WORKDIR $CATALINA_HOME

# Instalar Tomcat
RUN wget -q https://archive.apache.org/dist/tomcat/tomcat-9/v${TOMCAT_VERSION}/bin/apache-tomcat-${TOMCAT_VERSION}.tar.gz \
    && tar -xzf apache-tomcat-${TOMCAT_VERSION}.tar.gz --strip-components=1 \
    && rm apache-tomcat-${TOMCAT_VERSION}.tar.gz \
    && rm -rf webapps/*

# Habilitar resolución de variables de entorno en Tomcat
RUN echo "org.apache.tomcat.util.digester.PROPERTY_SOURCE=org.apache.tomcat.util.digester.EnvironmentPropertySource" >> conf/catalina.properties

# Copiar WAR y configuración
COPY --from=builder /app/target/inventorymanagementapplication.war webapps/ROOT.war
COPY context-prod.xml conf/context.xml

# Copiar wait-for-it.sh
RUN apk add --no-cache dumb-init curl netcat-openbsd bash
COPY wait-for-it.sh /usr/local/bin/wait-for-it
RUN chmod +x /usr/local/bin/wait-for-it

# Configurar logging y memoria
ENV JAVA_OPTS="-XX:+UseContainerSupport -XX:MaxRAMPercentage=75.0 -XX:+UseG1GC -XX:G1HeapRegionSize=16m -XX:+UseStringDeduplication"
ENV CATALINA_OPTS="-Djava.util.logging.config.file=$CATALINA_HOME/conf/logging.properties"

# Healthcheck
HEALTHCHECK --interval=30s --timeout=10s --start-period=60s --retries=3 \
  CMD curl -f http://localhost:8080/health || exit 1

EXPOSE 8080

# Ejecutar con espera activa a la DB
ENTRYPOINT ["dumb-init", "--"]
CMD ["wait-for-it", "db:3306", "--", "catalina.sh", "run"]
