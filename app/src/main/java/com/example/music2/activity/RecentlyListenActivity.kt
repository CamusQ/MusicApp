package com.example.music2.activity

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import com.alibaba.fastjson.JSON
import com.example.music2.R
import com.example.music2.adapter.RecentlyItemAdapter
import com.example.music2.base.User
import com.example.music2.base.UserInfo
import com.example.music2.entity.AlbumItem
import com.example.music2.viewModel.RecentlyViewModel
import kotlinx.android.synthetic.main.activity_audio.*
import kotlinx.android.synthetic.main.activity_recently_listen.*
import kotlinx.android.synthetic.main.activity_recently_listen.toolbar
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import kotlin.math.log

class RecentlyListenActivity : AppCompatActivity() {

    companion object {
        val USER_ID = "user_id"
    }

    val viewModel by lazy { ViewModelProvider(this).get(RecentlyViewModel::class.java) }
    val shared by lazy { getSharedPreferences("userInfo", Context.MODE_PRIVATE) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_recently_listen)

        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val recentlyAdapter = RecentlyItemAdapter()
        recent_recyclerView.apply {
            layoutManager = GridLayoutManager(this@RecentlyListenActivity, 1)
            adapter = recentlyAdapter
        }

        /*如果登录 就去数据库取数据*/
        if (shared.getBoolean("LOGIN_STATE", false)) {
            lifecycleScope.launch {
                viewModel.getRecentlyListenList(intent.getIntExtra(USER_ID, -1))
            }
        } else {
            /*没登陆就从本地的文件 取出最近收听歌曲id 去数据库取歌曲信息*/
            lifecycleScope.launch {
                viewModel.getReceFromLocal(shared)
            }
        }


        viewModel.recentlyListenList.observe(this, Observer {
            Log.d("TAG", "最近常听列表" + JSON.toJSONString(it))
            recentlyAdapter.submitList(it)
            recentlyAdapter.notifyDataSetChanged()
        })

    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        when (item.itemId) {
            android.R.id.home -> {
                finish()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }
}
