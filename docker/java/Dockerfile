FROM eclipse-temurin:11-jdk

# Install Maven
RUN apt-get update && \
    apt-get install -y maven && \
    apt-get clean

# Create working directory
WORKDIR /app

# Copy Maven project files
COPY pom.xml .

# Create source directories
RUN mkdir -p src/main/java/com/example src/main/resources src/test/java/com/example

# Create a simple Java class to test j2mod
COPY src/test/java/com/example/J2ModReleaseTest.java src/test/java/com/example/J2ModReleaseTest.java

# Copy the log4j.properties file
COPY src/main/resources/log4j.properties src/main/resources/log4j.properties

# Create logs directory
RUN mkdir -p logs

# Download dependencies
RUN mvn dependency:go-offline

# Default command to run tests
CMD ["mvn", "test"]
