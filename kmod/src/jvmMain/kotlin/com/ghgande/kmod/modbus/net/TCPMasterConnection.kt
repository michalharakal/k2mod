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
import com.ghgande.kmod.modbus.io.ModbusTCPTransport
import com.ghgande.kmod.modbus.util.ModbusUtil
import java.io.IOException
import java.net.InetAddress
import java.net.InetSocketAddress
import java.net.Socket
import java.net.Proxy

/**
 * JVM implementation of TCPMasterConnection.
 * This class implements a TCP master connection for the JVM platform.
 */
actual class TCPMasterConnection actual constructor(address: String) {

    private var socket: Socket? = null
    actual var timeout: Int = Modbus.DEFAULT_TIMEOUT

    /**
     * Sets the timeout (msec) for this TCPMasterConnection. This is both the
     * connection timeout and the transaction timeout
     *
     * @param timeout - the timeout in milliseconds as an Int.
     */
    fun updateTimeout(timeout: Int) {
        try {
            this.timeout = timeout
            socket?.soTimeout = timeout
        } catch (ex: IOException) {
            // Ignore
        }
    }
    private var connected: Boolean = false

    private var address: InetAddress
    private var networkInterface: java.net.NetworkInterface? = null
    private var proxy: Proxy? = null
    actual var port: Int = Modbus.DEFAULT_PORT

    private var transport: ModbusTCPTransport? = null

    actual var useRtuOverTcp: Boolean = false
    actual var useUrgentData: Boolean = false

    init {
        this.address = InetAddress.getByName(address)
    }

    /**
     * Prepares the associated ModbusTransport of this TCPMasterConnection for use.
     *
     * @param useRtuOverTcp True if the RTU protocol should be used over TCP
     * @throws IOException if an I/O related error occurs.
     */
    @Throws(IOException::class)
    private fun prepareTransport(useRtuOverTcp: Boolean) {
        // If we don't have a transport, or the transport type has changed
        if (transport == null || (this.useRtuOverTcp != useRtuOverTcp)) {
            // Save the flag to tell us which transport type to use
            this.useRtuOverTcp = useRtuOverTcp

            // Select the correct transport
            val socket = this.socket ?: throw IOException("Socket is null")

            // For now, we'll just use the standard TCP transport
            // In a real implementation, we would create ModbusRTUTCPTransport if useRtuOverTcp is true
            transport = ModbusTCPTransport(socket)
            transport?.setMaster(this)
        } else {
            socket?.let { transport?.setSocket(it) }
        }
        //transport?.setTimeout(timeout)
    }

    /**
     * Opens this TCPMasterConnection.
     *
     * @throws Exception if there is a network failure.
     */
    @Throws(Exception::class)
    actual fun connect() {
        connect(useRtuOverTcp)
    }

    /**
     * Opens this TCPMasterConnection with the specified protocol.
     *
     * @param useRtuOverTcp True if the RTU protocol should be used over TCP
     * @throws Exception if there is a network failure.
     */
    @Throws(Exception::class)
    actual fun connect(useRtuOverTcp: Boolean) {
        if (!isConnected()) {
            // Create a socket without auto-connecting
            socket = if (proxy != null) Socket(proxy) else Socket()
            socket?.reuseAddress = true
            socket?.setSoLinger(true, 1)
            socket?.keepAlive = true
            updateTimeout(timeout)

            // If a Network Interface has been specified, then attempt to force the socket
            // to be bound to that card
            if (networkInterface != null) {
                socket?.bind(InetSocketAddress(networkInterface?.inetAddresses?.nextElement(), 0))
            }

            // Connect - only wait for the timeout number of milliseconds
            socket?.connect(InetSocketAddress(address, port), timeout)

            // Prepare the transport
            prepareTransport(useRtuOverTcp)
            connected = true
        }
    }

    /**
     * Tests if this TCPMasterConnection is connected.
     *
     * @return true if connected, false otherwise.
     */
    actual fun isConnected(): Boolean {
        val socket = this.socket
        if (connected && socket != null) {
            if (!socket.isConnected || socket.isClosed || socket.isInputShutdown || socket.isOutputShutdown) {
                try {
                    socket.close()
                } catch (e: IOException) {
                    // Ignore
                } finally {
                    connected = false
                }
            } else {
                /*
                 * When useUrgentData is set, a byte of urgent data
                 * will be sent to the server to test the connection. If
                 * the connection is actually broken, an Exception will
                 * occur and the connection will be closed.
                 *
                 * Note: RFC 6093 has decreed that we stop using urgent
                 * data.
                 */
                if (useUrgentData) {
                    try {
                        socket.sendUrgentData(0)
                        ModbusUtil.sleep(5)
                    } catch (e: IOException) {
                        connected = false
                        try {
                            socket.close()
                        } catch (e1: IOException) {
                            // Do nothing.
                        }
                    }
                }
            }
        }
        return connected
    }

    /**
     * Closes this TCPMasterConnection.
     */
    actual fun close() {
        if (connected) {
            try {
                transport?.close()
            } catch (ex: IOException) {
                // Ignore
            } finally {
                connected = false
            }
        }
    }

    /**
     * Returns the ModbusTransport associated with this TCPMasterConnection.
     *
     * @return the connection's ModbusTransport.
     */
    actual fun getModbusTransport(): AbstractModbusTransport? {
        return transport
    }

    /**
     * Sets the ModbusTransport associated with this TCPMasterConnection.
     *
     * @param trans associated transport
     */
    fun setModbusTransport(trans: ModbusTCPTransport) {
        transport = trans
    }

    /**
     * Returns the destination address of this TCPMasterConnection.
     *
     * @return the destination address as String.
     */
    actual fun getAddress(): String {
        return address.hostAddress
    }

    /**
     * Sets the destination address of this TCPMasterConnection.
     *
     * @param address the destination address as String.
     */
    actual fun setAddress(address: String) {
        this.address = InetAddress.getByName(address)
    }

    /**
     * Gets the local NetworkInterface that this socket is bound to.
     * If null (the default), the socket is bound to the adapter chosen by the system
     * based on routing to the destination address when the connection is made
     *
     * @return the network card as NetworkInterface.
     */
    fun getNetworkInterface(): java.net.NetworkInterface? {
        return networkInterface
    }

    /**
     * Sets the local NetworkInterface to bind to for this TCPMasterConnection.
     *
     * @param networkInterface of the network card as NetworkInterface.
     */
    fun setNetworkInterface(networkInterface: java.net.NetworkInterface?) {
        this.networkInterface = networkInterface
    }

    /**
     * Sets the Proxy that this socket uses. If null (the default), no proxy is used.
     */
    fun setProxy(proxy: Proxy?) {
        if (socket != null && proxy != null) {
            throw IllegalStateException("Cannot set proxy after connection has been established")
        }
        this.proxy = proxy
    }
}
