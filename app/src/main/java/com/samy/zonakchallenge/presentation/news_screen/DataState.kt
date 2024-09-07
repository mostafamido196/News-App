package com.samy.zonakchallenge.presentation.news_screen

sealed class DataState {
    object Idle : DataState()
    object Loading : DataState()
    data class Result<T>(val data: T) : DataState()
    data class Error(val msg: String) : DataState()
}