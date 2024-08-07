# Stadio di build
FROM ubuntu:latest AS build

# Aggiorna il sistema e installa OpenJDK 21 e Maven
RUN apt-get update && \
    apt-get install -y openjdk-21-jdk maven

# Verifica la versione di Java
RUN java -version
RUN javac -version

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
FROM openjdk:21-jdk-slim

# Verifica la versione di Java nel secondo stadio
RUN java -version

# Espone la porta 8080
EXPOSE 8080

# Copia il file JAR dal primo stadio di costruzione
COPY --from=build /app/target/*.jar app.jar

# Specifica il comando da eseguire quando il container parte
ENTRYPOINT ["java", "-jar", "app.jar"]

