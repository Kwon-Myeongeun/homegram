package com.lovesme.homegram.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.messaging.FirebaseMessaging
import com.lovesme.homegram.data.model.Result
import com.lovesme.homegram.data.model.UiState
import com.lovesme.homegram.data.repository.SignInRepository
import com.lovesme.homegram.data.repository.UserPreferencesRepository
import com.lovesme.homegram.data.usecase.SetMessageTokenUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

@HiltViewModel
class SignInViewModel @Inject constructor(
    private val signInRepository: SignInRepository,
    private val setMessageTokenUseCase: SetMessageTokenUseCase,
    private val userPreferencesRepository: UserPreferencesRepository,
) : ViewModel() {

    private val _uiState: MutableStateFlow<UiState<Unit>> = MutableStateFlow(UiState.Empty)
    val uiState: StateFlow<UiState<Unit>> = _uiState
    private val auth = FirebaseAuth.getInstance()
    private val authStateListener by lazy {
        FirebaseAuth.AuthStateListener {
            viewModelScope.launch {
                saveLogInUserInfo()
            }
        }
    }

    init {
        auth.addAuthStateListener(authStateListener)
    }

    private fun saveLogInUserInfo() {
        _uiState.value = UiState.Loading
        viewModelScope.launch {
            val existUser = userPreferencesRepository.existUser()
            if (existUser is Result.Success) {
                if (!existUser.data) {
                    val result = signInRepository.saveLogInUserInfo()
                    if (result is Result.Success) {
                        val token = FirebaseMessaging.getInstance().token.await()
                        setMessageTokenUseCase(token)
                        _uiState.value = UiState.Success(Unit)
                    } else {
                        _uiState.value = UiState.Error
                    }
                }
            } else {
                _uiState.value = UiState.Error
            }
        }
    }

    fun joinToInvitedGroup(groupId: String) {
        viewModelScope.launch {
            val result = userPreferencesRepository.existGroupId(groupId)
            if (result is Result.Success) {
                if (result.data) {
                    signInRepository.joinToInvitedGroup(groupId)
                }
            }
        }
    }

    override fun onCleared() {
        auth.removeAuthStateListener(authStateListener)
        super.onCleared()
    }
}