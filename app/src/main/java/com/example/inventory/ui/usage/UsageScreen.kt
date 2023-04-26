package com.example.inventory.ui.usage

import com.example.inventory.R
import com.example.inventory.ui.navigation.NavigationDestination
import com.example.inventory.ui.routines.TimerRoutineDetailsUiState
import com.example.inventory.ui.routines.TimerRoutineDetailsViewModel



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

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
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
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.inventory.InventoryTopAppBar
import com.example.inventory.ui.AppViewModelProvider


object UsageDetailsDestination : NavigationDestination {
    override val route = "usage_details"
    override val titleRes = R.string.usage_title

}

@Composable
fun UsageScreen(
    navigateBack: () -> Unit,
    modifier: Modifier = Modifier,
   // viewModel: NetworkHomeViewModel2 = viewModel(factory = AppViewModelProvider.Factory)
) {
    val uiState = viewModel.awsuiState.collectAsState()
    val coroutineScope = rememberCoroutineScope()
    Scaffold(
        topBar = {
            InventoryTopAppBar(
                title = stringResource(UsageDetailsDestination.titleRes),
                canNavigateBack = true,
                navigateUp = navigateBack
            )
        },

    ) { innerPadding ->
        UsageDetailsBody(
            UsageDetailsUiState = uiState.value,
            modifier = modifier.padding(innerPadding)
        )
    }
}

@Composable
private fun UsageDetailsBody(
    usageDetailsUiState: UsageDetailsUiState,
    modifier: Modifier = Modifier,

    ) {
    Column(
        modifier = modifier
            .padding(16.dp)
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
//        Text(
//            text = "Routine ID: ${timerRoutineDetailsUiState.timerRoutineDetails.id}",
//            fontSize = 16.sp,
//            fontWeight = FontWeight.Bold
//        )
//        Spacer(modifier = Modifier.height(8.dp))


    }
}

/*

@Composable
fun NetworkHomeBody(
    awsUiState: AwsUiState,
    retryAction: () -> Unit,
    modifier: Modifier = Modifier
){
    when(awsUiState) {
        is AwsUiState.Loading -> LoadingScreen(modifier)
        is AwsUiState.Success -> NetworkInventoryList(awsUiState.UsageList,  modifier)
        is AwsUiState.Error -> ErrorScreen(retryAction, modifier)
    }
}

@Composable
fun NetworkInventoryList(
    UsageList: List<UsageData>,
    modifier: Modifier = Modifier
){
    LazyColumn(modifier = Modifier, verticalArrangement = Arrangement.spacedBy(8.dp)){
        items( items = UsageList, key = {it.deviceName}){Usage ->
            NetworkDevice(Usage = Usage)
            Divider()
        }
    }
}
*/

