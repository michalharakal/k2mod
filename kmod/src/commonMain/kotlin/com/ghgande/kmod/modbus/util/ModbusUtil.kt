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
import com.ghgande.kmod.modbus.Modbus
import com.ghgande.kmod.modbus.io.BytesOutputStream
import com.ghgande.kmod.modbus.msg.ModbusMessage

/**
 * Helper class that provides utility methods.
 *
 * This is a Kotlin Multiplatform version of the original j2mod ModbusUtil class.
 */
object ModbusUtil {
    private val logger = Logger.getLogger("ModbusUtil")

    /* Table of CRC values for high-order byte */
    private val auchCRCHi = shortArrayOf(
        0x00, 0xC1, 0x81, 0x40, 0x01, 0xC0, 0x80, 0x41, 0x01, 0xC0,
        0x80, 0x41, 0x00, 0xC1, 0x81, 0x40, 0x01, 0xC0, 0x80, 0x41,
        0x00, 0xC1, 0x81, 0x40, 0x00, 0xC1, 0x81, 0x40, 0x01, 0xC0,
        0x80, 0x41, 0x01, 0xC0, 0x80, 0x41, 0x00, 0xC1, 0x81, 0x40,
        0x00, 0xC1, 0x81, 0x40, 0x01, 0xC0, 0x80, 0x41, 0x00, 0xC1,
        0x81, 0x40, 0x01, 0xC0, 0x80, 0x41, 0x01, 0xC0, 0x80, 0x41,
        0x00, 0xC1, 0x81, 0x40, 0x01, 0xC0, 0x80, 0x41, 0x00, 0xC1,
        0x81, 0x40, 0x00, 0xC1, 0x81, 0x40, 0x01, 0xC0, 0x80, 0x41,
        0x00, 0xC1, 0x81, 0x40, 0x01, 0xC0, 0x80, 0x41, 0x01, 0xC0,
        0x80, 0x41, 0x00, 0xC1, 0x81, 0x40, 0x00, 0xC1, 0x81, 0x40,
        0x01, 0xC0, 0x80, 0x41, 0x01, 0xC0, 0x80, 0x41, 0x00, 0xC1,
        0x81, 0x40, 0x01, 0xC0, 0x80, 0x41, 0x00, 0xC1, 0x81, 0x40,
        0x00, 0xC1, 0x81, 0x40, 0x01, 0xC0, 0x80, 0x41, 0x01, 0xC0,
        0x80, 0x41, 0x00, 0xC1, 0x81, 0x40, 0x00, 0xC1, 0x81, 0x40,
        0x01, 0xC0, 0x80, 0x41, 0x00, 0xC1, 0x81, 0x40, 0x01, 0xC0,
        0x80, 0x41, 0x01, 0xC0, 0x80, 0x41, 0x00, 0xC1, 0x81, 0x40,
        0x00, 0xC1, 0x81, 0x40, 0x01, 0xC0, 0x80, 0x41, 0x01, 0xC0,
        0x80, 0x41, 0x00, 0xC1, 0x81, 0x40, 0x01, 0xC0, 0x80, 0x41,
        0x00, 0xC1, 0x81, 0x40, 0x00, 0xC1, 0x81, 0x40, 0x01, 0xC0,
        0x80, 0x41, 0x00, 0xC1, 0x81, 0x40, 0x01, 0xC0, 0x80, 0x41,
        0x01, 0xC0, 0x80, 0x41, 0x00, 0xC1, 0x81, 0x40, 0x01, 0xC0,
        0x80, 0x41, 0x00, 0xC1, 0x81, 0x40, 0x00, 0xC1, 0x81, 0x40,
        0x01, 0xC0, 0x80, 0x41, 0x01, 0xC0, 0x80, 0x41, 0x00, 0xC1,
        0x81, 0x40, 0x00, 0xC1, 0x81, 0x40, 0x01, 0xC0, 0x80, 0x41,
        0x00, 0xC1, 0x81, 0x40, 0x01, 0xC0, 0x80, 0x41, 0x01, 0xC0,
        0x80, 0x41, 0x00, 0xC1, 0x81, 0x40
    )

