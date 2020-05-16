package com.example.music2.viewModel

import android.content.SharedPreferences
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.music2.base.BaseConst
import com.example.music2.base.User
import com.example.music2.entity.AlbumItem
import com.example.music2.entity.AlbumListenCounts
import com.example.music2.service.AlbumService
import com.example.music2.service.HolderUserDataService
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create

class RecentlyViewModel : ViewModel() {

    private var _recentlyListenList = MutableLiveData<ArrayList<AlbumListenCounts>>()
    val recentlyListenList
        get() = _recentlyListenList

    val retrofit = Retrofit.Builder()
        .baseUrl(BaseConst.USER_ACTIVITY_INFO)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    /*获取最近常听列表*/
    suspend fun getRecentlyListenList(id: Int) {

        val holder = retrofit.create<HolderUserDataService>()
        val result = holder.fetchRecentlyListenList(id)
        _recentlyListenList.value = result

    }

    /*更新最近常听*/
    suspend fun updateRecentlyListenList(user: User) {
        val holder = retrofit.create<HolderUserDataService>()
        holder.updateUserRecnetltListen(user)
    }

    suspend fun getReceFromLocal(shared: SharedPreferences) {

        val retrofit = Retrofit.Builder()
            .baseUrl(BaseConst.ALBUM_LIST_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val holder = retrofit.create<AlbumService>()
        val array = ArrayList<AlbumListenCounts>()
        var recent = shared.getString("recentlyListen", "最近收听数据为空")
//        recent = recent?.substring(1, recent.indexOf("}"))
        recent = recent?.replace(" ","")
        val list = recent?.split(",")
        if (list != null) {
            for (cell in list) {
                val list1 = cell.split("=")
                val id = list1[0]
                val counts = list1[1]

                val album = holder.getAlbum(id.toInt())
                val albumCountsCell = AlbumListenCounts(counts.toInt(), album)
                array.add(albumCountsCell)
            }
        }

        _recentlyListenList.value = array

    }


}