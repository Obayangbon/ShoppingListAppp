package com.example.shoppinglistapp

/**
 * Data class representing a shopping item with id, name,quantity
 * Encapsulates the item's properties for easy management and manipulation.
 */
data class ShoppingItem(
    var id: Int,
    var name: String,
    var quantity: Int,
    var isEditing : Boolean = false
)