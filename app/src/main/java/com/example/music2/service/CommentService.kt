package com.example.music2.service

import com.example.music2.bo.ResponseBO
import com.example.music2.entity.Comment
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface CommentService {

    @GET("getAlbumComments")
    suspend fun getCommentData(@Query("albumID") albumID: Int): ArrayList<Comment>

    @POST("addComment")
    suspend fun addComment(@Body comment: Comment): ResponseBO


    /**
     * replyCommId
     * commId 评论id
     *
     */
    @GET("getComm")
    suspend fun getSlaveComms(
        @Query("replyCommId") replyCommId: Int,
        @Query("commId") commId: Int
    ): ArrayList<Comment>
}