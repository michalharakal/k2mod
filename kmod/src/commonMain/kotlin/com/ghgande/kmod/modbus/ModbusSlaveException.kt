package com.ghgande.kmod.modbus

/**
 * Class that implements a ModbusSlaveException. Instances of this
 * exception are thrown when the slave returns a Modbus exception.
 *
 * This is a Kotlin Multiplatform version of the original j2mod ModbusSlaveException class.
 */
class ModbusSlaveException : ModbusException {
    /**
     * Instance type attribute
     */
    private val type: Int

    /**
     * Constructs a new ModbusSlaveException instance with the given type.
     *
     * Types are defined according to the protocol specification in Modbus.
     *
     * @param type the type of exception that occurred.
     */
    constructor(type: Int) : super() {
        this.type = type
    }

    /**
     * Returns the type of this ModbusSlaveException.
     * Types are defined according to the protocol specification in Modbus.
     *
     * @return the type of this ModbusSlaveException.
     */
    fun getType(): Int {
        return type
    }

    /**
     * Tests if this ModbusSlaveException is of a given type.
     * Types are defined according to the protocol specification in Modbus.
     *
     * @param type the type to test this ModbusSlaveException type against.
     * @return true if this ModbusSlaveException is of the given type, false otherwise.
     */
    fun isType(type: Int): Boolean {
        return type == this.type
    }


    companion object {
        /**
         * Get the exception type message associated with the given exception number.
         *
         * @param type Numerical value of the Modbus exception.
         * @return a String indicating the type of slave exception.
         */
        fun getMessage(type: Int): String {
            return when (type) {
                1 -> "Illegal Function"
                2 -> "Illegal Data Address"
                3 -> "Illegal Data Value"
                4 -> "Slave Device Failure"
                5 -> "Acknowledge"
                6 -> "Slave Device Busy"
                8 -> "Memory Parity Error"
                10 -> "Gateway Path Unavailable"
                11 -> "Gateway Target Device Failed to Respond"
                else -> "Error Code = $type"
            }
        }
    }
}