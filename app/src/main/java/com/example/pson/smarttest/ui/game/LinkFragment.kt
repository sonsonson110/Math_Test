package com.example.pson.smarttest.ui.game

import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.pson.smarttest.R
import com.example.pson.smarttest.databinding.FragmentLinkBinding

class LinkFragment : Fragment() {

    companion object {
        const val FBID = "100006655110582"
        const val FBLINK = "https://www.facebook.com/pson02/"
        const val INSTALINK = "https://www.instagram.com/"
        const val GITHUBLINK = "https://github.com/sonsonson110"
    }

    private lateinit var binding: FragmentLinkBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentLinkBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //Facebook link
        binding.facebookIcon.setOnClickListener {
            val fbIntent = Intent(Intent.ACTION_VIEW).apply {
                data = Uri.parse("fb://profile/$FBID")
            }
            //yêu cầu mở ứng dụng, nếu không thì mở web
            try {
                context?.startActivity(fbIntent)
            } catch (e: ActivityNotFoundException) {
                Intent(Intent.ACTION_VIEW).apply {
                    data = Uri.parse(FBLINK)
                    context?.startActivity(this)
                }
            }
        }

        //Insta link
        binding.instaIcon.setOnClickListener {
            val instaIntent = Intent(Intent.ACTION_VIEW).apply {
                data = Uri.parse("$INSTALINK/_u/thaibinhhoa_/")
            }
            instaIntent.setPackage("com.instagram.android")

            try {
                startActivity(instaIntent)
            } catch (e: ActivityNotFoundException) {
                Intent(Intent.ACTION_VIEW).apply {
                    data = Uri.parse("$INSTALINK/thaibinhhoa_/")
                    context?.startActivity(instaIntent)
                }
            }
        }

        //github link
        binding.githubIcon.setOnClickListener {
            val githubIntent = Intent(Intent.ACTION_VIEW).apply {
                data = Uri.parse(GITHUBLINK)
                context?.startActivity(this)
            }
        }
    }
}