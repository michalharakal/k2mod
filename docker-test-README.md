# j2mod Docker Testing Environment

This directory contains Docker configuration for testing the j2mod library. The setup provides two testing environments:

1. **Local Project Testing**: Tests the current project code
2. **Release Testing**: Tests against the released version of j2mod from Maven Central

## Prerequisites

- Docker
- Docker Compose

## Directory Structure

```
j2mod/
├── Dockerfile              # Docker image for testing local project code
├── docker-compose.yml      # Docker Compose configuration
├── test-scripts/           # Scripts for testing
│   └── run-release-tests.sh # Script to test released j2mod version
└── test-results/           # Directory where test results are stored
```

## Usage

### Using the Convenience Script

The easiest way to run the tests is to use the provided convenience script:

```bash
./run-docker-tests.sh
```

This script will:
1. Check if Docker and Docker Compose are installed
2. Build the Docker images
3. Run tests on local project code
4. Run tests against the released j2mod version

### Manual Usage

If you prefer to run the commands manually:

#### Building the Docker Images

```bash
docker-compose build
```

#### Running Tests on Local Project Code

This will run the tests on your current project code:

```bash
docker-compose run j2mod-test
```

#### Running Tests Against Released j2mod Version

This will run tests against the released version of j2mod from Maven Central:

```bash
docker-compose run j2mod-release-test
```

#### Running All Tests

```bash
docker-compose up
```

## Test Results

Test results are stored in the `test-results` directory, which is mounted as a volume in the Docker containers. You can view the test results in this directory after running the tests.

## Customizing Tests

### Local Project Tests

The local project tests use the standard Maven test configuration. You can customize the tests by modifying the Maven command in the `docker-compose.yml` file:

```yaml
command: mvn test -Dtest=TestClassName
```

### Release Tests

The release tests are defined in the `test-scripts/run-release-tests.sh` script. You can customize these tests by modifying the script.

## Troubleshooting

### Port Conflicts

If you encounter port conflicts (e.g., port 2502 is already in use), you can modify the port in the test scripts.

### Memory Issues

If you encounter memory issues, you can adjust the memory allocation in the `docker-compose.yml` file:

```yaml
environment:
  - MAVEN_OPTS=-Xmx2048m
```

## Notes

- The tests use port 2502 for Modbus TCP communication. Make sure this port is available.
- The Docker containers are configured to use the host network to simplify networking.
