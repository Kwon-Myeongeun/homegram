package com.lovesme.homegram.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lovesme.homegram.data.model.NotificationType
import com.lovesme.homegram.data.repository.QuestionRepository
import com.lovesme.homegram.data.usecase.SendNotificationUseCase
import com.lovesme.homegram.util.Constants
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DailyCompositionViewModel @Inject constructor(
    private val repository: QuestionRepository,
    private val sendNotificationUseCase: SendNotificationUseCase
) : ViewModel() {

    fun updateAnswer(key: String, answer: String) {
        viewModelScope.launch {
            val result = repository.updateAnswer(key, answer)
            sendNotificationUseCase.invoke(
                NotificationType.UPDATE_ANSWER,
                Constants.userId.toString(),
                ""
            )
        }
    }
}