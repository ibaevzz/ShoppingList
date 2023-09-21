package com.ibaevzz.shoppinglist.view_models

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ibaevzz.shoppinglist.network.ShoppingAPIClient
import com.ibaevzz.shoppinglist.network.models.AllLists
import com.ibaevzz.shoppinglist.network.models.CreateShoppingList
import com.ibaevzz.shoppinglist.network.models.RemoveShoppingList
import com.ibaevzz.shoppinglist.network.models.ShopList
import kotlinx.coroutines.launch
import javax.inject.Inject

class ListsViewModel @Inject constructor(private val apiClient: ShoppingAPIClient): ViewModel() {
    private val _authKey = MutableLiveData<String?>()
    private val _authentication = MutableLiveData<Boolean>()
    private val _createList = MutableLiveData<CreateShoppingList?>()
    private val _removeList = MutableLiveData<RemoveShoppingList?>()
    private val _allLists = MutableLiveData<AllLists?>()

    fun getAuthKey(): LiveData<String?>{
        viewModelScope.launch {
            val response = apiClient.getAuthKey()
            if(response.isSuccessful){
                _authKey.value = response.body()?.string()
            }else{
                _authKey.value = null
            }
        }
        return _authKey
    }

    fun auth(key: String): LiveData<Boolean>{
        viewModelScope.launch {
            val response = apiClient.getAuthentication(key)
            if(response.isSuccessful){
                _authentication.value = response.body()?.success
            }else{
                _authentication.value = false
            }
        }
        return _authentication
    }

    fun createList(key: String, name: String): LiveData<CreateShoppingList?>{
        viewModelScope.launch {
            val response = apiClient.createShoppingList(key, name)
            if(response.isSuccessful){
                _createList.value = response.body()
            }else{
                _createList.value = null
            }
        }
        return _createList
    }

    fun removeList(listId: Long): LiveData<RemoveShoppingList?>{
        viewModelScope.launch {
            val response = apiClient.removeShoppingList(listId)
            if(response.isSuccessful){
                _removeList.value = response.body()
            }else{
                _removeList.value = null
            }
        }
        return _removeList
    }

    fun getAllLists(key: String): LiveData<AllLists?>{
        viewModelScope.launch {
            val response = apiClient.getAllLists(key)
            val result = response.body()
            if (response.isSuccessful) {
                _allLists.value = result
            } else {
                _allLists.value = null
            }
        }
        return _allLists
    }
}