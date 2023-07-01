package com.dev.piyushhh.composeworld

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.IntentSenderRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.dev.piyushhh.composeworld.gsign.GoogleAuthClient
import com.dev.piyushhh.composeworld.home.ComposeHome
import com.dev.piyushhh.composeworld.home.UserHomeMain
import com.dev.piyushhh.composeworld.model.SignInViewModel
import com.dev.piyushhh.composeworld.ui.theme.ComposeWorldTheme
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    private  val googleAuthClient by lazy {
        GoogleAuthClient(
            context = applicationContext,
            oneTapClient = com.google.android.gms.auth.api.identity.Identity.getSignInClient(applicationContext)
        )
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ComposeWorldTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val navController = rememberNavController()

                    NavHost(navController = navController, startDestination = "compose_home"){
                        composable("compose_home"){
                            val viewModel = viewModel<SignInViewModel>()
                            val state by viewModel.state.collectAsStateWithLifecycle()
                            
                            LaunchedEffect(key1 = Unit){
                                if(googleAuthClient.getSignInWithUser() != null){
                                    navController.navigate("main_activity")
                                }
                            }

                            val launcher = rememberLauncherForActivityResult(
                                contract = ActivityResultContracts.StartIntentSenderForResult(),
                                onResult = {
                                    result ->
                                    if(result.resultCode == RESULT_OK){
                                        lifecycleScope.launch {
                                            val signInResult = googleAuthClient.signInWithIntent(
                                                intent = result.data ?: return@launch
                                            )
                                            viewModel.onSignInResult(signInResult)
                                        }
                                    }
                                }
                            )
                            
                            LaunchedEffect(key1 = state.isSignInSuccessful ){
                                if(state.isSignInSuccessful){
                                    navController.navigate("main_activity")
                                    viewModel.resetState()
                                }
                            }

                            ComposeHome(
                                state = state,
                                onSingleClick = {
                                    lifecycleScope.launch {
                                        val signInIntentSender = googleAuthClient.signIn()
                                        launcher.launch(
                                            IntentSenderRequest.Builder(
                                                signInIntentSender ?: return@launch
                                            ).build()
                                        )
                                    }
                                }
                            )
                        }

                        composable("main_activity"){
                            UserHomeMain(
                                userData = googleAuthClient.getSignInWithUser()
                            ){
                                lifecycleScope.launch {
                                    googleAuthClient.signOut()
                                    navController.popBackStack()
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
