package com.ghgande.kmod.modbus.io

import com.fazecast.jSerialComm.SerialPort
import java.io.IOException

/**
 * JVM implementation of SerialConnection using jSerialComm library.
 */
actual class SerialConnection {
    private var serialPort: SerialPort? = null

    /**
     * Opens the serial port with the specified parameters.
     *
     * @param portName The name of the serial port to open
     * @param baudRate The baud rate to use
     * @param dataBits The number of data bits (typically 7 or 8)
     * @param stopBits The number of stop bits (1 or 2)
     * @param parity The parity mode (NONE, ODD, EVEN, MARK, SPACE)
     * @param flowControl The flow control mode (NONE, RTS_CTS, XON_XOFF)
     * @param timeout The timeout for read operations in milliseconds
     * @return true if the port was opened successfully, false otherwise
     */
    actual fun open(
        portName: String,
        baudRate: Int,
        dataBits: Int,
        stopBits: Int,
        parity: Parity,
        flowControl: FlowControl,
        timeout: Int
    ): Boolean {
        try {
            // Find the serial port by name
            val port = SerialPort.getCommPorts().find { it.systemPortName == portName || it.descriptivePortName.contains(portName) }
                ?: return false

            // Configure the port
            port.baudRate = baudRate
            port.numDataBits = dataBits
            port.numStopBits = convertStopBits(stopBits)
            port.parity = convertParity(parity)
            //port.flowControl = convertFlowControl(flowControl)
            port.setComPortTimeouts(SerialPort.TIMEOUT_READ_BLOCKING, timeout, 0)

            // Open the port
            val success = port.openPort()
            if (success) {
                serialPort = port
            }
            return success
        } catch (e: Exception) {
            return false
        }
    }

    /**
     * Closes the serial port.
     */
    actual fun close() {
        serialPort?.closePort()
        serialPort = null
    }

    /**
     * Reads data from the serial port.
     *
     * @param buffer The buffer to read data into
     * @param offset The offset in the buffer to start writing data
     * @param length The maximum number of bytes to read
     * @return The number of bytes read, or -1 if an error occurred
     */
    actual fun read(buffer: ByteArray, offset: Int, length: Int): Int {
        return try {
            serialPort?.readBytes(buffer, length, offset)?.toInt() ?: -1
        } catch (e: IOException) {
            -1
        }
    }

    /**
     * Writes data to the serial port.
     *
     * @param buffer The buffer containing the data to write
     * @param offset The offset in the buffer to start reading data
     * @param length The number of bytes to write
     * @return The number of bytes written, or -1 if an error occurred
     */
    actual fun write(buffer: ByteArray, offset: Int, length: Int): Int {
        return try {
            serialPort?.writeBytes(buffer, length, offset)?.toInt() ?: -1
        } catch (e: IOException) {
            -1
        }
    }

    /**
     * Returns the number of bytes available to read from the serial port.
     *
     * @return The number of bytes available to read
     */
    actual fun bytesAvailable(): Int {
        return serialPort?.bytesAvailable() ?: 0
    }

    /**
     * Converts stop bits value to jSerialComm constant.
     */
    private fun convertStopBits(stopBits: Int): Int {
        return when (stopBits) {
            1 -> SerialPort.ONE_STOP_BIT
            2 -> SerialPort.TWO_STOP_BITS
            else -> SerialPort.ONE_STOP_BIT
        }
    }

    /**
     * Converts Parity enum to jSerialComm constant.
     */
    private fun convertParity(parity: Parity): Int {
        return when (parity) {
            Parity.NONE -> SerialPort.NO_PARITY
            Parity.ODD -> SerialPort.ODD_PARITY
            Parity.EVEN -> SerialPort.EVEN_PARITY
            Parity.MARK -> SerialPort.MARK_PARITY
            Parity.SPACE -> SerialPort.SPACE_PARITY
        }
    }

    /**
     * Converts FlowControl enum to jSerialComm constant.
     */
    private fun convertFlowControl(flowControl: FlowControl): Int {
        return when (flowControl) {
            FlowControl.NONE -> SerialPort.FLOW_CONTROL_DISABLED
            FlowControl.RTS_CTS -> SerialPort.FLOW_CONTROL_RTS_ENABLED or SerialPort.FLOW_CONTROL_CTS_ENABLED
            FlowControl.XON_XOFF -> SerialPort.FLOW_CONTROL_XONXOFF_IN_ENABLED or SerialPort.FLOW_CONTROL_XONXOFF_OUT_ENABLED
        }
    }
}