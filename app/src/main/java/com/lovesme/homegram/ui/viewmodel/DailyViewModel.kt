package com.lovesme.homegram.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lovesme.homegram.data.model.Question
import com.lovesme.homegram.data.repository.QuestionRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject
import com.lovesme.homegram.data.model.Result
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest

@HiltViewModel
class DailyViewModel @Inject constructor(private val repository: QuestionRepository) : ViewModel() {

    private val _questions = MutableStateFlow<List<Question>>(listOf())
    val questions: StateFlow<List<Question>> = _questions

    fun loadQuestion() {
        viewModelScope.launch {
            repository.getQuestion().collectLatest { result ->
                if (result is Result.Success) {
                    _questions.value = result.data
                }
            }
        }
    }
}