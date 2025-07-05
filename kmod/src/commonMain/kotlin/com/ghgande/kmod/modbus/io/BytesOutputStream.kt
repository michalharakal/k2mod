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

/**
 * Class implementing a byte array output stream with
 * a dynamically expandable byte array.
 *
 * This is a stub implementation for the Kotlin Multiplatform migration.
 * The actual implementation will be added later.
 */
class BytesOutputStream(size: Int) {
    private val buffer = ByteArray(size)
    private var count = 0

    /**
     * Returns the reference to the byte array
     * used as buffer for the output data.
     *
     * @return the buffer with the data.
     */
    fun getBuffer(): ByteArray {
        return buffer
    }

    /**
     * Returns the number of bytes written to the buffer.
     *
     * @return the number of bytes written to the buffer.
     */
    fun size(): Int {
        return count
    }

    /**
     * Writes a byte to the buffer.
     *
     * @param b the byte to be written.
     */
    fun write(b: Int) {
        if (count < buffer.size) {
            buffer[count++] = b.toByte()
        }
    }

    /**
     * Writes an array of bytes to the buffer.
     *
     * @param data the byte array to be written.
     */
    fun write(data: ByteArray) {
        write(data, 0, data.size)
    }

    /**
     * Writes an array of bytes to the buffer.
     *
     * @param data the byte array to be written.
     * @param off the offset from which to start writing.
     * @param len the number of bytes to be written.
     */
    fun write(data: ByteArray, off: Int, len: Int) {
        val bytesToWrite = minOf(len, buffer.size - count)
        if (bytesToWrite > 0) {
            data.copyInto(buffer, count, off, off + bytesToWrite)
            count += bytesToWrite
        }
    }

    /**
     * Resets the stream to start from the beginning.
     */
    fun reset() {
        count = 0
    }
}