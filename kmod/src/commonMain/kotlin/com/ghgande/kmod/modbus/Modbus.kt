package com.ghgande.kmod.modbus

/**
 * Object defining all constants related to the Modbus protocol.
 *
 * This is a Kotlin Multiplatform version of the original j2mod Modbus interface.
 */
object Modbus {
    /**
     * Defines the class 1 function code for "read coils".
     */
    const val READ_COILS = 1

    /**
     * Defines a class 1 function code for "read input discretes".
     */
    const val READ_INPUT_DISCRETES = 2

    /**
     * Defines a class 1 function code for "read holding registers"
     */
    const val READ_HOLDING_REGISTERS = 3

    /**
     * Defines the class 0 function code for "read multiple registers".
     * The proper name is "Read Holding Registers".
     */
    const val READ_MULTIPLE_REGISTERS = 3

    /**
     * Defines a class 1 function code for "read input registers".
     */
    const val READ_INPUT_REGISTERS = 4

    /**
     * Defines a class 1 function code for "write coil".
     */
    const val WRITE_COIL = 5

    /**
     * Defines a class 1 function code for "write single register".
     */
    const val WRITE_SINGLE_REGISTER = 6

    /**
     * "read exception status"
     *
     * Serial devices only.
     */
    const val READ_EXCEPTION_STATUS = 7

    /**
     * "get serial diagnostics"
     *
     * Serial devices only.
     */
    const val READ_SERIAL_DIAGNOSTICS = 8

    /**
     * "get comm event counter"
     *
     * Serial devices only.
     */
    const val READ_COMM_EVENT_COUNTER = 11

    /**
     * "get comm event log"
     *
     * Serial devices only.
     */
    const val READ_COMM_EVENT_LOG = 12

    /**
     * Defines a standard function code for "write multiple coils".
     */
    const val WRITE_MULTIPLE_COILS = 15

    /**
     * Defines the class 0 function code for "write multiple registers".
     */
    const val WRITE_MULTIPLE_REGISTERS = 16

    /**
     * Defines a standard function code for "read slave ID".
     */
    const val REPORT_SLAVE_ID = 17

    /**
     * "read file record"
     */
    const val READ_FILE_RECORD = 20

    /**
     * "write file record"
     */
    const val WRITE_FILE_RECORD = 21

    /**
     * "mask write register"
     *
     * Update a single register using its current value and an AND and OR mask.
     */
    const val MASK_WRITE_REGISTER = 22

    /**
     * "read / write multiple registers"
     *
     * Write some number of registers, then read some number of potentially other registers back.
     */
    const val READ_WRITE_MULTIPLE = 23

    /**
     * "read FIFO queue"
     *
     * Read from a FIFO queue.
     */
    const val READ_FIFO_QUEUE = 24

    /**
     * Defines the function code for reading encapsulated data, such as vendor information.
     */
    const val READ_MEI = 43
    const val READ_MEI_VENDOR_INFO = 14

    /**
     * Defines the byte representation of the coil state "on".
     */
    const val COIL_ON = 255

    /**
     * Defines the byte representation of the coil state "off".
     */
    const val COIL_OFF = 0

    /**
     * Defines the word representation of the coil state "on".
     */
    val COIL_ON_BYTES = byteArrayOf(COIL_ON.toByte(), COIL_OFF.toByte())

    /**
     * Defines the word representation of the coil state "off".
     */
    val COIL_OFF_BYTES = byteArrayOf(COIL_OFF.toByte(), COIL_OFF.toByte())

    /**
     * Defines the maximum number of bits in multiple read/write of input discretes or coils (2000).
     */
    const val MAX_BITS = 2000

    /**
     * Defines the Modbus slave exception offset that is added to the function code, to flag an exception.
     */
    const val EXCEPTION_OFFSET = 128 // the last valid function code is 127

    /**
     * Defines the Modbus slave exception type "illegal function".
     * This exception code is returned if the slave:
     * - does not implement the function code or
     * - is not in a state that allows it to process the function
     */
    const val ILLEGAL_FUNCTION_EXCEPTION = 1

    /**
     * Defines the Modbus slave exception type "illegal data address".
     * This exception code is returned if the reference:
     * - does not exist on the slave or
     * - the combination of reference and length exceeds the bounds of the existing registers.
     */
    const val ILLEGAL_ADDRESS_EXCEPTION = 2

