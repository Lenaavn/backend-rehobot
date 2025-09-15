# Imagen base con JDK 17
FROM openjdk:17-jdk-alpine

# Directorio de trabajo dentro del contenedor
WORKDIR /app

# Copiar archivos necesarios para construir el proyecto
COPY pom.xml ./
COPY mvnw ./
COPY .mvn .mvn
COPY src ./src

# Dar permisos de ejecución al Maven Wrapper
RUN chmod +x mvnw

# Construir el proyecto usando Maven Wrapper
RUN ./mvnw clean package -DskipTests

# Exponer el puerto en el que corre Spring Boot
EXPOSE 8080

# Comando para ejecutar la aplicación
CMD ["java", "-jar", "target/rehobot-0.0.1-SNAPSHOT.jar"]
