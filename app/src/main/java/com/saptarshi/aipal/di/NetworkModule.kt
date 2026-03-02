package com.saptarshi.aipal.di

import com.saptarshi.aipal.data.remote.AuthInterceptor
import com.saptarshi.aipal.data.remote.api.AuthApi
import com.saptarshi.aipal.data.remote.api.ConversationApi
import com.saptarshi.aipal.data.remote.api.FactsApi
import com.saptarshi.aipal.data.remote.api.FavouritesApi
import com.saptarshi.aipal.data.remote.api.QuotesApi
import com.saptarshi.aipal.data.remote.api.TrendingApi
import com.saptarshi.aipal.utils.Constants
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Provides
    @Singleton
    fun provideOkHttpClient(
        authInterceptor: AuthInterceptor
    ): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(authInterceptor)
            .addInterceptor(HttpLoggingInterceptor().apply {
                level = HttpLoggingInterceptor.Level.BODY
            })
            .build()

    }

    @Provides
    @Singleton
    fun provideRetrofit(client: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .baseUrl(Constants.BASE_URL)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    @Provides
    @Singleton
    fun provideAuthApi(retrofit: Retrofit): AuthApi {
        return retrofit.create(AuthApi::class.java)
    }

    @Provides
    @Singleton
    fun provideFactsApi(retrofit: Retrofit): FactsApi {
        return retrofit.create(FactsApi::class.java)
    }

    @Provides
    @Singleton
    fun provideQuotesApi(retrofit: Retrofit): QuotesApi {
        return retrofit.create(QuotesApi::class.java)
    }

    @Provides
    @Singleton
    fun provideConversationApi(retrofit: Retrofit): ConversationApi {
        return retrofit.create(ConversationApi::class.java)
    }

    @Provides
    @Singleton
    fun provideFavouritesApi(retrofit: Retrofit): FavouritesApi {
        return retrofit.create(FavouritesApi::class.java)
    }

    @Provides
    @Singleton
    fun provideTrendingApi(retrofit: Retrofit): TrendingApi {
        return retrofit.create(TrendingApi::class.java)
    }

}