package com.raveendran.mvvmtodo.data

import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey

@Entity
data class Todo(
    val title: String,
    val description: String?,
    val isChecked: Boolean,
    @PrimaryKey val id: Int? = null
)
