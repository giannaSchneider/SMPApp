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
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.inventory.InventoryTopAppBar
import com.example.inventory.R
import com.example.inventory.data.Item
import com.example.inventory.data.ItemsRepository
import com.example.inventory.ui.AppViewModelProvider
import com.example.inventory.ui.item.ItemInputForm
import com.example.inventory.ui.navigation.NavigationDestination
import kotlinx.coroutines.launch
import java.util.Currency
import java.util.Locale

object MultiRoutineEntryDestination : NavigationDestination {
    override val route = "multi_routine_entry"
    override val titleRes = R.string.multi_routine_entry_title
}

/*
This routine allows the user to turn a device on or off after another device has been set on or off
 */

@Composable
fun MultiRoutineEntryScreen(
    navigateBack: () -> Unit,
    onNavigateUp: () -> Unit,
    modifier: Modifier = Modifier,
    canNavigateBack: Boolean = true,
    viewModel: MultiRoutineEntryViewModel = viewModel(factory = AppViewModelProvider.Factory)
) {
    val coroutineScope = rememberCoroutineScope()
    Scaffold(
        topBar = {
            InventoryTopAppBar(
                title = stringResource(MultiRoutineEntryDestination.titleRes),
                canNavigateBack = canNavigateBack,
                navigateUp = onNavigateUp
            )
        }
    ) { innerPadding ->
        MultiRoutineEntryBody(
            multiRoutineUiState = viewModel.multiRoutineUiState,
            onMultiRoutineValueChange = viewModel::updateUiState,
            onSaveClick = {
                // Note: If the user rotates the screen very fast, the operation may get cancelled
                // and the multiRoutine may not be saved in the Database. This is because when config
                // change occurs, the Activity will be recreated and the rememberCoroutineScope will
                // be cancelled - since the scope is bound to composition.
                coroutineScope.launch {
                    viewModel.saveMultiRoutine()
                    navigateBack()
                }
            },
            modifier = modifier.padding(innerPadding)
        )
    }
}

