# Dockerfile pour le backend Spring Boot PerformaAssist
FROM openjdk:21-jdk-slim as builder

# Métadonnées
LABEL maintainer="PerformaAssist Team"
LABEL description="Backend Spring Boot pour PerformaAssist"
LABEL version="1.0.0"

# Variables d'environnement pour la construction
ENV MAVEN_VERSION=3.9.5
ENV MAVEN_HOME=/opt/maven
ENV PATH=$MAVEN_HOME/bin:$PATH

# Installation de Maven
RUN apt-get update && \
    apt-get install -y wget && \
    wget https://archive.apache.org/dist/maven/maven-3/$MAVEN_VERSION/binaries/apache-maven-$MAVEN_VERSION-bin.tar.gz && \
    tar -xzf apache-maven-$MAVEN_VERSION-bin.tar.gz -C /opt && \
    mv /opt/apache-maven-$MAVEN_VERSION /opt/maven && \
    rm apache-maven-$MAVEN_VERSION-bin.tar.gz && \
    apt-get clean && \
    rm -rf /var/lib/apt/lists/*

# Répertoire de travail
WORKDIR /app

# Copier les fichiers de configuration Maven
COPY pom.xml .
COPY src ./src

# Construire l'application
RUN mvn clean package -DskipTests

# Image de production
FROM openjdk:21-jre-slim

# Métadonnées pour l'image de production
LABEL maintainer="PerformaAssist Team"
LABEL description="Backend Spring Boot pour PerformaAssist - Production"
LABEL version="1.0.0"

# Variables d'environnement
ENV SPRING_PROFILES_ACTIVE=docker
ENV SERVER_PORT=8080
ENV JAVA_OPTS="-Xmx512m -Xms256m"

# Créer un utilisateur non-root pour la sécurité
RUN groupadd -r performaassist && useradd -r -g performaassist performaassist

# Répertoire de travail
WORKDIR /app

# Copier l'application depuis l'étape de construction
COPY --from=builder /app/target/*.jar app.jar

# Changer le propriétaire des fichiers
RUN chown -R performaassist:performaassist /app

# Utiliser l'utilisateur non-root
USER performaassist

# Exposer le port
EXPOSE 8080

# Vérification de santé
HEALTHCHECK --interval=30s --timeout=10s --start-period=60s --retries=3 \
    CMD curl -f http://localhost:8080/actuator/health || exit 1

# Point d'entrée
ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar app.jar"]

# Commande par défaut
CMD []

