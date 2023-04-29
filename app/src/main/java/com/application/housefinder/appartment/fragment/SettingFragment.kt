package com.application.housefinder.appartment.fragment

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.application.housefinder.appartment.MyPostActivity
import com.application.housefinder.appartment.databinding.FragmentSettingBinding

class SettingFragment : Fragment() {
    lateinit var binding : FragmentSettingBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSettingBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.tvMyPost.setOnClickListener {
            startActivity(Intent(requireActivity(),MyPostActivity::class.java))
        }
    }


}