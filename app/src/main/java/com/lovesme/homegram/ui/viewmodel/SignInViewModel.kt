package com.lovesme.homegram.ui.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.messaging.FirebaseMessaging
import com.lovesme.homegram.data.repository.SignInRepository
import com.lovesme.homegram.data.usecase.SetMessageTokenUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SignInViewModel @Inject constructor(
    private val signInRepository: SignInRepository
) : ViewModel() {

    @Inject
    lateinit var setMessageTokenUseCase: SetMessageTokenUseCase

    fun saveLogInUserInfo() {
        viewModelScope.launch {
            signInRepository.saveLogInUserInfo()
            Log.d("FCM", "saveLogInUserInfo Start")
            FirebaseMessaging.getInstance().token.addOnCompleteListener(
                OnCompleteListener { task ->
                    if (!task.isSuccessful) {
                        return@OnCompleteListener
                    }
                    GlobalScope.launch {
                        setMessageTokenUseCase(task.result)
                        Log.d("FCM", task.result)
                    }
                })
        }
    }

    fun joinToInvitedGroup(groupId: String) {
        viewModelScope.launch {
            signInRepository.joinToInvitedGroup(groupId)
        }
    }
}