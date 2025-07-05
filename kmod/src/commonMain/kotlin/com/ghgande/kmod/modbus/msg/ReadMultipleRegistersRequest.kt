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
package com.ghgande.kmod.modbus.msg

import com.ghgande.kmod.modbus.Modbus
import com.ghgande.kmod.modbus.io.BytesOutputStream

/**
 * Class implementing a ModbusRequest for reading multiple registers (function code 3).
 * This is a basic implementation that can be used for testing against the j2mod implementation.
 */
class ReadMultipleRegistersRequest : ModbusRequest {

    private var reference: Int = 0
    private var wordCount: Int = 0
    private var unitID: Int = 0
    private var headless: Boolean = false
    private var functionCode: Int = Modbus.READ_MULTIPLE_REGISTERS
    private var transactionID: Int = 0

    /**
     * Constructs a new ReadMultipleRegistersRequest instance.
     */
    constructor() {
        // Default constructor
    }

    /**
     * Constructs a new ReadMultipleRegistersRequest instance with a given reference and count.
     *
     * @param ref the reference number of the register to read from.
     * @param count the number of registers to read.
     */
    constructor(ref: Int, count: Int) {
        reference = ref
        wordCount = count
    }

    /**
     * Returns the reference of the register to start reading from.
     *
     * @return the reference of the register to start reading from.
     */
    fun getReference(): Int {
        return reference
    }

    /**
     * Sets the reference of the register to start reading from.
     *
     * @param ref the reference of the register to start reading from.
     */
    fun setReference(ref: Int) {
        reference = ref
    }

    /**
     * Returns the number of registers to be read.
     *
     * @return the number of registers to be read.
     */
    fun getWordCount(): Int {
        return wordCount
    }

    /**
     * Sets the number of registers to be read.
     *
     * @param count the number of registers to be read.
     */
    fun setWordCount(count: Int) {
        wordCount = count
    }

    /**
     * Returns the function code of this ModbusRequest.
     *
     * @return the function code as <tt>int</tt>.
     */
    override fun getFunctionCode(): Int {
        return functionCode
    }

    /**
     * Sets the function code of this ModbusRequest.
     *
     * @param code the function code as <tt>int</tt>.
     */
    override fun setFunctionCode(code: Int) {
        functionCode = code
    }

    /**
     * Returns the unit ID of this ModbusRequest.
     *
     * @return the unit ID as <tt>int</tt>.
     */
    override fun getUnitID(): Int {
        return unitID
    }

    /**
     * Sets the unit ID of this ModbusRequest.
     *
     * @param unitID the unit ID as <tt>int</tt>.
     */
    override fun setUnitID(unitID: Int) {
        this.unitID = unitID
    }

    /**
     * Sets the headless flag of this message.
     *
     * @param b true if headless, false otherwise.
     */
    override fun setHeadless(b: Boolean) {
        headless = b
    }

    /**
     * Returns the headless flag of this message.
     *
     * @return true if headless, false otherwise.
     */
    override fun isHeadless(): Boolean {
        return headless
    }

    /**
     * Sets the data length of this message.
     *
     * @param length the data length as <tt>int</tt>.
     */
    override fun setDataLength(length: Int) {
        // Not implemented for this message type
    }

    /**
     * Returns the protocol identifier of this message.
     *
     * @return the protocol identifier as <tt>int</tt>.
     */
    override fun getProtocolID(): Int {
        return 0 // Default protocol ID
    }

    /**
     * Returns the header length of this message.
     *
     * @return the header length as <tt>int</tt>.
     */
    override fun getHeaderLength(): Int {
        return 7 // Default header length for Modbus/TCP
    }

    /**
     * Returns the transaction identifier of this message.
     *
     * @return the transaction identifier as <tt>int</tt>.
     */
    override fun getTransactionID(): Int {
        return transactionID
    }

    /**
     * Sets the transaction identifier of this message.
     *
     * @param transactionID the transaction identifier as <tt>int</tt>.
     */
    override fun setTransactionID(transactionID: Int) {
        this.transactionID = transactionID
    }

    /**
     * Returns a hexadecimal representation of this message.
     *
     * @return a hexadecimal representation of this message.
     */
    override fun getHexMessage(): String {
        return "ReadMultipleRegistersRequest(ref=$reference, count=$wordCount)"
    }

    /**
     * Writes this message to the given BytesOutputStream.
     *
     * @param output a BytesOutputStream.
     */
    @Throws(Exception::class)
    override fun writeTo(output: BytesOutputStream) {
        // This is a stub implementation for testing
        // In a real implementation, this would write the message to the output stream
    }
}
