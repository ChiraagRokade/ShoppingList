package com.example.shoppinglist

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHost
import androidx.navigation.Navigation
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.dialog
import androidx.navigation.compose.rememberNavController
import com.example.shoppinglist.helper.LocationUtils
import com.example.shoppinglist.ui.theme.ShoppingListTheme
import com.example.shoppinglist.viewModel.LocationViewModel
import com.example.shoppinglist.views.LocationSelectionScreen

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ShoppingListTheme {
                Surface(
                    modifier = Modifier.fillMaxSize()
                ) {
                    //ShoppingList()
                    Navigation()
                }
            }
        }
    }
}

@Composable
fun Navigation(){
    val nacController = rememberNavController()
    val viewModel: LocationViewModel = viewModel()
    val context = LocalContext.current
    val locationUtils = LocationUtils(context)

    NavHost(navController = nacController, startDestination = "shoppingList"){
        composable("shoppingList"){
            ShoppingList(locationUtils, viewModel, nacController, context, address = viewModel.address.value.firstOrNull()?.formatted_address ?: "No address")
        }

        dialog("locationSelectionScreen"){backstackt ->
            viewModel.location.value?.let {
                it1 -> LocationSelectionScreen(location = it1, onLocationSelected = { locationdata-> viewModel.fetchAddress("${locationdata.latitude},${locationdata.longitude}")
                nacController.popBackStack()
                })
            }
        }
    }
}