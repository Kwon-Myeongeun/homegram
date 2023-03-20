package com.lovesme.homegram.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lovesme.homegram.data.model.Question
import com.lovesme.homegram.data.repository.QuestionRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject
import com.lovesme.homegram.data.model.Result

@HiltViewModel
class DailyViewModel @Inject constructor(private val repository: QuestionRepository) : ViewModel() {

    private val _questions = MutableLiveData<List<Question>>()
    val questions: LiveData<List<Question>> = _questions

    fun loadQuestion() {
        viewModelScope.launch {
            val result = repository.getQuestion()
            if (result is Result.Success) {
                _questions.value = result.data as List<Question>
            }
        }
    }
}