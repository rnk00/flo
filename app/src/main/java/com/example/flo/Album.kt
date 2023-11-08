package com.example.flo
//com.example.flo에 속함

import java.util.ArrayList

data class Album(   //data class: 데이터 저장, 관리
    var title: String? = "",
    var singer: String? = "",//String도 "" 말고 null로 해도 된다
    var coverImg: Int? = null,//0 아니고 null
    var songs: ArrayList<Song>? = null
)
