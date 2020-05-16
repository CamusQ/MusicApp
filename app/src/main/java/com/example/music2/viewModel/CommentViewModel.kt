package com.example.music2.viewModel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.alibaba.fastjson.JSON
import com.example.music2.base.BaseConst
import com.example.music2.bo.ResponseBO
import com.example.music2.entity.Comment
import com.example.music2.service.CommentService
import com.google.gson.GsonBuilder
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create

class CommentViewModel : ViewModel() {

    private var _commentList = MutableLiveData<ArrayList<Comment>>()
    val commentList
        get() = _commentList


    val gson = GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create()

    private val retrofit = Retrofit.Builder()
        .baseUrl(BaseConst.COMMENT_LIST)
        .addConverterFactory(
            GsonConverterFactory.create(gson)
        )
        .build()
    val commentService = retrofit.create<CommentService>()

    suspend fun getCommentList(albumID: Int) {
        val commentList = commentService.getCommentData(albumID)

        Log.d("TAG", JSON.toJSONString(commentList))
        _commentList.value = commentList
    }


    suspend fun addComment(comment: Comment): ResponseBO {
        val bo = commentService.addComment(comment)
        _commentList.value?.add(comment)
        return bo
    }


}