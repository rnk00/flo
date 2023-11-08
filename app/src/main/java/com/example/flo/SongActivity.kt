package com.example.flo

import android.media.MediaPlayer
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.flo.databinding.ActivitySongBinding
import com.google.gson.Gson

class SongActivity : AppCompatActivity(){   //:는 상속. () 써줘야함
    lateinit var binding : ActivitySongBinding
    //lateinit var song : Song
    lateinit var timer: Timer
    private var mediaPlayer:MediaPlayer? = null
    private var gson: Gson = Gson()

    val songs = arrayListOf<Song>()
    lateinit var songDB: SongDatabase
    var nowPos = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySongBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initPlayList()
        initSong()
        initClickListener()
        //setPlayer(song)
//        if(intent.hasExtra("title")&&intent.hasExtra("singer")){
//            binding.songMusicTitleTv.text=intent.getStringExtra("title")
//            binding.songSingerNameTv.text=intent.getStringExtra("singer")
//        } 왜 주석처리한거지...그러고서 initsong()다음거 써넣음
    }

    override fun onPause() {
        super.onPause()
        setPlayerStatus(false)
        songs[nowPos].second = ((binding.songProgressSb.progress * songs[nowPos].playTime)/100)/1000  //재생 시간 저장
        //song을 songs[nosPos]로 대체
        val sharedPreferences = getSharedPreferences("song",MODE_PRIVATE)   //MODE_PRIVATE: 앱 내에서만 사용
        val editor = sharedPreferences.edit()
        //editor.putString("title",song.title) 이런식으로 하나하나 다 넣기 힘들어서 JSON을 사용한다

        //val songJson = gson.toJson(songs[nowPos])    //song을 JSON으로 바꿔줌
        //editor.putString("songData", songJson)     앱이 종료될 때 song이 아닌 songs[nowPos] 저장
        editor.putInt("songId",songs[nowPos].id)
        editor.apply()
    }//포커스를 잃었을 때 음악 중지

    override fun onDestroy() {
        super.onDestroy()
        timer.interrupt()
        mediaPlayer?.release() //미디어플레이어가 갖고 있던 리소스 해제
        mediaPlayer = null // 미디어 플레이어 해제
    }

    private fun initPlayList(){
        songDB = SongDatabase.getInstance(this)!!
        songs.addAll(songDB.songDao().getSongs())
    }

    private fun initClickListener() {
        binding.songDownIb.setOnClickListener {
            finish()
        }//songDownIb를 누를 때 실행될 작업을 구현

        binding.songMiniplayerIv.setOnClickListener{
            setPlayerStatus(true)
        }

        binding.songPauseIv.setOnClickListener{
            setPlayerStatus(false)
        }

        binding.songNextIv.setOnClickListener{
            moveSong(+1)
        }

        binding.songPreviousIv.setOnClickListener{
            moveSong(-1)
        }

        binding.songLikeIv.setOnClickListener{
            setLike(songs[nowPos].isLike)
        }
    }

    private fun initSong() {
        val spf = getSharedPreferences("song", MODE_PRIVATE)
        val songId = spf.getInt("songId", 0)
        nowPos = getPlayingSongPosition(songId)

        Log.d("now Song ID",songs[nowPos].id.toString())
        //sharedPreference에서 id값을 받아와 songId를 통해 songs와 비교해 index값을 구하는 함수
//        if(intent.hasExtra("title")&&intent.hasExtra("singer")){
//            song = Song(
//                intent.getStringExtra("title")!!,
//                intent.getStringExtra("singer")!!,
//            /*!!는 값이 null인 경우 Null Pointer Exception 발생
//            "non-null 단언 연산자" 또는 "뱅 연산자"라고도 한다
//            변수가 항상 null이 아니라고 확신할 때만 사용해야 한다. */
//                intent.getIntExtra("second",0),
//                intent.getIntExtra("playTime",0),
//                intent.getBooleanExtra("isPlaying",false)
//            )
//        }
        startTimer()
        setPlayer(songs[nowPos])
    }

    private fun setLike(isLike: Boolean){
        songs[nowPos].isLike = !isLike
        songDB.songDao().updateIsLikeById(!isLike, songs[nowPos].id)

        if(!isLike){
            binding.songLikeIv.setImageResource(R.drawable.ic_my_like_on)
        }else{
            binding.songLikeIv.setImageResource(R.drawable.ic_my_like_off)
        }
    }

    private fun moveSong(direct: Int){
        if(nowPos + direct < 0){
            Toast.makeText(this, "first song", Toast.LENGTH_SHORT).show()
            return
        }
        if(nowPos + direct >= songs.size){
            Toast.makeText(this, "last song", Toast.LENGTH_SHORT).show()
            return
        }
        nowPos += direct

        //새로운 노래를 실행해야 할 때 해야하는 것들
        timer.interrupt()
        startTimer()

        mediaPlayer?.release()
        mediaPlayer = null

        setPlayerStatus(songs[nowPos].isPlaying)//song이라 하면 왜 안되냐
    }
    private fun getPlayingSongPosition(songId:Int):Int{
        for(i in 0 until songs.size){
            if(songs[i].id == songId){
                return i
            }
        }
        return 0
    }

    private fun setPlayer(song:Song) {
        //binding.songMusicTitleTv.text = intent.getStringExtra("title")!!
        binding.songMusicTitleTv.text = song.title
        //binding.songSingerNameTv.text = intent.getStringExtra("singer")!!]
        binding.songSingerNameTv.text = song.singer
        binding.songStartTimeTv.text = String.format("%02d:%02d",song.second/60, song.second%60)
        binding.songEndTimeTv.text = String.format("%02d:%02d",song.playTime/60,song.playTime%60)
        binding.songAlbumIv.setImageResource(song.coverImg!!)
        binding.songProgressSb.progress = (song.second * 1000 / song.playTime)

        val music = resources.getIdentifier(song.music, "raw", this.packageName)
        mediaPlayer = MediaPlayer.create(this, music)

        if(song.isLike){
            binding.songLikeIv.setImageResource(R.drawable.ic_my_like_on)
        }else{
            binding.songLikeIv.setImageResource(R.drawable.ic_my_like_off)
        }
        setPlayerStatus(song.isPlaying)
    }

    fun setPlayerStatus(isPlaying: Boolean){
        songs[nowPos].isPlaying = isPlaying
        timer.isPlaying = isPlaying

        if(isPlaying){
            binding.songMiniplayerIv.visibility = View.VISIBLE
            binding.songPauseIv.visibility = View.GONE
            mediaPlayer?.start()
        }
        else{
            binding.songMiniplayerIv.visibility = View.GONE
            binding.songPauseIv.visibility = View.VISIBLE
            if(mediaPlayer?.isPlaying == true){
                mediaPlayer?.pause()
            }
        }
    }

    private fun startTimer(){
        timer = Timer(songs[nowPos].playTime, songs[nowPos].isPlaying)
        timer.start()
    }
    inner class Timer(private val playTime: Int, var isPlaying: Boolean = true):Thread(){
        private var second : Int = 0
        private var mills: Float = 0f

        override fun run(){
            super.run()
            try {
                while(true){
                    if(second>=playTime){
                        break
                    }
                    if(isPlaying){
                        sleep(50)//????????@@@@@@@@@@@@@@@@@
                        mills += 50

                        runOnUiThread{
                            binding.songProgressSb.progress = ((mills/playTime)*100).toInt()
                        }//퍼센트로 환산하기 위해 100 곱함
                        if(mills%1000 == 0f){
                            runOnUiThread {
                                binding.songStartTimeTv.text = String.format("%02d:%02d",second/60, second%60)
                            }
                            second++
                        }//1s=1000ms
                    }
                }
            }catch(e: InterruptedException){
            }

        }


    }



}
/*
var는 나중에 값을 수정할 수 있지만 val은 수정할 수 없다.
var (변수 이름) : (변수 타입) = (초기화 값)
*/