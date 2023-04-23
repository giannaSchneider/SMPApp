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
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.inventory.InventoryTopAppBar
import com.example.inventory.R
import com.example.inventory.data.ClockRoutine
import com.example.inventory.data.MixRoutine
import com.example.inventory.data.MultiRoutine
import com.example.inventory.data.TimerRoutine
import com.example.inventory.ui.AppViewModelProvider

import com.example.inventory.ui.navigation.NavigationDestination
import kotlinx.coroutines.launch

object ItemDetailsDestination : NavigationDestination {
    override val route = "item_details"
    override val titleRes = R.string.item_detail_title
    const val itemIdArg = "itemId"
    val routeWithArgs = "$route/{$itemIdArg}"
}

@Composable
fun ItemDetailsScreen(
    navigateToEditItem: (Int) -> Unit,
    navigateBack: () -> Unit,
    navigateToIntermediateScreen: () -> Unit,

    navigateToTimerRoutineUpdate: (Int) -> Unit,
    navigateToClockRoutineUpdate: (Int) -> Unit,
    navigateToMultiRoutineUpdate: (Int) -> Unit,
    navigateToMixRoutineUpdate: (Int) -> Unit,

    modifier: Modifier = Modifier,
    viewModel: ItemDetailsViewModel = viewModel(factory = AppViewModelProvider.Factory)
) {
    val uiState = viewModel.uiState.collectAsState()
    val itemUiState2 by viewModel.itemUiState2.collectAsState()
    val itemUiState3 by viewModel.itemUiState3.collectAsState()
    val itemUiState4 by viewModel.itemUiState4.collectAsState()
    val itemUiState5 by viewModel.itemUiState5.collectAsState()



    val coroutineScope = rememberCoroutineScope()
    Scaffold(
        topBar = {
            InventoryTopAppBar(
                title = stringResource(ItemDetailsDestination.titleRes),
                canNavigateBack = true,
                navigateUp = navigateBack
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { navigateToEditItem(uiState.value.itemDetails.id) },
                modifier = Modifier.navigationBarsPadding()
            ) {
                Icon(
                    imageVector = Icons.Default.Edit,
                    contentDescription = stringResource(R.string.edit_item_title),
                    tint = MaterialTheme.colors.onPrimary
                )
            }
        },
    ) { innerPadding ->
        ItemDetailsBody(
            itemDetailsUiState = uiState.value,
            onSellItem = { viewModel.reduceQuantityByOne() },
            onAddARoutine = navigateToIntermediateScreen,

            timerRoutineList = itemUiState2.timerRoutineList,
            onTimerRoutineClick = navigateToTimerRoutineUpdate,
            clockRoutineList = itemUiState3.clockRoutineList,
            onClockRoutineClick = navigateToClockRoutineUpdate,
            multiRoutineList = itemUiState4.multiRoutineList,
            onMultiRoutineClick = navigateToMultiRoutineUpdate,
            mixRoutineList = itemUiState5.mixRoutineList,
            onMixRoutineClick = navigateToMixRoutineUpdate,


            onDelete = {

                coroutineScope.launch {
                    viewModel.deleteItem()
                    navigateBack()
                }
            },
            modifier = modifier.padding(innerPadding)
        )
    }
}

