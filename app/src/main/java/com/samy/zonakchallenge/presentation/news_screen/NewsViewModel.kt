package com.samy.zonakchallenge.presentation.news_screen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.samy.zonakchallenge.data.remote.NewsRemoteDataSource
import com.samy.zonakchallenge.data.repo.NewsRepositoryImpl
import com.samy.zonakchallenge.domain.model.NewsResponse
import com.samy.zonakchallenge.domain.usecase.GetNewsArticlesUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NewsViewModel @Inject constructor(
    private val getNewsArticlesUseCase: GetNewsArticlesUseCase
    ) : ViewModel() {
    private val _intentFlow = MutableSharedFlow<MainIntents>()


    private val _newsViewState = MutableStateFlow<DataState>(DataState.Idle)
    val newsState get() = _newsViewState

    private var isDataLoaded = false

    init {
        // must
        processIntent()

    }

    fun sendIntent(intent: MainIntents) {
        viewModelScope.launch {
            _intentFlow.emit(intent)
        }
    }

    private fun processIntent() {
        viewModelScope.launch(Dispatchers.IO) {
            _intentFlow.collect { intent ->
                when (intent) {
                    is MainIntents.GetNews ->{
                        if (!isDataLoaded) {
                            getNews()
                        }
                    }
                }
            }
        }
    }

    fun getNews() {
        if (isDataLoaded) {
            return // Skip API call if data is already loaded
        }
        println("mos getnews viwemodel")
        _newsViewState.value = DataState.Loading
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val response = getNewsArticlesUseCase()
                _newsViewState.value = DataState.Result(response)
                isDataLoaded = true
            } catch (e: Exception) {
                _newsViewState.value = DataState.Error(e.message ?: "Unknown error occurred")
            }
        }
    }
    fun getDifferenceCategories(response: List<NewsResponse.Source>): List<String> {
        return response.map { it.category }.distinct()
    }


    fun getCategorySelectedList(news: List<NewsResponse.Source>, selectedCategory: String): List<NewsResponse.Source> {
        return news.filter { it.category == selectedCategory }
    }

}