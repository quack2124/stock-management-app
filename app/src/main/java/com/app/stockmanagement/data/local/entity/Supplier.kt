package com.app.stockmanagement.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "suppliers")
data class Supplier(
    @PrimaryKey(autoGenerate = true)
    val id: Long,
    val name: String,
    val contactPerson: String,
    val phone: String,
    val email: String,
    val address: String
)
