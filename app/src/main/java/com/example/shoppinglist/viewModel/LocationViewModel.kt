package com.example.shoppinglist.viewModel

import android.Manifest
import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.shoppinglist.apiservice.RetrofitClient
import com.example.shoppinglist.models.GeocodingResult
import com.example.shoppinglist.models.LocationData
import com.google.android.gms.maps.model.LatLng
import kotlinx.coroutines.launch

class LocationViewModel:ViewModel() {
    private val _location = mutableStateOf<LocationData?>(null)
    val location: State<LocationData?> = _location

    private val _address = mutableStateOf(listOf<GeocodingResult>())
    val address: State<List<GeocodingResult>> = _address

    fun updateLocation(newLocation: LocationData){
        _location.value = newLocation
    }

    fun fetchAddress(latLng: String){
        try {
            viewModelScope.launch {
                val response = RetrofitClient.create().getAddressFromCoordinates(latLng, "AIzaSyBanIftwChNDP4nPRxyAQxfEGYvreFlZGQ")

                _address.value = response.results
            }
        }
        catch (ex: Exception){
            Log.d("TAG", "fetchAddress: ${ex.message} ${ex.cause}")
        }
    }
}