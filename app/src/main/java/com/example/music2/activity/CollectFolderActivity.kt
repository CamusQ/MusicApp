package com.example.music2.activity

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.alibaba.fastjson.JSON
import com.example.music2.R
import com.example.music2.adapter.CollectItemAdapter
import com.example.music2.base.User
import com.example.music2.entity.CollectItem
import com.example.music2.viewModel.CollectViewModel
import kotlinx.android.synthetic.main.activity_audio.toolbar
import kotlinx.android.synthetic.main.activity_collect_folder.*
import kotlinx.coroutines.runBlocking

class CollectFolderActivity : AppCompatActivity() {

    companion object {
        val NICK_NAME = "nick_name"

    }

    var collects = ArrayList<CollectItem>()
    var ids = ArrayList<String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        Log.d("TAG", "onCreate")
        setContentView(R.layout.activity_collect_folder)


        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        toolbar.setTitle("收藏夹")


        /*这里应该有一个网络请求，到服务器取收藏夹数据*/
        val viewModel = ViewModelProvider(this).get(CollectViewModel::class.java)

        runBlocking {
            collects = viewModel.getCollectData(intent.getStringExtra(NICK_NAME))
            Log.d("TAG", "获取收藏列表完成：" + JSON.toJSONString(collects))
        }

        collect_recyclerView.layoutManager = GridLayoutManager(this, 1)
        collect_recyclerView.adapter = CollectItemAdapter(this, collects)

    }

    override fun onDestroy() {
        val prefs = getSharedPreferences("collect_folder", Context.MODE_PRIVATE)
        val allKeys = prefs.all.keys

        ids.clear()
        allKeys.forEach {
            if (prefs.getBoolean(it, false)) {
                ids.add(it)
            }
        }

        var user = User()
        user.collectList = ids.toString()

        val viewModel = ViewModelProvider(this).get(CollectViewModel::class.java)
        try {
            runBlocking {
                viewModel.updateCollectData(user)
                Log.d("TAG", "更新收藏夹完成")
            }
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            super.onDestroy()
        }


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
