# Usa una imagen con Maven para compilar
FROM maven:3.8.5-openjdk-17 AS builder

# Crea un directorio para la app
WORKDIR /app

# Copia todo el proyecto
COPY . .

# Compila el proyecto (esto crea target/*.jar)
RUN mvn clean package -DskipTests

# Imagen final más liviana con solo el JAR
FROM openjdk:17-jdk-slim

WORKDIR /app

# Copia el .jar desde la etapa anterior
COPY --from=builder /app/target/*.jar app_cotizacion.jar

# Si usas SECRET FILES en Render, copia el archivo secreto desde allí
COPY /etc/secrets/firebase-service-account.json firebase-service-account.json

# Expón el puerto (ajústalo si es diferente)
EXPOSE 8080

# Ejecuta la app
ENTRYPOINT ["java", "-jar", "app_cotizacion.jar"]
