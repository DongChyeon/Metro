package com.dongchyeon.metro.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.dongchyeon.metro.R

class SeatAdapter(private val people: String) :
    ListAdapter<Int, SeatAdapter.ViewHolder>(SeatComparator()) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder.create(parent)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val current = getItem(position)
        holder.bind(current, people, position)
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val layout = itemView.findViewById<ConstraintLayout>(R.id.layout)
        private val seatCount = itemView.findViewById<TextView>(R.id.seat_count)

        companion object {
            fun create(parent: ViewGroup): ViewHolder {
                val view: View = LayoutInflater.from(parent.context)
                    .inflate(R.layout.seat_item, parent, false)
                return ViewHolder(view)
            }
        }

        fun bind(item: Int, people: String, position: Int) {
            seatCount.text = item.toString()
            if (people == "pregnant") {
                if (position == 0) layout.setBackgroundResource(R.color.yellow)
                else if (item == 0) layout.setBackgroundResource(R.color.pale_pink)
                else layout.setBackgroundResource(R.color.pink)
            } else if (people == "elderly") {
                if (position == 0) layout.setBackgroundResource(R.color.yellow)
                else if (item == 0) layout.setBackgroundResource(R.color.gray)
                else layout.setBackgroundResource(R.color.black)
            }
        }
    }

    class SeatComparator : DiffUtil.ItemCallback<Int>() {
        override fun areItemsTheSame(
            oldItem: Int,
            newItem: Int
        ): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(
            oldItem: Int,
            newItem: Int
        ): Boolean {
            return oldItem == newItem
        }
    }
}