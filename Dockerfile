# Étape 1 : Construction de l'application
FROM maven:latest AS build

# Définir le répertoire de travail
WORKDIR /app

# Copier le fichier POM et les sources
COPY pom.xml .
COPY src ./src

# Construire l'application
RUN mvn clean package -DskipTests

# Étape 2 : Création de l'image finale
FROM openjdk:21-jdk-slim

# Définir le répertoire de travail
WORKDIR /app

# Copier le JAR construit depuis l'étape de build
COPY --from=build /app/target/*.jar app.jar

# Exposer le port de l'application (modifiable selon votre configuration)
EXPOSE 8080

# Commande pour exécuter l'application
ENTRYPOINT ["java", "-jar", "app.jar"]
