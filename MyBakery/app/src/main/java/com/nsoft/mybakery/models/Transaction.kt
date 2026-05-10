package com.nsoft.mybakery.models

class Transaction(
    val id: Int,
    val customer: Customer,
    val products: Product,
    val product_quantity: Float,
    val product_total_price: Float,
    val total_payed: Float,
    val total_due: Float,
    val seller: Seller,
    val timestamp: String
)