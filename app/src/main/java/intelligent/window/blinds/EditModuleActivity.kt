package intelligent.window.blinds

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import intelligent.window.blinds.api.weather.ApiInterface
import intelligent.window.blinds.api.weather.WeatherConstants
import intelligent.window.blinds.api.weather.WeatherData
import intelligent.window.blinds.api.weather.ZipcodeData
import intelligent.window.blinds.room.Module
import intelligent.window.blinds.room.ModuleEntity
import intelligent.window.blinds.room.ModuleViewModel
import intelligent.window.blinds.utils.*
import kotlinx.coroutines.DelicateCoroutinesApi
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import setRequest
import java.net.DatagramSocket

class EditModuleActivity : AppCompatActivity() {

    private val TAG = "EditModuleActivity"
    private val PORT = 4210
    private val TIMEOUT = 1000

    private lateinit var rootLayout: LinearLayout
    private lateinit var editLayout: View
    private var layoutOption: Int = 0

    private lateinit var moduleNameText: TextView
    @SuppressLint("UseSwitchCompatOrMaterialCode")
    private lateinit var adaptiveSwitcher: Switch

    private lateinit var latValueText: TextView
    private lateinit var lonValueText: TextView
    private lateinit var getWeatherButton: Button
    private lateinit var zipCodeText: EditText
    private lateinit var cityValueText: TextView
    private lateinit var tempValueText: TextView
    private lateinit var cloudValueText: TextView
    private lateinit var serValueText: TextView
    private lateinit var weatherImage: ImageView
    private lateinit var setSerValueButton: Button

