# Build stage: compila el WAR con Maven (perfil prod para incluir driver MySQL)
FROM maven:3.9.6-eclipse-temurin-11 AS builder

WORKDIR /app
COPY pom.xml .
COPY src ./src
RUN mvn clean package -Pprod -DskipTests

# Runtime stage: Tomcat con WAR copiado y context.xml configurado
FROM tomcat:9.0-jdk11

COPY --from=builder /app/target/inventorymanagementapplication.war /usr/local/tomcat/webapps/ROOT.war
COPY src/main/webapp/META-INF/context-prod.xml /usr/local/tomcat/conf/context.xml

EXPOSE 8080
CMD ["catalina.sh", "run"]
