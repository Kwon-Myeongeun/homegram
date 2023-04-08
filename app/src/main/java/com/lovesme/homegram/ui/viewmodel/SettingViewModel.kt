package com.lovesme.homegram.ui.viewmodel

import androidx.lifecycle.ViewModel
import com.lovesme.homegram.data.datasource.UserInfoLocalDataSource
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SettingViewModel @Inject constructor(
    private val userInfoLocalDataSource: UserInfoLocalDataSource
) : ViewModel() {
    suspend fun getGroupId(): String {
        return userInfoLocalDataSource.getGroupId()
    }
}