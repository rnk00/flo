package com.example.flo

import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "SongTable")
data class Song(
    var title: String = "", //제목
    var singer: String = "",    //가수
    var second: Int = 0,    //노래 시간
    var playTime: Int = 0,  //현재 재생시간
    var isPlaying: Boolean = false, //재생되고 있는지
    var music: String = "", //노래
    var coverImg: Int? = null,  //커버 이미지
    var isLike: Boolean = false //좋아요 되어있는지
){
    @PrimaryKey(autoGenerate = true) var id: Int = 0
}