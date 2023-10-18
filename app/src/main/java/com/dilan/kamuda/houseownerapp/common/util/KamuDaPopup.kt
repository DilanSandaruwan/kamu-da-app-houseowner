package com.dilan.kamuda.houseownerapp.common.util

import java.io.Serializable

class KamuDaPopup(
    val title: String,
    val message: String,
    val positiveButtonText: String = "",
    val negativeButtonText: String = "",
    val type: Int,
) : Serializable