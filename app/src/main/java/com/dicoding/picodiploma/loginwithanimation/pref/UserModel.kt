package com.dicoding.picodiploma.loginwithanimation.pref

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class UserModel (
    var userId: String? = null,
    var name: String? = null,
    var token: String? = null
) : Parcelable