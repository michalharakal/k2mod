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

import com.ghgande.kmod.common.logging.Logger

/**
 * Class that implements a collection for bits, storing them packed into bytes.
 * Per default the access operations will index from the LSB (rightmost) bit.
 *
 * This is a Kotlin Multiplatform version of the original j2mod BitVector class.
 */
class BitVector {
    companion object {
        private val logger = Logger.getLogger("BitVector")
        private val ODD_OFFSETS = intArrayOf(-1, -3, -5, -7)
        private val STRAIGHT_OFFSETS = intArrayOf(7, 5, 3, 1)

        // Constants for bit operations
        private const val BYTE_SIZE = 8

        /**
         * Converts an integer to its binary string representation.
         * This is a platform-independent replacement for Integer.toBinaryString.
         *
         * @param value The integer value to convert
         * @return The binary string representation
         */
        private fun toBinaryString(value: Int): String {
            if (value == 0) return "0"

            val result = StringBuilder()
            var temp = value

            while (temp != 0) {
                result.insert(0, if (temp and 1 == 1) '1' else '0')
                temp = temp ushr 1
            }

            return result.toString()
        }

        /**
         * Factory method for creating a BitVector instance
         * wrapping the given byte data.
         *
         * @param data a byte[] containing packed bits.
         * @param size Size to set the bit vector to
         * @return the newly created BitVector instance.
         */
        fun createBitVector(data: ByteArray, size: Int): BitVector {
            val bv = BitVector(data.size * 8)
            bv.setBytes(data)
            bv.size = size
            return bv
        }

        /**
         * Factory method for creating a BitVector instance
         * wrapping the given byte data.
         *
         * @param data a byte[] containing packed bits.
         * @return the newly created BitVector instance.
         */
        fun createBitVector(data: ByteArray): BitVector {
            val bv = BitVector(data.size * 8)
            bv.setBytes(data)
            return bv
        }
    }

    // Instance attributes
    private var size: Int
    private val data: ByteArray
    private var msbAccess = false

    /**
     * Constructs a new BitVector instance with a given size.
     *
     * @param size the number of bits the BitVector should be able to hold.
     */
    constructor(size: Int) {
        // Store bits
        this.size = size

        // Calculate size in bytes
        val byteSize = if (size % 8 > 0) (size / 8) + 1 else (size / 8)
        data = ByteArray(byteSize)
    }

    /**
     * Toggles the flag deciding whether the LSB
     * or the MSB of the byte corresponds to the
     * first bit (index=0).
     *
     * @param b true if LSB=0 up to MSB=7, false otherwise.
     */
    fun toggleAccess(b: Boolean) {
        msbAccess = !msbAccess
    }

    /**
     * Tests if this BitVector has the LSB (rightmost) as the first bit
     * (i.e. at index 0).
     *
     * @return true if LSB=0 up to MSB=7, false otherwise.
     */
    fun isLSBAccess(): Boolean {
        return !msbAccess
    }

    /**
     * Tests if this BitVector has the MSB (leftmost) as the first bit
     * (i.e. at index 0).
     *
     * @return true if MSB=0 down to LSB=7, false otherwise.
     */
    fun isMSBAccess(): Boolean {
        return msbAccess
    }

    /**
     * Returns the byte[] which is used to store the bits of this BitVector.
     *
     * @return the byte[] used to store the bits.
     */
    fun getBytes(): ByteArray {
        // Note: In a multiplatform context, we can't use @Synchronized
        // In a real implementation, we would use a platform-specific synchronization mechanism
        return data.copyOf()
    }

    /**
     * Sets the byte[] which stores the bits of this BitVector.
     *
     * @param data a byte[].
     */
    fun setBytes(data: ByteArray) {
        // Note: In a multiplatform context, we can't use @Synchronized
        // In a real implementation, we would use a platform-specific synchronization mechanism
        data.copyInto(this.data, 0, 0, minOf(data.size, this.data.size))
    }

    /**
     * Sets the byte[] which stores the bits of this BitVector.
     *
     * @param data a byte[].
     * @param size Size to set the bit vector to
     */
    fun setBytes(data: ByteArray, size: Int) {
        data.copyInto(this.data, 0, 0, minOf(data.size, this.data.size))
        this.size = size
    }

