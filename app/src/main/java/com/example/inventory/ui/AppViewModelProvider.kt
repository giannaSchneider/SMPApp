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

package com.example.inventory.ui

import android.app.Application
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory
import androidx.lifecycle.createSavedStateHandle
import androidx.lifecycle.viewmodel.CreationExtras
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.inventory.InventoryApplication
import com.example.inventory.ui.home.HomeViewModel
import com.example.inventory.ui.item.ItemDetailsViewModel
import com.example.inventory.ui.item.ItemEditViewModel
import com.example.inventory.ui.item.ItemEntryViewModel
import com.example.inventory.ui.routines.*

/**
 * Provides Factory to create instance of ViewModel for the entire Inventory app
 */
object AppViewModelProvider {
    val Factory = viewModelFactory {
        // Initializer for ItemEditViewModel
        initializer {
            ItemEditViewModel(
                this.createSavedStateHandle(),
                inventoryApplication().container.itemsRepository
            )

        }
      // Initializer for ItemEntryViewModel
        initializer {
            ItemEntryViewModel(inventoryApplication().container.itemsRepository)
        }

        // Initializer for ItemDetailsViewModel
        initializer {
            ItemDetailsViewModel(
                this.createSavedStateHandle(),
                inventoryApplication().container.itemsRepository,
                inventoryApplication().container.routinesRepository,
                inventoryApplication().container.clockRoutineRepository,
                inventoryApplication().container.multiRoutineRepository,
                inventoryApplication().container.mixRoutineRepository
            )
        }

        // Initializer for HomeViewModel
        initializer {
            HomeViewModel(inventoryApplication().container.itemsRepository)
        }

        //////////////////////////Timer Routine/////////////////////////////////

        // Initializer for TimerRoutineEntryViewModel
        initializer {
            TimerRoutineEntryViewModel(inventoryApplication().container.routinesRepository,inventoryApplication().container.itemsRepository)
        }

        initializer {
            TimerRoutineEditViewModel(
                this.createSavedStateHandle(),
                inventoryApplication().container.routinesRepository
            )

        }
        initializer {
            TimerRoutineDetailsViewModel(
                this.createSavedStateHandle(),
                inventoryApplication().container.routinesRepository
            )
        }

        ///////////////////////////////////Clock Routine///////////////////////////
        initializer {
            ClockRoutineEntryViewModel(inventoryApplication().container.clockRoutineRepository, inventoryApplication().container.itemsRepository)
        }
        initializer {
            ClockRoutineEditViewModel(
                this.createSavedStateHandle(),
                inventoryApplication().container.clockRoutineRepository
            )

        }

        initializer {
            ClockRoutineDetailsViewModel(
                this.createSavedStateHandle(),
                inventoryApplication().container.clockRoutineRepository
            )
        }

        //////////////////////////Multi Routine/////////////////////////
        initializer {
            MultiRoutineEntryViewModel(inventoryApplication().container.multiRoutineRepository, inventoryApplication().container.itemsRepository)
        }
        initializer {
            MultiRoutineEditViewModel(
                this.createSavedStateHandle(),
                inventoryApplication().container.multiRoutineRepository
            )
        }
        initializer {
            MultiRoutineDetailsViewModel(
                this.createSavedStateHandle(),
                inventoryApplication().container.multiRoutineRepository
            )
        }

        /////////////////////////Mix Routine//////////////////////////////
        initializer {
            MixRoutineEntryViewModel(inventoryApplication().container.mixRoutineRepository, inventoryApplication().container.itemsRepository)
        }
        initializer {
            MixRoutineEditViewModel(
                this.createSavedStateHandle(),
                inventoryApplication().container.mixRoutineRepository
            )
        }
        initializer {
            MixRoutineDetailsViewModel(
                this.createSavedStateHandle(),
                inventoryApplication().container.mixRoutineRepository,
                inventoryApplication().container.itemsRepository
            )
        }


    }
}

/**
 * Extension function to queries for [Application] object and returns an instance of
 * [InventoryApplication].
 */
fun CreationExtras.inventoryApplication(): InventoryApplication =
    (this[AndroidViewModelFactory.APPLICATION_KEY] as InventoryApplication)
