package com.lovesme.homegram.ui.setting

import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.dynamiclinks.ktx.androidParameters
import com.google.firebase.dynamiclinks.ktx.dynamicLink
import com.google.firebase.dynamiclinks.ktx.dynamicLinks
import com.google.firebase.ktx.Firebase
import com.lovesme.homegram.BuildConfig
import com.lovesme.homegram.R
import com.lovesme.homegram.data.model.UiState
import com.lovesme.homegram.databinding.ActivitySettingBinding
import com.lovesme.homegram.ui.signin.SignInActivity
import com.lovesme.homegram.ui.viewmodel.SettingViewModel
import com.lovesme.homegram.util.Constants
import com.lovesme.homegram.util.location.LocationService
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
        binding.userSignOutTv.setOnClickListener {
            val locationServiceIntent = Intent(this, LocationService::class.java)
            locationServiceIntent.putExtra(Constants.PARCELABLE_SERVICE_STOP, true)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                this.startForegroundService(locationServiceIntent)
            } else {
                this.startService(locationServiceIntent)
            }
            settingViewModel.deleteUserInfo()
        }

        binding.settingToolbar.setNavigationOnClickListener {
            finish()
        }

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                settingViewModel.uiState.collect { state ->
                    when (state) {
                        is UiState.Success -> {
                            setCheckDialog(this@SettingActivity)
                        }
                        is UiState.Error -> {
                            Snackbar.make(
                                binding.root,
                                getString(R.string.signout_fail),
                                Snackbar.LENGTH_SHORT
                            ).show()
                            return@collect
                        }
                        else -> {
                            return@collect
                        }
                    }
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

    private fun setCheckDialog(context: Context) {
        val builder = AlertDialog.Builder(context)
        builder.setTitle("탈퇴 시 모든 데이터가 삭제됩니다.\n정말 탈퇴하시겠습니까?")

        val listener = DialogInterface.OnClickListener { _, p1 ->
            when (p1) {
                DialogInterface.BUTTON_POSITIVE ->
                    signOut()
            }
        }
        builder.setPositiveButton("네", listener)
        builder.setNegativeButton("아니오", null)

        builder.show()
    }

    private fun signOut() {
        Firebase.auth.currentUser?.delete()
        FirebaseAuth.getInstance().signOut()

        val intent = Intent(this@SettingActivity, SignInActivity::class.java)
        intent.flags =
            Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
    }
}