package com.lovesme.homegram.data.repository.impl

import android.util.Log
import com.google.firebase.messaging.FirebaseMessaging
import com.lovesme.homegram.data.datasource.SignInRemoteDataSource
import com.lovesme.homegram.data.model.Result
import com.lovesme.homegram.data.repository.SignInRepository
import com.lovesme.homegram.data.usecase.SetMessageTokenUseCase
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class SignInRepositoryImpl @Inject constructor(
    private val signInDataSource: SignInRemoteDataSource,
    private val setMessageTokenUseCase: SetMessageTokenUseCase
) :
    SignInRepository {

    override suspend fun saveLogInUserInfo(): Result<Unit> {
        signInDataSource.saveLogInUserInfo()
        val token = FirebaseMessaging.getInstance().token.await()
        Log.d("FCM", token)
        return setMessageTokenUseCase(token)
    }

    override suspend fun joinToInvitedGroup(groupId: String): Result<Unit> {
        return signInDataSource.joinToInvitedGroup(groupId)
    }

    override suspend fun updateUserInfo(
        groupId: String,
        name: String,
        birth: String
    ): Result<Unit> {
        TODO("Not yet implemented")
    }

}