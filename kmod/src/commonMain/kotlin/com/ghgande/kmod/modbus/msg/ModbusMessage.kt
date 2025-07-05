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

import com.ghgande.kmod.modbus.io.BytesOutputStream

/**
 * Interface defining a ModbusMessage.
 *
 * This is a stub implementation for the Kotlin Multiplatform migration.
 * The actual implementation will be added later.
 */
interface ModbusMessage {
    /**
     * Returns the function code of this ModbusMessage as <tt>int</tt>.
     *
     * @return the function code as <tt>int</tt>.
     */
    fun getFunctionCode(): Int

    /**
     * Returns the function code of this ModbusMessage as <tt>int</tt>.
     *
     * @return the function code as <tt>int</tt>.
     */
    fun getHexMessage(): String

    /**
     * Sets the function code of this ModbusMessage as <tt>int</tt>.
     *
     * @param code the function code as <tt>int</tt>.
     */
    fun setFunctionCode(code: Int)

    /**
     * Returns the protocol identifier of this ModbusMessage as <tt>int</tt>.
     *
     * @return the protocol identifier as <tt>int</tt>.
     */
    fun getProtocolID(): Int

    /**
     * Returns the unit identifier of this ModbusMessage as <tt>int</tt>.
     *
     * @return the unit identifier as <tt>int</tt>.
     */
    fun getUnitID(): Int

    /**
     * Sets the unit identifier of this ModbusMessage as <tt>int</tt>.
     *
     * @param unitID the unit identifier as <tt>int</tt>.
     */
    fun setUnitID(unitID: Int)

    /**
     * Returns the headerlength of the ModbusMessage as <tt>int</tt>.
     *
     * @return the headerlength as <tt>int</tt>.
     */
    fun getHeaderLength(): Int

    /**
     * Returns the transaction identifier of this ModbusMessage as <tt>int</tt>.
     *
     * @return the transaction identifier as <tt>int</tt>.
     */
    fun getTransactionID(): Int

    /**
     * Sets the transaction identifier of this ModbusMessage as <tt>int</tt>.
     *
     * @param transactionID the transaction identifier as <tt>int</tt>.
     */
    fun setTransactionID(transactionID: Int)

    /**
     * Writes this ModbusMessage to the given BytesOutputStream.
     *
     * @param output a BytesOutputStream.
     * @throws Exception if an I/O error occurs.
     */
    @Throws(Exception::class)
    fun writeTo(output: BytesOutputStream)
}