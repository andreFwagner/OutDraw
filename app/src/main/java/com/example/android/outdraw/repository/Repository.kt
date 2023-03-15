package com.example.android.outdraw.repository

import android.util.Log
import com.example.android.outdraw.database.ArtPieceData
import com.example.android.outdraw.database.DatabaseDao
import com.example.android.outdraw.database.Painting
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File

/**
 * Repository for interaction with the database
 */

class Repository(private val databaseDao: DatabaseDao, private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO) {

    suspend fun loadPaintings(): List<Painting> {
        return withContext(ioDispatcher) {
            databaseDao.getAll()
        }
    }

    suspend fun savePainting(painting: Painting) =
        withContext(ioDispatcher) {
            databaseDao.savePainting(painting)
        }

    suspend fun deletePainting(painting: Painting) =
        withContext(ioDispatcher) {
            databaseDao.delete(painting)
        }

    suspend fun updateArtPiece() =
        withContext(ioDispatcher) {
            databaseDao.getArtPiece()
        }

    suspend fun saveArtPiece(artPieceData: ArtPieceData) {
        return withContext(ioDispatcher) {
            try {
                File(updateArtPiece()!!.primaryImage).delete()
            } catch (e: Exception) {
                Log.e("Repository", "failed deleting old ArtPiece: $e")
            }
            databaseDao.saveArtPiece(artPieceData)
        }
    }
}
