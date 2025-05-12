package com.example.tempexpirymate

import android.app.Application
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.AndroidViewModel
import androidx.room.Room
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.SharingStarted
import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel

import java.time.Instant
import java.time.ZoneId
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.tempexpirymate.data.ExpiryEntity
import com.example.tempexpirymate.data.AppDatabase
import kotlinx.coroutines.launch
import androidx.compose.ui.graphics.toArgb



class ExpiryViewModel(app: Application): AndroidViewModel(app){
    private val db=Room.databaseBuilder(app, AppDatabase::class.java,"expiry.db").build()
    private val dao=db.expiryDao()

    //Expose a compose-friendly Stateflow of your domain model
    @RequiresApi(Build.VERSION_CODES.O)
    val items=dao.getAllFlow().map { list->
        list.map { entity-> ExpiryItem(
            id = entity.id,
            name = entity.name,
            expiryText = Instant.ofEpochMilli(entity.expiryEpochMs)
                .atZone(ZoneId.systemDefault())
                .toLocalDate()
                .format(DateTimeFormatter.ofPattern("MMM d, yyyy")),
            statusColor = Color(entity.statusColorInt)
        )
        }
    }
        .stateIn(
                scope = viewModelScope,
                started= SharingStarted.WhileSubscribed(5_000),
                initialValue = emptyList()

                )
    //Call this to insert new row
    @RequiresApi(Build.VERSION_CODES.O)
    fun addItem(name: String, date: LocalDate, color: Color)=viewModelScope.launch {
        dao.insert(
            ExpiryEntity(
                name        =name,
                expiryEpochMs = date.atStartOfDay(ZoneId.systemDefault()).toInstant().toEpochMilli(),
                statusColorInt = color.toArgb()
            )
        )
    }
    //Delete Support
    fun deleteItem(id:Long)=viewModelScope.launch {
        dao.getById(id)?.let {dao.delete(it)}
    }

    /**Reset the expiry date of[item] to *today*.*/
    @RequiresApi(Build.VERSION_CODES.O)
    fun resetItemDate(id:Long)=viewModelScope.launch {
        val todayEpoch= LocalDate.now().atStartOfDay(ZoneId.systemDefault())
            .toInstant().toEpochMilli()
        dao.updateExpiryDate(id,todayEpoch)
    }
}

