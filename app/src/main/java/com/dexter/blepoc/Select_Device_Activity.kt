package com.dexter.blepoc

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.smart.pen.core.common.Listeners.OnConnectStateListener
import com.smart.pen.core.services.PenService
import com.smart.pen.core.services.SmartPenService
import com.smart.pen.core.symbol.ConnectState
import com.smart.pen.core.symbol.Keys
import kotlinx.android.synthetic.main.activity_select_device.*

class Select_Device_Activity : AppCompatActivity() {

    lateinit var service: PenService

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_select_device)

        service = SmartPenApplication.getInstance().penService

        val address = intent.getStringExtra(Keys.KEY_DEVICE_ADDRESS)
        if (address != null && !address.isEmpty()) {
            connectDevice(address)
        }else{
            Toast.makeText(this, "address is not found", Toast.LENGTH_SHORT).show()
        }

        btn_disconnect.setOnClickListener {
            val service = SmartPenApplication.getInstance().penService
            service?.disconnectDevice()
            startActivity(Intent(this,MainActivity::class.java))
        }
        btn_get_data.setOnClickListener {
            val mPenServiceReceiver = PenServiceReceiver()
            val intentFilter = IntentFilter()
            intentFilter.addAction(Keys.ACTION_SERVICE_SEND_POINT)
            registerReceiver(mPenServiceReceiver, intentFilter)
        }
    }

    private fun connectDevice(address: String) {
        val service = SmartPenApplication.getInstance().penService
        if (service != null) {
            val state = (service as SmartPenService).connectDevice(onConnectStateListener, address)
            if (state != ConnectState.CONNECTING) {
                Toast.makeText(this, "can't connect", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "connecting", Toast.LENGTH_SHORT).show()
            }
        }
    }
    private val onConnectStateListener =
        OnConnectStateListener { address, state ->
            when (state) {
                ConnectState.PEN_READY -> {}
                ConnectState.PEN_INIT_COMPLETE -> {
                    progress_bar.visibility = View.GONE
                    btn_get_data.visibility = View.VISIBLE
                    btn_disconnect.visibility = View.VISIBLE
                    Toast.makeText(this, "Connected", Toast.LENGTH_LONG).show()
                }
                ConnectState.CONNECTED -> {}
                ConnectState.SERVICES_FAIL -> {
                    progress_bar.visibility = View.GONE
                    Log.e("abhi", "The pen service discovery failed. ")

                }
                ConnectState.CONNECT_FAIL -> {
                    progress_bar.visibility = View.GONE
                    Log.e("abhi", "The pen service connection failure. ")
                }
                ConnectState.DISCONNECTED -> {
                    progress_bar.visibility = View.GONE
                    Toast.makeText(this, "Disconnected", Toast.LENGTH_SHORT
                    ).show()
                }
                else -> {}
            }
        }

    override fun onResume() {
        super.onResume()
        btn_disconnect.visibility = View.GONE
    }

    override fun onDestroy() {
        super.onDestroy()
        val service = SmartPenApplication.getInstance().penService
        service?.disconnectDevice()
    }

    private class PenServiceReceiver : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            val action = intent.action
            if (action == Keys.ACTION_SERVICE_SEND_POINT) {
                val pointJson = intent.getStringExtra(Keys.KEY_PEN_POINT)
                if (pointJson != null && !pointJson.isEmpty()) {
                    Log.d("point_json", "onReceive: $pointJson")
//                    Toast.makeText(this, "$pointJson", Toast.LENGTH_SHORT).show()
                }
                return
            }
        }
    }
}