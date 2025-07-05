package com.ghgande.kmod.modbus

/**
 * Class that implements a ModbusIOException. Instances of this
 * exception are thrown when errors in the I/O occur.
 *
 * This is a Kotlin Multiplatform version of the original j2mod ModbusIOException class.
 */
class ModbusIOException : ModbusException {
    private var eof = false

    /**
     * Constructs a new ModbusIOException instance.
     */
    constructor() : super()

    /**
     * Constructs a new ModbusIOException instance with the given message.
     *
     * @param message the message describing this ModbusIOException.
     */
    constructor(message: String) : super(message)

    /**
     * Constructs a new ModbusIOException instance with the given message.
     *
     * @param message the message describing this ModbusIOException.
     * @param values optional values of the exception
     */
    constructor(message: String, vararg values: Any) : super(message, *values)

    /**
     * Constructs a new ModbusIOException instance.
     *
     * @param b true if caused by end of stream, false otherwise.
     */
    constructor(b: Boolean) : super() {
        eof = b
    }

    /**
     * Constructs a new ModbusIOException instance with the given message.
     *
     * @param message the message describing this ModbusIOException.
     * @param b true if caused by end of stream, false otherwise.
     */
    constructor(message: String, b: Boolean) : super(message) {
        eof = b
    }

    /**
     * Constructs a new ModbusIOException instance with the given message and underlying cause.
     *
     * @param message the message describing this ModbusIOException.
     * @param cause the cause (which is saved for later retrieval by the getCause() method).
     * A null value is permitted, and indicates that the cause is nonexistent or unknown.
     */
    constructor(message: String, cause: Throwable) : super(message, cause)

    /**
     * Tests if this ModbusIOException is caused by an end of the stream.
     *
     * @return true if stream ended, false otherwise.
     */
    fun isEOF(): Boolean {
        return eof
    }

    /**
     * Sets the flag that determines whether this ModbusIOException was
     * caused by an end of the stream.
     *
     * @param b true if stream ended, false otherwise.
     */
    fun setEOF(b: Boolean) {
        eof = b
    }
}