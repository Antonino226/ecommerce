# Stadio di build
FROM ubuntu:latest AS build

# Aggiorna il sistema e installa OpenJDK 17 e Maven
RUN apt-get update && \
    apt-get install -y openjdk-17-jdk maven

# Imposta la directory di lavoro all'interno del container
WORKDIR /app

# Copia il file pom.xml e scarica le dipendenze di Maven
COPY pom.xml .
RUN mvn dependency:go-offline

# Copia il resto del progetto
COPY . .

# Compila il progetto Maven
RUN mvn clean package -DskipTests

# Stadio di runtime
FROM openjdk:17-jdk-slim

# Espone la porta 8080
EXPOSE 8080

# Copia il file JAR dal primo stadio di costruzione
COPY --from=build /app/target/*.jar app.jar

# Specifica il comando da eseguire quando il container parte
ENTRYPOINT ["java", "-jar", "app.jar"]
