package com.lovesme.homegram.ui.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import com.google.firebase.messaging.FirebaseMessaging
import com.lovesme.homegram.data.repository.SignInRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

@HiltViewModel
class SignInViewModel @Inject constructor(
    private val signInRepository: SignInRepository
) : ViewModel() {

    suspend fun saveLogInUserInfo() {
        CoroutineScope(Dispatchers.IO).launch {
            Log.d("FCM", "saveLogInUserInfo Start")
            val token = FirebaseMessaging.getInstance().token.await()
            Log.d("FCM", token)
            signInRepository.saveLogInUserInfo()
        }
    }

    suspend fun joinToInvitedGroup(groupId: String) {
        CoroutineScope(Dispatchers.IO).launch {
            signInRepository.joinToInvitedGroup(groupId)
        }
    }
}