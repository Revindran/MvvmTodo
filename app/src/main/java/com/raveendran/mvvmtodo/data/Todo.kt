package com.raveendran.mvvmtodo.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Todo(
    @PrimaryKey val id: Int? = null,
    val title: String,
    val description: String?,
    val isChecked: Boolean
)
