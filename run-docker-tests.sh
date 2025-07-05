#!/bin/bash
set -e

# Create necessary directories
mkdir -p test-scripts
mkdir -p test-results

# Check if Docker is installed
if ! command -v docker &> /dev/null; then
    echo "Docker is not installed. Please install Docker and try again."
    exit 1
fi

# Check if Docker Compose is installed
if ! command -v docker-compose &> /dev/null; then
    echo "Docker Compose is not installed. Please install Docker Compose and try again."
    exit 1
fi

# Build the Docker images
echo "Building Docker images..."
docker-compose build

# Run the tests
echo "Running tests on local project code..."
docker-compose run j2mod-test

echo "Running tests against released j2mod version..."
docker-compose run j2mod-release-test

echo "All tests completed. Test results are available in the test-results directory."