package com.ghgande.kmod.modbus.io

/**
 * SerialConnection provides a cross-platform abstraction for serial port communication.
 * Platform-specific implementations are provided through expect/actual declarations.
 */
expect class SerialConnection {
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
    fun open(
        portName: String,
        baudRate: Int,
        dataBits: Int,
        stopBits: Int,
        parity: Parity,
        flowControl: FlowControl,
        timeout: Int
    ): Boolean

    /**
     * Closes the serial port.
     */
    fun close()

    /**
     * Reads data from the serial port.
     *
     * @param buffer The buffer to read data into
     * @param offset The offset in the buffer to start writing data
     * @param length The maximum number of bytes to read
     * @return The number of bytes read, or -1 if an error occurred
     */
    fun read(buffer: ByteArray, offset: Int, length: Int): Int

    /**
     * Writes data to the serial port.
     *
     * @param buffer The buffer containing the data to write
     * @param offset The offset in the buffer to start reading data
     * @param length The number of bytes to write
     * @return The number of bytes written, or -1 if an error occurred
     */
    fun write(buffer: ByteArray, offset: Int, length: Int): Int

    /**
     * Returns the number of bytes available to read from the serial port.
     *
     * @return The number of bytes available to read
     */
    fun bytesAvailable(): Int
}

/**
 * Parity modes for serial communication.
 */
enum class Parity {
    NONE,
    ODD,
    EVEN,
    MARK,
    SPACE
}

/**
 * Flow control modes for serial communication.
 */
enum class FlowControl {
    NONE,
    RTS_CTS,
    XON_XOFF
}