package com.example.finalproject.presentation.home.profile

import androidx.compose.runtime.Immutable

@Immutable
data class AccountInfo(
    val infoType: InfoType,
    val titleId: Int,
    val imageId: Int
)

@Immutable
enum class InfoType {
    NAME,
    MOBILE,
    ADDRESS,
    BIRTHDATE
}
