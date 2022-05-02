package pl.kn.intelligentwindowblindsapp

import Module
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.widget.ArrayAdapter
import android.widget.ListView
import android.widget.RelativeLayout
import java.net.DatagramSocket
import java.net.InetAddress


class ScannerActivity : AppCompatActivity() {

    private val port = 4210
    private lateinit var socket: DatagramSocket
    private lateinit var circle: RelativeLayout
    private lateinit var rootLayout: RelativeLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_scanner)
        socket = DatagramSocket(port)
        circle = findViewById(R.id.loadingPanel)
        rootLayout = findViewById(R.id.rootLayout)
    }

    override fun onResume() {
        super.onResume()
        Handler(Looper.getMainLooper()).postDelayed({
            circle.visibility = View.GONE;
            showListOfDevices()
        }, 3000)
    }

    override fun onPause() {
        super.onPause()
        socket.close()
    }

    private fun showListOfDevices() {
        val newID: Short = 0x1234.toShort()
        val newSER: Byte = 0xAB.toByte()
        val newPHR: Byte = 0x4.toByte()
        val addr = InetAddress.getByName("192.168.1.1")

//        var example = Module(addr, newID, newPHR, newSER)
//
//        val list = ListView(this)
//        val items = arrayOf(addr.toString())
//
//        val itemsAdapter: ArrayAdapter<String> =
//            ArrayAdapter(this, R.layout.list_scanned_devices, items)
//
//        list.adapter = itemsAdapter
//        rootLayout.addView(list)
    }
}
