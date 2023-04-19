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

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.inventory.data.MixRoutineRepository
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

/**
 * ViewModel to retrieve and update an item from the [ItemsRepository]'s data source.
 */
class MixRoutineEditViewModel(
    savedStateHandle: SavedStateHandle,
    private val mixRoutineRepository: MixRoutineRepository
) : ViewModel() {

    /**
     * Holds current item ui state
     */
    var mixRoutineUiState by mutableStateOf(MixRoutineUiState())
        private set

    private val mixRoutineId: Int = checkNotNull(savedStateHandle[MixRoutineEditDestination.mixRoutineIdArg])

    init {
        viewModelScope.launch {
            mixRoutineUiState = mixRoutineRepository.getMixRoutineStream(mixRoutineId)
                .filterNotNull()
                .first()
                .toMixRoutineUiState(true)
        }
    }

    /**
     * Update the item in the [ItemsRepository]'s data source
     */
    suspend fun updateMixRoutine() {
        if (validateInput(mixRoutineUiState.mixRoutineDetails)) {
            mixRoutineRepository.updateMixRoutine(mixRoutineUiState.mixRoutineDetails.toMixRoutine())
        }
    }

    /**
     * Updates the [itemUiState] with the value provided in the argument. This method also triggers
     * a validation for input values.
     */
    fun updateUiState(mixRoutineDetails: MixRoutineDetails) {
        mixRoutineUiState =
            MixRoutineUiState(mixRoutineDetails = mixRoutineDetails, isEntryValid = validateInput(mixRoutineDetails))
    }

    private fun validateInput(uiState: MixRoutineDetails = mixRoutineUiState.mixRoutineDetails): Boolean {
        return with(uiState) {
            name.isNotBlank() && time.isNotBlank() //&& status.isNotBlank()
        }
    }
}
