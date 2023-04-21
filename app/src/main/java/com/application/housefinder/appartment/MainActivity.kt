package com.application.housefinder.appartment

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.ViewParent
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import androidx.viewpager.widget.ViewPager
import com.application.housefinder.appartment.databinding.ActivityMainBinding
import com.application.housefinder.appartment.fragment.AccountFragment
import com.application.housefinder.appartment.fragment.FeedFragment
import com.application.housefinder.appartment.fragment.InfoFragment

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
    }

    class VpAdapter(fragmentManager: FragmentManager) : FragmentPagerAdapter(fragmentManager) {
        override fun getCount(): Int {
            return 3
        }

        override fun getItem(position: Int): Fragment {

            return FeedFragment()

        }

    }
}