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

import android.app.TimePickerDialog
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import android.content.Context
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.sp
import androidx.room.Room
import com.example.inventory.InventoryTopAppBar
import com.example.inventory.R
import com.example.inventory.data.Item
import com.example.inventory.data.ItemDao
import com.example.inventory.data.InventoryDatabase
import com.example.inventory.data.ItemsRepository
import com.example.inventory.data.ItemsRepositoryImpl
import com.example.inventory.ui.AppViewModelProvider
import com.example.inventory.ui.item.ItemInputForm
import com.example.inventory.ui.navigation.NavigationDestination
import kotlinx.coroutines.flow.map

import kotlinx.coroutines.launch
import java.time.Duration
import java.time.LocalTime
import java.time.format.DateTimeParseException
import java.util.*

object MixRoutineEntryDestination : NavigationDestination {
    override val route = "mix_routine_entry"
    override val titleRes = R.string.mix_routine_entry_title
}
enum class ShowTimeInputOption {
    SHOW,
    HIDE
}
/*
This routine allows the user to turn a device on or off after another device has been set on or off after a time
 */

@Composable
fun MixRoutineEntryScreen(
    navigateBack: () -> Unit,
    onNavigateUp: () -> Unit,
    modifier: Modifier = Modifier,
    canNavigateBack: Boolean = true,
    viewModel: MixRoutineEntryViewModel = viewModel(factory = AppViewModelProvider.Factory),
    //showTimeInput: Boolean

) {
    val coroutineScope = rememberCoroutineScope()
    Scaffold(
        topBar = {
            InventoryTopAppBar(
                title = stringResource(MixRoutineEntryDestination.titleRes),
                canNavigateBack = canNavigateBack,
                navigateUp = onNavigateUp
            )
        }
    ) { innerPadding ->
        MixRoutineEntryBody(
            mixRoutineUiState = viewModel.mixRoutineUiState,
            onMixRoutineValueChange = viewModel::updateUiState,
            onSaveClick = {
                // Note: If the user rotates the screen very fast, the operation may get cancelled
                // and the mixRoutine may not be saved in the Database. This is because when config
                // change occurs, the Activity will be recreated and the rememberCoroutineScope will
                // be cancelled - since the scope is bound to composition.
                coroutineScope.launch {
                    viewModel.saveMixRoutine()
                    navigateBack()
                }
            },
            modifier = modifier.padding(innerPadding)
        )
    }
}

