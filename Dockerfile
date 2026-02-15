# Build stage
FROM eclipse-temurin:17-jdk AS build

WORKDIR /build

# Copy Gradle files
COPY gradlew .
COPY gradle gradle
COPY build.gradle.kts .
COPY settings.gradle.kts .

# Copy source code
COPY src src

# Build the application
RUN ./gradlew bootJar --no-daemon

# Runtime stage
FROM eclipse-temurin:17-jre

WORKDIR /app

# Copy the built JAR from build stage
COPY --from=build /build/build/libs/*.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]
