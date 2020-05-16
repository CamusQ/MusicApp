package com.example.music2.viewModel

import androidx.lifecycle.ViewModel
import com.example.music2.base.BaseConst
import com.example.music2.base.User
import com.example.music2.bo.LoginResponseBO
import com.example.music2.service.LoginOrRegisterService
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create

class LoginViewModel : ViewModel() {


    private val retrofit = Retrofit.Builder()
        .baseUrl(BaseConst.USER_ACTIVITY_INFO)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    suspend fun login(userName: String, password: String) : LoginResponseBO {
        val loginOrRegisterService = retrofit.create<LoginOrRegisterService>()
        val user = User()
        user.name = userName
        user.password = password
        return loginOrRegisterService.login(user)
    }

}