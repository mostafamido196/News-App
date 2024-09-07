package com.samy.zonakchallenge.data.remote

import com.samy.zonakchallenge.domain.model.NewsResponse
import retrofit2.http.GET

interface NewsApiService {
    @GET("sources.json")
    suspend fun getNewsSources(): NewsResponse
}