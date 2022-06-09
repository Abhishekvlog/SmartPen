package com.dexter.blepoc

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.smart.pen.core.model.DeviceObject
import kotlinx.android.synthetic.main.item_layout.view.*

class PenAdapter(val deviceObjectList: List<DeviceObject>) : RecyclerView.Adapter<PenViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PenViewHolder {
        val view: View = LayoutInflater.from(parent.context).inflate(R.layout.item_layout, parent , false)
        return PenViewHolder(view)
    }

    override fun onBindViewHolder(holder: PenViewHolder, position: Int) {
        holder.setData(deviceObjectList[position])
    }

    override fun getItemCount(): Int {
        return deviceObjectList.size
    }
}
class PenViewHolder(val view : View) : RecyclerView.ViewHolder(view){
    fun setData(deviceObject: DeviceObject) {
        view.apply {
            device_name.text = deviceObject.name
        }
    }

}