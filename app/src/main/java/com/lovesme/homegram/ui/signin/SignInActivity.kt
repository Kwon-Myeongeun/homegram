package com.lovesme.homegram.ui.signin

import android.Manifest
import android.content.Intent
import android.content.IntentSender
import android.os.Build
import android.os.Bundle
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.IntentSenderRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.PermissionChecker
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.google.android.gms.auth.api.identity.Identity
import com.google.android.gms.auth.api.identity.SignInClient
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.dynamiclinks.ktx.dynamicLinks
import com.google.firebase.ktx.Firebase
import com.lovesme.homegram.R
import com.lovesme.homegram.data.model.Result
import com.lovesme.homegram.data.model.UiState
import com.lovesme.homegram.databinding.ActivitySignInBinding
import com.lovesme.homegram.ui.main.MainActivity
import com.lovesme.homegram.ui.setting.UserPreferenceActivity
import com.lovesme.homegram.ui.viewmodel.SignInViewModel
import com.lovesme.homegram.util.sns.LegacySignInManager
import com.lovesme.homegram.util.sns.OneTapSignInManager
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class SignInActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySignInBinding
    private val auth: FirebaseAuth = Firebase.auth
    private lateinit var oneTapSignInClient: SignInClient

    private lateinit var legacySignInManagerInstance: LegacySignInManager
    private lateinit var legacySignResultLauncher: ActivityResultLauncher<Intent>
    private lateinit var oneTapSignInManagerInstance: OneTapSignInManager
    private lateinit var signInResultLauncher: ActivityResultLauncher<IntentSenderRequest>
    private lateinit var notificationPermissionLauncher: ActivityResultLauncher<String>

    private val signInViewModel: SignInViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        installSplashScreen()
        binding = ActivitySignInBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initData()
        requestNotificationPermission()
        if (auth.currentUser != null) {
            gotoLoginSuccessActivity()
        }
    }

    private fun initData() {
        initLauncher()
        initManager()

        binding.signInGoogleBtn.setOnClickListener {
            try {
                oneTapSignInManagerInstance.loginGoogle()
            } catch (e: IntentSender.SendIntentException) {
                Snackbar.make(
                    binding.root,
                    getString(R.string.signin_google_fail),
                    Snackbar.LENGTH_SHORT
                ).show()
            }
        }
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                signInViewModel.uiState.collect { state ->
                    when (state) {
                        is UiState.Success -> {
                            gotoUserPreference()
                        }
                        is UiState.Error -> {
                            Snackbar.make(
                                binding.root,
                                getString(R.string.signin_google_fail),
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

    private fun initManager() {
        legacySignInManagerInstance = LegacySignInManager(this, legacySignResultLauncher)
        oneTapSignInManagerInstance = OneTapSignInManager(
            oneTapSignInClient,
            signInResultLauncher,
            legacySignInManagerInstance
        )
    }

    private fun initLauncher() {
        oneTapSignInClient = Identity.getSignInClient(this)

        legacySignResultLauncher = registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ) { result ->
            if (result.resultCode == RESULT_OK) {
                val task: Task<GoogleSignInAccount> =
                    GoogleSignIn.getSignedInAccountFromIntent(result.data)
                val loginResult = legacySignInManagerInstance.handleLegacySignInResult(task)
                if (loginResult is Result.Error) {
                    Snackbar.make(
                        binding.root,
                        getString(R.string.signin_google_fail),
                        Snackbar.LENGTH_SHORT
                    )
                        .show()
                }
            } else {
                Snackbar.make(
                    binding.root,
                    getString(R.string.signin_google_fail),
                    Snackbar.LENGTH_SHORT
                )
                    .show()
            }
        }

        signInResultLauncher =
            registerForActivityResult(
                ActivityResultContracts.StartIntentSenderForResult(),
            ) { result ->
                if (result.resultCode == RESULT_OK) {
                    val credential =
                        oneTapSignInClient.getSignInCredentialFromIntent(result.data)
                    try {
                        credential.googleIdToken?.let {
                            oneTapSignInManagerInstance.handleOneTapSignInResult(credential)
                        }
                    } catch (e: ApiException) {
                        Snackbar.make(
                            binding.root,
                            getString(R.string.signin_google_fail),
                            Snackbar.LENGTH_SHORT
                        ).show()
                    }
                } else {
                    Snackbar.make(
                        binding.root,
                        getString(R.string.signin_google_fail),
                        Snackbar.LENGTH_SHORT
                    )
                        .show()
                }
            }

        notificationPermissionLauncher =
            registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
                if (isGranted.not()) {
                    Snackbar.make(
                        binding.root,
                        getString(R.string.permission_notification_denied),
                        Snackbar.LENGTH_SHORT
                    ).show()
                }
            }
    }

    private fun gotoLoginSuccessActivity() {
        lifecycleScope.launch() {
            val result = signInViewModel.existUserName()
            if (result is Result.Success) {
                if (result.data) {
                    gotoHome()
                } else {
                    gotoUserPreference()
                }
            }
        }
    }

    private fun gotoHome() {
        handleDynamicLinks()
        startActivity(Intent(this, MainActivity::class.java))
        finish()
    }

    private fun gotoUserPreference() {
        startActivity(Intent(this, UserPreferenceActivity::class.java))
        finish()
    }

    private fun handleDynamicLinks() {
        Firebase.dynamicLinks
            .getDynamicLink(intent)
            .addOnSuccessListener(this) { linkData ->
                if (linkData != null && linkData.link != null) {
                    val groupId = linkData.link?.getQueryParameter("code")
                    if (groupId != null) {
                        signInViewModel.joinToInvitedGroup(groupId)
                    }
                }
            }
    }

    private fun requestNotificationPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            val permissionCheck = PermissionChecker.checkCallingOrSelfPermission(
                this,
                Manifest.permission.POST_NOTIFICATIONS
            )
            if (permissionCheck != PermissionChecker.PERMISSION_GRANTED) {
                notificationPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
            }
        }
    }
}