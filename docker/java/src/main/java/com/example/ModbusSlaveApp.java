package com.example;

import com.ghgande.j2mod.modbus.procimg.SimpleProcessImage;
import com.ghgande.j2mod.modbus.procimg.SimpleRegister;
import com.ghgande.j2mod.modbus.slave.ModbusSlave;
import com.ghgande.j2mod.modbus.slave.ModbusSlaveFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * A simple application that starts a Modbus TCP slave for testing purposes.
 * This slave can be used to test the KModDockerTest locally without Docker.
 */
public class ModbusSlaveApp {

    private static final Logger logger = LoggerFactory.getLogger(ModbusSlaveApp.class);
    private static final int PORT = 1502;
    private static final int UNIT_ID = 15;
    private static final int REGISTER_VALUE = 42;

    public static void main(String[] args) {
        try {
            // Start a Modbus TCP slave
            ModbusSlave slave = ModbusSlaveFactory.createTCPSlave(PORT, 1);
            slave.addProcessImage(UNIT_ID, createProcessImage());
            slave.open();
            
            logger.info("Modbus TCP slave started on port {}. Press Ctrl+C to stop.", PORT);
            
            // Keep the application running until interrupted
            Runtime.getRuntime().addShutdownHook(new Thread(() -> {
                logger.info("Shutting down Modbus TCP slave...");
                slave.close();
                logger.info("Modbus TCP slave stopped");
            }));
            
            // Wait indefinitely
            Thread.currentThread().join();
            
        } catch (Exception e) {
            logger.error("Error starting Modbus TCP slave", e);
        }
    }

    private static SimpleProcessImage createProcessImage() {
        SimpleProcessImage processImage = new SimpleProcessImage(UNIT_ID);
        
        // Add a register with a known value
        SimpleRegister register = new SimpleRegister(REGISTER_VALUE);
        processImage.addRegister(register);
        
        return processImage;
    }
}