package com.ibaevzz.shoppinglist.network.models

import com.google.gson.annotations.SerializedName

data class RemoveShoppingList(
    @SerializedName("success")
    val success: Boolean,
    @SerializedName("new_value")
    val newValue: Boolean
)
