package com.samy.zonakchallenge.data.remote

import com.samy.zonakchallenge.domain.model.NewsResponse
import javax.inject.Inject

class NewsRemoteDataSource @Inject
    constructor(
        private val newsApiService: NewsApiService
    ) {


    suspend fun fetchNewsArticles(): NewsResponse {
        return newsApiService.getNewsSources()
    }
}