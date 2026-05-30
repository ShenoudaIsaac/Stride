package com.shenouda.stride.feature_auth.data.model

import com.shenouda.stride.common.core.navigation.AppGraph
import com.shenouda.stride.common.domain.model.Role
import com.shenouda.stride.common.domain.model.User

data class AuthUserDto(
    val uid: String,
    val email: String,
    val displayName: String?,
    val photoUrl: String?,
    val isEmailVerified: Boolean,
    val role: String
)

fun AuthUserDto.toDomain() = User(
    uid=uid,
    email=email,
    displayName=displayName,
    photoUrl=photoUrl,
    isEmailVerified=isEmailVerified,
    role = when(role.uppercase()){
        "TEACHER" -> Role.TEACHER
        "STUDENT" -> Role.STUDENT
        else ->Role.NONE
    }

)

