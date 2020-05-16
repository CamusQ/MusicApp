package com.example.music2.viewModel

import android.util.Log
import androidx.lifecycle.ViewModel
import com.example.music2.service.LRCService
import com.lauzy.freedom.library.Lrc
import com.lauzy.freedom.library.LrcHelper
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create
import java.io.File
import java.io.FileOutputStream
import java.io.OutputStreamWriter

class LrcViewModel : ViewModel() {

    fun initLrc() {

    }


//    object SeriviceCreator {
//        /*按歌曲名称查歌词*/
//        var BSES_URL = "http://gecimi.com/api/lyric/"
//
//        val retrofit = Retrofit.Builder()
//            .baseUrl(BSES_URL)
//            .addConverterFactory(GsonConverterFactory.create())
//            .build()
//
//        inline fun <reified T> create(): T = retrofit.create(T::class.java)
//    }


    suspend fun getLrc(lrcURL: String): MutableList<Lrc> {

        var lrcs: MutableList<Lrc> = withContext(Dispatchers.IO) {
//            val LrcService = SeriviceCreator.create<LRCService>()
//            val data = LrcService.getLrcDataList(lrcName)
//            Log.d("Tag", data.toString())
//
//            /*这里暂时指定歌词文件,日后确定好之后，要把判断文件是否存在搬到网络请求之前*/
//            var filePath = fetchLrcStreamData(data.result[1].lrc)
//
//            var lrcs = LrcHelper.parseLrcFromFile(File(filePath))
//            return@withContext lrcs
            var filePath = fetchLrcStreamData(lrcURL)

            var lrcs = LrcHelper.parseLrcFromFile(File(filePath))
            return@withContext lrcs

        }

        return lrcs
    }

    private suspend fun fetchLrcStreamData(url: String): String {

        val lrcNo = url.substring(url.lastIndexOf("/") + 1, url.lastIndex + 1)
        val file = File("/data/data/com.example.music2/LRC/${lrcNo}")
        if (file.exists()) {
            return file.path
        }

        var path: String = withContext(Dispatchers.IO) {
            val baseUrl = url.substring(0, url.lastIndexOf("/") + 1)
            val retrofit = Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .build()

            try {
                val lrcStream = retrofit.create<LRCService>()
                val stream = lrcStream.getLrcStream(lrcNo)

                val byteStream = stream.byteStream().readBytes()
                if (!file.parentFile.exists()) {
                    file.parentFile.mkdirs()
                }
                if (!file.exists()) {
                    file.createNewFile()
                    file.writeBytes(byteStream)
                }
                Log.d("TAG", "写入完毕")
            } catch (e: Exception) {
                e.printStackTrace()
                Log.d("TAG", "歌词写入失败")
            }
            return@withContext file.path
        }
        return path
    }


}