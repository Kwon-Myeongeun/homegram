package com.lovesme.homegram.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lovesme.homegram.data.datasource.UserInfoLocalDataSource
import com.lovesme.homegram.data.model.Result
import com.lovesme.homegram.data.model.UiState
import com.lovesme.homegram.data.repository.UserPreferencesRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingViewModel @Inject constructor(
    private val userInfoLocalDataSource: UserInfoLocalDataSource,
    private val userPreferencesRepository: UserPreferencesRepository,
) : ViewModel() {

    private val _uiState: MutableStateFlow<UiState<Unit>> = MutableStateFlow(UiState.Empty)
    val uiState: StateFlow<UiState<Unit>> = _uiState

    suspend fun getGroupId(): String {
        return userInfoLocalDataSource.getGroupId()
    }

    fun deleteUserInfo() {
        _uiState.value = UiState.Loading
        viewModelScope.launch {
            val result = userPreferencesRepository.deleteUserInfo()
            if (result is Result.Success) {
                _uiState.value = UiState.Success(Unit)
            } else {
                _uiState.value = UiState.Error
            }
        }
    }
}