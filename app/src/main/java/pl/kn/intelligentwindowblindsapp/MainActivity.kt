package pl.kn.intelligentwindowblindsapp

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.Button
import pl.kn.intelligentwindowblindsapp.utils.ModuleJson




class MainActivity : AppCompatActivity() {

    private lateinit var scannerBtn : Button
    private lateinit var devicesBtn : Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        scannerBtn = findViewById(R.id.scannerBtn)
        devicesBtn = findViewById(R.id.devicesBtn)

        scannerBtn.setOnClickListener {
            showScannerActivity()
        }

        devicesBtn.setOnClickListener {
            showDevicesActivity()
        }
    }

    private fun showScannerActivity() {
        ModuleJson.getListOfSavedModules(resources)
//        Intent(this, ScannerActivity::class.java).also {
//            startActivity(it)
//        }
    }

    private fun showDevicesActivity() {

    }
}