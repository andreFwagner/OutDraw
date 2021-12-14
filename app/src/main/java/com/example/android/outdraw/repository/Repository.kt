package com.example.android.outdraw.repository

import androidx.lifecycle.LiveData
import com.example.android.outdraw.database.DatabaseDao
import com.example.android.outdraw.database.Painting
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.withContext

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

    suspend fun getPainting(id: Long): Painting? {
        return withContext(ioDispatcher) {
            databaseDao.getPaintingById(id)
        }
    }
}
