package com.samy.zonakchallenge.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.samy.zonakchallenge.domain.model.NewsResponse

@Dao
interface NewsDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertNews(newsResponse: NewsResponse)

    @Query("SELECT * FROM news")
    suspend fun getNewsResponse(): NewsResponse // Assuming you want to get a list of news responses
}
