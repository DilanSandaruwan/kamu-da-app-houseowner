package com.dilan.kamuda.houseownerapp.feature.order

import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RelativeLayout
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.dilan.kamuda.houseownerapp.R
import com.dilan.kamuda.houseownerapp.feature.order.model.OrderDetail
import com.google.android.material.button.MaterialButton
import com.google.android.material.card.MaterialCardView
import com.google.android.material.textview.MaterialTextView

class OrderAdapter(
    private val itemClickListener: OnItemClickListener,
) : ListAdapter<OrderDetail, OrderAdapter.ViewHolder>(diff_util) {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val orderId: MaterialTextView = view.findViewById(R.id.mtvOrderId)
        val orderTotal: MaterialTextView = view.findViewById(R.id.mtvOrderTotal)
        val orderStatus: MaterialTextView = view.findViewById(R.id.tvOrderStatus)
        val rvOrderItems: RecyclerView = view.findViewById(R.id.rvViewOrderItems)
        val lytBtnToggle: RelativeLayout = view.findViewById(R.id.lytBtnToggle)
        var lytMatCardOrderDetail: MaterialCardView = view.findViewById(R.id.lytMatCardOrderDetail)
        val btnArrowUp: ImageView = view.findViewById(R.id.btnArrowUp)
        val btnArrowDown: ImageView = view.findViewById(R.id.btnArrowDown)
        val btnOrderReject: MaterialButton = view.findViewById(R.id.btnOrderReject)
        val btnOrderAccept: MaterialButton = view.findViewById(R.id.btnOrderAccept)
        val btnOrderReady: MaterialButton = view.findViewById(R.id.btnOrderReady)
    }

    interface OnItemClickListener {
        fun itemClick(itemId: Int, status: String)
    }

    companion object {

        val diff_util = object : DiffUtil.ItemCallback<OrderDetail>() {

            override fun areItemsTheSame(oldItem: OrderDetail, newItem: OrderDetail): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(
                oldItem: OrderDetail,
                newItem: OrderDetail
            ): Boolean {
                return oldItem == newItem
            }

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.layout_order_detail, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getItem(position)
        when (item.status) {
            "pending" -> {
                holder.lytMatCardOrderDetail.setBackgroundColor(
                    ContextCompat.getColor(
                        holder.itemView.context,
                        R.color.light_grey
                    )
                )
                holder.btnOrderReject.visibility = VISIBLE
                holder.btnOrderAccept.visibility = VISIBLE
                holder.btnOrderReady.visibility = GONE
            }

            "accepted" -> {
                holder.lytMatCardOrderDetail.setBackgroundColor(
                    ContextCompat.getColor(
                        holder.itemView.context,
                        R.color.light_blue
                    )
                )
                holder.btnOrderReject.visibility = GONE
                holder.btnOrderAccept.visibility = GONE
                holder.btnOrderReady.visibility = VISIBLE
            }

            "rejected" -> {
                holder.lytMatCardOrderDetail.setBackgroundColor(
                    ContextCompat.getColor(
                        holder.itemView.context,
                        R.color.teal_700
                    )
                )
                holder.btnOrderReject.visibility = GONE
                holder.btnOrderAccept.visibility = GONE
                holder.btnOrderReady.visibility = GONE
            }

            "ready" -> {
                holder.lytMatCardOrderDetail.setBackgroundColor(
                    ContextCompat.getColor(
                        holder.itemView.context,
                        R.color.semi_transparent
                    )
                )
                holder.btnOrderReject.visibility = GONE
                holder.btnOrderAccept.visibility = GONE
                holder.btnOrderReady.visibility = GONE
            }

            "cancelled" -> {
                holder.btnOrderReject.visibility = GONE
                holder.btnOrderAccept.visibility = GONE
                holder.btnOrderReady.visibility = GONE
            }
        }
        holder.orderId.text = item.id.toString()
        holder.orderTotal.text = item.total.toString()
        holder.orderStatus.text = item.status
        // Set up child RecyclerView
        val childAdapter = ViewOrderedItemsAdapter()
        holder.rvOrderItems.layoutManager =
            LinearLayoutManager(holder.rvOrderItems.context) // Set layout manager
        holder.rvOrderItems.addItemDecoration(
            DividerItemDecoration(
                holder.rvOrderItems.context,
                (holder.rvOrderItems.layoutManager as LinearLayoutManager).orientation
            )
        )
        holder.rvOrderItems.adapter = childAdapter
        childAdapter.submitList(item.items)

        holder.lytBtnToggle.setOnClickListener {
            if (holder.btnArrowDown.visibility == VISIBLE) {
                holder.btnArrowDown.visibility = GONE
                holder.btnArrowUp.visibility = VISIBLE
                holder.rvOrderItems.visibility = VISIBLE
            } else {
                holder.btnArrowUp.visibility = GONE
                holder.btnArrowDown.visibility = VISIBLE
                holder.rvOrderItems.visibility = GONE
            }
        }

        holder.btnOrderReject.setOnClickListener {
            itemClickListener.itemClick(item.id, "rejected")
        }
        holder.btnOrderAccept.setOnClickListener {
            itemClickListener.itemClick(item.id, "accepted")
        }
        holder.btnOrderReady.setOnClickListener {
            itemClickListener.itemClick(item.id, "completed")
        }

    }
}