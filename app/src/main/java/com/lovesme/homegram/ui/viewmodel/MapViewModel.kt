package com.lovesme.homegram.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lovesme.homegram.data.model.Location
import com.lovesme.homegram.data.model.Result
import com.lovesme.homegram.data.repository.LocationRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MapViewModel @Inject constructor(private val repository: LocationRepository) : ViewModel() {

    private val _locations = MutableLiveData<List<Location>>()
    val locations: LiveData<List<Location>> = _locations

    val personalLocation = MutableStateFlow(Location(37.553836, 126.969652))

    fun loadLocation() {
        viewModelScope.launch {
            repository.getLocation().collect { result ->
                if (result is Result.Success) {
                    _locations.value = result.data as List<Location>
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