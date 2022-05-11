package intelligent.window.blinds.api.weather

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiInterface {

    // https://api.openweathermap.org/geo/1.0

    @GET("zip")
    fun getZipcodeData(@Query("zip") zipcode: String, @Query("appid") appId: String): Call<ZipcodeData>

    @GET("weather")
    fun getWeatherData(@Query("lat") lat: String, @Query("lon") lon: String, @Query("appid") appId: String, @Query("units") units: String): Call<WeatherData>
}