package com.dev.piyushhh.composeworld.model

data class SignInResultModel(
    val data: com.dev.piyushhh.composeworld.model.UserData?,
    val errorMessage: String?
)

data class UserData(
    val userId:String,
    val userName:String?,
    val userProfilePictureUrl: String?
)