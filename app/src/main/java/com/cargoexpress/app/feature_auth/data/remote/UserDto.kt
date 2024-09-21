package com.cargoexpress.app.feature_auth.data.remote

import com.cargoexpress.app.feature_auth.domain.model.User
import com.google.gson.annotations.SerializedName

data class UserDto(
    @SerializedName("id")
    val id: Int,
    @SerializedName("username")
    val username: String,
    @SerializedName("password")
    val password: String
)

fun UserDto.toUser() = User(
    username = username
)