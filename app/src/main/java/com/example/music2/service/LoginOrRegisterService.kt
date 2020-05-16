package com.example.music2.service

import com.example.music2.base.User
import com.example.music2.bo.LoginResponseBO
import retrofit2.http.Body
import retrofit2.http.POST

interface LoginOrRegisterService {

    @POST("login")
    suspend fun login(@Body user : User) : LoginResponseBO

}