package com.ibaevzz.shoppinglist.network.models

import com.google.gson.annotations.SerializedName

data class Success(
    @SerializedName("success")
    val success: Boolean
)