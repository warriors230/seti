# ── Etapa 1: Build ──────────────────────────────────────────────────────────
FROM maven:3.9.6-eclipse-temurin-17-alpine AS build

WORKDIR /app

# Copiamos solo el pom primero para aprovechar caché de dependencias
COPY pom.xml .
RUN mvn dependency:go-offline -B

# Copiamos el código fuente y lo compilamos
COPY src ./src
RUN mvn clean package -DskipTests -B

# ── Etapa 2: Runtime - ejecucion ───────────────────────────────────
FROM eclipse-temurin:17-jre-alpine

WORKDIR /app

# Creamos un usuario que no sea el root por seguridad
RUN addgroup -S acme && adduser -S acme -G acme
USER acme

# Copiamos el JAR desde la etapa de build
COPY --from=build /app/target/acme-pedidos-1.0.0.jar app.jar

# Puerto expuesto
EXPOSE 8080

# Variables de entorno configurables
ENV ACME_SOAP_ENDPOINT=https://run.mocky.io/v3/19217075-6d4e-4818-98bc-416d1feb7b84

# Iniciamos la aplicación
ENTRYPOINT ["java", \
            "-XX:+UseContainerSupport", \
            "-XX:MaxRAMPercentage=75.0", \
            "-jar", "app.jar"]
