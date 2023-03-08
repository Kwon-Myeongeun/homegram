package com.lovesme.homegram.presentation.ui.viewmodel

import androidx.lifecycle.ViewModel
import com.lovesme.homegram.data.repository.SignInRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SignInViewModel @Inject constructor(
    private val signInRepository: SignInRepository
) : ViewModel() {

    suspend fun saveLogInUserInfo() {
        CoroutineScope(Dispatchers.IO).launch {
            signInRepository.saveLogInUserInfo()
        }
    }

    suspend fun joinToInvitedGroup(groupId: String) {
        CoroutineScope(Dispatchers.IO).launch {
            signInRepository.joinToInvitedGroup(groupId)
        }
    }
}