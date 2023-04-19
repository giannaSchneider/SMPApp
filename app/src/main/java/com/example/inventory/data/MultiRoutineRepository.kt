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

import kotlinx.coroutines.flow.Flow

/////////////////////////delete

/**
 * Repository that provides insert, update, delete, and retrieve of [Routine] from a given data source.
 */
interface MultiRoutineRepository {
    /**
     * Retrieve all the routine from the the given data source.
     */
    //fun getAllRoutinesStream(): Flow<List<TimerRoutine>>

    /**
     * Retrieve an routine from the given data source that matches with the [id].
     */
    /**
     * Retrieve an routine from the given data source that matches with the [id].
     */
    fun getMultiRoutineStream(id: Int): Flow<MultiRoutine?>


    /**
     * Insert Routine in the data source
     */

    suspend fun insertMultiRoutine(routine: MultiRoutine)


    /**
     * Delete Routine from the data source
     */

    suspend fun deleteMultiRoutine(routine: MultiRoutine)


    /**
     * Update routine in the data source
     */

    suspend fun updateMultiRoutine(routine: MultiRoutine)


    /**
     * Update routine in the data source
     */
   // suspend fun updateRoutine(routine: TimerRoutine)
}
