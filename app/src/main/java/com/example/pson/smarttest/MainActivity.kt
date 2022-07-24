package com.example.pson.smarttest

import android.os.Bundle
import android.view.View
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //giấu status bar và actionBar
        supportActionBar?.hide()
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN)

        // TODO: Make the content under the nav and status bar so that UI stay the same
        //giấu thanh điều hướng
        window.decorView.setOnSystemUiVisibilityChangeListener { visibility ->
            // Note that system bars will only be "visible" if none of the
            // LOW_PROFILE, HIDE_NAVIGATION, or FULLSCREEN flags are set.
            if (visibility and View.SYSTEM_UI_FLAG_FULLSCREEN != 0) {
                // The system bars are NOT visible. Make any desired
                // adjustments to your UI, such as hiding the action bar or
                // other navigational controls.
                window.decorView.apply {
                    // Hide both the navigation bar and the status bar.
                    // SYSTEM_UI_FLAG_FULLSCREEN is only available on Android 4.1 and higher, but as
                    // a general rule, you should design your app to hide the status bar whenever you
                    // hide the navigation bar.
                    systemUiVisibility =
                        View.SYSTEM_UI_FLAG_HIDE_NAVIGATION or View.SYSTEM_UI_FLAG_FULLSCREEN
                }
            }
        }

        setContentView(R.layout.activity_main)
    }
}