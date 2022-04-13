package com.example.perludilindungi.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [BookmarkDB::class], version = 1)
abstract class BookmarkRoom : RoomDatabase() {
    abstract fun bookmarkDAO(): BookmarkDAO

    companion object {
        @Volatile
        private var INSTANCE: BookmarkRoom? = null

        @JvmStatic
        fun getDatabase(context: Context): BookmarkRoom {
            if (INSTANCE == null) {
                synchronized(BookmarkRoom::class.java) {
                    INSTANCE = Room.databaseBuilder(
                        context.applicationContext,
                        BookmarkRoom::class.java, "bookmark_db"
                    ).allowMainThreadQueries()
                        .fallbackToDestructiveMigration().build()
                }

            }
            return INSTANCE as BookmarkRoom
        }
    }
}