package com.example.music2.activity

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.music2.R
import com.example.music2.base.BaseConst
import com.example.music2.base.InitUserInfo
import com.example.music2.base.UserInfo
import com.example.music2.service.ConnectServer
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.runBlocking
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create

class MainActivity : AppCompatActivity() {

    val shared by lazy { getSharedPreferences("userInfo", Context.MODE_PRIVATE) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        /*设置用户状态*/
        val editor = shared.edit()
        editor.putBoolean("LOGIN_STATE",false)
        editor.apply()

        /*初始化用户最近收听列表*/
        setUserRecentFromLocal()
//        InitUserInfo.initUserRecent()

        textView.setOnClickListener {
            val intent = Intent(this,AudioListActivity::class.java)
            startActivity(intent)
        }
    }

    private fun setUserRecentFromLocal(){
        val recently = shared.getString("recentlyListen",BaseConst.USER_HISTORY_EMPTY)
        if (recently != null) {
            UserInfo.recentlyListen = recently
        }

        /*将本地字符串记录 加入LRU*/
        UserInfo.copyUserRecentInfo()
    }
}
