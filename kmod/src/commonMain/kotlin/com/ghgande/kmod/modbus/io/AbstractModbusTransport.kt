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
package com.ghgande.kmod.modbus.io

import com.ghgande.kmod.modbus.Modbus
import com.ghgande.kmod.modbus.ModbusIOException
import com.ghgande.kmod.modbus.msg.ModbusRequest
import com.ghgande.kmod.modbus.msg.ModbusResponse
import com.ghgande.kmod.modbus.net.AbstractModbusListener

/**
 * Abstract class defining the I/O mechanisms for ModbusMessage instances.
 * This is a Kotlin Multiplatform version of the original j2mod AbstractModbusTransport class.
 */
abstract class AbstractModbusTransport {

    /**
     * The timeout for this transport in milliseconds.
     */
    var timeout: Int = Modbus.DEFAULT_TIMEOUT


    /**
     * Closes the raw input and output streams of this ModbusTransport.
     *
     * @throws Exception if a stream cannot be closed properly.
     */
    @Throws(Exception::class)
    abstract fun close()

    /**
     * Creates a Modbus transaction for the underlying transport.
     *
     * @return the new transaction
     */
    abstract fun createTransaction(): ModbusTransaction

    /**
     * Writes a ModbusRequest to the output stream of this ModbusTransport.
     *
     * @param msg a ModbusRequest.
     * @throws ModbusIOException if data cannot be written properly to the raw output stream
     */
    @Throws(ModbusIOException::class)
    abstract fun writeRequest(msg: ModbusRequest)

    /**
     * Writes a ModbusResponse to the output stream of this ModbusTransport.
     *
     * @param msg a ModbusResponse.
     * @throws ModbusIOException if data cannot be written properly to the raw output stream
     */
    @Throws(ModbusIOException::class)
    abstract fun writeResponse(msg: ModbusResponse)

    /**
     * Reads a ModbusRequest from the input stream of this ModbusTransport.
     *
     * @param listener Listener the request was received by
     * @return the ModbusRequest read from the underlying stream.
     * @throws ModbusIOException if data cannot be read properly from the raw input stream
     */
    @Throws(ModbusIOException::class)
    abstract fun readRequest(listener: AbstractModbusListener): ModbusRequest

    /**
     * Reads a ModbusResponse from the input stream of this ModbusTransport.
     *
     * @return the ModbusResponse read from the underlying stream.
     * @throws ModbusIOException if data cannot be read properly from the raw input stream
     */
    @Throws(ModbusIOException::class)
    abstract fun readResponse(): ModbusResponse
}
