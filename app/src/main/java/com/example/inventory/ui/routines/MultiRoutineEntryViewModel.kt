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
import com.example.inventory.data.MultiRoutine
import com.example.inventory.data.MultiRoutineRepository

/**
 * View Model to validate and insert items in the Room database.
 */
class MultiRoutineEntryViewModel(private val multiRoutineRepository: MultiRoutineRepository) : ViewModel() {

    /**
     * Holds current item ui state
     */
    var multiRoutineUiState by mutableStateOf(MultiRoutineUiState())
        private set

    /**
     * Updates the [itemUiState] with the value provided in the argument. This method also triggers
     * a validation for input values.
     */
    fun updateUiState(multiRoutineDetails: MultiRoutineDetails) {
        multiRoutineUiState =
            MultiRoutineUiState(multiRoutineDetails = multiRoutineDetails, isEntryValid = validateInput(multiRoutineDetails))
    }

    /**
     * Inserts an [Item] in the Room database
     */
    suspend fun saveMultiRoutine() {
        if (validateInput()) {
            multiRoutineRepository.insertMultiRoutine(multiRoutineUiState.multiRoutineDetails.toMultiRoutine())
        }
    }

    private fun validateInput(uiState: MultiRoutineDetails = multiRoutineUiState.multiRoutineDetails): Boolean {
        return with(uiState) {
            name.isNotBlank() && /*time.isNotBlank() &&*/ status.isNotBlank()
        }
    }
}

/**
 * Represents Ui State for an Item.
 */
data class MultiRoutineUiState(
    val multiRoutineDetails: MultiRoutineDetails = MultiRoutineDetails(),
    val isEntryValid: Boolean = false
)

data class MultiRoutineDetails(
    val id: Int = 0,
    val name: String = "",
    //val time: String = "",
    val status: String = "",
    val id2: Int = 0,
    val name2: String = "",
    //val time2: String = "",
    val status2: String = ""
)

/**
 * Extension function to convert [ItemUiState] to [Item]. If the value of [ItemUiState.price] is
 * not a valid [Double], then the price will be set to 0.0. Similarly if the value of
 * [ItemUiState] is not a valid [Int], then the quantity will be set to 0
 */
fun MultiRoutineDetails.toMultiRoutine(): MultiRoutine = MultiRoutine(
    id = id,
    name = name,
    //time = time.toDoubleOrNull() ?: 0.0,
    status = status,//.toBooleanStrictOrNull()
    id2 = id2,
    name2 = name2,
    //time = time.toDoubleOrNull() ?: 0.0,
    status2 = status2//.toBooleanStrictOrNull()
)

/**
 * Extension function to convert [Item] to [ItemUiState]
 */
fun MultiRoutine.toMultiRoutineUiState(isEntryValid: Boolean = false): MultiRoutineUiState = MultiRoutineUiState(
    multiRoutineDetails = this.toMultiRoutineDetails(),
    isEntryValid = isEntryValid
)

/**
 * Extension function to convert [Item] to [ItemDetails]
 */
fun MultiRoutine.toMultiRoutineDetails(): MultiRoutineDetails = MultiRoutineDetails(
    id = id,
    name = name,
    //time = time.toString(),
    status = status,
    id2 = id2,
    name2 = name2,
    //time = time.toString(),
    status2 = status2
)

