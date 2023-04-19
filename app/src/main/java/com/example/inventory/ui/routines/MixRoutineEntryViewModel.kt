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
import com.example.inventory.data.MixRoutine
import com.example.inventory.data.MixRoutineRepository
import com.example.inventory.data.ItemsRepository
import kotlinx.coroutines.flow.Flow

/**
 * View Model to validate and insert items in the Room database.
 */

class MixRoutineEntryViewModel(private val mixRoutineRepository: MixRoutineRepository, val itemsRepository: ItemsRepository) : ViewModel() {

    /**
     * Holds current item ui state
     */
    var mixRoutineUiState by mutableStateOf(MixRoutineUiState())
        private set

    /**
     * Updates the [itemUiState] with the value provided in the argument. This method also triggers
     * a validation for input values.
     */
    fun updateUiState(mixRoutineDetails: MixRoutineDetails) {
        mixRoutineUiState =
            MixRoutineUiState(mixRoutineDetails = mixRoutineDetails, isEntryValid = validateInput(mixRoutineDetails))
    }

    suspend fun getAllItemsStream(): Flow<List<Item>> {
        return itemsRepository.getAllItemsStream()
    }

    /**
     * Inserts an [Item] in the Room database
     */
    suspend fun saveItem(item: Item) {
        itemsRepository.insertItem(item)
    }

    /**
     * Inserts an [Item] in the Room database
     */
    suspend fun saveMixRoutine() {
        if (validateInput()) {
            mixRoutineRepository.insertMixRoutine(mixRoutineUiState.mixRoutineDetails.toMixRoutine())
        }
    }

//    suspend fun saveMixRoutine() {
//        if (validateInput()) {
//            val item1 = Item(name = mixRoutineUiState.mixRoutineDetails.name, status = mixRoutineUiState.mixRoutineDetails.status)
//            val item2 = Item(name = mixRoutineUiState.mixRoutineDetails.name2, status = mixRoutineUiState.mixRoutineDetails.status2)
//            itemsRepository.insertItem(item1)
//            itemsRepository.insertItem(item2)
//            val mixRoutine = mixRoutineUiState.mixRoutineDetails.toMixRoutine()
//            mixRoutine.item1Id = item1.id
//            mixRoutine.item2Id = item2.id
//            mixRoutineRepository.insertMixRoutine(mixRoutine)
//        }
//    }

    private fun validateInput(uiState: MixRoutineDetails = mixRoutineUiState.mixRoutineDetails): Boolean {
        return with(uiState) {
            name.isNotBlank() && /*time.isNotBlank() &&*/ status.isNotBlank()
        }
    }
}

/**
 * Represents Ui State for an Item.
 */
data class MixRoutineUiState(
    val mixRoutineDetails: MixRoutineDetails = MixRoutineDetails(),
    val isEntryValid: Boolean = false
)

data class MixRoutineDetails(
    val id: Int = 0,
    val name: String = "",
    val time: String = "",
    val status: String = "",
    val id2: Int = 0,
    val name2: String = "",
    //val time2: String = "",
    val status2: String = "",
    val endTime: String = "",
    val startTime: String = "",
    val duration: String = ""
)

/**
 * Extension function to convert [ItemUiState] to [Item]. If the value of [ItemUiState.price] is
 * not a valid [Double], then the price will be set to 0.0. Similarly if the value of
 * [ItemUiState] is not a valid [Int], then the quantity will be set to 0
 */
fun MixRoutineDetails.toMixRoutine(): MixRoutine = MixRoutine(
    id = id,
    name = name,
    time = time.toDoubleOrNull() ?: 0.0,
    status = status,//.toBooleanStrictOrNull()
    id2 = id2,
    name2 = name2,
    //time = time.toDoubleOrNull() ?: 0.0,
    status2 = status2,//.toBooleanStrictOrNull()
    endTime = endTime,
    startTime = startTime,
    duration = duration

)

/**
 * Extension function to convert [Item] to [ItemUiState]
 */
fun MixRoutine.toMixRoutineUiState(isEntryValid: Boolean = false): MixRoutineUiState = MixRoutineUiState(
    mixRoutineDetails = this.toMixRoutineDetails(),
    isEntryValid = isEntryValid
)

/**
 * Extension function to convert [Item] to [ItemDetails]
 */
fun MixRoutine.toMixRoutineDetails(): MixRoutineDetails = MixRoutineDetails(
    id = id,
    name = name,
    time = time.toString(),
    status = status,
    id2 = id2,
    name2 = name2,
    //time = time.toString(),
    status2 = status2
)
