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

/**
 * Interface for a ModbusRequest message.
 * This is a stub implementation for the Kotlin Multiplatform migration.
 * The actual implementation will be added later.
 */
interface ModbusRequest : ModbusMessage {
    /**
     * Sets the headless flag of this message.
     * 
     * @param b true if headless, false otherwise.
     */
    fun setHeadless(b: Boolean)
    
    /**
     * Returns the headless flag of this message.
     * 
     * @return true if headless, false otherwise.
     */
    fun isHeadless(): Boolean
    
    /**
     * Sets the data length of this message.
     * 
     * @param length the data length as <tt>int</tt>.
     */
    fun setDataLength(length: Int)
    
    companion object {
        /**
         * Factory method for creating a request based on a function code.
         * This is a stub implementation that will be replaced with the actual implementation.
         * 
         * @param functionCode the function code as <tt>int</tt>.
         * @return a ModbusRequest instance specific for the given function code.
         */
        fun createModbusRequest(functionCode: Int): ModbusRequest {
            // This is a stub implementation that will be replaced with the actual implementation
            throw UnsupportedOperationException("Not implemented yet")
        }
    }
}