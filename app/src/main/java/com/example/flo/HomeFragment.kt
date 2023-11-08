package com.example.flo

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.viewpager2.widget.ViewPager2
import com.example.flo.databinding.FragmentHomeBinding
import com.google.gson.Gson
import java.util.ArrayList

class HomeFragment : Fragment() {

    lateinit var binding: FragmentHomeBinding
    private var albumDatas = ArrayList<Album>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeBinding.inflate(inflater, container, false)

//        binding.homeAlbumImgIv1.setOnClickListener {
//            (context as MainActivity).supportFragmentManager.beginTransaction()
//                .replace(R.id.main_frm , AlbumFragment())   //main_frm의 범위를 AlbumFragment의 화면으로 바꿈
//                .commitAllowingStateLoss()
//        }//homeAlbumImgIv1을 누르면 실행되는 것 구현
        //오늘 발매 음악을 array list로 다시 만들어서 iv1이 없어짐
        albumDatas.apply {
            add(Album("Butter", "방탄소년단", R.drawable.img_album_exp))
            add(Album("Lilac", "아이유", R.drawable.img_album_exp2))
            add(Album("Next Level", "에스파", R.drawable.img_album_exp3))
            add(Album("Boy with Luv", "방탄소년단", R.drawable.img_album_exp4))
            add(Album("BBoom BBoom", "모모랜드", R.drawable.img_album_exp5))
            add(Album("Weekend", "태연", R.drawable.img_album_exp6))
            //ArrayList에 담을 것들

            val albumRVAdapter = AlbumRVAdapter(albumDatas)
            binding.homeTodayMusicAlbumRv.adapter = albumRVAdapter
            binding.homeTodayMusicAlbumRv.layoutManager =
                LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)

            albumRVAdapter.setMyItemClickListener(object : AlbumRVAdapter.MyItemClickListener {
                override fun onItemClick(album: Album) {
                    extracted(album)//extracted함수의 body는 원래 여기 있었음
                }

                /*
            override fun onRemoveAlbum(position:Int){
                albumRVAdapter.removeItem(position)
            }앨범 없애는 거 한 번 해봄
            */
            })

            val bannerAdapter = BannerVPAdapter(this)
            bannerAdapter.addFragment(BannerFragment(R.drawable.img_home_viewpager_exp))
            bannerAdapter.addFragment(BannerFragment(R.drawable.img_home_viewpager_exp2))
            binding.homeBannerVp.adapter = bannerAdapter // ViewPager 2가 배너 이미지를 표시
            binding.homeBannerVp.orientation = ViewPager2.ORIENTATION_HORIZONTAL

            return binding.root
        }
    }
    private fun extracted(album: Album) {
        (context as MainActivity).supportFragmentManager.beginTransaction()
            .replace(R.id.main_frm, AlbumFragment().apply {
                arguments = Bundle().apply {
                    val gson = Gson()
                    val albumJson = gson.toJson(album)
                    putString("album", albumJson)
                }
            })   //main_frm의 범위를 AlbumFragment의 화면으로 바꿈
            .commitAllowingStateLoss()
    }//refractor - function하면 함수가 바깥에 만들어짐
}