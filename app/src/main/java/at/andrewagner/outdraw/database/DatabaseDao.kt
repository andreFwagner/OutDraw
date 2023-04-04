package at.andrewagner.outdraw.database

import androidx.room.* // ktlint-disable no-wildcard-imports

/**
 * Dao for the Room-Database
 */

@Dao
interface DatabaseDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun savePainting(painting: Painting)

    @Query("SELECT * from Painting ORDER BY id DESC")
    fun getAll(): List<Painting>

    @Delete
    fun delete(painting: Painting)

//    @Query("SELECT * FROM Painting where id = :paintingId")
//    fun getPaintingById(paintingId: Long): Painting?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun saveArtPiece(artPieceData: ArtPieceData)

    @Query("SELECT * FROM ArtPieceData where id = 1")
    fun getArtPiece(): ArtPieceData?
}
