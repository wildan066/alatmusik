package com.example.toko.UI

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.example.toko.Repository.ShopRepository
import com.example.toko.model.Shop
import kotlinx.coroutines.launch

class ShopViewModel(private val repository: ShopRepository): ViewModel() {
    val allShop: LiveData<List<Shop>> = repository.allshop.asLiveData()


    fun insert(shop: Shop) = viewModelScope.launch {
        repository.InsertShop(shop)
    }
    fun delete(shop: Shop) = viewModelScope.launch {
        repository.DeleteShop(shop)

    }
    fun update(shop: Shop)= viewModelScope.launch {
        repository.UpdateShop(shop)
    }

}

class ShopViewModelFactory(private val repository: ShopRepository):ViewModelProvider.Factory{

    override
    fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom((ShopViewModel::class.java))){
            return ShopViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel Class")
    }
}