package com.samy.zonakchallenge.data.local

import com.samy.zonakchallenge.domain.model.NewsResponse
import javax.inject.Inject


class NewsLocalDataSource @Inject
    constructor(
        private val  localDataSource: NewsDao
    ) {


    suspend fun fetchNewsArticles(): NewsResponse {
        return localDataSource.getNewsResponse()
    }
    suspend fun insertNewsArticles(newsResponse: NewsResponse) {
        return localDataSource.insertNews(newsResponse)
    }
}