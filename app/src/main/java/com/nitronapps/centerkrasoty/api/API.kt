package com.nitronapps.centerkrasoty.api

import com.nitronapps.centerkrasoty.model.SERVER_ADDRESS
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

class API {
    companion object {
        fun getRetrofitAPI() : IAPI {

            //TODO: Remove in release version
            val interceptor = HttpLoggingInterceptor()
            interceptor.setLevel(HttpLoggingInterceptor.Level.BODY)

            val client = OkHttpClient.Builder()
                .connectTimeout(30, TimeUnit.SECONDS)
                .addInterceptor(interceptor)
                .addNetworkInterceptor(interceptor)
                .build()

            val retrofit = Retrofit.Builder()
                .baseUrl(SERVER_ADDRESS)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.createAsync())
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build()

            return retrofit.create(IAPI::class.java)
        }
    }
}