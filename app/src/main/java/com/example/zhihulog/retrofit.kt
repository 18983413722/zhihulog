package com.example.zhihulog

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path

/*interface ZhihuApiService {
    @GET("news/before/{date}")
    suspend fun getNews(
        @Path("date") date: String
    ): ZhihuDailyResponse
}*/

interface ZhihuApiService {
    @GET("news/latest")
    suspend fun getLatestNews(): ZhihuDailyResponse

    @GET("news/before/{date}")
    suspend fun getNewsBefore(
        @Path("date") date: String
    ): ZhihuDailyResponse
}

object RetrofitClient{
    private const val BASE_URL = "https://news-at.zhihu.com/api/4/"
   val apiService : ZhihuApiService by lazy {
       Retrofit.Builder()
           .baseUrl(BASE_URL)
           .addConverterFactory(GsonConverterFactory.create())
           .build()
           .create(ZhihuApiService::class.java)
   }
}
