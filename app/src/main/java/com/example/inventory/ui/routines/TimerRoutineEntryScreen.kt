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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.inventory.InventoryTopAppBar
import com.example.inventory.R
import com.example.inventory.data.RoutinesRepository
import com.example.inventory.data.TimerRoutine
import com.example.inventory.ui.AppViewModelProvider
import com.example.inventory.ui.navigation.NavigationDestination
import kotlinx.coroutines.launch
import java.time.Duration
import java.time.LocalTime
import java.time.LocalTime.parse
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeParseException
import java.util.*

object TimerRoutineEntryDestination : NavigationDestination {
    override val route = "timer_routine_entry"
    override val titleRes = R.string.timer_routine_entry_title
}

//// Function to calculate duration of selected period
//fun calculateDuration(startTime: String, endTime: String): String {
//    val start = parse(startTime)
//    val end = parse(endTime)
//    val duration = Duration.between(start, end)
//    val hours = duration.toHours()
//    val minutes = duration.toMinutes() % 60
//    return String.format("%d hours %d minutes", hours, minutes)
//}

@Composable
fun TimerRoutineEntryScreen(
    navigateBack: () -> Unit,
    onNavigateUp: () -> Unit,
    modifier: Modifier = Modifier,
    canNavigateBack: Boolean = true,
    viewModel: TimerRoutineEntryViewModel = viewModel(factory = AppViewModelProvider.Factory)
) {
    val coroutineScope = rememberCoroutineScope()
    Scaffold(
        topBar = {
            InventoryTopAppBar(
                title = stringResource(TimerRoutineEntryDestination.titleRes),
                canNavigateBack = canNavigateBack,
                navigateUp = onNavigateUp
            )
        }
    ) { innerPadding ->
        TimerRoutineEntryBody(
            timerRoutineUiState = viewModel.timerRoutineUiState,
            onTimerRoutineValueChange = viewModel::updateUiState,
            onSaveClick = {
                // Note: If the user rotates the screen very fast, the operation may get cancelled
                // and the timerRoutine may not be saved in the Database. This is because when config
                // change occurs, the Activity will be recreated and the rememberCoroutineScope will
                // be cancelled - since the scope is bound to composition.
                coroutineScope.launch {
                    viewModel.saveTimerRoutine()
                    navigateBack()
                }
            },
            modifier = modifier.padding(innerPadding)
        )
    }
}

@Composable
fun TimerRoutineEntryBody(
    timerRoutineUiState: TimerRoutineUiState,
    onTimerRoutineValueChange: (TimerRoutineDetails) -> Unit,
    onSaveClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(32.dp)
    ) {
        TimerRoutineInputForm(timerRoutineDetails = timerRoutineUiState.timerRoutineDetails, onTimerRoutineValueChange = onTimerRoutineValueChange)
        Button(
            onClick = onSaveClick,
            enabled = timerRoutineUiState.isEntryValid,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(stringResource(R.string.save_action))
        }
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun TimerRoutineInputForm(
    timerRoutineDetails: TimerRoutineDetails,
    modifier: Modifier = Modifier,
    onTimerRoutineValueChange: (TimerRoutineDetails) -> Unit = {},
    enabled: Boolean = true
) {
    Column(modifier = modifier.fillMaxWidth(), verticalArrangement = Arrangement.spacedBy(16.dp)) {
        OutlinedTextField(
            value = timerRoutineDetails.name,
            onValueChange = { onTimerRoutineValueChange(timerRoutineDetails.copy(name = it)) },
            label = { Text(stringResource(R.string.timer_routine_name_req)) },
            modifier = Modifier.fillMaxWidth(),
            enabled = enabled,
            singleLine = true
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
                            onTimerRoutineValueChange(timerRoutineDetails.copy(status = selectedOptionText))
                        }
                    ){
                        Text(text = selectionOption)
                    }
                }
            }
        }


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
                onTimerRoutineValueChange(timerRoutineDetails.copy(startTime = selectedTime ))
            // Update the start time value in your repository
            }, mStartHour, mStartMinute, false
        )

        val mEndTimePickerDialog = TimePickerDialog(
            mContext,
            { _, mHour: Int, mMinute: Int ->
                val selectedTime = "$mHour:$mMinute"
                mEndTime.value = selectedTime
                onTimerRoutineValueChange(timerRoutineDetails.copy(endTime = selectedTime)) // Update the end time value in your repository
            }, mEndHour, mEndMinute, false
        )


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
        onTimerRoutineValueChange(timerRoutineDetails.copy(duration = duration))

    }
}
