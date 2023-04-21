package com.application.housefinder.appartment.fragment

import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.application.housefinder.appartment.databinding.FragmentFeedBinding

class FeedFragment : Fragment() {
    lateinit var binding: FragmentFeedBinding
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentFeedBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Handler().postDelayed({ binding.rcvData.hideShimmerAdapter() }, 3000)

    }


}