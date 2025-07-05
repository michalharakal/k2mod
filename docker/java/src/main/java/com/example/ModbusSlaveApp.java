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
            logger.info("Starting Modbus TCP slave on port {} with unit ID {}", PORT, UNIT_ID);

            // Start a Modbus TCP slave
            logger.debug("Creating TCP slave with 1 connection...");
            ModbusSlave slave = ModbusSlaveFactory.createTCPSlave(PORT, 1);

            logger.debug("Creating and adding process image for unit ID {}...", UNIT_ID);
            SimpleProcessImage processImage = createProcessImage();
            slave.addProcessImage(UNIT_ID, processImage);

            logger.debug("Opening slave connection...");
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
        logger.debug("Creating SimpleProcessImage for unit ID {}", UNIT_ID);
        SimpleProcessImage processImage = new SimpleProcessImage(UNIT_ID);

        // Add a register with a known value
        logger.debug("Adding register with value {} at address 0", REGISTER_VALUE);
        SimpleRegister register = new SimpleRegister(REGISTER_VALUE);
        processImage.addRegister(register);

        logger.debug("Process image created successfully with {} registers", 1);
        return processImage;
    }
}
