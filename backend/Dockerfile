# ============================================
# ETAPA 1: Build con Gradle y JDK 21
# ============================================
FROM gradle:8.5-jdk21 AS build

WORKDIR /app

# Copiar archivos de configuración de Gradle
COPY build.gradle settings.gradle ./
COPY gradlew ./
COPY gradle ./gradle

RUN chmod +x gradlew

# Descargar dependencias
RUN ./gradlew dependencies --no-daemon || true

# Copiar el código fuente
COPY src ./src

# Compilar (sin tests)
RUN ./gradlew clean build -x test --no-daemon

# ============================================
# ETAPA 2: Runtime - JRE 21
# ============================================
FROM eclipse-temurin:21-jre-alpine

# Crear usuario no root
RUN addgroup -S spring && adduser -S spring -G spring
USER spring:spring

WORKDIR /app

# Copiar el JAR
COPY --from=build /app/build/libs/appsEmpresariales-0.0.1-SNAPSHOT.jar app.jar

# Exponer puerto
EXPOSE 8080

# Variables de entorno (pueden sobreescribirse en compose)
ENV JAVA_OPTS="-Xms256m -Xmx512m"

# Ejecutar app
ENTRYPOINT ["sh", "-c", "java ${JAVA_OPTS} -jar app.jar"]
