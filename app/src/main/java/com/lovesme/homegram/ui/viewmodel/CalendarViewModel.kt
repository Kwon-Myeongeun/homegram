package com.lovesme.homegram.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lovesme.homegram.data.datasource.UserInfoLocalDataSource
import com.lovesme.homegram.data.model.NotificationType
import com.lovesme.homegram.data.model.Result
import com.lovesme.homegram.data.model.Todo
import com.lovesme.homegram.data.repository.SyncRepository
import com.lovesme.homegram.data.repository.TodoRepository
import com.lovesme.homegram.data.usecase.SendNotificationUseCase
import com.lovesme.homegram.util.DateFormatText
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CalendarViewModel @Inject constructor(
    private val repository: TodoRepository,
    private val sendNotificationUseCase: SendNotificationUseCase,
    private val dateFormatText: DateFormatText,
    private val syncRepository: SyncRepository,
    private val userInfoLocalDataSource: UserInfoLocalDataSource,
) : ViewModel() {

    private val _todo = MutableStateFlow<List<Todo>>(listOf())
    val todo: StateFlow<List<Todo>> = _todo

    private val _date = MutableStateFlow(dateFormatText.getTodayText())
    val date: StateFlow<String> = _date

    private val _connect = MutableStateFlow<Boolean>(true)
    val connect: StateFlow<Boolean> = _connect

    fun loadTodo(date: String) {
        viewModelScope.launch {
            val isConnect = syncRepository.isConnect().first()
            if (isConnect is Result.Success) {
                if (isConnect.data) {
                    repository.getSchedule(date).collectLatest { result ->
                        if (result is Result.Success) {
                            _connect.value = true
                            _todo.value = result.data
                        }
                    }
                } else {
                    _connect.value = false
                    _todo.value = userInfoLocalDataSource.getAllTodo()
                }
            } else {
                _connect.value = false
                _todo.value = userInfoLocalDataSource.getAllTodo()
            }
        }
    }

    fun writeTodo(date: String, contents: String) {
        viewModelScope.launch {
            val result = repository.addSchedule(date, contents)
            if (result is Result.Success) {
                sendNotificationUseCase.invoke(
                    NotificationType.UPDATE_TODO,
                    date,
                )
            }
        }
    }

    fun changeDate(year: Int, month: Int, dayOfMonth: Int) {
        _date.value = dateFormatText.convertToDateText(year, month, dayOfMonth)
    }

    fun deleteTodo(key: String) {
        viewModelScope.launch {
            repository.deleteSchedule(date.value, key)
        }
    }

    fun getDate() = dateFormatText.convertToDateInt(date.value)
}