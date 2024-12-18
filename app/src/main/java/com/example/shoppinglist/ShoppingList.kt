package com.example.shoppinglist

import android.widget.Toast
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
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp

data class ShoppingItem(
    val id:Int,
    var name: String,
    var quantity: Int,
    var isEditing: Boolean = false
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ShoppingList() {
    var shoppingItems by remember { mutableStateOf(listOf<ShoppingItem>()) }
    var showDialog by remember { mutableStateOf(false) }
    var itemName by remember { mutableStateOf("") }
    var itemQuantity by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = 56.dp),
        verticalArrangement = Arrangement.Center
    ) {
        Button(
            onClick = {
                showDialog = true
            },
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
        ) {
            Text(
                text = "Add item"
            )
        }
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            items(shoppingItems){
                    item ->
                if(item.isEditing) {
                    ShoppingItemEditor(
                        item = item,
                        onEditComplete = {
                                editedName, editedQuantity ->
                            shoppingItems = shoppingItems.map { it.copy(
                                isEditing = false
                            )}
                            val editedItem = shoppingItems.find { it.id == item.id }
                            editedItem?.let {
                                it.name = editedName
                                it.quantity = editedQuantity
                            }
                        }
                    )
                } else {
                    ShoppingListItem(
                        item = item,
                        onEdit = {
                            shoppingItems = shoppingItems.map {
                                it.copy(
                                    isEditing = it.id == item.id
                                )
                            }
                        },
                        onDelete = {
                            shoppingItems = shoppingItems - item
                        }
                    )
                }
            }
        }
    }

    if(showDialog){
        itemQuantity = "1"
        var context = LocalContext.current
        AlertDialog(
            onDismissRequest = { showDialog = false },
            confirmButton = {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Button(
                        onClick = {
                            if(itemName.isNotBlank()) {
                                val newItem = ShoppingItem(
                                    id = shoppingItems.size + 1,
                                    name = itemName,
                                    quantity = itemQuantity.toInt() // check this is the number nor
                                )
                                shoppingItems = shoppingItems + newItem
                                showDialog = false
                                itemName = ""
                            } else {
                                Toast.makeText(context,"Please enter a item name",Toast.LENGTH_SHORT).show()
                            }
                        }
                    ) {
                        Text("Add")
                    }
//                        Spacer(modifier = Modifier.width(80.dp))
                    Button( onClick = {
                        showDialog = false
                    }
                    ) {
                        Text("Cancel")
                    }
                }
            },
            title = {
                Text(
                    "Add shopping item"
                )
            },
            text = {
                Column {
                    OutlinedTextField(
                        value = itemName,
                        onValueChange = {
                            itemName = it
                        },
                        label = {
                            Text("Item name")
                        },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                            .padding(8.dp)
                    )
                    OutlinedTextField(
                        value = itemQuantity,
                        onValueChange = {
                            itemQuantity = it
                        },
                        label = {
                            Text("Item quantity")
                        },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                            .padding(8.dp),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                    )
                }
            }
        )
    }
}


@Composable
private fun ShoppingListItem(
    item: ShoppingItem,
    onEdit: () -> Unit,
    onDelete: () -> Unit
){
    Row(
        modifier = Modifier.padding(8.dp).fillMaxWidth().border(
            border = BorderStroke(2.dp, Color.Cyan),
            shape = RoundedCornerShape(20)
        ),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = item.name,
            modifier = Modifier.padding(8.dp)
        )
        Text(
            text = "Qty: ${item.quantity}",
            modifier = Modifier.padding(8.dp)
        )
        Row(
            modifier = Modifier.padding(8.dp)
        ){
            IconButton(
                onClick = onEdit
            ) {
                Icon(
                    imageVector = Icons.Default.Edit,
                    contentDescription = "Edit"
                )
            }
            IconButton(
                onClick = onDelete
            ) {
                Icon(
                    imageVector = Icons.Default.Delete,
                    contentDescription = "Delete"
                )
            }
        }
    }
}

@Composable
private fun ShoppingItemEditor(
    item: ShoppingItem,
    onEditComplete: (String, Int) -> Unit,
) {
    var editedName by remember { mutableStateOf(item.name) }
    var editedQuantity by remember { mutableStateOf(item.quantity.toString()) }
    var isEditing by remember { mutableStateOf(item.isEditing) }

    Row(
        modifier = Modifier.fillMaxWidth()
            .background(Color.LightGray)
            .padding(8.dp),
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        Column {
            BasicTextField(
                value = editedName,
                onValueChange = {
                    editedName = it
                },
                singleLine = true,
                modifier = Modifier.padding(8.dp)
                    .wrapContentSize()
            )
            BasicTextField(
                value = editedQuantity,
                onValueChange = {
                    editedQuantity = it
                },
                singleLine = true,
                modifier = Modifier.padding(8.dp)
                    .wrapContentSize()
            )
        }
        Button(
            onClick = {
                onEditComplete(editedName, editedQuantity.toIntOrNull() ?: 1)
                isEditing = false
            }
        ) {
            Text("Save")
        }
    }
}