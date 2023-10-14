package com.dilan.kamuda.customerapp.model.specific

import com.google.gson.annotations.SerializedName

data class KamuDaResponseObject(
    @SerializedName("responseCode") val responseCode: String,
    @SerializedName("responseObject") val responseObject: Any,
)
