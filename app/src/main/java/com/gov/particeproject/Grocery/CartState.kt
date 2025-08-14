package com.gov.particeproject.Grocery

import androidx.compose.runtime.mutableStateMapOf

class CartState(private val products: List<Product>) {
    private val cartItems = mutableStateMapOf<String, Int>()

    fun getCount(product: Product): Int = cartItems[product.name] ?: 0

    fun addItem(product: Product): Boolean {
        val current = getCount(product)
        return if (current < 5) {
            cartItems[product.name] = current + 1
            true
        } else {
            false
        }
    }

    fun getAllItems(): Map<String, Int> = cartItems

    fun getProductMap(): Map<String, Product> =
        products.associateBy { it.name }

    fun increaseItem(product: Product) {
        val current = getCount(product)
        if (current < 5) {
            cartItems[product.name] = current + 1
        }
    }

    fun decreaseItem(product: Product) {
        val current = getCount(product)
        if (current > 1) {
            cartItems[product.name] = current - 1
        } else {
            cartItems.remove(product.name) // Remove if quantity hits 0
        }
    }

    fun removeItem(product: Product) {
        cartItems.remove(product.name)
    }
    fun getTotalCount(): Int {
        return cartItems.values.sum()
    }

}

