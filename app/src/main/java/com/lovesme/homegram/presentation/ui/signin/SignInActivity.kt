package com.lovesme.homegram.presentation.ui.signin

import android.content.Intent
import android.content.IntentSender
import android.os.Bundle
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.IntentSenderRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.auth.api.identity.Identity
import com.google.android.gms.auth.api.identity.SignInClient
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.lovesme.homegram.databinding.ActivitySignInBinding
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.lovesme.homegram.R
import com.lovesme.homegram.presentation.ui.main.MainActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import com.lovesme.homegram.data.model.Result
import com.lovesme.homegram.presentation.ui.setting.UserPreferenceActivity
import com.lovesme.homegram.presentation.ui.viewmodel.SignInViewModel
import com.lovesme.homegram.util.sns.LegacySignInManager
import com.lovesme.homegram.util.sns.OneTapSignInManager
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SignInActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySignInBinding
    private val auth: FirebaseAuth = Firebase.auth
    private lateinit var oneTapSignInClient: SignInClient

    private lateinit var legacySignInManagerInstance: LegacySignInManager
    private lateinit var legacySignResultLauncher: ActivityResultLauncher<Intent>
    private lateinit var oneTapSignInManagerInstance: OneTapSignInManager
    private lateinit var signInResultLauncher: ActivityResultLauncher<IntentSenderRequest>

    private val signInViewModel: SignInViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignInBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initData()
        if (auth.currentUser != null) {
            gotoHome()
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
                if (loginResult is Result.Success) {
                    saveLogInUserInfo()
                } else if (loginResult is Result.Error) {
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
                            saveLogInUserInfo()
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

    private fun saveLogInUserInfo() {
        CoroutineScope(Dispatchers.IO).launch {
            signInViewModel.saveLogInUserInfo()
        }
        gotoUserPreference()
    }

    private fun handleDynamicLinks() {
        intent.getStringExtra("groupId")?.let { groupId ->
            CoroutineScope(Dispatchers.IO).launch {
                signInViewModel.joinToInvitedGroup(groupId)
            }
        }
    }
}