    /* Table of CRC values for low-order byte */
    private val auchCRCLo = shortArrayOf(
        0x00, 0xC0, 0xC1, 0x01, 0xC3, 0x03, 0x02, 0xC2, 0xC6, 0x06,
        0x07, 0xC7, 0x05, 0xC5, 0xC4, 0x04, 0xCC, 0x0C, 0x0D, 0xCD,
        0x0F, 0xCF, 0xCE, 0x0E, 0x0A, 0xCA, 0xCB, 0x0B, 0xC9, 0x09,
        0x08, 0xC8, 0xD8, 0x18, 0x19, 0xD9, 0x1B, 0xDB, 0xDA, 0x1A,
        0x1E, 0xDE, 0xDF, 0x1F, 0xDD, 0x1D, 0x1C, 0xDC, 0x14, 0xD4,
        0xD5, 0x15, 0xD7, 0x17, 0x16, 0xD6, 0xD2, 0x12, 0x13, 0xD3,
        0x11, 0xD1, 0xD0, 0x10, 0xF0, 0x30, 0x31, 0xF1, 0x33, 0xF3,
        0xF2, 0x32, 0x36, 0xF6, 0xF7, 0x37, 0xF5, 0x35, 0x34, 0xF4,
        0x3C, 0xFC, 0xFD, 0x3D, 0xFF, 0x3F, 0x3E, 0xFE, 0xFA, 0x3A,
        0x3B, 0xFB, 0x39, 0xF9, 0xF8, 0x38, 0x28, 0xE8, 0xE9, 0x29,
        0xEB, 0x2B, 0x2A, 0xEA, 0xEE, 0x2E, 0x2F, 0xEF, 0x2D, 0xED,
        0xEC, 0x2C, 0xE4, 0x24, 0x25, 0xE5, 0x27, 0xE7, 0xE6, 0x26,
        0x22, 0xE2, 0xE3, 0x23, 0xE1, 0x21, 0x20, 0xE0, 0xA0, 0x60,
        0x61, 0xA1, 0x63, 0xA3, 0xA2, 0x62, 0x66, 0xA6, 0xA7, 0x67,
        0xA5, 0x65, 0x64, 0xA4, 0x6C, 0xAC, 0xAD, 0x6D, 0xAF, 0x6F,
        0x6E, 0xAE, 0xAA, 0x6A, 0x6B, 0xAB, 0x69, 0xA9, 0xA8, 0x68,
        0x78, 0xB8, 0xB9, 0x79, 0xBB, 0x7B, 0x7A, 0xBA, 0xBE, 0x7E,
        0x7F, 0xBF, 0x7D, 0xBD, 0xBC, 0x7C, 0xB4, 0x74, 0x75, 0xB5,
        0x77, 0xB7, 0xB6, 0x76, 0x72, 0xB2, 0xB3, 0x73, 0xB1, 0x71,
        0x70, 0xB0, 0x50, 0x90, 0x91, 0x51, 0x93, 0x53, 0x52, 0x92,
        0x96, 0x56, 0x57, 0x97, 0x55, 0x95, 0x94, 0x54, 0x9C, 0x5C,
        0x5D, 0x9D, 0x5F, 0x9F, 0x9E, 0x5E, 0x5A, 0x9A, 0x9B, 0x5B,
        0x99, 0x59, 0x58, 0x98, 0x88, 0x48, 0x49, 0x89, 0x4B, 0x8B,
        0x8A, 0x4A, 0x4E, 0x8E, 0x8F, 0x4F, 0x8D, 0x4D, 0x4C, 0x8C,
        0x44, 0x84, 0x85, 0x45, 0x87, 0x47, 0x46, 0x86, 0x82, 0x42,
        0x43, 0x83, 0x41, 0x81, 0x80, 0x40
    )

    /**
     * Converts a ModbusMessage instance into a hex encoded string representation.
     *
     * @param msg the message to be converted.
     * @return the converted hex encoded string representation of the message.
     */
    fun toHex(msg: ModbusMessage): String {
        val byteOutputStream = BytesOutputStream(Modbus.MAX_MESSAGE_LENGTH)
        var ret = "-1"
        try {
            msg.writeTo(byteOutputStream)
            ret = toHex(byteOutputStream.getBuffer(), 0, byteOutputStream.size())
        } catch (ex: Exception) {
            logger.debug { "Hex conversion error $ex" }
        }
        return ret
    }

    /**
     * Returns the given byte[] as hex encoded string.
     *
     * @param data a byte[] array.
     * @return a hex encoded String.
     */
    fun toHex(data: ByteArray): String {
        return toHex(data, 0, data.size)
    }

