package com.example.music2.service

import com.example.music2.bo.ResponseBO
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part

interface ResourceService {

    /*上传音频*/
    @Multipart
    @POST("upload")
    suspend fun uploadMusic(@Part file : MultipartBody.Part ) : ResponseBO



}