package com.samy.zonakchallenge.di

import android.content.Context
import androidx.room.Room
import com.samy.zonakchallenge.data.local.AppDatabase
import com.samy.zonakchallenge.data.local.NewsDao
import com.samy.zonakchallenge.data.local.NewsLocalDataSource
import com.samy.zonakchallenge.data.remote.NewsApiService
import com.samy.zonakchallenge.data.remote.NewsRemoteDataSource
import com.samy.zonakchallenge.data.repo.NewsRepositoryImpl
import com.samy.zonakchallenge.domain.repository.NewsRepository
import com.samy.zonakchallenge.domain.usecase.GetNewsArticlesUseCase
import com.samy.zonakchallenge.utils.Constants
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    private const val TIMEOUT = 30L

    @Provides
    @Singleton
    fun providesHttpLoggingInterceptor(): HttpLoggingInterceptor {

        val httpLoggingInterceptor = HttpLoggingInterceptor()
        httpLoggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY)
        return httpLoggingInterceptor
    }

    @Provides
    @Singleton
    fun providesOkHttp(httpLoggingInterceptor: HttpLoggingInterceptor): OkHttpClient =
        OkHttpClient.Builder().apply {
            /*   if (BuildConfig.DEBUG) {
                   addInterceptor(HttpLoggingInterceptor().apply {
                       level = HttpLoggingInterceptor.Level.BODY
                   })
               }*/
            addInterceptor(httpLoggingInterceptor)
            connectTimeout(TIMEOUT, TimeUnit.SECONDS)
            writeTimeout(TIMEOUT, TimeUnit.SECONDS)
            readTimeout(TIMEOUT, TimeUnit.SECONDS)
        }.build()

    @Provides
    @Singleton
    fun providesRetrofit(okHttpClient: OkHttpClient): Retrofit = Retrofit.Builder().apply {
        baseUrl(Constants.BASE_URL)
        client(okHttpClient)
        addConverterFactory(GsonConverterFactory.create())
    }.build()

    @Provides
    @Singleton
    fun provideNewsApiService(retrofit: Retrofit): NewsApiService {
        return retrofit.create(NewsApiService::class.java)
    }
    @Provides
    @Singleton
    fun provideNewsRepository(remoteDataSource: NewsRemoteDataSource,localDataSource: NewsLocalDataSource): NewsRepository {
        return NewsRepositoryImpl(remoteDataSource,localDataSource)
    }



    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): AppDatabase {
        return Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            "app_database"
        ).build()
    }

    @Provides
    fun provideNewsDao(appDatabase: AppDatabase): NewsDao {
        return appDatabase.newsDao()
    }

}