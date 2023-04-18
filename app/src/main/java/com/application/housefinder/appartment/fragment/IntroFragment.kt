package com.application.housefinder.appartment.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.application.housefinder.appartment.R
import com.application.housefinder.appartment.databinding.FragmentIntroBinding

class IntroFragment(var pos : Int) : Fragment() {
    lateinit var binding : FragmentIntroBinding
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentIntroBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        when(pos) {
            0 -> {
                binding.imgIntro.setImageResource(R.drawable.intro1)
                binding.introTitle.text = "Find apartment"
                binding.introContent.text = "Find and rent an apartment quickly and easily"
            }

            1 -> {
                binding.imgIntro.setImageResource(R.drawable.intro2)
                binding.introTitle.text = "Want to lease an apartment"
                binding.introContent.text = "Register an account and connect with anyone want to rent"
            }

            2 -> {
                binding.imgIntro.setImageResource(R.drawable.intro3)
                binding.introTitle.text = "Chatting together"
                binding.introContent.text = "exchange and negotiate for a contract"
            }
        }

    }
}