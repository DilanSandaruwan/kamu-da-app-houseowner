package com.dilan.kamuda.customerapp.model.specific

import com.google.gson.annotations.SerializedName

data class KamuDaError(
    @SerializedName("errorCode") val errorCode: String,
    @SerializedName("errorMessage") val errorMessage: String,
)
