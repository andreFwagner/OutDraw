package com.example.android.outdraw.network

import com.example.android.outdraw.database.ArtPieceData

/**
 * Data Class to load ARTPieces from the API
 */

data class ArtPiece(

    val title: String,

    val primaryImage: String

)

data class AllArt(
    val total: Int,

    val objectIDs: List<Int>
)

fun ArtPiece.asDatabaseModel(): ArtPieceData {
    return ArtPieceData(
        id = 1,
        title = this.title,
        primaryImage = this.primaryImage
    )
}
