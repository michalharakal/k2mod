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
 * A cleanroom implementation of the Observer interface
 * for the Observable design pattern.
 *
 * This is a Kotlin Multiplatform version of the original j2mod Observer interface.
 */
interface Observer {
    /**
     * Updates the state of this Observer to be in
     * synch with an Observable instance.
     * The argument should usually be an indication of the
     * aspects that changed in the Observable.
     *
     * @param o   an Observable instance.
     * @param arg an arbitrary argument to be passed.
     */
    fun update(o: Observable, arg: Any?)
}