package com.lovesme.homegram.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lovesme.homegram.data.datasource.DailyLocalDataSource
import com.lovesme.homegram.data.model.Question
import com.lovesme.homegram.data.repository.QuestionRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject
import com.lovesme.homegram.data.model.Result
import com.lovesme.homegram.data.repository.SyncRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.first

@HiltViewModel
class DailyViewModel @Inject constructor(
    private val questionRepository: QuestionRepository,
    private val syncRepository: SyncRepository,
    private val dailyLocalDataSource: DailyLocalDataSource
) : ViewModel() {

    private val _questions = MutableStateFlow<List<Question>>(listOf())
    val questions: StateFlow<List<Question>> = _questions

    private val _connect = MutableStateFlow<Boolean>(true)
    val connect: StateFlow<Boolean> = _connect

    fun loadQuestion() {
        viewModelScope.launch {
            val isConnect = syncRepository.isConnect().first()
            if (isConnect is Result.Success) {
                if (isConnect.data) {
                    questionRepository.getQuestion().collectLatest { result ->
                        if (result is Result.Success) {
                            _connect.value = true
                            _questions.value = result.data
                        }
                    }
                } else {

                    _connect.value = false
                    _questions.value = dailyLocalDataSource.getAllQuestion()
                }
            } else {

                _connect.value = false
                _questions.value = dailyLocalDataSource.getAllQuestion()
            }
        }
    }
}