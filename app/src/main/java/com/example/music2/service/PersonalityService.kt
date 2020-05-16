package com.example.music2.service

import com.example.music2.base.User
import com.example.music2.bo.UpdateUserInfoResponseBO
import retrofit2.http.Body
import retrofit2.http.POST

interface PersonalityService {


    @POST("updateUserInfo")
    suspend fun updateUserInfo(@Body user : User) : UpdateUserInfoResponseBO

}