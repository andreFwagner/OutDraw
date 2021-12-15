package com.example.android.outdraw.database

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface DatabaseDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun savePainting(painting: Painting)

    @Query("SELECT * from Painting ORDER BY id DESC")
    fun getAll(): List<Painting>

    @Delete
    fun delete(painting: Painting)

    @Query("SELECT * FROM Painting where id = :paintingId")
    fun getPaintingById(paintingId: Long): Painting?
}
