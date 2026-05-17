package com.shenouda.stride.common.domain.model

data class User(
    val uid: String,
    val email:String,
    val displayName:String?,
    val photoUrl: String?,
    val isEmailVerified: Boolean,
    val role: Role,
    )