@Composable
fun MixRoutineEntryBody(
    mixRoutineUiState: MixRoutineUiState,
    onMixRoutineValueChange: (MixRoutineDetails) -> Unit,
    onSaveClick: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: MixRoutineEntryViewModel = viewModel(factory = AppViewModelProvider.Factory),
    showClockInput: Boolean = false,
    showTimeInput : Boolean = false


) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(32.dp)
    ) {
        MixRoutineInputForm(mixRoutineDetails = mixRoutineUiState.mixRoutineDetails, onMixRoutineValueChange = onMixRoutineValueChange, itemsRepository = viewModel.itemsRepository, onItemSelected = {}, showTimeInput = showTimeInput, showClockInput = showClockInput // Pass the itemsRepository parameter
        )
        Button(
            onClick = onSaveClick,
            enabled = mixRoutineUiState.isEntryValid,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(stringResource(R.string.save_action))
        }
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun MixRoutineInputForm(
    mixRoutineDetails: MixRoutineDetails,
    modifier: Modifier = Modifier,
    onMixRoutineValueChange: (MixRoutineDetails) -> Unit = {},
    enabled: Boolean = true,
    itemsRepository: ItemsRepository,
    onItemSelected: (Item) -> Unit,
    showTimeInput: Boolean = false, // new parameter to show/hide time input
    showClockInput: Boolean = false

) {
    Column(modifier = modifier.fillMaxWidth(), verticalArrangement = Arrangement.spacedBy(16.dp)) {
        OutlinedTextField(
            value = mixRoutineDetails.name,
            onValueChange = { onMixRoutineValueChange(mixRoutineDetails.copy(name = it)) },
            label = { Text(stringResource(R.string.mix_routine_name_req)) },
            modifier = Modifier.fillMaxWidth(),
            enabled = enabled,
            singleLine = true
        )
        Text(text = "Turn this device..:")
//        OutlinedTextField(
//            value = mixRoutineDetails.time,
//            onValueChange = { onMixRoutineValueChange(mixRoutineDetails.copy(time = it)) },
//            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
//            label = { Text(stringResource(R.string.mix_routine_time_req)) },
//            leadingIcon = { Text(Currency.getInstance(Locale.getDefault()).symbol) },
//            modifier = Modifier.fillMaxWidth(),
//            enabled = enabled,
//            singleLine = true
//        )

        ///////////////////////////Status Selection//////////////////////////////////////////////////
        val options = listOf("On", "Off")
        var expanded by remember { mutableStateOf(false) }
        var selectedOptionText by remember { mutableStateOf(options[0]) }

//        OutlinedTextField(
//            value = mixRoutineDetails.name2,
//            onValueChange = { onMixRoutineValueChange(mixRoutineDetails.copy(name2 = it)) },
//            label = { Text(stringResource(R.string.mix_routine_name_req)) },
//            modifier = Modifier.fillMaxWidth(),
//            enabled = enabled,
//            singleLine = true
//        )

//    var expanded2 by remember { mutableStateOf(false) }
//        //var selectedOptionText2 by remember { mutableStateOf(options[0]) }
//        var items = itemsRepository.getAllItemsStream().collectAsState(emptyList())
//        var options = items
//        var selectedOptionText2 by remember { mutableStateOf(options[0]) }
//
//        ExposedDropdownMenuBox(
//            expanded = expanded2,
//            onExpandedChange = {
//                expanded2 = !expanded2
//            }
//        ) {
//            TextField(
//                readOnly = true,
//                value = selectedOptionText2,
//                onValueChange = { },
//                label = { Text("Select Device") },
//                trailingIcon = {
//                    ExposedDropdownMenuDefaults.TrailingIcon(
//                        expanded = expanded2
//                    )
//                },
//                colors = ExposedDropdownMenuDefaults.textFieldColors()
//            )
//            ExposedDropdownMenu(
//                expanded = expanded2,
//                onDismissRequest = {
//                    expanded2 = false
//                }
//            ) {
//                options.forEach { selectionOption ->
//                    DropdownMenuItem(
//                        onClick = {
//                            selectedOptionText2 = selectionOption
//                            expanded2 = false
//                            onMixRoutineValueChange(mixRoutineDetails.copy(name2 = selectedOptionText2))
//                        }
//                    ){
//                        Text(text = selectionOption)
//                    }
//                }
//            }
//        }


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
                label = { Text("Set Status") },
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
                            onMixRoutineValueChange(mixRoutineDetails.copy(status = selectedOptionText))
                        }
                    ) {
                        Text(text = selectionOption)
                    }
                }
            }
        }

        ///////////////////////////////////////////////////////////////////////////////////////////////////////////////
        var selectedOption by remember { mutableStateOf(ShowTimeInputOption.SHOW) }

        //Column(modifier = Modifier.fillMaxWidth()) {
        Text(text = "Show Time Input")

        //Row(modifier = Modifier.padding(start = 8.dp)) {
        RadioButton(
            selected = selectedOption == ShowTimeInputOption.SHOW,
            onClick = { selectedOption = ShowTimeInputOption.SHOW }
        )
        Text(text = "Show")

        RadioButton(
            selected = selectedOption == ShowTimeInputOption.HIDE,
            onClick = { selectedOption = ShowTimeInputOption.HIDE }
        )
        Text(text = "Hide", modifier = Modifier.padding(start = 16.dp))
        //}

        if (selectedOption == ShowTimeInputOption.SHOW) {
            val mContext = LocalContext.current

            // Declaring and initializing a calendar
            val mCalendar = Calendar.getInstance()
            val mHour = mCalendar[Calendar.HOUR_OF_DAY]
            val mMinute = mCalendar[Calendar.MINUTE]

            // Value for storing time as a string
            val mTime = remember { mutableStateOf("") }

            val mTimePickerDialog = TimePickerDialog(
                mContext,
                { _, mHour: Int, mMinute: Int ->
                    val selectedTime = "$mHour:$mMinute"
                    mTime.value = selectedTime
                    mixRoutineDetails.copy(time = selectedTime) // Update the time value in your repository
                }, mHour, mMinute, false
            )

            /*Column(modifier = Modifier.fillMaxSize(), verticalArrangement = Arrangement.Center, horizontalAlignment = Alignment.CenterHorizontally) {*/

            // On button click, TimePicker is
            // displayed, user can select a time
            Button(
                onClick = { mTimePickerDialog.show() },
                colors = ButtonDefaults.buttonColors(backgroundColor = Color(0XFF0F9D58))
            ) {
                Text(text = "Select a time to turn on/off", color = Color.White)
            }

            // Add a spacer of 75dp
            Spacer(modifier = Modifier.size(75.dp))

            // Display selected time
            Text(text = "Selected Time: ${mTime.value}", fontSize = 14.sp)
            //}


            //////////////////////////////////////////////////////////////////////////////////////////////////////////////////


            //////////////////////////////////////////////////
//        var isSwitchOn by remember { mutableStateOf(showTimeInput) }
//        Text(text = "Show Time Input")
//        Switch(
//            checked = isSwitchOn,
//            onCheckedChange = { isSwitchOn = it },
//            modifier = Modifier.padding(start = 8.dp)
//        )
//
//        if (isSwitchOn) {
//            // Fetching local context
//            val mContext = LocalContext.current
//
//            // Declaring and initializing a calendar
//            val mCalendar = Calendar.getInstance()
//            val mHour = mCalendar[Calendar.HOUR_OF_DAY]
//            val mMinute = mCalendar[Calendar.MINUTE]
//
//            // Value for storing time as a string
//            val mTime = remember { mutableStateOf("") }
//
//            val mTimePickerDialog = TimePickerDialog(
//                mContext,
//                { _, mHour: Int, mMinute: Int ->
//                    val selectedTime = "$mHour:$mMinute"
//                    mTime.value = selectedTime
//                    mixRoutineDetails.copy(time = selectedTime) // Update the time value in your repository
//                }, mHour, mMinute, false
//            )
//
//            /*Column(modifier = Modifier.fillMaxSize(), verticalArrangement = Arrangement.Center, horizontalAlignment = Alignment.CenterHorizontally) {*/
//
//            // On button click, TimePicker is
//            // displayed, user can select a time
//            Button(onClick = { mTimePickerDialog.show() }, colors = ButtonDefaults.buttonColors(backgroundColor = Color(0XFF0F9D58))) {
//                Text(text = "Select a time to turn on/off", color = Color.White)
//            }
//
//            // Add a spacer of 75dp
//            Spacer(modifier = Modifier.size(75.dp))
//
//            // Display selected time
//            Text(text = "Selected Time: ${mTime.value}", fontSize = 14.sp)
//        }

            //////////////////////////////////////////////////
            var isSwitchOn2 by remember { mutableStateOf(showClockInput) }
            Text(text = "Show Clock Input")
            Switch(
                checked = isSwitchOn2,
                onCheckedChange = { isSwitchOn2 = it },
                modifier = Modifier.padding(start = 8.dp)
            )

            if (isSwitchOn2) {
                // Fetching local context
                // Fetching local context
                val mContext = LocalContext.current

// Declaring and initializing a calendar
                val mCalendar = Calendar.getInstance()
                val mStartHour = mCalendar[Calendar.HOUR_OF_DAY]
                val mStartMinute = mCalendar[Calendar.MINUTE]
                val mEndHour = mCalendar[Calendar.HOUR_OF_DAY]
                val mEndMinute = mCalendar[Calendar.MINUTE]

// Creating mutable states to hold the selected times
                val mStartTime = remember { mutableStateOf("") }
                val mEndTime = remember { mutableStateOf("") }


// Creating TimePicker dialogs for start and end times
                val mStartTimePickerDialog = TimePickerDialog(
                    mContext,
                    { _, mHour: Int, mMinute: Int ->
                        val selectedTime = "$mHour:$mMinute"
                        mStartTime.value = selectedTime
                        mixRoutineDetails.copy(startTime = selectedTime)// Update the start time value in your repository
                    }, mStartHour, mStartMinute, false
                )

                val mEndTimePickerDialog = TimePickerDialog(
                    mContext,
                    { _, mHour: Int, mMinute: Int ->
                        val selectedTime = "$mHour:$mMinute"
                        mEndTime.value = selectedTime
                        mixRoutineDetails.copy(endTime = selectedTime) // Update the end time value in your repository
                    }, mEndHour, mEndMinute, false
                )

                /*Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {*/
                // On button click, start time picker is displayed
                Button(
                    onClick = { mStartTimePickerDialog.show() },
                    colors = ButtonDefaults.buttonColors(backgroundColor = Color(0XFF0F9D58))
                ) {
                    Text(text = "Select Start Time", color = Color.White)
                }

                // Add a spacer of 20dp
                Spacer(modifier = Modifier.size(20.dp))

                // On button click, end time picker is displayed
                Button(
                    onClick = { mEndTimePickerDialog.show() },
                    colors = ButtonDefaults.buttonColors(backgroundColor = Color(0XFF0F9D58))
                ) {
                    Text(text = "Select End Time", color = Color.White)
                }

                // Add a spacer of 100dp
                Spacer(modifier = Modifier.size(100.dp))

                // Display selected start and end times
                Text(text = "Selected Start Time: ${mStartTime.value}", fontSize = 14.sp)
                Text(text = "Selected End Time: ${mEndTime.value}", fontSize = 14.sp)

                // Function to calculate duration of selected period
                fun calculateDuration(startTime: String, endTime: String): String {
                    if (startTime.isBlank() || endTime.isBlank()) {
                        return "Please select start and end times"
                    }

                    try {
                        val start = LocalTime.parse(startTime)
                        val end = LocalTime.parse(endTime)
                        val duration = Duration.between(start, end)
                        val hours = duration.toHours()
                        val minutes = duration.toMinutes() % 60
                        return String.format("%d hours %d minutes", hours, minutes)
                    } catch (e: DateTimeParseException) {
                        return "Invalid time format"
                    }
                }

                val duration = calculateDuration(mStartTime.value, mEndTime.value)
                mixRoutineDetails.copy(duration = duration)
            }
            /////////////////////////////////////TEXT//////////////////////////
            /////////////////////////////////////TEXT//////////////////////////


            Text(text = "Then turn device:")

            /////////////////////////////////Device Selection/////////////////////////////////////////////////////
            var items = itemsRepository.getAllItemsStream().collectAsState(emptyList())

            var expanded2 by remember { mutableStateOf(false) }
            var selectedOption2 by remember { mutableStateOf<Item?>(null) }

            ExposedDropdownMenuBox(
                expanded = expanded2,
                onExpandedChange = { expanded2 = !expanded2 }
            ) {
                TextField(
                    readOnly = true,
                    value = selectedOption2?.name ?: "",
                    onValueChange = { },
                    label = { Text("Select Device") },
                    trailingIcon = {
                        ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded2)
                    },
                    colors = ExposedDropdownMenuDefaults.textFieldColors()
                )

                ExposedDropdownMenu(
                    expanded = expanded2,
                    onDismissRequest = { expanded2 = false }
                ) {
                    items.value.forEach { item ->
                        DropdownMenuItem(
                            onClick = {
                                selectedOption2 = item
                                expanded2 = false
                                onItemSelected(item)
                            }
                        ) {
                            Text(text = item.name)
                        }
                    }

                }
            }

            ////////////////////////END OF DEVICE SELECTION/////////////////
            /////////////////////SET SECOND STATUS//////////////////////////////////////
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
                    label = { Text("Set Status") },
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
                                onMixRoutineValueChange(mixRoutineDetails.copy(status2 = selectedOptionText))
                            }
                        ) {
                            Text(text = selectionOption)
                        }
                    }
                }
            }
        }