    /**
     * Returns a String containing unsigned hexadecimal numbers as digits.
     * The String will contain two hex digit characters for each byte from the passed in byte[].
     * The bytes will be separated by a space character.
     *
     * @param data the array of bytes to be converted into a hex-string.
     * @param off the offset to start converting from.
     * @param end the offset of the end of the byte array.
     * @return the generated hexadecimal representation as String.
     */
    fun toHex(data: ByteArray, off: Int, end: Int): String {
        // Double size, two bytes (hex range) for one byte
        val buf = StringBuilder(data.size * 2)
        val actualEnd = minOf(end, data.size)

        for (i in off until actualEnd) {
            // Don't forget the second hex digit
            if ((data[i].toInt() and 0xff) < 0x10) {
                buf.append("0")
            }
            buf.append((data[i].toInt() and 0xff).toString(16).uppercase())
            if (i < actualEnd - 1) {
                buf.append(" ")
            }
        }
        return buf.toString()
    }

    /**
     * Returns a byte[] containing the given byte as unsigned hexadecimal number digits.
     *
     * @param i the int to be converted into a hex string.
     * @return the generated hexadecimal representation as byte[].
     */
    fun toHex(i: Int): ByteArray? {
        val buf = StringBuilder(2)
        // Don't forget the second hex digit
        if ((i and 0xff) < 0x10) {
            buf.append("0")
        }
        buf.append((i and 0xff).toString(16).uppercase())

        return try {
            buf.toString().encodeToByteArray()
        } catch (e: Exception) {
            logger.debug { "Problem converting bytes to string - ${e.message}" }
            null
        }
    }

    /**
     * Converts the register (a 16 bit value) into an unsigned short.
     * The value returned is:
     * (((a & 0xff) << 8) | (b & 0xff))
     *
     * @param bytes a register as byte[2].
     * @return the unsigned short value as int.
     */
    fun registerToUnsignedShort(bytes: ByteArray): Int {
        return ((bytes[0].toInt() and 0xff) shl 8) or (bytes[1].toInt() and 0xff)
    }

    /**
     * Converts the given unsigned short into a register (2 bytes).
     * The byte values in the register, in the order shown, are:
     * (byte)(0xff & (v >> 8))
     * (byte)(0xff & v)
     *
     * @param v Value to convert
     * @return the register as byte[2].
     */
    fun unsignedShortToRegister(v: Int): ByteArray {
        val register = ByteArray(2)
        register[0] = (0xff and (v shr 8)).toByte()
        register[1] = (0xff and v).toByte()
        return register
    }

    /**
     * Converts the given register (16-bit value) into a short.
     * The value returned is:
     * (short)((a << 8) | (b & 0xff))
     *
     * @param bytes a register as byte[2].
     * @return the signed short as short.
     */
    fun registerToShort(bytes: ByteArray): Short {
        return ((bytes[0].toInt() shl 8) or (bytes[1].toInt() and 0xff)).toShort()
    }

    /**
     * Converts the register (16-bit value) at the given index into a short.
     * The value returned is:
     * (short)((a << 8) | (b & 0xff))
     *
     * @param bytes a byte[] containing a short value.
     * @param idx an offset into the given byte[].
     * @return the signed short as short.
     */
    fun registerToShort(bytes: ByteArray, idx: Int): Short {
        return ((bytes[idx].toInt() shl 8) or (bytes[idx + 1].toInt() and 0xff)).toShort()
    }

    /**
     * Converts the given short into a register (2 bytes).
     * The byte values in the register, in the order shown, are:
     * (byte)(0xff & (v >> 8))
     * (byte)(0xff & v)
     *
     * @param s Value to convert
     * @return a register containing the given short value.
     */
    fun shortToRegister(s: Short): ByteArray {
        val register = ByteArray(2)
        register[0] = (0xff and (s.toInt() shr 8)).toByte()
        register[1] = (0xff and s.toInt()).toByte()
        return register
    }

