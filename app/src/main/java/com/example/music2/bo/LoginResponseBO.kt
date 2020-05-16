package com.example.music2.bo

import com.example.music2.base.User

class LoginResponseBO(
    val message: String,
    val code: String,
    val user : User
)