package com.dilan.kamuda.houseownerapp.common.util

import com.google.gson.annotations.SerializedName

data class KamuDaResponseObject(
    @SerializedName("responseCode") val responseCode: String,
    @SerializedName("responseObject") val responseObject: Any,
)
