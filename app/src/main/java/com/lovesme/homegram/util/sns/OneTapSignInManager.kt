package com.lovesme.homegram.util.sns

import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.IntentSenderRequest
import com.google.android.gms.auth.api.identity.BeginSignInRequest
import com.google.android.gms.auth.api.identity.SignInClient
import com.google.android.gms.auth.api.identity.SignInCredential
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.lovesme.homegram.BuildConfig

class OneTapSignInManager(
    private val client: SignInClient,
    private val activityLauncher: ActivityResultLauncher<IntentSenderRequest>,
    private val legacySignInManager: LegacySignInManager
) {
    private val auth: FirebaseAuth = Firebase.auth
    private val signInRequest = BeginSignInRequest.builder()
        .setGoogleIdTokenRequestOptions(
            BeginSignInRequest.GoogleIdTokenRequestOptions.builder()
                .setSupported(true)
                .setServerClientId(BuildConfig.GOOGLE_CLIENT_ID)
                .setFilterByAuthorizedAccounts(false)
                .build()
        )
        .setAutoSelectEnabled(true)
        .build()

    fun loginGoogle() {
        client.beginSignIn(signInRequest)
            .addOnSuccessListener { result ->
                activityLauncher.launch(
                    IntentSenderRequest.Builder(result.pendingIntent.intentSender)
                        .build()
                )
            }
            .addOnFailureListener {
                legacySignInManager.loginGoogle()
            }
    }

    fun handleOneTapSignInResult(credential: SignInCredential) {
        val firebaseCredential =
            GoogleAuthProvider.getCredential(credential.googleIdToken, null)

        auth.signInWithCredential(firebaseCredential)
    }
}