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

/**
 * Stub interface for AbstractSerialConnection with constants.
 * This is a temporary implementation for the Kotlin Multiplatform migration.
 * The actual implementation will be added later.
 */
interface AbstractSerialConnection {
    companion object {
        // Flow control constants
        const val FLOW_CONTROL_DISABLED = 0
        const val FLOW_CONTROL_XONXOFF_OUT_ENABLED = 1
        const val FLOW_CONTROL_XONXOFF_IN_ENABLED = 2
        const val FLOW_CONTROL_CTS_ENABLED = 4
        const val FLOW_CONTROL_RTS_ENABLED = 8
        const val FLOW_CONTROL_DSR_ENABLED = 16
        const val FLOW_CONTROL_DTR_ENABLED = 32

        // Parity constants
        const val NO_PARITY = 0
        const val ODD_PARITY = 1
        const val EVEN_PARITY = 2
        const val MARK_PARITY = 3
        const val SPACE_PARITY = 4

        // Stop bits constants
        const val ONE_STOP_BIT = 1
        const val ONE_POINT_FIVE_STOP_BITS = 2
        const val TWO_STOP_BITS = 3

        // Default open delay
        const val OPEN_DELAY = 0
    }
}