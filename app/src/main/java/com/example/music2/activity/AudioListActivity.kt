package com.example.music2.activity

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.core.view.iterator
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.bumptech.glide.Glide
import com.example.music2.R
import com.example.music2.adapter.AudioItemAdapter
import com.example.music2.base.BaseConst
import com.example.music2.base.HandleData
import com.example.music2.utils.showToast
import com.example.music2.viewModel.AudioListViewModel
import de.hdodenhof.circleimageview.CircleImageView
import kotlinx.android.synthetic.main.activity_audio_list.*
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking


class AudioListActivity : AppCompatActivity() {

    val shared by lazy { getSharedPreferences("userInfo", Context.MODE_PRIVATE) }

    val viewmodel by lazy { ViewModelProvider(this).get(AudioListViewModel::class.java) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_audio_list)

        setSupportActionBar(toolbar)
        avi_audio_list.show()

        supportActionBar?.let {
            it.setDisplayHomeAsUpEnabled(true)
            it.setHomeAsUpIndicator(R.drawable.ic_menu)
        }

        nav_view.setCheckedItem(R.id.nav_collect)
        if (shared.getInt("isSinger", -1) != BaseConst.IS_SINGER) {
            nav_view.menu.findItem(R.id.nav_upload).isVisible = false
        }

        if (Build.VERSION.SDK_INT >= 23) {
            val REQUEST_CODE_CONTACT = 101
            val permissions = ArrayList<String>()
            permissions.add(Manifest.permission.WRITE_EXTERNAL_STORAGE)
//            val permissions : Array<String> = { Manifest.permission.WRITE_EXTERNAL_STORAGE}
            //验证是否许可权限
            for (str in permissions) {
                if (this.checkSelfPermission(str) != PackageManager.PERMISSION_GRANTED) {
                    //申请权限
                    this.requestPermissions(permissions.toTypedArray(), REQUEST_CODE_CONTACT);
                }
            }
        }



        setOnClickListener()

        runBlocking {
            try {
                viewmodel.fetchAudioList(0, 20)
            } catch (e: Exception) {
                "网络加载异常!".showToast(this@AudioListActivity)
            }
        }
        recycleView.layoutManager = GridLayoutManager(this, 1)
        recycleView.adapter = AudioItemAdapter(this, viewmodel.albumList)
        avi_audio_list.hide()


    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> drawer_layout.openDrawer(GravityCompat.START)
        }
        return true
    }

    private fun setOnClickListener() {
        val headerView = nav_view.inflateHeaderView(R.layout.nav_header)
        val headerImage: CircleImageView = headerView.findViewById(R.id.header_image)

//        val headerImageView = findViewById<ImageView>(R.id.header_image)
        val headerImage2 = shared.getString("headerImageUrl", BaseConst.USER_INFO_NULL)
        if (shared.getBoolean("LOGIN_STATE", false) && headerImage2 != null) {
            if (headerImage2.contains("http")) {
                Glide.with(this).load(headerImage2).into(headerImage)
            }
        }

        headerImage.setOnClickListener {
            /*头像*/
            "点击了头像".showToast(this)

            if (shared.getBoolean("LOGIN_STATE", false)) {
                /*如果已登录 进入用户详情*/
                val intent = Intent(this, PersonalityActivity::class.java)
                startActivity(intent)
            } else {
                /*未登录 进入登录注册页面*/
                val intent = Intent(this, LoginActivity::class.java)
                startActivity(intent)
            }
        }

        nav_view.setNavigationItemSelectedListener {

            when (it.itemId) {
                /*收藏夹*/
                R.id.nav_collect -> {
                    val intent = Intent(this, CollectFolderActivity::class.java)
                    intent.putExtra(
                        CollectFolderActivity.NICK_NAME,
                        shared.getString("nickName", "暂无用户昵称信息")
                    )
                    startActivity(intent)
                }
                /*最近收听*/
                R.id.nav_recently -> {
                    val intent = Intent(this, RecentlyListenActivity::class.java)
                    intent.putExtra(RecentlyListenActivity.USER_ID, shared.getInt("id", -1))
                    startActivity(intent)
                }
                /*上传*/
                R.id.nav_upload -> {
                    val intent = Intent(this, UploadActivity::class.java)
                    startActivity(intent)
                }

                /*下载*/
                R.id.nav_download -> {
                    val intent = Intent(this, DownloadActivity::class.java)
                    startActivity(intent)
                }

            }
            drawer_layout.closeDrawers()
            true
        }
    }


}
