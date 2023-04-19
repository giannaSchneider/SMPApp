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

package com.example.inventory.ui.routines

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.inventory.data.RoutinesRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

/**
 * ViewModel to retrieve, update and delete an timerRoutine from the [RoutinesRepository]'s data source.
 */
class TimerRoutineDetailsViewModel(
    savedStateHandle: SavedStateHandle,
    private val RoutinesRepository: RoutinesRepository,
) : ViewModel() {

    private val timerRoutineId: Int = checkNotNull(savedStateHandle[TimerRoutineDetailsDestination.timerRoutineIdArg])

    /**
     * Holds the timerRoutine details ui state. The data is retrieved from [TimerRoutinesRepository] and mapped to
     * the UI state.
     */
    val uiState: StateFlow<TimerRoutineDetailsUiState> =
        RoutinesRepository.getTimerRoutineStream(timerRoutineId)
            .filterNotNull()
            .map {
                TimerRoutineDetailsUiState(/*outOfStock = it.status = true,*/ timerRoutineDetails = it.toTimerRoutineDetails())
            }.stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(TIMEOUT_MILLIS),
                initialValue = TimerRoutineDetailsUiState()
            )

    /**
     * Reduces the timerRoutine status by one and update the [TimerRoutinesRepository]'s data source.
     */
    /*fun reduceQuantityByOne() {
        viewModelScope.launch {
            val currentTimerRoutine = uiState.value.timerRoutineDetails.toTimerRoutine()
            if (currentTimerRoutine.status = true) {
                RoutinesRepository.updateTimerRoutine(currentTimerRoutine.copy(status = currentTimerRoutine.status - 1))
            }
        }
    }*/

    /**
     * Deletes the timerRoutine from the [TimerRoutinesRepository]'s data source.
     */
    suspend fun deleteTimerRoutine() {
        RoutinesRepository.deleteTimerRoutine(uiState.value.timerRoutineDetails.toTimerRoutine())
    }

    companion object {
        private const val TIMEOUT_MILLIS = 5_000L
    }
}

/**
 * UI state for TimerRoutineDetailsScreen
 */
data class TimerRoutineDetailsUiState(
    //val outOfStock: Boolean = true,
    val timerRoutineDetails: TimerRoutineDetails = TimerRoutineDetails()
)
