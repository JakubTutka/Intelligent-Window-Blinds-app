package intelligent.window.blinds


import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import intelligent.window.blinds.utils.ModuleJson
import java.net.InetAddress
import intelligent.window.blinds.utils.Module as iwbuModule


class MainActivity : AppCompatActivity() {

    private lateinit var scannerButton : Button
    private lateinit var moduleList : RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        var savedModules = getSavedModules()

        moduleList = findViewById(R.id.saved_modules_list)

        if (!savedModules.isEmpty()) {

            var savedTitle = findViewById<TextView>(R.id.saved_modules_title)
            savedTitle.visibility = View.VISIBLE

            for (module in savedModules) {
                // TODO("Add saved modules to list")
            }
        }

        setContentView(R.layout.activity_main)


        scannerButton = findViewById(R.id.scanner_button)

        scannerButton.setOnClickListener {
            getNetworkModules()
        }
    }

    private fun showScannerActivity() {
        ModuleJson.getListOfSavedModules(resources)
//        Intent(this, ScannerActivity::class.java).also {
//            startActivity(it)
//        }
    }

    private fun getSavedModules(): List<iwbuModule> {
        // TODO("Open database and select all saved modules")
        return listOf(iwbuModule(InetAddress.getByName("10.10.10.10"), 0x1234.toShort(), 0xBB.toByte(), 0xCC.toByte()))
    }

    private fun getNetworkModules(): List<iwbuModule> {
        // TODO("Scan network modules")
        return listOf(iwbuModule(InetAddress.getByName("20.20.20.20"), 0x4321.toShort(), 0xBB.toByte(), 0xCC.toByte()))
    }

    private fun saveModule() {
        // TODO("Save module")
    }

    private fun editModule() {
        // TODO("Edit module, i.e. change local name, ID or delete from saved")
    }

}