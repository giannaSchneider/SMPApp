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

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.AlertDialog
import androidx.compose.material.Button
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedButton
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.inventory.InventoryTopAppBar
import com.example.inventory.R
import com.example.inventory.ui.AppViewModelProvider
import com.example.inventory.ui.item.ItemInputForm
import com.example.inventory.ui.navigation.NavigationDestination
import com.example.inventory.ui.theme.InventoryTheme
import kotlinx.coroutines.launch

object MultiRoutineDetailsDestination : NavigationDestination {
    override val route = "multi_routine_details"
    override val titleRes = R.string.multi_routine_details_title
    const val multiRoutineIdArg = "multiRoutineId"
    val routeWithArgs = "$route/{$multiRoutineIdArg}"
}

@Composable
fun MultiRoutineDetailsScreen(
    navigateToEditMultiRoutine: (Int) -> Unit,
    navigateBack: () -> Unit,
    //navigateToMultiRoutineEntry: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: MultiRoutineDetailsViewModel = viewModel(factory = AppViewModelProvider.Factory)
) {
    val uiState = viewModel.uiState.collectAsState()
    val coroutineScope = rememberCoroutineScope()
    Scaffold(
        topBar = {
            InventoryTopAppBar(
                title = stringResource(MultiRoutineDetailsDestination.titleRes),
                canNavigateBack = true,
                navigateUp = navigateBack
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { navigateToEditMultiRoutine(uiState.value.multiRoutineDetails.id) },
                modifier = Modifier.navigationBarsPadding()
            ) {
                Icon(
                    imageVector = Icons.Default.Edit,
                    contentDescription = stringResource(R.string.edit_multi_routine_title),
                    tint = MaterialTheme.colors.onPrimary
                )
            }
        },
    ) { innerPadding ->
        MultiRoutineDetailsBody(
            /*multiRoutineUiState = viewModel.multiRoutineUiState,
            onRoutineValueChange = viewModel::updateUiState,
            onTimeSet = {coroutineScope.launch {
                viewModel.saveRoutine()
                navigateBack()
            }*/

            multiRoutineDetailsUiState = uiState.value,
            //onSellMultiRoutine = { viewModel.reduceQuantityByOne() },
            //onAddMultiRoutine = {navigateToMultiRoutineEntry()},
            onDelete = {
                // Note: If the user rotates the screen very fast, the operation may get cancelled
                // and the multiRoutine may not be deleted from the Database. This is because when config
                // change occurs, the Activity will be recreated and the rememberCoroutineScope will
                // be cancelled - since the scope is bound to composition.
                coroutineScope.launch {
                    viewModel.deleteMultiRoutine()
                    navigateBack()
                }
            },
            modifier = modifier.padding(innerPadding)
        )
    }
}

@Composable
private fun MultiRoutineDetailsBody(
    multiRoutineDetailsUiState: MultiRoutineDetailsUiState,
    //onSellMultiRoutine: () -> Unit,
    //onAddMultiRoutine: () -> Unit,
    onDelete: () -> Unit,
    modifier: Modifier = Modifier

) {
    Column(
        modifier = modifier
            .padding(16.dp)
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        var deleteConfirmationRequired by rememberSaveable { mutableStateOf(false) }
        //MultiRoutineInputForm(multiRoutineDetails = multiRoutineDetailsUiState.multiRoutineDetails, enabled = false)
        Text(
            text = "Routine Name: ${multiRoutineDetailsUiState.multiRoutineDetails.name}",
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = "If Primary Device: ${multiRoutineDetailsUiState.multiRoutineDetails.deviceId}",
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = "Is Set To: ${multiRoutineDetailsUiState.multiRoutineDetails.status}",
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = "Then Turn Secondary Device: ${multiRoutineDetailsUiState.multiRoutineDetails.name}",
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = "To: ${multiRoutineDetailsUiState.multiRoutineDetails.status2}",
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.height(8.dp))
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
    }
}

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
//fun MultiRoutineDetailsScreenPreview() {
//    InventoryTheme {
//        MultiRoutineDetailsBody(
//            MultiRoutineDetailsUiState(
//                outOfStock = true,
//                multiRoutineDetails = MultiRoutineDetails(1, "Pen", "$100", "10")
//            ),
//            onSellMultiRoutine = {},
//            onDelete = {}
//        )
//    }
//}
