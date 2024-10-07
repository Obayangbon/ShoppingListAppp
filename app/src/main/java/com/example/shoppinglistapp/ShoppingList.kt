package com.example.shoppinglistapp

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.unit.dp
import kotlin.math.sin

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


/**
 * Composable function for the main shopping list App.
 * This manages the state for the list of items, triggers the Add Item dialog,and displays the shopping list.
 * The function serves as the core UI and state manager for the shopping app.
 */
@Composable
fun ShoppingListApp(modifier :Modifier) {
    //List to store shopping items. Using mutable state ensures the UI updates when new items are added.
    var shoppingItems by remember { mutableStateOf(listOf<ShoppingItem>()) }

    // Controls whether the "Add Item" dialog is visible. False by default
    var showDialog by remember { mutableStateOf(false) }

    // Holds the input for item name and quantity, resent after adding an item
    var itemName by remember { mutableStateOf("") }
    var itemQuantity by remember { mutableStateOf("") }

    // Layout for the entire app
    Column(
        modifier = modifier.fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally

    ) {
        // Button to show the dialog for adding a new item
        Button(
            onClick = { showDialog = true},
            modifier = Modifier.padding(vertical = 8.dp)

        ) {
            Text(text = "Add Item")
        }

        // LazyColum efficiently renders only visible list items, improving performance for long lists.
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(shoppingItems) {
                ShoppingListItem(it, {}, {})

            }
        }
    }

    // Show the "Add Item: dialog when showDialog is true
    if (showDialog) {
        AlertDialog(
            onDismissRequest = {showDialog = false},
            title = {Text("Add Shopping Item", style = MaterialTheme.typography.headlineSmall)},
            text = {
                //The fields to input item name and quantity. Column organized fields vertically
                Column(modifier = Modifier.fillMaxWidth()) {
                    // Input for Item name. State automatically updates when user types
                    OutlinedTextField(
                        value = itemName,
                        onValueChange = {itemName = it},
                        label = { Text("Item Name")},
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth().padding(8.dp)
                    )
                    // Input for item quantity. Follow the same structure as the item name field
                    OutlinedTextField(
                        value = itemQuantity,
                        onValueChange = {itemQuantity = it},
                        label = { Text("Quantity")},
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth().padding(8.dp)
                    )
                }
            },
            confirmButton = {
                // Provides two options to the user - add the item or cancel.
                Row(
                    modifier = Modifier.fillMaxWidth().padding(8.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ){
                    Button(onClick = {
                        if(itemName.isNotEmpty()){
                            var newItem = ShoppingItem(
                                id = shoppingItems.size +1,
                                name = itemName,
                                quantity = itemQuantity.toInt()
                            )

                            shoppingItems = shoppingItems + newItem
                            showDialog = false
                            itemName = ""
                            itemQuantity = ""
                        }



                    }) {
                        Text("Add")
                    }
                    Button(onClick = {}) {
                        Text("Cancel")
                    }
                }
            },

        )

    }
}

/**
 * Composable to display a single shopping item with edit and delete options.
 * This UI element shows the item's details and allows the user to either edit or delete the item.
 *
 * @param item The shopping item to display.
 * @param onEditClick Action triggered when the edit button is clicked.
 * @param onDeleteClick Action triggered when the delete button is clicked.
 */
@Composable
fun ShoppingListItem(
    item :  ShoppingItem,
    onEditClick : () -> Unit,
    onDeleteClick : () -> Unit
){
    // Row layout for displaying the item, with options for editing and deleting.
    Row(
        modifier = Modifier.fillMaxWidth().padding(8.dp)
            .border(border = BorderStroke(2.dp, Color(0XFF018786)),
                shape = RoundedCornerShape(20)
            )
    ){
        Text(text = item.name, modifier = Modifier.padding(8.dp))
        //Spacer(modifier = Modifier.padding(16.dp))
        Text(text = "Qty: ${item.quantity}", modifier = Modifier.padding(8.dp))

        Row(modifier = Modifier.padding(8.dp)){
            // Edit button icon triggers the onEditClick lambda when pressed.
            IconButton(onClick = onEditClick) {
                Icon(imageVector = Icons.Default.Edit, contentDescription = null)
            }
            // Delete button icon triggers the DeleteClick lambda when pressed.
            IconButton(onClick = onDeleteClick) {
                Icon(imageVector = Icons.Default.Delete, contentDescription = null)
            }
        }
    }
}


