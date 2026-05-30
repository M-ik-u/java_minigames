# --- Этап сборки ---
FROM maven:3.9-eclipse-temurin-25 AS build
WORKDIR /build
COPY pom.xml .
RUN mvn -q -B dependency:go-offline
COPY src ./src
RUN mvn -q -B clean package -DskipTests

# --- Этап рантайма ---
FROM tomcat:11-jdk25-temurin
RUN rm -rf /usr/local/tomcat/webapps/ROOT
COPY --from=build /build/target/ROOT.war /usr/local/tomcat/webapps/ROOT.war
EXPOSE 8080
CMD ["catalina.sh", "run"]
