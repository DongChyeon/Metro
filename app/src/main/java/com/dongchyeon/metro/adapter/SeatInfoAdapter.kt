package com.dongchyeon.metro.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.dongchyeon.metro.R
import com.dongchyeon.metro.data.SeatInfo

class SeatInfoAdapter(private val context: Context) :
    ListAdapter<SeatInfo, SeatInfoAdapter.ViewHolder>(SeatInfoComparator()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder.create(parent)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val current = getItem(position)
        holder.bind(current, context)
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val lineText = itemView.findViewById<TextView>(R.id.lineText)
        private val recyclerView1 = itemView.findViewById<RecyclerView>(R.id.recyclerView1)
        private val recyclerView2 = itemView.findViewById<RecyclerView>(R.id.recyclerView2)

        companion object {
            fun create(parent: ViewGroup): ViewHolder {
                val view: View = LayoutInflater.from(parent.context)
                    .inflate(R.layout.seat_info_item, parent, false)
                return ViewHolder(view)
            }
        }

        fun bind(item: SeatInfo, context: Context) {
            lineText.text = item.trainLineNm

            val pregnantAdapter = SeatAdapter()
            recyclerView1.layoutManager =
                LinearLayoutManager(context, RecyclerView.HORIZONTAL, false)
            recyclerView1.adapter = pregnantAdapter

            val elderlyAdapter = SeatAdapter()
            recyclerView2.layoutManager =
                LinearLayoutManager(context, RecyclerView.HORIZONTAL, false)
            recyclerView2.adapter = elderlyAdapter

            pregnantAdapter.submitList(item.pregnant)
            elderlyAdapter.submitList(item.elderly)
        }
    }

    class SeatInfoComparator : DiffUtil.ItemCallback<SeatInfo>() {
        override fun areItemsTheSame(oldItem: SeatInfo, newItem: SeatInfo): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: SeatInfo, newItem: SeatInfo): Boolean {
            return oldItem == newItem
        }
    }
}