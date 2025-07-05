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
import com.ghgande.kmod.modbus.net.TCPMasterConnection

/**
 * JVM implementation of ModbusTCPTransaction.
 * This is a stub implementation that will be expanded later.
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
     * Locks the ModbusTransport for sending the ModbusRequest and reading the related ModbusResponse.
     *
     * @throws ModbusException if an I/O error occurs, or the response is a modbus protocol exception.
     */
    @Throws(ModbusException::class)
    override fun execute() {
        // This is a stub implementation that will be expanded later
        throw UnsupportedOperationException("Not implemented yet")
    }
}