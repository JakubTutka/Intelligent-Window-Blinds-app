package intelligent.window.blinds

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import intelligent.window.blinds.room.Module
import intelligent.window.blinds.room.ModuleEntity
import intelligent.window.blinds.room.ModuleViewModel
import intelligent.window.blinds.utils.convertEntityToModule
import intelligent.window.blinds.utils.convertLevelToPercentage
import intelligent.window.blinds.utils.convertPercentageToLevel
import intelligent.window.blinds.utils.toPositiveInt
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
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
    }

    override fun onResume() {
        super.onResume()
        Log.d(TAG, "ON RESUME")
        inflateProperLayout()
//        when(layoutOption){
//            R.layout.edit_manual_layout -> setUpManualLayout()
//            else ->
//        }
        setUpManualLayout()
    }

    override fun onPause() {
        super.onPause()
    }

    private fun inflateProperLayout() {
        // TODO
        layoutOption = if (module.isAdaptive) 1 else R.layout.edit_manual_layout
        rootLayout.addView(layoutInflater.inflate(layoutOption, rootLayout, false))
    }

    @SuppressLint("SetTextI18n")
    private fun setUpManualLayout() {

        val levelPicker: NumberPicker = findViewById(R.id.ser_level_picker)
        val lightLevel: TextView = findViewById(R.id.light_level_value)

        levelPicker.maxValue = 100
        levelPicker.minValue = 0
        levelPicker.value = convertLevelToPercentage(module.ser.toPositiveInt())

        lightLevel.text = convertLevelToPercentage(module.phr.toPositiveInt()).toString() + "%"

        findViewById<Button>(R.id.set_level_button).setOnClickListener {
            updateModule(levelPicker)
        }
    }

    @DelicateCoroutinesApi
    private fun updateModule(levelPicker: NumberPicker) {

        val serLevel = convertPercentageToLevel(levelPicker.value).toByte()
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
}