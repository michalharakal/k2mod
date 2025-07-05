package com.ghgande.kmod.common.logging

import mu.KLogger
import mu.KotlinLogging

/**
 * A cross-platform logging interface for the kmod library.
 * This class wraps kotlin-logging to provide a consistent logging API across all platforms.
 */
object Logger {
    /**
     * Get a logger for the specified class.
     *
     * @param clazz The class to get a logger for
     * @return A logger instance
     */
    inline fun <reified T : Any> getLogger(): KLogger {
        return KotlinLogging.logger(T::class.simpleName ?: "Unknown")
    }

    /**
     * Get a logger with the specified name.
     *
     * @param name The name of the logger
     * @return A logger instance
     */
    fun getLogger(name: String): KLogger {
        return KotlinLogging.logger(name)
    }
}