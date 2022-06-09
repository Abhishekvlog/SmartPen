package com.dexter.blepoc

import android.Manifest.permission.*
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothManager
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.smart.pen.core.common.Listeners.OnScanDeviceListener
import com.smart.pen.core.model.DeviceObject
import com.smart.pen.core.services.PenService
import com.smart.pen.core.symbol.Keys
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {

    lateinit var service: PenService
    var list = mutableListOf<DeviceObject>()
//    lateinit var penAdapter: PenAdapter
    lateinit var arrayAdapter: ArrayAdapter<DeviceObject>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermissions(
                arrayOf(
                    READ_EXTERNAL_STORAGE, BLUETOOTH, BLUETOOTH_ADMIN,
                    ACCESS_FINE_LOCATION, ACCESS_COARSE_LOCATION, WRITE_EXTERNAL_STORAGE
                ), 101
            )
        }

//        penAdapter = PenAdapter(list)
        btn_scan.setOnClickListener {
            service = SmartPenApplication.getInstance().penService
            if (service != null)
                service.scanDevice(onScanDeviceListener)
            else Toast.makeText(this, "Faield", Toast.LENGTH_SHORT).show()
        }
        Log.d("qwert", "oncCreate: $list")
//        setAdapter(list)

        arrayAdapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, list)
        list_view.setAdapter(arrayAdapter)
        list_view.onItemClickListener = AdapterView.OnItemClickListener { _, _, position, _ ->

            val device: DeviceObject = list[position]
            val address: String = device.address

            val intent = Intent(this, Select_Device_Activity::class.java)
            intent.putExtra(Keys.KEY_DEVICE_ADDRESS, address)
            startActivity(intent)
        }

    }

//    private fun setAdapter(list: List<DeviceObject>) {
//        recycler_view.adapter = PenAdapter(list)
//        recycler_view.layoutManager = LinearLayoutManager(this)
//    }

    private val onScanDeviceListener: OnScanDeviceListener = object : OnScanDeviceListener {
        override fun find(device: DeviceObject) {
            list.clear()
            btn_scan.isEnabled = false
            btn_scan.text = "Scanning.."
            list.add(device)
            arrayAdapter.notifyDataSetChanged()
            Log.d("list_check", "device: ${device.name}")
            Log.d("list_check", "list: $list")
        }

        override fun complete(list: HashMap<String, DeviceObject>) {
            btn_scan.setText("Start Scan")
            btn_scan.setEnabled(true)

        }
    }

    override fun onResume() {
        super.onResume()
        if (!SmartPenApplication.getInstance().isBindPenService) {
            SmartPenApplication.getInstance().bindPenService(Keys.APP_PEN_SERVICE_NAME)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        service = SmartPenApplication.getInstance().penService
        service.stopScanDevice()
        SmartPenApplication.getInstance().stopPenService(Keys.APP_PEN_SERVICE_NAME)
    }
}