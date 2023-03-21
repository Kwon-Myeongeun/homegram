package com.lovesme.homegram.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lovesme.homegram.data.model.Location
import com.lovesme.homegram.data.model.Result
import com.lovesme.homegram.data.repository.LocationRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MapViewModel @Inject constructor(private val repository: LocationRepository) : ViewModel() {

    private val _locations = MutableLiveData<List<Location>>()
    val location: LiveData<List<Location>> = _locations

    fun loadLocation() {
        viewModelScope.launch {
            repository.getLocation().collect { result ->
                if (result is Result.Success) {
                    _locations.value = result.data as List<Location>
                }
            }
        }
    }
}