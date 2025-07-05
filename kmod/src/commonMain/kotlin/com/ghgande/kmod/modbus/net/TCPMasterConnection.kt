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
import com.ghgande.kmod.modbus.io.AbstractModbusTransport

/**
 * Class that implements a TCPMasterConnection for Kotlin Multiplatform.
 * This is a cross-platform abstraction for TCP socket connections.
 * Platform-specific implementations are provided through expect/actual declarations.
 */
expect class TCPMasterConnection(address: String) {
    /**
     * The timeout for this connection in milliseconds.
     */
    var timeout: Int

    /**
     * The destination port for this connection.
     * Default is Modbus.DEFAULT_PORT (502).
     */
    var port: Int

    /**
     * Flag that controls whether RTU over TCP protocol is used.
     */
    var useRtuOverTcp: Boolean

    /**
     * Flag that controls sending urgent data to test a network connection.
     */
    var useUrgentData: Boolean

    /**
     * Opens this TCPMasterConnection.
     *
     * @throws Exception if there is a network failure.
     */
    @Throws(Exception::class)
    fun connect()

    /**
     * Opens this TCPMasterConnection with the specified protocol.
     *
     * @param useRtuOverTcp True if the RTU protocol should be used over TCP
     * @throws Exception if there is a network failure.
     */
    @Throws(Exception::class)
    fun connect(useRtuOverTcp: Boolean)

    /**
     * Tests if this TCPMasterConnection is connected.
     *
     * @return true if connected, false otherwise.
     */
    fun isConnected(): Boolean

    /**
     * Closes this TCPMasterConnection.
     */
    fun close()

    /**
     * Returns the ModbusTransport associated with this TCPMasterConnection.
     *
     * @return the connection's ModbusTransport.
     */
    fun getModbusTransport(): AbstractModbusTransport?

    /**
     * Returns the destination address of this TCPMasterConnection.
     *
     * @return the destination address as String.
     */
    fun getAddress(): String

    /**
     * Sets the destination address of this TCPMasterConnection.
     *
     * @param address the destination address as String.
     */
    fun setAddress(address: String)
}
