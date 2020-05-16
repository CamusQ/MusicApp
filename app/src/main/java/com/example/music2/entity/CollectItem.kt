package com.example.music2.entity

class CollectItem(
    val collected: Boolean = true,
    id: Int,
    producerName: String,
    producerNickname: String,
    albumCoverUrl: String,
    mediaLrcUrl: String,
    mediaUrl: String,
    albumName: String,
    headerIcon: String,
    contentDesc: String

) : AlbumItem(
    id,
    producerName,
    producerNickname,
    albumCoverUrl,
    mediaLrcUrl,
    mediaUrl,
    albumName,
    headerIcon,
    contentDesc
)