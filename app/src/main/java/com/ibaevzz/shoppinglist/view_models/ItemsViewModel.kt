package com.ibaevzz.shoppinglist.view_models

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ibaevzz.shoppinglist.network.ShoppingAPIClient
import com.ibaevzz.shoppinglist.network.models.AddItem
import com.ibaevzz.shoppinglist.network.models.AllItemsOfList
import com.ibaevzz.shoppinglist.network.models.Item
import com.ibaevzz.shoppinglist.network.models.ShopList
import kotlinx.coroutines.launch
import javax.inject.Inject

class ItemsViewModel @Inject constructor(private val apiClient: ShoppingAPIClient): ViewModel(){

    private val _addItem = MutableLiveData<AddItem?>()
    private val _crossItem = MutableLiveData<Boolean>()
    private val _allItemsOfList = MutableLiveData<AllItemsOfList?>()
    private val _deleteItem = MutableLiveData<Boolean>()

    fun addItem(listId: Long, name: String, n: Int): LiveData<AddItem?>{
        viewModelScope.launch {
            val response = apiClient.addItem(listId, name, n)
            if(response.isSuccessful){
                _addItem.value = response.body()
            }else{
                _addItem.value = null
            }
        }
        return _addItem
    }

    fun crossItem(listId: Long, itemId: Long): LiveData<Boolean>{
        viewModelScope.launch {
            val response = apiClient.crossItem(listId, itemId)
            if(response.isSuccessful){
                _crossItem.value = response.body()?.success
            }else{
                _crossItem.value = false
            }
        }
        return _crossItem
    }

    fun deleteItem(listId: Long, itemId: Long): LiveData<Boolean>{
        viewModelScope.launch {
            val response = apiClient.removeItem(listId, itemId)
            if(response.isSuccessful){
                _deleteItem.value = response.body()?.success
            }else{
                _crossItem.value = false
            }
        }
        return _deleteItem
    }

    fun getAllItemsOfList(listId: Long): LiveData<AllItemsOfList?>{
        viewModelScope.launch {
            val response = apiClient.getAllItemsOfList(listId)
            val result = response.body()
            if(response.isSuccessful){
                _allItemsOfList.value = response.body()
            }else{
                _allItemsOfList.value = null
            }
        }
        return _allItemsOfList
    }
}