package com.lovesme.homegram.data.model

sealed class UiState<out T> {
    object Loading : UiState<Nothing>()
    data class Success<out T>(val item: T) : UiState<T>()
    object Empty : UiState<Nothing>()
    object Error : UiState<Nothing>()
}
