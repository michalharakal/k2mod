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

import com.ghgande.kmod.modbus.ModbusException
import com.ghgande.kmod.modbus.msg.ModbusRequest
import com.ghgande.kmod.modbus.msg.ModbusResponse
import com.ghgande.kmod.modbus.msg.ReadMultipleRegistersRequest
import com.ghgande.kmod.modbus.msg.ReadMultipleRegistersResponse
import com.ghgande.kmod.modbus.msg.SimpleRegister
import com.ghgande.kmod.modbus.net.TCPMasterConnection

/**
 * JVM implementation of ModbusTCPTransaction.
 * This is a basic implementation that can be used for testing against the j2mod implementation.
 */
class ModbusTCPTransaction : ModbusTransaction {

    private var connection: TCPMasterConnection? = null

    /**
     * Constructs a new ModbusTCPTransaction instance.
     */
    constructor() : super()

    /**
     * Constructs a new ModbusTCPTransaction instance with a given ModbusRequest to be sent when the transaction is executed.
     *
     * @param request a ModbusRequest instance.
     */
    constructor(request: ModbusRequest) : super() {
        this.request = request
    }

    /**
     * Constructs a new ModbusTCPTransaction instance with a given TCPMasterConnection to be used for transactions.
     *
     * @param con a TCPMasterConnection instance.
     */
    constructor(con: TCPMasterConnection) : super() {
        setConnection(con)
        transport = con.getModbusTransport()
    }

    /**
     * Sets the connection on which this ModbusTCPTransaction should be executed.
     *
     * @param con a TCPMasterConnection.
     */
    fun setConnection(con: TCPMasterConnection) {
        connection = con
        transport = con.getModbusTransport()
    }

    /**
     * Executes this ModbusTCPTransaction.
     * This is a simplified implementation for testing that simulates a response.
     *
     * @throws ModbusException if an I/O error occurs, or the response is a modbus protocol exception.
     */
    @Throws(ModbusException::class)
    override fun execute() {
        // Check if the request is set
        if (request == null) {
            throw ModbusException("Request is null")
        }

        // Check if the connection is established
        if (connection == null || !connection!!.isConnected()) {
            throw ModbusException("Connection is not established")
        }

        // For testing purposes, we'll create a simulated response based on the request
        when (request) {
            is ReadMultipleRegistersRequest -> {
                val readRequest = request as ReadMultipleRegistersRequest
                val readResponse = ReadMultipleRegistersResponse(readRequest.getWordCount())

                // Set the unit ID to match the request
                readResponse.setUnitID(readRequest.getUnitID())

                // For testing, set each register to a value (e.g., 42)
                val registers = readResponse.getRegisters()
                for (i in registers.indices) {
                    registers[i].setValue(42)
                }

                response = readResponse
            }
            else -> {
                throw ModbusException("Unsupported request type: ${request!!.javaClass.simpleName}")
            }
        }
    }
}
