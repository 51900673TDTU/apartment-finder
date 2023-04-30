package com.application.housefinder.appartment

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.ViewParent
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import androidx.viewpager.widget.ViewPager
import com.application.housefinder.appartment.databinding.ActivityMainBinding
import com.application.housefinder.appartment.fragment.*

class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.viewPager.adapter = VpAdapter(supportFragmentManager)
        binding.bottomBar.setOnItemSelectedListener {
            binding.viewPager.currentItem = it
        }
        binding.viewPager.offscreenPageLimit = 3

        binding.icMessage.setOnClickListener {
            startActivity(Intent(this@MainActivity,ChatActivity::class.java))
        }
    }

    class VpAdapter(fragmentManager: FragmentManager) : FragmentPagerAdapter(fragmentManager) {
        override fun getCount(): Int {
            return 3
        }

        override fun getItem(position: Int): Fragment {

            return when(position) {
                0 -> {
                    FeedFragment()
                }

                2 -> {
                    SettingFragment()
                }

                else -> {
                    SearchFragment()
                }
            }

        }

    }
}