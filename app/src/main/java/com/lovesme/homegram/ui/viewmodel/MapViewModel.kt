package com.lovesme.homegram.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lovesme.homegram.data.model.Location
import com.lovesme.homegram.data.model.Result
import com.lovesme.homegram.data.repository.LocationRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MapViewModel @Inject constructor(private val repository: LocationRepository) : ViewModel() {

    private val _locations = MutableStateFlow<List<Location>>(listOf())
    val locations: StateFlow<List<Location>> = _locations

    val personalLocation = MutableStateFlow(Location(37.553836, 126.969652))

    fun loadLocation() {
        viewModelScope.launch {
            repository.getLocation().collect { result ->
                if (result is Result.Success) {
                    _locations.value = result.data
                }
            }
        }
    }

    fun updateLocation(location: Location) {
        viewModelScope.launch {
            repository.setLocation(location)
        }
    }
}