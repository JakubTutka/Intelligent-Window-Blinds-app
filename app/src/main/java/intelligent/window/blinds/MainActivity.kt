package intelligent.window.blinds


import android.os.Bundle
import android.util.Log
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import intelligent.window.blinds.adapters.NetworkModulesAdapter
import intelligent.window.blinds.adapters.SavedModulesAdapter
import intelligent.window.blinds.room.ModuleViewModel
import intelligent.window.blinds.utils.convertEntityToModule
import intelligent.window.blinds.utils.convertListEntityToModule
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import receiveBroadcastModules
import java.net.DatagramSocket
import java.net.InetAddress
import intelligent.window.blinds.room.Module as iwbuModule


class MainActivity : AppCompatActivity() {

    private val TAG = "MainActivity"
    private val PORT = 4210
    private val TIMEOUT = 1000

    private lateinit var scannerButton: Button
    private lateinit var moduleList: RecyclerView
    private lateinit var networkModuleList: RecyclerView
    private lateinit var mModuleViewModel: ModuleViewModel
    private lateinit var idList: List<Short>
    private val socket: DatagramSocket = DatagramSocket(PORT)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        moduleList = findViewById(R.id.saved_modules_list)
        networkModuleList = findViewById(R.id.network_modules_list)

        mModuleViewModel = ViewModelProvider(this).get(ModuleViewModel::class.java)
        setUpSavedModules()

        scannerButton = findViewById(R.id.scanner_button)
        scannerButton.setOnClickListener {
            setUpNetworkModules()
        }
    }

    override fun onPause() {
        super.onPause()
        socket.close()
    }

    @DelicateCoroutinesApi
    private fun getNetworkModules(): MutableList<iwbuModule> {
        // TODO("Scan network modules")

        var list: List<iwbuModule> = emptyList()


        val thread = Thread {
            try {
                val modulesMap = receiveBroadcastModules(TIMEOUT, socket)
                list = modulesMap.values.toList()
                Log.d(TAG, "GET LIST: $list")
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
        thread.start()

        list = getSampleListOfModules()
        val listOfNetworkModules: MutableList<iwbuModule> = mutableListOf()

        for (module in list) {
            if (!idList.contains(module.id)) {
                listOfNetworkModules.add(module)
            }
        }
        return listOfNetworkModules
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
            Log.d(TAG, "Data changed.")
           adapter.setData(convertListEntityToModule(module))
        })

        mModuleViewModel.readAllId.observe(this, Observer {
            this.idList = it
        })
    }

    fun getSampleListOfModules(): List<iwbuModule> {
        val list : MutableList<iwbuModule> = mutableListOf()
        list.add(iwbuModule(0x1.toShort(), InetAddress.getByName("20.20.20.20"), 0x80.toByte(), 0x80.toByte()))
        list.add(iwbuModule(0x2.toShort(), InetAddress.getByName("30.30.30.30"), 0x14.toByte(), 0xF.toByte()))
        list.add(iwbuModule(0x3.toShort(), InetAddress.getByName("30.30.30.30"), 0xFF.toByte(), 0xA.toByte()))
        list.add(iwbuModule(0x4.toShort(), InetAddress.getByName("30.30.30.30"), 0xFF.toByte(), 0xA.toByte()))
        return list
    }

}