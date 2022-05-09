package intelligent.window.blinds

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.*
import intelligent.window.blinds.room.Module
import intelligent.window.blinds.utils.convertLevelToPercentage
import intelligent.window.blinds.utils.toPositiveInt
import org.w3c.dom.Text

class EditModuleActivity : AppCompatActivity() {

    private val TAG = "EditModuleActivity"
    private lateinit var rootLayout: LinearLayout
    private lateinit var editLayout: View
    private var layoutOption: Int = 0

    private lateinit var moduleNameText: TextView
    @SuppressLint("UseSwitchCompatOrMaterialCode")
    private lateinit var adaptiveSwitcher: Switch
    private lateinit var idText: EditText

    private lateinit var module: Module

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_module)

        rootLayout = findViewById(R.id.root_edit_layout)
        module = intent.extras?.get("EXTRA_MODULE") as Module
        Log.d(TAG, "Edit Module: ${module.toString()}")

        moduleNameText = findViewById(R.id.current_module_name)
        adaptiveSwitcher = findViewById(R.id.adaptiveSwitch)
        idText = findViewById(R.id.edit_id)

        val ip = module.ipAddress.toString().drop(1)
        val idHex = "0x" + Integer.toHexString(module.id.toInt())

        moduleNameText.text = "$ip:$idHex"
        adaptiveSwitcher.isChecked = true
        val id = module.id.toInt().toString()
        idText.text = Editable.Factory.getInstance().newEditable(id)

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

    private fun inflateProperLayout() {
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
    }
}