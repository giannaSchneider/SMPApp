///*
// * Copyright (C) 2022 The Android Open Source Project
// *
// * Licensed under the Apache License, Version 2.0 (the "License");
// * you may not use this file except in compliance with the License.
// * You may obtain a copy of the License at
// *
// *      http://www.apache.org/licenses/LICENSE-2.0
// *
// * Unless required by applicable law or agreed to in writing, software
// * distributed under the License is distributed on an "AS IS" BASIS,
// * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// * See the License for the specific language governing permissions and
// * limitations under the License.
// */
//
//package com.example.inventory.ui.routines
//
//import androidx.lifecycle.SavedStateHandle
//import androidx.lifecycle.ViewModel
//import androidx.lifecycle.viewModelScope
//import com.example.inventory.data.ClockRoutineRepository
//import kotlinx.coroutines.flow.SharingStarted
//import kotlinx.coroutines.flow.StateFlow
//import kotlinx.coroutines.flow.filterNotNull
//import kotlinx.coroutines.flow.map
//import kotlinx.coroutines.flow.stateIn
//import kotlinx.coroutines.launch
//
///**
// * ViewModel to retrieve, update and delete an clockRoutine from the [RoutinesRepository]'s data source.
// */
//class ClockRoutineDetailsViewModel(
//    savedStateHandle: SavedStateHandle,
//    private val RoutinesRepository: ClockRoutineRepository,
//) : ViewModel() {
//
//    private val clockRoutineId: Int = checkNotNull(savedStateHandle[ClockRoutineDetailsDestination.clockRoutineIdArg])
//
//    /**
//     * Holds the clockRoutine details ui state. The data is retrieved from [ClockRoutinesRepository] and mapped to
//     * the UI state.
//     */
//    val uiState: StateFlow<ClockRoutineDetailsUiState> =
//        RoutinesRepository.getClockRoutineStream(clockRoutineId)
//            .filterNotNull()
//            .map {
//                ClockRoutineDetailsUiState(/*outOfStock = it.status = true,*/ clockRoutineDetails = it.toClockRoutineDetails())
//            }.stateIn(
//                scope = viewModelScope,
//                started = SharingStarted.WhileSubscribed(TIMEOUT_MILLIS),
//                initialValue = ClockRoutineDetailsUiState()
//            )
//
//    /**
//     * Reduces the clockRoutine status by one and update the [ClockRoutinesRepository]'s data source.
//     */
//    /*fun reduceQuantityByOne() {
//        viewModelScope.launch {
//            val currentClockRoutine = uiState.value.clockRoutineDetails.toClockRoutine()
//            if (currentClockRoutine.status = true) {
//                RoutinesRepository.updateClockRoutine(currentClockRoutine.copy(status = currentClockRoutine.status - 1))
//            }
//        }
//    }*/
//
//    /**
//     * Deletes the clockRoutine from the [ClockRoutinesRepository]'s data source.
//     */
//    suspend fun deleteClockRoutine() {
//        RoutinesRepository.deleteClockRoutine(uiState.value.clockRoutineDetails.toClockRoutine())
//    }
//
//    companion object {
//        private const val TIMEOUT_MILLIS = 5_000L
//    }
//}
//
///**
// * UI state for ClockRoutineDetailsScreen
// */
//data class ClockRoutineDetailsUiState(
//    //val outOfStock: Boolean = true,
//    val clockRoutineDetails: ClockRoutineDetails = ClockRoutineDetails()
//)
