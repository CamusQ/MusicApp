package com.example.music2.entity

open class  AlbumItem(
    var id: Int,
    //创作人
    var producerName: String,
    //昵称
    var producerNickname: String,
    //封面
    var albumCoverUrl: String,
    //歌词
    var mediaLrcUrl: String,
    //音乐
    var mediaUrl: String,
    //专辑名
    var albumName: String,
    //头像
    var headerIcon: String,
    //内容描述
    var contentDesc: String
)