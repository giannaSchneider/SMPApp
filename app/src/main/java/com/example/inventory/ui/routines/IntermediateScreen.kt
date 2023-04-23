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

import androidx.annotation.StringRes
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.inventory.InventoryTopAppBar
import com.example.inventory.R
import com.example.inventory.data.Item
import com.example.inventory.data.TimerRoutine
import com.example.inventory.ui.AppViewModelProvider

import com.example.inventory.ui.navigation.NavigationDestination
import com.example.inventory.ui.theme.InventoryTheme
import kotlinx.coroutines.launch
import java.text.NumberFormat

object IntermediateDestination : NavigationDestination {
    override val route = "intermediate"
    override val titleRes = R.string.intermediate_title
    const val itemIdArg = "itemId"
    val routeWithArgs = "$route/{$itemIdArg}"
}

@Composable
fun IntermediateScreen(
    navigateBack: () -> Unit,
    navigateToTimerRoutineEntry: () -> Unit,
    navigateToClockRoutineEntry: () -> Unit,
    navigateToMultiRoutineEntry: () -> Unit,
    navigateToMixRoutineEntry: () -> Unit,

    modifier: Modifier = Modifier,
) {

    val coroutineScope = rememberCoroutineScope()
    Scaffold(
        topBar = {
            InventoryTopAppBar(
                title = stringResource(IntermediateDestination.titleRes),
                canNavigateBack = true,
                navigateUp = navigateBack
            )
        },

    ) { innerPadding ->
        IntermediateBody(

            onAddTimerRoutine = {navigateToTimerRoutineEntry()},
            onAddClockRoutine = {navigateToClockRoutineEntry()},
            onAddMultiRoutine = {navigateToMultiRoutineEntry()},
            onAddMixRoutine = {navigateToMixRoutineEntry()},

            modifier = modifier.padding(innerPadding)
        )
    }
}

@Composable
private fun IntermediateBody(

    onAddTimerRoutine: () -> Unit,
    onAddClockRoutine: () -> Unit,
    onAddMultiRoutine: () -> Unit,
    onAddMixRoutine: () -> Unit,

    modifier: Modifier = Modifier
) {

    Column(
        modifier = modifier
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {

        //ItemInputForm(itemDetails = itemDetailsUiState.itemDetails, enabled = false)
        Button(
                onClick = onAddTimerRoutine,
                modifier = Modifier.fillMaxWidth(),
                enabled = true
            ) {
                Text(stringResource(R.string.add_timer_routine))
            }
        Button(
             onClick = onAddClockRoutine,
             modifier = Modifier.fillMaxWidth(),
             enabled = true
         ) {
             Text(stringResource(R.string.add_clock_routine))
         }
         Button(
             onClick = onAddMultiRoutine,
             modifier = Modifier.fillMaxWidth(),
             enabled = true
         ) {
             Text(stringResource(R.string.add_multi_routine))
         }
         Button(
             onClick = onAddMixRoutine,
             modifier = Modifier.fillMaxWidth(),
             enabled = true
         ) {
             Text(stringResource(R.string.add_mix_routine))
         }


    }
}


