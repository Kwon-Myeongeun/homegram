package com.lovesme.homegram.util.sns

import android.content.Context
import android.content.Intent
import androidx.activity.result.ActivityResultLauncher
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.lovesme.homegram.BuildConfig
import com.lovesme.homegram.R
import com.lovesme.homegram.data.model.Result

class LegacySignInManager(
    private val context: Context,
    private val activityLauncher: ActivityResultLauncher<Intent>
) {
    private val auth: FirebaseAuth = Firebase.auth
    val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
        .requestIdToken(BuildConfig.GOOGLE_CLIENT_ID)
        .requestEmail()
        .build()

    private val legacySignInClient = GoogleSignIn.getClient(context, gso)

    fun loginGoogle() {
        val signIntent: Intent = legacySignInClient.signInIntent
        activityLauncher.launch(signIntent)
    }

    fun handleLegacySignInResult(completedTask: Task<GoogleSignInAccount>): Result<Any> {
        return try {
            val credential = completedTask.getResult(ApiException::class.java)
            if (credential.idToken != null) {
                val firebaseCredential =
                    GoogleAuthProvider.getCredential(credential.idToken, null)
                auth.signInWithCredential(firebaseCredential)
                    .addOnFailureListener {
                        Result.Error(Exception(context.getString(R.string.signin_google_fail)))
                    }
            }
            Result.Success(Unit)
        } catch (e: ApiException) {
            Result.Error(e)
        }
    }
}