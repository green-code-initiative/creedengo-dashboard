# Dockerfile to build a SonarQube plugin with Maven and run it in SonarQube

ARG MAVEN_BUILDER=3-openjdk-17-slim
ARG SONARQUBE_VERSION=25.9.0.112764-community

# STEP 1 : build the SonarQube plugin with Maven
#FROM maven:${MAVEN_BUILDER} AS builder

#USER root
#
#RUN apt-get update && \
#    apt-get install -y curl && \
#    curl -fsSL https://deb.nodesource.com/setup_22.x | bash - && \
#    apt-get install -y nodejs
#
#RUN echo "node -version :" && node --version
#RUN echo "npm -version :" && npm --version
#
#RUN corepack enable && corepack prepare pnpm@10.13.1 --activate
#RUN echo "pnpm -version :" && pnpm --version

#COPY . /usr/src/creedengo
#
#WORKDIR /usr/src/creedengo
#COPY src src/
#COPY pom.xml tool_build.sh ./
#
#RUN echo "********** --- ********** Contenu du dossier - ETAPE 1 :" && ls -l
#RUN echo "********** --- ********** Contenu du dossier 'target' - ETAPE 2 :" && ls -l target
#RUN echo "********** --- ********** Contenu du dossier 'node' - ETAPE 3 :" && ls -l node
#RUN echo "********** --- ********** Contenu du dossier 'node_modules' - ETAPE 4 :" && ls -l node_modules
#RUN echo "********** --- ********** Contenu du dossier 'node_modules/@creedengo' - ETAPE 4 :" && ls -l node_modules/@creedengo
#RUN echo "node -version :" && node --version
#RUN echo "pnpm -version :" && pnpm --version
#
#RUN mvn clean package -DskipTests,docker

# STEP 2 : create a SonarQube image with the plugin installed
FROM sonarqube:${SONARQUBE_VERSION}

#COPY --from=builder /usr/src/creedengo/target/creedengo-*.jar /opt/sonarqube/extensions/plugins/

USER sonarqube