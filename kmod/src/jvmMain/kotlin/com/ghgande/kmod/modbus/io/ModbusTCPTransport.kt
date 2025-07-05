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

import com.ghgande.kmod.modbus.ModbusIOException
import com.ghgande.kmod.modbus.msg.ModbusRequest
import com.ghgande.kmod.modbus.msg.ModbusResponse
import com.ghgande.kmod.modbus.net.AbstractModbusListener
import com.ghgande.kmod.modbus.net.TCPMasterConnection
import java.io.DataInputStream
import java.io.DataOutputStream
import java.io.IOException
import java.net.Socket

/**
 * JVM implementation of ModbusTCPTransport.
 * This is a stub implementation that will be expanded later.
 */
class ModbusTCPTransport : AbstractModbusTransport {

    private var dataInputStream: DataInputStream? = null
    private var dataOutputStream: DataOutputStream? = null
    private var socket: Socket? = null
    private var master: TCPMasterConnection? = null
    private var headless: Boolean = false

    /**
     * Default constructor
     */
    constructor() {
        // Default constructor
    }

    /**
     * Constructs a new ModbusTCPTransport instance, for a given Socket.
     *
     * @param socket the Socket used for message transport.
     */
    constructor(socket: Socket) {
        try {
            setSocket(socket)
            socket.soTimeout = timeout
        } catch (ex: IOException) {
            throw IllegalStateException("Socket invalid", ex)
        }
    }

    /**
     * Sets the Socket used for message transport and prepares the streams used for the actual I/O.
     *
     * @param socket the Socket used for message transport.
     * @throws IOException if an I/O related error occurs.
     */
    @Throws(IOException::class)
    fun setSocket(socket: Socket) {
        this.socket?.close()
        this.socket = socket
        //setTimeout(timeout)
        prepareStreams(socket)
    }

    /**
     * Set the transport to be headless
     */
    fun setHeadless() {
        headless = true
    }

    /**
     * Set the transport to be headless
     *
     * @param headless True if headless
     */
    fun setHeadless(headless: Boolean) {
        this.headless = headless
    }

    /**
     * Sets the master connection for the transport to use
     *
     * @param master Master
     */
    fun setMaster(master: TCPMasterConnection) {
        this.master = master
    }

    /**
     * Set the socket timeout
     *
     * @param time Timeout in milliseconds
     */
    /*
    override fun setTimeout(time: Int) {
        super.setTimeout(time)
        socket?.let {
            try {
                it.soTimeout = time
            } catch (e: Exception) {
                // Ignore
            }
        }
    }

     */

    /**
     * Closes the raw input and output streams of this ModbusTransport.
     *
     * @throws Exception if a stream cannot be closed properly.
     */
    @Throws(Exception::class)
    override fun close() {
        dataInputStream?.close()
        dataOutputStream?.close()
        socket?.close()
    }

    /**
     * Creates a Modbus transaction for the underlying transport.
     *
     * @return the new transaction
     */
    override fun createTransaction(): ModbusTransaction {
        if (master == null) {
            socket?.let {
                val address: String = it.inetAddress.hostAddress
                master = TCPMasterConnection(address)
                master?.port = it.port
                master?.setModbusTransport(this)
            }
        }
        return ModbusTCPTransaction(master ?: throw IllegalStateException("Master connection is null"))
    }

    /**
     * Writes a ModbusRequest to the output stream of this ModbusTransport.
     *
     * @param msg a ModbusRequest.
     * @throws ModbusIOException if data cannot be written properly to the raw output stream
     */
    @Throws(ModbusIOException::class)
    override fun writeRequest(msg: ModbusRequest) {
        // This is a stub implementation that will be expanded later
        throw UnsupportedOperationException("Not implemented yet")
    }

    /**
     * Writes a ModbusResponse to the output stream of this ModbusTransport.
     *
     * @param msg a ModbusResponse.
     * @throws ModbusIOException if data cannot be written properly to the raw output stream
     */
    @Throws(ModbusIOException::class)
    override fun writeResponse(msg: ModbusResponse) {
        // This is a stub implementation that will be expanded later
        throw UnsupportedOperationException("Not implemented yet")
    }

    /**
     * Reads a ModbusRequest from the input stream of this ModbusTransport.
     *
     * @param listener Listener the request was received by
     * @return the ModbusRequest read from the underlying stream.
     * @throws ModbusIOException if data cannot be read properly from the raw input stream
     */
    @Throws(ModbusIOException::class)
    override fun readRequest(listener: AbstractModbusListener): ModbusRequest {
        // This is a stub implementation that will be expanded later
        throw UnsupportedOperationException("Not implemented yet")
    }

    /**
     * Reads a ModbusResponse from the input stream of this ModbusTransport.
     *
     * @return the ModbusResponse read from the underlying stream.
     * @throws ModbusIOException if data cannot be read properly from the raw input stream
     */
    @Throws(ModbusIOException::class)
    override fun readResponse(): ModbusResponse {
        // This is a stub implementation that will be expanded later
        throw UnsupportedOperationException("Not implemented yet")
    }

    /**
     * Prepares the input and output streams of this ModbusTCPTransport instance based on the given socket.
     *
     * @param socket the socket used for communications.
     * @throws IOException if an I/O related error occurs.
     */
    @Throws(IOException::class)
    private fun prepareStreams(socket: Socket) {
        // Close any open streams if I'm being called because a new socket was
        // set to handle this transport.
        try {
            dataInputStream?.close()
            dataOutputStream?.close()
        } catch (x: IOException) {
            // Do nothing.
        }

        dataInputStream = DataInputStream(socket.getInputStream().buffered())
        dataOutputStream = DataOutputStream(socket.getOutputStream().buffered())
    }
}
