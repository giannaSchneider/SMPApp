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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
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
import java.time.format.DateTimeFormatter
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
enum class ShowTimeInputOption2 {
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
            .padding(20.dp),
        verticalArrangement = Arrangement.spacedBy(32.dp)
    ) {
        MixRoutineInputForm(mixRoutineDetails = mixRoutineUiState.mixRoutineDetails, mixRoutineUiState = mixRoutineUiState, onMixRoutineValueChange = onMixRoutineValueChange, itemsRepository = viewModel.itemsRepository, onItemSelected = {}, onSaveClick = onSaveClick, showTimeInput = showTimeInput, showClockInput = showClockInput // Pass the itemsRepository parameter
        )


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
    onSaveClick: () -> Unit,
    mixRoutineUiState: MixRoutineUiState,
    showTimeInput: Boolean = false, // new parameter to show/hide time input
    showClockInput: Boolean = false

) {
    LazyColumn(
        modifier = modifier
            .fillMaxWidth()
            .padding(20.dp),
        verticalArrangement = Arrangement.spacedBy(32.dp)
    ) {item{

        Text(
            text = "Name this routine:",
            fontStyle = FontStyle.Italic,
            fontSize = 16.sp
        )
        OutlinedTextField(
                value = mixRoutineDetails.name,
                onValueChange = { onMixRoutineValueChange(mixRoutineDetails.copy(name = it)) },
                label = { Text(stringResource(R.string.mix_routine_name_req)) },
                modifier = Modifier.fillMaxWidth(),
                enabled = enabled,
                singleLine = true
            )
        Spacer(modifier = Modifier.size(10.dp))
        Text(text = "Turn device:",
            fontStyle = FontStyle.Italic,
            fontSize = 18.sp)

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
                            onMixRoutineValueChange(mixRoutineDetails.copy(deviceId = item2.name))
                        }
                    ) {
                        Text(text = item2.name)
                    }
                }

            }
        }
        Spacer(modifier = Modifier.size(15.dp))

        ////////////////////////END OF DEVICE SELECTION/////////////////


            ///////////////////////////Status Selection//////////////////////////////////////////////////
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
        Spacer(modifier = Modifier.size(15.dp))

            ///////////////////////////////////////////////////////////////////////////////////////////////////////////////
            var selectedOption by remember { mutableStateOf(ShowTimeInputOption.SHOW) }
        var selectedOption2 by remember { mutableStateOf(ShowTimeInputOption2.SHOW) }


            //Column(modifier = Modifier.fillMaxWidth()) {
            Text(text = "Set at a certain time")

            //Row(modifier = Modifier.padding(start = 8.dp)) {
            RadioButton(
                selected = selectedOption == ShowTimeInputOption.SHOW,
                onClick = { selectedOption = ShowTimeInputOption.SHOW }
            )
            //Text(text = "Show")
            Text(text = "Hide", modifier = Modifier.padding(start = 10.dp))
            RadioButton(
                selected = selectedOption == ShowTimeInputOption.HIDE,
                onClick = { selectedOption = ShowTimeInputOption.HIDE }
            )

            if (selectedOption == ShowTimeInputOption.SHOW) {
                 selectedOption2 = ShowTimeInputOption2.HIDE

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
                        val selectedTime = String.format("%02d:%02d", mHour, mMinute)
                        mTime.value = selectedTime
                        onMixRoutineValueChange(mixRoutineDetails.copy(time = selectedTime)) // Update the time value in your repository
                    }, mHour, mMinute, false
                )

                Button(
                    onClick = { mTimePickerDialog.show() },
                    colors = ButtonDefaults.buttonColors(backgroundColor = Color(0xFF117A65))
                ) {
                    Text(text = "Select a time to turn on/off", color = Color.White)
                }

                // Add a spacer of 10dp
                Spacer(modifier = Modifier.size(10.dp))

                // Display selected time
                Text(text = "Selected Time: ${mTime.value}", fontSize = 18.sp, fontWeight = FontWeight.Bold)

                //////////////////////////////

                } else {
                    // Hide timer input
                Text(text = "Set after a certain amount of time")
                RadioButton(
                    selected = selectedOption2 == ShowTimeInputOption2.SHOW && selectedOption == ShowTimeInputOption.HIDE,
                    onClick = { selectedOption2 = ShowTimeInputOption2.SHOW }
                )
                Text(text = "Hide", modifier = Modifier.padding(start = 10.dp))
                RadioButton(
                    selected = selectedOption2 == ShowTimeInputOption2.HIDE && selectedOption == ShowTimeInputOption.HIDE,
                    onClick = { selectedOption2 = ShowTimeInputOption2.HIDE }
                )

                if (selectedOption2 == ShowTimeInputOption2.SHOW) {
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
                            val selectedTime = String.format("%02d:%02d", mHour, mMinute)
                            mStartTime.value = selectedTime
                            onMixRoutineValueChange(mixRoutineDetails.copy(startTime = selectedTime))// Update the start time value in your repository
                        }, mStartHour, mStartMinute, false
                    )

                    val mEndTimePickerDialog = TimePickerDialog(
                        mContext,
                        { _, mHour: Int, mMinute: Int ->
                            val selectedTime = String.format("%02d:%02d", mHour, mMinute)
                            mEndTime.value = selectedTime
                            onMixRoutineValueChange(mixRoutineDetails.copy(endTime = selectedTime)) // Update the end time value in your repository
                        }, mEndHour, mEndMinute, false
                    )

                    Button(
                        onClick = { mStartTimePickerDialog.show() },
                        colors = ButtonDefaults.buttonColors(backgroundColor = Color(0xFF117A65))
                    ) {
                        Text(text = "Select Start Time", color = Color.White)
                    }

                    // Add a spacer of 20dp
                    Spacer(modifier = Modifier.size(20.dp))

                    // On button click, end time picker is displayed
                    Button(
                        onClick = { mEndTimePickerDialog.show() },
                        colors = ButtonDefaults.buttonColors(backgroundColor = Color(0xFF117A65))
                    ) {
                        Text(text = "Select End Time", color = Color.White)
                    }

                    // Add a spacer of 5dp
                    Spacer(modifier = Modifier.size(10.dp))

                    // Display selected start and end times
                    Text(text = "Selected Start Time: ${mStartTime.value}", fontSize = 18.sp, fontWeight = FontWeight.Bold)
                    Text(text = "Selected End Time: ${mEndTime.value}", fontSize = 18.sp, fontWeight = FontWeight.Bold)

                    // Function to calculate duration of selected period
                    fun calculateDuration(startTime: String, endTime: String): String {
                        if (startTime.isBlank() || endTime.isBlank()) {
                            return "Please select start and end times"
                        }

                        try {
                            val start = LocalTime.parse(startTime, DateTimeFormatter.ofPattern("HH:mm"))
                            val end = LocalTime.parse(endTime, DateTimeFormatter.ofPattern("HH:mm"))
                            val duration = Duration.between(start, end)
                            val hours = duration.toHours()
                            val minutes = duration.toMinutes() % 60
                            return String.format("%d hours %d minutes", hours, minutes)
                        } catch (e: DateTimeParseException) {
                            return "Invalid time format"
                        }
                    }

                    val duration = calculateDuration(mStartTime.value, mEndTime.value)
                    onMixRoutineValueChange(mixRoutineDetails.copy(duration = duration))
                }
            }

        Spacer(modifier = Modifier.size(10.dp))

            Text(text = "Then turn device:",
                fontStyle = FontStyle.Italic,
                fontSize = 18.sp)

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
                                onMixRoutineValueChange(mixRoutineDetails.copy(deviceId2 = item.name))
                            }
                        ) {
                            Text(text = item.name)
                        }
                    }

                }
            }

            ////////////////////////END OF DEVICE SELECTION/////////////////
        Spacer(modifier = Modifier.size(10.dp))
            /////////////////////SET SECOND STATUS//////////////////////////////////////
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
                            onMixRoutineValueChange(mixRoutineDetails.copy(status2 = selectedOptionText2))
                        }
                    ){
                        Text(text = selectionOption2)
                    }
                }
            }
            }
        ////////////////////////////////////////////////END SECOND STATUS////////////////////////////////////////////////////
        Spacer(modifier = Modifier.size(10.dp))
        ///////////////////////////////////////////////////////////////////////////////////////////////////////////////
        var selectedOption6 by remember { mutableStateOf(ShowTimeInputOption.SHOW) }
        var selectedOption7 by remember { mutableStateOf(ShowTimeInputOption2.SHOW) }


        //Column(modifier = Modifier.fillMaxWidth()) {
        Text(text = "Set at a certain time")

        //Row(modifier = Modifier.padding(start = 8.dp)) {
        RadioButton(
            selected = selectedOption6 == ShowTimeInputOption.SHOW,
            onClick = { selectedOption6 = ShowTimeInputOption.SHOW }
        )
        //Text(text = "Show")
        Text(text = "Hide", modifier = Modifier.padding(start = 10.dp))
        RadioButton(
            selected = selectedOption6 == ShowTimeInputOption.HIDE,
            onClick = { selectedOption6 = ShowTimeInputOption.HIDE }
        )

        if (selectedOption6 == ShowTimeInputOption.SHOW) {
            selectedOption7 = ShowTimeInputOption2.HIDE

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
                    onMixRoutineValueChange(mixRoutineDetails.copy(time2 = selectedTime)) // Update the time value in your repository
                }, mHour, mMinute, false
            )

            Button(
                onClick = { mTimePickerDialog.show() },
                colors = ButtonDefaults.buttonColors(backgroundColor = Color(0xFF117A65))
            ) {
                Text(text = "Select a time to turn on/off", color = Color.White)
            }

            // Add a spacer of 50dp
            Spacer(modifier = Modifier.size(50.dp))

            // Display selected time
            Text(text = "Selected Time: ${mTime.value}", fontSize = 18.sp, fontWeight = FontWeight.Bold)

            //////////////////////////////

        } else {
            // Hide timer input
            Text(text = "Set after a certain amount of time")
            RadioButton(
                selected = selectedOption7 == ShowTimeInputOption2.SHOW && selectedOption6 == ShowTimeInputOption.HIDE,
                onClick = { selectedOption7 = ShowTimeInputOption2.SHOW }
            )
            Text(text = "Hide", modifier = Modifier.padding(start = 10.dp))
            RadioButton(
                selected = selectedOption7 == ShowTimeInputOption2.HIDE && selectedOption6 == ShowTimeInputOption.HIDE,
                onClick = { selectedOption7 = ShowTimeInputOption2.HIDE }
            )

            if (selectedOption7 == ShowTimeInputOption2.SHOW) {
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
                        val selectedTime = String.format("%02d:%02d", mHour, mMinute)
                        mStartTime.value = selectedTime
                        onMixRoutineValueChange(mixRoutineDetails.copy(startTime2 = selectedTime))// Update the start time value in your repository
                    }, mStartHour, mStartMinute, false
                )

                val mEndTimePickerDialog = TimePickerDialog(
                    mContext,
                    { _, mHour: Int, mMinute: Int ->
                        val selectedTime = String.format("%02d:%02d", mHour, mMinute)
                        mEndTime.value = selectedTime
                        onMixRoutineValueChange(mixRoutineDetails.copy(endTime2 = selectedTime)) // Update the end time value in your repository
                    }, mEndHour, mEndMinute, false
                )

                Button(
                    onClick = { mStartTimePickerDialog.show() },
                    colors = ButtonDefaults.buttonColors(backgroundColor = Color(0xFF117A65))
                ) {
                    Text(text = "Select Start Time", color = Color.White)
                }

                // Add a spacer of 20dp
                Spacer(modifier = Modifier.size(20.dp))

                // On button click, end time picker is displayed
                Button(
                    onClick = { mEndTimePickerDialog.show() },
                    colors = ButtonDefaults.buttonColors(backgroundColor = Color(0xFF117A65))
                ) {
                    Text(text = "Select End Time", color = Color.White)
                }

                // Add a spacer of 100dp
                Spacer(modifier = Modifier.size(10.dp))

                // Display selected start and end times
                Text(text = "Selected Start Time: ${mStartTime.value}", fontSize = 18.sp, fontWeight = FontWeight.Bold)
                Text(text = "Selected End Time: ${mEndTime.value}", fontSize = 18.sp, fontWeight = FontWeight.Bold)

                // Function to calculate duration of selected period
                fun calculateDuration(startTime: String, endTime: String): String {
                    if (startTime.isBlank() || endTime.isBlank()) {
                        return "Please select start and end times"
                    }

                    try {
                        val start = LocalTime.parse(startTime, DateTimeFormatter.ofPattern("HH:mm"))
                        val end = LocalTime.parse(endTime, DateTimeFormatter.ofPattern("HH:mm"))
                        val duration = Duration.between(start, end)
                        val hours = duration.toHours()
                        val minutes = duration.toMinutes() % 60
                        return String.format("%d hours %d minutes", hours, minutes)
                    } catch (e: DateTimeParseException) {
                        return "Invalid time format"
                    }
                }

                val duration = calculateDuration(mStartTime.value, mEndTime.value)
                onMixRoutineValueChange(mixRoutineDetails.copy(duration2 = duration))
            }
        }
        Button(
            onClick = onSaveClick,
            enabled = mixRoutineUiState.isEntryValid,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(stringResource(R.string.save_action))
        }

        ////////////////////////////////////////////END////////////////////////////////////////////////////////////////////

        }
    }
}