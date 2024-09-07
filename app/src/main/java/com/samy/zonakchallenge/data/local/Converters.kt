package com.samy.zonakchallenge.data.local

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.samy.zonakchallenge.domain.model.NewsResponse

object Converters {

    @TypeConverter
    fun fromSourceList(value: List<NewsResponse.Source>?): String? {
        return Gson().toJson(value)
    }

    @TypeConverter
    fun toSourceList(value: String?): List<NewsResponse.Source>? {
        val listType = object : TypeToken<List<NewsResponse.Source>>() {}.type
        return Gson().fromJson(value, listType)
    }
}
