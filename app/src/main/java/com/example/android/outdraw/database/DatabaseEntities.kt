package com.example.android.outdraw.database

import android.graphics.Bitmap
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

@Entity
data class Painting constructor(
    @PrimaryKey(autoGenerate = true)
    val id: Long,

    val date: String,

    val image: String
)
