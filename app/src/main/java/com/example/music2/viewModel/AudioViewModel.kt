package com.example.music2.viewModel

import android.util.Log
import androidx.lifecycle.ViewModel
import com.alibaba.fastjson.JSON
import com.example.music2.base.BaseConst
import com.example.music2.bo.ResponseBO
import com.example.music2.service.AlbumService
import com.example.music2.utils.showToast
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create
import java.io.File


class AudioViewModel : ViewModel() {

    private val retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(BaseConst.ALBUM_LIST_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    private val albumService = retrofit.create<AlbumService>()

    suspend fun uploadAlbum(id: Int, albumDesc: String): ResponseBO {
        val bo = albumService.addAlbum(id, albumDesc)
        Log.d("TAG", JSON.toJSONString(bo))
        return bo
    }

    suspend fun checkAlbum(albumName: String): ResponseBO {
        val bo = albumService.checkAlbum(albumName)
        Log.d("TAG", JSON.toJSONString(bo))
        return bo
    }

    suspend fun downAlbum(albumName: String): ResponseBO {
        val name = albumName.substring(albumName.lastIndexOf("/") + 1)
        Log.d("TAG", "albumName : $name")


        val file = File("/data/data/com.example.music2/download/${name}")
        var bo : ResponseBO

        val ret = Retrofit.Builder()
            .baseUrl(BaseConst.DOWN_LOAD_FILE)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val albSer = ret.create<AlbumService>()

        val albumStream = albSer.getAlbumStream(name)
        val bytes = albumStream.byteStream().readBytes()

        try {
            if (!file.parentFile.exists()) {
                file.parentFile.mkdirs()
            }
            if (!file.exists()) {
                file.createNewFile()
                file.writeBytes(bytes)
            }
            Log.d("TAG", file.name + "下载成功")
            bo = ResponseBO("下载成功",BaseConst.CODE_SUCCESS)
        } catch (e: Exception) {
            e.printStackTrace()
            Log.d("TAG", file.name + "下载失败")
            bo = ResponseBO("下载失败",BaseConst.CODE_FAILED)
        }

        return bo
    }




}