package com.example.music2.base

import java.io.Serializable
import java.util.*

class User : Serializable {

    var userName = "老胡"

    var password: String? = null

    var id : Int? = null

    var collectList: String? = null

    var name: String? = null

    var nickName: String? = null

    var sex: String? = null

    var age: String? = null

    var registerData: Date? = null

    var isSinger: Int? = null

    var headerImageUrl: String? = null

    var albumName: String? = null

    var uploadDate: Date? = null

    var ispassed: Int? = null

    var recentlyListen: String? = null
}