package intelligent.window.blinds


import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import java.net.InetAddress
import intelligent.window.blinds.room.Module as iwbuModule


class MainActivity : AppCompatActivity() {

    private lateinit var scannerButton: Button
    private lateinit var moduleList: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)
        var savedModules = getSavedModules()

        moduleList = findViewById(R.id.saved_modules_list)

        if (savedModules.isNotEmpty()) {

            var savedTitle = findViewById<TextView>(R.id.saved_modules_title)
            savedTitle.visibility = View.VISIBLE

            for (module in savedModules) {
                // TODO("Add saved modules to list")
            }
        }

        scannerButton = findViewById(R.id.scanner_button)

        scannerButton.setOnClickListener {
            getNetworkModules()
        }
    }

    private fun getSavedModules(): List<iwbuModule> {
        // TODO("Open database and select all saved modules")
        return listOf(iwbuModule(0x1234.toShort(), InetAddress.getByName("10.10.10.10"), 0xBB.toByte(), 0xCC.toByte()))
    }

    private fun getNetworkModules(): List<iwbuModule> {
        // TODO("Scan network modules")
        return listOf(iwbuModule(0x4321.toShort(), InetAddress.getByName("20.20.20.20"), 0xBB.toByte(), 0xCC.toByte()))
    }

    private fun saveModule() {
        // TODO("Save module")
    }

    private fun editModule() {
        // TODO("Edit module, i.e. change local name, ID or delete from saved")
    }

}