# Dockerfile for discovery-service
FROM eclipse-temurin:17-jdk-alpine
WORKDIR /app

# Copy Maven wrapper + project files
COPY pom.xml .
COPY .mvn ./.mvn
COPY mvnw .
COPY src ./src

# Build jar using Maven wrapper
RUN chmod +x mvnw
RUN ./mvnw clean package -DskipTests

# Run the service
CMD ["sh", "-c", "java -jar target/*.jar"]
