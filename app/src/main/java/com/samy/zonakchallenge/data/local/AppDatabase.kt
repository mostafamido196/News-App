package com.samy.zonakchallenge.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.samy.zonakchallenge.domain.model.NewsResponse

@Database(entities = [NewsResponse::class], version = 1)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun newsDao(): NewsDao
}