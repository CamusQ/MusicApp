package com.example.music2.service

import com.example.music2.base.User
import com.example.music2.base.UserInfo
import com.example.music2.entity.AlbumItem
import com.example.music2.entity.AlbumListenCounts
import com.example.music2.entity.CollectItem
import okhttp3.ResponseBody
import retrofit2.http.*

/* 必须优化 */
interface HolderUserDataService {
    /* 后期优化 有必要把 更新用户活动信息 整合为一个接口 */

    @GET("getUserCollect")
    suspend fun fetchCollectList(@Query("nickName") nickName : String) : ArrayList<CollectItem>

    @POST("updateUserCollect")
    suspend fun updateUserCollect(@Body userInfo : User) : ResponseBody

    @GET("getUserRecentlyListen")
    suspend fun fetchRecentlyListenList(@Query("id") id: Int) : ArrayList<AlbumListenCounts>

    @POST("updateUserRecentlyListen")
    suspend fun updateUserRecnetltListen(@Body user : User) : ResponseBody



}