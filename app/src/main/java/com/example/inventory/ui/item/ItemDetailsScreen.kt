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
    navigateToTimerRoutineEntry: () -> Unit,
    navigateToClockRoutineEntry: () -> Unit,
    navigateToMultiRoutineEntry: () -> Unit,
    navigateToMixRoutineEntry: () -> Unit,
    navigateToTimerRoutineUpdate: (Int) -> Unit,

    modifier: Modifier = Modifier,
    viewModel: ItemDetailsViewModel = viewModel(factory = AppViewModelProvider.Factory)
) {
    val uiState = viewModel.uiState.collectAsState()
    val itemUiState2 by viewModel.itemUiState2.collectAsState()

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
            //onAddItem = {navigateToItemEntry()},
            onAddTimerRoutine = {navigateToTimerRoutineEntry()},
            onAddClockRoutine = {navigateToClockRoutineEntry()},
            onAddMultiRoutine = {navigateToMultiRoutineEntry()},
            onAddMixRoutine = {navigateToMixRoutineEntry()},

            timerRoutineList = itemUiState2.timerRoutineList,
            onTimerRoutineClick = navigateToTimerRoutineUpdate,
            //timerRoutineList: List<TimerRoutine>,
        //onTimerRoutineClick: (Int) -> Unit,


            /*onViewTimerRoutine = {navigateToTimerRoutineDetails()},
            onViewClockRoutine = {navigateToClockRoutineDetails()},
            onViewMultiRoutine = {navigateToMultiRoutineDetails()},
            onViewMixRoutine = {navigateToMixRoutineDetails()},*/
            onDelete = {
                // Note: If the user rotates the screen very fast, the operation may get cancelled
                // and the item may not be deleted from the Database. This is because when config
                // change occurs, the Activity will be recreated and the rememberCoroutineScope will
                // be cancelled - since the scope is bound to composition.
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
    onAddTimerRoutine: () -> Unit,
    onAddClockRoutine: () -> Unit,
    onAddMultiRoutine: () -> Unit,
    onAddMixRoutine: () -> Unit,
    timerRoutineList: List<TimerRoutine>,
    onTimerRoutineClick: (Int) -> Unit,


    /*onViewTimerRoutine: () -> Unit,
    onViewClockRoutine: () -> Unit,
    onViewMultiRoutine: () -> Unit,
    onViewMixRoutine: () -> Unit,*/
    //onAddItem: () -> Unit,
    onDelete: () -> Unit,
    modifier: Modifier = Modifier
) {
   // Column(modifier.verticalScroll(rememberScrollState())) {

        Column(
            modifier = modifier
                .padding(16.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            var deleteConfirmationRequired by rememberSaveable { mutableStateOf(false) }
            ItemInputForm(itemDetails = itemDetailsUiState.itemDetails, enabled = false)
            Button(
                onClick = onSellItem,
                modifier = Modifier.fillMaxWidth(),
                enabled = !itemDetailsUiState.outOfStock
            ) {
                Text(stringResource(R.string.sell))
            }
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


//        Button(
//            onClick = onViewTimerRoutine,
//            modifier = Modifier.fillMaxWidth(),
//            //enabled = !itemDetailsUiState.outOfStock
//        ) {
//            Text(stringResource(R.string.timer_routine_details_title))
//        }
//        Button(
//            onClick = onViewClockRoutine,
//            modifier = Modifier.fillMaxWidth(),
//            //enabled = !itemDetailsUiState.outOfStock
//        ) {
//            Text(stringResource(R.string.clock_routine_details_title))
//        }
//        Button(
//            onClick = onViewMultiRoutine,
//            modifier = Modifier.fillMaxWidth(),
//            //enabled = !itemDetailsUiState.outOfStock
//        ) {
//            Text(stringResource(R.string.multi_routine_details_title))
//        }
//        Button(
//            onClick = onViewMixRoutine,
//            modifier = Modifier.fillMaxWidth(),
//            //enabled = !itemDetailsUiState.outOfStock
//        ) {
//            Text(stringResource(R.string.mix_routine_details_title))
//        }
//        Button(
//            onClick = onAddItem,
//            modifier = Modifier.fillMaxWidth(),
//            enabled = !itemDetailsUiState.outOfStock
//        ) {
//            Text(stringResource(R.string.item_entry_title))
//        }
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
            Divider()
        }

            TimerRoutineListHeader()
            Divider()
//            LazyColumn(modifier = modifier.weight(1f)) {
//                items(timerRoutineList) { routine ->
//                    TimerRoutineList(timerRoutineList = timerRoutineList,
//                onTimerRoutineClick = { onTimerRoutineClick(it.id) })
//                }
//            }

//        Box(
//            modifier = Modifier
//                .padding(bottom = 48.dp) // set the bottom padding to the height of the TimerRoutineListHeader
//                .fillMaxSize(),
//            contentAlignment = Alignment.BottomCenter
//        ) {
//        TimerRoutineListHeader()
//        Divider()
        if (timerRoutineList.isEmpty()) {
            Text(
                text = stringResource(R.string.no_item_description),
                style = MaterialTheme.typography.subtitle2
            )
        } else {
            TimerRoutineList(
                timerRoutineList = timerRoutineList,
                onTimerRoutineClick = { onTimerRoutineClick(it.id) })
        }
//    }
//        Divider()

//}
}
@Composable
private fun TimerRoutineList(
    timerRoutineList: List<TimerRoutine>,
    onTimerRoutineClick: (TimerRoutine) -> Unit,
    modifier: Modifier = Modifier
) {

    LazyColumn(modifier = modifier, verticalArrangement = Arrangement.spacedBy(8.dp)) {
        items(items = timerRoutineList, key = { it.id }) { timerRoutine ->
            TimerRoutine(timerRoutine = timerRoutine, onTimerRoutineClick = onTimerRoutineClick)
            Divider()
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
        }
    )
}

//@Preview(showBackground = true)
//@Composable
//fun ItemDetailsScreenPreview() {
//    InventoryTheme {
//        ItemDetailsBody(
//            ItemDetailsUiState(
//                outOfStock = true,
//                itemDetails = ItemDetails(1, "Pen", "$100", "10")
//            ),
//            onSellItem = {},
//            onDelete = {}
//        )
//    }
//}
