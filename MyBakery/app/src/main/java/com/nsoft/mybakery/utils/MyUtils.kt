package com.nsoft.mybakery.utils

import com.nsoft.mybakery.models.Product

class MyUtils {

    fun parseStringToMap(input: String): ArrayList<HashMap<Int, ArrayList<Double>>> {
        val productList = ArrayList<HashMap<Int, ArrayList<Double>>>()

        // Line break onujayi split kora (12 => 2.0)
        val lines = input.trim().split("\n")

        for (line in lines) {
            if (line.contains("=>")) {
                // "=>" diye split kore ID ar Quantity alada kora
                val parts = line.split("=>")

                if (parts.size >= 3) {
                    val id = parts[0].trim().toInt()
                    val price = parts[1].trim().toDouble()
                    val quantity = parts[2].trim().toDouble()

                    // HashMap toiri kora
                    val productMap = HashMap<Int, ArrayList<Double>>()
                    val details = ArrayList<Double>()
                    details.add(price)
                    details.add(quantity)
                    productMap[id] = details

                    // List-e add kora
                    productList.add(productMap)
                } else if (parts.size == 2) {
                    val id = parts[0].trim().toInt()
                    val quantity = parts[1].trim().toDouble()

                    // HashMap toiri kora
                    val productMap = HashMap<Int, ArrayList<Double>>()
                    val details = ArrayList<Double>()
                    details.add(0.0) // Default price if not found
                    details.add(quantity)
                    productMap[id] = details

                    // List-e add kora
                    productList.add(productMap)
                }
            }
        }
        return productList
    }
}