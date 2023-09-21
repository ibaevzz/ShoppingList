package com.ibaevzz.shoppinglist.network.models

import com.google.gson.annotations.SerializedName

data class AllItemsOfList(
    @SerializedName("success")
    val success: Boolean,
    @SerializedName("item_list")
    val items: List<Item>
)

data class Item(
    @SerializedName("created")
    val created: String,
    @SerializedName("name")
    val name: String,
    @SerializedName("id")
    val id: Long,
    @SerializedName("is_crossed")
    val isCrossed: Boolean)