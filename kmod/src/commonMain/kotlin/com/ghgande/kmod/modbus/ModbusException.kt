package com.ghgande.kmod.modbus

import com.ghgande.kmod.modbus.util.format

/**
 * Superclass of all specialized exceptions in this package.
 *
 * This is a Kotlin Multiplatform version of the original j2mod ModbusException class.
 */
open class ModbusException : Exception {
    /**
     * Constructs a new ModbusException instance.
     */
    constructor() : super()

    /**
     * Constructs a new ModbusException instance with the given message.
     *
     * @param message the message describing this ModbusException.
     */
    constructor(message: String) : super(message)

    /**
     * Constructs a new ModbusException instance with the given message.
     *
     * @param message the message describing this ModbusException.
     * @param values optional values of the exception
     */
    constructor(message: String, vararg values: Any) : super(message.format(*values))

    /**
     * Constructs a new ModbusException instance with the given message and underlying cause.
     *
     * @param message the message describing this ModbusException.
     * @param cause the cause (which is saved for later retrieval by the getCause() method).
     * A null value is permitted, and indicates that the cause is nonexistent or unknown.
     */
    constructor(message: String, cause: Throwable) : super(message, cause)
}
