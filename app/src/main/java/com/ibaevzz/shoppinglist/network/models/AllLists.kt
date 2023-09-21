package com.ibaevzz.shoppinglist.network.models

import com.google.gson.annotations.SerializedName

data class AllLists(
    @SerializedName("shop_list")
    val lists: List<ShopList>,
    @SerializedName("success")
    val success: Boolean,
)

data class ShopList(
    @SerializedName("created")
    val created: String,
    @SerializedName("name")
    val name: String,
    @SerializedName("id")
    val id: Long
)