    /**
     * Converts a byte[4] binary int value to a primitive int.
     * The value returned is:
     * (((a & 0xff) << 24) | ((b & 0xff) << 16) | ((c & 0xff) << 8) | (d & 0xff))
     *
     * @param bytes registers as byte[4].
     * @return the integer contained in the given register bytes.
     */
    fun registersToInt(bytes: ByteArray): Int {
        return (((bytes[0].toInt() and 0xff) shl 24) or
                ((bytes[1].toInt() and 0xff) shl 16) or
                ((bytes[2].toInt() and 0xff) shl 8) or
                (bytes[3].toInt() and 0xff))
    }

    /**
     * Converts an int value to a byte[4] array.
     *
     * @param v the value to be converted.
     * @return a byte[4] containing the value.
     */
    fun intToRegisters(v: Int): ByteArray {
        val registers = ByteArray(4)
        registers[0] = (0xff and (v shr 24)).toByte()
        registers[1] = (0xff and (v shr 16)).toByte()
        registers[2] = (0xff and (v shr 8)).toByte()
        registers[3] = (0xff and v).toByte()
        return registers
    }

    /**
     * Converts a byte[8] binary long value into a long primitive.
     *
     * @param bytes a byte[8] containing a long value.
     * @return a long value.
     */
    fun registersToLong(bytes: ByteArray): Long {
        return (((bytes[0].toLong() and 0xff) shl 56) or
                ((bytes[1].toLong() and 0xff) shl 48) or
                ((bytes[2].toLong() and 0xff) shl 40) or
                ((bytes[3].toLong() and 0xff) shl 32) or
                ((bytes[4].toLong() and 0xff) shl 24) or
                ((bytes[5].toLong() and 0xff) shl 16) or
                ((bytes[6].toLong() and 0xff) shl 8) or
                (bytes[7].toLong() and 0xff))
    }

    /**
     * Converts a long value to a byte[8].
     *
     * @param v the value to be converted.
     * @return a byte[8] containing the long value.
     */
    fun longToRegisters(v: Long): ByteArray {
        val registers = ByteArray(8)
        registers[0] = (0xffL and (v shr 56)).toByte()
        registers[1] = (0xffL and (v shr 48)).toByte()
        registers[2] = (0xffL and (v shr 40)).toByte()
        registers[3] = (0xffL and (v shr 32)).toByte()
        registers[4] = (0xffL and (v shr 24)).toByte()
        registers[5] = (0xffL and (v shr 16)).toByte()
        registers[6] = (0xffL and (v shr 8)).toByte()
        registers[7] = (0xffL and v).toByte()
        return registers
    }

    /**
     * Converts a byte[4] binary float value to a float primitive.
     *
     * @param bytes the byte[4] containing the float value.
     * @return a float value.
     */
    fun registersToFloat(bytes: ByteArray): Float {
        val intBits = (((bytes[0].toInt() and 0xff) shl 24) or
                ((bytes[1].toInt() and 0xff) shl 16) or
                ((bytes[2].toInt() and 0xff) shl 8) or
                (bytes[3].toInt() and 0xff))
        return Float.fromBits(intBits)
    }

    /**
     * Converts a float value to a byte[4] binary float value.
     *
     * @param f the float to be converted.
     * @return a byte[4] containing the float value.
     */
    fun floatToRegisters(f: Float): ByteArray {
        return intToRegisters(f.toBits())
    }

    /**
     * Converts a byte[8] binary double value into a double primitive.
     *
     * @param bytes a byte[8] to be converted.
     * @return a double value.
     */
    fun registersToDouble(bytes: ByteArray): Double {
        val longBits = (((bytes[0].toLong() and 0xff) shl 56) or
                ((bytes[1].toLong() and 0xff) shl 48) or
                ((bytes[2].toLong() and 0xff) shl 40) or
                ((bytes[3].toLong() and 0xff) shl 32) or
                ((bytes[4].toLong() and 0xff) shl 24) or
                ((bytes[5].toLong() and 0xff) shl 16) or
                ((bytes[6].toLong() and 0xff) shl 8) or
                (bytes[7].toLong() and 0xff))
        return Double.fromBits(longBits)
    }

    /**
     * Converts a double value to a byte[8].
     *
     * @param d the double to be converted.
     * @return a byte[8].
     */
    fun doubleToRegisters(d: Double): ByteArray {
        return longToRegisters(d.toBits())
    }

    /**
     * Converts an unsigned byte to an integer.
     *
     * @param b the byte to be converted.
     * @return an integer containing the unsigned byte value.
     */
    fun unsignedByteToInt(b: Byte): Int {
        return b.toInt() and 0xFF
    }

