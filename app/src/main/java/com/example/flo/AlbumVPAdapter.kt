package com.example.flo

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter

class AlbumVPAdapter(fragment:Fragment):FragmentStateAdapter(fragment) {
    override fun getItemCount(): Int = 3
    //수록곡, 상세정보, 영상이라서 3

    override fun createFragment(position: Int): Fragment {
        return when(position){
            0->SongFragment()
            1->DetailFragment()
            else->VideoFragment()
        }
    }
    //when은 조건에 따라 다른 것을 수행(switch와 비슷)
}
//ViewPager2가 스와이프할 때마다 해당 위치에 맞는 프래그먼트를 생성하여 화면에 표시합니다.