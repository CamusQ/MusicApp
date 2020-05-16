package com.example.music2.base

import android.util.Log
import com.alibaba.fastjson.JSON
import com.example.music2.bo.ResponseBO
import com.example.music2.service.ResourceService
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create
import java.io.File
import java.net.URLEncoder

class HandleData {

    suspend fun uploadMusic(musicFilePath: String) : ResponseBO{

        val retrofit = Retrofit.Builder()
            .baseUrl(BaseConst.UPLOAD_FILE)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val uploadMusic = retrofit.create<ResourceService>()
        val file = File(musicFilePath);

        // 创建 RequestBody，用于封装构建MultipartBody.Part。设置：multipart/form-data）
        val requestBody = RequestBody.create(MediaType.parse("multipart/form-data"), file)

        val encode = URLEncoder.encode(file.name, "UTF-8")
        // MultipartBody.Part  和后端约定好Key，这里的partName暂时用"file_key_*"
        val partMusicFile = MultipartBody.Part.createFormData("music_key", encode, requestBody)

        val responseBO = uploadMusic.uploadMusic(partMusicFile)

        return responseBO

    }

}