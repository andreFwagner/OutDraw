package com.example.android.outdraw.database

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize

/**
 * Entities for the Roomdatabase
 */

@Entity
@Parcelize
data class Painting constructor(
    @PrimaryKey(autoGenerate = true)
    val id: Long,

    val date: String,

    val image: String
) : Parcelable

@Entity
data class ArtPieceData constructor(

    @PrimaryKey
    val id: Long,

    val title: String,

    val primaryImage: String

)
