/*
 * Copyright (C) 2022 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example.inventory.data

import android.content.Context

/**
 * App container for Dependency injection.
 */
interface AppContainer {
    val itemsRepository: ItemsRepository
    val routinesRepository: RoutinesRepository
    val clockRoutineRepository: ClockRoutineRepository
    val multiRoutineRepository: MultiRoutineRepository
    val mixRoutineRepository: MixRoutineRepository


}

/**
 * [AppContainer] implementation that provides instance of [OfflineItemsRepository]
 */
class AppDataContainer(private val context: Context) : AppContainer {
    /**
     * Implementation for [ItemsRepository]
     */
    override val itemsRepository: ItemsRepository by lazy {
        OfflineItemsRepository(InventoryDatabase.getDatabase(context).itemDao())
    }
    override val routinesRepository: RoutinesRepository by lazy {
        OfflineRoutinesRepository(RoutineDatabase.getDatabase(context).routineDao())
    }
    override val clockRoutineRepository: ClockRoutineRepository by lazy {
        OfflineClockRoutineRepository(ClockRoutineDatabase.getDatabase(context).ClockRoutineDao())
    }
    override val multiRoutineRepository: MultiRoutineRepository by lazy {
        OfflineMultiRoutineRepository(MultiRoutineDatabase.getDatabase(context).MultiRoutineDao())
    }
    override val mixRoutineRepository: MixRoutineRepository by lazy {
        OfflineMixRoutineRepository(MixRoutineDatabase.getDatabase(context).MixRoutineDao())
    }
}