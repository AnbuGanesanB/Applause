# Dockerfile for config-server
FROM eclipse-temurin:17-jdk-alpine

# Health check
RUN apk add --no-cache curl

WORKDIR /app

# Copy Maven wrapper + project files
COPY pom.xml .
COPY .mvn ./.mvn
COPY mvnw .
COPY src ./src

# Build jar using the Maven wrapper
RUN chmod +x mvnw
RUN ./mvnw clean package -DskipTests

# Run
CMD ["sh", "-c", "java -jar target/*.jar"]
