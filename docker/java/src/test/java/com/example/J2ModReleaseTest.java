package com.example;

import com.ghgande.j2mod.modbus.ModbusException;
import com.ghgande.j2mod.modbus.io.ModbusTCPTransaction;
import com.ghgande.j2mod.modbus.msg.ReadMultipleRegistersRequest;
import com.ghgande.j2mod.modbus.msg.ReadMultipleRegistersResponse;
import com.ghgande.j2mod.modbus.net.TCPMasterConnection;
import com.ghgande.j2mod.modbus.procimg.Register;
import com.ghgande.j2mod.modbus.procimg.SimpleRegister;
import com.ghgande.j2mod.modbus.slave.ModbusSlave;
import com.ghgande.j2mod.modbus.slave.ModbusSlaveFactory;
import com.ghgande.j2mod.modbus.util.SerialParameters;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetAddress;
import java.net.UnknownHostException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * Test class for j2mod library to verify it works correctly
 * This will be used as a reference implementation for testing the Kotlin port
 */
public class J2ModReleaseTest {

    private static final Logger logger = LoggerFactory.getLogger(J2ModReleaseTest.class);
    private static final int PORT = 1502;
    private static final int UNIT_ID = 15;
    private static final int REGISTER_ADDRESS = 0;
    private static final int REGISTER_COUNT = 1;
    private static final int REGISTER_VALUE = 42;

    private ModbusSlave slave;
    private TCPMasterConnection connection;

    @Before
    public void setUp() throws Exception {
        logger.info("Setting up test with port: {} and unit ID: {}", PORT, UNIT_ID);

        // Start a Modbus TCP slave
        logger.debug("Creating TCP slave with 1 connection...");
        slave = ModbusSlaveFactory.createTCPSlave(PORT, 1);

        logger.debug("Creating and adding process image for unit ID {}...", UNIT_ID);
        slave.addProcessImage(UNIT_ID, createProcessImage());

        logger.debug("Opening slave connection...");
        slave.open();

        logger.info("Modbus TCP slave started on port {}", PORT);

        // Create a Modbus TCP master connection
        logger.debug("Getting localhost address...");
        InetAddress address = InetAddress.getLocalHost();
        logger.debug("Creating TCP master connection to {}...", address.getHostAddress());
        connection = new TCPMasterConnection(address);
        connection.setPort(PORT);

        logger.debug("Connecting to Modbus TCP slave...");
        connection.connect();

        logger.info("Modbus TCP master connected to {}:{}", address.getHostAddress(), PORT);
    }

    @After
    public void tearDown() {
        logger.info("Tearing down test...");

        // Close the connection and stop the slave
        if (connection != null) {
            logger.debug("Closing Modbus TCP master connection...");
            connection.close();
            logger.info("Modbus TCP master connection closed");
        } else {
            logger.debug("No Modbus TCP master connection to close");
        }

        if (slave != null) {
            logger.debug("Stopping Modbus TCP slave...");
            slave.close();
            logger.info("Modbus TCP slave stopped");
        } else {
            logger.debug("No Modbus TCP slave to stop");
        }

        logger.info("Test teardown complete");
    }

    @Test
    public void testReadHoldingRegister() throws ModbusException {
        logger.info("Testing read holding register");

        // Create the request
        logger.debug("Creating request: address={}, count={}, unitId={}", REGISTER_ADDRESS, REGISTER_COUNT, UNIT_ID);
        ReadMultipleRegistersRequest request = new ReadMultipleRegistersRequest(REGISTER_ADDRESS, REGISTER_COUNT);
        request.setUnitID(UNIT_ID);

        // Execute the transaction
        logger.debug("Creating Modbus TCP transaction...");
        ModbusTCPTransaction transaction = new ModbusTCPTransaction(connection);
        transaction.setRequest(request);

        logger.debug("Executing Modbus TCP transaction...");
        transaction.execute();
        logger.debug("Transaction executed successfully");

        // Get the response
        logger.debug("Getting response from transaction...");
        ReadMultipleRegistersResponse response = (ReadMultipleRegistersResponse) transaction.getResponse();
        logger.debug("Received response with unit ID: {}", response.getUnitID());

        // Verify the response
        assertNotNull("Response should not be null", response);
        assertEquals("Unit ID should match", UNIT_ID, response.getUnitID());

        Register[] registers = response.getRegisters();
        assertNotNull("Registers should not be null", registers);
        assertEquals("Should have received the requested number of registers", REGISTER_COUNT, registers.length);

        logger.debug("Received {} registers", registers.length);
        for (int i = 0; i < registers.length; i++) {
            logger.debug("Register[{}] value: {}", i, registers[i].getValue());
        }

        assertEquals("Register value should match", REGISTER_VALUE, registers[0].getValue());

        logger.info("Successfully read register value: {}", registers[0].getValue());
    }

    private com.ghgande.j2mod.modbus.procimg.ProcessImage createProcessImage() {
        logger.debug("Creating SimpleProcessImage for unit ID {}", UNIT_ID);
        com.ghgande.j2mod.modbus.procimg.SimpleProcessImage processImage = new com.ghgande.j2mod.modbus.procimg.SimpleProcessImage(UNIT_ID);

        // Add a register with a known value
        logger.debug("Adding register with value {} at address 0", REGISTER_VALUE);
        SimpleRegister register = new SimpleRegister(REGISTER_VALUE);
        processImage.addRegister(register);

        logger.debug("Process image created successfully with {} registers", 1);
        return processImage;
    }
}
