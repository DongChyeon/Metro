package com.dongchyeon.metro.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.dongchyeon.metro.R
import com.dongchyeon.metro.data.network.dto.RealtimeArrivalList

class SubwayAdapter :
    ListAdapter<RealtimeArrivalList, SubwayAdapter.ViewHolder>(SubwayComparator()) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder.create(parent)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val current = getItem(position)
        holder.bind(current)
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val infoText1 = itemView.findViewById<TextView>(R.id.infoText1)
        private val infoText2 = itemView.findViewById<TextView>(R.id.infoText2)

        companion object {
            fun create(parent: ViewGroup): ViewHolder {
                val view: View = LayoutInflater.from(parent.context)
                    .inflate(R.layout.subway_item, parent, false)
                return ViewHolder(view)
            }
        }

        fun bind(item: RealtimeArrivalList) {
            infoText1.text = item.trainLineNm
            infoText2.text = item.arvlMsg2
        }
    }

    class SubwayComparator : DiffUtil.ItemCallback<RealtimeArrivalList>() {
        override fun areItemsTheSame(
            oldItem: RealtimeArrivalList,
            newItem: RealtimeArrivalList
        ): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(
            oldItem: RealtimeArrivalList,
            newItem: RealtimeArrivalList
        ): Boolean {
            return oldItem == newItem
        }
    }
}