package com.dev.piyushhh.composeworld.home

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.ShapeDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.dev.piyushhh.composeworld.R
import com.dev.piyushhh.composeworld.model.SignInModel

@Composable
fun ComposeHome(
    state : SignInModel,
    onSingleClick: () -> Unit
){
    Surface(modifier = Modifier
        .fillMaxSize()
    ) {
        val context = LocalContext.current
        LaunchedEffect(key1 = state.signInError){
            state.signInError?.let {
                error -> Toast.makeText(context,error,Toast.LENGTH_SHORT).show()
            }
        }
        val backgroundImage = painterResource(id = R.drawable.bg_home_image)
        Box(
            modifier = Modifier
                .padding(top = 72.dp),
            contentAlignment = Alignment.TopCenter
        ) {
            Image(
                modifier = Modifier
                    .size(128.dp),
                painter = backgroundImage,
                contentDescription = "Background Icon",
                colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.primary)
            )
        }
        Column(
            modifier = Modifier
                .padding(bottom = 52.dp),
            verticalArrangement = Arrangement.Bottom,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            OutlinedButton(
                modifier = Modifier
                    .width(212.dp)
                    .padding(bottom = 2.dp),
                shape = ShapeDefaults.Small,
                onClick = onSingleClick
            ) {
                Icon(
                    painterResource(id = R.drawable.google),
                    contentDescription = "google Sign In",
                    modifier = Modifier
                        .size(ButtonDefaults.IconSize)
                )
                Spacer(
                    modifier = Modifier
                        .padding(ButtonDefaults.IconSpacing)
                )
                Text(
                    "Sign in with Google"
                )
            }
            Button(
                modifier = Modifier
                    .width(212.dp)
                    .padding(top = 2.dp),
                shape = ShapeDefaults.Small,
                elevation = ButtonDefaults.buttonElevation(
                    defaultElevation = 5.dp,
                    pressedElevation = 8.dp,
                    disabledElevation = 0.dp
                ), onClick = {

                }) {
                Icon(
                    imageVector = Icons.Default.Email,
                    contentDescription = "Sign up with Email",
                    modifier = Modifier
                        .size(ButtonDefaults.IconSize)
                )
                Spacer(
                    modifier = Modifier
                        .padding(ButtonDefaults.IconSpacing)
                )
                Text(
                    text = "Sign Up with Email"
                )
            }
        }
    }
}
