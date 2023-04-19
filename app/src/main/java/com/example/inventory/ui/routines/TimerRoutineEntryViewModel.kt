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
import androidx.lifecycle.ViewModel
import com.example.inventory.data.Item
import com.example.inventory.data.TimerRoutine
import com.example.inventory.data.RoutinesRepository

/**
 * View Model to validate and insert items in the Room database.
 */
class TimerRoutineEntryViewModel(private val routinesRepository: RoutinesRepository) : ViewModel() {

    /**
     * Holds current item ui state
     */
    var timerRoutineUiState by mutableStateOf(TimerRoutineUiState())
        private set

    /**
     * Updates the [itemUiState] with the value provided in the argument. This method also triggers
     * a validation for input values.
     */
    fun updateUiState(timerRoutineDetails: TimerRoutineDetails) {
        timerRoutineUiState =
            TimerRoutineUiState(timerRoutineDetails = timerRoutineDetails, isEntryValid = validateInput(timerRoutineDetails))
    }

    /**
     * Inserts an [Item] in the Room database
     */
    suspend fun saveTimerRoutine() {
        if (validateInput()) {
            routinesRepository.insertTimerRoutine(timerRoutineUiState.timerRoutineDetails.toTimerRoutine())
        }
    }

    private fun validateInput(uiState: TimerRoutineDetails = timerRoutineUiState.timerRoutineDetails): Boolean {
        return with(uiState) {
            name.isNotBlank()  &&status.isNotBlank()
        }
    }
}

/**
 * Represents Ui State for an Item.
 */
data class TimerRoutineUiState(
    val timerRoutineDetails: TimerRoutineDetails = TimerRoutineDetails(),
    val isEntryValid: Boolean = false
)

data class TimerRoutineDetails(
    val id: Int = 0,
    val name: String = "",
    val startTime: String = "",
    val endTime: String = "",
    val duration: String = "",
    val status: String = ""
)

/**
 * Extension function to convert [ItemUiState] to [Item]. If the value of [ItemUiState.price] is
 * not a valid [Double], then the price will be set to 0.0. Similarly if the value of
 * [ItemUiState] is not a valid [Int], then the quantity will be set to 0
 */
fun TimerRoutineDetails.toTimerRoutine(): TimerRoutine = TimerRoutine(
    id = id,
    name = name,
    startTime = startTime,//.toDoubleOrNull() ?: 0.0,
    endTime = endTime,//.toDoubleOrNull() ?: 0.0,
    duration = duration,//.toDoubleOrNull() ?: 0.0,
    status = status//.toBooleanStrictOrNull()
)

/**
 * Extension function to convert [Item] to [ItemUiState]
 */
fun TimerRoutine.toTimerRoutineUiState(isEntryValid: Boolean = false): TimerRoutineUiState = TimerRoutineUiState(
    timerRoutineDetails = this.toTimerRoutineDetails(),
    isEntryValid = isEntryValid
)

/**
 * Extension function to convert [Item] to [ItemDetails]
 */
fun TimerRoutine.toTimerRoutineDetails(): TimerRoutineDetails = TimerRoutineDetails(
    id = id,
    name = name,
    startTime = startTime.toString(),
    endTime = endTime.toString(),
    status = status
)

