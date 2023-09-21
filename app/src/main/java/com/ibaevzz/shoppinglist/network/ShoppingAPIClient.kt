package com.ibaevzz.shoppinglist.network

import com.ibaevzz.shoppinglist.constants.Constants
import com.ibaevzz.shoppinglist.network.models.*
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.POST
import retrofit2.http.Query

interface ShoppingAPIClient {

    @POST(Constants.CREATE_KEY)
    suspend fun getAuthKey(): Response<ResponseBody>

    @POST(Constants.AUTHENTICATION)
    suspend fun getAuthentication(@Query("key") key: String): Response<Success>

    @POST(Constants.CREATE_LIST)
    suspend fun createShoppingList(@Query("key") key: String, @Query("name") name: String): Response<CreateShoppingList>

    @POST(Constants.REMOVE_LIST)
    suspend fun removeShoppingList(@Query("list_id") listId: Long): Response<RemoveShoppingList>

    @POST(Constants.ADD_ITEM)
    suspend fun addItem(@Query("id") listId: Long, @Query("value") name: String, @Query("n") n: Int): Response<AddItem>

    @POST(Constants.CROSS_ITEM)
    suspend fun crossItem(@Query("list_id") listId: Long, @Query("id") itemId: Long): Response<CrossItem>

    @POST(Constants.REMOVE_FROM_LIST)
    suspend fun removeItem(@Query("list_id") listId: Long, @Query("item_id") itemId: Long): Response<Success>

    @POST(Constants.GET_ALL_LISTS)
    suspend fun getAllLists(@Query("key") key: String): Response<AllLists>

    @POST(Constants.GET_LIST)
    suspend fun getAllItemsOfList(@Query("list_id") listId: Long): Response<AllItemsOfList>
}