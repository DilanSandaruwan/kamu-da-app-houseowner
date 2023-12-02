package com.dilan.kamuda.houseownerapp.feature.order.model

import com.google.gson.annotations.SerializedName

data class OrderDetail(
    @SerializedName("id") val id: Int = -1,
    @SerializedName("custId") val custId: Int,
    @SerializedName("firstName") val firstName: String,
    @SerializedName("lastName") val lastName: String,
    @SerializedName("contactNumber") val contactNumber: String,
    @SerializedName("total") val total: Double,
    @SerializedName("date") val date: String,
    @SerializedName("status") val status: String,
    @SerializedName("createdAt") val createdAt: String,
    @SerializedName("items") val items: List<OrderItem>,
)
