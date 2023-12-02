package com.dilan.kamuda.houseownerapp.feature.order

import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
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
import com.google.android.material.divider.MaterialDivider
import com.google.android.material.textview.MaterialTextView
import java.text.SimpleDateFormat

class OrderAdapter(
    private val itemClickListener: OnItemClickListener,
) : ListAdapter<OrderDetail, OrderAdapter.ViewHolder>(diff_util) {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val orderId: MaterialTextView = view.findViewById(R.id.mtvOrderId)
        val orderDate: MaterialTextView = view.findViewById(R.id.mtvOrderDate)
        val orderTime: MaterialTextView = view.findViewById(R.id.mtvOrderTime)
        val orderItemCount: MaterialTextView = view.findViewById(R.id.mtvOrderedItemCount)
        val orderTotal: MaterialTextView = view.findViewById(R.id.mtvOrderTotal)
        val orderStatus: MaterialTextView = view.findViewById(R.id.tvOrderStatus)
        val custName: MaterialTextView = view.findViewById(R.id.mtvCustName)
        val custContactNumber: MaterialTextView = view.findViewById(R.id.mtvCustMobile)
        val rvOrderItems: RecyclerView = view.findViewById(R.id.rvViewOrderItems)
        val lytBtnToggle: RelativeLayout = view.findViewById(R.id.lytBtnToggle)
        val lytContactCustomer: LinearLayout = view.findViewById(R.id.lytContactCustomer)
        var lytMatCardOrderDetail: MaterialCardView = view.findViewById(R.id.lytMatCardOrderDetail)
        var lytMatCardOrderDivider: MaterialDivider = view.findViewById(R.id.verticalDivider)
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
                holder.lytMatCardOrderDivider.dividerColor =
                ContextCompat.getColor(
                    holder.itemView.context,
                    R.color.yellow
                )

                holder.btnOrderReject.visibility = VISIBLE
                holder.btnOrderAccept.visibility = VISIBLE
                holder.btnOrderReady.visibility = GONE
            }

            "accepted" -> {
                holder.lytMatCardOrderDivider.dividerColor =
                ContextCompat.getColor(
                    holder.itemView.context,
                    R.color.light_blue
                )

                holder.btnOrderReject.visibility = GONE
                holder.btnOrderAccept.visibility = GONE
                holder.btnOrderReady.visibility = VISIBLE
            }

            "rejected" -> {
                holder.lytMatCardOrderDivider.dividerColor =
                ContextCompat.getColor(
                    holder.itemView.context,
                    R.color.redish
                )

                holder.btnOrderReject.visibility = GONE
                holder.btnOrderAccept.visibility = GONE
                holder.btnOrderReady.visibility = GONE
            }

            "completed" -> {
                holder.lytMatCardOrderDivider.dividerColor =
                    ContextCompat.getColor(
                        holder.itemView.context,
                        R.color.black
                    )

                holder.btnOrderReject.visibility = GONE
                holder.btnOrderAccept.visibility = GONE
                holder.btnOrderReady.visibility = GONE
            }

            "cancelled" -> {
                holder.lytMatCardOrderDivider.dividerColor =
                ContextCompat.getColor(
                    holder.itemView.context,
                    R.color.grey
                )

                holder.btnOrderReject.visibility = GONE
                holder.btnOrderAccept.visibility = GONE
                holder.btnOrderReady.visibility = GONE
            }
        }
        holder.orderId.text = item.id.toString()
        SimpleDateFormat("yyyy MMM dd").format(SimpleDateFormat("yyyy-MM-dd").parse(item.date))
            .also { holder.orderDate.text = it }
        holder.orderTime.text = item.createdAt
        holder.orderItemCount.text = "${item.items.sumOf { it.quantity }} Items"
        holder.orderTotal.text = item.total.toString()
        holder.orderStatus.text = item.status
        holder.custName.text = "${item.firstName} ${item.lastName}"
        holder.custContactNumber.text = item.contactNumber
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
                holder.lytContactCustomer.visibility = VISIBLE
            } else {
                holder.btnArrowUp.visibility = GONE
                holder.btnArrowDown.visibility = VISIBLE
                holder.rvOrderItems.visibility = GONE
                holder.lytContactCustomer.visibility = GONE
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