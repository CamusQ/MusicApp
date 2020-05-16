package com.example.music2.viewModel

import android.util.Log
import androidx.lifecycle.ViewModel
import com.example.music2.base.BaseConst
import com.example.music2.entity.AlbumItem
import com.example.music2.service.AlbumService
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create

class AudioListViewModel : ViewModel() {

    val albumList
        get() = _albumList

    private var _albumList = ArrayList<AlbumItem>()

    suspend fun fetchAudioList(offset: Int, limit: Int) {
        try {
            val retrofit = Retrofit.Builder()
                .baseUrl(BaseConst.ALBUM_LIST_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()

            val albumService = retrofit.create<AlbumService>()
            _albumList = albumService.fetchAlbumList(offset, limit)
            Log.d("TAG", _albumList.toString())
            Log.d("TAG", "协程:" + System.currentTimeMillis().toString())

        } catch (e: Exception) {
            e.printStackTrace()
            throw Exception(e)
        }
    }


}