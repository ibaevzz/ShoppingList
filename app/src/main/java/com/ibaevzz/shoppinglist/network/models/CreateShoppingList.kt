package com.ibaevzz.shoppinglist.network.models

import com.google.gson.annotations.SerializedName

data class CreateShoppingList(
    @SerializedName("success")
    val success: Boolean,
    @SerializedName("list_id")
    val listId: Long
)
