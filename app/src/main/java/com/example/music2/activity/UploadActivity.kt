package com.example.music2.activity

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.webkit.MimeTypeMap
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.example.music2.R
import com.example.music2.base.BaseConst
import com.example.music2.base.HandleData
import com.example.music2.bo.ResponseBO
import com.example.music2.utils.showToast
import com.example.music2.viewModel.AudioViewModel
import kotlinx.android.synthetic.main.activity_upload.*
import kotlinx.android.synthetic.main.upload_music_cell.*
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch


class UploadActivity : AppCompatActivity() {

    val viewModel by lazy { ViewModelProvider(this).get(AudioViewModel::class.java) }

    val shared by lazy { getSharedPreferences("userInfo", Context.MODE_PRIVATE) }

    var imagePath = ""
    var lrcPath = ""
    var albumPath = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_upload)

        setSupportActionBar(upload_toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        /*效果： 深色状态栏*/
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR


        Log.d("TAG", "onCreate")
        setOnClickListener()
    }

    override fun onResume() {
        super.onResume()
        if (album_desc.text?.isNotEmpty()!! && imagePath.isNotEmpty() && albumPath.isNotEmpty() && lrcPath.isNotEmpty()) {
            publish_button.isEnabled = true
            publish_button.background = resources.getDrawable(R.drawable.btn_normal)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        Log.d("TAG", "onActivityResult")
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == 1) {
                val uri = data?.getData()

                var path = uri?.path
                if (path?.contains(Regex(".jpg"))!!) {
                    path = "/sdcard/" + path.substring(path.indexOf(":") + 1)
                    Log.d("封面文件路径", path)
                    Glide.with(applicationContext).load(path).into(upload_image)
                    imagePath = path
                    return
                }

                if (path.contains(Regex(".lrc"))) {
                    ci_filePath.text = path.substring(path.lastIndexOf("/") + 1)
                    path = "/sdcard/" + path.substring(path.indexOf(":") + 1)
                    Log.d("歌词文件路径", path)
                    lrcPath = path
                    return
                }
                if (path.contains(Regex(".mp3"))) {
                    qu_filePath.text = path.substring(path.lastIndexOf("/") + 1)
                    path = "/sdcard/" + path.substring(path.indexOf(":") + 1)
                    Log.d("专辑文件路径", path)
                    albumPath = path
                    return
                }
                "文件格式不正确,歌词：lrc,专辑：mp3,封面：jpg".showToast(this)
            }
        }


    }

    fun setOnClickListener() {

        publish_button.setOnClickListener {

            MainScope().launch {

                /*判断文件是否在数据库存在*/
                val name = albumPath.substring(albumPath.lastIndexOf("-") + 1,albumPath.lastIndexOf(".")).trim()
                val responseBO = viewModel.checkAlbum(name)
                if (responseBO.code == BaseConst.CODE_FAILED) {
                    "文件已存在".showToast(this@UploadActivity)
                    Log.d("TAG","文件已存在")
                    return@launch
                }


                var bo: ResponseBO
                val albumBO = coroutineScope { HandleData().uploadMusic(albumPath) }

                if (albumBO.code == BaseConst.CODE_SUCCESS) {
                    bo = coroutineScope { HandleData().uploadMusic(lrcPath) }
                } else {
                    "上传专辑失败".showToast(this@UploadActivity)
                    return@launch
                }

                if (bo.code == BaseConst.CODE_SUCCESS) {
                    bo = coroutineScope { HandleData().uploadMusic(imagePath) }
                } else {
                    "上传歌词失败".showToast(this@UploadActivity)
                    return@launch
                }

                if (bo.code == BaseConst.CODE_FAILED) {
                    "上传封面失败".showToast(this@UploadActivity)
                    return@launch
                }

                bo = viewModel.uploadAlbum(shared.getInt("id",-1),album_desc.text.toString())
                if(bo.code == BaseConst.CODE_FAILED){
                    "上传专辑描述信息失败".showToast(this@UploadActivity)
                    return@launch
                }

                "上传成功".showToast(this@UploadActivity)
                finish()
            }
        }

        qu_filePath.setOnClickListener {
            val intent = Intent(Intent.ACTION_GET_CONTENT)
            intent.type = "audio/mpeg"
            intent.addCategory(Intent.CATEGORY_OPENABLE)
            startActivityForResult(intent, 1)
        }

        ci_filePath.setOnClickListener {
            val intent = Intent(Intent.ACTION_GET_CONTENT)
            intent.type = "*/*"
            intent.addCategory(Intent.CATEGORY_OPENABLE)
            startActivityForResult(intent, 1)
        }

        upload_image.setOnClickListener {
            val intent = Intent(Intent.ACTION_GET_CONTENT);
            intent.type = "image/*"
            intent.addCategory(Intent.CATEGORY_OPENABLE);
            startActivityForResult(intent, 1);
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
