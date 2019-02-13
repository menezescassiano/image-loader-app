package com.cassianomenezes.imageloaderapplication.network

import com.cassianomenezes.imageloaderapplication.entries.DogResponse
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Streaming
import retrofit2.http.Url


interface RetrofitModel {

    @GET("breeds/image/random")
    fun fetchImageUrl(): Call<DogResponse>

    @Streaming
    @GET
    fun fetchImage(@Url url: String): Call<ResponseBody>
}