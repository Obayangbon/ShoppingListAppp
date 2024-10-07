package com.example.shoppinglistapp

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp



/**
 * Composable function for the main shopping list App.
 *
 * This function manages the state for the list of shopping items, triggers the Add Item dialog,
 * and displays the shopping list. It serves as the core UI and state manager for the shopping app.
 *
 * @param modifier Modifier to adjust the layout or appearance of the Composable.
 */
@Composable
fun ShoppingListApp(modifier :Modifier) {
    // State variables to manage shopping items, dialog visibility, and input fields.
    var shoppingItems by remember { mutableStateOf(listOf<ShoppingItem>()) }
    var showDialog by remember { mutableStateOf(false) }
    var itemName by remember { mutableStateOf("") }
    var itemQuantity by remember { mutableStateOf("") }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally

    ) {
        // Button to trigger adding a new item
        Button(
            onClick = { showDialog = true},
            modifier = Modifier.padding(vertical = 8.dp)

        ) {
            Text(text = "Add Item")
        }

        // Render shopping list items with LazyColumn for performance
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(shoppingItems) { item ->
                if(item.isEditing){
                    ShoppingItemEditor(item = item, onEditComplete = { editedName, editedQuantity ->
                        // Update the item in the list after editing
                        shoppingItems = shoppingItems.map {
                            if(it.id == item.id){
                                it.copy(name = editedName, quantity = editedQuantity, isEditing = false)
                            } else{
                                it
                            }
                        }
                    })
                }else{
                    ShoppingListItem(
                        item = item,
                        onEditClick = {
                            // Switch to edit mode gor the selected item
                        shoppingItems = shoppingItems.map {
                            it.copy(isEditing = it.id == item.id) }
                    },
                        onDeleteClick = {
                            // Remove item from the list
                        shoppingItems = shoppingItems - item
                    })
                }

            }
        }
    }

    if (showDialog) {
        AlertDialog(
            onDismissRequest = {showDialog = false},
            title = {Text("Add Shopping Item", style = MaterialTheme.typography.headlineSmall)},
            text = {
                // Input for item name and quantity
                Column(modifier = Modifier.fillMaxWidth()) {
                    OutlinedTextField(
                        value = itemName,
                        onValueChange = {itemName = it},
                        label = { Text("Item Name")},
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth().padding(8.dp)
                    )
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
                Row(
                    modifier = Modifier.fillMaxWidth().padding(8.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ){
                    Button(onClick = {
                        // Validate and add the new item
                        if(itemName.isNotEmpty()){
                            shoppingItems = shoppingItems + ShoppingItem(
                                id = shoppingItems.size +1,
                                name = itemName,
                                quantity = itemQuantity.toIntOrNull() ?: 1 // Default to 1 if parsing fails
                            )
                            showDialog = false
                            itemName = ""   // Reset input fields after adding
                            itemQuantity = ""
                        }
                    }) {
                        Text("Add")
                    }
                    Button(onClick = {}) {
                        Text("Cancel")
                    }
                }
            }

        )

    }
}

/**
 * Composable for editing a shopping item.
 * This UI allows the user to modify the name and quantity of a shopping item and save the changes.
 *
 * @param item The shopping item to be edited.
 * @param onEditComplete Callback invoked when editing is complete with the updated name and quantity.
 */
@Composable
fun ShoppingItemEditor(item : ShoppingItem, onEditComplete: (String, Int) -> Unit){
    var editedName by remember { mutableStateOf(item.name)}
    var editedQuantity by remember { mutableStateOf(item.quantity.toString()) }
    var isEditing by remember { mutableStateOf(item.isEditing) }

    // Editing UI for name and quantity
    Row(modifier = Modifier
        .fillMaxWidth()
        .background(Color.White)
        .padding(8.dp),
        horizontalArrangement = Arrangement.SpaceEvenly
    ){
        // Colum to hold the text fields for name and quantity editing
        Column{
            BasicTextField(
                value = editedName,
                onValueChange = { editedName = it},
                singleLine = true,
                modifier = Modifier
                    .wrapContentSize()
                    .padding(8.dp)
            )
            BasicTextField(
                value = editedQuantity,
                onValueChange = {editedQuantity = it},
                singleLine = true,
                modifier = Modifier
                    .wrapContentSize()
                    .padding(8.dp)
            )

        }

        // Save button to confirm the changes made to the item
        Button(onClick = {
            isEditing = false

            // Invoke the callback to save edited name and quantity
            // Quantity defaults to 1 if the input is invalid or empty
            onEditComplete(editedName, editedQuantity.toIntOrNull() ?: 1)
        }) {
            Text(text = "Save")
        }
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
fun ShoppingListItem(item :  ShoppingItem, onEditClick : () -> Unit, onDeleteClick : () -> Unit){
    // Row layout for displaying the item, with options for editing and deleting.
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .border(border = BorderStroke(2.dp, Color(0XFF018786)),
                shape = RoundedCornerShape(20)
            ),
        horizontalArrangement = Arrangement.SpaceBetween
    ){
        // Display the item name with padding for better readability
        Text(text = item.name, modifier = Modifier.padding(8.dp))

        // Display the item quantity with padding for better readability
        Text(text = "Qty: ${item.quantity}", modifier = Modifier.padding(8.dp))

        // Row fo action buttons (edit and delete)
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


