package com.example.tempexpirymate.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow


@Dao
interface ExpiryDao {
    @Query("SELECT*FROM expiry_items ORDER BY expiryEpochMs")
    fun getAllFlow():Flow<List<ExpiryEntity>>

    @Insert
    suspend fun insert(entity: ExpiryEntity)

    @Delete
    suspend fun delete(entity: ExpiryEntity)

    @Query("UPDATE expiry_items SET expiryEpochMs = :newEpoch WHERE id = :id")
    suspend fun updateExpiryDate(id:Long,newEpoch:Long)

    @Query("SELECT* FROM expiry_items WHERE id = :id")
    suspend fun getById(id:Long): ExpiryEntity ?
}