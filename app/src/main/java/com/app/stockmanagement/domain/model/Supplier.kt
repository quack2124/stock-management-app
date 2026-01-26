package com.app.stockmanagement.domain.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Supplier(
    val id: Long,
    val name: String,
    val contactPerson: String,
    val phone: String,
    val email: String,
    val address: String
) : Parcelable
