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
package com.ghgande.kmod.modbus.util

/**
 * Extension functions for String class to provide cross-platform functionality.
 */

/**
 * Formats a string by replacing placeholders with provided values.
 * This is a simple implementation of String.format for Kotlin Multiplatform.
 * It replaces %s, %d, etc. with the corresponding values from the vararg parameter.
 *
 * @param args The values to be inserted into the format string
 * @return The formatted string
 */
fun String.format(vararg args: Any?): String {
    if (args.isEmpty()) return this
    
    var result = this
    var i = 0
    val regex = Regex("%[\\w\\d\\.]*")
    
    return regex.replace(result) { matchResult ->
        if (i < args.size) {
            val arg = args[i++]
            arg?.toString() ?: "null"
        } else {
            matchResult.value
        }
    }
}