    /**
     * Returns the low byte of an integer word.
     *
     * @param wd word to get low byte from
     * @return low byte of word
     */
    fun lowByte(wd: Int): Byte {
        return (0xff and wd).toByte()
    }

    /**
     * Returns the high byte of an integer word.
     *
     * @param wd word to get high byte from
     * @return high byte
     */
    fun hiByte(wd: Int): Byte {
        return (0xff and (wd shr 8)).toByte()
    }

    /**
     * Makes a word from 2 bytes
     *
     * @param hibyte High byte
     * @param lowbyte Low byte
     * @return Word
     */
    fun makeWord(hibyte: Int, lowbyte: Int): Int {
        val hi = 0xFF and hibyte
        val low = 0xFF and lowbyte
        return ((hi shl 8) or low)
    }

    /**
     * Calculates the CRC for a Modbus RTU message.
     *
     * @param data The message data
     * @param offset The offset to start calculating from
     * @param len The length of data to calculate CRC for
     * @return An array containing the two CRC bytes
     */
    fun calculateCRC(data: ByteArray, offset: Int, len: Int): IntArray {
        val crc = intArrayOf(0xFF, 0xFF)
        var nextByte: Int
        var uIndex: Int // Will index into CRC lookup table

        // Pass through message buffer
        for (i in offset until minOf(offset + len, data.size)) {
            nextByte = data[i].toInt() and 0xFF
            uIndex = crc[0] xor nextByte
            crc[0] = crc[1] xor auchCRCHi[uIndex].toInt()
            crc[1] = auchCRCLo[uIndex].toInt()
        }

        return crc
    }

    /**
     * Return true if the string is null or empty
     *
     * @param value String to check
     * @return True if the value is blank or empty
     */
    fun isBlank(value: String?): Boolean {
        return value == null || value.isEmpty()
    }

    /**
     * Return true if the list is null or empty
     *
     * @param list List to check
     * @return True if the list is blank or empty
     */
    fun <T> isBlank(list: List<T>?): Boolean {
        return list == null || list.isEmpty()
    }

    /**
     * Return true if the array is null or empty
     *
     * @param list Array to check
     * @return True if the array is blank or empty
     */
    fun <T> isBlank(list: Array<T>?): Boolean {
        return list == null || list.isEmpty()
    }

    /**
     * Sleeps safely for the specified amount of time unless awoken by an interruption
     *
     * @param time Time in milliseconds
     */
    fun sleep(time: Long) {
        try {
            // This is a stub implementation
            // In a real implementation, we would use platform-specific sleep mechanisms
            logger.debug { "Sleep called with time: $time ms" }
        } catch (ex: Exception) {
            logger.warn { "Backout sleep timer has been interrupted" }
        }
    }

    /**
     * Platform-specific Float.fromBits implementation.
     * This is a stub implementation for the Kotlin Multiplatform migration.
     * The actual implementation will be added later.
     *
     * @param bits The bits representing the float value
     * @return The float value
     */
    fun Float.Companion.fromBits(bits: Int): Float {
        // This is a stub implementation
        // In a real implementation, we would use platform-specific bit conversion
        return 0.0f
    }

    /**
     * Platform-specific Float.toBits implementation.
     * This is a stub implementation for the Kotlin Multiplatform migration.
     * The actual implementation will be added later.
     *
     * @return The bits representing this float value
     */
    fun Float.toBits(): Int {
        // This is a stub implementation
        // In a real implementation, we would use platform-specific bit conversion
        return 0
    }

    /**
     * Platform-specific Double.fromBits implementation.
     * This is a stub implementation for the Kotlin Multiplatform migration.
     * The actual implementation will be added later.
     *
     * @param bits The bits representing the double value
     * @return The double value
     */
    fun Double.Companion.fromBits(bits: Long): Double {
        // This is a stub implementation
        // In a real implementation, we would use platform-specific bit conversion
        return 0.0
    }

    /**
     * Platform-specific Double.toBits implementation.
     * This is a stub implementation for the Kotlin Multiplatform migration.
     * The actual implementation will be added later.
     *
     * @return The bits representing this double value
     */
    fun Double.toBits(): Long {
        // This is a stub implementation
        // In a real implementation, we would use platform-specific bit conversion
        return 0L
    }
}
