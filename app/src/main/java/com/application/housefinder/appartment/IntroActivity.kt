package com.application.housefinder.appartment

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.application.housefinder.appartment.databinding.ActivityIntroBinding
import com.application.housefinder.appartment.fragment.IntroFragment

class IntroActivity : AppCompatActivity() {
    lateinit var binding : ActivityIntroBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityIntroBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.viewpager.adapter = VpAdapter(supportFragmentManager)
        binding.indicatorDots.attachTo(binding.viewpager)

        binding.tvNext.setOnClickListener {
            if (binding.viewpager.currentItem < 2) {
                binding.viewpager.currentItem += 1
            } else {
                startActivity(Intent(this@IntroActivity,MainActivity::class.java))
                finish()
            }
        }
    }

    override fun onBackPressed() {
        if (binding.viewpager.currentItem > 0) {
            binding.viewpager.currentItem -= 1
        } else {
            super.onBackPressed()
        }
    }

    class VpAdapter(fragmentManager: FragmentManager) : FragmentPagerAdapter(fragmentManager) {
        override fun getCount(): Int {
            return 3
        }

        override fun getItem(position: Int): Fragment {
            return IntroFragment(position)
        }

    }
}