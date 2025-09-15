# Proyecto Spring Boot: Rehobot

## Descripción
Rehobot es una aplicación desarrollada con **Spring Boot** que proporciona una solución robusta para la gestión de servicios, citas, pagos, usuarios y vehículos. Este proyecto está diseñado para ser escalable, modular y fácil de mantener.

## Estructura del Proyecto
El proyecto está organizado en las siguientes carpetas y archivos principales:

### Directorios
- **src/main/java**: Código fuente principal.
  - **com.reho.persistence**: Entidades y repositorios para interactuar con la base de datos.
  - **com.reho.service**: Clases de servicio que contienen la lógica del negocio.
  - **com.reho.web**: Controladores para gestionar las peticiones HTTP.
  - **RehobotApplication.java**: Clase principal que inicializa la aplicación.

- **src/main/resources**:
  - `application.properties`: Archivo de configuración de la aplicación.
  - `datos.sql`: Archivo para inicializar la base de datos con datos predefinidos.
  - `docker-compose.yml`: Configuración para despliegue con Docker.

- **src/test/java**: Contiene pruebas unitarias e integradas.

- **pom.xml**: Administrador de dependencias y configuración de Maven.

### Archivos
- **application.properties**: Configuración de la aplicación, incluyendo la conexión a la base de datos.
- **docker-compose.yml**: Define los contenedores para desplegar la aplicación usando Docker.
- **datos.sql**: Archivo opcional para la inicialización de datos en la base de datos.

## Requisitos Previos
Antes de instalar y ejecutar el proyecto, asegúrate de cumplir con estos requisitos:

- **Java SE 17** instalado en tu sistema.
- **Maven** para la construcción del proyecto.
- **Docker** si deseas desplegar la aplicación en contenedores.

## Instalación
Para instalar y configurar el proyecto en tu máquina local, sigue estos pasos:

1. Clona el repositorio:
   ```bash
   git clone https://github.com/Lenaavn/rehobot.git
   
2. Accede al directorio del proyecto:
   ```bash
   cd rehobot
   
3. Construye el proyecto utilizando Maven:
   ```bash
   ./mvnw clean install

## Configuración
Puedes personalizar la configuración de la aplicación según tus necesidades:

- **Base de datos:** Edita el archivo application.properties para configurar la conexión a la base de datos.

- **Docker:** Ajusta los parámetros del archivo docker-compose.yml.

## Contribuidores
Aquí estamos los creadores de esta aplicación.

- **SrLechuga03** MarioMoreno37
- **Lenaavn**
