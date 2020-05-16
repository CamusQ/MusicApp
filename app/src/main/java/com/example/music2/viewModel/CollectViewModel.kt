package com.example.music2.viewModel

import androidx.lifecycle.ViewModel
import com.example.music2.base.BaseConst
import com.example.music2.base.User
import com.example.music2.entity.CollectItem
import com.example.music2.service.HolderUserDataService
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create

class CollectViewModel : ViewModel() {

    //获取收藏夹数据
    suspend fun getCollectData(nickName: String): ArrayList<CollectItem> {
        //网络请求

        val retrofit = Retrofit.Builder()
            .baseUrl(BaseConst.USER_ACTIVITY_INFO)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        val fetchUserDataService = retrofit.create<HolderUserDataService>()
        val collectList = fetchUserDataService.fetchCollectList(nickName)
        return collectList
    }

    //更新收藏夹数据
    suspend fun updateCollectData(userInfo : User){
        val retrofit = Retrofit.Builder()
            .baseUrl(BaseConst.USER_ACTIVITY_INFO)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        val holderUserDataService = retrofit.create<HolderUserDataService>()
        holderUserDataService.updateUserCollect(userInfo)

    }

}