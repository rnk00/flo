package com.example.flo

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.flo.databinding.FragmentBannerBinding

class BannerFragment(val imgRes : Int) : Fragment() {
    lateinit var  binding : FragmentBannerBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentBannerBinding.inflate(inflater, container ,false)

        binding.bannerImageIv.setImageResource(imgRes)
        //bannerImageIv에 imgRes를 할당
        return binding.root
    }
}//bannerAdapter.addFragment(BannerFragment(R.drawable.img_home_viewpager_exp))로 사용됨