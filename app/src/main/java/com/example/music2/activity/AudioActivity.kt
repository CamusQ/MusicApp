package com.example.music2.activity

import android.content.Context
import android.content.Intent
import android.media.MediaPlayer
import android.os.Bundle
import android.os.Looper
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import com.alibaba.fastjson.JSON
import com.bumptech.glide.Glide
import com.example.music2.R
import com.example.music2.adapter.CommentAdapter
import com.example.music2.adapter.SlaveCommentAdapter
import com.example.music2.base.*
import com.example.music2.entity.Comment
import com.example.music2.utils.showToast
import com.example.music2.viewModel.AudioViewModel
import com.example.music2.viewModel.CommentViewModel
import com.example.music2.viewModel.RecentlyViewModel
import com.example.music2.viewModel.SlaveCommentViewModel
import kotlinx.android.synthetic.main.activity_audio.*
import kotlinx.coroutines.*
import java.util.*

class AudioActivity : AppCompatActivity() {

    companion object {
        const val ALBUM_ID = "album_id"
        const val AUDIO_NAME = "audio_name"
        const val AUDIO_IMAGE = "audio_image"
        const val AUDIO_URL = "audio_url"
        const val AUDIO_LRC_URL = "audio_lrc_url"
        lateinit var mediaPlayer: MediaPlayer

    }

    val recViewModel by lazy { ViewModelProvider(this).get(RecentlyViewModel::class.java) }
    val audioViewModel by lazy { ViewModelProvider(this).get(AudioViewModel::class.java) }
    val shared by lazy { getSharedPreferences("userInfo", Context.MODE_PRIVATE) }
    val commentAdapter by lazy { CommentAdapter(this) }
    val commentViewModel by lazy { ViewModelProvider(this).get(CommentViewModel::class.java) }
    val slaveCommentViewModel by lazy { ViewModelProvider(this).get(SlaveCommentViewModel::class.java) }

    val slaveCommentAdapter by lazy { SlaveCommentAdapter(this) }

