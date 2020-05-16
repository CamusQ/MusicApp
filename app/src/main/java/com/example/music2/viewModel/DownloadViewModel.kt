package com.example.music2.viewModel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.music2.entity.AudioItem
import java.io.File

class DownloadViewModel : ViewModel() {

    private val _localAlbum = MutableLiveData<ArrayList<AudioItem>>()
    val localAlbum
        get() = _localAlbum


    fun getFileAllName(path: String) {
        val file = File(path)
        val fileList = file.listFiles()
        val pathList = ArrayList<AudioItem>()
        for (file in fileList) {
            Log.d("FILEPATH", file.name)
            val audioItem = AudioItem(file.path)
            pathList.add(audioItem)
        }

        _localAlbum.value = pathList

    }

}