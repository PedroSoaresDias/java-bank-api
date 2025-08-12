FROM gradle:jdk21-alpine AS builder

WORKDIR /build

COPY build.gradle.kts settings.gradle.kts ./
COPY gradle ./gradle
RUN gradle dependencies --no-daemon

COPY src ./src
RUN gradle bootJar -x test --no-daemon

FROM eclipse-temurin:21-jre-alpine

WORKDIR /app

RUN addgroup -S spring && adduser -S spring -G spring
USER spring:spring

COPY --from=builder /build/build/libs/*.jar java-bank-api.jar

EXPOSE 8080

ENV SPRING_PROFILES_ACTIVE=docker

CMD [ "java", "-jar", "java-bank-api.jar" ]