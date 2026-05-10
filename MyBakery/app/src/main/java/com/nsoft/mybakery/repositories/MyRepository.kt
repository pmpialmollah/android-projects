package com.nsoft.mybakery.repositories

import android.content.Context
import com.nsoft.mybakery.models.Customer
import com.nsoft.mybakery.models.Product
import com.nsoft.mybakery.models.SellTransaction
import com.nsoft.mybakery.models.Transaction
import com.nsoft.mybakery.sqlitedatabase.DBHelper

class MyRepository(context: Context) {
    val myDatabase = DBHelper(context)

    suspend fun insertProduct(product: Product): Long {
        return myDatabase.insertProduct(product)
    }

    suspend fun getAllProducts(): List<Product> {
        return myDatabase.getAllProducts()
    }

    suspend fun deleteProductById(id: Int): Int {
        return myDatabase.deleteProductById(id)
    }

    suspend fun updateProduct(product: Product): Int {
        return myDatabase.updateProduct(product)
    }

    suspend fun insertCustomer(customer: Customer): Long {
        return myDatabase.insertCustomer(customer)
    }

    suspend fun getAllCustomers(): List<Customer> {
        return myDatabase.getAllCustomers()
    }

    suspend fun deleteCustomer(id: Int): Int {
        return myDatabase.deleteCustomer(id)
    }

    suspend fun updateCustomer(customer: Customer): Int {
        return myDatabase.updateCustomer(customer)
    }

    suspend fun insertTransaction(sellTransaction: SellTransaction): Long{
        return myDatabase.insertTransaction(sellTransaction)
    }

    suspend fun getAllTransactions() = myDatabase.getAllTransactions()

}