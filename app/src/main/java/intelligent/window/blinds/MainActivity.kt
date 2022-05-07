package intelligent.window.blinds


import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import intelligent.window.blinds.adapters.NetworkModulesAdapter
import intelligent.window.blinds.adapters.SavedModulesAdapter
import intelligent.window.blinds.room.ModuleViewModel
import intelligent.window.blinds.utils.convertListEntityToModule
import java.net.InetAddress
import intelligent.window.blinds.room.Module as iwbuModule


class MainActivity : AppCompatActivity() {

    private lateinit var scannerButton: Button
    private lateinit var moduleList: RecyclerView
    private lateinit var networkModuleList: RecyclerView
    private lateinit var mModuleViewModel: ModuleViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val savedModules = getSavedModules()
        moduleList = findViewById(R.id.saved_modules_list)
        networkModuleList = findViewById(R.id.network_modules_list)
        mModuleViewModel = ViewModelProvider(this).get(ModuleViewModel::class.java)
        setUpSavedModules()
        scannerButton = findViewById(R.id.scanner_button)
        scannerButton.setOnClickListener {
            setUpNetworkModules()
        }

//        if (savedModules.isNotEmpty()) {
//
//            var savedTitle = findViewById<TextView>(R.id.saved_modules_title)
//            savedTitle.visibility = View.VISIBLE
//
//            for (module in savedModules) {
//                // TODO("Add saved modules to list")
//            }
//        }

    }

    private fun getSavedModules(): List<iwbuModule> {
        // TODO("Open database and select all saved modules")
        return listOf(iwbuModule(0x1234.toShort(), InetAddress.getByName("10.10.10.10"), 0xBB.toByte(), 0xCC.toByte()))
    }

    private fun getNetworkModules(): MutableList<iwbuModule> {
        // TODO("Scan network modules")
        val list : MutableList<iwbuModule> = mutableListOf()
        list.add(iwbuModule(0x1.toShort(), InetAddress.getByName("20.20.20.20"), 0x1.toByte(), 0x2.toByte()))
        list.add(iwbuModule(0x2.toShort(), InetAddress.getByName("30.30.30.30"), 0x14.toByte(), 0xF.toByte()))
        list.add(iwbuModule(0x3.toShort(), InetAddress.getByName("30.30.30.30"), 0xFF.toByte(), 0xA.toByte()))
        list.add(iwbuModule(0x4.toShort(), InetAddress.getByName("30.30.30.30"), 0xFF.toByte(), 0xA.toByte()))
        list.add(iwbuModule(0x4.toShort(), InetAddress.getByName("30.30.30.30"), 0xFF.toByte(), 0xA.toByte()))

        return list
    }

    private fun setUpNetworkModules(): Unit {
        networkModuleList.adapter = NetworkModulesAdapter(getNetworkModules(), mModuleViewModel)
        networkModuleList.layoutManager = LinearLayoutManager(this)
    }

    private fun setUpSavedModules(): Unit {
        val adapter = SavedModulesAdapter(mModuleViewModel)
        moduleList.adapter = adapter
        moduleList.layoutManager = LinearLayoutManager(this)

        mModuleViewModel.readAllData.observe(this, Observer { module ->
           adapter.setData(convertListEntityToModule(module))
        })
    }
    private fun saveModule() {
        // TODO("Save module")
    }

    private fun editModule() {
        // TODO("Edit module, i.e. change local name, ID or delete from saved")
    }

}