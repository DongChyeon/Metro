package com.dongchyeon.metro.adapter

import android.annotation.SuppressLint
import android.bluetooth.BluetoothDevice
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.dongchyeon.metro.R

class BleAdapter() : ListAdapter<BluetoothDevice, BleAdapter.ViewHolder>(BtComparator()) {
    private lateinit var listener: OnItemClickListener

    interface OnItemClickListener {
        fun onItemClick(view: View, position: Int)
    }

    fun setOnItemClickListener(listener: OnItemClickListener) {
        this.listener = listener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder.create(parent)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val current = getItem(position)
        holder.bind(current)
        holder.itemView.setOnClickListener {
            listener.onItemClick(it, position)
        }
    }

    fun getDevice(position: Int): BluetoothDevice {
        return getItem(position)
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val bleNm = itemView.findViewById<TextView>(R.id.ble_name)
        private val bleAddress = itemView.findViewById<TextView>(R.id.ble_address)

        companion object {
            fun create(parent: ViewGroup): ViewHolder {
                val view: View = LayoutInflater.from(parent.context)
                    .inflate(R.layout.ble_item, parent, false)
                return ViewHolder(view)
            }
        }

        @SuppressLint("MissingPermission")
        fun bind(item: BluetoothDevice) {
            bleNm.text = item.name
            bleAddress.text = item.address
        }
    }

    class BtComparator : DiffUtil.ItemCallback<BluetoothDevice>() {
        override fun areItemsTheSame(
            oldItem: BluetoothDevice,
            newItem: BluetoothDevice
        ): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(
            oldItem: BluetoothDevice,
            newItem: BluetoothDevice
        ): Boolean {
            return oldItem == newItem
        }
    }
}