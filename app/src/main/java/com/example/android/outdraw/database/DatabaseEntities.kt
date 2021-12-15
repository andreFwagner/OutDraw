package com.example.android.outdraw.database

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize
import java.util.*

@Entity
@Parcelize
data class Painting constructor(
    @PrimaryKey(autoGenerate = true)
    val id: Long,

    val date: String,

    val image: String
) : Parcelable
