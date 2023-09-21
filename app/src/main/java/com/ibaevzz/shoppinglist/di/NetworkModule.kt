package com.ibaevzz.shoppinglist.di

import com.ibaevzz.shoppinglist.constants.Constants
import com.ibaevzz.shoppinglist.network.ShoppingAPIClient
import dagger.Module
import dagger.Provides
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
class NetworkModule{

    @Singleton
    @Provides
    fun getShoppingAPIClient(retrofit: Retrofit): ShoppingAPIClient {
        return retrofit.create(ShoppingAPIClient::class.java)
    }

    @Singleton
    @Provides
    fun getRetrofit(gsonConverterFactory: GsonConverterFactory): Retrofit {
        return Retrofit.Builder()
            .baseUrl(Constants.BASE_URL)
            .addConverterFactory(gsonConverterFactory)
            .build()
    }

    @Singleton
    @Provides
    fun getGsonConverterFactory(): GsonConverterFactory {
        return GsonConverterFactory.create()
    }
}