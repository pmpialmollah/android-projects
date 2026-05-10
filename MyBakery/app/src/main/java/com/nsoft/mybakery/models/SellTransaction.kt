package com.nsoft.mybakery.models

class SellTransaction(
    val id: Int,
    val customer: Customer?,
    val products: ArrayList<HashMap<Product, Double>>,
    val product_total_price: Double,
    val total_paid: Double,
    val total_due: Double,
    val seller: Seller,
    val timestamp: String
)