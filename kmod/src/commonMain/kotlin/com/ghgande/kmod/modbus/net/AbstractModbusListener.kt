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
package com.ghgande.kmod.modbus.net

import com.ghgande.kmod.modbus.Modbus
import com.ghgande.kmod.modbus.ModbusIOException
import com.ghgande.kmod.modbus.io.AbstractModbusTransport

/**
 * Definition of a listener class for Kotlin Multiplatform.
 * This is a simplified stub implementation that will be expanded later.
 */
abstract class AbstractModbusListener {

    protected var _port: Int = Modbus.DEFAULT_PORT
    protected var _listening: Boolean = false

    /**
     * The listening state of this ModbusListener.
     * This property can be overridden by subclasses.
     */
    open var listening: Boolean
        get() = _listening
        set(value) {
            _listening = value
        }
    protected var error: String? = null
    protected var timeout: Int = Modbus.DEFAULT_TIMEOUT
    protected var threadName: String? = null

    /**
     * The port to be listened to.
     * This property can be overridden by subclasses.
     */
    open var port: Int
        get() = _port
        set(value) {
            _port = if (value > 0) value else Modbus.DEFAULT_PORT
        }

    /**
     * Stop the listener thread for this ModbusListener instance.
     */
    abstract fun stop()

    /**
     * Tests if this ModbusListener is listening and accepting incoming connections.
     *
     * @return true if listening (and accepting incoming connections), false otherwise.
     * @deprecated Use the listening property instead.
     */
    @Deprecated("Use the listening property instead", ReplaceWith("listening"))
    fun isListening(): Boolean {
        return listening
    }

    /**
     * Reads the request, checks it is valid and that the unit ID is ok and sends back a response.
     * This is a stub implementation that will be expanded later.
     *
     * @param transport Transport to read request from
     * @param listener Listener that the request was received by
     * @throws ModbusIOException If there is an issue with the transport or transmission
     */
    @Throws(ModbusIOException::class)
    protected fun handleRequest(transport: AbstractModbusTransport?, listener: AbstractModbusListener) {
        if (transport == null) {
            throw ModbusIOException("No transport specified")
        }

        // This is a stub implementation that will be expanded later
        throw UnsupportedOperationException("Not implemented yet")
    }
}
