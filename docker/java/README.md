# Running the Java Modbus Slave

This directory contains a simple Java application that starts a Modbus TCP slave for testing purposes. This slave can be used to test the KModDockerTest locally or with Docker.

## Running Locally

### Prerequisites

- JDK 11 or higher
- Maven 3.6 or higher

### Running the Java Modbus Slave

To run the Java Modbus slave locally, follow these steps:

1. Navigate to the `docker/java` directory:
   ```bash
   cd docker/java
   ```

2. Run the ModbusSlaveApp using Maven:
   ```bash
   mvn compile exec:java
   ```

3. The Modbus slave will start on port 1502 with a unit ID of 15 and a register value of 42. You should see output similar to this:
   ```
   [INFO] --- exec-maven-plugin:3.1.0:java (default-cli) @ j2mod-release-test ---
   [INFO] Modbus TCP slave started on port 1502. Press Ctrl+C to stop.
   ```

4. The slave will continue running until you press Ctrl+C to stop it.

## Running the KModDockerTest Against the Local Slave

Once the Java Modbus slave is running, you can run the KModDockerTest against it:

1. Open a new terminal window (keep the slave running in the first terminal).

2. Navigate to the root directory of the project:
   ```bash
   cd /path/to/k2mod
   ```

3. Run the KModDockerTest using Gradle:
   ```bash
   ./gradlew jvmTest --tests "com.ghgande.kmod.test.docker.KModDockerTest"
   ```

4. The test should connect to the locally running Java Modbus slave and pass.

## Running with Docker

### Prerequisites

- Docker installed on your system

### Using the CLI Dockerfile

This repository includes a Dockerfile.cli that can be used to run the ModbusSlaveApp in a Docker container:

1. Navigate to the `docker/java` directory:
   ```bash
   cd docker/java
   ```

2. Build the Docker image:
   ```bash
   docker build -t modbus-slave-app -f Dockerfile.cli .
   ```

3. Run the Docker container:
   ```bash
   docker run -p 1502:1502 modbus-slave-app
   ```

4. The Modbus slave will start on port 1502 inside the container, and the port is mapped to the host machine.

5. To stop the container, press Ctrl+C or run:
   ```bash
   docker stop $(docker ps -q --filter ancestor=modbus-slave-app)
   ```

## Troubleshooting

If you encounter issues, check the following:

1. Make sure the Java Modbus slave is running and listening on port 1502.
2. Check if there are any firewall rules blocking the connection.
3. Verify that the KModDockerTest is configured to connect to "localhost" on port 1502.
4. If you're running the test on a different machine, update the host in the KModDockerTest to point to the IP address of the machine running the Java Modbus slave.
5. When using Docker, ensure that port 1502 is properly mapped from the container to the host.