    /**
     * Returns the state of the bit at the given index of this BitVector.
     *
     * @param index the index of the bit to be returned.
     * @return true if the bit at the specified index is set, false otherwise.
     * @throws IndexOutOfBoundsException if the index is out of bounds.
     */
    fun getBit(index: Int): Boolean {
        val translatedIndex = translateIndex(index)
        logger.debug { "Get bit #$translatedIndex" }
        val byteVal = data[byteIndex(translatedIndex)].toInt() and 0xFF
        val bitMask = 0x01 shl bitIndex(translatedIndex)
        return (byteVal and bitMask) != 0
    }

    /**
     * Sets the state of the bit at the given index of this BitVector.
     *
     * @param index the index of the bit to be set.
     * @param b true if the bit should be set, false if it should be reset.
     * @throws IndexOutOfBoundsException if the index is out of bounds.
     */
    fun setBit(index: Int, b: Boolean) {
        val translatedIndex = translateIndex(index)
        logger.debug { "Set bit #$translatedIndex" }
        val value = if (b) 1 else 0
        val byteNum = byteIndex(translatedIndex)
        val bitNum = bitIndex(translatedIndex)

        // Convert byte to int, perform bit operations, then convert back to byte
        val byteVal = data[byteNum].toInt() and 0xFF
        val newByteVal = (byteVal and (0x01 shl bitNum).inv()) or ((value and 0x01) shl bitNum)
        data[byteNum] = newByteVal.toByte()
    }

    /**
     * Returns the number of bits in this BitVector as Int.
     *
     * @return the number of bits in this BitVector.
     */
    fun size(): Int {
        return size
    }

    /**
     * Forces the number of bits in this BitVector.
     *
     * @param size Size to set the bit vector to
     * @throws IllegalArgumentException if the size exceeds the byte[] store size multiplied by 8.
     */
    fun forceSize(size: Int) {
        if (size > data.size * 8) {
            throw IllegalArgumentException("Size exceeds byte[] store")
        } else {
            this.size = size
        }
    }

    /**
     * Returns the number of bytes used to store the collection of bits as Int.
     *
     * @return the number of bytes in this BitVector.
     */
    fun byteSize(): Int {
        return data.size
    }

    /**
     * Returns a String representing the contents of the bit collection in a way that
     * can be printed to a screen or log.
     *
     * Note that this representation will ALWAYS show the MSB to the left and the LSB
     * to the right in each byte.
     *
     * @return a String representing this BitVector.
     */
    override fun toString(): String {
        val sbuf = StringBuilder()
        for (i in data.indices) {
            val numberOfBitsToPrint = minOf(BYTE_SIZE, size - (i * BYTE_SIZE))
            if (numberOfBitsToPrint <= 0) break

            val binaryString = toBinaryString(data[i].toInt() and 0xFF)
            val paddedString = binaryString.padStart(numberOfBitsToPrint, '0')
            sbuf.append(paddedString)
            sbuf.append(" ")
        }
        return sbuf.toString()
    }

    /**
     * Returns the index of the byte in the byte array that contains the given bit.
     *
     * @param index the index of the bit.
     * @return the index of the byte where the given bit is stored.
     * @throws IndexOutOfBoundsException if index is out of bounds.
     */
    private fun byteIndex(index: Int): Int {
        if (index < 0 || index >= data.size * 8) {
            throw IndexOutOfBoundsException("Bit index out of bounds: $index")
        } else {
            return index / 8
        }
    }

    /**
     * Returns the index of the given bit in the byte where it is stored.
     *
     * @param index the index of the bit.
     * @return the bit index relative to the position in the byte that stores the specified bit.
     * @throws IndexOutOfBoundsException if index is out of bounds.
     */
    private fun bitIndex(index: Int): Int {
        if (index < 0 || index >= data.size * 8) {
            throw IndexOutOfBoundsException("Bit index out of bounds: $index")
        } else {
            return index % 8
        }
    }

    /**
     * Translates the bit index based on the access mode (LSB or MSB).
     *
     * @param idx the original index.
     * @return the translated index.
     */
    private fun translateIndex(idx: Int): Int {
        return if (msbAccess) {
            val mod4 = idx % 4
            val div4 = idx / 4

            if (div4 % 2 != 0) {
                // odd
                idx + ODD_OFFSETS[mod4]
            } else {
                // straight
                idx + STRAIGHT_OFFSETS[mod4]
            }
        } else {
            idx
        }
    }
}
