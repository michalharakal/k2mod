FROM eclipse-temurin:11-jdk

# Install Maven
RUN apt-get update && \
    apt-get install -y maven && \
    apt-get clean && \
    rm -rf /var/lib/apt/lists/*

# Set working directory
WORKDIR /app

# Copy the project files
COPY . .

# Set environment variables
ENV MAVEN_OPTS="-Xmx1024m"

# Default command to run tests
CMD ["mvn", "test"]