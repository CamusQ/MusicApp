package com.example.music2.entity

data class LRC(
    val code: Int,
    val count: Int,
    val result: List<Result>


)


data class Result(

    val aid: Int,
    val artist_id: Int,
    val lrc: String,
    val sid: Int,
    val song: String
)