package com.nsoft.mybakery.repositories

import android.content.Context
import com.nsoft.mybakery.models.Product
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
}