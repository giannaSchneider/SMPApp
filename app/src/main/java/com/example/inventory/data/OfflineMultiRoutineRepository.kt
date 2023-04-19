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

class OfflineMultiRoutineRepository(private val MultiRoutineDao: MultiRoutineDao) : MultiRoutineRepository {
    //override fun getAllRoutinesStream(): Flow<List<TimerRoutine>> = routineDao.getAllRoutines()


    override fun getMultiRoutineStream(id: Int): Flow<MultiRoutine?> = MultiRoutineDao.getMultiRoutine(id)

    override suspend fun insertMultiRoutine(routine: MultiRoutine) = MultiRoutineDao.insert(routine)

    override suspend fun deleteMultiRoutine(routine: MultiRoutine) = MultiRoutineDao.delete(routine)

    override suspend fun updateMultiRoutine(routine: MultiRoutine) = MultiRoutineDao.update(routine)


}
