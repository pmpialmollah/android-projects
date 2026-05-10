package com.nsoft.mybakery.models

class Product(
    var id: Int,
    var image: String,
    var name: String,
    var quantity: Double = 0.0,
    var price: Double = 0.0
)
