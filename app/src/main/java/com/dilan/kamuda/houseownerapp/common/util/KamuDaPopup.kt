package com.dilan.kamuda.customerapp.model.specific

import java.io.Serializable

class KamuDaPopup(
    val title: String,
    val message: String,
    val positiveButtonText: String = "",
    val negativeButtonText: String = "",
    val type: Int,
) : Serializable