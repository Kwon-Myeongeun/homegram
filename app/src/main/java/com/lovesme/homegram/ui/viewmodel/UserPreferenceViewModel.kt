package com.lovesme.homegram.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lovesme.homegram.data.repository.UserPreferencesRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UserPreferenceViewModel @Inject constructor(
    private val repository: UserPreferencesRepository
) : ViewModel() {
    fun updateUserInfo(name: String, birth: String) {
        viewModelScope.launch {
            repository.updateUserInfo(name, birth)
        }
    }
}