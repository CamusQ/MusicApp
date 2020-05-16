package com.example.music2.service

import retrofit2.http.GET

interface ConnectServer {

    @GET
    suspend fun checkConnecttion() : String
}