package com.lovesme.homegram.presentation.ui.signin

import android.content.Intent
import android.content.IntentSender
import android.os.Bundle
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.IntentSenderRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.auth.api.identity.BeginSignInRequest
import com.google.android.gms.auth.api.identity.Identity
import com.google.android.gms.auth.api.identity.SignInClient
import com.google.android.gms.auth.api.identity.SignInCredential
import com.lovesme.homegram.BuildConfig
import com.lovesme.homegram.databinding.ActivitySignInBinding
import com.google.android.gms.common.api.ApiException
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.lovesme.homegram.R
import com.lovesme.homegram.presentation.ui.home.HomeActivity

class SignInActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySignInBinding
    private val auth: FirebaseAuth = Firebase.auth
    private lateinit var oneTapSignInClient: SignInClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignInBinding.inflate(layoutInflater)
        setContentView(binding.root)
        if (auth.currentUser == null) {
            initialize()
        } else {
            gotoHome()
        }
    }

    private fun initialize() {
        oneTapSignInClient = Identity.getSignInClient(this)

        val signInResultLauncher =
            registerForActivityResult(
                ActivityResultContracts.StartIntentSenderForResult(),
            ) { result ->
                if (result.resultCode == RESULT_OK) {
                    try {
                        val credential =
                            oneTapSignInClient.getSignInCredentialFromIntent(result.data)
                        credential.googleIdToken?.let {
                            handleOneTapSignInResult(credential)
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

        binding.signInGoogleBtn.setOnClickListener {
            initGoogleLogin(signInResultLauncher)
        }
    }

    private fun initGoogleLogin(
        oneTapSignInResultLauncher: ActivityResultLauncher<IntentSenderRequest>
    ) {
        val signInRequest = BeginSignInRequest.builder()
            .setGoogleIdTokenRequestOptions(
                BeginSignInRequest.GoogleIdTokenRequestOptions.builder()
                    .setSupported(true)
                    .setServerClientId(BuildConfig.GOOGLE_CLIENT_ID)
                    .setFilterByAuthorizedAccounts(false)
                    .build()
            )
            .setAutoSelectEnabled(true)
            .build()

        oneTapSignInClient.beginSignIn(signInRequest)
            .addOnSuccessListener(this) { result ->
                try {
                    oneTapSignInResultLauncher.launch(
                        IntentSenderRequest.Builder(result.pendingIntent.intentSender)
                            .build()
                    )
                } catch (e: IntentSender.SendIntentException) {
                    Snackbar.make(
                        binding.root,
                        getString(R.string.signin_google_fail),
                        Snackbar.LENGTH_SHORT
                    ).show()
                }
            }
            .addOnFailureListener(this) {
                TODO("Legacy Sign In")
            }
    }

    private fun handleOneTapSignInResult(credential: SignInCredential) {
        val firebaseCredential = GoogleAuthProvider.getCredential(credential.googleIdToken, null)

        auth.signInWithCredential(firebaseCredential)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    gotoHome()
                } else {
                    Snackbar.make(
                        binding.root,
                        getString(R.string.signin_google_fail),
                        Snackbar.LENGTH_SHORT
                    ).show()
                }
            }
    }

    private fun gotoHome() {
        startActivity(Intent(this, HomeActivity::class.java))
        finish()
    }
}