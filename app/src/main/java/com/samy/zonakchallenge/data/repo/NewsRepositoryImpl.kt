package com.samy.zonakchallenge.data.repo

import com.samy.zonakchallenge.data.local.NewsDao
import com.samy.zonakchallenge.data.local.NewsLocalDataSource
import com.samy.zonakchallenge.data.remote.NewsRemoteDataSource
import com.samy.zonakchallenge.domain.model.NewsResponse
import com.samy.zonakchallenge.domain.repository.NewsRepository
import com.samy.zonakchallenge.utils.Utils
import javax.inject.Inject

class NewsRepositoryImpl @Inject constructor(
    private val remoteDataSource: NewsRemoteDataSource,
    private val localDataSource: NewsLocalDataSource,
) : NewsRepository {
    override suspend fun getNewsArticles(): NewsResponse {
        if (Utils.isInternetAvailable()) {
            val response = remoteDataSource.fetchNewsArticles()
            localDataSource.insertNewsArticles(response)
            return response
        } else {
            val response = localDataSource.fetchNewsArticles()
            if (response == null)
                Exception("Empty Data Cashing!!")
            return response
        }

    }
}
