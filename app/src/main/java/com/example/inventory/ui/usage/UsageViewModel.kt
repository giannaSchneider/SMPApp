//package com.example.inventory.ui.usage
//
//import androidx.compose.runtime.getValue
//import androidx.compose.runtime.mutableStateOf
//import androidx.compose.runtime.setValue
//import androidx.lifecycle.SavedStateHandle
//import androidx.lifecycle.ViewModel
//import androidx.lifecycle.viewModelScope
//import com.example.smartpowerconnector_room.internet.idata.AwsRepository
//import com.example.smartpowerconnector_room.internet.idata.UsageData
//import kotlinx.coroutines.flow.SharingStarted
//import kotlinx.coroutines.flow.StateFlow
//import kotlinx.coroutines.flow.filterNotNull
//import kotlinx.coroutines.flow.map
//import kotlinx.coroutines.flow.stateIn
//import kotlinx.coroutines.launch
//import retrofit2.HttpException
//import java.io.IOException
//
///**
// * ViewModel to retrieve, update and delete an timerRoutine from the [RoutinesRepository]'s data source.
// */
//sealed interface AwsUiState{
//    data class Success(val iDeviceList: List<UsageData>): AwsUiState
//    object Error: AwsUiState
//    object Loading: AwsUiState
//    //object Updated: AwsUiState
//}
//
//
//class UsageViewModel(private val awsRepository: AwsRepository): ViewModel() {
//    var awsUiState: AwsUiState by mutableStateOf(AwsUiState.Loading)
//        private set
//
//    init{
//        getAllDevices()
//    }
//
//    fun getAllDevices(){
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
//
//}
//
