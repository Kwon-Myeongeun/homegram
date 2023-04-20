package com.lovesme.homegram.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lovesme.homegram.data.datasource.UserInfoLocalDataSource
import com.lovesme.homegram.data.model.Answer
import com.lovesme.homegram.data.model.NotificationType
import com.lovesme.homegram.data.model.Question
import com.lovesme.homegram.data.model.Result
import com.lovesme.homegram.data.repository.QuestionRepository
import com.lovesme.homegram.data.usecase.SendNotificationUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DailyDetailViewModel @Inject constructor(
    private val repository: QuestionRepository,
    private val sendNotificationUseCase: SendNotificationUseCase,
    private val userInfoLocalDataSource: UserInfoLocalDataSource,
) : ViewModel() {

    private val _answer = MutableStateFlow<List<Answer>>(listOf())
    val answer: StateFlow<List<Answer>> = _answer

    fun updateAnswer(no: String, key: String, answer: String) {
        viewModelScope.launch {
            val result = repository.updateAnswer(key, answer)
            if (result is Result.Success) {
                sendNotificationUseCase.invoke(
                    NotificationType.UPDATE_ANSWER,
                    no,
                )
            }
            refreshQuestion(key)
        }
    }

    suspend fun getMyAnswer(item: Question?): String? {
        val userName = userInfoLocalDataSource.getUserName()
        return item?.answer?.firstOrNull { it.name == userName }?.contents
    }

    private fun refreshQuestion(key: String) {
        viewModelScope.launch {
            repository.getQuestion().collectLatest { result ->
                if (result is Result.Success) {
                    _answer.value = result.data.first { it.key == key }.answer
                }
            }
        }
    }

    fun setAnswer(answer: List<Answer>?) {
        answer?.let {
            _answer.value = answer
        }
    }
}