package com.example.music2.entity

import java.io.Serializable
import java.util.*

class Comment : Serializable {

    /*评论id*/
    var id: Int? = null

    /*要评论的专辑id*/
    var replyAlbumId: Int? = null

    /*待回复的评论id*/
    var replyCommId: Int? = null

    /*评论时间*/
    var commTime: Date? = null

    /*评论的用户名称*/
    var commName: String? = null

    /*评论内容*/
    var commContent: String? = null

    /*头像id*/
    var headerImageUrl: String = ""
}