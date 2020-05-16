package com.example.music2.base

import android.content.SharedPreferences

object InitUserInfo {


    fun updateSharedUserInfo(editor : SharedPreferences.Editor,user : User){
        //记录登录状态
        editor.putBoolean("LOGIN_STATE",true)

        editor.putInt("id",user.id!!)
        editor.putString("name",user.name)
        editor.putString("collectList",user.collectList)
        editor.putString("nickName",user.nickName)
        editor.putString("sex",user.sex)
        editor.putString("age", user.age)
//        editor.putLong("registerData", user.registerData!!.time)
        user.isSinger?.let { editor.putInt("isSinger", it) }
        editor.putString("headerImageUrl",user.headerImageUrl)
        editor.putString("albumName",user.albumName)
//        user.uploadDate?.time?.let { it1 -> editor.putLong("uploadDate", it1) }
//        user.ispassed?.let { it1 -> editor.putInt("ispassed", it1) }
        editor.putString("recentlyListen",user.recentlyListen)

        editor.apply()

    }

}