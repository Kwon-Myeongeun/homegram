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
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.lovesme.homegram.BuildConfig
import com.lovesme.homegram.databinding.ActivitySignInBinding
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.lovesme.homegram.R
import com.lovesme.homegram.data.model.User
import com.lovesme.homegram.data.repository.GroupRepository
import com.lovesme.homegram.data.repository.UserPreferencesRepository
import com.lovesme.homegram.presentation.ui.main.MainActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import com.lovesme.homegram.data.model.Result

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
                    val credential =
                        oneTapSignInClient.getSignInCredentialFromIntent(result.data)
                    try {
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

        val legacySignResultLauncher = registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ) { result ->
            if (result.resultCode == RESULT_OK) {
                val task: Task<GoogleSignInAccount> =
                    GoogleSignIn.getSignedInAccountFromIntent(result.data)
                try {
                    handleLegacySignInResult(task)
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
            initGoogleLogin(signInResultLauncher, legacySignResultLauncher)
        }
    }

    private fun initGoogleLogin(
        oneTapSignInResultLauncher: ActivityResultLauncher<IntentSenderRequest>,
        legacySignResultLauncher: ActivityResultLauncher<Intent>
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
                initLegacyLogin(legacySignResultLauncher)
            }
    }

    private fun initLegacyLogin(legacySignResultLauncher: ActivityResultLauncher<Intent>) {
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(BuildConfig.GOOGLE_CLIENT_ID)
            .requestEmail()
            .build()

        val legacySignInClient = GoogleSignIn.getClient(this, gso)
        val signIntent: Intent = legacySignInClient.signInIntent

        legacySignResultLauncher.launch(signIntent)
    }

    private fun handleOneTapSignInResult(credential: SignInCredential) {
        val firebaseCredential = GoogleAuthProvider.getCredential(credential.googleIdToken, null)

        auth.signInWithCredential(firebaseCredential)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    registerUser()
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

    private fun handleLegacySignInResult(completedTask: Task<GoogleSignInAccount>) {
        val credential = completedTask.getResult(ApiException::class.java)

        if (credential.idToken != null) {
            val firebaseCredential =
                GoogleAuthProvider.getCredential(credential.idToken, null)
            auth.signInWithCredential(firebaseCredential)
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        registerUser()
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
    }

    private fun gotoHome() {
        startActivity(Intent(this, MainActivity::class.java))
        finish()
    }

    private fun registerUser() {
        val email = auth.currentUser?.email ?: ""
        CoroutineScope(Dispatchers.IO).launch {
            val result = GroupRepository().addMember()
            if (result is Result.Success) {
                UserPreferencesRepository().addUser(User(email, "", "", result.data))
            }
        }
    }
}