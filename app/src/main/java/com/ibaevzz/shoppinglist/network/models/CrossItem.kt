package com.ibaevzz.shoppinglist.network.models

import com.google.gson.annotations.SerializedName

data class CrossItem(
    @SerializedName("success")
    val success: Boolean,
    @SerializedName("rows_affected")
    val rowsAffected: Int
)
