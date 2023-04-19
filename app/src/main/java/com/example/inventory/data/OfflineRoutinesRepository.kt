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

class OfflineRoutinesRepository(private val routineDao: RoutineDao) : RoutinesRepository {
    override fun getAllTimerRoutinesStream(): Flow<List<TimerRoutine>> = routineDao.getAllTimerRoutines()

    //override fun getRoutineStream(id: Int): Flow<TimerRoutine?> = routineDao.getTimerRoutine(id)
    override fun getTimerRoutineStream(id: Int): Flow<TimerRoutine?> = routineDao.getTimerRoutine(id)

    override suspend fun insertTimerRoutine(routine: TimerRoutine) = routineDao.insert(routine)

    override suspend fun deleteTimerRoutine(routine: TimerRoutine) = routineDao.delete(routine)

    override suspend fun updateTimerRoutine(routine: TimerRoutine) = routineDao.update(routine)

    //override suspend fun updateRoutine(routine: TimerRoutine) = routineDao.update(routine)

}
