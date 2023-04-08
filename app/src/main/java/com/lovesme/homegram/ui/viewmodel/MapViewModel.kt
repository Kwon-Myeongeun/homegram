package com.lovesme.homegram.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lovesme.homegram.data.datasource.UserInfoLocalDataSource
import com.lovesme.homegram.data.model.Location
import com.lovesme.homegram.data.model.Result
import com.lovesme.homegram.data.repository.LocationRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MapViewModel @Inject constructor(
    private val repository: LocationRepository,
    private val userInfoLocalDataSource: UserInfoLocalDataSource,
) : ViewModel() {

    private val _locations = MutableStateFlow<List<Location>>(listOf())
    val locations: StateFlow<List<Location>> = _locations

    lateinit var name: String

    init {
        viewModelScope.launch {
            name = userInfoLocalDataSource.getUserName()
        }
    }

    fun loadLocation() {
        viewModelScope.launch {
            repository.getLocation().collect { result ->
                if (result is Result.Success) {
                    _locations.value = result.data
                }
            }
        }
    }
}