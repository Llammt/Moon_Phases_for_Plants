package com.example.moonphases

import android.icu.text.SimpleDateFormat
import android.os.Build
import androidx.annotation.RequiresApi
import okhttp3.OkHttpClient
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query
import java.util.Date

object ApiClient {
    val apiClient : ApiService = createRetrofitClient()
}

@RequiresApi(Build.VERSION_CODES.N)
fun getCurrentDate() :String {
    val formatter = SimpleDateFormat("yyyy-MM-dd")
    val currentDate = formatter.format(Date())
    return currentDate
}

@RequiresApi(Build.VERSION_CODES.N)
fun getEndPoint() :String {
    val key :String = BuildConfig.Key
    return "/v1/astronomy.json?key=${key}=${getCurrentDate()}"
}

fun createRetrofitClient() : ApiService {
    return Retrofit.Builder()
        .baseUrl("http://api.weatherapi.com")
        .client(createClient())
        .addConverterFactory(GsonConverterFactory.create())
        .build()
        .create(ApiService::class.java)
}

fun createClient() : OkHttpClient {
    return OkHttpClient()
        .newBuilder()
        .build()
}

interface ApiService{
    @GET("/v1/astronomy.json")
    suspend fun getAstroData(@Query("q") city :String, @Query("dt") currentDate :String, @Query("key") key :String) :Response<ResponseStruct>
    //@GET("/request?data={data}")
    //fun getEndPoint(@Path("data") data: Long): Response<Any>
}

data class ResponseStruct(
    val location: LocationData,
    val astronomy: InfoData
)

data class LocationData(
    val name: String,
    val region: String,
    val country: String,
    val lat: Double,
    val lon: Double,
    val tz_id: String,
    val localtime_epoch: Long,
    val localtime: String
)

data class InfoData(
    val astro: AstroData
)

data class AstroData(
    val sunrise: String,
    val sunset: String,
    val moonrise: String,
    val m: String,
    val moon_phase: String,
    val moon_illumination: Int,
    val is_moon_up: Int,
    val is_sun_up: Int
)