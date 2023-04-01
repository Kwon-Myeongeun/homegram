package com.lovesme.homegram.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lovesme.homegram.data.model.Result
import com.lovesme.homegram.data.model.Todo
import com.lovesme.homegram.data.repository.TodoRepository
import com.lovesme.homegram.data.usecase.SendNotificationUseCase
import com.lovesme.homegram.util.Constants
import com.lovesme.homegram.util.DateFormatText
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CalendarViewModel @Inject constructor(
    private val repository: TodoRepository,
    private val sendNotificationUseCase: SendNotificationUseCase
) : ViewModel() {

    @Inject
    lateinit var dateFormatText: DateFormatText

    private val _todo = MutableLiveData<Map<String, Todo>>()
    val todo: LiveData<Map<String, Todo>> = _todo

    private val _date = MutableLiveData<String>()
    val date: LiveData<String> = _date

    fun loadTodo(date: String) {
        viewModelScope.launch {
            val result = repository.getSchedule(date)
            if (result is Result.Success) {
                _todo.value = result.data as Map<String, Todo>
            }
        }
    }

    fun writeTodo(date: String, contents: String) {
        viewModelScope.launch {
            val result = repository.addSchedule(date, contents)
            sendNotificationUseCase.invoke(Constants.userId.toString(), "")
        }
    }

    fun changeDate(year: Int, month: Int, dayOfMonth: Int) {
        _date.value = dateFormatText.convertToDateText(year, month, dayOfMonth)
    }


    fun deleteTodo(key: String) {
        viewModelScope.launch {
            val result = date.value?.let { repository.deleteSchedule(it, key) }
        }
    }
}