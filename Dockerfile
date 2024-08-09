# Usa un'immagine Maven come base, che include OpenJDK e Maven
FROM maven:3.8.1-openjdk-11-slim AS build

# Imposta la directory di lavoro all'interno del container
WORKDIR /app

# Copia i file pom.xml e src nel container
COPY pom.xml .
COPY src ./src

# Compila e pacchettizza l'applicazione, saltando i test
RUN mvn clean package -DskipTests

# Usa un'immagine JDK più leggera per l'esecuzione finale
FROM openjdk:11-jre-slim

# Imposta la directory di lavoro all'interno del container
WORKDIR /app

# Copia il file JAR generato dalla fase di build precedente
COPY --from=build /app/target/ecommerce-0.0.1-SNAPSHOT.jar .

# Esponi la porta su cui gira l'applicazione
EXPOSE 9090

# Esegui l'applicazione
CMD ["java", "-Xms512m", "-Xmx1024m", "-jar", "ecommerce-0.0.1-SNAPSHOT.jar"]

