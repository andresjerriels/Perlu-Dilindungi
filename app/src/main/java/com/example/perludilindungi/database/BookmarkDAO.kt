package com.example.perludilindungi.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy.IGNORE
import androidx.room.Query

@Dao
interface BookmarkDAO {
    @Query("SELECT * from BookmarkDB ORDER BY id DESC ")
    fun getAllBookmarks(): LiveData<List<BookmarkDB>>

    @Query("SELECT EXISTS (SELECT * from BookmarkDB where kode =:kode)")
    fun checkIfFaskesBookmarked(kode: String): Boolean

    @Insert(onConflict = IGNORE)
    fun insert(data: BookmarkDB)

    @Query("DELETE from BOOKMARKDB where kode = :kode")
    fun delete(kode: String)
}