//        OutlinedTextField(
//            value = mixRoutineDetails.status,
//            onValueChange = { onValueChange(mixRoutineDetails.copy(status = it)) },
//           //keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
//            label = { Text(stringResource(R.string.status_req)) },
//            modifier = Modifier.fillMaxWidth(),
//            enabled = enabled,
//            singleLine = true
//        )
    }
}
//}

//@OptIn(ExperimentalMaterialApi::class)
//@Composable
//fun ItemDropdownMenu(
//    itemsRepository: ItemsRepository,
//    onItemSelected: (Item) -> Unit
//) {
//    var items = itemsRepository.getAllItemsStream().collectAsState(emptyList())
//
//    var expanded by remember { mutableStateOf(false) }
//    var selectedOption by remember { mutableStateOf<Item?>(null) }
//
//    ExposedDropdownMenuBox(
//        expanded = expanded,
//        onExpandedChange = { expanded = !expanded }
//    ) {
//        TextField(
//            readOnly = true,
//            value = selectedOption?.name ?: "",
//            onValueChange = { },
//            label = { Text("Select Item") },
//            trailingIcon = {
//                ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded)
//            },
//            colors = ExposedDropdownMenuDefaults.textFieldColors()
//        )
//
//        ExposedDropdownMenu(
//            expanded = expanded,
//            onDismissRequest = { expanded = false }
//        ) {
//            items.value.forEach { item ->
//                DropdownMenuItem(
//                    onClick = {
//                        selectedOption = item
//                        expanded = false
//                        onItemSelected(item)
//                    }
//                ) {
//                    Text(text = item.name)
//                }
//            }
//
//        }
//    }
//}

//@Preview(showBackground = true)
//@Composable
//private fun MixRoutineEntryScreenPreview() {
//    InventoryTheme {
//        MixRoutineEntryBody(
//            mixRoutineUiState = MixRoutineUiState(
//                MixRoutineDetails(
//                    name = "MixRoutine name",
//                    price = "10.00",
//                    quantity = "5"
//                )
//            ),
//            onMixRoutineValueChange = {},
//            onSaveClick = {}
//        )
//    }
//}
