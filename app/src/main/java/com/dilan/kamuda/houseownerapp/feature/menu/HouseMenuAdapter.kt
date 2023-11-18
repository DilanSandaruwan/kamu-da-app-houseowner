package com.dilan.kamuda.houseownerapp.feature.menu

import android.graphics.BitmapFactory
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.dilan.kamuda.houseownerapp.R
import com.dilan.kamuda.houseownerapp.common.util.component.RoundedImageView
import com.dilan.kamuda.houseownerapp.feature.menu.model.FoodMenu
import com.google.android.material.checkbox.MaterialCheckBox
import com.google.android.material.textview.MaterialTextView

class HouseMenuAdapter(
    private val context: MenuFragment,
    private val itemClickListener: OnItemClickListener,
    private val checkedItemListener: CheckedItemListener,
) : ListAdapter<FoodMenu, HouseMenuAdapter.ViewHolder>(diff_util) {

    interface OnItemClickListener {
        fun itemClick(item: FoodMenu)
    }

    interface CheckedItemListener {
        fun onItemChecked(item: FoodMenu, isChecked: Boolean)
    }

    private val checkedItems = mutableListOf<FoodMenu>()

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val itemName: MaterialTextView = view.findViewById(R.id.mtvItemName)
        val itemPrice: MaterialTextView = view.findViewById(R.id.mtvItemPrice)
        val cbxOrderItem: MaterialCheckBox = view.findViewById(R.id.mcbOrderItem)
        val btnIncrement: ImageButton = view.findViewById(R.id.btnIncrement)
        val btnDecrement: ImageButton = view.findViewById(R.id.btnDecrement)
        val ivRoundedImageView: RoundedImageView = view.findViewById(R.id.ivRoundMenuItem)
        val tvItemCount: TextView = view.findViewById(R.id.tvItemCount)
        val ivEditMenuItem: ImageView = view.findViewById(R.id.ivEditMenuItem)
    }

    companion object {

        val diff_util = object : DiffUtil.ItemCallback<FoodMenu>() {

            override fun areItemsTheSame(oldItem: FoodMenu, newItem: FoodMenu): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(
                oldItem: FoodMenu,
                newItem: FoodMenu
            ): Boolean {
                return oldItem == newItem
            }

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val _view = LayoutInflater.from(parent.context)
            .inflate(R.layout.layout_menu_item, parent, false)
        return ViewHolder(_view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getItem(position)
        // Set data to the EditText views
        holder.itemName.text = item.name.toString()
        holder.itemPrice.text = item.price.toString()
        holder.tvItemCount.text = item.price.toString()
        if (item.image != null) {
            var imageBitmap =
                BitmapFactory.decodeByteArray(item.image as ByteArray?, 0, item.image.size)
            Glide.with(context)
                .load(imageBitmap)
                .diskCacheStrategy(
                    DiskCacheStrategy.ALL
                )
                .into(holder.ivRoundedImageView)
        }
        holder.cbxOrderItem.isChecked =
            !(item.status.isNullOrBlank() || "N".equals(item.status, true))
        //holder.cbxOrderItem.isChecked = item in checkedItems

        holder.cbxOrderItem.setOnCheckedChangeListener { _, isChecked ->
            checkedItemListener.onItemChecked(item, isChecked) // Call the callback method

            holder.tvItemCount.visibility = if (isChecked) View.VISIBLE else View.INVISIBLE
            holder.btnDecrement.visibility =
                if (isChecked) View.VISIBLE else View.INVISIBLE // Enable decrement if checked
            holder.btnIncrement.visibility =
                if (isChecked) View.VISIBLE else View.INVISIBLE // Enable increment if checked

            if (!isChecked) {
                holder.tvItemCount.text = item.price.toString()
            }
        }

        holder.ivEditMenuItem.setOnClickListener {
            itemClickListener.itemClick(item)
        }

        if (item in checkedItems) {
            holder.tvItemCount.visibility =
                if (holder.cbxOrderItem.isChecked) View.VISIBLE else View.INVISIBLE
            holder.btnDecrement.visibility =
                if (holder.cbxOrderItem.isChecked) View.VISIBLE else View.INVISIBLE // Enable decrement if checked
            holder.btnIncrement.visibility =
                if (holder.cbxOrderItem.isChecked) View.VISIBLE else View.INVISIBLE // Enable increment if checked

            if (!holder.cbxOrderItem.isChecked) {
                holder.tvItemCount.text = item.price.toString()
            }
        }

        holder.btnIncrement.setOnClickListener {
            item.price++
            holder.tvItemCount.text = item.price.toString()
        }

        holder.btnDecrement.setOnClickListener {
            if (item.price > 0.00) {
                item.price--
                holder.tvItemCount.text = item.price.toString()
            }
        }


    }

    fun setCheckedItems(items: List<FoodMenu>) {
        checkedItems.clear()
        checkedItems.addAll(items)
        submitList(currentList)
    }

    fun getCheckedItemsList(): List<FoodMenu> {
        return checkedItems.toList()
    }
}