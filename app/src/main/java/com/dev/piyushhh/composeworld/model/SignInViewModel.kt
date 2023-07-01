package com.dev.piyushhh.composeworld.model

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class SignInViewModel() : ViewModel(){
    private val _state = MutableStateFlow(SignInModel())
    val state = _state.asStateFlow()

    fun onSignInResult(result: SignInResultModel){
        _state.update {
            it.copy(
                isSignInSuccessful = result.data != null,
                signInError = result.errorMessage
            )
        }
    }

    fun resetState(){
        _state.update {
            SignInModel()
        }
    }
}
