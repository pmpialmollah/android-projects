package com.nsoft.mybakery.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.nsoft.mybakery.R
import com.nsoft.mybakery.databinding.FragmentAnalyticsBinding

class AnalyticsFragment : Fragment() {
    private lateinit var binding: FragmentAnalyticsBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAnalyticsBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // my code starts here ---------------------------------------------------------------------



        // my code ends here ---------------------------------------------------------------------
    }
}