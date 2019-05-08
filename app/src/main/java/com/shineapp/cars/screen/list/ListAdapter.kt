package com.shineapp.cars.screen.list

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.shineapp.cars.R
import com.shineapp.cars.data.model.Data
import com.shineapp.cars.system.Lg
import java.lang.IllegalArgumentException

const val VIEW_TYPE_ODD = 0
const val VIEW_TYPE_EVEN = 1

class ListAdapter(val onSelected: (Data) -> Unit): PagedListAdapter<Data, DataViewHolder>(DATA_COMPARATOR) {

    companion object {
          val DATA_COMPARATOR = object : DiffUtil.ItemCallback<Data>() {

            override fun areItemsTheSame(oldItem: Data, newItem: Data): Boolean = oldItem.id == newItem.id

            override fun areContentsTheSame(oldItem: Data, newItem: Data): Boolean = oldItem == newItem

        }
    }

    var selectedItemPosition: Int? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DataViewHolder {

        val layoutId = when(viewType){
            VIEW_TYPE_EVEN -> R.layout.item_text_even
            VIEW_TYPE_ODD -> R.layout.item_text_odd
            else -> throw IllegalArgumentException("Unknown view type: $viewType")
        }

        return DataViewHolder(LayoutInflater.from(parent.context).inflate(layoutId, parent, false))
    }

    override fun onBindViewHolder(holder: DataViewHolder, position: Int) {

        val selectedItemId = selectedItemPosition?.let {
            getItem(it)!!.id
        }

        holder.bind(getItem(position)!!, selectedItemId){
            selectedItemPosition?.let {
                notifyItemChanged(it)
            }
            selectedItemPosition = it
            notifyItemChanged(position)
        }
    }

    override fun getItemViewType(position: Int): Int {
        return position % 2
    }
}

open class DataViewHolder(val view: View): RecyclerView.ViewHolder(view){

    fun bind(data: Data, selectedItemId: String?, onClick: (Int) -> Unit){

        view.findViewById<TextView>(R.id.text).text = data.value

        view.setOnClickListener {
            onClick(adapterPosition)
        }

        view.isSelected = selectedItemId == data.id
    }

}

class DataViewHolder2(view: View): DataViewHolder(view)