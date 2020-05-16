package com.example.music2.service

import com.example.music2.bo.ResponseBO
import com.example.music2.entity.AlbumItem
import okhttp3.ResponseBody
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query
import retrofit2.http.Streaming

interface AlbumService {

    @GET("getAudioList")
    suspend fun fetchAlbumList(@Query("offset") offset : Int,@Query("limit")limit : Int ) : ArrayList<AlbumItem>

    @GET("selectOne")
    suspend fun getAlbum(@Query("id") albumID : Int) : AlbumItem

    @GET("addAlbum")
    suspend fun addAlbum(@Query("id") id : Int,@Query("albumDesc") albumDesc : String ) : ResponseBO

    @GET("checkAlbum")
    suspend fun checkAlbum(@Query("albumName") albumName : String) : ResponseBO

    //请求歌曲流
    @Streaming
    @GET("{albumName}")
    suspend fun getAlbumStream(@Path("albumName") albumName : String) : ResponseBody
}