package com.example.tempexpirymate.data

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(
    entities = [ExpiryEntity::class],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase: RoomDatabase(){
    abstract fun expiryDao(): ExpiryDao

}