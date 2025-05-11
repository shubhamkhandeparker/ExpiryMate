package com.example.tempexpirymate.data

import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "expiry_items")
data class ExpiryEntity(
    @PrimaryKey(autoGenerate = true)
    val id:Long=0,

    val name: String,
    val expiryEpochMs:Long,
    val statusColorInt: Int
)