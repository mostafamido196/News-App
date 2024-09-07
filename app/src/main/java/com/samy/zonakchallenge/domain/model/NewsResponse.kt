package com.samy.zonakchallenge.domain.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "news")
data class NewsResponse(
    @PrimaryKey val status: String,
    val sources: List<Source>,
) {
    data class Source(
        val id: String,
        val category: String,
        val country: String,
        val description: String,
        val language: String,
        val name: String,
        val url: String
    )
}
