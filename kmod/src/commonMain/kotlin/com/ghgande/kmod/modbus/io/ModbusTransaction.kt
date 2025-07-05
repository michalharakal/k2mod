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
import com.ghgande.kmod.modbus.ModbusException
import com.ghgande.kmod.modbus.ModbusIOException
import com.ghgande.kmod.modbus.msg.ModbusRequest
import com.ghgande.kmod.modbus.msg.ModbusResponse
import kotlin.random.Random

/**
 * Abstract class defining a ModbusTransaction.
 * 
 * A transaction is defined by the sequence of sending a request message 
 * and receiving a related response message.
 * 
 * This is a Kotlin Multiplatform version of the original j2mod ModbusTransaction class.
 */
abstract class ModbusTransaction {

    protected var transport: AbstractModbusTransport? = null
    protected var _request: ModbusRequest? = null
    protected var _response: ModbusResponse? = null
    var validityCheck: Boolean = Modbus.DEFAULT_VALIDITYCHECK
    var retries: Int = Modbus.DEFAULT_RETRIES
    private val random = Random.Default

    companion object {
        var transactionID: Int = Modbus.DEFAULT_TRANSACTION_ID
            private set
    }

    /**
     * The ModbusRequest instance associated with this ModbusTransaction.
     * This property can be overridden by subclasses.
     */
    open var request: ModbusRequest?
        get() = _request
        set(req) {
            _request = req
            req?.setTransactionID(getTransactionID())
        }

    /**
     * The ModbusResponse instance associated with this ModbusTransaction.
     * This property can be overridden by subclasses.
     */
    open var response: ModbusResponse?
        get() = _response
        set(res) {
            _response = res
        }

    /**
     * Tests whether the validity of a transaction will be checked.
     *
     * @return true if checking validity, false otherwise.
     */
    fun isCheckingValidity(): Boolean {
        return validityCheck
    }

    /**
     * Sets the flag that controls whether the validity of a transaction will be checked.
     *
     * @param b true if checking validity, false otherwise.
     */
    fun setCheckingValidity(b: Boolean) {
        validityCheck = b
    }

    /**
     * Get the next transaction ID to use.
     * 
     * @return next transaction ID to use
     */
    fun getTransactionID(): Int {
        /*
         * Ensure that the transaction ID is in the valid range between
         * 0 and MAX_TRANSACTION_ID (65534).  If not, the value will be forced
         * to 0.
         */
        if (transactionID < Modbus.DEFAULT_TRANSACTION_ID && isCheckingValidity()) {
            transactionID = Modbus.DEFAULT_TRANSACTION_ID
        }
        if (transactionID >= Modbus.MAX_TRANSACTION_ID) {
            transactionID = Modbus.DEFAULT_TRANSACTION_ID
        }
        return transactionID
    }

    /**
     * A useful method for getting a random sleep time based on an increment of the retry count and retry sleep time
     *
     * @param count Retry count
     * @return Random sleep time in milliseconds
     */
    protected fun getRandomSleepTime(count: Int): Long {
        return (Modbus.RETRY_SLEEP_TIME / 2) + (random.nextDouble() * Modbus.RETRY_SLEEP_TIME * count).toLong()
    }

    /**
     * Checks the validity of the transaction, by checking if the values of the response correspond
     * to the values of the request.
     *
     * @throws ModbusException if the transaction is not valid.
     */
    @Throws(ModbusException::class)
    protected fun checkValidity() {
        val req = request
        val res = response
        if (req != null && res != null) {
            if (req.getUnitID() != res.getUnitID()) {
                throw ModbusIOException("Unit ID mismatch - Request [${req.getHexMessage()}] Response [${res.getHexMessage()}]")
            }
            if (req.getFunctionCode() != res.getFunctionCode()) {
                throw ModbusIOException("Function code mismatch - Request [${req.getHexMessage()}] Response [${res.getHexMessage()}]")
            }
        }
    }

    /**
     * Executes this ModbusTransaction.
     * Locks the ModbusTransport for sending the ModbusRequest and reading the
     * related ModbusResponse.
     * If reconnecting is activated the connection will be opened for the transaction and closed afterwards.
     *
     * @throws ModbusException if an I/O error occurs, or the response is a modbus protocol exception.
     */
    @Throws(ModbusException::class)
    abstract fun execute()
}