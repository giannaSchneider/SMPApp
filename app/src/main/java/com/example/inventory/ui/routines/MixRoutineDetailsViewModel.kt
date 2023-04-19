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
//import com.example.inventory.data.MixRoutineRepository
//import kotlinx.coroutines.flow.SharingStarted
//import kotlinx.coroutines.flow.StateFlow
//import kotlinx.coroutines.flow.filterNotNull
//import kotlinx.coroutines.flow.map
//import kotlinx.coroutines.flow.stateIn
//import kotlinx.coroutines.launch
//
///**
// * ViewModel to retrieve, update and delete an mixRoutine from the [RoutinesRepository]'s data source.
// */
//class MixRoutineDetailsViewModel(
//    savedStateHandle: SavedStateHandle,
//    private val MixRoutineRepository: MixRoutineRepository,
//) : ViewModel() {
//
//    private val mixRoutineId: Int = checkNotNull(savedStateHandle[MixRoutineDetailsDestination.mixRoutineIdArg])
//
//    /**
//     * Holds the mixRoutine details ui state. The data is retrieved from [MixRoutinesRepository] and mapped to
//     * the UI state.
//     */
//    val uiState: StateFlow<MixRoutineDetailsUiState> =
//        MixRoutineRepository.getMixRoutineStream(mixRoutineId)
//            .filterNotNull()
//            .map {
//                MixRoutineDetailsUiState(/*outOfStock = it.status = true,*/ mixRoutineDetails = it.toMixRoutineDetails())
//            }.stateIn(
//                scope = viewModelScope,
//                started = SharingStarted.WhileSubscribed(TIMEOUT_MILLIS),
//                initialValue = MixRoutineDetailsUiState()
//            )
//
//    /**
//     * Reduces the mixRoutine status by one and update the [MixRoutinesRepository]'s data source.
//     */
//    /*fun reduceQuantityByOne() {
//        viewModelScope.launch {
//            val currentMixRoutine = uiState.value.mixRoutineDetails.toMixRoutine()
//            if (currentMixRoutine.status = true) {
//                RoutinesRepository.updateMixRoutine(currentMixRoutine.copy(status = currentMixRoutine.status - 1))
//            }
//        }
//    }*/
//
//    /**
//     * Deletes the mixRoutine from the [MixRoutinesRepository]'s data source.
//     */
//    suspend fun deleteMixRoutine() {
//        MixRoutineRepository.deleteMixRoutine(uiState.value.mixRoutineDetails.toMixRoutine())
//    }
//
//    companion object {
//        private const val TIMEOUT_MILLIS = 5_000L
//    }
//}
//
///**
// * UI state for MixRoutineDetailsScreen
// */
//data class MixRoutineDetailsUiState(
//    //val outOfStock: Boolean = true,
//    val mixRoutineDetails: MixRoutineDetails = MixRoutineDetails()
//)
