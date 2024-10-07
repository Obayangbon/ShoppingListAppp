package com.example.shoppinglistapp

/**
 * Data class representing a shopping item.
 * This class encapsulates the properties of a shopping item for easy management and manipulation.
 *
 * @property id Unique identifier for the shopping item.
 * @property name Name of the shopping item.
 * @property quantity Quantity of the item to be purchased.
 * @property isEditing Flag indicating whether the item is currently being edited (default is false).
 */
data class ShoppingItem(
    var id: Int,                        // Unique identifier for shopping item
    var name: String,                  // Name of the shopping item
    var quantity: Int,                 // Quantity of the item to be purchased
    var isEditing : Boolean = false   // Flag to indicate if the item in editing
)