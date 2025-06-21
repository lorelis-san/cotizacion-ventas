FROM openjdk:17-jdk-slim

# ARG debe ir antes de usarse
ARG JAR_FILE=target/cotizacion-ventas-0.0.1.jar

# Crea un directorio de trabajo en el contenedor
WORKDIR /app

# Copia el JAR dentro del directorio de trabajo
COPY ${JAR_FILE} app_cotizacion.jar

# Copia el archivo de credenciales a la misma carpeta
COPY src/main/resources/firebase-service-account.json firebase-service-account.json

# Expone el puerto de la aplicación
EXPOSE 8080

# Comando de inicio
ENTRYPOINT ["java", "-jar", "app_cotizacion.jar"]
