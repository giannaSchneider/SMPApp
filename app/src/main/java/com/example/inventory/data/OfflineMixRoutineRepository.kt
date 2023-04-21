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

class OfflineMixRoutineRepository(private val MixRoutineDao: MixRoutineDao) : MixRoutineRepository {
    override fun getAllMixRoutinesStream(): Flow<List<MixRoutine>> = MixRoutineDao.getAllMixRoutines()


    override fun getMixRoutineStream(id: Int): Flow<MixRoutine?> = MixRoutineDao.getMixRoutine(id)

    override suspend fun insertMixRoutine(routine: MixRoutine) = MixRoutineDao.insert(routine)

    override suspend fun deleteMixRoutine(routine: MixRoutine) = MixRoutineDao.delete(routine)

    override suspend fun updateMixRoutine(routine: MixRoutine) = MixRoutineDao.update(routine)


}
