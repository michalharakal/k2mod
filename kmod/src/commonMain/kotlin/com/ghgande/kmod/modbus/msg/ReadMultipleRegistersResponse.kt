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
 * Class implementing a ModbusResponse for reading multiple registers (function code 3).
 * This is a basic implementation that can be used for testing against the j2mod implementation.
 */
class ReadMultipleRegistersResponse : ModbusResponse {
    
    private var unitID: Int = 0
    private var functionCode: Int = Modbus.READ_MULTIPLE_REGISTERS
    private var headless: Boolean = false
    private var transactionID: Int = 0
    private var registers: Array<Register> = emptyArray()
    
    /**
     * Constructs a new ReadMultipleRegistersResponse instance.
     */
    constructor() {
        // Default constructor
    }
    
    /**
     * Constructs a new ReadMultipleRegistersResponse instance with a given count of registers.
     *
     * @param count the number of registers to create.
     */
    constructor(count: Int) {
        registers = Array(count) { SimpleRegister(0) }
    }
    
    /**
     * Returns the registers of this ReadMultipleRegistersResponse.
     *
     * @return the registers as <tt>Register[]</tt>.
     */
    fun getRegisters(): Array<Register> {
        return registers
    }
    
    /**
     * Sets the registers of this ReadMultipleRegistersResponse.
     *
     * @param registers the registers as <tt>Register[]</tt>.
     */
    fun setRegisters(registers: Array<Register>) {
        this.registers = registers
    }
    
    /**
     * Returns the function code of this ModbusResponse.
     *
     * @return the function code as <tt>int</tt>.
     */
    override fun getFunctionCode(): Int {
        return functionCode
    }
    
    /**
     * Sets the function code of this ModbusResponse.
     *
     * @param code the function code as <tt>int</tt>.
     */
    override fun setFunctionCode(code: Int) {
        functionCode = code
    }
    
    /**
     * Returns the unit ID of this ModbusResponse.
     *
     * @return the unit ID as <tt>int</tt>.
     */
    override fun getUnitID(): Int {
        return unitID
    }
    
    /**
     * Sets the unit ID of this ModbusResponse.
     *
     * @param unitID the unit ID as <tt>int</tt>.
     */
    override fun setUnitID(unitID: Int) {
        this.unitID = unitID
    }
    
    /**
     * Sets the headless flag of this message.
     */
    override fun setHeadless() {
        headless = true
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
        return "ReadMultipleRegistersResponse(registers=${registers.size})"
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

/**
 * Simple interface for a Modbus register.
 */
interface Register {
    /**
     * Returns the value of this register as <tt>int</tt>.
     *
     * @return the value as <tt>int</tt>.
     */
    fun getValue(): Int
    
    /**
     * Sets the value of this register.
     *
     * @param value the value as <tt>int</tt>.
     */
    fun setValue(value: Int)
}

/**
 * Simple implementation of a Modbus register.
 */
class SimpleRegister : Register {
    private var value: Int = 0
    
    /**
     * Constructs a new SimpleRegister instance with a given value.
     *
     * @param value the value as <tt>int</tt>.
     */
    constructor(value: Int) {
        this.value = value
    }
    
    /**
     * Returns the value of this register as <tt>int</tt>.
     *
     * @return the value as <tt>int</tt>.
     */
    override fun getValue(): Int {
        return value
    }
    
    /**
     * Sets the value of this register.
     *
     * @param value the value as <tt>int</tt>.
     */
    override fun setValue(value: Int) {
        this.value = value
    }
}