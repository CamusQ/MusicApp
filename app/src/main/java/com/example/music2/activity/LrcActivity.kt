package com.example.music2.activity

import android.media.MediaPlayer
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.music2.R
import com.example.music2.viewModel.LrcViewModel
import kotlinx.android.synthetic.main.activity_lrc.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class LrcActivity : AppCompatActivity() {

    companion object {
        const val LRC_NAME = "lrc_name"
        const val LRC_URL = "lrc_url"
    }

    private lateinit var mediaPlayer: MediaPlayer
    private var lrcActivityWorking: Boolean = false


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_lrc)

        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        lrcActivityWorking = true

        //传过来的歌词格式 ： 歌手 · 歌词名
        val info = intent.getStringExtra(LRC_NAME)
        Log.d("TAG", info)
        collapsing_toolbar.title = info
        mLrcView.setEmptyContent("正在加载歌词...")


        if(info.contains("空白")){
            mLrcView.setEmptyContent("纯音乐，敬请欣赏...")
            return
        }

//        val songName = info.substring(info.lastIndexOf("·") + 1)
        val viewModel = ViewModelProvider(this).get(LrcViewModel::class.java)


        CoroutineScope(Dispatchers.IO).launch {
            val lrcData = viewModel.getLrc(intent.getStringExtra(LRC_URL))

            mLrcView.setLrcData(lrcData)

            mediaPlayer = AudioActivity.mediaPlayer
            mLrcView.setOnPlayIndicatorLineListener { time, content -> mediaPlayer.seekTo(time.toInt()) }
            while (lrcActivityWorking) {
                mLrcView.updateTime(mediaPlayer.currentPosition.toLong())
                Log.d("TAG", Thread.currentThread().name + " : " + mediaPlayer.currentPosition)
                Thread.sleep(1000)
            }
            Log.d("TAG", "滚动歌词活动结束")
        }
    }

    /*返回按钮*/
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                finish()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onPause() {
        super.onPause()
    }

    override fun onDestroy() {
        super.onDestroy()
        lrcActivityWorking = false
    }
}
