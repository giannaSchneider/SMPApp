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

package com.example.inventory.ui.item

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.inventory.data.*
import com.example.inventory.ui.home.HomeUiState
import com.example.inventory.ui.home.HomeViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

/**
 * ViewModel to retrieve, update and delete an item from the [ItemsRepository]'s data source.
 */
class ItemDetailsViewModel(
    savedStateHandle: SavedStateHandle,
    private val itemsRepository: ItemsRepository,
    private val routinesRepository: RoutinesRepository,
    private val clockRoutineRepository: ClockRoutineRepository,
    private val multiRoutineRepository: MultiRoutineRepository,
    private val mixRoutineRepository: MixRoutineRepository



) : ViewModel() {

    private val itemId: Int = checkNotNull(savedStateHandle[ItemDetailsDestination.itemIdArg])

    /**
     * Holds the item details ui state. The data is retrieved from [ItemsRepository] and mapped to
     * the UI state.
     */
    val uiState: StateFlow<ItemDetailsUiState> =
        itemsRepository.getItemStream(itemId)
            .filterNotNull()
            .map {
                ItemDetailsUiState(outOfStock = it.quantity <= 0, itemDetails = it.toItemDetails())
            }.stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(TIMEOUT_MILLIS),
                initialValue = ItemDetailsUiState()
            )

    val itemUiState2: StateFlow<ItemUiState2> =
        routinesRepository.getAllTimerRoutinesStream().map { ItemUiState2(it) }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(ItemDetailsViewModel.TIMEOUT_MILLIS),
                initialValue = ItemUiState2()
            )
    val itemUiState3: StateFlow<ItemUiState3> =
        clockRoutineRepository.getAllClockRoutinesStream().map { ItemUiState3(it) }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(ItemDetailsViewModel.TIMEOUT_MILLIS),
                initialValue = ItemUiState3()
            )
    val itemUiState4: StateFlow<ItemUiState4> =
        multiRoutineRepository.getAllMultiRoutinesStream().map { ItemUiState4(it) }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(ItemDetailsViewModel.TIMEOUT_MILLIS),
                initialValue = ItemUiState4()
            )

    val itemUiState5: StateFlow<ItemUiState5> =
        mixRoutineRepository.getAllMixRoutinesStream().map { ItemUiState5(it) }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(ItemDetailsViewModel.TIMEOUT_MILLIS),
                initialValue = ItemUiState5()
            )
    /**
     * Reduces the item quantity by one and update the [ItemsRepository]'s data source.
     */
    fun reduceQuantityByOne() {
        viewModelScope.launch {
            val currentItem = uiState.value.itemDetails.toItem()
            if (currentItem.quantity > 0) {
                itemsRepository.updateItem(currentItem.copy(quantity = currentItem.quantity - 1))
            }
        }
    }

    /**
     * Deletes the item from the [ItemsRepository]'s data source.
     */
    suspend fun deleteItem() {
        itemsRepository.deleteItem(uiState.value.itemDetails.toItem())
    }

    companion object {
        private const val TIMEOUT_MILLIS = 5_000L
    }
}

/**
 * UI state for ItemDetailsScreen
 */
data class ItemDetailsUiState(
    val outOfStock: Boolean = true,
    val itemDetails: ItemDetails = ItemDetails(),

)
data class ItemUiState2(val timerRoutineList: List<TimerRoutine> = listOf())

data class ItemUiState3(val clockRoutineList: List<ClockRoutine> = listOf())

data class ItemUiState4(val multiRoutineList: List<MultiRoutine> = listOf())

data class ItemUiState5(val mixRoutineList: List<MixRoutine> = listOf())




