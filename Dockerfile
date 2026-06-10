
FROM maven:3.9.6-eclipse-temurin-21-alpine AS build

WORKDIR /app

COPY pom.xml .
RUN mvn dependency:go-offline -B

COPY src ./src
RUN mvn clean package -DskipTests -B


FROM eclipse-temurin:21-jre-alpine

WORKDIR /app


RUN addgroup -S appgroup && adduser -S appuser -G appgroup


COPY --from=build /app/target/*.jar app.jar


RUN chown appuser:appgroup app.jar

USER appuser


EXPOSE 8080

# Run the app with prod profile
ENTRYPOINT ["java", \
  "-Dspring.profiles.active=prod", \
  "-Djava.security.egd=file:/dev/./urandom", \
  "-jar", "app.jar"]