@Composable
fun MultiRoutineEntryBody(
    multiRoutineUiState: MultiRoutineUiState,
    onMultiRoutineValueChange: (MultiRoutineDetails) -> Unit,
    viewModel: MixRoutineEntryViewModel = viewModel(factory = AppViewModelProvider.Factory),
    onSaveClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(32.dp)
    ) {
        MultiRoutineInputForm(multiRoutineDetails = multiRoutineUiState.multiRoutineDetails, onMultiRoutineValueChange = onMultiRoutineValueChange, itemsRepository = viewModel.itemsRepository, onItemSelected = {})
        Button(
            onClick = onSaveClick,
            enabled = multiRoutineUiState.isEntryValid,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(stringResource(R.string.save_action))
        }
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun MultiRoutineInputForm(
    multiRoutineDetails: MultiRoutineDetails,
    modifier: Modifier = Modifier,
    onMultiRoutineValueChange: (MultiRoutineDetails) -> Unit = {},
    enabled: Boolean = true,
    itemsRepository: ItemsRepository,
    onItemSelected: (Item) -> Unit,
) {
    Column(modifier = modifier.fillMaxWidth() .padding(20.dp), verticalArrangement = Arrangement.spacedBy(16.dp)) {

        Text(
            text = "Name this routine:",
            fontStyle = FontStyle.Italic,
            fontSize = 16.sp
        )
        OutlinedTextField(
            value = multiRoutineDetails.name,
            onValueChange = { onMultiRoutineValueChange(multiRoutineDetails.copy(name = it)) },
            label = { Text(stringResource(R.string.multi_routine_name_req)) },
            modifier = Modifier.fillMaxWidth(),
            enabled = enabled,
            singleLine = true
        )

        /////////////////////////////////Device Selection/////////////////////////////////////////////////////
        var items = itemsRepository.getAllItemsStream().collectAsState(emptyList())

        var expanded3 by remember { mutableStateOf(false) }
        var selectedOption3 by remember { mutableStateOf<Item?>(null) }

        ExposedDropdownMenuBox(
            expanded = expanded3,
            onExpandedChange = { expanded3 = !expanded3 }
        ) {
            TextField(
                readOnly = true,
                value = selectedOption3?.name ?: "",
                onValueChange = { },
                label = { Text("Select Device") },
                trailingIcon = {
                    ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded3)
                },
                colors = ExposedDropdownMenuDefaults.textFieldColors()
            )

            ExposedDropdownMenu(
                expanded = expanded3,
                onDismissRequest = { expanded3 = false }
            ) {
                items.value.forEach { item ->
                    DropdownMenuItem(
                        onClick = {
                            selectedOption3 = item
                            expanded3 = false
                            onItemSelected(item)
                            onMultiRoutineValueChange(multiRoutineDetails.copy(deviceId = item.name))
                        }
                    ) {
                        Text(text = item.name)
                    }
                }

            }
        }

        ////////////////////////END OF DEVICE SELECTION/////////////////

        Text(text = "If this device is ",
            fontStyle = FontStyle.Italic,
            fontSize = 16.sp
            )

        val options = listOf("On", "Off")
        var expanded by remember { mutableStateOf(false) }
        var selectedOptionText by remember { mutableStateOf(options[0]) }

        ExposedDropdownMenuBox(
            expanded = expanded,
            onExpandedChange = {
                expanded = !expanded
            }
        ) {
            TextField(
                readOnly = true,
                value = selectedOptionText,
                onValueChange = { },
                label = { Text("Set Device On or Off") },
                trailingIcon = {
                    ExposedDropdownMenuDefaults.TrailingIcon(
                        expanded = expanded
                    )
                },
                colors = ExposedDropdownMenuDefaults.textFieldColors()
            )
            ExposedDropdownMenu(
                expanded = expanded,
                onDismissRequest = {
                    expanded = false
                }
            ) {
                options.forEach { selectionOption ->
                    DropdownMenuItem(
                        onClick = {
                            selectedOptionText = selectionOption
                            expanded = false
                            onMultiRoutineValueChange(multiRoutineDetails.copy(status = selectedOptionText))
                        }
                    ){
                        Text(text = selectionOption)
                    }
                }
            }
        }
//////////////////////////////////////////////DEVICE 2////////////////////////////////////////////////////
        Text(text = "Then turn:",
            fontStyle = FontStyle.Italic,
            fontSize = 16.sp)

        /////////////////////////////////Device Selection/////////////////////////////////////////////////////
        var items2 = itemsRepository.getAllItemsStream().collectAsState(emptyList())

        var expanded4 by remember { mutableStateOf(false) }
        var selectedOption4 by remember { mutableStateOf<Item?>(null) }

        ExposedDropdownMenuBox(
            expanded = expanded4,
            onExpandedChange = { expanded4 = !expanded4 }
        ) {
            TextField(
                readOnly = true,
                value = selectedOption4?.name ?: "",
                onValueChange = { },
                label = { Text("Select Device") },
                trailingIcon = {
                    ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded4)
                },
                colors = ExposedDropdownMenuDefaults.textFieldColors()
            )

            ExposedDropdownMenu(
                expanded = expanded4,
                onDismissRequest = { expanded4 = false }
            ) {
                items2.value.forEach { item2 ->
                    DropdownMenuItem(
                        onClick = {
                            selectedOption4 = item2
                            expanded4 = false
                            onItemSelected(item2)
                            onMultiRoutineValueChange(multiRoutineDetails.copy(deviceId2 = item2.name))
                        }
                    ) {
                        Text(text = item2.name)
                    }
                }

            }
        }

        ////////////////////////END OF DEVICE SELECTION/////////////////

        var expanded2 by remember { mutableStateOf(false) }
        var selectedOptionText2 by remember { mutableStateOf(options[0]) }
        ExposedDropdownMenuBox(
            expanded = expanded2,
            onExpandedChange = {
                expanded2 = !expanded2
            }
        ) {
            TextField(
                readOnly = true,
                value = selectedOptionText2,
                onValueChange = { },
                label = { Text("Set Device On or Off") },
                trailingIcon = {
                    ExposedDropdownMenuDefaults.TrailingIcon(
                        expanded = expanded2
                    )
                },
                colors = ExposedDropdownMenuDefaults.textFieldColors()
            )
            ExposedDropdownMenu(
                expanded = expanded2,
                onDismissRequest = {
                    expanded2 = false
                }
            ) {
                options.forEach { selectionOption2 ->
                    DropdownMenuItem(
                        onClick = {
                            selectedOptionText2 = selectionOption2
                            expanded2 = false
                            onMultiRoutineValueChange(multiRoutineDetails.copy(status2 = selectedOptionText2))
                        }
                    ){
                        Text(text = selectionOption2)
                    }
                }
            }
        }
    }
}
