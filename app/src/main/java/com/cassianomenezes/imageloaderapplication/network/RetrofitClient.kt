package com.cassianomenezes.imageloaderapplication.network

import com.cassianomenezes.imageloaderapplication.BuildConfig
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.IOException

class RetrofitClient : Interceptor {

    companion object {
        private var instance: RetrofitClient? = null

        fun getInstance(): RetrofitClient {
            if (instance == null) {
                instance = RetrofitClient()
            }


            return instance as RetrofitClient
        }
    }

    val model: RetrofitModel

    init {
        val logging = HttpLoggingInterceptor()
        logging.level = HttpLoggingInterceptor.Level.BODY
        val httpClient = OkHttpClient.Builder()
        httpClient.addInterceptor(logging)
        httpClient.addInterceptor(this)
        val retrofit = Retrofit.Builder()
            .baseUrl(BuildConfig.SERVER_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(httpClient.build())
            .build()
        model = retrofit.create(RetrofitModel::class.java)
    }

    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {
        val original = chain.request()

        val request = original.newBuilder()
            .method(original.method(), original.body())
            .build()

        return chain.proceed(request)
    }
}
