//package com.example.smartpowerconnector_room.internet.idata
//
//import android.util.Log
//import retrofit2.Call
//import retrofit2.Callback
//import retrofit2.Response
//import java.io.IOException
//
//
//interface AwsRepository {
//    suspend fun getAllUsage(): List<UsageData>
//    //suspend fun onOffSwitch(/*string: String,*/ usageData: UsageData): Call<UsageData>
//}
//
//class NetworkDeviceRepository2(
//    private val awsApiService: AwsApiService
//):AwsRepository{
//    override suspend fun getAllUsage(): List<UsageData> = awsApiService.getAllUsage()
//
//    /*override suspend fun onOffSwitch(usageData: UsageData): Call<UsageData> {
//        try {
//            awsApiService.onOffSwitch(usageData).enqueue(object : Callback<UsageData> {
//                override fun onResponse(call: Call<UsageData>, response: Response<UsageData>) {
//                    if (response.isSuccessful) {
//                        response.body()?.let {
//                            return Unit
//                        }
//                    } else {
//                        // handle error case here
//                        val errorBody = response.errorBody()?.string()
//                        Log.e("DeviceDetailsViewModel", "Error: $errorBody")
//                    }
//                }
//
//                override fun onFailure(call: Call<UsageData>, t: Throwable) {
//                    // handle network error here
//                    Log.e("DeviceDetailsViewModel", "Network error: ${t.message}")
//                }
//            })
//        } catch (e: IOException) {
//            // handle network error here
//            Log.e("DeviceDetailsViewModel", "Network error: ${e.message}")
//        }
//        return awsApiService.onOffSwitch(usageData)
//    }*/
//}
