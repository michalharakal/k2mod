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
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.ClosedReceiveChannelException

/**
 * Class implementing a simple thread pool using Kotlin coroutines.
 *
 * This is a Kotlin Multiplatform version of the original j2mod ThreadPool class,
 * reimplemented using Kotlin coroutines and channels for cross-platform compatibility.
 */
class ThreadPool(private val size: Int) {
    private val logger = Logger.getLogger("ThreadPool")
    private val taskChannel = Channel<Runnable>(Channel.UNLIMITED)
    private val coroutineScope = CoroutineScope(Dispatchers.Default + SupervisorJob())
    private val workers = mutableListOf<Job>()
    private var running = false

    /**
     * Execute the [Runnable] instance through a coroutine in this [ThreadPool].
     *
     * @param task the [Runnable] to be executed.
     */
    fun execute(task: Runnable) {
        if (running) {
            try {
                taskChannel.trySend(task)
            } catch (e: Exception) {
                logger.error { "Failed to submit task: ${e.message}" }
            }
        }
    }

    /**
     * Initializes the pool, creating and starting the worker coroutines.
     * 
     * @param name Name prefix to give each worker
     */
    fun initPool(name: String) {
        running = true
        
        repeat(size) { index ->
            val worker = coroutineScope.launch {
                val workerName = "$name Worker-$index"
                logger.debug { "Starting $workerName" }
                
                try {
                    while (isActive && running) {
                        try {
                            val task = taskChannel.receive()
                            logger.debug { "Running task in $workerName" }
                            task.run()
                        } catch (e: ClosedReceiveChannelException) {
                            // Channel was closed, exit the loop
                            break
                        } catch (e: Exception) {
                            if (running) {
                                logger.error { "Error executing task in $workerName: ${e.message}" }
                            }
                        }
                    }
                } finally {
                    logger.debug { "Shutting down $workerName" }
                }
            }
            workers.add(worker)
        }
    }

    /**
     * Shutdown the pool of coroutines
     */
    fun close() {
        if (running) {
            running = false
            taskChannel.close()
            
            // Cancel all workers
            workers.forEach { it.cancel() }
            workers.clear()
            
            // Cancel the scope
            coroutineScope.cancel()
        }
    }
}