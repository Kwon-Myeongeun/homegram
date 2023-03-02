package com.lovesme.homegram.presentation.ui.invite

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.lovesme.homegram.databinding.ActivityInviteLinkBinding

class InviteLinkActivity : AppCompatActivity() {

    private lateinit var binding: ActivityInviteLinkBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityInviteLinkBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}