package com.nsoft.mybakery.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.nsoft.mybakery.models.Product
import com.nsoft.mybakery.repositories.MyRepository
import kotlinx.coroutines.launch

class MyViewModel(application: Application) : AndroidViewModel(application) {
    val context = getApplication<Application>()
    val myRepository = MyRepository(context)
    private val allProductsLiveData = MutableLiveData<List<Product>>()
    var checkInsertLiveData = MutableLiveData<Boolean?>()
    var checkDeleteLiveData = MutableLiveData<Boolean?>()

    fun checkInsert(): LiveData<Boolean?> {
        return checkInsertLiveData
    }


    fun insertProduct(product: Product) {
        viewModelScope.launch {
            val result = myRepository.insertProduct(product)

            if (result == (-1).toLong()) {
                checkInsertLiveData.postValue(false)
                return@launch
            }

            val newList = allProductsLiveData?.value?.toMutableList() ?: ArrayList<Product>()
            newList.add(0, product)

            allProductsLiveData.postValue(newList as ArrayList<Product>)
            checkInsertLiveData.postValue(true)
        }
    }

    fun getAllProducts(): LiveData<List<Product>> {
        viewModelScope.launch {
            val allProducts = myRepository.getAllProducts()

            if (allProducts != null) {
                allProductsLiveData.postValue(allProducts)
            }
        }
        return allProductsLiveData
    }

    fun deleteProductById(id: Int) {
        viewModelScope.launch {
            val result = myRepository.deleteProductById(id)
            if (result == 0) {
                checkDeleteLiveData.postValue(false)
                return@launch
            }
            var newList = allProductsLiveData?.value?.toMutableList() ?: ArrayList<Product>()
            newList = newList.filter { it.id != id } as MutableList<Product>
            allProductsLiveData.postValue(newList)
            checkDeleteLiveData.postValue(true)
        }
    }

}