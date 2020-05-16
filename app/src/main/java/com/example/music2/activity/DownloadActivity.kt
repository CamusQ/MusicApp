package com.example.music2.activity

import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.alibaba.fastjson.JSON
import com.example.music2.R
import com.example.music2.adapter.DownloadAdapter
import com.example.music2.utils.showToast
import com.example.music2.viewModel.DownloadViewModel
import kotlinx.android.synthetic.main.activity_download.*
import kotlinx.android.synthetic.main.download_cell.*

class DownloadActivity : AppCompatActivity() {

    val viewModel by lazy { ViewModelProvider(this).get(DownloadViewModel::class.java) }

    val downloadAdapter by lazy { DownloadAdapter() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_download)

        setSupportActionBar(download_toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        /*效果： 深色状态栏*/
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR

        viewModel.getFileAllName("/data/data/com.example.music2/download")



        download_recyclerView.apply {
            layoutManager = GridLayoutManager(this@DownloadActivity, 1)
            adapter = downloadAdapter
        }



        viewModel.localAlbum.observe(this, Observer {
            Log.d("TAG", "本地文件 ： " + JSON.toJSONString(it))
            downloadAdapter.submitList(it)
            downloadAdapter.notifyDataSetChanged()
        })
    }

    override fun onDestroy() {
        super.onDestroy()
        downloadAdapter.mediaPlayer.release()
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