    /**
     * Defines the Modbus slave exception type "illegal data value".
     * This exception code indicates a fault in the structure of the data values
     * of a complex request, such as an incorrect implied length.
     *
     * This code does not indicate a problem with application specific validity of the value.
     */
    const val ILLEGAL_VALUE_EXCEPTION = 3

    /**
     * Defines the Modbus slave exception type "slave device failure".
     * This exception code indicates a fault in the slave device itself.
     */
    const val SLAVE_DEVICE_FAILURE = 4

    /**
     * Defines the Modbus slave exception type "slave busy".
     * This exception indicates the the slave is unable to perform the operation
     * because it is performing an operation which cannot be interrupted.
     */
    const val SLAVE_BUSY_EXCEPTION = 6

    /**
     * Defines the Modbus slave exception type "negative acknowledgment".
     * This exception code indicates the slave cannot perform the requested action.
     */
    const val NEGATIVE_ACKNOWLEDGEMENT = 7

    /**
     * Defines the Modbus slave exception type "Gateway target failed to respond".
     * This exception code indicates that a Modbus gateway failed to receive a response from the specified target.
     */
    const val GATEWAY_TARGET_NO_RESPONSE = 11

    /**
     * Defines the default port number of Modbus (502).
     */
    const val DEFAULT_PORT = 502

    /**
     * Defines the maximum message length in bytes (256).
     */
    const val MAX_MESSAGE_LENGTH = 256

    /**
     * Defines the default transaction identifier (0).
     */
    const val DEFAULT_TRANSACTION_ID = 0

    /**
     * Defines the default protocol identifier (0).
     */
    const val DEFAULT_PROTOCOL_ID = 0

    /**
     * Defines the default unit identifier (0).
     */
    const val DEFAULT_UNIT_ID = 0

    /**
     * Defines the default setting for validity checking in transactions (true).
     */
    const val DEFAULT_VALIDITYCHECK = true

    /**
     * Defines the default setting for I/O operation timeouts in milliseconds (3000).
     */
    const val DEFAULT_TIMEOUT = 3000

    /**
     * Defines the sleep period between transaction retries in milliseconds (500).
     */
    const val RETRY_SLEEP_TIME = 500

    /**
     * Defines the default reconnecting setting for transactions (false).
     */
    const val DEFAULT_RECONNECTING = false

    /**
     * Defines the default amount of retires for opening a connection (5).
     */
    const val DEFAULT_RETRIES = 5

    /**
     * Defines the default number of msec to delay before transmission.
     *
     * Inter-message delays are managed by the SerialTransaction object automatically based on the
     * baud rate. Setting this value to anything other than zero will bypass that process and force
     * a specific inter-message delay (0).
     */
    const val DEFAULT_TRANSMIT_DELAY = 0

    /**
     * Defines the default number of msec to delay before transmission if not overridden by DEFAULT_TRANSMIT_DELAY (2).
     */
    const val MINIMUM_TRANSMIT_DELAY = 2

    /**
     * The number of characters delay that must be maintained between adjacent requests on
     * the same serial port (within the same transaction)
     */
    const val INTER_MESSAGE_GAP = 4.0

    /**
     * The number of characters delay that is the allowed maximum between characters on
     * the same serial port (within the same transaction)
     */
    const val INTER_CHARACTER_GAP = 1.5

    /**
     * Defines the maximum value of the transaction identifier.
     *
     * Note: The standard requires that the server copy whatever value the client provides.
     * However, the transaction ID is being limited to signed 16-bit integers to prevent
     * problems with servers that might incorrectly assume the value is a signed value.
     */
    const val MAX_TRANSACTION_ID = Short.MAX_VALUE.toInt()

    /**
     * Defines the serial encoding "ASCII".
     */
    const val SERIAL_ENCODING_ASCII = "ascii"

    /**
     * Defines the serial encoding "RTU".
     */
    const val SERIAL_ENCODING_RTU = "rtu"

    /**
     * Defines the default serial encoding (ASCII).
     */
    const val DEFAULT_SERIAL_ENCODING = SERIAL_ENCODING_ASCII
}