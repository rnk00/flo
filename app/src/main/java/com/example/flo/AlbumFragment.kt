package com.example.flo

import android.os.Bundle    //key-value 쌍을 저장할 수 있는 데이터 컨테이너
/*Fragment는 Bundle 객체를 사용하여 데이터를 다른 Fragment나 Activity로 전달하고 받습니다.
Bundle객체는 arguments라는 이름으로 Fragment에 포함된다.*/
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.flo.databinding.FragmentAlbumBinding
import com.google.android.material.tabs.TabLayoutMediator
import com.google.gson.Gson

class AlbumFragment : Fragment() {  //~Fragment는 Fragment 상속받음

    lateinit var binding: FragmentAlbumBinding
    //fragment_album과 이어짐
    //FragmentAlbumBinding 타입인 이름이 binding인 변수
    private var gson:Gson = Gson()

    private val information = arrayListOf("수록곡", "상세정보", "영상")

    override fun onCreateView(
        inflater: LayoutInflater,   //xml 레이아웃 파일의 view를 읽어 실제 view 객체로 변환
        container: ViewGroup?,      //Fragment의 부모 뷰
        savedInstanceState: Bundle? //이전 상태 정보를 포함하는 Bundle
    ): View? {//onCreate의 반환 타입이 View?  ?: nullable(null을 반환할 수도 있음)
        binding = FragmentAlbumBinding.inflate(inflater,container,false)
        //FragmentAlbumBinding 클래스를 사용하여 album_fragment.xml 레이아웃을 확장하고 그 결과를 binding 변수에 할당
        //binding을 사용하여 XML 레이아웃에 있는 뷰 요소에 액세스할 수 있게 됩니다.

        val albumJson = arguments?.getString("album")
        /* Fragment 클래스는 다른 Fragment나 Activity로부터 데이터나 인수를 전달받을 수 있는 Bundle 객체를 가지고 있다.
         arguments는 이 Bundle 객체를 나타냅니다.
         getString("album"): Bundle에서 "album"이라는 이름의 문자열을 albumJson에 할당
         *///Fragment에 전달된 인수(arguments) 중에서 "album"의 문자열 값을 가져옴
        val album = gson.fromJson(albumJson, Album::class.java) //JSON 데이터를 Album형식으로 바꾼다
        setInit(album)

        binding.albumBackIv.setOnClickListener {
            (context as MainActivity).supportFragmentManager.beginTransaction()
                .replace(R.id.main_frm, HomeFragment())
                .commitAllowingStateLoss()
        } //album화면에서 뒤로가기 버튼 누르면 main으로 돌아가게 함
        /*
        (context as MainActivity): context를 MainActivity 형식으로 캐스팅

        Fragment 트랜잭션: Fragment를 추가, 교체, 삭제, 다른 Fragment로 전환
        supportFragmentManager: Fragment의 트랜잭션을 관리하기 위한 매니저
        .beginTransaction(): Fragment 트랜잭션을 시작

        .replace(R.id.main_frm, HomeFragment()): 현재 Fragment를 HomeFragment로 대체

        .commitAllowingStateLoss():
        commit: Fragment 트랜잭션에서 수행한 변경 사항을 화면에 적용하여 업데이트
        AllowingStateLoss: 실행 중 예기치 않은 상황에서 상태 손실이 발생할 때도 앱이 멈추지 않도록 한다
        그러나 상태 손실을 허용하는 것은 권장되는 방법이 아니며, 트랜잭션을 가능한 상태에서 커밋하고 관리하는 것이 안전합니다.
        */

        val albumAdapter = AlbumVPAdapter(this)
        binding.albumContentVp.adapter = albumAdapter       //binding.albumContentVp.adapter = AlbumVPAdapter(this)
        TabLayoutMediator(binding.albumContentTb, binding.albumContentVp){
            tab, position->
            tab.text = information[position]
        }.attach()
        //Tb랑 Vp 연결해줌
        //binding. 다음에 나오는 건 fragment_album에 있는 id를 대문자로 바꾼 것
        /*
        val albumAdapter = AlbumVPAdapter(this)
        AlbumVPAdapter 클래스의 인스턴스를 생성하여 albumAdapter 변수에 할당
        AlbumVPAdapter: 페이지 간 전환 및 콘텐츠 관리

        binding.albumContentVp.adapter = albumAdapter:
        binding.albumContentVp는 ViewPager 위젯
        albumAdapter를 ViewPager에 연결하여 페이지의 내용을 표시, 전환

        TabLayoutMediator(binding.albumContentTb, binding.albumContentVp) { tab, position -> tab.text = information[position] }.attach():
        TabLayoutMediator
        TabLayout(albumContentTb)과 ViewPager(albumContentVp)를 연결하고 선택한 탭에 따라 페이지를 전환하도록 도와주는 클래스

        tab, position -> tab.text = information[position]
        각 탭을 설정하는 클로저(람다 함수)
        position은 현재 탭의 위치를 나타내며, tab은 해당 탭을 나타냅니다.
        information[position]에서 가져온 텍스트를 tab.text에 설정하여 탭의 텍스트를 동적으로 변경합니다.

        .attach()
        TabLayoutMediator를 적용하여 TabLayout과 ViewPager 연동
        */

        return binding.root //onCreat()는 View를 return
    }
    private fun setInit(album: Album){
        binding.albumAlbumIv.setImageResource(album.coverImg!!)
        binding.albumMusicTitleTv.text = album.title.toString()
        binding.albumSingerNameTv.text = album.singer.toString()
        //album 객체에서 가수의 이름(singer)을 가져와서 text 속성을 사용하여 해당 텍스트를 albumSingerNameTv에 설정
    }
}