package com.lovesme.homegram.ui.setting

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.dynamiclinks.ktx.androidParameters
import com.google.firebase.dynamiclinks.ktx.dynamicLink
import com.google.firebase.dynamiclinks.ktx.dynamicLinks
import com.google.firebase.ktx.Firebase
import com.lovesme.homegram.BuildConfig
import com.lovesme.homegram.R
import com.lovesme.homegram.databinding.ActivitySettingBinding
import com.lovesme.homegram.ui.viewmodel.SettingViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.*

@AndroidEntryPoint
class SettingActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySettingBinding
    private val settingViewModel: SettingViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySettingBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.inviteLinkTv.setOnClickListener {
            CoroutineScope(Dispatchers.IO).launch {
                val dynamicLink = createLink()
                if (dynamicLink != null) {
                    shareLink(dynamicLink.toString())
                }
            }
        }
    }

    private fun shareLink(url: String) {
        val intent = Intent().apply {
            action = Intent.ACTION_SEND
            putExtra(Intent.EXTRA_TEXT, getString(R.string.invitation_send_msg).format(url))
            type = "text/plain"
        }
        startActivity(Intent.createChooser(intent, getString(R.string.invitation_chooser_msg)))
    }

    private suspend fun createLink(): Uri? =
        withContext(CoroutineScope(Dispatchers.IO).coroutineContext) {
            val groupId = settingViewModel.getGroupId()

            val dynamicLink = Firebase.dynamicLinks.dynamicLink {
                link = Uri.parse(getString(R.string.invitation_url).format(groupId))
                domainUriPrefix = BuildConfig.LINK_PREFIX_URL
                androidParameters { }
            }
            dynamicLink.uri

        }
}