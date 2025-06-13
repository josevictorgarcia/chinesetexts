#################################################
# Etapa 1: Construcción del frontend Angular
#################################################
FROM node:lts-alpine3.19 as builderFront

# Establecer directorio de trabajo en el contenedor
WORKDIR /frontend

# Copiar solo archivos de configuración y dependencias
COPY ../chinesetexts/package*.json ./
COPY ../chinesetexts/angular.json ./
COPY ../chinesetexts/tsconfig*.json ./
COPY ../chinesetexts/proxy.conf.json ./

# Instalar las dependencias del proyecto Angular
RUN npm install

# Copiar el código fuente de Angular
COPY ../chinesetexts/src ./src

# Generar la build de producción con base-href personalizado
RUN npm run build -- --configuration production --base-href="/"


#################################################
# Base image for the build container
#################################################
FROM  maven:3.9.6-eclipse-temurin-21 as builder

# Sets the working directory for commands to run
WORKDIR /demo

# Copies the project's dependencies
COPY ../demo/pom.xml ./pom.xml

# Downloads the project's dependencies
#RUN mvn clean verify 

# Copies the project's code
COPY ../demo/src ./src

#Copy frontend files to backend
COPY --from=builderFront /frontend/dist/shushuguan/browser/ ./src/main/resources/static

# Compiles the project
RUN mvn clean package -DskipTests=true

#################################################
# Base image for the application container
#################################################
FROM openjdk:21-jdk AS runtime

# Sets the working directory where the JAR is located
WORKDIR /demo

# Copies the JAR from the build container
COPY --from=builder ../demo/target/demo-0.0.1-SNAPSHOT.jar .

# Indicates the port that the container exposes
EXPOSE 8443

# Command to run on docker run
CMD ["java", "-jar", "demo-0.0.1-SNAPSHOT.jar"]