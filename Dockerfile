FROM --platform=linux/amd64 openjdk:17-jdk-slim AS builder

COPY gradlew .
COPY gradle gradle
COPY build.gradle .
COPY settings.gradle .
COPY src src
RUN chmod +x ./gradlew
RUN ./gradlew bootJAR

FROM --platform=linux/amd64 openjdk:17-jdk-slim
COPY --from=builder build/libs/*.jar app.jar

ARG ENVIRONMENT
ENV SPRING_PROFILES_ACTIVE=dev

EXPOSE 8080
ENTRYPOINT ["java", "-Dspring.profiles.active=dev", "-Dencryptor.key=blaybus", "-jar", "/app.jar"]