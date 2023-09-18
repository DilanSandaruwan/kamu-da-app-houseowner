package com.dilan.kamuda.houseownerapp.feature.menu.model

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class FoodMenu(
    @SerializedName("id") val id: Int,
    @SerializedName("name") val name: String,
    @SerializedName("price") var price: Double,
    @SerializedName("status") val status: Boolean,
) : Serializable