@Composable
private fun ItemDetailsBody(
    itemDetailsUiState: ItemDetailsUiState,
    onSellItem: () -> Unit,
    onAddARoutine: () -> Unit,


    timerRoutineList: List<TimerRoutine>,
    onTimerRoutineClick: (Int) -> Unit,

    clockRoutineList: List<ClockRoutine>,
    onClockRoutineClick: (Int) -> Unit,

    multiRoutineList: List<MultiRoutine>,
    onMultiRoutineClick: (Int) -> Unit,

    mixRoutineList: List<MixRoutine>,
    onMixRoutineClick: (Int) -> Unit,

    onDelete: () -> Unit,
    modifier: Modifier = Modifier,


) {
        Column(
            modifier = modifier
                .padding(16.dp),
                //.verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            var deleteConfirmationRequired by rememberSaveable { mutableStateOf(false) }
            var routineAlreadyMade: Boolean = false

            ItemInputForm(itemDetails = itemDetailsUiState.itemDetails, enabled = false)
            Button(
                onClick = onSellItem,
                modifier = Modifier.fillMaxWidth(),
                enabled = !itemDetailsUiState.outOfStock
            ) {
                Text(stringResource(R.string.sell))
            }

            OutlinedButton(
                onClick = { deleteConfirmationRequired = true },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(stringResource(R.string.delete))
            }
            if (deleteConfirmationRequired) {
                DeleteConfirmationDialog(
                    onDeleteConfirm = {
                        deleteConfirmationRequired = false
                        onDelete()
                    },
                    onDeleteCancel = { deleteConfirmationRequired = false }
                )
            }
            val filteredList = timerRoutineList.filter { timerRoutine ->
                itemDetailsUiState.itemDetails.name == timerRoutine.deviceId
            }
            val filteredList2 = clockRoutineList.filter { clockRoutine ->
                itemDetailsUiState.itemDetails.name == clockRoutine.deviceId
            }
            val filteredList3 = multiRoutineList.filter { multiRoutine ->
                itemDetailsUiState.itemDetails.name == multiRoutine.deviceId
            }
            val filteredList4 = mixRoutineList.filter { mixRoutine ->
                itemDetailsUiState.itemDetails.name == mixRoutine.deviceId
            }

            if(filteredList.isEmpty() && filteredList2.isEmpty() && filteredList3.isEmpty() && filteredList4.isEmpty()) {
                Button(
                    onClick = onAddARoutine,
                    modifier = Modifier.fillMaxWidth(),
                    enabled = true
                ) {
                    Text(stringResource(R.string.add_a_routine))
                }
            }
            else {
                Button(
                    onClick = onAddARoutine,
                    modifier = Modifier.fillMaxWidth(),
                    enabled = false
                ) {
                    Text(stringResource(R.string.add_a_routine))
                }

            }
            Divider()

            TimerRoutineListHeader()
            Divider()

            if (timerRoutineList.isEmpty()) {

            } else {
                TimerRoutineList(
                    timerRoutineList = timerRoutineList,
                    onTimerRoutineClick = { onTimerRoutineClick(it.id) },
                    itemDetailsUiState = itemDetailsUiState,

                )
            }
            if (clockRoutineList.isEmpty()) {
                Text(
                    text = ("No Clock Routines"),
                    style = MaterialTheme.typography.subtitle2
                )
            } else {
                ClockRoutineList(
                    clockRoutineList = clockRoutineList,
                    onClockRoutineClick = { onClockRoutineClick(it.id) },
                    itemDetailsUiState = itemDetailsUiState)
            }
            if (multiRoutineList.isEmpty()) {
                Text(
                    text = ("No Conditional Routines"),
                    style = MaterialTheme.typography.subtitle2
                )
            } else {
                MultiRoutineList(
                    multiRoutineList = multiRoutineList,
                    onMultiRoutineClick = { onMultiRoutineClick(it.id) },
                    itemDetailsUiState = itemDetailsUiState)
            }
            if (mixRoutineList.isEmpty()) {
                Text(
                    text = ("No Mix Routines"),
                    style = MaterialTheme.typography.subtitle2
                )
            } else {
                MixRoutineList(
                    mixRoutineList = mixRoutineList,
                    onMixRoutineClick = { onMixRoutineClick(it.id) },
                    itemDetailsUiState = itemDetailsUiState)
            }
        }

}


@Composable
private fun TimerRoutineList(
    timerRoutineList: List<TimerRoutine>,
    onTimerRoutineClick: (TimerRoutine) -> Unit,
    modifier: Modifier = Modifier,
    itemDetailsUiState: ItemDetailsUiState,
) {
    val filteredList = timerRoutineList.filter { timerRoutine ->
        itemDetailsUiState.itemDetails.name == timerRoutine.deviceId
    }

    if(filteredList.isNotEmpty()) {
        //var routineAlreadyMade: Boolean = true
        LazyColumn(modifier = modifier, verticalArrangement = Arrangement.spacedBy(8.dp)) {
            items(items = timerRoutineList, key = { it.id }) { timerRoutine ->
                TimerRoutine(timerRoutine = timerRoutine, onTimerRoutineClick = onTimerRoutineClick)
                Divider()
            }
        }
    }
}

@Composable
private fun TimerRoutineListHeader(modifier: Modifier = Modifier) {
    Row(modifier = modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
        headerList.forEach {
            Text(
                text = stringResource(it.headerStringId),
                modifier = Modifier.weight(it.weight),
                style = MaterialTheme.typography.h6
            )
        }
    }
}

@Composable
private fun TimerRoutine(
    timerRoutine: TimerRoutine,
    onTimerRoutineClick: (TimerRoutine) -> Unit,
    modifier: Modifier = Modifier
) {
    Row(modifier = modifier
        .fillMaxWidth()
        .clickable { onTimerRoutineClick(timerRoutine) }
        .padding(vertical = 16.dp)
    ) {
        Text(
            text = timerRoutine.name,
            modifier = Modifier.weight(1.5f),
            fontWeight = FontWeight.Bold
        )
        Text(
            text = timerRoutine.duration,
            modifier = Modifier.weight(1.0f)
        )
        Text(text = timerRoutine.status, modifier = Modifier.weight(1.0f))
    }
}

private data class TimerRoutineHeader(@StringRes val headerStringId: Int, val weight: Float)

private val headerList = listOf(
    TimerRoutineHeader(headerStringId = R.string.item, weight = 1.5f),
    TimerRoutineHeader(headerStringId = R.string.price, weight = 1.0f),
    TimerRoutineHeader(headerStringId = R.string.quantity_in_stock, weight = 1.0f)
)

