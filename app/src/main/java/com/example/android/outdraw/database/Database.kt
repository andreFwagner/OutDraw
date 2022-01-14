package com.example.android.outdraw.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

/**
 * Room-Database to save Paintings from the App and ArtPieces from the API
 */
@Database(entities = [Painting::class, ArtPieceData::class], version = 1)
abstract class Database : RoomDatabase() {

    abstract fun databaseDao(): DatabaseDao
}

object LocalDB {

    /**
     * static method that creates a database class and returns the DAO of it
     */
    fun createDatabaseDao(context: Context): DatabaseDao {
        return Room.databaseBuilder(
            context.applicationContext,
            com.example.android.outdraw.database.Database::class.java, "paintings.db"
        ).build().databaseDao()
    }
}
