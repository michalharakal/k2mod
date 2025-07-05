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
 * A cleanroom implementation of the Observable pattern.
 *
 * This is a Kotlin Multiplatform version of the original j2mod Observable class.
 * 
 * Note: In a production environment, proper synchronization mechanisms should be
 * implemented for each target platform. This implementation does not include
 * synchronization for simplicity.
 */
class Observable {
    // Using a thread-safe list would be ideal in a production environment
    private val observers = mutableListOf<Observer>()

    /**
     * Constructs a new Observable instance.
     */
    constructor()

    /**
     * Returns the number of observers.
     *
     * @return the number of observers.
     */
    fun getObserverCount(): Int {
        return observers.size
    }

    /**
     * Adds an observer instance if it is not already in the set of observers
     * for this Observable.
     *
     * @param o an observer instance to be added.
     */
    fun addObserver(o: Observer) {
        if (!observers.contains(o)) {
            observers.add(o)
        }
    }

    /**
     * Removes an observer instance from the set of observers of this
     * Observable.
     *
     * @param o an observer instance to be removed.
     */
    fun removeObserver(o: Observer) {
        observers.remove(o)
    }

    /**
     * Removes all observer instances from the set of observers of this
     * Observable.
     */
    fun removeObservers() {
        observers.clear()
    }

    /**
     * Notifies all observer instances in the set of observers of this
     * Observable.
     *
     * @param arg an arbitrary argument to be passed.
     */
    fun notifyObservers(arg: Any?) {
        // Create a copy of the observers list to avoid concurrent modification issues
        val observersCopy = observers.toList()
        for (observer in observersCopy) {
            observer.update(this, arg)
        }
    }
}
