package com.nsoft.mybakery.viewmodels

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.nsoft.mybakery.models.Customer
import com.nsoft.mybakery.models.Product
import com.nsoft.mybakery.models.SellTransaction
import com.nsoft.mybakery.models.Transaction
import com.nsoft.mybakery.repositories.MyRepository
import kotlinx.coroutines.launch

class MyViewModel(application: Application) : AndroidViewModel(application) {
    val context = getApplication<Application>()
    val myRepository = MyRepository(context)
    private var allProductsOriginalList = ArrayList<Product>()
    private val allProductsLiveData = MutableLiveData<List<Product>>()
    private var allCustomersOriginalList = ArrayList<Customer>()
    private val allCustomersLiveData = MutableLiveData<List<Customer>>()
    private var allTransactionOriginalList = ArrayList<SellTransaction>()
    private val allTransactionLiveData = MutableLiveData<List<SellTransaction>>()
    var checkInsertLiveData = MutableLiveData<Boolean?>()
    var checkDeleteLiveData = MutableLiveData<Boolean?>()
    var checkUpdateLiveData = MutableLiveData<Boolean?>()
    var checkCustomerInsertLiveData = MutableLiveData<Boolean?>()
    var checkCustomerDeleteLiveData = MutableLiveData<Boolean?>()
    var checkCustomerUpdateLiveData = MutableLiveData<Boolean?>()
    var checkTransactionInsertLiveData = MutableLiveData<Boolean?>()


    fun insertProduct(product: Product) {
        viewModelScope.launch {
            val result = myRepository.insertProduct(product)

            if (result == (-1).toLong()) {
                checkInsertLiveData.postValue(false)
                return@launch
            }

            val newList = allProductsLiveData.value?.toMutableList() ?: ArrayList<Product>()
            newList.add(0, product)

            allProductsOriginalList.add(0, product)

            allProductsLiveData.postValue(newList)
            checkInsertLiveData.postValue(true)
        }
    }

    fun getAllProducts(): LiveData<List<Product>> {
        viewModelScope.launch {
            val allProducts = myRepository.getAllProducts()

            allProductsLiveData.postValue(allProducts)
            allProductsOriginalList.clear()
            allProductsOriginalList.addAll(allProducts)
        }
        return allProductsLiveData
    }

    fun filterProducts(keyword: String) {
        viewModelScope.launch {
            if (keyword.isEmpty()) {
                allProductsLiveData.postValue(allProductsOriginalList.toList())
            } else {
                val newList =
                    allProductsOriginalList.filter {
                        it.name.lowercase().contains(keyword.lowercase())
                    }
                allProductsLiveData.postValue(newList)
            }
        }
    }

    fun deleteProductById(id: Int) {
        viewModelScope.launch {
            val result = myRepository.deleteProductById(id)
            if (result == 0) {
                checkDeleteLiveData.postValue(false)
                return@launch
            }
            var newList = allProductsLiveData.value?.toMutableList() ?: ArrayList<Product>()
            newList = newList.filter { it.id != id } as MutableList<Product>
            allProductsLiveData.postValue(newList)
            checkDeleteLiveData.postValue(true)

            allProductsOriginalList.clear()
            allProductsOriginalList.addAll(newList)
        }
    }

    fun updateProduct(product: Product) {
        viewModelScope.launch {
            val result = myRepository.updateProduct(product)
            if (result <= 0) {
                checkUpdateLiveData.postValue(false)
                return@launch
            }

            val productsList = allProductsLiveData.value?.toMutableList() ?: ArrayList<Product>()
            val index = productsList.indexOfFirst { it.id == product.id }

            if (index != -1) {
                productsList[index] = product
                allProductsLiveData.postValue(productsList)
                checkUpdateLiveData.postValue(true)

                allProductsOriginalList.clear()
                allProductsOriginalList.addAll(productsList)
            }
        }
    }

    fun insertCustomer(customer: Customer) {
        viewModelScope.launch {
            val result = myRepository.insertCustomer(customer)
            if (result <= 0) {
                checkCustomerInsertLiveData.postValue(false)
                return@launch
            }
            val newList = allCustomersLiveData.value?.toMutableList() ?: ArrayList<Customer>()
            newList.add(0, customer)

            allCustomersOriginalList.add(0, customer)
            allCustomersLiveData.postValue(newList)
            checkCustomerInsertLiveData.postValue(true)
        }
    }

    fun getAllCustomers(): LiveData<List<Customer>> {
        viewModelScope.launch {
            val allCustomers = myRepository.getAllCustomers()

            allCustomersLiveData.postValue(allCustomers)
            allCustomersOriginalList.clear()
            allCustomersOriginalList.addAll(allCustomers)
        }
        return allCustomersLiveData
    }

    fun deleteCustomer(id: Int) {
        viewModelScope.launch {
            val result = myRepository.deleteCustomer(id)
            if (result == 0) {
                checkCustomerDeleteLiveData.postValue(false)
                return@launch
            }

            var newList = allCustomersLiveData.value?.toMutableList() ?: ArrayList<Customer>()
            newList = newList.filter { it.id != id } as MutableList<Customer>

            allCustomersLiveData.postValue(newList)
            checkCustomerDeleteLiveData.postValue(true)

            allCustomersOriginalList.clear()
            allCustomersOriginalList.addAll(newList)
        }
    }

    fun updateCustomer(customer: Customer) {
        viewModelScope.launch {
            val result = myRepository.updateCustomer(customer)
            if (result <= 0) {
                checkCustomerUpdateLiveData.postValue(false)
                return@launch
            }

            val newList = allCustomersLiveData.value?.toMutableList() ?: ArrayList<Customer>()
            val index = newList.indexOfFirst { it.id == customer.id }

            if (index != -1) {
                newList[index] = customer
                allCustomersLiveData.postValue(newList)
                checkCustomerUpdateLiveData.postValue(true)

                allCustomersOriginalList.clear()
                allCustomersOriginalList.addAll(newList)

            }

        }
    }

    fun filterCustomer(keyword: String) {
        viewModelScope.launch {
            if (keyword.isEmpty()) {
                allCustomersLiveData.postValue(allCustomersOriginalList.toList())
            } else {
                val newList = allCustomersOriginalList.filter {
                    it.name.lowercase().contains(keyword.lowercase())
                }
                allCustomersLiveData.postValue(newList)
            }
        }
    }

    fun insertTransaction(sellTransaction: SellTransaction) {
        viewModelScope.launch {
            val result = myRepository.insertTransaction(sellTransaction)
            if (result <= 0) {
                checkTransactionInsertLiveData.postValue(false)
                return@launch
            }

            val newList =
                allTransactionOriginalList.toMutableList() ?: ArrayList<SellTransaction>()
            newList.add(0, sellTransaction)

            allTransactionOriginalList.add(0, sellTransaction)

            allTransactionLiveData.postValue(newList)

        }
    }

    fun getAllTransactions(): LiveData<List<SellTransaction>> {
        viewModelScope.launch {
            val result = myRepository.getAllTransactions()
            allTransactionLiveData.postValue(result)
            Log.d("TRANSACTIONS", "getAllTransactions: ${result.toString()}")
        }
        return allTransactionLiveData
    }

}
