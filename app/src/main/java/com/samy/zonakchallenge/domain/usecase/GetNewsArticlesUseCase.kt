package com.samy.zonakchallenge.domain.usecase

import com.samy.zonakchallenge.domain.model.NewsResponse
import com.samy.zonakchallenge.domain.repository.NewsRepository
import javax.inject.Inject

class GetNewsArticlesUseCase @Inject constructor(
    private val newsRepository: NewsRepository
) {

    suspend operator fun invoke(): List<NewsResponse.Source> {
        return newsRepository.getNewsArticles().sources.filter { it.country == "us" }

    }
}