@Composable
private fun DeleteConfirmationDialog(
    onDeleteConfirm: () -> Unit,
    onDeleteCancel: () -> Unit,
    modifier: Modifier = Modifier
) {
    AlertDialog(
        onDismissRequest = { /* Do nothing */ },
        title = { Text(stringResource(R.string.attention)) },
        text = { Text(stringResource(R.string.delete_question)) },
        modifier = modifier.padding(16.dp),
        dismissButton = {
            TextButton(onClick = onDeleteCancel) {
                Text(text = stringResource(R.string.no))
            }
        },
        confirmButton = {
            TextButton(onClick = onDeleteConfirm) {
                Text(text = stringResource(R.string.yes))
            }
        },

    )
}

@Composable
private fun ClockRoutineList(
    clockRoutineList: List<ClockRoutine>,
    onClockRoutineClick: (ClockRoutine) -> Unit,
    modifier: Modifier = Modifier,
    itemDetailsUiState: ItemDetailsUiState
) {

    val filteredList = clockRoutineList.filter { clockRoutine ->
        itemDetailsUiState.itemDetails.name == clockRoutine.deviceId
    }

    if(filteredList.isNotEmpty()) {

        LazyColumn(modifier = modifier, verticalArrangement = Arrangement.spacedBy(8.dp)) {
            items(items = clockRoutineList, key = { it.id }) { clockRoutine ->
                ClockRoutine(clockRoutine = clockRoutine, onClockRoutineClick = onClockRoutineClick)
                Divider()
            }
        }
    }
}



@Composable
private fun ClockRoutine(
    clockRoutine: ClockRoutine,
    onClockRoutineClick: (ClockRoutine) -> Unit,
    modifier: Modifier = Modifier
) {
    Row(modifier = modifier
        .fillMaxWidth()
        .clickable { onClockRoutineClick(clockRoutine) }
        .padding(vertical = 16.dp)
    ) {
        Text(
            text = clockRoutine.name,
            modifier = Modifier.weight(1.5f),
            fontWeight = FontWeight.Bold
        )

        Text(text = clockRoutine.status, modifier = Modifier.weight(1.0f))
    }
}

@Composable
private fun MultiRoutineList(
    multiRoutineList: List<MultiRoutine>,
    onMultiRoutineClick: (MultiRoutine) -> Unit,
    modifier: Modifier = Modifier,
    itemDetailsUiState: ItemDetailsUiState
) {

    val filteredList = multiRoutineList.filter { multiRoutine ->
        itemDetailsUiState.itemDetails.name == multiRoutine.deviceId
    }

    if(filteredList.isNotEmpty()) {

        LazyColumn(modifier = modifier, verticalArrangement = Arrangement.spacedBy(8.dp)) {
            items(items = multiRoutineList, key = { it.id }) { multiRoutine ->
                MultiRoutine(multiRoutine = multiRoutine, onMultiRoutineClick = onMultiRoutineClick)
                Divider()
            }
        }
    }
}

@Composable
private fun MultiRoutine(
    multiRoutine: MultiRoutine,
    onMultiRoutineClick: (MultiRoutine) -> Unit,
    modifier: Modifier = Modifier
) {
    Row(modifier = modifier
        .fillMaxWidth()
        .clickable { onMultiRoutineClick(multiRoutine) }
        .padding(vertical = 16.dp)
    ) {
        Text(
            text = multiRoutine.name,
            modifier = Modifier.weight(1.5f),
            fontWeight = FontWeight.Bold
        )
        Text(text = multiRoutine.status, modifier = Modifier.weight(1.0f))
    }
}

@Composable
private fun MixRoutineList(
    mixRoutineList: List<MixRoutine>,
    onMixRoutineClick: (MixRoutine) -> Unit,
    modifier: Modifier = Modifier,
    itemDetailsUiState: ItemDetailsUiState
) {

    val filteredList = mixRoutineList.filter { mixRoutine ->
        itemDetailsUiState.itemDetails.name == mixRoutine.deviceId
    }

    if(filteredList.isNotEmpty()) {

        LazyColumn(modifier = modifier, verticalArrangement = Arrangement.spacedBy(8.dp)) {
            items(items = mixRoutineList, key = { it.id }) { mixRoutine ->
                MixRoutine(mixRoutine = mixRoutine, onMixRoutineClick = onMixRoutineClick)
                Divider()
            }
        }
    }
}

@Composable
private fun MixRoutine(
    mixRoutine: MixRoutine,
    onMixRoutineClick: (MixRoutine) -> Unit,
    modifier: Modifier = Modifier
) {
    Row(modifier = modifier
        .fillMaxWidth()
        .clickable { onMixRoutineClick(mixRoutine) }
        .padding(vertical = 16.dp)
    ) {
        Text(
            text = mixRoutine.name,
            modifier = Modifier.weight(1.5f),
            fontWeight = FontWeight.Bold
        )
        Text(text = mixRoutine.status, modifier = Modifier.weight(1.0f))
    }
}