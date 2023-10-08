package com.dilan.kamuda.houseownerapp.feature.add.model

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class AddNewMenu(
    @SerializedName("id") val id: Int,
    @SerializedName("name") val name: String,
    @SerializedName("price") var price: Double,
    @SerializedName("status") val status: String,
) : Serializable
