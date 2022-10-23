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
import com.dongchyeon.metro.data.SubwayInfo

class LineAdapter(private val context: Context) :
    ListAdapter<SubwayInfo, LineAdapter.ViewHolder>(LineComparator()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder.create(parent)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val current = getItem(position)
        holder.bind(current, context)
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val lineView = itemView.findViewById<TextView>(R.id.lineText)
        private val recyclerView = itemView.findViewById<RecyclerView>(R.id.recyclerView)

        companion object {
            fun create(parent: ViewGroup): ViewHolder {
                val view: View = LayoutInflater.from(parent.context)
                    .inflate(R.layout.line_item, parent, false)
                return ViewHolder(view)
            }
        }

        fun bind(item: SubwayInfo, context: Context) {
            lineView.text = item.updnLine
            when (lineView.text) {
                "상행" -> lineView.setBackgroundResource(R.drawable.red_rectangle)
                "하행" -> lineView.setBackgroundResource(R.drawable.blue_rectangle)
                "내선" -> lineView.setBackgroundResource(R.drawable.pale_red_rectangle)
                "외선" -> lineView.setBackgroundResource(R.drawable.pale_blue_rectangle)
            }

            val adapter = SubwayAdapter()
            recyclerView.layoutManager = LinearLayoutManager(context)
            recyclerView.adapter = adapter

            adapter.submitList(item.subwayList)
        }
    }

    class LineComparator : DiffUtil.ItemCallback<SubwayInfo>() {
        override fun areItemsTheSame(oldItem: SubwayInfo, newItem: SubwayInfo): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: SubwayInfo, newItem: SubwayInfo): Boolean {
            return oldItem == newItem
        }
    }
}