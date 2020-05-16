package com.example.music2.viewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.music2.base.BaseConst
import com.example.music2.entity.Comment
import com.example.music2.service.CommentService
import com.google.gson.GsonBuilder
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create

class SlaveCommentViewModel : ViewModel() {
    private var _slaveComms = MutableLiveData<ArrayList<Comment>>()
    val slaveComms
        get() = _slaveComms

    val gson = GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create()

    private val retrofit = Retrofit.Builder()
        .baseUrl(BaseConst.COMMENT_LIST)
        .addConverterFactory(GsonConverterFactory.create(gson))
        .build()
    val commentService = retrofit.create<CommentService>()

    suspend fun getSlaveComms(replyCommId: Int, commId: Int) {
        val value = commentService.getSlaveComms(replyCommId, commId)
        if (value.size == 0){
            return
        }
        _slaveComms.value = value
    }
}