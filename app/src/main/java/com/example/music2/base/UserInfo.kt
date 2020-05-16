package com.example.music2.base

import android.util.Log
import java.io.Serializable

/**
 * 这个类 优化的时候用sharedPrefs 代替
 */

object UserInfo : Serializable {
//    var id = 1

    var recentlyListen = ""

    private val recnetLRU = RecentLRU<Int, Int>()

    fun copyUserRecentInfo() {
        if (recentlyListen.isEmpty()){
            return
        }
        val cell = recentlyListen.replace(" ","").split(",")
        for (s in cell) {
            if(s == BaseConst.USER_HISTORY_EMPTY){
                return
            }
            val id = s.substring(0, s.indexOf("="))
            val counts = s.substring(s.indexOf("=") + 1)
            recnetLRU.put(id.toInt(),counts.toInt())
        }
    }

    fun addRecent(id: Int) {
        if (recnetLRU.containsKey(id)) {
            recnetLRU.get(id)?.plus(1)?.let { recnetLRU.put(id, it) }
        } else {
            recnetLRU.put(id, 1)
        }
        Log.d("最近播放榜单顺序：", recnetLRU.toString())
        recentlyListen = recnetLRU.toString().substring(1, recnetLRU.toString().indexOf("}"))
    }


}