    private lateinit var _mediaPlayer: MediaPlayer
    var buttonState = false
    lateinit var audioName: String
    lateinit var lrcUrl: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_audio)


        audioName = intent.getStringExtra(AUDIO_NAME) ?: ""
        lrcUrl = intent.getStringExtra(AUDIO_LRC_URL) ?: ""
        val audioImage = intent.getStringExtra(AUDIO_IMAGE)

        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        collapsing_toolbar.title = audioName
        Glide.with(this).load(audioImage).into(audio_image_ivew)
        avi.show()

        if (shared.getBoolean("LOGIN_STATE", false)) {
            edit_comment.isEnabled = true
            push_comment.isEnabled = true

        } else {
            edit_comment.hint = "请登录再评论!"
            push_comment.isEnabled = false
        }

        comment_recycleView.apply {
            layoutManager = GridLayoutManager(this@AudioActivity, 1)
            adapter = commentAdapter
        }



        GlobalScope.launch {
            prepareMedia()
        }

        lifecycleScope.launch {
            coroutineScope {
                commentViewModel.getCommentList(intent.getIntExtra(ALBUM_ID, -1))
            }

        }

        slaveCommentViewModel.slaveComms.observe(this, androidx.lifecycle.Observer {
            Log.d("TAG", "子评论列表发生变化")
            Log.d("TAG",JSON.toJSONString(it))
            slaveCommentAdapter.submitList(it)
            slaveCommentAdapter.notifyDataSetChanged()

            val replyName: TextView = findViewById(R.id.reply_name)
            val replyContent: TextView = findViewById(R.id.reply_content)

            if(it.size > 0){
                replyName.text = it[0].commName
                replyContent.text = it[0].commContent
                replyName.visibility = View.VISIBLE
                replyContent.visibility = View.VISIBLE
            }

        })


        commentViewModel.commentList.observe(this, androidx.lifecycle.Observer {
            Log.d("TAG", "评论列表发生变化")
            commentAdapter.submitList(it)
            commentAdapter.notifyDataSetChanged()
        })

        onClickListern()
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

    private fun generateAudioContent(audioName: String) = audioName.repeat(500)

    suspend fun prepareMedia() {
        _mediaPlayer = MediaPlayer()
        _mediaPlayer.apply {
            setDataSource(intent.getStringExtra(AUDIO_URL))
//                setDataSource("/data/data/com.example.music2/download/Wild Child - Crazy Bird.mp3")
            prepareAsync()
            setOnPreparedListener {
                avi.hide()
                Log.d("TAG", "准备就绪,音频长度：" + _mediaPlayer.duration)
            }
            setOnCompletionListener {
                Glide.with(this@AudioActivity).load(R.drawable.ic_pause).into(audio_image_ivew)
                Log.d("TAG", "播放结束")
            }
        }
    }

    fun onClickListern() {

        edit_comment.setOnClickListener {
            Log.d("TAG", "你选择了评论框")
            if (!shared.getBoolean("LOGIN_STATE", false)) {
                "请先登录再评论!".showToast(this)
                return@setOnClickListener
            }
        }

        push_comment.setOnClickListener {
            Log.d("TAG", "你点击了推送评论按钮")
            hideInput()

            /*发送评论信息*/
            var comment = Comment()
            comment.replyAlbumId = intent.getIntExtra(ALBUM_ID, -1)
            comment.commContent = edit_comment.text.toString()
            comment.commTime = Date()
            comment.commName = shared.getString("name", BaseConst.USER_INFO_NULL).toString()

            /*0 代表 评论当前专辑   其余数字代表评论编号*/
            comment.replyCommId = 0
            comment.headerImageUrl =
                shared.getString("headerImageUrl", BaseConst.USER_HISTORY_EMPTY).toString()

            Log.d("TAG", JSON.toJSONString(comment))

            MainScope().launch {
                val bo = coroutineScope { commentViewModel.addComment(comment) }
                bo.message.showToast(this@AudioActivity)
                edit_comment.setText("")
            }
        }

        fabDownload.setOnClickListener {
            Log.d("TAG", "你点击了下载悬浮按钮")

            CoroutineScope(Dispatchers.IO).launch {
                val bo = audioViewModel.downAlbum(intent.getStringExtra(AUDIO_URL))
                Looper.prepare()
                bo.message.showToast(this@AudioActivity)
                Looper.loop()
            }
        }

        fabPlay.setOnClickListener {
            Log.d("TAG", "你点击了悬浮按钮")
            if (!buttonState) {

                fabPlay.setImageDrawable(resources.getDrawable(R.drawable.ic_pause, null))
                _mediaPlayer.start()
                buttonState = !buttonState

                UserInfo.addRecent(intent.getIntExtra(ALBUM_ID, -1))

                val edit = shared.edit()
                edit.putString("recentlyListen", UserInfo.recentlyListen)
                edit.apply()

            } else {
                fabPlay.setImageDrawable(resources.getDrawable(R.drawable.ic_play_arrow, null))
                _mediaPlayer.pause()
                buttonState = !buttonState
            }
            Log.d("TAG", "播放： " + _mediaPlayer.isPlaying)
        }

        fabLrc.setOnClickListener {
            val intent = Intent(this, LrcActivity::class.java)
            intent.putExtra(LrcActivity.LRC_NAME, audioName)
            intent.putExtra(LrcActivity.LRC_URL, lrcUrl)
            mediaPlayer = _mediaPlayer
            startActivity(intent)
        }
    }

    //隐藏键盘
    fun hideInput() {
        val imm = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
        val v = getWindow().peekDecorView();
        if (null != v) {
            imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
        }
    }

    override fun onDestroy() {
        _mediaPlayer.stop()
        _mediaPlayer.release()
        Log.d("TAG", "销毁media")
        try {
            /*如果登录，就同步最近收听信息*/
            if (shared.getBoolean("LOGIN_STATE", false)) {
                val user = User()
                user.id = shared.getInt("id", -1)
                user.recentlyListen = UserInfo.recentlyListen
                MainScope().launch(Dispatchers.IO) {
                    recViewModel.updateRecentlyListenList(user)
                    Log.d("TAG", Thread.currentThread().name + " : 完成更新最近常听")
                }
            } else {
                val editor = shared.edit()
                editor.putString("recentlyListen", UserInfo.recentlyListen)
                editor.apply()
            }
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            super.onDestroy()
        }
    }


}
