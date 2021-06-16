package com.example.iconfinder.data.api

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

//my personal key
//val API_KEY="Bearer PEYwGXCMDlwkf8nhWSo3oIa4NuVMlrBqOgFfy9KJ19VZvRnF6LMdyH1JDV7MUsZ5"

//open key
const val API_KEY="Bearer X0vjEUN6KRlxbp2DoUkyHeM0VOmxY91rA6BbU5j3Xu6wDodwS0McmilLPBWDUcJ1"
const val BASE_URL="https://api.iconfinder.com/v4/"
const val BASE_PREVIEW_ICONS_URL="https://cdn1.iconfinder.com/data/icons/"
const val BASE_DOWNLOAD_ICONS_URL="https://api.iconfinder.com/"

const val FIRST_COUNT=0
const val POST_PER_PAGE=20

object IconClient {
    fun getClient():IconInterface{

        val logging = HttpLoggingInterceptor()
        logging.setLevel(HttpLoggingInterceptor.Level.BODY)

        val okHttpClient = OkHttpClient().newBuilder()
                .connectTimeout(120, TimeUnit.SECONDS)
                .addInterceptor(logging)
                .build()


        val retrofit: Retrofit = Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(okHttpClient)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create()) //Here we are using the GsonConverterFactory to directly convert json data to object
                .build()



        return retrofit.create(IconInterface::class.java)
    }
}