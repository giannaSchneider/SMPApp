

package com.example.inventory.data

import android.content.Context
//import com.example.smartpowerconnector_room.internet.idata.AwsApiService
//import com.example.smartpowerconnector_room.internet.idata.AwsRepository
//import com.example.smartpowerconnector_room.internet.idata.NetworkDeviceRepository2
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MediaType.Companion.toMediaType
import retrofit2.Retrofit
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory


/**
 * App container for Dependency injection.
 */
interface AppContainer {
    val itemsRepository: ItemsRepository
    val routinesRepository: RoutinesRepository
    val clockRoutineRepository: ClockRoutineRepository
    val multiRoutineRepository: MultiRoutineRepository
    val mixRoutineRepository: MixRoutineRepository
    //val awsRepository: AwsRepository


}

/**
 * [AppContainer] implementation that provides instance of [OfflineItemsRepository]
 */
class AppDataContainer(private val context: Context) : AppContainer {
    /**
     * Implementation for [ItemsRepository]
     */
    override val itemsRepository: ItemsRepository by lazy {
        OfflineItemsRepository(InventoryDatabase.getDatabase(context).itemDao())
    }
    override val routinesRepository: RoutinesRepository by lazy {
        OfflineRoutinesRepository(RoutineDatabase.getDatabase(context).routineDao())
    }
    override val clockRoutineRepository: ClockRoutineRepository by lazy {
        OfflineClockRoutineRepository(ClockRoutineDatabase.getDatabase(context).ClockRoutineDao())
    }
    override val multiRoutineRepository: MultiRoutineRepository by lazy {
        OfflineMultiRoutineRepository(MultiRoutineDatabase.getDatabase(context).MultiRoutineDao())
    }
    override val mixRoutineRepository: MixRoutineRepository by lazy {
        OfflineMixRoutineRepository(MixRoutineDatabase.getDatabase(context).MixRoutineDao())
    }
//
//    // Internet Portion of the app container
//    //val BASE_URL = "https://tlyr3e3uvd.execute-api.us-east-2.amazonaws.com/"
//    val BASE_URL = "https://aaeh7rnpl1.execute-api.us-east-1.amazonaws.com/"
//
//    //Use the Retrofit builder to build a retrofit object using a kotlinx.serialization converter
//    private val retrofit: Retrofit = Retrofit.Builder()
//        .addConverterFactory(Json.asConverterFactory("application/json".toMediaType()))
//        .baseUrl(BASE_URL)
//        .build()
//
//    /*private val retrofit2 = Retrofit.Builder()
//        .addConverterFactory(GsonConverterFactory.create())
//        .baseUrl(com.example.smartpowerconnector_room.internet.idata.BASE_URL)
//        .build()
//*/
//    //Retrofit service object for creating api calls
//    private val retrofitService: AwsApiService by lazy{
//        retrofit.create(AwsApiService::class.java)
//    }
//
//    override val awsRepository: AwsRepository by lazy {
//        NetworkDeviceRepository2(retrofitService)
//    }
}