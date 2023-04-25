//package com.example.smartpowerconnector_room.internet.idata
//
//import androidx.compose.foundation.Image
//import androidx.compose.foundation.layout.*
//import androidx.compose.material.Button
//import androidx.compose.material.Text
//import androidx.compose.runtime.Composable
//import androidx.compose.runtime.getValue
//import androidx.compose.runtime.mutableStateOf
//import androidx.compose.runtime.setValue
//import androidx.compose.ui.Alignment
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.res.painterResource
//import androidx.compose.ui.res.stringResource
//import androidx.compose.ui.unit.dp
//import androidx.lifecycle.ViewModel
//import androidx.lifecycle.viewModelScope
//import com.example.inventory.R
//import kotlinx.coroutines.launch
//import retrofit2.HttpException
//import java.io.IOException
//
//sealed interface AwsUiState{
//    data class Success(val UsageList: List<UsageData>): AwsUiState
//    object Error: AwsUiState
//    object Loading: AwsUiState
//    object Updated: AwsUiState
//}
//
//class NetworkHomeViewModel2(private val awsRepository: AwsRepository): ViewModel() {
//    var awsUiState: AwsUiState by mutableStateOf(AwsUiState.Loading)
//        private set
//
//    init{
//        getAllUsage()
//    }
//
//    fun getAllUsage(){
//        viewModelScope.launch {
//            awsUiState = AwsUiState.Loading
//            awsUiState = try{
//                AwsUiState.Success(awsRepository.getAllUsage())
//            }catch (e: IOException){
//                AwsUiState.Error
//            }catch(e: HttpException) {
//                AwsUiState.Error
//            }
//        }
//    }
//
///*    fun onOffSwitch(deviceData: DeviceData) {
//        viewModelScope.launch {
//            awsUiState = AwsUiState.Loading
//            awsUiState = try {
//                awsRepository.onOffSwitch(deviceData)
//                AwsUiState.Success(awsRepository.getAllDevices()) // refresh the device list after updating
//            } catch (e: IOException) {
//                AwsUiState.Error
//            } catch (e: HttpException) {
//                AwsUiState.Error
//            }
//        }
//    }*/
//}
///*
//@Composable
//fun ErrorScreen(retryAction: () -> Unit, modifier: Modifier = Modifier) {
//    Column(
//        modifier = modifier.fillMaxSize(),
//        verticalArrangement = Arrangement.Center,
//        horizontalAlignment = Alignment.CenterHorizontally
//    ) {
//        Text(stringResource(R.string.loading_failed))
//        Button(onClick = retryAction) {
//            Text(stringResource(R.string.retry))
//        }
//    }
//}
//
//@Composable
//fun LoadingScreen(modifier: Modifier = Modifier) {
//    Box(
//        contentAlignment = Alignment.Center,
//        modifier = modifier.fillMaxSize()
//    ) {
//        Image(
//            modifier = Modifier.size(200.dp),
//            painter = painterResource(R.drawable.loading_img),
//            contentDescription = stringResource(R.string.loading)
//        )
//    }
//
//
//}
//*/
