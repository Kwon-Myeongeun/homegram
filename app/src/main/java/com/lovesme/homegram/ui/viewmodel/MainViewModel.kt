package com.lovesme.homegram.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.messaging.FirebaseMessaging
import com.lovesme.homegram.data.model.Result
import com.lovesme.homegram.data.model.UiState
import com.lovesme.homegram.data.repository.SyncRepository
import com.lovesme.homegram.data.usecase.SetMessageTokenUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val repository: SyncRepository,
    private val setMessageTokenUseCase: SetMessageTokenUseCase,
) : ViewModel() {

    private val _uiState: MutableStateFlow<UiState<Unit>> = MutableStateFlow(UiState.Empty)
    val uiState: StateFlow<UiState<Unit>> = _uiState

    fun startSync() {
        _uiState.value = UiState.Loading
        viewModelScope.launch {
            val result = repository.syncStart()
            if (result is Result.Success) {
                val token = FirebaseMessaging.getInstance().token.await()
                setMessageTokenUseCase(token)
                _uiState.value = UiState.Success(Unit)
            } else {
                _uiState.value = UiState.Error
            }
        }
    }
}