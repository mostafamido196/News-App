package com.samy.zonakchallenge.domain.repository

import com.samy.zonakchallenge.domain.model.NewsResponse

interface NewsRepository {
    suspend fun getNewsArticles(): NewsResponse
}