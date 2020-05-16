package com.example.music2.service

import com.example.music2.entity.LRC
import okhttp3.ResponseBody
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Streaming

interface LRCService {

    //根据歌词名请求歌词列表
    @GET("{lrcName}")
    suspend fun getLrcDataList(@Path("lrcName") lrcName : String) : LRC

    //请求歌词流
    @Streaming
    @GET("{lrcNo}")
    suspend fun getLrcStream(@Path("lrcNo") lrcNo : String) : ResponseBody
}