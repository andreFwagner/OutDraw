package at.andrewagner.outdraw.database

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

/**
 * Entities for the Room-Database
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
