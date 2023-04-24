package com.lovesme.homegram.ui.viewmodel

import androidx.lifecycle.ViewModel
import com.lovesme.homegram.data.model.Result
import com.lovesme.homegram.data.repository.QuestionRepository
import com.lovesme.homegram.data.repository.UserPreferencesRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.first
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val userPreferencesRepository: UserPreferencesRepository,
    private val questionRepository: QuestionRepository,
) : ViewModel() {

    suspend fun notExistMember(): Boolean {
        val result = userPreferencesRepository.existMember()
        return if (result is Result.Success) {
            !result.data
        } else {
            false
        }
    }

    suspend fun isNotAnswered(): Boolean {
        val result = questionRepository.getQuestion().first()
        return if (result is Result.Success) {
            !result.data.maxBy { it.no }.isDone
        } else {
            false
        }
    }
}