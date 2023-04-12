package com.lovesme.homegram.data.usecase

import com.lovesme.homegram.data.datasource.UserInfoLocalDataSource
import com.lovesme.homegram.data.repository.UserPreferencesRepository
import javax.inject.Inject

class SetMessageTokenUseCase @Inject constructor(
    private val repository: UserPreferencesRepository,
    private val userInfoLocalDataSource: UserInfoLocalDataSource,
) {
    suspend operator fun invoke(string: String) {
        repository.setMessageToken(string)
        userInfoLocalDataSource.updateToken(string)
    }
}