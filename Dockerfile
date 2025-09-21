# Stage_1: Build the application
FROM eclipse-temurin:21-jdk AS builder
WORKDIR /app
COPY pom.xml mvnw .
COPY .mvn/ .mvn
RUN ./mvnw dependency:go-offline -B
COPY src ./src
RUN ./mvnw clean package -DskipTests
# --------------------
# Stage_2: Run the application
FROM eclipse-temurin:21-jre
WORKDIR /app
COPY --from=builder  /app/target/*.jar  app.jar
EXPOSE 8085
ENTRYPOINT ["java", "-jar", "app.jar", "--server.port=8085"]
