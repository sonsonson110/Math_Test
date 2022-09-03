package com.example.pson.smarttest.ui.game

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.pson.smarttest.R
import com.example.pson.smarttest.databinding.FragmentLinkBinding

class LinkFragment : Fragment() {

    private lateinit var binding: FragmentLinkBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentLinkBinding.inflate(inflater, container, false)
        return binding.root
    }
}