# Usa una base image di OpenJDK 11 (puoi cambiare la versione di Java se necessario)
FROM openjdk:11-jdk-slim

# Imposta la directory di lavoro all'interno del container
WORKDIR /app

# Copia il file pom.xml e scarica le dipendenze Maven (per evitare di scaricarle di nuovo in caso di cambiamenti nel codice)
COPY pom.xml ./
COPY src ./src
RUN mvn -B dependency:resolve dependency:resolve-plugins

# Copia il resto del codice sorgente e compila l'applicazione
COPY . ./
RUN mvn -B clean package -DskipTests

# Esponi la porta su cui gira l'applicazione (definita in application.properties)
EXPOSE 9090

# Esegui l'applicazione
CMD ["java", "-Xms512m", "-Xmx1024m", "-jar", "target/ecommerce-0.0.1-SNAPSHOT.jar"]
