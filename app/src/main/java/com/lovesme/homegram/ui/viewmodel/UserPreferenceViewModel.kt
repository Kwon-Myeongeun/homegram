package com.lovesme.homegram.ui.viewmodel

import androidx.lifecycle.ViewModel
import com.lovesme.homegram.data.repository.UserPreferencesRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class UserPreferenceViewModel @Inject constructor(
    private val repository: UserPreferencesRepository
) : ViewModel() {
    suspend fun updateUserInfo(name: String, birth: String) =
        repository.updateUserInfo(name, birth)
}