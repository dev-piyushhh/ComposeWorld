package com.dev.piyushhh.composeworld.gsign

import android.content.Context
import android.content.Intent
import android.content.IntentSender
import com.dev.piyushhh.composeworld.R
import com.dev.piyushhh.composeworld.model.SignInResultModel
import com.dev.piyushhh.composeworld.model.UserData
import com.google.android.gms.auth.api.identity.BeginSignInRequest
import com.google.android.gms.auth.api.identity.BeginSignInRequest.GoogleIdTokenRequestOptions
import com.google.android.gms.auth.api.identity.SignInClient
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.tasks.await
import java.util.concurrent.CancellationException

class GoogleAuthClient(
    private val context: Context,
    private val oneTapClient: SignInClient
){
    private val auth = Firebase.auth

    suspend fun signIn() : IntentSender?{
        val result = try{
            oneTapClient.beginSignIn(
                buildSignInRequest()
            ).await()
        }catch(e : Exception){
            e.printStackTrace()
            if( e is CancellationException) throw e
            null
        }
        return result?.pendingIntent?.intentSender
    }
    suspend fun signInWithIntent(intent: Intent) : SignInResultModel{
        val credential = oneTapClient.getSignInCredentialFromIntent(intent)
        val googleIdToken = credential.googleIdToken
        val googleCredential = GoogleAuthProvider.getCredential(googleIdToken,null)
        return try {
            val user = auth.signInWithCredential(googleCredential)
                .await()
                .user
            SignInResultModel(
                data = user?.run {
                    UserData(
                        userId = uid,
                        userName = displayName,
                        userProfilePictureUrl = photoUrl?.toString()
                    )
                },
                errorMessage = null
            )
        }catch (e : Exception){
            e.printStackTrace()
            if( e is CancellationException) throw  e
            SignInResultModel(
                data = null,
                errorMessage = e.message
            )
        }
    }
    suspend fun signOut(){
        try {
            oneTapClient.signOut().await()
            auth.signOut()
        }catch (e : Exception){
            e.printStackTrace()
            if(e  is CancellationException) throw e
        }
    }

    fun getSignInWithUser() : UserData? = auth.currentUser?.run {
        UserData(
            userId = uid,
            userName = displayName,
            userProfilePictureUrl = photoUrl?.toString()
        )
    }

    private fun buildSignInRequest() : BeginSignInRequest{
        return BeginSignInRequest.Builder()
            .setGoogleIdTokenRequestOptions(
                GoogleIdTokenRequestOptions.builder()
                    .setSupported(true)
                    .setFilterByAuthorizedAccounts(false)
                    .setServerClientId(context.getString(R.string.web_client_id))
                    .build()
            )
            .setAutoSelectEnabled(true)
            .build()
    }
}