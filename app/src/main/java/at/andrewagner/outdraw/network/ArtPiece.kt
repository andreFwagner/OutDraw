package at.andrewagner.outdraw.network

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
