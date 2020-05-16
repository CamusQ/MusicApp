package com.example.music2.viewModel

import androidx.lifecycle.ViewModel
import com.example.music2.base.BaseConst
import com.example.music2.base.User
import com.example.music2.bo.UpdateUserInfoResponseBO
import com.example.music2.service.PersonalityService
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create

class PersonalityViewModel : ViewModel() {

    private val retrofit = Retrofit.Builder()
        .baseUrl(BaseConst.USER_ACTIVITY_INFO)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    suspend fun updateUserInfo(user: User): UpdateUserInfoResponseBO {
        val personalityService = retrofit.create<PersonalityService>()
        val repsonseBO = personalityService.updateUserInfo(user)
        return repsonseBO
    }
}