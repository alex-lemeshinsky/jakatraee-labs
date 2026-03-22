# Stage 1: Build the WAR with Maven
FROM maven:3.9-eclipse-temurin-21-alpine AS build
WORKDIR /app
COPY pom.xml .
COPY src ./src
RUN mvn clean package -DskipTests

# Stage 2: GlassFish 7 on Alpine with JDK 21
FROM eclipse-temurin:21-jdk-alpine

ENV GLASSFISH_HOME=/opt/glassfish7
ENV PATH="${GLASSFISH_HOME}/bin:${PATH}"

RUN apk add --no-cache curl unzip \
    && curl -fsSL -o /tmp/glassfish.zip \
       https://download.eclipse.org/ee4j/glassfish/glassfish-7.0.22.zip \
    && unzip -q /tmp/glassfish.zip -d /opt \
    && curl -L https://jdbc.postgresql.org/download/postgresql-42.7.10.jar -o ${GLASSFISH_HOME}/glassfish/lib/postgresql.jar \
    && rm /tmp/glassfish.zip \
    && apk del curl unzip

COPY --from=build /app/target/Project-1.0-SNAPSHOT.war \
    ${GLASSFISH_HOME}/glassfish/domains/domain1/autodeploy/app.war

COPY setup-glassfish.sh /opt/setup-glassfish.sh
RUN chmod +x /opt/setup-glassfish.sh

EXPOSE 8080 4848

ENTRYPOINT ["/opt/setup-glassfish.sh"]