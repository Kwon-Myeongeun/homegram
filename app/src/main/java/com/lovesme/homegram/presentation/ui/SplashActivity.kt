package com.lovesme.homegram.presentation.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.google.firebase.dynamiclinks.ktx.dynamicLinks
import com.google.firebase.ktx.Firebase
import com.lovesme.homegram.databinding.ActivitySplashBinding
import com.lovesme.homegram.presentation.ui.signin.SignInActivity

class SplashActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySplashBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
        super.onCreate(savedInstanceState)
        binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding.root)
        handleDynamicLinks()
    }

    private fun handleDynamicLinks() {
        Firebase.dynamicLinks
            .getDynamicLink(intent)
            .addOnSuccessListener(this) { linkData ->
                if (linkData != null && linkData.link != null) {
                    val groupId = linkData.link?.getQueryParameter("code")
                    Intent(this, SignInActivity::class.java).apply {
                        putExtra("groupId", groupId)
                        startActivity(this)
                    }
                    finish()
                } else {
                    startActivity(Intent(this, SignInActivity::class.java))
                    finish()
                }
            }
    }
}