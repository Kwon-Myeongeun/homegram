package com.lovesme.homegram.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject
import com.lovesme.homegram.data.repository.SyncRepository
import com.lovesme.homegram.util.Constants

@HiltViewModel
class HomeViewModel @Inject constructor(private val repository: SyncRepository) : ViewModel() {

    fun startSync() {
        viewModelScope.launch {
            Constants.userId?.let {
                repository.syncStart(it)
            }

        }
    }
}