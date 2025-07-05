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
 * iOS implementation of TCPMasterConnection.
 * This is a stub implementation that will be expanded later.
 */
actual class TCPMasterConnection actual constructor(address: String) {

    private var connected: Boolean = false
    private var addressStr: String = address

    actual var timeout: Int = Modbus.DEFAULT_TIMEOUT
    actual var port: Int = Modbus.DEFAULT_PORT
    actual var useRtuOverTcp: Boolean = false
    actual var useUrgentData: Boolean = false

    /**
     * Opens this TCPMasterConnection.
     *
     * @throws Exception if there is a network failure.
     */
    actual fun connect() {
        connect(useRtuOverTcp)
    }

    /**
     * Opens this TCPMasterConnection with the specified protocol.
     *
     * @param useRtuOverTcp True if the RTU protocol should be used over TCP
     * @throws Exception if there is a network failure.
     */
    actual fun connect(useRtuOverTcp: Boolean) {
        // This is a stub implementation that will be expanded later
        throw UnsupportedOperationException("Not implemented yet")
    }

    /**
     * Tests if this TCPMasterConnection is connected.
     *
     * @return true if connected, false otherwise.
     */
    actual fun isConnected(): Boolean {
        return connected
    }

    /**
     * Closes this TCPMasterConnection.
     */
    actual fun close() {
        connected = false
    }

    /**
     * Returns the ModbusTransport associated with this TCPMasterConnection.
     *
     * @return the connection's ModbusTransport.
     */
    actual fun getModbusTransport(): AbstractModbusTransport? {
        // This is a stub implementation that will be expanded later
        return null
    }

    /**
     * Returns the destination address of this TCPMasterConnection.
     *
     * @return the destination address as String.
     */
    actual fun getAddress(): String {
        return addressStr
    }

    /**
     * Sets the destination address of this TCPMasterConnection.
     *
     * @param address the destination address as String.
     */
    actual fun setAddress(address: String) {
        addressStr = address
    }
}
