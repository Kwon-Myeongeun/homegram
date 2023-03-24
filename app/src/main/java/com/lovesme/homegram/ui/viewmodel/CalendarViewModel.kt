package com.lovesme.homegram.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lovesme.homegram.data.model.Result
import com.lovesme.homegram.data.model.Todo
import com.lovesme.homegram.data.repository.TodoRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CalendarViewModel @Inject constructor(private val repository: TodoRepository) : ViewModel() {

    private val _todo = MutableLiveData<List<Todo>>()
    val todo: LiveData<List<Todo>> = _todo

    fun loadTodo(date: String) {
        viewModelScope.launch {
            val result = repository.getSchedule(date)
            if (result is Result.Success) {
                _todo.value = result.data as List<Todo>
            }
        }
    }
}