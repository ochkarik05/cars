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
import java.lang.IllegalArgumentException

const val VIEW_TYPE_ODD = 0
const val VIEW_TYPE_EVEN = 1

class ListAdapter: PagedListAdapter<Data, DataViewHolder>(DATA_COMPARATOR) {

    companion object {
          val DATA_COMPARATOR = object : DiffUtil.ItemCallback<Data>() {

            override fun areItemsTheSame(oldItem: Data, newItem: Data): Boolean = oldItem.id == newItem.id

            override fun areContentsTheSame(oldItem: Data, newItem: Data): Boolean = oldItem == newItem

        }
    }

    var selectedItemId: String? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DataViewHolder {

        val layoutId = when(viewType){
            VIEW_TYPE_EVEN -> R.layout.item_text_even
            VIEW_TYPE_ODD -> R.layout.item_text_odd

            else -> throw IllegalArgumentException("Unknown view type: $viewType")
        }

        return DataViewHolder(LayoutInflater.from(parent.context).inflate(layoutId, parent, false))

    }

    override fun onBindViewHolder(holder: DataViewHolder, position: Int) {
        holder.bind(getItem(position)!!, selectedItemId){
           selectedItemId = it
        }
    }

    override fun getItemViewType(position: Int): Int {
        return position % 2
    }
}

class DataViewHolder(val view: View): RecyclerView.ViewHolder(view){

    fun bind(data: Data, selectedItemId: String?, onClick: (String) -> Unit){

        view.findViewById<TextView>(R.id.text).text = data.value

        view.setOnClickListener {
            onClick(data.id)
        }

        view.isSelected = selectedItemId == data.id
    }

}