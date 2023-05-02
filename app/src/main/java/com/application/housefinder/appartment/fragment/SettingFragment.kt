package com.application.housefinder.appartment.fragment

import android.content.Intent
import android.os.Bundle
import android.preference.PreferenceManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.application.housefinder.appartment.*
import com.application.housefinder.appartment.databinding.FragmentSettingBinding

class SettingFragment : Fragment() {
    lateinit var binding: FragmentSettingBinding

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
            startActivity(Intent(requireActivity(), MyPostActivity::class.java))
        }

        binding.tvChangePassword.setOnClickListener {
            startActivity(Intent(requireActivity(), ChangePasswordActivity::class.java))
        }

        binding.tvUserInformation.setOnClickListener {
            startActivity(Intent(requireActivity(), InformationActivity::class.java))
        }

        binding.tvAboutUs.setOnClickListener {
            startActivity(Intent(requireActivity(), AboutUsActivity::class.java))
        }

        binding.tvLogOut.setOnClickListener {
            Application.username = ""

            val prefs = PreferenceManager.getDefaultSharedPreferences(requireActivity())
            prefs.edit().putString("username","").apply()
            prefs.edit().putString("password","").apply()

            startActivity(
                Intent(
                    requireActivity(),
                    LoginActivity::class.java
                ).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
            )

        }
    }


}