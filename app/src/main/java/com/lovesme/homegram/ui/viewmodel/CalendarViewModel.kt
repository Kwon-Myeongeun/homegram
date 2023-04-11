package com.lovesme.homegram.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lovesme.homegram.data.model.NotificationType
import com.lovesme.homegram.data.model.Result
import com.lovesme.homegram.data.model.Todo
import com.lovesme.homegram.data.repository.TodoRepository
import com.lovesme.homegram.data.usecase.SendNotificationUseCase
import com.lovesme.homegram.util.Constants
import com.lovesme.homegram.util.DateFormatText
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CalendarViewModel @Inject constructor(
    private val repository: TodoRepository,
    private val sendNotificationUseCase: SendNotificationUseCase,
    private val dateFormatText: DateFormatText
) : ViewModel() {

    private val _todo = MutableStateFlow<Map<String, Todo>>(mapOf())
    val todo: StateFlow<Map<String, Todo>> = _todo

    private val _date = MutableStateFlow(dateFormatText.getTodayText())
    val date: StateFlow<String> = _date

    fun loadTodo(date: String) {
        viewModelScope.launch {
            val result = repository.getSchedule(date).first()
            if (result is Result.Success) {
                _todo.value = result.data
            }
        }
    }

    fun writeTodo(date: String, contents: String) {
        viewModelScope.launch {
            val result = repository.addSchedule(date, contents)
            sendNotificationUseCase.invoke(
                NotificationType.UPDATE_TODO,
                Constants.userId.toString(),
            )
        }
    }

    fun changeDate(year: Int, month: Int, dayOfMonth: Int) {
        _date.value = dateFormatText.convertToDateText(year, month, dayOfMonth)
    }

    fun deleteTodo(key: String) {
        viewModelScope.launch {
            val result = repository.deleteSchedule(date.value, key)
        }
    }
}