    private lateinit var module: Module
    private lateinit var mModuleViewModel: ModuleViewModel

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_module)

        rootLayout = findViewById(R.id.root_edit_layout)
        module = intent.extras?.get("EXTRA_MODULE") as Module
        mModuleViewModel = ViewModelProvider(this).get(ModuleViewModel::class.java)
        Log.d(TAG, "Edit Module: ${module.toString()}")

        moduleNameText = findViewById(R.id.current_module_name)
        adaptiveSwitcher = findViewById(R.id.adaptiveSwitch)

        val ip = module.ipAddress.toString().drop(1)
        val idHex = "0x" + Integer.toHexString(module.id.toInt())

        moduleNameText.text = "$ip:$idHex"
        adaptiveSwitcher.isChecked = module.isAdaptive
        setUpProperLayout()

        adaptiveSwitcher.setOnCheckedChangeListener { buttonView, isChecked ->
            module.isAdaptive = isChecked
            mModuleViewModel.updateAdaptiveColumn(module.isAdaptive.toInt(), module.id)
            rootLayout.removeViewAt(2)
            Log.d(TAG, "UPDATED MODULES: ${mModuleViewModel.readAllData}")
            setUpProperLayout()
        }
    }

    override fun onResume() {
        super.onResume()
        Log.d(TAG, "ON RESUME")
    }

    private fun setUpProperLayout() {
        inflateProperLayout()
        when(getLayoutOption()){
            R.layout.edit_manual_layout -> setUpManualLayout()
            else -> setUpAdaptiveLayout()
        }
    }

    private fun getLayoutOption(): Int {
        return if (module.isAdaptive) R.layout.edit_adaptive_layout else R.layout.edit_manual_layout
    }

    private fun inflateProperLayout() {
        // TODO
        rootLayout.addView(layoutInflater.inflate(getLayoutOption(), rootLayout, false))
    }

    @DelicateCoroutinesApi
    @SuppressLint("SetTextI18n")
    private fun setUpManualLayout() {

        val levelPicker: NumberPicker = findViewById(R.id.ser_level_picker)
        val lightLevel: TextView = findViewById(R.id.light_level_value)

        levelPicker.maxValue = 100
        levelPicker.minValue = 0
        levelPicker.value = convertLevelToPercentage(module.ser.toPositiveInt())

        lightLevel.text = convertLevelToPercentage(module.phr.toPositiveInt()).toString() + "%"

        findViewById<Button>(R.id.set_level_button).setOnClickListener {
            updateModule(levelPicker.value)
        }

        findViewById<Button>(R.id.get_level_button).setOnClickListener {
//            getCurrentState(levelPicker)
        }
    }

    @DelicateCoroutinesApi
    private fun updateModule(serValue: Int) {

        val serLevel = convertPercentageToLevel(serValue).toByte()
        val updatedModule = ModuleEntity(module.id, module.ipAddress.toString().drop(1), module.phr, serLevel, module.isAdaptive)
        mModuleViewModel.updateModule(updatedModule)

        val thread = Thread {
            try {
                val socket = DatagramSocket(PORT)
               setRequest(convertEntityToModule(updatedModule), socket, PORT, newSER = serLevel)
                Log.d(TAG, "SEND PACKET")
                socket.close()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
        thread.start()

        Log.d(TAG, "Updated Module to: ${updatedModule.toString()}")
        Toast.makeText(this, "Module Updated!", Toast.LENGTH_SHORT).show()
    }

//    private fun getCurrentState(levelPicker: NumberPicker) {
//        var getReqModule: Module? = null
//        val thread = Thread {
//            try {
//                val socket = DatagramSocket(PORT)
//                getReqModule = getRequest(module, socket, PORT, 5000)
//                Log.d(TAG, "GET REQ")
//                socket.close()
//            } catch (e: Exception) {
//                e.printStackTrace()
//            }
//        }
//        Log.d(TAG, "GET REQUEST MODULES: $getReqModule")
//        levelPicker.value = convertLevelToPercentage(getReqModule!!.ser.toInt())
//        findViewById<TextView>(R.id.light_level_value).text = convertLevelToPercentage(getReqModule!!.phr.toInt()).toString()
//        Toast.makeText(this, "Value updated!", Toast.LENGTH_SHORT).show()
//    }

    private fun setUpAdaptiveLayout() {
        latValueText = findViewById(R.id.lat_value)
        lonValueText = findViewById(R.id.lon_value)
        getWeatherButton = findViewById(R.id.get_weather_button)
        zipCodeText = findViewById(R.id.zipcode_value)
        cityValueText = findViewById(R.id.city_value)
        tempValueText = findViewById(R.id.temp_value)
        cloudValueText = findViewById(R.id.cloud_value)
        serValueText = findViewById(R.id.ser_value)
        weatherImage = findViewById(R.id.weather_image)
        setSerValueButton = findViewById(R.id.set_ser_value)

        getWeatherButton.setOnClickListener {
            setWeatherData(zipCodeText.text.toString())
        }
    }

    @DelicateCoroutinesApi
    private fun setWeatherData(zipcode: String) {
        val retrofitBuilder = Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl("https://api.openweathermap.org/geo/1.0/")
            .build()
            .create(ApiInterface::class.java)

        val retrofitData = retrofitBuilder.getZipcodeData(zipcode = "$zipcode,pl", appId = WeatherConstants.API_KEY)
        retrofitData.enqueue(object : Callback<ZipcodeData?> {
            @SuppressLint("SetTextI18n")
            override fun onResponse(call: Call<ZipcodeData?>, response: Response<ZipcodeData?>) {
                val responseBody = response.body()
                latValueText.text = responseBody?.lat.toString()
                lonValueText.text = responseBody?.lon.toString()
                cityValueText.text = "${responseBody?.name},${responseBody?.country}"
                Log.d(TAG, responseBody.toString())
                getFullWeatherData(responseBody!!)
            }

            override fun onFailure(call: Call<ZipcodeData?>, t: Throwable) {
                println("FAILURE")
            }
        })
    }

    @DelicateCoroutinesApi
    private fun getFullWeatherData(data: ZipcodeData) {
        val retrofitBuilder2 = Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl("https://api.openweathermap.org/data/2.5/")
            .build()
            .create(ApiInterface::class.java)

        val retrofitData2 = retrofitBuilder2.getWeatherData(lat = data.lat.toString(), lon = data.lon.toString(), appId = WeatherConstants.API_KEY, units = "metric")
        retrofitData2.enqueue(object : Callback<WeatherData?> {
            @SuppressLint("SetTextI18n")
            override fun onResponse(call: Call<WeatherData?>, response: Response<WeatherData?>) {
                val responseBody = response.body()
                tempValueText.text = responseBody?.main?.temp.toString()
                cloudValueText.text = "Cloud %: " + responseBody?.clouds?.all.toString()
                val id: String = responseBody!!.weather[0].icon
                weatherImage.setImageResource(setWeatherImage(id))
                val calculatedSer = calculateSer(id, responseBody.main.temp, responseBody.clouds.all)
                serValueText.text = "Window Blind should be: " + calculatedSer.toString() +"%"
                setSerValueButton.setOnClickListener {
                    updateModule(calculatedSer)
                }
            }

            override fun onFailure(call: Call<WeatherData?>, t: Throwable) {
                println(t.cause)
            }
        })
    }

    private fun setWeatherImage(id: String?): Int {
        return when(id){
            "01d" -> R.drawable.sun
            "02d" -> R.drawable.sun_and_cloud
            "03d" -> R.drawable.cloud
            "04d" -> R.drawable.cloud_black
            "09d" -> R.drawable.rain_cloud
            "10d" -> R.drawable.rain_sun
            "11d" -> R.drawable.thunderstorm
            else -> R.drawable.sun_and_cloud
        }
    }

}