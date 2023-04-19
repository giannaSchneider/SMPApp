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
import com.example.inventory.data.MultiRoutineRepository
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

/**
 * ViewModel to retrieve and update an item from the [ItemsRepository]'s data source.
 */
class MultiRoutineEditViewModel(
    savedStateHandle: SavedStateHandle,
    private val multiRoutineRepository: MultiRoutineRepository
) : ViewModel() {

    /**
     * Holds current item ui state
     */
    var multiRoutineUiState by mutableStateOf(MultiRoutineUiState())
        private set

    private val multiRoutineId: Int = checkNotNull(savedStateHandle[MultiRoutineEditDestination.multiRoutineIdArg])

    init {
        viewModelScope.launch {
            multiRoutineUiState = multiRoutineRepository.getMultiRoutineStream(multiRoutineId)
                .filterNotNull()
                .first()
                .toMultiRoutineUiState(true)
        }
    }

    /**
     * Update the item in the [ItemsRepository]'s data source
     */
    suspend fun updateMultiRoutine() {
        if (validateInput(multiRoutineUiState.multiRoutineDetails)) {
            multiRoutineRepository.updateMultiRoutine(multiRoutineUiState.multiRoutineDetails.toMultiRoutine())
        }
    }

    /**
     * Updates the [itemUiState] with the value provided in the argument. This method also triggers
     * a validation for input values.
     */
    fun updateUiState(multiRoutineDetails: MultiRoutineDetails) {
        multiRoutineUiState =
            MultiRoutineUiState(multiRoutineDetails = multiRoutineDetails, isEntryValid = validateInput(multiRoutineDetails))
    }

    private fun validateInput(uiState: MultiRoutineDetails = multiRoutineUiState.multiRoutineDetails): Boolean {
        return with(uiState) {
            name.isNotBlank() //&& starTime.isNotBlank() //&& status.isNotBlank()
        }
    }
}
