package com.lovesme.homegram.presentation.ui.setting

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.firebase.dynamiclinks.ktx.androidParameters
import com.google.firebase.dynamiclinks.ktx.dynamicLink
import com.google.firebase.dynamiclinks.ktx.dynamicLinks
import com.google.firebase.ktx.Firebase
import com.lovesme.homegram.databinding.ActivitySettingBinding
import com.lovesme.homegram.BuildConfig
import com.lovesme.homegram.data.model.Result
import com.lovesme.homegram.data.repository.UserPreferencesRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

class SettingActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySettingBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySettingBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.inviteLinkTv.setOnClickListener {
            CoroutineScope(Dispatchers.IO).launch {
                val result = async {
                    createLink()
                }
                val dynamicLink = result.await()
                if(dynamicLink != null){
                    shareLink(dynamicLink.toString())
                }
            }
        }
    }

    private fun shareLink(test: String) {
        val intent = Intent().apply {
            action = Intent.ACTION_SEND
            putExtra(Intent.EXTRA_TEXT, test)
            type = "text/plain"
        }
        startActivity(Intent.createChooser(intent, "Invite significant other"))
    }

    private suspend fun createLink(): Uri? {
        val deferredGroupId = CoroutineScope(Dispatchers.IO).async {
            UserPreferencesRepository().getGroupId()
        }
        val result = deferredGroupId.await()
        return if (result is Result.Success) {
            val groupCode = result.data
            val dynamicLink = Firebase.dynamicLinks.dynamicLink {
                link = Uri.parse("https://homegram.com/?code=$groupCode")
                domainUriPrefix = BuildConfig.LINK_PREFIX_URL
                androidParameters { }
            }
            dynamicLink.uri
        } else {
            null
        }
    }
}