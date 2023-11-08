package com.example.flo

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter

class BannerVPAdapter(fragment: Fragment) :FragmentStateAdapter(fragment){

    private val fragmentlist: ArrayList<Fragment> = ArrayList()
    //fragmentlist는 Fragment타입들의 배열인 ArrayList

    override fun getItemCount(): Int {
        return fragmentlist.size
    }
    //override fun getItemCount(): Int  = fragmentlist.size와 같음

    override fun createFragment(position: Int): Fragment = fragmentlist[position]
/*override fun createFragment(position: Int)
 FragmentStateAdapter에서 오버라이드하여 구현되는 메서드
 ViewPager2가 특정 위치(position)에 있는 Fragment를 생성할 때 호출
 */
    fun addFragment(fragment:Fragment){
        fragmentlist.add(fragment)
        notifyItemInserted(fragmentlist.size-1) //새로운 값이 추가된 위치
    }
}
