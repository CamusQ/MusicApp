package com.example.music2.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.lifecycle.ViewModelProvider
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.input.input
import com.afollestad.materialdialogs.list.listItems
import com.bumptech.glide.Glide
import com.example.music2.R
import com.example.music2.base.BaseConst
import com.example.music2.base.User
import com.example.music2.utils.showToast
import com.example.music2.utils.successDialog
import com.example.music2.utils.waringDialog
import com.example.music2.viewModel.PersonalityViewModel
import kotlinx.android.synthetic.main.activity_audio_list.*
import kotlinx.android.synthetic.main.activity_personality.*
import kotlinx.coroutines.*


class PersonalityActivity : AppCompatActivity() {

    val viewModel by lazy { ViewModelProvider(this).get(PersonalityViewModel::class.java) }
    val shared by lazy { getSharedPreferences("userInfo", Context.MODE_PRIVATE) }

    val exceptionHandler = CoroutineExceptionHandler { coroutineContext, throwable ->
        throwable.printStackTrace()
    }
    lateinit var nickname: TextView
    lateinit var age: TextView
    lateinit var sex: TextView
    lateinit var username : TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_personality)

        setSupportActionBar(personality_toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        nickname = findViewById(R.id.cell_nickName)
        age = findViewById(R.id.cell_age)
        sex = findViewById(R.id.cell_sex)
        username = findViewById(R.id.user_name)

        val headerImageView  = findViewById<ImageView>(R.id.user_header_image)

        setOnClickListener()

        sex.text = shared.getString("sex", BaseConst.USER_INFO_NULL)
        nickname.text = shared.getString("nickName", BaseConst.USER_INFO_NULL)
        age.text = shared.getString("age", BaseConst.USER_INFO_NULL)
        username.text = nickname.text
        val headerImage = shared.getString("headerImageUrl",BaseConst.USER_INFO_NULL)
        if (headerImage != null) {
            if (headerImage.contains("http")){
                Glide.with(this).load(headerImage).into(headerImageView)
            }
        }
    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
//                drawer_layout.openDrawer(GravityCompat.START)
                val intent = Intent(this,AudioListActivity::class.java)
                startActivity(intent)
            }
        }
        return true
    }

    fun setOnClickListener() {
        all_nickName.setOnClickListener {
            "更改昵称".showToast(this)
            showNickNameDia()

        }

        all_age.setOnClickListener {
            "更改年龄".showToast(this)
            showAgeDia()

        }

        all_sex.setOnClickListener {
            "更改性别".showToast(this)
            showSexDia()

        }
    }


    private fun showNickNameDia() {
        MaterialDialog(this).show {
            title(R.string.nickName_title)
            positiveButton(R.string.positiveButton)
            input(maxLength = 15, hint = "新的昵称") { dialog, text ->
                val user = User()
                user.id = shared.getInt("id", -1)
                user.nickName = text.toString()

                runBlocking {
                    launch(exceptionHandler) {
                        updateUserInfo(user)
                    }
                }
            }

        }

    }

    private fun showAgeDia() {
        MaterialDialog(this).show {
            title(R.string.age_title)

            input { dialog, text ->
                val user = User()
                user.id = shared.getInt("id", -1)
                user.age = text.toString()

                MainScope().launch(exceptionHandler) {
                    updateUserInfo(user)
                }
            }
        }

    }

    private fun showSexDia() {

        MaterialDialog(this).show {
            title(R.string.sex_title)
            titleColor
            positiveButton(R.string.positiveButton)
            listItems(R.array.items) { dialog, index, text ->
                text.toString().showToast(this@PersonalityActivity)

                val user = User()
                user.id = shared.getInt("id", -1)
                user.sex = text.toString()

                MainScope().launch(exceptionHandler) {
                    updateUserInfo(user)
                }
            }
        }

    }

    suspend fun updateUserInfo(user: User) {
        val bo = withContext(Dispatchers.IO) {
            viewModel.updateUserInfo(user)
        }
        if (bo.code == BaseConst.CODE_SUCCESS) {
            successDialog(bo.message, this@PersonalityActivity)

            val editor = shared.edit()
            user.nickName?.let { editor.putString("nickName", it) }
            user.age?.let { editor.putString("age", user.age) }
            user.sex?.let { editor.putString("sex", it) }

            editor.apply()

            nickname.text = shared.getString("nickName", BaseConst.USER_INFO_NULL)
            sex.text = shared.getString("sex", BaseConst.USER_INFO_NULL)
            age.text = shared.getString("age", BaseConst.USER_INFO_NULL)
            username.text = nickname.text
        } else {
            waringDialog(bo.message, this@PersonalityActivity)
        }
    }


}
