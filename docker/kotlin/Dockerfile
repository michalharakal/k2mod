FROM eclipse-temurin:17-jdk

# Install required packages
RUN apt-get update && \
    apt-get install -y curl unzip && \
    apt-get clean

# Set up environment variables
ENV GRADLE_HOME=/opt/gradle
ENV PATH=${GRADLE_HOME}/bin:${PATH}

# Install Gradle
RUN curl -L https://services.gradle.org/distributions/gradle-8.5-bin.zip -o gradle.zip && \
    mkdir -p ${GRADLE_HOME} && \
    unzip gradle.zip -d /opt && \
    mv /opt/gradle-8.5/* ${GRADLE_HOME} && \
    rm gradle.zip

# Create working directory
WORKDIR /app

# Copy the Kotlin project
COPY . .

# Default command to run tests
CMD ["./gradlew", "jvmTest", "--tests", "com.ghgande.kmod.test.docker.KModDockerTest"]