package com.example.music2.activity

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.alibaba.fastjson.JSON
import com.example.music2.R
import com.example.music2.base.BaseConst
import com.example.music2.base.InitUserInfo
import com.example.music2.utils.showToast
import com.example.music2.viewModel.LoginViewModel
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class LoginActivity : AppCompatActivity() {

    val viewModel by lazy { ViewModelProvider(this).get(LoginViewModel::class.java) }

    private val editor: SharedPreferences.Editor by lazy { getSharedPreferences("userInfo", Context.MODE_PRIVATE).edit() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        setSupportActionBar(login_toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

//        window.statusBarColor = resources.getColor(R.color.textBackground)

        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
        val exceptionHandler = CoroutineExceptionHandler { coroutineContext, throwable ->
            throwable.printStackTrace()
        }

        login.setOnClickListener {

            hideInput()
            loading.visibility = View.VISIBLE

            lifecycleScope.launch(exceptionHandler) {
                val loginResponseBO = withContext(Dispatchers.IO) {
                    viewModel.login(
                        username.text.toString(),
                        password.text.toString()
                    )
                }

                Log.d("登录消息", JSON.toJSONString(loginResponseBO))

//                Looper.prepare()
                if (loginResponseBO.code == BaseConst.CODE_SUCCESS) {

                    loading.visibility = View.GONE
                    loginResponseBO.message.showToast(this@LoginActivity)

                    val user = loginResponseBO.user

                    /*更新本地用户信息*/
                    InitUserInfo.updateSharedUserInfo(editor,user)

                    val intent = Intent(this@LoginActivity, PersonalityActivity::class.java)
                    this@LoginActivity.startActivity(intent)

                } else {
                    loading.visibility = View.GONE
                    loginResponseBO.message.showToast(this@LoginActivity)
                }
//                Looper.loop()
            }


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


    //隐藏键盘
    fun hideInput() {
        val imm = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
        val v = getWindow().peekDecorView();
        if (null != v) {
            imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
        }
    }

}