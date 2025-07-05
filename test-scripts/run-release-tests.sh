#!/bin/bash
set -e

# Create directory for test project
mkdir -p /tmp/j2mod-release-test
cd /tmp/j2mod-release-test

# Create a simple Maven project
cat > pom.xml << 'EOF'
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0"
         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
    <modelVersion>4.0.0</modelVersion>

    <groupId>com.example</groupId>
    <artifactId>j2mod-release-test</artifactId>
    <version>1.0-SNAPSHOT</version>

    <properties>
        <project.build.sourceEncoding>UTF-8</project.build.sourceEncoding>
        <maven.compiler.source>1.8</maven.compiler.source>
        <maven.compiler.target>1.8</maven.compiler.target>
    </properties>

    <dependencies>
        <!-- Use the latest release version of j2mod from Maven Central -->
        <dependency>
            <groupId>com.ghgande</groupId>
            <artifactId>j2mod</artifactId>
            <version>3.2.1</version>
        </dependency>

        <!-- JUnit for testing -->
        <dependency>
            <groupId>junit</groupId>
            <artifactId>junit</artifactId>
            <version>4.13.2</version>
            <scope>test</scope>
        </dependency>

        <!-- SLF4J and Log4j for logging -->
        <dependency>
            <groupId>org.slf4j</groupId>
            <artifactId>slf4j-api</artifactId>
            <version>2.0.9</version>
        </dependency>
        <dependency>
            <groupId>org.slf4j</groupId>
            <artifactId>slf4j-log4j12</artifactId>
            <version>2.0.9</version>
        </dependency>
        <dependency>
            <groupId>org.apache.logging.log4j</groupId>
            <artifactId>log4j-core</artifactId>
            <version>2.20.0</version>
        </dependency>
    </dependencies>

    <build>
        <plugins>
            <plugin>
                <groupId>org.apache.maven.plugins</groupId>
                <artifactId>maven-surefire-plugin</artifactId>
                <version>2.22.2</version>
            </plugin>
        </plugins>
    </build>
</project>
EOF

# Create directories for source code
mkdir -p src/test/java/com/example
mkdir -p src/test/resources

# Create log4j.properties
cat > src/test/resources/log4j.properties << 'EOF'
log4j.rootLogger=INFO, stdout

log4j.appender.stdout=org.apache.log4j.ConsoleAppender
log4j.appender.stdout.layout=org.apache.log4j.PatternLayout
log4j.appender.stdout.layout.ConversionPattern=%d{yyyy-MM-dd HH:mm:ss} %-5p %c{1}:%L - %m%n
EOF

# Create a simple test class
cat > src/test/java/com/example/J2ModReleaseTest.java << 'EOF'
package com.example;

import com.ghgande.j2mod.modbus.Modbus;
import com.ghgande.j2mod.modbus.facade.ModbusTCPMaster;
import com.ghgande.j2mod.modbus.procimg.*;
import com.ghgande.j2mod.modbus.slave.ModbusSlave;
import com.ghgande.j2mod.modbus.slave.ModbusSlaveFactory;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

/**
 * A simple test to verify the released version of j2mod from Maven Central
 */
public class J2ModReleaseTest {

    private static final Logger logger = LoggerFactory.getLogger(J2ModReleaseTest.class);
    
    private static final int UNIT_ID = 15;
    private static final int PORT = 2502;
    private static final String LOCALHOST = "127.0.0.1";
    
    private ModbusSlave slave;
    private ModbusTCPMaster master;

    @Before
    public void setUp() {
        try {
            // Create and start the slave
            slave = ModbusSlaveFactory.createTCPSlave(PORT, 20);
            slave.addProcessImage(UNIT_ID, getSimpleProcessImage());
            slave.open();
            
            // Create and connect the master
            master = new ModbusTCPMaster(LOCALHOST, PORT);
            master.connect();
            
            logger.info("[DEBUG_LOG] Test setup complete - slave and master initialized");
        }
        catch (Exception e) {
            logger.error("Error setting up test: {}", e.getMessage(), e);
            fail("Cannot set up test: " + e.getMessage());
        }
    }
    
    @After
    public void tearDown() {
        // Disconnect master and close slave
        if (master != null) {
            master.disconnect();
        }
        if (slave != null) {
            slave.close();
        }
        logger.info("[DEBUG_LOG] Test teardown complete");
    }
    
    @Test
    public void testReadHoldingRegister() {
        try {
            // Read the first holding register and verify its value
            int value = master.readMultipleRegisters(UNIT_ID, 0, 1)[0].getValue();
            logger.info("[DEBUG_LOG] Read holding register 0 value: {}", value);
            assertEquals("Incorrect value for holding register 0", 251, value);
            logger.info("[DEBUG_LOG] Test passed successfully");
        }
        catch (Exception e) {
            logger.error("Error in test: {}", e.getMessage(), e);
            fail("Cannot read holding register: " + e.getMessage());
        }
    }
    
    /**
     * Creates a simple process image for testing
     */
    private SimpleProcessImage getSimpleProcessImage() {
        SimpleProcessImage spi = new SimpleProcessImage(UNIT_ID);
        
        // Add a register with value 251
        Register register = new SimpleRegister(251);
        spi.addRegister(register);
        
        return spi;
    }
}
EOF

# Run the tests
echo "Running tests with released version of j2mod from Maven Central..."
mvn test

# Copy test results to the mounted volume
mkdir -p /test-results
cp -r target/surefire-reports/* /test-results/

echo "Tests completed. Results are available in the test-results directory."