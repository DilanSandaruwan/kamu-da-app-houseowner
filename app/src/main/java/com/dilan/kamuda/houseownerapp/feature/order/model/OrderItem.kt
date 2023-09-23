package com.dilan.kamuda.houseownerapp.feature.order.model

import com.google.gson.annotations.SerializedName

data class OrderItem(
    @SerializedName("name") val name: String,
    @SerializedName("price") val price: Double,
    @SerializedName("quantity") var quantity: Int,
)
