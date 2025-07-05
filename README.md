# k2mod
PoC - KMP port of j2mod Modbus comm library

Original j2mod library: https://github.com/steveohara/j2mod

## Overview
k2mod is a Kotlin Multiplatform (KMP) port of the j2mod Modbus communication library. It aims to provide Modbus protocol support for various platforms including JVM, iOS, and browser environments.

## Testing with Docker
k2mod uses Docker to test the Kotlin implementation against the original Java implementation (j2mod) from Maven Central. This approach ensures compatibility and interoperability between the two implementations.

The tests are automatically run on GitHub using GitHub Actions whenever changes are pushed to the main or develop branches, or when a pull request is created for these branches.

### Prerequisites
- Docker and Docker Compose installed on your system
- JDK 17 or higher
- Gradle 8.0 or higher

### Running Tests with Docker
To run the tests using Docker, follow these steps:

1. Clone the repository:
   ```bash
   git clone https://github.com/yourusername/k2mod.git
   cd k2mod
   ```

2. Build and run the tests using Docker Compose:
   ```bash
   cd docker
   docker-compose up --build
   ```

This will:
- Build the Java container with j2mod from Maven Central
- Build the Kotlin container with the k2mod implementation
- Run the tests that verify interoperability between the two implementations
- Output the test results to the `test-results` directory

### Test Scenarios
The Docker-based tests verify the following scenarios:
- Kotlin Master connecting to Java Slave (testing the Kotlin client implementation)
- Java Master connecting to Kotlin Slave (testing the Kotlin server implementation)

### Interpreting Test Results
After running the tests, check the `test-results` directory for the test output. Successful tests indicate that the Kotlin implementation is compatible with the Java implementation.

## Manual Testing
You can also run the tests manually without Docker:

```bash
./gradlew jvmTest --tests "com.ghgande.kmod.test.docker.KModDockerTest"
```

Note that for manual testing, you'll need to have a j2mod slave running locally.

## For More Information
For detailed information about the testing strategy, see [docs/testing.md](docs/testing.md).
