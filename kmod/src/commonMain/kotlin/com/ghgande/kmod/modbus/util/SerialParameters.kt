/**
 * Copyright 2002-2016 jamod & j2mod development teams
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.ghgande.kmod.modbus.util

import com.ghgande.kmod.modbus.Modbus
import com.ghgande.kmod.modbus.net.AbstractSerialConnection

/**
 * Helper class wrapping all serial port communication parameters.
 * Very similar to the javax.comm demos, however, not the same.
 *
 * This is a Kotlin Multiplatform version of the original j2mod SerialParameters class.
 */
class SerialParameters(
    var portName: String = "",
    var baudRate: Int = 9600,
    var flowControlIn: Int = AbstractSerialConnection.FLOW_CONTROL_DISABLED,
    var flowControlOut: Int = AbstractSerialConnection.FLOW_CONTROL_DISABLED,
    var databits: Int = 8,
    var stopbits: Int = AbstractSerialConnection.ONE_STOP_BIT,
    var parity: Int = AbstractSerialConnection.NO_PARITY,
    private var _encoding: String = Modbus.SERIAL_ENCODING_RTU,
    var echo: Boolean = false,
    var openDelay: Int = AbstractSerialConnection.OPEN_DELAY,
    var rs485Mode: Boolean = DEFAULT_RS485_MODE,
    var rs485TxEnableActiveHigh: Boolean = DEFAULT_RS485_TX_ENABLE_ACTIVE_HIGH,
    var rs485EnableTermination: Boolean = DEFAULT_RS485_ENABLE_TERMINATION,
    var rs485RxDuringTx: Boolean = DEFAULT_RS485_TX_DURING_RX,
    private var _rs485DelayBeforeTxMicroseconds: Int = DEFAULT_RS485_DELAY_BEFORE_TX_MICROSECONDS,
    private var _rs485DelayAfterTxMicroseconds: Int = DEFAULT_RS485_DELAY_AFTER_TX_MICROSECONDS
) {
    companion object {
        private const val DEFAULT_RS485_MODE = false
        private const val DEFAULT_RS485_TX_ENABLE_ACTIVE_HIGH = true
        private const val DEFAULT_RS485_ENABLE_TERMINATION = false
        private const val DEFAULT_RS485_TX_DURING_RX = false
        private const val DEFAULT_RS485_DELAY_BEFORE_TX_MICROSECONDS = 1000
        private const val DEFAULT_RS485_DELAY_AFTER_TX_MICROSECONDS = 1000
    }

    /**
     * Gets the encoding to be used.
     *
     * @return the encoding as string.
     *
     * @see Modbus.SERIAL_ENCODING_ASCII
     * @see Modbus.SERIAL_ENCODING_RTU
     */
    fun getEncoding(): String {
        return _encoding
    }

    /**
     * Gets the RS485 delay before TX in microseconds.
     *
     * @return The delay in microseconds
     */
    fun getRs485DelayBeforeTxMicroseconds(): Int {
        return _rs485DelayBeforeTxMicroseconds
    }

    /**
     * Gets the RS485 delay after TX in microseconds.
     *
     * @return The delay in microseconds
     */
    fun getRs485DelayAfterTxMicroseconds(): Int {
        return _rs485DelayAfterTxMicroseconds
    }

    /**
     * Constructs a new SerialParameters instance with parameters obtained from a Properties instance.
     *
     * @param props a Properties instance.
     * @param prefix a prefix for the properties keys if embedded into other properties.
     */
    constructor(props: Map<String, String>, prefix: String = "") : this() {
        val actualPrefix = prefix ?: ""
        
        portName = props["${actualPrefix}portName"] ?: ""
        baudRate = props["${actualPrefix}baudRate"]?.toIntOrNull() ?: 9600
        flowControlIn = props["${actualPrefix}flowControlIn"]?.toIntOrNull() ?: AbstractSerialConnection.FLOW_CONTROL_DISABLED
        flowControlOut = props["${actualPrefix}flowControlOut"]?.toIntOrNull() ?: AbstractSerialConnection.FLOW_CONTROL_DISABLED
        parity = props["${actualPrefix}parity"]?.toIntOrNull() ?: AbstractSerialConnection.NO_PARITY
        databits = props["${actualPrefix}databits"]?.toIntOrNull() ?: 8
        stopbits = props["${actualPrefix}stopbits"]?.toIntOrNull() ?: AbstractSerialConnection.ONE_STOP_BIT
        setEncoding(props["${actualPrefix}encoding"] ?: Modbus.DEFAULT_SERIAL_ENCODING)
        echo = props["${actualPrefix}echo"] == "true"
        openDelay = props["${actualPrefix}openDelay"]?.toIntOrNull() ?: AbstractSerialConnection.OPEN_DELAY
        
        rs485Mode = props["${actualPrefix}rs485Mode"] == "true"
        rs485TxEnableActiveHigh = props["${actualPrefix}rs485TxEnableActiveHigh"] == "true"
        setRs485DelayBeforeTxMicroseconds(props["${actualPrefix}rs485DelayBeforeTxMicroseconds"]?.toIntOrNull() ?: DEFAULT_RS485_DELAY_BEFORE_TX_MICROSECONDS)
        setRs485DelayAfterTxMicroseconds(props["${actualPrefix}rs485DelayAfterTxMicroseconds"]?.toIntOrNull() ?: DEFAULT_RS485_DELAY_AFTER_TX_MICROSECONDS)
    }

    /**
     * Sets the baud rate from the given String.
     *
     * @param rate the new baud rate.
     */
    fun setBaudRate(rate: String) {
        baudRate = rate.toIntOrNull() ?: 9600
    }

    /**
     * Returns the baud rate as a String.
     *
     * @return the baud rate as String.
     */
    fun getBaudRateString(): String {
        return baudRate.toString()
    }

    /**
     * Sets the type of flow control for the input as given by the passed in String.
     *
     * @param flowControl the flow control for reading type.
     * @throws IllegalArgumentException on invalid flowControl
     */
    fun setFlowControlIn(flowControl: String) {
        flowControlIn = stringToFlow(flowControl)
    }

    /**
     * Returns the input flow control type as String.
     *
     * @return the input flow control type as String.
     */
    fun getFlowControlInString(): String {
        return flowToString(flowControlIn)
    }

    /**
     * Sets the output flow control type as given by the passed in String.
     *
     * @param flowControlOut the new output flow control type as String.
     */
    fun setFlowControlOut(flowControlOut: String) {
        this.flowControlOut = stringToFlow(flowControlOut)
    }

    /**
     * Returns the output flow control type as String.
     *
     * @return the output flow control type as String.
     */
    fun getFlowControlOutString(): String {
        return flowToString(flowControlOut)
    }

    /**
     * Returns the list of valid flow controls IN or OUT
     *
     * @return List<Int>
     */
    private fun validFlowControls(): List<Int> {
        return listOf(
            AbstractSerialConnection.FLOW_CONTROL_DISABLED,
            AbstractSerialConnection.FLOW_CONTROL_XONXOFF_OUT_ENABLED,
            AbstractSerialConnection.FLOW_CONTROL_XONXOFF_IN_ENABLED,
            AbstractSerialConnection.FLOW_CONTROL_CTS_ENABLED or AbstractSerialConnection.FLOW_CONTROL_RTS_ENABLED,
            AbstractSerialConnection.FLOW_CONTROL_DSR_ENABLED or AbstractSerialConnection.FLOW_CONTROL_DTR_ENABLED
        )
    }

    /**
     * Returns whether an int is a valid flow control
     *
     * @param control the int to test
     * @return control validity
     */
    private fun isValidFlowControlInt(control: Int): Boolean {
        return validFlowControls().contains(control)
    }

    /**
     * Sets the number of data bits from the given String.
     *
     * @param databits the new number of data bits as String.
     */
    fun setDatabits(databits: String) {
        this.databits = if (databits.isNotBlank() && databits.matches(Regex("[0-9]+"))) {
            databits.toInt()
        } else {
            8
        }
    }

    /**
     * Returns the number of data bits as String.
     *
     * @return the number of data bits as String.
     */
    fun getDatabitsString(): String {
        return databits.toString()
    }

    /**
     * Sets the number of stop bits from the given String.
     *
     * @param stopbits the number of stop bits as String.
     */
    fun setStopbits(stopbits: String) {
        this.stopbits = when (stopbits) {
            "1" -> AbstractSerialConnection.ONE_STOP_BIT
            "1.5" -> AbstractSerialConnection.ONE_POINT_FIVE_STOP_BITS
            "2" -> AbstractSerialConnection.TWO_STOP_BITS
            else -> throw IllegalArgumentException("Invalid stopBit passed in setStopbits. Valid values are: [1, 1.5, 2]")
        }
    }

    /**
     * Returns the number of stop bits as String.
     *
     * @return the number of stop bits as String.
     */
    fun getStopbitsString(): String {
        return when (stopbits) {
            AbstractSerialConnection.ONE_STOP_BIT -> "1"
            AbstractSerialConnection.ONE_POINT_FIVE_STOP_BITS -> "1.5"
            AbstractSerialConnection.TWO_STOP_BITS -> "2"
            else -> "1"
        }
    }

    /**
     * Sets the parity schema from the given String.
     *
     * @param parity the new parity schema as String.
     * @throws IllegalArgumentException on invalid parity
     */
    fun setParity(parity: String) {
        this.parity = when {
            parity.equals("none", ignoreCase = true) -> AbstractSerialConnection.NO_PARITY
            parity.equals("even", ignoreCase = true) -> AbstractSerialConnection.EVEN_PARITY
            parity.equals("odd", ignoreCase = true) -> AbstractSerialConnection.ODD_PARITY
            parity.equals("mark", ignoreCase = true) -> AbstractSerialConnection.MARK_PARITY
            parity.equals("space", ignoreCase = true) -> AbstractSerialConnection.SPACE_PARITY
            else -> throw IllegalArgumentException("Invalid value passed in setParity: $parity. Valid values are [none, even, odd, mark, space]")
        }
    }

    /**
     * Returns the parity schema as String.
     *
     * @return the parity schema as String.
     */
    fun getParityString(): String {
        return when (parity) {
            AbstractSerialConnection.NO_PARITY -> "none"
            AbstractSerialConnection.EVEN_PARITY -> "even"
            AbstractSerialConnection.ODD_PARITY -> "odd"
            AbstractSerialConnection.MARK_PARITY -> "mark"
            AbstractSerialConnection.SPACE_PARITY -> "space"
            else -> "none"
        }
    }

    /**
     * Sets the encoding to be used.
     *
     * @param enc the encoding as string.
     * @throws IllegalArgumentException on invalid encoding
     * @see Modbus.SERIAL_ENCODING_ASCII
     * @see Modbus.SERIAL_ENCODING_RTU
     */
    fun setEncoding(enc: String) {
        _encoding = when {
            enc.equals(Modbus.SERIAL_ENCODING_ASCII, ignoreCase = true) -> Modbus.SERIAL_ENCODING_ASCII
            enc.equals(Modbus.SERIAL_ENCODING_RTU, ignoreCase = true) -> Modbus.SERIAL_ENCODING_RTU
            else -> throw IllegalArgumentException("Invalid value passed in setEncoding: $enc. Valid values are [${Modbus.SERIAL_ENCODING_ASCII}, ${Modbus.SERIAL_ENCODING_RTU}]")
        }
    }

    /**
     * Sets the sleep time that occurs just prior to opening a coms port.
     * Some OS don't like to have their comms ports opened/closed in very quick succession
     * particularly, virtual ports. This delay is a rather crude way of stopping the problem that
     * a comms port doesn't re-appear immediately after a close.
     *
     * @param openDelay Sleep time in milliseconds
     */
    fun setOpenDelay(openDelay: String) {
        this.openDelay = openDelay.toIntOrNull() ?: AbstractSerialConnection.OPEN_DELAY
    }

    /**
     * Sets the delay between activating the RS-485 transmitter and actually sending data.
     * There are devices in the field requiring such a delay for start bit detection.
     *
     * This is a convenience wrapper around setRs485DelayBeforeTxMicroseconds(int) which parses the delay
     * from the supplied string. See the documentation of this method for more details.
     *
     * @param microseconds The string to parse the delay value from
     */
    fun setRs485DelayBeforeTxMicroseconds(microseconds: String) {
        setRs485DelayBeforeTxMicroseconds(microseconds.toIntOrNull() ?: DEFAULT_RS485_DELAY_BEFORE_TX_MICROSECONDS)
    }

    /**
     * Sets the delay between activating the RS-485 transmitter and actually sending data.
     * There are devices in the field requiring such a delay for start bit detection.
     *
     * Please note that the actual interface might not support a resolution
     * down to microseconds and might require appropriately large values for
     * actually generating a delay.
     *
     * RS-485 half-duplex mode is only available on Linux and only if the
     * device driver supports it. Its configuration parameters have no effect
     * on other platforms.
     *
     * @param microseconds The delay in microseconds
     * @throws IllegalArgumentException on strictly negative input
     */
    fun setRs485DelayBeforeTxMicroseconds(microseconds: Int) {
        if (microseconds < 0) {
            throw IllegalArgumentException("Expecting non-negative delay.")
        }
        _rs485DelayBeforeTxMicroseconds = microseconds
    }

    /**
     * Sets the delay between end of transmitting data and deactivating the RS-458 transmitter.
     *
     * This is a convenience wrapper around setRs485DelayAfterTxMicroseconds(int) which parses the delay
     * from the supplied string. See the documentation of this method for more details.
     *
     * @param microseconds The string to parse the delay value from
     */
    fun setRs485DelayAfterTxMicroseconds(microseconds: String) {
        setRs485DelayAfterTxMicroseconds(microseconds.toIntOrNull() ?: DEFAULT_RS485_DELAY_AFTER_TX_MICROSECONDS)
    }

    /**
     * Sets the delay between the end of transmitting data and deactivating the RS-485 transmitter.
     *
     * Please note that the actual interface might not support a resolution
     * down to microseconds and might require appropriately large values for
     * actually generating a delay.
     *
     * RS-485 half-duplex mode is only available on Linux and only if the
     * device driver supports it. Its configuration parameters have no effect
     * on other platforms.
     *
     * @param microseconds The delay in microseconds
     */
    fun setRs485DelayAfterTxMicroseconds(microseconds: Int) {
        if (microseconds < 0) {
            throw IllegalArgumentException("Expecting non-negative delay.")
        }
        _rs485DelayAfterTxMicroseconds = microseconds
    }

    /**
     * Converts a String describing a flow control type to the int which is defined in SerialPort.
     *
     * @param flowControl the String describing the flow control type.
     * @return the int describing the flow control type.
     * @throws IllegalArgumentException on invalid flowControl
     */
    private fun stringToFlow(flowControl: String): Int {
        return when {
            flowControl.equals("none", ignoreCase = true) -> AbstractSerialConnection.FLOW_CONTROL_DISABLED
            flowControl.equals("xon/xoff out", ignoreCase = true) -> AbstractSerialConnection.FLOW_CONTROL_XONXOFF_OUT_ENABLED
            flowControl.equals("xon/xoff in", ignoreCase = true) -> AbstractSerialConnection.FLOW_CONTROL_XONXOFF_IN_ENABLED
            flowControl.equals("rts/cts", ignoreCase = true) -> AbstractSerialConnection.FLOW_CONTROL_CTS_ENABLED or AbstractSerialConnection.FLOW_CONTROL_RTS_ENABLED
            flowControl.equals("dsr/dtr", ignoreCase = true) -> AbstractSerialConnection.FLOW_CONTROL_DSR_ENABLED or AbstractSerialConnection.FLOW_CONTROL_DTR_ENABLED
            else -> throw IllegalArgumentException("Invalid value passed as flowControl: $flowControl. Valid values are ['none', 'xon/xoff out', 'xon/xoff in', 'rts/cts', 'dsr/dtr']")
        }
    }

    /**
     * Converts an int describing a flow control type to a String describing a flow control type.
     *
     * @param flowcontrol the int describing the flow control type.
     * @return the String describing the flow control type.
     */
    private fun flowToString(flowcontrol: Int): String {
        return when (flowcontrol) {
            AbstractSerialConnection.FLOW_CONTROL_DISABLED -> "none"
            AbstractSerialConnection.FLOW_CONTROL_XONXOFF_OUT_ENABLED -> "xon/xoff out"
            AbstractSerialConnection.FLOW_CONTROL_XONXOFF_IN_ENABLED -> "xon/xoff in"
            AbstractSerialConnection.FLOW_CONTROL_CTS_ENABLED -> "rts/cts"
            AbstractSerialConnection.FLOW_CONTROL_DTR_ENABLED -> "dsr/dtr"
            else -> "none"
        }
    }

    /**
     * Returns a string representation of this SerialParameters object.
     *
     * @return a string representation of this SerialParameters object.
     */
    override fun toString(): String {
        return "SerialParameters(" +
                "portName='$portName', " +
                "baudRate=$baudRate, " +
                "flowControlIn=$flowControlIn, " +
                "flowControlOut=$flowControlOut, " +
                "databits=$databits, " +
                "stopbits=$stopbits, " +
                "parity=$parity, " +
                "encoding='${getEncoding()}', " +
                "echo=$echo, " +
                "openDelay=$openDelay, " +
                "rs485Mode=$rs485Mode, " +
                "rs485TxEnableActiveHigh=$rs485TxEnableActiveHigh, " +
                "rs485EnableTermination=$rs485EnableTermination, " +
                "rs485RxDuringTx=$rs485RxDuringTx, " +
                "rs485DelayBeforeTxMicroseconds=${getRs485DelayBeforeTxMicroseconds()}, " +
                "rs485DelayAfterTxMicroseconds=${getRs485DelayAfterTxMicroseconds()}" +
                ")"
    }
}