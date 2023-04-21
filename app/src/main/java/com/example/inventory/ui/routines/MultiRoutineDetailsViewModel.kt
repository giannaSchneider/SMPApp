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
import com.example.inventory.data.MultiRoutineRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

/**
 * ViewModel to retrieve, update and delete an multiRoutine from the [RoutinesRepository]'s data source.
 */
class MultiRoutineDetailsViewModel(
    savedStateHandle: SavedStateHandle,
    private val MultiRoutineRepository: MultiRoutineRepository,
) : ViewModel() {

    private val multiRoutineId: Int = checkNotNull(savedStateHandle[MultiRoutineDetailsDestination.multiRoutineIdArg])

    /**
     * Holds the multiRoutine details ui state. The data is retrieved from [MultiRoutinesRepository] and mapped to
     * the UI state.
     */
    val uiState: StateFlow<MultiRoutineDetailsUiState> =
        MultiRoutineRepository.getMultiRoutineStream(multiRoutineId)
            .filterNotNull()
            .map {
                MultiRoutineDetailsUiState(/*outOfStock = it.status = true,*/ multiRoutineDetails = it.toMultiRoutineDetails())
            }.stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(TIMEOUT_MILLIS),
                initialValue = MultiRoutineDetailsUiState()
            )

    /**
     * Reduces the multiRoutine status by one and update the [MultiRoutinesRepository]'s data source.
     */
    /*fun reduceQuantityByOne() {
        viewModelScope.launch {
            val currentMultiRoutine = uiState.value.multiRoutineDetails.toMultiRoutine()
            if (currentMultiRoutine.status = true) {
                RoutinesRepository.updateMultiRoutine(currentMultiRoutine.copy(status = currentMultiRoutine.status - 1))
            }
        }
    }*/

    /**
     * Deletes the multiRoutine from the [MultiRoutinesRepository]'s data source.
     */
    suspend fun deleteMultiRoutine() {
        MultiRoutineRepository.deleteMultiRoutine(uiState.value.multiRoutineDetails.toMultiRoutine())
    }

    companion object {
        private const val TIMEOUT_MILLIS = 5_000L
    }
}

/**
 * UI state for MultiRoutineDetailsScreen
 */
data class MultiRoutineDetailsUiState(
    //val outOfStock: Boolean = true,
    val multiRoutineDetails: MultiRoutineDetails = MultiRoutineDetails()
)
