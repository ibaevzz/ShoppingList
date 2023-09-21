package com.ibaevzz.shoppinglist.network.models

import com.google.gson.annotations.SerializedName

data class AddItem(
    @SerializedName("success")
    val success: Boolean,
    @SerializedName("item_id")
    val itemId: Long
)
