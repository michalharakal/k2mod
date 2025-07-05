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
        // Start a Modbus TCP slave
        slave = ModbusSlaveFactory.createTCPSlave(PORT, 1);
        slave.addProcessImage(UNIT_ID, createProcessImage());
        slave.open();
        
        logger.info("Modbus TCP slave started on port {}", PORT);

        // Create a Modbus TCP master connection
        InetAddress address = InetAddress.getLocalHost();
        connection = new TCPMasterConnection(address);
        connection.setPort(PORT);
        connection.connect();
        
        logger.info("Modbus TCP master connected to {}:{}", address.getHostAddress(), PORT);
    }

    @After
    public void tearDown() {
        // Close the connection and stop the slave
        if (connection != null) {
            connection.close();
            logger.info("Modbus TCP master connection closed");
        }
        
        if (slave != null) {
            slave.close();
            logger.info("Modbus TCP slave stopped");
        }
    }

    @Test
    public void testReadHoldingRegister() throws ModbusException {
        logger.info("Testing read holding register");
        
        // Create the request
        ReadMultipleRegistersRequest request = new ReadMultipleRegistersRequest(REGISTER_ADDRESS, REGISTER_COUNT);
        request.setUnitID(UNIT_ID);
        
        // Execute the transaction
        ModbusTCPTransaction transaction = new ModbusTCPTransaction(connection);
        transaction.setRequest(request);
        transaction.execute();
        
        // Get the response
        ReadMultipleRegistersResponse response = (ReadMultipleRegistersResponse) transaction.getResponse();
        
        // Verify the response
        assertNotNull("Response should not be null", response);
        assertEquals("Unit ID should match", UNIT_ID, response.getUnitID());
        
        Register[] registers = response.getRegisters();
        assertNotNull("Registers should not be null", registers);
        assertEquals("Should have received the requested number of registers", REGISTER_COUNT, registers.length);
        assertEquals("Register value should match", REGISTER_VALUE, registers[0].getValue());
        
        logger.info("Successfully read register value: {}", registers[0].getValue());
    }

    private com.ghgande.j2mod.modbus.procimg.ProcessImage createProcessImage() {
        com.ghgande.j2mod.modbus.procimg.SimpleProcessImage processImage = new com.ghgande.j2mod.modbus.procimg.SimpleProcessImage(UNIT_ID);
        
        // Add a register with a known value
        SimpleRegister register = new SimpleRegister(REGISTER_VALUE);
        processImage.addRegister(register);
        
        return processImage;
    }
}