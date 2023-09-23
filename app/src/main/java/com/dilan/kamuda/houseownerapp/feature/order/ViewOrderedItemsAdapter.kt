package com.dilan.kamuda.houseownerapp.feature.order

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.dilan.kamuda.houseownerapp.R
import com.dilan.kamuda.houseownerapp.feature.order.model.OrderItem
import com.google.android.material.textview.MaterialTextView

class ViewOrderedItemsAdapter() :
    ListAdapter<OrderItem, ViewOrderedItemsAdapter.ViewHolder>(diff_utils) {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val orderedItemName: MaterialTextView = view.findViewById(R.id.mtvOrderedItemName)
        val orderedItemQty: MaterialTextView = view.findViewById(R.id.mtvOrderedItemQuantity)
    }


    companion object {

        val diff_utils = object : DiffUtil.ItemCallback<OrderItem>() {

            override fun areItemsTheSame(oldItem: OrderItem, newItem: OrderItem): Boolean {
                return oldItem.name == newItem.name
            }

            override fun areContentsTheSame(
                oldItem: OrderItem,
                newItem: OrderItem
            ): Boolean {
                return oldItem == newItem
            }

        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.layout_order_detail_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getItem(position)
        holder.orderedItemName.text = item.name.toString()
        holder.orderedItemQty.text = item.quantity.toString()
    }
}