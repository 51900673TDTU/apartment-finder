package com.application.housefinder.appartment

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.application.housefinder.appartment.databinding.ActivityWelcomeBinding


class WelcomeActivity : AppCompatActivity() {

    lateinit var binding : ActivityWelcomeBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityWelcomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.tvAbout.setOnClickListener {
            val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse("http://www.google.com"))
            startActivity(browserIntent)
        }

        binding.tvCreate.setOnClickListener {
            startActivity(Intent(this@WelcomeActivity,RegisterActivity::class.java))
            finish()

        }

        binding.tvSignIn.setOnClickListener {

        }

    }
}