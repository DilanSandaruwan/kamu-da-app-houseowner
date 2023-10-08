package com.dilan.kamuda.houseownerapp.feature.menu.model

import android.graphics.Bitmap
import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class FoodMenu(
    @SerializedName("id") val id: Int,
    @SerializedName("name") val name: String,
    @SerializedName("price") var price: Double,
    @SerializedName("status") var status: String,
    @SerializedName("image") val image: Any?,
) : Serializable {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as FoodMenu

        if (image == null) {
            if (other.image != null) return false
        } else if (image is String) {
            if (other.image !is String || image != other.image) return false
        } else if (image is List<*>) {
            if (other.image !is List<*> || !image.equals(other.image)) return false
        } else if (image is Bitmap) {
            if (other.image !is Bitmap || !image.equals(other.image)) return false
        } else if (image is ByteArray) {
            if (other.image !is ByteArray || !image.equals(other.image)) return false
        }

        return true
    }

    override fun hashCode(): Int {
        return when (image) {
            null -> 0
            is String -> image.hashCode()
            is List<*> -> image.hashCode()
            is ByteArray -> image.hashCode()
            is Bitmap -> image.hashCode()
            else -> 0 // Handle other types as needed
        }
    }
}